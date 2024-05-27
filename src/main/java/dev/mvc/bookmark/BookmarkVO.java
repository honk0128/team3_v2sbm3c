package dev.mvc.bookmark;

import lombok.Getter;
import lombok.Setter;


@Setter @Getter
public class BookmarkVO {

  private int bookmarkno;

  private int accountno;

  private int boardno;

  private String bookmarkurl;
  
  private String bmdate;
}
