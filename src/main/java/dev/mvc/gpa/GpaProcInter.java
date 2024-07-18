package dev.mvc.gpa;

import java.util.ArrayList;
import java.util.HashMap;

public interface GpaProcInter {
  /**
   * 등록
   * insert id="create" parameterType="dev.mvc.gpa.GpaVO"
   * @param VideoVO
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

    public ArrayList<GpaVO> list_search_paging(HashMap<String, Object> map);

    public int list_cno_search_count(HashMap<String, Object> hashMap);

    
    public String pagingBox(int boardno, int now_page, String word, String list_file, int search_count, 
    int record_per_page, int page_per_block);
    
    
    
    public ArrayList<GpaVO> avgscore(int boardno);
    
    public int update(GpaVO gpaVO);
    
    public int delete(int gpano);
}
