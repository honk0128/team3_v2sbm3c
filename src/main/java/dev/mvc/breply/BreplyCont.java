package dev.mvc.breply;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.crudcate.CrudcateProcInter;
import dev.mvc.crudcate.CrudcateVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
// import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@RequestMapping("/breply")
@Controller
public class BreplyCont {
  @Autowired
  @Qualifier("dev.mvc.breply.BreplyProc")
  private BreplyProcInter breplyProc;

  @Autowired
  @Qualifier("dev.mvc.crudcate.CrudcateProc")
  private CrudcateProcInter crudcateProc;

  public BreplyCont() {
    System.out.println("-> BreplyCont created.");
  }

  @GetMapping(value="/msg")
  public String msg(Model model, String url){

    return url;
  }

  /**
   * 등록 폼
   * @param model
   * @param breplyVO
   * @return
   */
  @GetMapping("/create")
  public String replycreate(Model model, BreplyVO breplyVO) {
      return "/breply/create";
  }

  /**
   * 등록 처리
   * @param model
   * @param breplyVO
   * @param bindingResult
   * @param ra
   * @param word
   * @param now_page
   * @return
   */
  @PostMapping(value = "/create")
  public String replycreate(Model model, @Valid BreplyVO breplyVO, BindingResult bindingResult,
                            RedirectAttributes ra,
                            HttpSession session,
                            @RequestParam(name="word", defaultValue = "") String word,
                            @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    if (bindingResult.hasErrors()) {
      return "breply/create";
    }

    if (session.getAttribute("accountno") != null || session.getAttribute("managerno") != null) { // 회원 로그인
      // ------------------------------------------------------------------------------
      // 파일 전송 코드 시작
      // ------------------------------------------------------------------------------
      String file1 = "";
      String file1saved = "";
      String thumb1 = "";

      String upDir =  Breply.getUploadDir();
      System.out.println("-> upDir: " + upDir);
      
      MultipartFile mf = breplyVO.getFile1MF();
      
      file1 = mf.getOriginalFilename();
      System.out.println("-> 원본 파일명 산출 file1: " + file1);
      
      long size1 = mf.getSize();
      if (size1 > 0) {
        if (Tool.checkUploadFile(file1) == true) {
          file1saved = Upload.saveFileSpring(mf, upDir); 
          
          if (Tool.isImage(file1saved)) {
            thumb1 = Tool.preview(upDir, file1saved, 200, 150);
          }

          breplyVO.setBreplyimg(file1);
          breplyVO.setBreplysaved(file1saved);
          breplyVO.setBreplythumb(thumb1);
          breplyVO.setBreplysize(size1);

        } else {
          ra.addFlashAttribute("code", "check_upload_file_fail");
          ra.addFlashAttribute("cnt", 0);
          ra.addFlashAttribute("url", "/Breply/msg");
          return "redirect:/Breply/msg";
        }
      } else {
        System.out.println("-> 글만 등록");
      }
      
      // ------------------------------------------------------------------------------
      // 파일 전송 코드 종료
      // ------------------------------------------------------------------------------

      int cnt = this.breplyProc.replycreate(breplyVO);
      System.out.println("-> cnt: " + cnt);

      model.addAttribute("cnt", cnt);
      if(cnt ==1) {
        return "redirect:/breply/list";
      } else {
        model.addAttribute("code", "code");
        return "breply/msg";
      }
    } else {
      return "redirect:/account/login";
    }
  }

  /**
   * 댓글 목록
   * @param model
   * @param breplyVO
   * @return
   */
  @GetMapping(value = "/list")
  public String reply_list(Model model, BreplyVO breplyVO) {
    ArrayList<BreplyVO> list = this.breplyProc.reply_list();
    model.addAttribute("list", list);

    return "breply/list";
  }

  /**
   * 댓글 조회
   */
  @GetMapping(value = "/read")
  public String read(Model model, int breplyno, String word, CrudcateVO crudcateVO,
                     @RequestParam(name="now_page", defaultValue="1") int now_page) {
    model.addAttribute("now_page", now_page);

    BreplyVO breplyVO = this.breplyProc.read(breplyno);

    long size = breplyVO.getBreplysize();
    String size_label = Tool.unit(size);
    breplyVO.setSize_label(size_label);

    word = Tool.checkNull(word).trim();

    model.addAttribute("breplyVO", breplyVO);

    model.addAttribute("crudcateVO", crudcateVO);

    model.addAttribute("word", word);
    
    return "breply/read";
  }
  
