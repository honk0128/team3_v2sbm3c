package dev.mvc.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




public interface AccountDAOInter {
  /**
   * 중복 아이디 검사
   * @param aid
   * @return 중복 아이디 갯수
   */
  public int checkID_account(String aid);
  
  /**
   * 회원 가입
   * @param accountVO
   * @return 추가한 레코드 갯수
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
  
  public ArrayList<AccountVO> list_account_search(String word);
  
  public ArrayList<AccountVO> list_account_search_paging(Map <String, Object> map);
  
  public int list_account_search_count(String word);
  
  public String pagingBox(int now_page, String word, String list_file, int search_count, int record_per_page, int page_per_blocK);
  
  /**
   * 아이디 찾기(유저 확인)
   * @param aid
   * @return 일치하는 id 정보 있는지 확인
   */
  public int check_user(Map <String, Object> map);
  
  /**
   * 아이디 출력
   * @param aid
   * @return 일치하는 id 정보 있는지 확인
   */
  public String find_aid(Map <String, Object> map);
  
  /**
   * 비밀번호 찾기(유저 확인)
   * @param aid
   * @return 일치하는 id 정보 있는지 확인
   */
  public int check_user_passwd(Map <String, Object> map);
  
}
 

