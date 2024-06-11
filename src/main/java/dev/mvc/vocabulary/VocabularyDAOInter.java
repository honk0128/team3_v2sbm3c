package dev.mvc.vocabulary;

import java.util.ArrayList;

public interface VocabularyDAOInter {
  
  /**
   * 단어 등록
   * @param vocabularyVO
   * @return
   */
  public int create_voca(VocabularyVO vocabularyVO);
  
  /**
   * 단어 전체 목록
   * @return
   */
  public ArrayList <VocabularyVO> list();
  
  /**
   * 해당 단어 뜻 출력
   * @param voca
   * @return
   */
  public String list_mean(String voca);

}