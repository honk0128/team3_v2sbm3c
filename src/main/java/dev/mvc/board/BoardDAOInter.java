package dev.mvc.board;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestParam;

public interface BoardDAOInter {
  /**
   * 등록
   * @param boradVO
   * @return
   */
  public int create(BoardVO boradVO);

  /**
   * 전체 목록
   * @return
   */
  public ArrayList<BoardVO> list_all(HashMap<String, Object> map);

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

  public int list_all_sc(HashMap<String, Object> hashmap);
}
