package dev.mvc.crudcate;

import java.util.ArrayList;
import java.util.Map;

public interface CrudcateProcInter {
    /**
   * 등록
   * 
   */
  public int create(CrudcateVO crudcateVO);

  /**
   * 전체 목록
   * @return
   */
  public ArrayList<CrudcateVO> list_all();

  /**
   * 조회 
   * @param crudcateno
   * @return
   */
  public CrudcateVO read(int crudcateno);

  /**
   * 수정
   * @param crudcateVO
   * @return
   */
  public int update(CrudcateVO crudcateVO);

  /**
   * 삭제
   * @param crudcateno
   * @return
   */
  public int delete(int crudcateno);

  public int seqno_forward(int crudcateno);


  public int seqno_backward(int crudcateno);


  public int visible_y(int crudcateno);


  public int visible_n(int crudcateno);

  public ArrayList<CrudcateVO> list_all_name_y();

  public ArrayList<CrudcateVO> list_all_namesub_y(String name);

  /** 메뉴 */
  public ArrayList<CrudcateVOMenu> menu();

  public ArrayList<CrudcateVO> list_search(String word);

  public ArrayList<CrudcateVO> list_search_paging(String word, int now_page, int record_per_page);

  public int list_search_count(String word);

  public String pagingBox(int movieno, int now_page, String word, String list_file, int search_count, int record_per_page, int page_per_blocK);
}