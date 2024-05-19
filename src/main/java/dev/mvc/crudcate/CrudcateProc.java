package dev.mvc.crudcate;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@Service("dev.mvc.crudcate.CrudcateProc")
public class CrudcateProc implements CrudcateProcInter {
  @Autowired
  private CrudcateDAOInter crudcateDAO;

  public CrudcateProc() {
  }

  @Override
  public int create(CrudcateVO crudcateVO) {
    int cnt = this.crudcateDAO.create(crudcateVO);
    return cnt;
  }

  @Override
  public ArrayList<CrudcateVO> list_all() {
    ArrayList<CrudcateVO> list = this.crudcateDAO.list_all();
    return list;
  }

  @Override
  public CrudcateVO read(int crudcateeno) {
    CrudcateVO crudcateVO = this.crudcateDAO.read(crudcateeno);
    return crudcateVO;
  }

  @Override
  public int update(CrudcateVO crudcateVO) {
    int cnt = this.crudcateDAO.update(crudcateVO);
    return cnt;
  }

  @Override
  public int delete(int crudcateeno) {
    int cnt = this.crudcateDAO.delete(crudcateeno);
    return cnt;
  }
  
}
