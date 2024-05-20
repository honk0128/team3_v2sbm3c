package dev.mvc.crudcate;

import java.util.ArrayList;

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
}
