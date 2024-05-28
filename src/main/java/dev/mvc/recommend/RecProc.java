package dev.mvc.recommend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.mvc.tool.Security;

@Service("dev.mvc.recommend.RecProc")
public class RecProc implements RecProcInter{
  @Autowired
  Security security;

  @Autowired
  private RecDAOInter RecDAO;

  public RecProc() {
    System.out.println("-> RecProc created.");
  }

  @Override
  public int reco_up(int recono) {
    int cnt = this.RecDAO.reco_up(recono);
    return cnt;
  }

  @Override
  public int reco_down(int recono) {
    int cnt = this.RecDAO.reco_down(recono);
    return cnt;
  }

  

}