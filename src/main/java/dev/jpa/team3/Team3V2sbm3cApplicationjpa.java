package dev.jpa.team3;

import java.text.SimpleDateFormat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import dev.jpa.issue.domain.ISSUE;
import dev.jpa.issue.domain.ISSUERepository;
import dev.mvc.team3.Team3V2sbm3cApplication;

@SpringBootApplication
@ComponentScan(basePackages = {"dev.jpa"})
public class Team3V2sbm3cApplicationjpa implements CommandLineRunner {

  @Autowired
  private ISSUERepository repository;
  
   public static void main(String[] args) {
      SpringApplication.run(Team3V2sbm3cApplicationjpa.class, args);
   }

    /**
   * 서버 실행(재실행)시 자동 실행됨. String... args: 가변 인자, 문자열 0개 이상 인수로 받을 수 있음.
   */
  @Override
  public void run(String... args) throws Exception {
//    // 객체 생성시 레코드 등록됨, SQL 필요 없음.
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    // String now = sdf.format(new Date());

    // 등록, Repository 선언 필요없이 완전 자동 지원
//    repository.save(new ISSUE("시스템 접속자 증가", "시스템 장애 조치중", 0, sdf.format(new Date())));
//    repository.save(new ISSUE("시스템 접속자 증가", "시스템 장애 조치 완료", 0, sdf.format(new Date())));
//    repository.save(new ISSUE("사용자 로그인 오류 발생 증가", "시스템 장애 조치중", 0, sdf.format(new Date())));
//    repository.save(new ISSUE("연휴 휴무 안내", "시스템 장애 접수를 제외한 모든 없무를 23일~25일까지 중지함", 0, sdf.format(new Date())));
//    repository.save(new ISSUE("Happy new year!", "Merry Christmas~", 0, sdf.format(new Date())));
//    repository.save(new ISSUE("접속자 증가", "접속자 증가로 인한 오류 발생 증가", 0, sdf.format(new Date())));
//    repository.save(new ISSUE("우분투 OS 지원", "우분투 OS 22 버전 지원 시작", 0, sdf.format(new Date())));
//    
//    // 전체 목록 SQL 자동 실행, Repository 선언 필요없이 완전 자동 지원
//    for (ISSUE issue : repository.findAll()) {
//      System.out.println("-> " + issue.getIssueno() + " " + issue.getTitle() + " " + issue.getContent() + " " + issue.getRdate());
//    }

    // title LIKE(Containing 포함)로 검색
//    for (ISSUE issue : repository.findByTitleContaining("시스템")) {
//      System.out.println("-> " + issue.getIssueno() + " " + issue.getTitle() + " " + issue.getContent() + " " + issue.getRdate());
//    }

    // rdate LIKE로 검색
//    for (ISSUE issue : repository.findByRdateContaining("2024-06-17")) {
//      System.out.println("-> " + issue.getIssueno() + " " + issue.getTitle() + " " + issue.getContent() + " " + issue.getRdate());
//    }

    // title or content로 검색
//    for (ISSUE issue : repository.findByTitleContainingOrContentContaining("접속자", "접속자")) {
//      System.out.println("-> " + issue.getIssueno() + " title: " + issue.getTitle() + " content: " + issue.getContent() + " " + issue.getRdate());
//    }

    // title and content로 검색
//    for (ISSUE issue : repository.findByTitleContainingAndContentContaining("접속자", "접속자")) {
//      System.out.println("-> " + issue.getIssueno() + " title: " + issue.getTitle() + " content: " + issue.getContent() + " " + issue.getRdate());
//    }

    // title 대소문자 무시 검색
//    for (ISSUE issue : repository.findByTitleContainingIgnoreCase("happy")) {
//      System.out.println("-> " + issue.getIssueno() + " title: " + issue.getTitle() + " content: " + issue.getContent() + " " + issue.getRdate());
//    }

    // SQL 사용, 컬럼을 모두 명시해야함, 날짜 구간 검색
//    for (ISSUE issue : repository.findByRdatePeriod("2024-06-01", "2024-06-31")) {
//      System.out.println("-> " + issue.getIssueno() + " title: " + issue.getTitle() + " content: " + issue.getContent() + " " + issue.getRdate());
//    }

    // 조회, Repository 선언 필요없이 완전 자동 지원
//    ISSUE issue = repository.findById((long)1).get();
//    System.out.println("-> " + issue.getIssueno() + " title: " + issue.getTitle() + " content: " + issue.getContent() + " " + issue.getRdate());

    // 수정: Read -> Update, Repository 선언 필요없이 완전 자동 지원
//    ISSUE issue = repository.findById((long)1).get(); // 레코드 읽음, 조회
//    System.out.println("-> 1)");
//    issue.setTitle("프로젝트 발표 4"); // update하고 싶은 컬럼의 값 저장, setter 호출
//    System.out.println("-> 2)");
//    issue.setContent("27일 중간 발표");
//    System.out.println("-> 3)");
//    
//    // 혹시 변경됬을수도 있는 경우를위해 기존의 값을 다시 읽어와 새로 지정된 값과 결합하여 update
//    ISSUE issue_new = repository.save(issue);  
//    System.out.println("-> 4)");
//    System.out.println(issue_new.toString());   // 변경된 값 확인

    // 삭제, Repository 선언 필요없이 완전 자동 지원
//    Optional<ISSUE> issue = repository.findById((long) 1); // 삭제할 레코드 읽기
//    
//    if (issue.isPresent()) { // 레코드 존재하면
//       ISSUE entity = issue.get(); // 레코드 추출
//       System.out.println(entity.toString());   // 데이터 출력
//        
//        repository.delete(entity); // 삭제
//    } else {
//      System.out.println("존재하지 않는 번호 입니다.");
//    }

    // -----------------------------------------------------------------------------------
    // Employee
    // -----------------------------------------------------------------------------------
    // Employee 직원 등록
    // public Employee(String id, String passwd, String mname, String rdate, int grade) {
//    employeeRepository.save(new Employee("emp1", "1234", "피어스", sdf.format(new Date()), 1));
//    employeeRepository.save(new Employee("emp2", "1234", "메릴", sdf.format(new Date()), 1));
//    employeeRepository.save(new Employee("emp3", "1234", "아만다", sdf.format(new Date()), 1));
    
    // 직원 로그인
//    Employee employee = employeeRepository.findByIdAndPasswd("emp1", "1234");
//    System.out.println(employee.toString());
    
    // 페이징, Oracle 12C~
//    Pageable pageable = PageRequest.of(1, 2);
//    Page<ISSUE> list = repository.findAllByOrderByRdateDesc(pageable);
//    for (ISSUE issue : list) {
//      System.out.println("-> " + issue.getIssueno() + " title: " + issue.getTitle() + " content: " + issue.getContent() + " " + issue.getRdate());
//    }
    
    // 페이징, Oracle 11G
    for (ISSUE issue : repository.findAllByOrderByRdateDesc()) {
      System.out.println("-> " + issue.getIssueno() + " title: " + issue.getTitle() + " content: " + issue.getContent() + " " + issue.getRdate());
    }
  }

}
