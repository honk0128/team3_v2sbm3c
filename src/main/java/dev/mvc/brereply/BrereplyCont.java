package dev.mvc.brereply;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.account.AccountProcInter;
import dev.mvc.breply.BreplyProcInter;
import dev.mvc.breply.BreplyVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RequestMapping("/brereply")
@Controller
public class BrereplyCont {
  @Autowired
  @Qualifier("dev.mvc.brereply.BrereplyProc")
  private BrereplyProcInter brereplyProc;

  @Autowired
  @Qualifier("dev.mvc.breply.BreplyProc")
  private BreplyProcInter breplyProc;

  @Autowired
  @Qualifier("dev.mvc.account.AccountProc")
  private AccountProcInter AccountProc;

  public BrereplyCont() {
    System.out.println("-> BreplyCont created.");
  }

  /**
   * 등록 폼
   * @param model
   * @param brereplyVO
   * @return
   */
  @GetMapping("/brereply_create")
  public String replycreate(Model model, BrereplyVO brereplyVO,
                            Integer breplyno, Integer accountno) {
    BreplyVO breplyVO = this.breplyProc.read(breplyno); 
    System.out.println("breplyVO : " + breplyVO);
    return "/brereply/brereply_create";
  }


  /**
   * 등록 프로세스
   */
  @PostMapping(value = "/brereply_create")
  public String brereply_create(Model model, @Valid BrereplyVO brereplyVO, Integer breplyno, BindingResult bindingResult, HttpSession session,
                            RedirectAttributes ra) {
    if (bindingResult.hasErrors()) {
      return "redirect:/breply/read/{breplyno}";
    }

    if (session.getAttribute("accountno") != null || session.getAttribute("managerno") != null) { // 회원 로그인
      // ------------------------------------------------------------------------------
      // 파일 전송 코드 시작
      // ------------------------------------------------------------------------------
      String file1 = "";
      String file1saved = "";
      String thumb1 = "";

      String upDir =  Brereply.getUploadDir();
      System.out.println("-> upDir: " + upDir);
      
      MultipartFile mf = brereplyVO.getFileMF();
      
      file1 = mf.getOriginalFilename();
      System.out.println("-> 원본 파일명 산출 file1: " + file1);
      
      long size1 = mf.getSize();
      if (size1 > 0) {
        if (Tool.checkUploadFile(file1) == true) {
          file1saved = Upload.saveFileSpring(mf, upDir); 
          
          if (Tool.isImage(file1saved)) {
            thumb1 = Tool.preview(upDir, file1saved, 200, 150);
          }

          brereplyVO.setBrereplyimg(file1);
          brereplyVO.setBrereplysave(file1saved);
          brereplyVO.setBrereplythumb(thumb1);
          brereplyVO.setBrereplysize(size1);

        } else {
          ra.addFlashAttribute("code", "check_upload_file_fail");
          ra.addFlashAttribute("cnt", 0);
          ra.addFlashAttribute("url", "/Breply/msg");
          return "redirect:/Breply/msg";
        }
      } else {
        System.out.println("-> 글만 등록");
      }
      
      // ------------------------------------------------------------------------------
      // 파일 전송 코드 종료
      // ------------------------------------------------------------------------------

      int cnt = this.brereplyProc.brereply_create(brereplyVO);
      System.out.println("-> cnt: " + cnt);

      model.addAttribute("cnt", cnt);
      if(cnt ==1) {
        System.out.println("breplyno: " + breplyno);
        return "redirect:/brereply/brereply_list";
      } else {
        model.addAttribute("code", "code");
        return "breply/msg";
      }
    } else {
      return "redirect:/account/login";
    }
  }

  /**
   * 대댓글 목록
   * @param model
   * @param brereplyVO
   * @return
   */
  @GetMapping(value = "/brereply_list")
  public String brereply_list(Model model, BrereplyVO brereplyVO) {
    ArrayList<BrereplyVO> list = this.brereplyProc.brereply_list();
    model.addAttribute("list", list);

    return "brereply/brereply_list";
  }

  /**
   * 대댓글 조회
   * @param model
   * @param brereplyno
   * @return
   */
  @GetMapping(value = "/brereply_read")
  public String brereply_read(Model model, int brereplyno) {

    BrereplyVO brereplyVO = this.brereplyProc.brereply_read(brereplyno);

    long size = brereplyVO.getBrereplysize();
    String size_label = Tool.unit(size);
    brereplyVO.setSize_label(size_label);

    model.addAttribute("brereplyVO", brereplyVO);
    
    return "brereply/brereply_read";
  }
  
  
  @GetMapping(value = "/brereply_update")
  public String brereply_update(Model model, Integer brereplyno) {
    BrereplyVO brereplyVO = this.brereplyProc.brereply_read(brereplyno);
    model.addAttribute("brereplyVO", brereplyVO);

    System.out.println("cont: " + brereplyVO.getBrereplycont());;

    return "brereply/brereply_update";
  }

