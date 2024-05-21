package dev.mvc.manager;

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

import dev.mvc.account.AccountProcInter;
// import dev.mvc.contents.Contents;
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

@RequestMapping("/manager")
@Controller
public class ManagerCont {
  @Autowired
  @Qualifier("dev.mvc.manager.ManagerProc")
  private ManagerProcInter managerProc;
  
  
  @Autowired
  Security security;

  public ManagerCont() {
    System.out.println("-> ManagerCont created");
  }
  
  @GetMapping(value = "/checkID")
  @ResponseBody
  public String checkID(String mid) {
    System.out.println("-> id: " + mid);
    int cnt = this.managerProc.checkID_manager(mid);
    
    JSONObject obj = new JSONObject();
    obj.put("cnt", cnt);

    return obj.toString();
  }
  
  /**
   * 회원 가입 폼
   * @param model
   * @param memberVO
   * @return
   */
  @GetMapping(value="/signin") // http://localhost:9091/account/create
  public String create_form(Model model, ManagerVO managerVO) {
    return "manager/create";    // /template/member/create.html
  }
  
  /**
   * 회원가입 실행
   * @param model
   * @param managerVO
   * @return
   */
  @PostMapping(value="/signin_manager")
  public String create_proc(Model model, ManagerVO managerVO) {
    int checkID_cnt = this.managerProc.checkID_manager(managerVO.getMid());
    
    if (checkID_cnt == 0) {
      managerVO.setMgrade(1); // 관리자 등급 번호 1
      int cnt = this.managerProc.signin_manager(managerVO);
      
      if (cnt == 1) {
        model.addAttribute("code", "create_success");
        model.addAttribute("mname", managerVO.getMname());
        model.addAttribute("mid", managerVO.getMid());
      } else {
        model.addAttribute("code", "create_fail");
      }
      
      model.addAttribute("cnt", cnt);
    } else { // id 중복
      model.addAttribute("code", "duplicte_fail");
      model.addAttribute("cnt", 0);
    }
    
    return "manager/create"; // /templates/member/msg.html
  }

  /**
   * 로그인
   * 
   * @param model
   * @param managerno 관리자 번호
   * @return 관리자 정보
   */
  @GetMapping(value = "/login")
  public String login_form(Model model) {
    return "manager/login"; // templates/member/login.html
  }

  /**
   * 로그인 처리
   * 
   * @param model
   * @param managerno 관리자 번호
   * @return 관리자 정보
   */
  @PostMapping(value = "/login_manager")
  public String login_proc(HttpSession session, Model model, String mid, String mpasswd) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("mid", mid);
    map.put("mpasswd", this.security.aesEncode(mpasswd));

    int cnt = this.managerProc.login_manager(map);
    System.out.println("-> login_proc cnt: " + cnt);

    model.addAttribute("cnt", cnt);

    if (cnt == 1) {
      // id를 이용하여 회원 정보 조회
      ManagerVO managerVO = this.managerProc.readById(mid);
      session.setAttribute("managerno", managerVO.getManagerno());
      session.setAttribute("mid", managerVO.getMid());
      session.setAttribute("mname", managerVO.getMname());
      session.setAttribute("mgrade", managerVO.getMgrade());
      
      if (managerVO.getMgrade() >= 1 && managerVO.getMgrade() <= 10) {
        session.setAttribute("mgrade", "admin");
      } else if (managerVO.getMgrade() >= 11 && managerVO.getMgrade() <= 20) {
        session.setAttribute("mgrade", "account");
      } else if (managerVO.getMgrade() >= 21) {
        session.setAttribute("mgrade", "guest");
      }

      return "index";
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
