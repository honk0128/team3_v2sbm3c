package dev.mvc.crudcate;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface CrudcateDAOInter {
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
  
  /**
   * 출력 순서 높임
   * @param crudcateno
   * @return
   */
  public int seqno_forward(int crudcateno);

  /**
   * 출력 순서 낮춤
   * @param crudcateno
   * @return
   */
  public int seqno_backward(int crudcateno);

  /**
   * 카테고리 공개
   * @param crudcateno
   * @return
   */
  public int visible_y(int crudcateno);

  /**
   * 카테고리 비공개
   * @param crudcateno
   * @return
   */
  public int visible_n(int crudcateno);

  public ArrayList<CrudcateVO> list_all_name_y();

  public ArrayList<CrudcateVO> list_all_namesub_y(String name);

  public ArrayList<CrudcateVO> list_search(String word);

  public ArrayList<CrudcateVO> list_search_paging(Map<String, Object> map);

  public int list_search_count(String word);

  public String pagingBox(int movieno, int now_page, String word, String list_file, int search_count, int record_per_page, int page_per_blocK);

}
