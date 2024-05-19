package dev.mvc.crudcate;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import jakarta.servlet.http.HttpSession;


@RequestMapping("/crudcate")
@Controller
public class CrudcateCont {
  @Autowired
  @Qualifier("dev.mvc.crudcate.CrudcateProc")
  private CrudcateProcInter crudcateProc;

  public CrudcateCont() {
    System.out.println("-> CrudCateCont created.");  
  }

  /**
   * Create 폼
   * http://localhost:9093/crudcate/create
   */
  @GetMapping(value="/create") 
  public String create(Model model, CrudcateVO crudcateVO) {
    return "/crudcate/create"; // /templates/crudcate/create.html
  }


  /**
   * Create 폼 데이터 처리
   * http://localhost:9093/crudcate/create
   */
  // 폼 데이터 처리
  @PostMapping(value="/create")
  public String create(Model model, CrudcateVO crudcateVO, BindingResult result) {
    if (result.hasErrors()) {
      // 유효성 검사에 실패한 경우
      return "/crudcate/create"; // 다시 등록 폼으로 이동
    }
    
    int cnt = this.crudcateProc.create(crudcateVO);
    System.out.println("-> cnt: " + cnt);
    
    if (cnt == 1) {
      model.addAttribute("code", "create_success");
      model.addAttribute("name", crudcateVO.getName());
      model.addAttribute("namesub", crudcateVO.getNamesub());
      model.addAttribute("namesubsub", crudcateVO.getNamesubsub());
    } else {
      model.addAttribute("code", "create_fail");
    }
    
    model.addAttribute("cnt", cnt);
    return "/crudcate/msg";
  }

  /**
   * 카테고리 전체 목록
   * /crudcate/list_all.html
   * 
   */
  @GetMapping(value="/list_all")
  public String list_all(HttpSession session, Model model, CrudcateVO crudcateVO) {
    ArrayList<CrudcateVO> list = this.crudcateProc.list_all();
    model.addAttribute("list", list);
    return "crudcate/list_all"; 
  }
  
  /**
   * 조회
   * @param model
   * @param crudcateno
   * @return
   */
  @GetMapping(value="/read/{crudcateno}")
  public String read(Model model, @PathVariable("crudcateno") Integer crudcateno) {
    CrudcateVO crudcateVO = this.crudcateProc.read(crudcateno);
    model.addAttribute("crudcateVO", crudcateVO);
    return "crudcate/read";
  }
  
  @GetMapping(value="/update/{crudcateno}")
  public String update(Model model, @PathVariable("crudcateno") Integer crudcateno) {
    CrudcateVO crudcateVO = this.crudcateProc.read(crudcateno);
    model.addAttribute("crudcateVO", crudcateVO);
    return "crudcate/update";
  }

  @PostMapping(value="/update")
  public String update_process(Model model, CrudcateVO crudcateVO, BindingResult result) {
    if (result.hasErrors()) {
      // 유효성 검사에 실패한 경우
      return "/crudcate/update"; // 다시 수정 폼으로 이동
    }
      
    int cnt = this.crudcateProc.update(crudcateVO); // 수정
    if (cnt == 1) {
      return "redirect:/crudcate/update/" + crudcateVO.getCrudcateno();
    } else {
      model.addAttribute("code", "update_fail");
      return "crudcate/msg"; // /templates/crudcate/msg.html
    }
  }

  @GetMapping(value="/delete/{crudcateno}")
  public String delete(Model model, @PathVariable("crudcateno") Integer crudcateno) {
    CrudcateVO crudcateVO = this.crudcateProc.read(crudcateno);
    model.addAttribute("crudcateVO", crudcateVO);
    return "crudcate/delete";
  }

  @PostMapping(value="/delete")
  public String delete_process(Model model, Integer crudcateno) {

    CrudcateVO crudcateVO = this.crudcateProc.read(crudcateno);
    model.addAttribute("crudcateVO", crudcateVO);
      
    int cnt = this.crudcateProc.delete(crudcateno); // 삭제
    model.addAttribute("cnt" , cnt);
    if (cnt == 1) {
      return "redirect:/crudcate/list_all";
    } else {
      model.addAttribute("code", "delete_fail");
      return "crudcate/msg"; // /templates/crudcate/msg.html
    }
  }
  
  
}
