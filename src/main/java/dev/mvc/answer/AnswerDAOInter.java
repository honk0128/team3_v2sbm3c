package dev.mvc.answer;

import java.util.ArrayList;

public interface AnswerDAOInter {
  
  public ArrayList <AnswerVO> list(int accountno);
  
  public ArrayList <AnswerVO> list_all();

}
