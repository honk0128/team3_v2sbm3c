package dev.mvc.ai;

import java.util.ArrayList;
import java.util.Map;

public interface AiProcInter {
  /**
   * 등록
   * insert id="create" parameterType="dev.mvc.cate.CateVO"
   * @param cateVO
   * @return 등록한 레코드 갯수
   */
  public int create(AiVO aiVO);
  
  public ArrayList<AiVO> list();
  
  public int update(AiVO aiVO);
  
  public int delete(int searchno);


  
}







