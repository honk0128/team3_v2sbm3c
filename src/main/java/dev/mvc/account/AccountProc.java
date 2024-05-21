package dev.mvc.account;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.tool.Security;

@Component("dev.mvc.account.AccountProc")
public class AccountProc implements AccountProcInter{
  
  @Autowired
  private AccountDAOInter accountDAO;
  
  @Autowired
  Security security;
  
  public AccountProc(){
     System.out.println("-> AccountProc created.");
  }
  

  @Override
  public int checkID_account(String aid) {
    int cnt = this.accountDAO.checkID_account(aid);
    return cnt;
  }

  @Override
  public int signin_account(AccountVO accountVO) {
   String apasswd = accountVO.getApasswd();
   String passwd_encoded = this.security.aesEncode(apasswd);
   accountVO.setApasswd(passwd_encoded);
   
   int cnt = this.accountDAO.signin_account(accountVO);
    return cnt;
  }


  @Override
  public ArrayList<AccountVO> list() {
    ArrayList<AccountVO> list = this.accountDAO.list();
    return list;
  }


  @Override
  public AccountVO read(int accountno) {
    AccountVO accountVO = this.accountDAO.read(accountno);
    return accountVO;
  }


  @Override
  public AccountVO readById(String aid) {
    AccountVO accountVO = this.accountDAO.readById(aid);
    return accountVO;
  }


  @Override
  public int login_account(HashMap<String, Object> map) {
    int cnt = this.accountDAO.login_account(map);
    return cnt;
  }


  @Override
  public int update_account(AccountVO accountVO) {
    int cnt = this.accountDAO.update_account(accountVO);
    return cnt;
  }


  @Override
  public int delete_account(int accountno) {
    int cnt = this.accountDAO.delete_account(accountno);
    return cnt;
  }
  
}
