package dev.mvc.bookmark;

import java.util.ArrayList;
import java.util.HashMap;

public interface BookmarkProcInter {

  public int bm_insert(BookmarkVO bookmarkVO);

  public int bm_delete(BookmarkVO bookmarkVO);

  public int bm_list_count(HashMap<String, Object> hashMap);

  public ArrayList<BookmarkVO> bm_list(HashMap<String, Object> map);

  public String pagingBox(int accountno, int now_page, String word, String list_file, int search_count, 
  int record_per_page, int page_per_block);
}
