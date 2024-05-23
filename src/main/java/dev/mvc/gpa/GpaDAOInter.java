package dev.mvc.gpa;

import java.util.ArrayList;





public interface GpaDAOInter {

  
  /**
   * 등록
   * insert id="create" parameterType="dev.mvc.gpa.GpaVO"
   * @param GpaVO
   * @return 등록한 레코드 갯수
   */
    public int create(GpaVO gpaVO);
    
    /**
     * id로 회원 정보 조회
     * @param id
     * @return
     */
    public GpaVO readById(String aid);
    
    
    public ArrayList<GpaVO> list();
    
    public int update(GpaVO gpaVO);
    
    public int delete(int gpano);

}