  /**
   * 업데이트
   */
  @GetMapping(value = "/update/{breplyno}")
  public String update(Model model, @PathVariable(name="breplyno") Integer breplyno) {

    BreplyVO breplyVO = this.breplyProc.read(breplyno);
    model.addAttribute("breplyVO", breplyVO);

    System.out.println("cont: " + breplyVO.getBreplycont());;

    return "breply/update";
  }

  
  @PostMapping(value="/update")
  public String update_process(Model model, BreplyVO breplyVO, RedirectAttributes ra, HttpSession session) {
    BreplyVO breplyVO_old = breplyProc.read(breplyVO.getBreplyno());
    
    if (session.getAttribute("accountno") != null || session.getAttribute("managerno") != null) { // 회원 로그인
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("breplyno", breplyVO.getBreplyno());
      map.put("breplypasswd", breplyVO.getBreplypasswd());

      int cnt = this.breplyProc.password_check(map);
      System.out.println("cnt: " + cnt);

      if (cnt == 1) { // 비밀번호 확인
        // -------------------------------------------------------------------
        // 파일 삭제 시작
        // -------------------------------------------------------------------
        String file1saved = breplyVO_old.getBreplysaved();  // 실제 저장된 파일명
        String thumb1 = breplyVO_old.getBreplythumb();       // 실제 저장된 preview 이미지 파일명
        long size1 = 0;
          
        String upDir =  Breply.getUploadDir(); // C:/kd/deploy/resort_v4sbm3c/contents/storage/
        
        Tool.deleteFile(upDir, file1saved);  // 실제 저장된 파일삭제
        Tool.deleteFile(upDir, thumb1);     // preview 이미지 삭제
        // -------------------------------------------------------------------
        // 파일 삭제 종료
        // -------------------------------------------------------------------
            
        // -------------------------------------------------------------------
        // 파일 전송 시작
        // -------------------------------------------------------------------
        String file1 = "";          // 원본 파일명 image

        // 전송 파일이 없어도 file1MF 객체가 생성됨.
        // <input type='file' class="form-control" name='file1MF' id='file1MF' 
        //           value='' placeholder="파일 선택">
        MultipartFile mf = breplyVO.getFile1MF();
            
        file1 = mf.getOriginalFilename(); // 원본 파일명
        size1 = mf.getSize();  // 파일 크기
            
        if (size1 > 0) { // 폼에서 새롭게 올리는 파일이 있는지 파일 크기로 체크 ★
          // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
          file1saved = Upload.saveFileSpring(mf, upDir); 
          
          if (Tool.isImage(file1saved)) { // 이미지인지 검사
            // thumb 이미지 생성후 파일명 리턴됨, width: 250, height: 200
            thumb1 = Tool.preview(upDir, file1saved, 250, 200); 
          }
          
        } else { // 파일이 삭제만 되고 새로 올리지 않는 경우
          file1="";
          file1saved="";
          thumb1="";
          size1=0;
        }
            
        breplyVO.setBreplyimg(file1);
        breplyVO.setBreplysaved(file1saved);
        breplyVO.setBreplythumb(thumb1);
        breplyVO.setBreplysize(size1);
        // -------------------------------------------------------------------
        // 파일 전송 코드 종료
        // -------------------------------------------------------------------
        model.addAttribute("breplyVO", breplyVO);
        System.out.println("cont: "+ breplyVO.getBreplycont());
        
        cnt = this.breplyProc.update(breplyVO); // 수정
        System.out.println("cnt: " + cnt);
        
        if (cnt == 1) {
          model.addAttribute("code", "update_success");
          model.addAttribute("breplycont", breplyVO.getBreplycont());
          model.addAttribute("breplypasswd", breplyVO.getBreplypasswd());

          ra.addAttribute("breplyno", breplyVO.getBreplyno());
          
          return "redirect:/breply/list"; // request -> param으로 접근 전환
        } else {
          ra.addFlashAttribute("code", "update_fail");
        }
        
        ra.addFlashAttribute("cnt", cnt);
        
        return "redirect:/breply/msg"; // /templates/member/msg.html
      } else {
        ra.addFlashAttribute("code", "not_exist_passwd");
        ra.addFlashAttribute("cnt", cnt);

        return "redirect:/breply/msg";
      }
    } else {
      return "redirect:/account/login";
    }
  }
  
  @GetMapping(value = "/delete/{breplyno}")
  public String delete(Model model, @PathVariable("breplyno") Integer breplyno) {
    
    BreplyVO breplyVO = this.breplyProc.read(breplyno);
    model.addAttribute("breplyVO", breplyVO);
    return "breply/delete";
  }
  
  @PostMapping(value = "/delete")
  public String delete_process(Model model,
                      HttpSession session,
                      RedirectAttributes ra,
                      Integer breplyno) {
    BreplyVO breplyVO = this.breplyProc.read(breplyno);
    model.addAttribute("breplyVO", breplyVO);

    if (session.getAttribute("accountno") != null || session.getAttribute("managerno") != null) { // 회원 로그인
      // HashMap<String, Object> map = new HashMap<String, Object>();
      // map.put("breplyno", breplyVO.getBreplyno());
      // map.put("breplypasswd", breplyVO.getBreplypasswd());

      // int cnt = this.breplyProc.password_check(map);
      // System.out.println("cnt: " + cnt);

      // if (cnt == 1) { // 비밀번호 확인
      int cnt = this.breplyProc.delete(breplyno);
      System.out.println("cnt : " + cnt);
      if(cnt == 1) {
        return "redirect:/breply/list";
      } else {
        ra.addFlashAttribute("code", "delete_fail");
        return "breply/msg";
      }
      // } else {
      //   ra.addFlashAttribute("code", "not_exist_passwd");
      //   ra.addFlashAttribute("cnt", cnt);

      //   return "redirect:/breply/msg";
      // }
    } else {
      return "redirect:/account/login";
    }
  }   
}