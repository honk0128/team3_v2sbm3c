package dev.jpa.issue.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.jpa.issue.domain.ISSUE;

@RestController // REST 컨트롤러 선언
@RequestMapping("/issue") // 이 컨트롤러의 모든 메서드가 처리할 기본 URL 경로 설정
public class ISSUEController {
  @Autowired
  private ISSUEService service;

  public ISSUEController() {
    System.out.println("-> ISSUEController created.");
  }

  /**
   * HTML 특수 문자의 변경
   * https://en.wikipedia.org/wiki/List_of_XML_and_HTML_character_entity_references    
   * @param str
   * @return
   */
  public static synchronized String convertChar(String str) {
    str = str.replace("&", "&amp;");  // 특수 문자 -> 엔티티로 변경 -> 브러우저 출력시 기능이 없는 단순 문자로 출력
    str = str.replace("<", "&lt;");
    str = str.replace(">", "&gt;");
    str = str.replace("'", "&apos;");
    str = str.replace("\"", "&quot;");
    str = str.replace("\r\n", "<BR>");
    str = str.replace(" ", "&nbsp;");
    return str;
  }
  
  /**
   * Create - POST 요청을 처리하여 새로운 MyEntity 객체를 생성
   * @RequestBody ISSUE entity: JSON → JAVA 객체로 매핑
   * http://localhost:9100/issue
   * @PostMapping(consumes = "application/json") 생략 가능, 기본적으로 JSON 입출력
   * @param entity
   * @return
   */
  @PostMapping
  public ResponseEntity<ISSUE> createEntity(@RequestBody ISSUE entity) {
    System.out.println("-> 레코드 추가: " + entity.getTitle());
    
    ISSUE savedEntity = service.saveEntity(entity);
    return ResponseEntity.ok(savedEntity); // 생성된 엔티티 반환
  }

  /**
   * 전체 목록
   * GET 요청을 처리하여 모든 Entity 객체의 리스트를 반환
   * http://localhost:9100/issue/find_all
   * @return
   */
  @GetMapping(path = "/find_all")
  public List<ISSUE> find_all() {
    return service.find_all();
  }
  
  /**
   * title로 검색
   * http://localhost:9100/issue/find_by_title
   * @param title
   * @return
   */
  @GetMapping(path = "/find_by_title")
  public List<ISSUE> find_by_title(@RequestParam("title") String title) {
    return service.find_by_title(title);
  }
  
  /**
   * rdate로 검색
   * http://localhost:9100/issue/find_by_rdate?rdate=2023-12-23
   * @param rdate
   * @return
   */
  @GetMapping(path = "/find_by_rdate")
  public List<ISSUE> find_by_rdate(@RequestParam("rdate") String rdate) {
    return service.find_by_rdate(rdate);
  }
  
  /**
   * title or content로 검색
   * http://localhost:9100/issue/find_by_title_or_content?title=발생&content=Merry
   * @param 
   * @return
   */
  @GetMapping(path = "/find_by_title_or_content")
  public List<ISSUE> find_by_title_or_content(@RequestParam("title") String title, 
                                                                @RequestParam("content") String content) {
    return service.find_by_title_or_content(title, content);
  }
  
  /**
   * title and content로 검색
   * http://localhost:9100/issue/find_by_title_and_content?title=발생&content=Merry
   * @param 
   * @return
   */
  @GetMapping(path = "/find_by_title_and_content")
  public List<ISSUE> find_by_title_and_content(@RequestParam("title") String title, 
                                                                  @RequestParam("content") String content) {
    return service.find_by_title_and_content(title, content);
  }
  
  /**
   * title 대소문자 무시 검색
   * http://localhost:9100/issue/find_by_title_ignorecase?title=merry
   * @param 
   * @return
   */
  @GetMapping(path = "/find_by_title_ignorecase")
  public List<ISSUE> find_by_title_ignorecase(@RequestParam("title") String title) { 
    return service.find_by_title_ignorecase(title);
  }
  
  /**
   * 기간을 적용한 검색
   * http://localhost:9100/issue/find_by_rdate_period?start_date=2023-12-23&end_date=2023-12-23
   * @param start_date
   * @param end_date
   * @return
   */
  @GetMapping(path = "/find_by_rdate_period")
  public List<ISSUE> find_by_rdate_period(@RequestParam("start_date") String start_date, 
                                                            @RequestParam("end_date")   String end_date) {
    return service.find_by_rdate_period(start_date, end_date);
  }
  
