package dev.mvc.board;

import java.util.ArrayList;

import org.apache.catalina.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.account.AccountProcInter;
import dev.mvc.crudcate.CrudcateProcInter;
import dev.mvc.crudcate.CrudcateVO;
import dev.mvc.crudcate.CrudcateVOMenu;
import dev.mvc.manager.ManagerProc;
import dev.mvc.manager.ManagerProcInter;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping(value = "/board")
@Controller
public class BoardCont {
  @Autowired
  @Qualifier("dev.mvc.crudcate.CrudcateProc")
  private CrudcateProcInter crudcateProc;

  @Autowired
  @Qualifier("dev.mvc.account.AccountProc")
  private AccountProcInter accountProcInter;

  @Autowired
  @Qualifier("dev.mvc.manager.ManagerProc")
  private ManagerProcInter managerProc;
  
  @Autowired
  @Qualifier("dev.mvc.board.BoardProc")
  private BoardProcInter boardProc;

  public BoardCont() {
    System.out.println("-> BoardCont created.");
  }

    /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * @return
   */
  @GetMapping(value = "/msg")
  public String msg(Model model, String url) {
    ArrayList<CrudcateVOMenu> menu = this.crudcateProc.menu();
    model.addAttribute("menu", menu);

    return url;
  }

  /**
   * Create 폼
   * http://localhost:9093/board/create?crudcateno=1
   * http://localhost:9093/board/create?crudcateno=4
   * @param model
   * @param boardVO
   * @param crudcateno
   * @return
   */
  @GetMapping(value = "/create")
  public String create(Model model, BoardVO boardVO, int crudcateno) {

    ArrayList<CrudcateVOMenu> menu = this.crudcateProc.menu();
    model.addAttribute("menu", menu);

    CrudcateVO crudcateVO = this.crudcateProc.read(crudcateno); // create.jsp에 카테고리 정보를 출력하기위한 목적
    model.addAttribute("crudcateVO", crudcateVO);

    return "board/create"; // /templates/contents/create.html
  }

  /**
   * Create 폼 처리
   * @param request
   * @param session
   * @param model
   * @param boardVO
   * @param ra
   * @return
   */
  @PostMapping(value = "/create")
  public String create_process(HttpServletRequest request, HttpSession session, Model model, BoardVO boardVO, RedirectAttributes ra) {
      
    if (this.managerProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      // ------------------------------------------------------------------------------
      // 파일 전송 코드 시작
      // ------------------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image
      String file1saved = ""; // 저장된 파일명, image
      String thumb1 = ""; // preview image

      String upDir = Board.getUploadDir(); // 파일을 업로드할 폴더 준비
      System.out.println("-> upDir: " + upDir);

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = boardVO.getBphotoNF();

      file1 = mf.getOriginalFilename(); // 원본 파일명 산출, 01.jpg
      System.out.println("-> 원본 파일명 산출 Bphoto: " + file1);

      long size1 = mf.getSize(); // 파일 크기
      if (size1 > 0) { // 파일 크기 체크, 파일을 올리는 경우
        if (Tool.checkUploadFile(file1) == true) { // 업로드 가능한 파일인지 검사
          // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg, spring_2.jpg...
          file1saved = Upload.saveFileSpring(mf, upDir);

          if (Tool.isImage(file1saved)) { // 이미지인지 검사
            // thumb 이미지 생성후 파일명 리턴됨, width: 200, height: 150
            thumb1 = Tool.preview(upDir, file1saved, 200, 150);
          }

          boardVO.setBphoto(file1); // 순수 원본 파일명
          boardVO.setBphotosaved(file1saved); // 저장된 파일명(파일명 중복 처리)
          boardVO.setBthumb(thumb1); // 원본이미지 축소판
          boardVO.setBsize(size1); // 파일 크기

        } else { // 전송 못하는 파일 형식
          ra.addFlashAttribute("code", "check_upload_file_fail"); // 업로드 할 수 없는 파일
          ra.addFlashAttribute("cnt", 0); // 업로드 실패
          ra.addFlashAttribute("url", "/board/msg"); // msg.html, redirect parameter 적용
          return "redirect:/board/msg"; // Post -> Get - param...
        }
      } else { // 글만 등록하는 경우
        System.out.println("-> 글만 등록");
      }

      // ------------------------------------------------------------------------------
      // 파일 전송 코드 종료
      // ------------------------------------------------------------------------------

      // Call By Reference: 메모리 공유, Hashcode 전달
      

      Integer accountno = (Integer) session.getAttribute("accountno");
      Integer managerno = (Integer) session.getAttribute("managerno");

      // accountno가 null이 아닌 경우에는 intValue()를 호출하여 값을 얻고,
      // null인 경우에는 0을 반환
      int accountnoValue = accountno != null ? accountno.intValue() : 0;
      int managernoValue = managerno != null ? managerno.intValue() : 0;

      boardVO.setAccountno(accountnoValue);
      boardVO.setManagerno(managernoValue);

      int cnt = this.boardProc.create(boardVO);
      if (cnt == 1) {
        ra.addAttribute("curdcateno", boardVO.getCrudcateno()); // controller -> controller: O
        return "redirect:/board/list_by_crudcateno";
      } else {
        ra.addFlashAttribute("code", "create_fail"); // DBMS 등록 실패
        ra.addFlashAttribute("cnt", 0); // 업로드 실패
        ra.addFlashAttribute("url", "/board/msg"); // msg.html, redirect parameter 적용
        return "redirect:/board/msg"; // Post -> Get - param...
      }
  
  }
  return "redirect:/board/login_form_need"; // /member/login_cookie_need.html
}

}
