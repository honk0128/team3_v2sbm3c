package dev.mvc.bookmark;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dev.mvc.account.AccountProcInter;
import dev.mvc.board.Board;
import dev.mvc.board.BoardProcInter;
import dev.mvc.board.BoardVO;
import dev.mvc.crudcate.CrudcateProcInter;
import dev.mvc.crudcate.CrudcateVO;
import dev.mvc.crudcate.CrudcateVOMenu;
import dev.mvc.manager.ManagerProcInter;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;

@RequestMapping(value = "/bookmark")
@Controller
public class BookmarkCont {
  @Autowired
  @Qualifier("dev.mvc.crudcate.CrudcateProc")
  private CrudcateProcInter crudcateProc;

  @Autowired
  @Qualifier("dev.mvc.account.AccountProc")
  private AccountProcInter accountProcInter;

  @Autowired
  @Qualifier("dev.mvc.manager.ManagerProc")
  private ManagerProcInter managerProc;
  
  @Autowired
  @Qualifier("dev.mvc.board.BoardProc")
  private BoardProcInter boardProc;

  @Autowired
  @Qualifier("dev.mvc.bookmark.BookmarkProc")
  private BookmarkProcInter bookmarkProc;

  @PostMapping("/add")
  public ResponseEntity<?> bm_insert(@RequestBody BookmarkVO bookmarkVO) {
      int result = bookmarkProc.bm_insert(bookmarkVO);
      if (result > 0) {
          return ResponseEntity.ok().body("{\"message\": \"북마크가 저장되었습니다.\"}");
      } else {
          return ResponseEntity.status(500).body("{\"message\": \"북마크 저장에 실패했습니다.\"}");
      }
  }
  
  // 북마크 삭제 처리
  @PostMapping("/delete")
  @ResponseBody
  public ResponseEntity<?> bm_delete(@RequestBody BookmarkVO bookmarkVO) {
    int result = bookmarkProc.bm_delete(bookmarkVO);
    if (result > 0) {
      return ResponseEntity.ok().body("{\"message\": \"북마크가 삭제되었습니다.\"}");
    } else {
      return ResponseEntity.status(500).body("{\"message\": \"북마크 삭제에 실패했습니다.\"}");
    }
  }

  
  @GetMapping(value = "/bm_list")
  public String bm_list(HttpSession session, Model model,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

      // HttpSession에서 accountno를 가져옵니다.
      Integer accountno = (Integer) session.getAttribute("accountno");
      if (accountno == null) {
          // accountno가 없는 경우 처리 (예: 로그인 페이지로 리다이렉트)
          return "redirect:/login";
      }

      ArrayList<CrudcateVOMenu> menu = this.crudcateProc.menu();
      model.addAttribute("menu", menu);

      word = Tool.checkNull(word).trim();

      HashMap<String, Object> map = new HashMap<>();
      map.put("accountno", accountno);
      map.put("word", word);
      map.put("now_page", now_page);

      ArrayList<BookmarkVO> list = this.bookmarkProc.bm_list(map);
      model.addAttribute("list", list);
      model.addAttribute("word", word);

      int search_count = this.bookmarkProc.bm_list_count(map);
      String paging = this.bookmarkProc.pagingBox(accountno, now_page, word, "/bookmark/bm_list", search_count,
          Board.RECORD_PER_PAGE, Board.PAGE_PER_BLOCK);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      model.addAttribute("search_count", search_count);

      int no = search_count - ((now_page - 1) * Board.RECORD_PER_PAGE);
      model.addAttribute("no", no);

      return "bookmark/bm_list";
  }
}
