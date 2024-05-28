package dev.mvc.gpa;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

//CREATE TABLE GPA(
//    GPANO                             NUMBER(10)     NOT NULL    PRIMARY KEY,
//    GPASCORE                          INTEGER     NOT NULL ,
//    GPADATE                           DATE  DEFAULT SYSDATE NOT NULL,
//    ACCOUNTNO                         NUMBER(10)     NULL ,
//    BOARDNO                           NUMBER(10)     NOT NULL ,
//  FOREIGN KEY (ACCOUNTNO) REFERENCES ACCOUNT (ACCOUNTNO),
//  FOREIGN KEY (BOARDNO) REFERENCES BOARD (BOARDNO)
//);

@Setter @Getter
public class GpaVO {
  
  
  
  private String aid = "";
  
  
  
  private Integer gpano = 0;
  
  private Integer gpascore = 0;
  
  private Date gpadate;
  
  private Integer accountno = 0;
  
  private Integer boardno = 0;
  
}
