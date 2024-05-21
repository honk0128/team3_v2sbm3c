package dev.mvc.manager;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.tool.Security;
import jakarta.servlet.http.HttpSession;

@Component("dev.mvc.manager.ManagerProc")
public class ManagerProc implements ManagerProcInter {

  @Autowired
  private ManagerDAOInter ManagerDAO;

  @Autowired
  Security security;

  public ManagerProc() {
    System.out.println("-> ManagerProc created.");
  }

  @Override
  public int login_manager(HashMap<String, Object> map) {
    int cnt = this.ManagerDAO.login_manager(map);
    return cnt;
  }

  @Override
  public ManagerVO readById(String mid) {
    ManagerVO managerVO = this.ManagerDAO.readById(mid);
    return managerVO;
  }

  /**
   * 회원인지 검사
   */
  @Override
  public boolean isMember(HttpSession session) {
    boolean sw = false; // 로그인하지 않은 것으로 초기화
    String agrade = (String) session.getAttribute("agrade");
    String mgrade = (String) session.getAttribute("mgrade");

    if (agrade != null) {
      if (agrade.equals("admin") || agrade.equals("member")) {
        sw = true; // 로그인 한 경우
      }
    } else if (mgrade != null) {
      if (mgrade.equals("admin") || mgrade.equals("member")) {
        sw = true;
      }
    }

    return sw;
  }

  /**
   * 관리자, 회원인지 검사
   */
  @Override
  public boolean isMemberAdmin(HttpSession session) {
    boolean sw = false; // 로그인하지 않은 것으로 초기화
    String agrade = (String) session.getAttribute("grade");
    String mgrade = (String) session.getAttribute("mgrade");

    if (agrade != null) {
      if (agrade.equals("admin")) {
        sw = true; // 로그인 한 경우
      }
    } else if (mgrade != null) {
      if (mgrade.equals("admin")) {
        sw = true; // 로그인 한 경우
      }
    }

    return sw;
  }

}
