package dev.mvc.gpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dev.mvc.gpa.GpaProc")
public class GpaProc  implements GpaProcInter{
  @Autowired
  private GpaDAOInter gpaDAO;
  
  public GpaProc() {
    //System.out.println( " -> Gpaproc create");
  }
  
  @Override
  public int create(GpaVO gpaVO) {
    int cnt = this.gpaDAO.create(gpaVO);
    return cnt;
  }

  @Override
  public GpaVO readById(String aid) {
    GpaVO gpaVO = this.gpaDAO.readById(aid);
    return gpaVO;
  }
  
  
  
}
