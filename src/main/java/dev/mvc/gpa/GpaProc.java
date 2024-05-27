package dev.mvc.gpa;

import java.util.ArrayList;

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

  @Override
  public ArrayList<GpaVO> list() {
    ArrayList<GpaVO> list = this.gpaDAO.list();
    
    return list;
  }

  @Override
  public int delete(int gpano) {
    int cnt = this.gpaDAO.delete(gpano);
    return cnt;
  }

  @Override
  public int update(GpaVO gpaVO) {
    int cnt = this.gpaDAO.update(gpaVO);
    return cnt;
  }

  @Override
  public ArrayList<GpaVO> avgscore(int boardno) {
    ArrayList<GpaVO> list = this.gpaDAO.avgscore(boardno);
    return list;
  }
  
  
  
}
