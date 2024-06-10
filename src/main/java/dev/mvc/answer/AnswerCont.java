package dev.mvc.answer;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.mvc.ai.AiProcInter;
import dev.mvc.ai.AiVO;
import dev.mvc.manager.ManagerProcInter;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/answer")
@Controller
public class AnswerCont {
  
  @Autowired
  @Qualifier("dev.mvc.answer.AnswerProc")
  private AnswerProcInter answerProc;
  
  @Autowired
  @Qualifier("dev.mvc.ai.AiProc")
  private AiProcInter aiProc;
  
  @Autowired
  @Qualifier("dev.mvc.manager.ManagerProc")
  private ManagerProcInter managerProc;
  
  public AnswerCont() {
    System.out.println("-> AnswerCont created");
  }
  
  @GetMapping(value = "/list")
  public String answer_list(HttpSession session, Model model, AiVO aiVO) {
    
    
    Integer sessionAccountno = (Integer) session.getAttribute("accountno");
    
    
    if (this.managerProc.isAdmin(session)) {
      ArrayList <AnswerVO> list = this.answerProc.list_all();
      ArrayList <AiVO> img = this.aiProc.img_all();
     
      
      model.addAttribute("list", list) ;
      model.addAttribute("img", img);
     
    }else {
      ArrayList <AnswerVO> list = this.answerProc.list(sessionAccountno);
      ArrayList <AiVO> img = this.aiProc.img(sessionAccountno);
      
      model.addAttribute("list", list) ;
      model.addAttribute("img", img);
    }
    
 
    
 
    return "th/answer/list";
  }

}
