package dev.mvc.board;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.catalina.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    return "board/create"; // /templates/board/create.html
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

    if (session.getAttribute("accountno") != null || session.getAttribute("managerno") != null) { // 회원으로 로그인한 경우
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
        ra.addAttribute("crudcateno", boardVO.getCrudcateno()); // controller -> controller: O
        return "redirect:/board/list_cno";
      } else {
        ra.addFlashAttribute("code", "create_fail"); // DBMS 등록 실패
        ra.addFlashAttribute("cnt", 0); // 업로드 실패
        ra.addFlashAttribute("url", "/board/msg"); // msg.html, redirect parameter 적용
        return "redirect:/board/msg"; // Post -> Get - param...
      }
  
  }
  return "redirect:/board/login_form_need"; // /member/login_cookie_need.html
}
  /**
   * 전체 목록
   * http://localhost:9093/board/list_all
   * @param session
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(HttpSession session, Model model, @RequestParam(name = "word", defaultValue = "") String word,
  @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    if (this.managerProc.isMemberAdmin(session)) {
      ArrayList<CrudcateVOMenu> menu = this.crudcateProc.menu();
      model.addAttribute("menu", menu);
  
      word = Tool.checkNull(word).trim();
  
      HashMap<String, Object> map = new HashMap<>();
      map.put("word", word);
      map.put("now_page", now_page);
  
      ArrayList<BoardVO> list = this.boardProc.list_all(map);
      model.addAttribute("list", list);
  
      // System.out.println("-> size: " + list.size());
      model.addAttribute("word", word);
  
  
      int search_count = this.boardProc.list_all_sc(map);
      String paging = this.boardProc.pagingBox_all(now_page, word, "/board/list_cno", search_count,
          Board.RECORD_PER_PAGE, Board.PAGE_PER_BLOCK);
      model.addAttribute("paging_all", paging);
      model.addAttribute("now_page", now_page);
  
      model.addAttribute("search_count", search_count);
  
      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * Board.RECORD_PER_PAGE);
      model.addAttribute("no", no);
      return "board/list_all";
    } else {
      return "redirect:/manager/login_form_need";
    }
  }

  /**
   * 조회
   * @param model
   * @param boardno
   * @param word
   * @param now_page
   * @return
   */
  @GetMapping(value = "/read")
  public String read(Model model, Integer boardno, @RequestParam(name = "word", defaultValue = "") String word,
  @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    ArrayList<CrudcateVOMenu> menu = this.crudcateProc.menu();
    model.addAttribute("menu", menu);

    BoardVO boardVO = this.boardProc.read(boardno);
    long bsize = boardVO.getBsize();
    String bsize_label = Tool.unit(bsize);
    boardVO.setBsize_label(bsize_label);
    model.addAttribute("boardVO", boardVO);

    CrudcateVO crudcateVO = this.crudcateProc.read(boardVO.getCrudcateno());
    model.addAttribute("crudcateVO", crudcateVO);

    // 조회에서 화면 하단에 출력
    // ArrayList<ReplyVO> reply_list = this.replyProc.list_board(boardno);
    // mav.addObject("reply_list", reply_list);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    return "board/read";
  }

  /**
   * http://localhost:9093/board/youtube?crudcateno=4&boardno=14
   * @param model
   * @param boardno
   * @return
   */
  @GetMapping(value = "/youtube")
  public String youtube(Model model, Integer boardno) {
    ArrayList<CrudcateVOMenu> menu = this.crudcateProc.menu();
    model.addAttribute("menu", menu);

    BoardVO boardVO = this.boardProc.read(boardno); // youtube 정보 읽어 오기
    model.addAttribute("boardVO", boardVO);

    CrudcateVO crudcateVO = this.crudcateProc.read(boardVO.getCrudcateno()); // 그룹 정보 읽기
    model.addAttribute("crudcateVO", crudcateVO);

    // model.addAttribute("word", word);
    // model.addAttribute("now_page", now_page);
    
    return "board/youtube"; 
  }


  @PostMapping(value = "/youtube")
  public String youtube_update(Model model, RedirectAttributes ra, int boardno, String byoutube) {

    if (byoutube != null && byoutube.trim().length() > 0) { // 삭제 중인지 확인, 삭제가 아니면 youtube 크기 변경
      byoutube = Tool.youtubeResize(byoutube, 640); // youtube 영상의 크기를 width 기준 640 px로 변경
    }

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("boardno", boardno);
    hashMap.put("byoutube", byoutube);

    this.boardProc.youtube(hashMap);
    
    ra.addAttribute("boardno", boardno);
    // ra.addAttribute("word", word);
    // ra.addAttribute("now_page", now_page);

    return "redirect:/board/read";
  }

  /**
   * http://localhost:9093/board/list_cno?crudcateno=4
   * @param session
   * @param model
   * @param crudcateno
   * @param word
   * @param now_page
   * @return
   */
  @GetMapping(value = "/list_cno")
  public String list_cno_search_paging(HttpSession session, Model model, int crudcateno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    ArrayList<CrudcateVOMenu> menu = this.crudcateProc.menu();
    model.addAttribute("menu", menu);

    CrudcateVO crudcateVO = this.crudcateProc.read(crudcateno);
    model.addAttribute("crudcateVO", crudcateVO);

    word = Tool.checkNull(word).trim();

    HashMap<String, Object> map = new HashMap<>();
    map.put("crudcateno", crudcateno);
    map.put("word", word);
    map.put("now_page", now_page);

    ArrayList<BoardVO> list = this.boardProc.list_cno_search_paging(map);
    model.addAttribute("list", list);

    // System.out.println("-> size: " + list.size());
    model.addAttribute("word", word);

    int search_count = this.boardProc.list_cno_search_count(map);
    String paging = this.boardProc.pagingBox(crudcateno, now_page, word, "/board/list_cno", search_count,
        Board.RECORD_PER_PAGE, Board.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Board.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    return "board/list_cno_search_paging";
  }

}
