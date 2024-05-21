package dev.mvc.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.tool.Security;

@Component("dev.mvc.board.BoardProc")
public class BoardProc implements BoardProcInter {
  @Autowired
  Security security;

  @Autowired // BoardDAOInter interface를 구현한 클래스의 객체를 만들어 자동 할당
  private BoardDAOInter boardDAO;

  @Override // 추상 메소드를 구현했음.
  public int create(BoardVO boardVO) {
    // -------------------------------------------------------------------
    String passwd = boardVO.getBpasswd();
    String passwd_encoded = this.security.aesEncode(passwd);
    boardVO.setBpasswd(passwd_encoded);
    // -------------------------------------------------------------------
    
    int cnt = this.boardDAO.create(boardVO);
    return cnt;
  }
}
