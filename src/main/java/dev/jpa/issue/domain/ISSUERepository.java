package dev.jpa.issue.domain;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

// Long: 식별자(PK) 필드의 타입, DAO 역활
public interface ISSUERepository extends JpaRepository<ISSUE, Long> {
  // title로 LIKE 검색, find로 시작되어야 함, findBy + 컬럼명 + Containing.
  List<ISSUE> findByTitleContaining(String title);
  
  // rdate로 검색, findBy + 컬럼명 + Containing.
  List<ISSUE> findByRdateContaining(String rdate);

  // title or content로 검색
  List<ISSUE> findByTitleContainingOrContentContaining(String title, String content);
  
  // title and content로 검색
  List<ISSUE> findByTitleContainingAndContentContaining(String title, String content);

  // title 대소문자 무시 검색
  List<ISSUE> findByTitleContainingIgnoreCase(String title);
  
  // SQL 사용, 컬럼을 모두 명시해야함, 날짜 구간 검색
  @Query(value="SELECT issueno, title, content, cnt, rdate FROM issue WHERE (SUBSTR(rdate, 1, 10) >= :start_date) AND (SUBSTR(rdate, 1, 10) <= :end_date)", nativeQuery = true)
  List<ISSUE> findByRdatePeriod(@Param("start_date") String start_date, @Param("end_date") String end_date);
 
  // 페이징하지 않는 경우
  // rdate를 기준으로 내림차순 정렬하여 ISSUE 목록을 출력하는 메소드
  // List<ISSUE> findAllByOrderByRdateDesc();

  // 페이징 하는 경우
  // rdate를 기준으로 내림차순 정렬하여 ISSUE 페이징 목록을 출력하는 메소드
  // Oracle 12C~
  // Page<ISSUE> findAllByOrderByRdateDesc(Pageable pageable);
  
  // Oracle 11G
  @Query(value="SELECT issueno, title, content, cnt, rdate, r FROM ( SELECT issueno, title, content, cnt, rdate, rownum as r FROM ( SELECT issueno, title, content, cnt, rdate FROM issue ORDER BY rdate DESC )) WHERE r <= 500", nativeQuery = true)
  List<ISSUE> findAllByOrderByRdateDesc();
  
  // SQL 사용, 컬럼을 모두 명시해야함, 날짜 구간 검색, 날짜 내림 차순 정렬
  @Query(value="SELECT issueno, title, content, cnt, rdate FROM issue WHERE (SUBSTR(rdate, 1, 10) >= :start_date) AND (SUBSTR(rdate, 1, 10) <= :end_date) ORDER BY rdate DESC", nativeQuery = true)
  List<ISSUE> findByRdateDescPeriod(@Param("start_date") String start_date, @Param("end_date") String end_date);

  // 조회수 증가
  @Modifying
  @Transactional
  @Query(value="UPDATE issue SET cnt = cnt + 1 WHERE issueno=:id", nativeQuery = true)
  void increaseCnt(@Param("id") Long id);
  
}