  /**
   * rdate 내림 차순 정렬
   * http://localhost:9100/issue/find_all_by_order_by_rdate_desc
   * [
    {
        "issueno": 22,
        "title": "시스템 부하 발생",
        "content": "현재 조치중입니다.\n예상 소요 시간: 30분",
        "cnt": 0,
        "rdate": "2024-06-25 14:40:4"
    },
    {
        "issueno": 7,
        "title": "우분투 OS 지원",
        "content": "우분투 OS 22 버전 지원 시작",
        "cnt": 2,
        "rdate": "2024-06-17 04:31:16"
    }
    ]
   * @param start_date
   * @param end_date
   * @return
   */
  @GetMapping(path = "/find_all_by_order_by_rdate_desc")
  public List<ISSUE> find_all_by_order_by_rdate_desc() {
    return service.find_all_by_order_by_rdate_desc();
  }
  
  /**
   * 기간을 적용한 검색, 내림 차순
   * http://localhost:9100/issue/find_all_by_order_by_rdate_desc_period?start_date=2023-12-23&end_date=2023-12-23
   * @param start_date
   * @param end_date
   * @return
   */
  @GetMapping(path = "/find_all_by_order_by_rdate_desc_period")
  public List<ISSUE> find_all_by_order_by_rdate_desc_period(@RequestParam("start_date") String start_date, 
                                                                                      @RequestParam("end_date")   String end_date) {
    return service.find_all_by_order_by_rdate_desc_period(start_date, end_date);
  }
  
  /**
   * 조회, GET 요청을 처리하여 특정 ID를 가진 Entity 객체를 반환
   * 찾은 경우 객체 반환, 찾지 못한 경우 404 반환
   * http://localhost:9100/issue/12
   * @param id
   * @return
   */
  @GetMapping(path = "/{id}")
  public ResponseEntity<ISSUE> find_by_id(@PathVariable("id") Long id) {
      System.out.println("-> service.find_by_id(id): " + service.find_by_id(id));
      System.out.println("-> service.find_by_id(id).map(ResponseEntity::ok): " + service.find_by_id(id).map(ResponseEntity::ok));
      // result -> ResponseEntity.ok(result): result 파라미터를 ok 메소드로 전달, 람다식
      
      // 조회수 증가
      service.increaseCnt(id);
      
      // type 1
      return service.find_by_id(id).map(result -> ResponseEntity.ok(result)).orElseGet(() -> ResponseEntity.notFound().build());
      
      // type 2
//      return service.find_by_id(id).map(existingEntity -> {
//        existingEntity.setTitle(convertChar(existingEntity.getTitle()));
//        existingEntity.setContent(convertChar(existingEntity.getContent()));
//        return ResponseEntity.ok(existingEntity);
//      }).orElseGet(() -> ResponseEntity.notFound().build()); // 찾지 못한 경우 404 반환
  }
  
 
  /**
   * 수정
   * PUT 요청을 처리하여 특정 ID를 가진 Entity 객체를 업데이트
   * http://localhost:9100/issue/12
   * @param id
   * @param entity
   * @return
   */
  @PutMapping(path = "/{id}")
  public ResponseEntity<ISSUE> updateEntity(@PathVariable("id") Long id, @RequestBody ISSUE entity) {
    // id를 이용한 레코드 조회 -> existingEntity 객체에 할당 -> {} 실행 값 저장 -> DBMS 저장 -> 상태 코드 200 출력
    return service.find_by_id(id).map(existingEntity -> {
      existingEntity.setTitle(entity.getTitle());
      existingEntity.setContent(entity.getContent());
      
      return ResponseEntity.ok(service.saveEntity(existingEntity));
    }).orElseGet(() -> ResponseEntity.notFound().build()); // 찾지 못한 경우 404 반환
  }

  /**
   * DELETE 요청을 처리하여 특정 ID를 가진 Entity 객체를 삭제
   * http://localhost:9100/issue/12
   * @param id
   * @return
   */
  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Void> deleteEntity(@PathVariable("id") Long id) {
    if (service.find_by_id(id).isPresent()) { // Entity가 존재하면
      service.deleteEntity(id); // 삭제
      return ResponseEntity.ok().build(); // 성공적으로 삭제된 경우 200 반환
    } else {
      return ResponseEntity.notFound().build(); // 찾지 못한 경우 404 반환
    }
  }
  
}

