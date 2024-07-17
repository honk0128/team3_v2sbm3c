package dev.jpa.issue.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// domain 클래스
@Entity @Getter @Setter @ToString
public class ISSUE {
  /**
   * 식별자, sequence 자동 생성됨.
   * @Id: Primary Key
   */
  @Id
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="issue_seq")
  @SequenceGenerator(name="issue_seq", sequenceName="ISSUE_SEQ", allocationSize=1)
  private long issueno;
  
  /** 사건, 사고 제목 */
  private String title;
  
  /** 사건, 사고 내용 */
  private String content;
  
  /** 조회수 */
  private int cnt;

  /** 등록 날짜, Timestamp는 검색이 너무 불편함 */
  private String rdate;
  
  public ISSUE() {

  } 

  /**
   * 사용자로부터 입력받는 필드만 명시
   * @param title
   * @param content
   */
  public ISSUE(String title, String content, int cnt, String rdate) {
    this.title = title;
    this.content = content;
    this.cnt = cnt;
    this.rdate = rdate;
  }
  
}

