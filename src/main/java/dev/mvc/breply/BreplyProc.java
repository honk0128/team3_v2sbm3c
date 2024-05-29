package dev.mvc.breply;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.mvc.tool.Security;

@Service("dev.mvc.breply.BreplyProc")
public class BreplyProc implements BreplyProcInter{
  @Autowired
  Security security;

  @Autowired
  private BreplyDAOInter breplyDAO;

  public BreplyProc() {
    System.out.println("-> BreplyProc created.");
  }

  @Override
  public int replycreate(BreplyVO breplyVO) {
    String passwd = breplyVO.getBreplypasswd();
    String passwd_encoded = this.security.aesEncode(passwd);
    breplyVO.setBreplypasswd(passwd_encoded);

    int cnt = this.breplyDAO.replycreate(breplyVO);
    return cnt;
  }

  @Override
  public ArrayList<BreplyVO> reply_list(int boardno) {
    ArrayList<BreplyVO> list = this.breplyDAO.reply_list(boardno);
    return list;
  }

  @Override
  public BreplyVO read(int breplyno) {
    BreplyVO breplyVO = this.breplyDAO.read(breplyno);
    return breplyVO;
  }

  @Override
  public int update(BreplyVO breplyVO) {
    int cnt = this.breplyDAO.update(breplyVO);
    return cnt;
  }

  // @Override
  // public int update_img(BreplyVO breplyVO) {
  //   int cnt = this.breplyDAO.update_img(breplyVO);
  //   return cnt;
  // }

  public int delete(int breplyno) {
    int cnt = this.breplyDAO.delete(breplyno);
    return cnt;
  }

  @Override
  public int password_check(HashMap<String, Object> map) {
    String breplypasswd = (String)map.get("breplypasswd");
    System.out.println("-> passwd type: " + (String)map.get("breplypasswd"));
    breplypasswd = this.security.aesEncode(breplypasswd);
    System.out.println(breplypasswd);
    map.put("breplypasswd", breplypasswd);

    int cnt = this.breplyDAO.password_check(map);
    return cnt;
  }

}