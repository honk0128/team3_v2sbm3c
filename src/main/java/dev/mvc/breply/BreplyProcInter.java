package dev.mvc.breply;

import java.util.ArrayList;
import java.util.HashMap;

public interface BreplyProcInter {
  /**
   * 댓글 등록
   * insert id="replycreate" parameterType="dev.mvc.reply.breplyVO"
   * @param breplyVO
   * @return 등록한 개수
   */
  public int replycreate(BreplyVO breplyVO);
  
  /**
   * 댓글 목록
   * select id="reply_list" resultType="dev.mvc.reply.breplyVO"
   * @return
   */
  public ArrayList<BreplyVO> reply_list();

  /**
   * 댓글 조회
   * select id="read" resultType="dev.mvc.breply.BreplyVO" parameterType="int"
   * @param breplyno
   * @return
   */
  public BreplyVO read(int breplyno);

  /**
   * 댓글(글) 수정
   * update id="update_contents" parameterType="dev.mvc.breply.BreplyVO"
   * @param breplyVO
   * @return
   */
  public int update_contents(BreplyVO breplyVO);

  /**
   * 댓글(사진) 수정
   * update id="update_img" parameterType="dev.mvc.breply.BreplyVO"
   * @param breplyVO
   * @return
   */
  public int update_img(BreplyVO breplyVO);

  /**
   * delete id="delete" parameterType="int"
   * @param breplyno
   * @return 삭제된 레코드 갯수
   */
  public int delete(int breplyno);

    /**
   * 패스워드 검사
   * @param hashMap
   * @return
   */
  public int password_check(HashMap<String, Object> hashMap);
}