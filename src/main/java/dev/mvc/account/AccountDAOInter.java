package dev.mvc.account;

import java.util.ArrayList;
import java.util.HashMap;



public interface AccountDAOInter {
  /**
   * 중복 아이디 검사
   * @param id
   * @return 중복 아이디 갯수
   */
  public int checkID(String aid);
  
  /**
   * 회원 가입
   * @param accountVO
   * @return 추가한 레코드 갯수
   */
  public int signin(AccountVO accountVO);

  /**
   * 회원 전체 목록
   * @return
   */
  public ArrayList<AccountVO> list();
  
  /**
   * accountno로 회원 정보 조회
   * @param accountno
   * @return
   */
  public AccountVO read(int accountno);
  
  /**
   * id로 회원 정보 조회
   * @param id
   * @return
   */
  public AccountVO readById(String aid);
  
  /**
   * 로그인 처리
   */
  public int login(HashMap<String, Object> map);
  
}
 

