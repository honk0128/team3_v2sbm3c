package dev.mvc.account;

import java.util.ArrayList;
import java.util.HashMap;


public interface AccountProcInter {
  /**
   * 중복 아이디 검사
   * @param aid
   * @return 중복 아이디 갯수, 1: 중복, 0: 중복 없음
   */
  public int checkID_account(String aid);
  
  /**
   * 회원 가입
   * @param accountVO
   * @return
   */
  public int signin_account(AccountVO accountVO);
  
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
   * @param aid
   * @return
   */
  public AccountVO readById(String aid);
  
  /**
   * 로그인 처리
   */
  public int login_account(HashMap<String, Object> map);
  
  /**
   * 수정 처리
   * @param accountVO
   * @return
   */
  public int update_account(AccountVO accountVO);
  
  /**
   * 회원 삭제 처리
   * @param accountno
   * @return
   */
  public int delete_account(int accountno);
  
  

  
}




