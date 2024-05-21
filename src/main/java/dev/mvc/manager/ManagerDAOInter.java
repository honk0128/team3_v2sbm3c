package dev.mvc.manager;

import java.util.HashMap;


public interface ManagerDAOInter {

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
  
}
 

