package dev.mvc.vocabulary;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.mvc.crudcate.CrudcateProcInter;
import dev.mvc.crudcate.CrudcateVOMenu;
import dev.mvc.manager.ManagerProcInter;
import jakarta.servlet.http.HttpSession;

@RequestMapping(value = "vocabulary")
@Controller
public class VocabularyCont {
  @Autowired
  @Qualifier("dev.mvc.vocabulary.VocabularyProc")
  private VocabularyProcInter vocabularyProc;

  @Autowired
  @Qualifier("dev.mvc.crudcate.CrudcateProc")
  private CrudcateProcInter crudcateProc;
  
  @Autowired
  @Qualifier("dev.mvc.manager.ManagerProc")
  private ManagerProcInter managerProc;

  public VocabularyCont() {
    System.out.println("-> VocabularyCont created.");
  }

  @GetMapping(value = "/create")
  public String create(Model model, HttpSession session) {
    if (this.managerProc.isAdmin(session)) {
    ArrayList<CrudcateVOMenu> menu = this.crudcateProc.menu();
    model.addAttribute("menu", menu);

//    int managerno = (int) session.getAttribute("managerno");
//    System.out.println("->managerno: " + managerno);
    return "th/vocabulary/create";
    }
    return "redirect:/account/login_need";
   
  }

  @PostMapping(value = "/create")
  public String create_proc(VocabularyVO vocabularyVO, Model model, HttpSession session) {
    int cnt = this.vocabularyProc.create_voca(vocabularyVO);
    System.out.println("-> cnt: " + cnt);
    if (cnt == 1) {

      model.addAttribute("code", "create_success");
      model.addAttribute("voca", vocabularyVO.getVoca());
    } else {
      model.addAttribute("code", "create_fail");
    }

    return "th/vocabulary/msg";
  }

  @GetMapping(value = "/list")
  public String list(Model model, HttpSession session) {
    if (this.managerProc.isAdmin(session)) {
      ArrayList<VocabularyVO> list = this.vocabularyProc.list();
      System.out.println("list: " + list);
      model.addAttribute("list", list);
      return "th/vocabulary/list";
    }
    return "redirect:/account/login_need";
   
  }


}