  @PostMapping(value="/brereply_update")
  public String update_process(Model model, BrereplyVO brereplyVO, Integer brereplyno, RedirectAttributes ra, HttpSession session) {
    BrereplyVO brereplyVO_old = brereplyProc.brereply_read(brereplyVO.getBrereplyno());

    if (session.getAttribute("accountno") != null || session.getAttribute("managerno") != null) { // 회원 로그인
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("brereplyno", brereplyVO.getBrereplyno());
      map.put("brereplypasswd", brereplyVO.getBrereplypasswd());

      int cnt = this.breplyProc.password_check(map);
      System.out.println("cnt: " + cnt);

      if (cnt == 1) { // 비밀번호 확인
        
        // -------------------------------------------------------------------
        // 파일 삭제 시작
        // -------------------------------------------------------------------
        String file1saved = brereplyVO_old.getBrereplysave();  // 실제 저장된 파일명
        String thumb1 = brereplyVO_old.getBrereplythumb();       // 실제 저장된 preview 이미지 파일명
        long size1 = 0;
          
        String upDir =  Brereply.getUploadDir(); // C:/kd/deploy/resort_v4sbm3c/contents/storage/
        
        Tool.deleteFile(upDir, file1saved);  // 실제 저장된 파일삭제
        Tool.deleteFile(upDir, thumb1);     // preview 이미지 삭제
        // -------------------------------------------------------------------
        // 파일 삭제 종료
        // -------------------------------------------------------------------
            
        // -------------------------------------------------------------------
        // 파일 전송 시작
        // -------------------------------------------------------------------
        String file1 = "";          // 원본 파일명 image

        // 전송 파일이 없어도 file1MF 객체가 생성됨.
        // <input type='file' class="form-control" name='file1MF' id='file1MF' 
        //           value='' placeholder="파일 선택">
        MultipartFile mf = brereplyVO.getFileMF();
            
        file1 = mf.getOriginalFilename(); // 원본 파일명
        size1 = mf.getSize();  // 파일 크기
            
        if (size1 > 0) { // 폼에서 새롭게 올리는 파일이 있는지 파일 크기로 체크 ★
          // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
          file1saved = Upload.saveFileSpring(mf, upDir); 
          
          if (Tool.isImage(file1saved)) { // 이미지인지 검사
            // thumb 이미지 생성후 파일명 리턴됨, width: 250, height: 200
            thumb1 = Tool.preview(upDir, file1saved, 250, 200); 
          }
          
        } else { // 파일이 삭제만 되고 새로 올리지 않는 경우
          file1="";
          file1saved="";
          thumb1="";
          size1=0;
        }
            
        brereplyVO.setBrereplyimg(file1);
        brereplyVO.setBrereplysave(file1saved);
        brereplyVO.setBrereplythumb(thumb1);
        brereplyVO.setBrereplysize(size1);
        // -------------------------------------------------------------------
        // 파일 전송 코드 종료
        // -------------------------------------------------------------------
        model.addAttribute("brereplyVO", brereplyVO);
        System.out.println("cont: "+brereplyVO.getBrereplycont());
        
        cnt = this.brereplyProc.brereply_update(brereplyVO); // 수정
        System.out.println("cnt: " + cnt);
        
        if (cnt == 1) {
          model.addAttribute("code", "update_success");
          model.addAttribute("breplycont", brereplyVO.getBrereplycont());
          model.addAttribute("breplypasswd", brereplyVO.getBrereplypasswd());

          ra.addAttribute("breplyno", brereplyVO.getBrereplyno());
          
          return "redirect:/brereply/brereply_list?brereplyno=" + brereplyno; // request -> param으로 접근 전환
        } else {
          model.addAttribute("code", "update_fail");
        }
        
        model.addAttribute("cnt", cnt);
        
        return "member/msg"; // /templates/member/msg.html
      } else {
        ra.addFlashAttribute("code", "not_exist_passwd");
        return "redirect:/brereply/msg";
      }
    } else {
      return "redirect:/account/login";
    }
  }
  
  @GetMapping(value = "/brereply_delete")
  public String brereply_delete(Model model, Integer brereplyno) {
    
    BrereplyVO brereplyVO = this.brereplyProc.brereply_read(brereplyno);
    model.addAttribute("brereplyVO", brereplyVO);
    return "brereply/brereply_delete";
  }
  
  @PostMapping(value = "/brereply_delete")
    public String delete_process(Model model, HttpSession session,
                        Integer brereplyno) {
    BrereplyVO brereplyVO = this.brereplyProc.brereply_read(brereplyno);
    model.addAttribute("brereplyVO", brereplyVO);

    if (session.getAttribute("accountno") != null || session.getAttribute("managerno") != null) { // 회원 로그인
      int cnt = this.brereplyProc.brereply_delete(brereplyno);
      System.out.println("cnt : " + cnt);
      if(cnt == 1) {
        return "redirect:/brereply/brereply_list?brereplyno=" + brereplyno;
      } else {
        model.addAttribute("code", "delete_fail");
          return "breply/msg";
      }
    } else {
      return "redirect:/account/login";
    }
  }
}