package dev.mvc.team3;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.mvc.board.BoardProcInter;
import dev.mvc.board.BoardVO;
import dev.mvc.crudcate.CrudcateProcInter;
import dev.mvc.crudcate.CrudcateVO;
import dev.mvc.crudcate.CrudcateVOMenu;
import dev.mvc.tool.Security;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeCont {
  @Autowired
  @Qualifier("dev.mvc.crudcate.CrudcateProc")
  private CrudcateProcInter crudcateProc;
  
//  @Autowired
//  private Security security;
  
  public HomeCont() {
    System.out.println("-> HomeCont created.");
  }
  
  @Autowired
  @Qualifier("dev.mvc.board.BoardProc")
  private BoardProcInter boardProc;
  
  @GetMapping(value="/") 
  public String home(Model model, HttpSession session, @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) { // 파일명 return
//    if (this.security != null) {
//      System.out.println("-> 객체 고유 코드: " + security.hashCode());
//      System.out.println(security.aesEncode("1234"));
//    }
    
    int crudcateno = 4;
    
    System.out.println("-> http://localhost:9093");
    
    ArrayList<CrudcateVOMenu> menu = this.crudcateProc.menu();
    model.addAttribute("menu", menu);
    
    CrudcateVO crudcateVO = this.crudcateProc.read(crudcateno);
    model.addAttribute("crudcateVO", crudcateVO);
    
    HashMap<String, Object> map = new HashMap<>();
    map.put("crudcateno", crudcateno);
    map.put("word", word);
    map.put("now_page", now_page);
    
    
    ArrayList<BoardVO> list = this.boardProc.list_cno_search_paging(map);
    model.addAttribute("list", list);
    
    
//    model.addAttribute("search_count", search_count);
//    
    return "index"; // /templates/index.html  
  }
  
}






