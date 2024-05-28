package dev.mvc.recommend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.mvc.account.AccountProcInter;
import dev.mvc.breply.BreplyProcInter;


@RequestMapping("/recommend")
@Controller
public class RecCont {
  @Autowired
  @Qualifier("dev.mvc.recommend.RecProc")
  private RecProcInter recProcInter;

  @Autowired
  @Qualifier("dev.mvc.breply.BreplyProc")
  private BreplyProcInter breplyProc;

  @Autowired
  @Qualifier("dev.mvc.account.AccountProc")
  private AccountProcInter AccountProc;

  public RecCont() {
    System.out.println("-> BreplyCont created.");
  }  
  
  @GetMapping(value = "/rec_up")
  public String rec_up(Model model, @PathVariable(name="recono") Integer recono) {
    
    this.recProcInter.reco_up(recono);
    return "brereply/rec_up";
  }

  @GetMapping(value = "/rec_down")
  public String rec_down(Model model, @PathVariable(name="recono") Integer recono) {
    
    this.recProcInter.reco_down(recono);
    return "brereply/rec_down";
  }
  
}