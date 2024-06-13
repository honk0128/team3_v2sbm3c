package dev.mvc.answer;

import java.util.ArrayList;
import java.util.Map;

public interface AnswerDAOInter {
  
  /**
   * 유저 전용 리스트
   * @param accountno
   * @return
   */
  public ArrayList <AnswerVO> list(int accountno);
  
  /**
   * 관리자 전용 리스트
   * @return
   */
  public ArrayList <AnswerVO> list_all();
  
  /**
   * 답변 검색 카운트
   * @param word
   * @return
   */
  public int list_all_search_count(String word);
  
  /**
   * 질문 검색 + 페이징
   * @param map
   * @return
   */
  public ArrayList<AnswerVO> list_all_search_paging(Map <String, Object> map);

}
