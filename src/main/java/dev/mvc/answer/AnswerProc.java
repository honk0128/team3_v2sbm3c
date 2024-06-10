package dev.mvc.answer;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.account.AccountVO;

@Component("dev.mvc.answer.AnswerProc")
public class AnswerProc implements AnswerProcInter {
  
  @Autowired
  private AnswerDAOInter answerDAO;
  
  public AnswerProc() {
    System.out.println("-> AnswerProc created.");
  }

  @Override
  public ArrayList<AnswerVO> list(int accountno) {
    ArrayList <AnswerVO> list = this.answerDAO.list(accountno);
    return list;
  }

  @Override
  public ArrayList<AnswerVO> list_all() {
    ArrayList <AnswerVO> list_all = this.answerDAO.list_all();
    return list_all;
  }


}
