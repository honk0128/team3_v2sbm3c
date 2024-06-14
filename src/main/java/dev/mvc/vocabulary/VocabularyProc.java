package dev.mvc.vocabulary;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.vocabulary.VocabularyProc")
public class VocabularyProc implements VocabularyProcInter {

  @Autowired
  private VocabularyDAOInter vocabularyDAO;

  @Override
  public int create_voca(VocabularyVO vocabularyVO) {
    int cnt = this.vocabularyDAO.create_voca(vocabularyVO);
    return cnt;
  }

  @Override
  public ArrayList<VocabularyVO> list() {
    ArrayList <VocabularyVO> list = this.vocabularyDAO.list();
    return list;
  }

  @Override
  public String list_mean(String voca) {
    String mean = this.vocabularyDAO.list_mean(voca);
    return mean;
  }

  @Override
  public int delete(int vocano) {
    int cnt = this.vocabularyDAO.delete(vocano);
    return cnt;
  }

  @Override
  public int update(VocabularyVO vocabularyVO) {
    int cnt = this.vocabularyDAO.update(vocabularyVO);
    return cnt;
  }

  
}
