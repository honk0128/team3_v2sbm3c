package dev.mvc.gpa;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@RequestMapping("/gpa")
@Controller
public class GpaCont {

  @Autowired
  @Qualifier("dev.mvc.gpa.GpaProc")
  private GpaProcInter gpaproc;
 
  
  
  public GpaCont() {
    System.out.println("-> GpaCont created.");
  }
  
  @GetMapping(value = "/create")
    public String create(GpaVO gpaVO, Model model){
    
    return "gpa/create"; 
        
  }
  
  
  
  @PostMapping(value="/create")
  public String creategpa(@RequestParam("reviewStar") int star, Model model, GpaVO gpaVO, HttpSession session, String aid, RedirectAttributes ra) {

      // 세션에서 aid를 받아옵니다.
      String sessionAid = (String) session.getAttribute("aid");

      // aid를 사용하여 해당 계정의 accountno를 조회합니다.
      GpaVO gpaVO2 = gpaproc.readById(sessionAid);
      int accountno = gpaVO2.getAccountno();

      // GPA 객체에 필요한 정보를 설정합니다.
      gpaVO.setGpascore(star);
      gpaVO.setAccountno(accountno);
      gpaVO.setBoardno(8); // 이 부분은 boardno를 하드코딩하고 있는데, 필요에 따라 동적으로 설정해야 할 수도 있습니다.

      // GPA를 생성합니다.
      int cnt = gpaproc.create(gpaVO);

      if (cnt == 1) {
          // 생성 성공 시 다음 페이지로 리다이렉트합니다.
          return "redirect:/gpa/create"; // 성공 페이지로 리다이렉트하는 경로를 수정하세요.
      } else {
          // 생성 실패 시 에러 메시지를 설정합니다.
          ra.addFlashAttribute("error", "GPA 생성에 실패했습니다.");
          // 실패 시 다음 페이지로 리다이렉트합니다.
          return "redirect:/errorPage"; // 에러 페이지로 리다이렉트하는 경로를 수정하세요.
      }
  }
  
}
