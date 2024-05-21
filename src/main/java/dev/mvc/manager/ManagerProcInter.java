package dev.mvc.manager;

import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.http.HttpSession;


public interface ManagerProcInter {
  /**
   * 로그인 처리
   */
  public int login_manager(HashMap<String, Object> map);
  
  
  /**
   * id로 회원 정보 조회
   * @param id
   * @return
   */
  public ManagerVO readById(String mid);
  
  /**
   * 로그인된 회원 계정인지 검사.
   * @param session
   * @return true: 사용자
   */
  public boolean isMember(HttpSession session);

  /**
   * 로그인된 회원 관리자 계정인지 검사.
   * @param session
   * @return true: 사용자
   */
  public boolean isMemberAdmin(HttpSession session);
  


  
}




