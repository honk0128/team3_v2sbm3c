package dev.mvc.bookmark;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.board.BoardVO;

public interface BookmarkDAOInter {

  public int bm_insert(BookmarkVO bookmarkVO);

  public int bm_delete(BookmarkVO bookmarkVO);

  public int bm_list_count(HashMap<String, Object> hashMap);

  public ArrayList<BookmarkVO> bm_list(HashMap<String, Object> map);
}

