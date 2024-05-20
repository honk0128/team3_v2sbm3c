package dev.mvc.ai;

import java.util.ArrayList;
import jakarta.servlet.http.HttpServletRequest;  // 변경된 부분
import jakarta.servlet.http.HttpSession;        // 변경된 부분

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import dev.mvc.contents.Contents;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;

@RequestMapping("/ai")
@Controller
public class AiCont {

  @Autowired
  @Qualifier("dev.mvc.ai.AiProc")
  private AiProcInter aiProc;

  public AiCont() {
    System.out.println("-> AiCont created.");
  }

  // create 폼 출력
  @RequestMapping(value="/create", method=RequestMethod.GET) // http://localhost:9091/ai/create
  public String create(Model model, AiVO aiVO) {
    return "ai/create"; // /templates/ai/create.html
  }
  
  // create 폼 출력
  @RequestMapping(value="/msg", method=RequestMethod.GET) // http://localhost:9091/ai/create
  public String msg(Model model, AiVO aiVO) {
    return "ai/msg"; // /templates/ai/create.html
  }

  @RequestMapping(value="/msg.do", method=RequestMethod.GET)
  public ModelAndView msg(String url){
    ModelAndView mav = new ModelAndView();
    mav.setViewName(url); // forward
    return mav; // forward
  }

  /**
   * 등록 처리 http://localhost:9091/ai/create.do
   * 
   * @return
   */
  @RequestMapping(value = "/create.do", method = RequestMethod.POST)
  public ModelAndView create(HttpSession session, @ModelAttribute AiVO aiVO, @RequestParam("file1MF") MultipartFile file1MF) {
    ModelAndView mav = new ModelAndView();
    
    String text = "";
    String file1 = "";          // 원본 파일명
    String file1saved = "";     // 저장된 파일명
    String thumb1 = "";         // preview image

    String upDir =  Contents.getUploadDir(); // 파일을 업로드할 폴더 경로
    System.out.println("-> upDir: " + upDir);
    
    file1 = file1MF.getOriginalFilename(); // 원본 파일명
    System.out.println("-> 원본 파일명: " + file1);
    
    if (Tool.checkUploadFile(file1)) { // 업로드 가능한 파일인지 검사
      long size1 = file1MF.getSize();  // 파일 크기
      
      if (size1 > 0) { // 파일 크기 체크
        // 파일 저장 후 업로드된 파일명이 리턴됨
        file1saved = Upload.saveFileSpring(file1MF, upDir); 
        
        if (Tool.isImage(file1saved)) { // 이미지 파일인지 검사
          // thumb 이미지 생성 후 파일명 리턴
          thumb1 = Tool.preview(upDir, file1saved, 200, 150); 
        }
      }
      
      aiVO.setImg_search(file1);   // 원본 파일명 설정
      aiVO.setText_search(text);
      aiVO.setImg_search_save(file1saved); // 저장된 파일명 설정
      aiVO.setImg_search_thumb(thumb1);      // 축소판 이미지 설정
      aiVO.setImg_search_size((int)size1);  // 파일 크기 설정
      
//      int adminno = (int) session.getAttribute("adminno"); // 세션에서 adminno 가져오기
//      aiVO.setAccountno(adminno);
      int cnt = aiProc.create(aiVO); // 데이터베이스에 저장
      
      if (cnt == 1) {
          mav.addObject("code", "create_success");
      } else {
          mav.addObject("code", "create_fail");
      }
      mav.addObject("cnt", cnt); // 처리 결과 추가
      mav.addObject("accountno", aiVO.getAccountno()); // 계정 번호 추가
      mav.addObject("url", "/ai/msg"); // 메시지 URL 추가
      mav.setViewName("redirect:/ai/msg"); // 리다이렉트 설정
    } else {
      mav.addObject("cnt", "0"); // 업로드 실패 처리
      mav.addObject("code", "check_upload_file_fail"); // 실패 코드 추가
      mav.addObject("url", "/ai/msg"); // 메시지 URL 추가
      mav.setViewName("redirect:/ai/msg"); // 리다이렉트 설정
    }
    return mav; // 결과 반환
  }
}