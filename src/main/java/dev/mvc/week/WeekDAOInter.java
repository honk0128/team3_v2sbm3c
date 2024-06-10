package dev.mvc.week;

import java.util.ArrayList;
import java.util.HashMap;

public interface WeekDAOInter {
  
  /**
   * 
   * @param weekVO
   * @return
   */
  public int create(WeekVO weekVO);

  public ArrayList<WeekVO> list_wds(int accountno);

  public ArrayList<WeekVO> list_all(HashMap<String, Object> map);

  public int delete(HashMap<String, Object> map);

  public int update(WeekVO weekVO);

}
