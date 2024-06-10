package dev.mvc.answer;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class AnswerVO {
  
//  CREATE TABLE AI_ANSWER(
//      ANSWERNO                            NUMBER(10)       NOT NULL       PRIMARY KEY,
//      TEXT_ANSWER                         CLOB       NULL ,
//      SEARCHNO                            NUMBER(10)       NULL ,
//  FOREIGN KEY (SEARCHNO) REFERENCES AI_SEARCH (SEARCHNO)
//);
  
  private int answerno;
  
  private String text_answer = "";
  
  private int searchno;
  
  private int accountno;
  
  

}
