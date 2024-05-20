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
    int cnt = this.breplyDAO.replycreate(breplyVO);
    return cnt;
  }

  @Override
  public ArrayList<BreplyVO> reply_list() {
    ArrayList<BreplyVO> list = this.breplyDAO.reply_list();
    return list;
  }

  @Override
  public BreplyVO read(int breplyno) {
    BreplyVO breplyVO = this.breplyDAO.read(breplyno);
    return breplyVO;
  }

  @Override
  public int update_contents(BreplyVO breplyVO) {
    int cnt = this.breplyDAO.update_contents(breplyVO);
    return cnt;
  }

  @Override
  public int update_img(BreplyVO breplyVO) {
    int cnt = this.breplyDAO.update_img(breplyVO);
    return cnt;
  }

  public int delete(int breplyno) {
    int cnt = this.breplyDAO.delete(breplyno);
    return cnt;
  }

  @Override
  public int password_check(HashMap<String, Object> map) {
    String passwd = (String)map.get("passwd");
    passwd = this.security.aesEncode(passwd);

    int cnt = this.breplyDAO.password_check(map);
    return cnt;
  }

}