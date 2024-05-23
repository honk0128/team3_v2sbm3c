package dev.mvc.gpa;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.ai.AiVO;
import dev.mvc.ai.Aiurl;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
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
  
  @GetMapping(value = "/update")
  public String update(Model model,
                                HttpSession session,
                                @RequestParam("gpano") int gpano,
                                
                                RedirectAttributes ra
                                
                                ) {
    
    GpaVO gpaVO = new GpaVO();
    
    gpaVO.setGpano(gpano);
    
   
    model.addAttribute("gpaVO", gpaVO);

System.out.println(gpano);
    
    return "gpa/update";
  }
  
  @PostMapping(value = "/update")
  public String update_gpa(Model model,
                          HttpSession session,
                          @RequestParam("reviewStar") int star,
                          @ModelAttribute("gpaVO") GpaVO gpaVO,
                          RedirectAttributes ra) {
      // 여기서는 간단히 데이터를 수정하는 로직을 가정합니다
      // 실제로는 데이터를 수정하는 서비스나 DAO 계층을 호출해야 합니다
   
   int gpascore = star;
    
    gpaVO.setGpascore(gpascore);
    
    gpaproc.update(gpaVO); // 예를 들어, aiService.updateAi 메서드를 호출하여 데이터를 업데이트합니다

      ra.addAttribute("gpano", gpaVO.getGpano());
      ra.addAttribute("gpascore", gpaVO.getGpascore());
      

      return "redirect:/gpa/list";
  }
  
  
  
  @GetMapping(value = "/delete")
  public String delete(@RequestParam("gpano") int gpano, HttpSession session, Model model, RedirectAttributes ra, GpaVO gpaVO
                       ) {
    
    System.out.println("GET /delete, gpano: " + gpano);
    model.addAttribute("gpano", gpano);
    model.addAttribute("gpaVO", gpaVO);
    
    
    return "/gpa/delete";
  }
  
  @PostMapping(value = "/delete")
  public String delete(@RequestParam("gpano") int gpano, AiVO aiVO) {
    
//        
    this.gpaproc.delete(gpano);
//    
//    HashMap<String, Object> hashMap = new HashMap<String, Object>();
//    hashMap.put("word", word);
//
//    ra.addAttribute("word", word);
//    ra.addAttribute("now_page", now_page);
    
    return "redirect:/gpa/list";
  }   
}
