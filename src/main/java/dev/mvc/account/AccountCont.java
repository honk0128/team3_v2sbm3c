package dev.mvc.account;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.reflection.SystemMetaObject;

//import java.util.ArrayList;
//import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import dev.mvc.cate.CateProcInter;
//import dev.mvc.cate.CateVOMenu;
import dev.mvc.tool.Security;
//import dev.mvc.tool.Tool;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/account")
@Controller
public class AccountCont {
  @Autowired
  @Qualifier("dev.mvc.account.AccountProc")
  private AccountProcInter accountProc;
  
  @Autowired
  Security security;

  public AccountCont() {
    System.out.println("-> AccountCont created");
  }

  @GetMapping(value = "/checkID")
  @ResponseBody
  public String checkID(String aid) {
    System.out.println("-> id: " + aid);
    int cnt = this.accountProc.checkID(aid);

    JSONObject obj = new JSONObject();
    obj.put("cnt", cnt);

    return obj.toString();

  }

  @GetMapping(value = "/signin")
  public String signin_form(Model model, AccountVO accountVO) {
    return "account/create";
  }

  @PostMapping(value = "/signin")
  public String signin_proc(Model model, AccountVO accountVO, RedirectAttributes ra) {
    int checkID_cnt = this.accountProc.checkID(accountVO.getAid());

    if (checkID_cnt == 0) {
      // ------------------------------------------------------------------------------
      // 파일 전송 코드 시작
      // ------------------------------------------------------------------------------
      String aprofile_img = ""; // 원본 파일명 image
      String aprofile_imgsave = ""; // 저장된 파일명, image
      String aprofile_thum = ""; // preview image

      String upDir = Profiles.getUploadDir(); // 파일을 업로드할 폴더 준비
      System.out.println("-> upDir: " + upDir);

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = accountVO.getAprofile_imgMF();

      aprofile_img = mf.getOriginalFilename(); // 원본 파일명 산출, 01.jpg
      System.out.println("-> 원본 파일명 산출 file1: " + aprofile_img);

      long aprofile_size = mf.getSize(); // 파일 크기
      if (aprofile_size > 0) { // 파일 크기 체크, 파일을 올리는 경우
        if (Tool.checkUploadFile(aprofile_img) == true) { // 업로드 가능한 파일인지 검사
          // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg, spring_2.jpg...
          aprofile_imgsave = Upload.saveFileSpring(mf, upDir);

          if (Tool.isImage(aprofile_imgsave)) { // 이미지인지 검사
            // thumb 이미지 생성후 파일명 리턴됨, width: 200, height: 150
            aprofile_thum = Tool.preview(upDir, aprofile_imgsave, 200, 150);
          }

          accountVO.setAprofile_img(aprofile_img); // 순수 원본 파일명
          accountVO.setAprofile_imgsave(aprofile_imgsave); // 저장된 파일명(파일명 중복 처리)
          accountVO.setAprofile_thum(aprofile_thum); // 원본이미지 축소판
          accountVO.setAprofile_size(aprofile_size); // 파일 크기

        } else { // 전송 못하는 파일 형식
          ra.addFlashAttribute("code", "check_upload_file_fail"); // 업로드 할 수 없는 파일
          ra.addFlashAttribute("cnt", 0); // 업로드 실패
          ra.addFlashAttribute("url", "/contents/msg"); // msg.html, redirect parameter 적용
          return "redirect:/contents/msg"; // Post -> Get - param...
        }
      } else { // 글만 등록하는 경우
        System.out.println("-> 글만 등록");
      }

      // ------------------------------------------------------------------------------
      // 파일 전송 코드 종료
      // ------------------------------------------------------------------------------

      accountVO.setAgrade(15); // 기본 회원 15
      int cnt = this.accountProc.signin(accountVO);

      if (cnt == 1) {
        model.addAttribute("code", "create_success");
        model.addAttribute("aname", accountVO.getAname());
        model.addAttribute("aid", accountVO.getAid());

      } else {
        model.addAttribute("code", "create_fail");
      }

      model.addAttribute("cnt", cnt);
    } else { // id 중복
      model.addAttribute("code", "duplicte_fail");
      model.addAttribute("cnt", 0);
    }

    return "account/create"; // /templates/member/msg.html
  }

  @GetMapping(value = "/list")
  public String list(HttpSession session, Model model) {

    ArrayList<AccountVO> list = this.accountProc.list();

    model.addAttribute("list", list);

    return "account/list"; // templates/member/list.html
  }

  /**
   * 조회
   * 
   * @param model
   * @param accountno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value = "/read")
  public String read(HttpSession session, Model model, int accountno) {
    // 회원은 회원 등급만 처리, 관리자: 1 ~ 10, 사용자: 11 ~ 20
    // int gradeno = this.memberProc.read(memberno).getGrade(); // 등급 번호
    String agrade = (String) session.getAttribute("agrade"); // 등급: admin, member, guest

    // 사용자: member && 11 ~ 20
    // if (grade.equals("member") && (gradeno >= 11 && gradeno <= 20) && memberno ==
    // (int)session.getAttribute("memberno")) {
    if (agrade.equals("account") && accountno == (int) session.getAttribute("accountno")) {
      // System.out.println("-> read memberno: " + memberno);

      AccountVO accountVO = this.accountProc.read(accountno);
      model.addAttribute("accountVO", accountVO);
      System.out.println("agrade: " + agrade);

      return "account/read"; // templates/member/read.html

    } else if (agrade.equals("admin")) {
      // System.out.println("-> read memberno: " + memberno);

      AccountVO accountVO = this.accountProc.read(accountno);
      model.addAttribute("accountVO", accountVO);

      return "account/read"; // templates/member/read.html
    } else {
      return "redirect:/member/login_form_need"; // redirect
    }

  }

  /**
   * 로그인
   * 
   * @param model
   * @param memberno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value = "/login")
  public String login_form(Model model) {
    return "account/login"; // templates/member/login.html
  }

  /**
   * 로그인 처리
   * 
   * @param model
   * @param memberno 회원 번호
   * @return 회원 정보
   */
  @PostMapping(value = "/login")
  public String login_proc(HttpSession session, Model model, String aid, String apasswd) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("aid", aid);
    map.put("apasswd", this.security.aesEncode(apasswd));

    int cnt = this.accountProc.login(map);
    System.out.println("-> login_proc cnt: " + cnt);

    model.addAttribute("cnt", cnt);

    if (cnt == 1) {
      // id를 이용하여 회원 정보 조회
      AccountVO accountVO = this.accountProc.readById(aid);
      session.setAttribute("accountno", accountVO.getAccountno());
      session.setAttribute("aid", accountVO.getAid());
      session.setAttribute("aname", accountVO.getAname());
      session.setAttribute("agrade", accountVO.getAgrade());
      
      if (accountVO.getAgrade() >= 1 && accountVO.getAgrade() <= 10) {
        session.setAttribute("agrade", "admin");
      } else if (accountVO.getAgrade() >= 11 && accountVO.getAgrade() <= 20) {
        session.setAttribute("agrade", "account");
      } else if (accountVO.getAgrade() >= 21) {
        session.setAttribute("agrade", "guest");
      }

      return "account/list";
    } else {
      model.addAttribute("code", "login_fail");
      return "account/msg";
    }

  }
  
  /**
   * 로그아웃
   * @param model
   * @param accountno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value="/logout")
  public String logout(HttpSession session, Model model) {
    session.invalidate();  // 모든 세션 변수 삭제
    return "redirect:/";
  }
}
