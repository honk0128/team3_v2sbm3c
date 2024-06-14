package dev.mvc.gpa;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.board.BoardVO;





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
    
    
    public int list_cno_search_count(HashMap<String, Object> hashMap);

    /**
     * 카테고리 검색 + 페이징
     * @param map
     * @return
     */
    public ArrayList<GpaVO> list_search_paging(HashMap<String, Object> map);

    
    public ArrayList<GpaVO> avgscore(int boardno);
    
    public int update(GpaVO gpaVO);
    
    public int delete(int gpano);

}
