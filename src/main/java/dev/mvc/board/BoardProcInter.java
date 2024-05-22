package dev.mvc.board;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestParam;

public interface BoardProcInter {
  /**
   * 등록
   * @param boardVO
   * @return
   */
  public int create(BoardVO boardVO);

  /**
   * 전체 목록
   * @return
   */
  public ArrayList<BoardVO> list_all(HashMap<String, Object> map);

  public int list_all_sc(HashMap<String, Object> hashmap);

  public String pagingBox_all(int now_page, String word, String list_file, int search_count, 
  int record_per_page, int page_per_block); 

  /**
   * 카테고리별 목록
   * @param crudcateno
   * @return
   */
  public ArrayList<BoardVO> list_cno(int crudcateno);

  /**
   * 조회
   * @param boardno
   * @return
   */
  public BoardVO read(int boardno);

  /**
   * 유튜브 삽입
   * @param map
   * @return
   */
  public int youtube(HashMap<String, Object> map);

  public ArrayList<BoardVO> list_cno_search(HashMap<String, Object> hashMap);

  public int list_cno_search_count(HashMap<String, Object> hashMap);

  public ArrayList<BoardVO> list_cno_search_paging(HashMap<String, Object> map);

  public String pagingBox(int crudcateno, int now_page, String word, String list_file, int search_count, 
  int record_per_page, int page_per_block);   



}
