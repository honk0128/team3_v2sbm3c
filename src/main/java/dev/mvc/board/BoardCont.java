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
import dev.mvc.gpa.GpaProcInter;
import dev.mvc.gpa.GpaVO;
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
  
  @Autowired
  @Qualifier("dev.mvc.gpa.GpaProc")
  private GpaProcInter gpaProc;

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
      word = Tool.checkNull(word).trim();

      ArrayList<CrudcateVOMenu> menu = this.crudcateProc.menu();
      model.addAttribute("menu", menu);

      // 페이징 목록
      ArrayList<BoardVO> list = this.boardProc.list_all(word, now_page, Board.RECORD_PER_PAGE);
      model.addAttribute("list", list);
  
      // 페이징 버튼 목록
      int search_count = this.boardProc.list_all_sc(word);
      String paging = this.boardProc.pagingBox_all(now_page, word, "/board/list_all", search_count, Board.RECORD_PER_PAGE, Board.PAGE_PER_BLOCK);
      model.addAttribute("paging_all", paging);
      model.addAttribute("now_page", now_page);
  
      model.addAttribute("word", word);
  
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
  public String read(Model model, @RequestParam("boardno") int boardno, @RequestParam(name = "word", defaultValue = "") String word,
  @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    ArrayList<CrudcateVOMenu> menu = this.crudcateProc.menu();
    model.addAttribute("menu", menu);

    ArrayList<GpaVO> list = this.gpaProc.avgscore(boardno);
    model.addAttribute("list", list);
    
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

  @GetMapping(value = "/update_board")
  public String update_board(HttpSession session, Model model, int boardno, RedirectAttributes ra, String word, int now_page) {

    ArrayList<CrudcateVOMenu> menu = this.crudcateProc.menu();
    model.addAttribute("menu", menu);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    if (this.managerProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      BoardVO boardVO = this.boardProc.read(boardno);
      model.addAttribute("boardVO", boardVO);

      CrudcateVO crudcateVO = this.crudcateProc.read(boardVO.getCrudcateno());
      model.addAttribute("crudcateVO", crudcateVO);

      return "/board/update_board";
    } else {
      ra.addAttribute("url", "/manager/login_cookie_need"); 
      return "redirect:/board/msg";
    }

  }

  @PostMapping(value = "/update_board")
  public String update_board(HttpSession session, Model model, BoardVO boardVO, RedirectAttributes ra, String search_word, int now_page) {
    ra.addAttribute("word", search_word);
    ra.addAttribute("now_page", now_page);

    if (this.managerProc.isMemberAdmin(session)) { // 관리자 로그인 확인
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("boardno", boardVO.getBoardno());
      map.put("bpasswd", boardVO.getBpasswd());

      if (this.boardProc.password_check(map) == 1) { // 패스워드 일치
        this.boardProc.update_board(boardVO); // 글수정

        // mav 객체 이용
        ra.addAttribute("boardno", boardVO.getBoardno());
        ra.addAttribute("crudcateno", boardVO.getCrudcateno());
        return "redirect:/board/read"; // @GetMapping(value = "/read")

      } else { // 패스워드 불일치
        ra.addFlashAttribute("code", "passwd_fail"); // redirect -> forward -> html
        ra.addFlashAttribute("cnt", 0);
        ra.addAttribute("url", "/board/msg"); // msg.html, redirect parameter 적용

        return "redirect:/board/msg"; // @GetMapping(value = "/msg")
      }
    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/board/msg"; // @GetMapping(value = "/msg")
    }

  }

   /**
   * 파일 수정 폼 http://localhost:9091/board/update_file?boardno=1
   * 
   * @return
   */
  @GetMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, int boardno, String word, int now_page) {
    ArrayList<CrudcateVOMenu> menu = this.crudcateProc.menu();
    model.addAttribute("menu", menu);
    
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    BoardVO boardVO = this.boardProc.read(boardno);
    model.addAttribute("boardVO", boardVO);

    CrudcateVO crudcateVO = this.crudcateProc.read(boardVO.getCrudcateno());
    model.addAttribute("crudcateVO", crudcateVO);

    return "/board/update_file";

  }

  /**
   * 파일 수정 처리 http://localhost:9091/board/update_file
   * 
   * @return
   */
  @PostMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, RedirectAttributes ra, BoardVO boardVO, String word, int now_page) {

    if (this.managerProc.isMemberAdmin(session)) {
      // 삭제할 파일 정보를 읽어옴, 기존에 등록된 레코드 저장용
      BoardVO boardVO_old = boardProc.read(boardVO.getBoardno());

      // -------------------------------------------------------------------
      // 파일 삭제 시작
      // -------------------------------------------------------------------
      String file1saved = boardVO_old.getBphotosaved(); // 실제 저장된 파일명
      String thumb1 = boardVO_old.getBthumb(); // 실제 저장된 preview 이미지 파일명
      long size1 = 0;

      String upDir = Board.getUploadDir(); // C:/kd/deploy/resort_v4sbm3c/board/storage/

      Tool.deleteFile(upDir, file1saved); // 실제 저장된 파일삭제
      Tool.deleteFile(upDir, thumb1); // preview 이미지 삭제
      // -------------------------------------------------------------------
      // 파일 삭제 종료
      // -------------------------------------------------------------------

      // -------------------------------------------------------------------
      // 파일 전송 시작
      // -------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = boardVO.getBphotoNF();

      file1 = mf.getOriginalFilename(); // 원본 파일명
      size1 = mf.getSize(); // 파일 크기

      if (size1 > 0) { // 폼에서 새롭게 올리는 파일이 있는지 파일 크기로 체크 ★
        // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
        file1saved = Upload.saveFileSpring(mf, upDir);

        if (Tool.isImage(file1saved)) { // 이미지인지 검사
          // thumb 이미지 생성후 파일명 리턴됨, width: 250, height: 200
          thumb1 = Tool.preview(upDir, file1saved, 250, 200);
        }

      } else { // 파일이 삭제만 되고 새로 올리지 않는 경우
        file1 = "";
        file1saved = "";
        thumb1 = "";
        size1 = 0;
      }

      boardVO.setBphoto(file1);
      boardVO.setBphotosaved(file1saved);
      boardVO.setBthumb(thumb1);
      boardVO.setBsize(size1);
      // -------------------------------------------------------------------
      // 파일 전송 코드 종료
      // -------------------------------------------------------------------

      this.boardProc.update_file(boardVO); // Oracle 처리
      ra.addAttribute ("boardno", boardVO.getBoardno());
      ra.addAttribute("crudcateno", boardVO.getCrudcateno());
      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);
      
      return "redirect:/board/read";
    } else {
      ra.addAttribute("url", "/member/login_cookie_need"); 
      return "redirect:/board/msg"; // GET
    }
  }

  /**
   * 파일 삭제 폼
   * http://localhost:9091/board/delete?boardno=1
   * 
   * @return
   */
  @GetMapping(value = "/delete")
  public String delete(HttpSession session, Model model, RedirectAttributes ra,int crudcateno, int boardno, String word, int now_page) {
    if (this.managerProc.isMemberAdmin(session)) { // 관리자로 로그인한경우
      model.addAttribute("crudcateno", crudcateno);
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);
      
      ArrayList<CrudcateVOMenu> menu = this.crudcateProc.menu();
      model.addAttribute("menu", menu);
      
      BoardVO boardVO = this.boardProc.read(boardno);
      model.addAttribute("boardVO", boardVO);
      
      CrudcateVO crudcateVO = this.crudcateProc.read(boardVO.getCrudcateno());
      model.addAttribute("crudcateVO", crudcateVO);
      
      return "/board/delete"; // forward
      
    } else {
      ra.addAttribute("url", "/manager/login_cookie_need");
      return "redirect:/board/msg"; 
    }

  }
  
  /**
   * 삭제 처리 http://localhost:9091/board/delete
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(RedirectAttributes ra,
                              int boardno, int crudcateno, String word, int now_page) {
    // -------------------------------------------------------------------
    // 파일 삭제 시작
    // -------------------------------------------------------------------
    // 삭제할 파일 정보를 읽어옴.
    BoardVO boardVO_read = boardProc.read(boardno);
        
    String file1saved = boardVO_read.getBphotosaved();
    String thumb1 = boardVO_read.getBthumb();
    
    String uploadDir = Board.getUploadDir();
    Tool.deleteFile(uploadDir, file1saved);  // 실제 저장된 파일삭제
    Tool.deleteFile(uploadDir, thumb1);     // preview 이미지 삭제
    // -------------------------------------------------------------------
    // 파일 삭제 종료
    // -------------------------------------------------------------------
        
    this.boardProc.delete(boardno); // DBMS 삭제
        
    // -------------------------------------------------------------------------------------
    // 마지막 페이지의 마지막 레코드 삭제시의 페이지 번호 -1 처리
    // -------------------------------------------------------------------------------------    
    // 마지막 페이지의 마지막 10번째 레코드를 삭제후
    // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
    // 페이지수를 4 -> 3으로 감소 시켜야함, 마지막 페이지의 마지막 레코드 삭제시 나머지는 0 발생
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("crudcateno", crudcateno);
    map.put("word", word);
    
    if (this.boardProc.list_cno_search_count(map) % Board.RECORD_PER_PAGE == 0) {
      now_page = now_page - 1; // 삭제시 DBMS는 바로 적용되나 크롬은 새로고침등의 필요로 단계가 작동 해야함.
      if (now_page < 1) {
        now_page = 1; // 시작 페이지
      }
    }
    // -------------------------------------------------------------------------------------

    ra.addAttribute("crudcateno", crudcateno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    
    return "redirect:/board/list_cno";    
    
  }   
}
