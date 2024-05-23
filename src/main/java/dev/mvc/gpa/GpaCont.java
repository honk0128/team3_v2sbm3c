package dev.mvc.gpa;

import java.util.ArrayList;
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

import dev.mvc.ai.AiVO;
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

      
      String sessionAid = (String) session.getAttribute("aid");

      
      GpaVO gpaVO2 = gpaproc.readById(sessionAid);
      int accountno = gpaVO2.getAccountno();

      
      gpaVO.setGpascore(star);
      gpaVO.setAccountno(accountno);
      gpaVO.setBoardno(8); 
     
      int cnt = gpaproc.create(gpaVO);

      if (cnt == 1) {
          
          return "redirect:/gpa/list"; 
          } else {
          
          ra.addFlashAttribute("error", "GPA 생성에 실패했습니다.");
          
          return "redirect:/errorPage";     }
  }
  
  
  
  @GetMapping(value = "/list")
  public String list(HttpSession session, Model model) {

    ArrayList<GpaVO> list = this.gpaproc.list();

    model.addAttribute("list", list);

    return "gpa/list"; // templates/member/list.html
  }
  
}
