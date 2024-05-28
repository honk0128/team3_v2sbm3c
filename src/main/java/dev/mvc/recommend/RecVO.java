package dev.mvc.recommend;

import lombok.Getter;
import lombok.Setter;

// CREATE TABLE RECOMMEND(
// 		RECONO                        		NUMBER(10)	 NOT NULL 		 PRIMARY KEY,
// 		RECODATE                      		DATE		 NOT NULL,
// 		BOARDNO                       		NUMBER(10)		 NULL ,
// 		ACCOUNTNO                     		NUMBER(10)		 NULL ,
//   FOREIGN KEY (BOARDNO) REFERENCES BOARD (BOARDNO),
//   FOREIGN KEY (ACCOUNTNO) REFERENCES ACCOUNT (ACCOUNTNO)
// );

@Getter @Setter
public class RecVO {

private int recono;

private String recodate;

private int boardno;

private int accountno;
  
}
