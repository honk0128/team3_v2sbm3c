package dev.mvc.breply;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.crudcate.CrudcateProcInter;
import dev.mvc.crudcate.CrudcateVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@RequestMapping("/breply")
@Controller
public class BreplyCont {
  @Autowired
  @Qualifier("dev.mvc.breply.BreplyProc")
  private BreplyProcInter breplyProc;

  @Autowired
  @Qualifier("dev.mvc.crudcate.CrudcateProc")
  private CrudcateProcInter crudcateProc;

  public BreplyCont() {
    System.out.println("-> BreplyCont created.");
  }

  /**
   * 등록 폼
   * @param model
   * @param breplyVO
   * @return
   */
  @GetMapping("/create")
  public String replycreate(Model model, BreplyVO breplyVO) {
      return "/breply/create";
  }

  /**
   * 등록 처리
   * @param model
   * @param breplyVO
   * @param bindingResult
   * @param ra
   * @param word
   * @param now_page
   * @return
   */
  @PostMapping(value = "/create")
  public String replycreate(Model model, @Valid BreplyVO breplyVO, BindingResult bindingResult,
                            RedirectAttributes ra,
                            @RequestParam(name="word", defaultValue = "") String word,
                            @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    if (bindingResult.hasErrors()) {
      return "breply/create";
    }

    // ------------------------------------------------------------------------------
    // 파일 전송 코드 시작
    // ------------------------------------------------------------------------------
    String file1 = "";
    String file1saved = "";
    String thumb1 = "";

    String upDir =  Breply.getUploadDir();
    System.out.println("-> upDir: " + upDir);
    
    MultipartFile mf = breplyVO.getFile1MF();
    
    file1 = mf.getOriginalFilename();
    System.out.println("-> 원본 파일명 산출 file1: " + file1);
    
    long size1 = mf.getSize();
    if (size1 > 0) {
      if (Tool.checkUploadFile(file1) == true) {
        file1saved = Upload.saveFileSpring(mf, upDir); 
        
        if (Tool.isImage(file1saved)) {
          thumb1 = Tool.preview(upDir, file1saved, 200, 150);
        }

        breplyVO.setBreplyimg(file1);
        breplyVO.setBreplysaved(file1saved);
        breplyVO.setBreplythumb(thumb1);
        breplyVO.setBreplysize(size1);

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

    int cnt = this.breplyProc.replycreate(breplyVO);
    System.out.println("-> cnt: " + cnt);

    model.addAttribute("cnt", cnt);
    if(cnt ==1) {
      return "redirect:/breply/list";
    } else {
      model.addAttribute("code", "code");
      return "breply/msg";
    }
  }

  /**
   * 댓글 목록
   * @param model
   * @param breplyVO
   * @return
   */
  @GetMapping(value = "/list")
  public String reply_list(Model model, BreplyVO breplyVO) {
    ArrayList<BreplyVO> list = this.breplyProc.reply_list();
    model.addAttribute("list", list);

    return "breply/list";
  }

  /**
   * 댓글 조회
   */
  @GetMapping(value = "/read")
  public String read(Model model, int breplyno, String word, CrudcateVO crudcateVO,
                     @RequestParam(name="now_page", defaultValue="1") int now_page) {
    model.addAttribute("now_page", now_page);

    BreplyVO breplyVO = this.breplyProc.read(breplyno);

    long size = breplyVO.getBreplysize();
    String size_label = Tool.unit(size);
    breplyVO.setSize_label(size_label);

    word = Tool.checkNull(word).trim();

    model.addAttribute("breplyVO", breplyVO);

    model.addAttribute("crudcateVO", crudcateVO);

    model.addAttribute("word", word);
    
    return "breply/read";
  }
  
  /**
   * 업데이트
   */
  @GetMapping(value = "update_contents")
  public String update_contents(Model model,
                                HttpSession session,
                                @RequestParam(name="breplyno") Integer breplyno,
                                RedirectAttributes ra) {

    BreplyVO breplyVO = this.breplyProc.read(breplyno);
    model.addAttribute("breplyVO", breplyVO);

    return "breply/update_contents";
  }

  @PostMapping(value = "update_contents")
  public String update_contents(Model model,
                              HttpSession session,
                              BreplyVO breplyVO,
                              RedirectAttributes ra) {

    // 댓글 내용 업데이트
    int cnt = this.breplyProc.update_contents(breplyVO);
    System.out.println("cnt : " + cnt);

    // 비밀번호 확인을 위한 맵 생성
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("breplyno", breplyVO.getBreplyno());
    map.put("breplypasswd", breplyVO.getBreplypasswd());
    // System.out.println("-> map : " + map.put("breplypasswd", breplyVO.getBreplypasswd()));

    // 비밀번호 확인
    if(this.breplyProc.password_check(map) == 1) {
        // 비밀번호가 맞으면
        if(cnt == 1) {
            // 댓글 내용이 정상적으로 업데이트 되었을 때
            model.addAttribute("content", breplyVO.getBreplycont());
            return "redirect:/breply/list";
        } else {
            // 댓글 내용이 업데이트되지 않았을 때
            return null;
        }
    } else {
        // 비밀번호가 틀렸을 때
        ra.addAttribute("code", "passwd_fail");
        ra.addAttribute("cnt", 0);
        // ra.addAttribute("url", "breply/msg");

        return "redirect:/breply/msg";
    }
  }


  @PostMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, 
                            BreplyVO breplyVO,
                            String word, 
                            int now_page,
                            RedirectAttributes ra) {
    
    BreplyVO breplyVO_old = breplyProc.read(breplyVO.getBreplyno());
    
    String file1saved = breplyVO_old.getBreplyimg();
    String thumb1 = breplyVO_old.getBreplysaved();
    long size1 = 0;
        
    String upDir =  Breply.getUploadDir();
    
    Tool.deleteFile(upDir, file1saved);
    Tool.deleteFile(upDir, thumb1);
    
    String file1 = "";

    MultipartFile mf = breplyVO.getFile1MF();
        
    file1 = mf.getOriginalFilename();
    size1 = mf.getSize();
        
    if (size1 > 0) {
      file1saved = Upload.saveFileSpring(mf, upDir); 
      
      if (Tool.isImage(file1saved)) {
        thumb1 = Tool.preview(upDir, file1saved, 250, 200); 
      }
      
    } else {
      file1="";
      file1saved="";
      thumb1="";
      size1=0;
    }
        
    breplyVO.setBreplyimg(file1);
    breplyVO.setBreplysaved(file1saved);
    breplyVO.setBreplythumb(thumb1);
    breplyVO.setBreplysize(size1);
        
    this.breplyProc.update_img(breplyVO);

    ra.addAttribute("breplyno", breplyVO.getBreplyno());
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    return "redirect:/breply/read";
              
  }   
  
  @GetMapping(value = "/delete")
  public String delete(HttpSession session, Model model, RedirectAttributes ra, 
                       @RequestParam(name = "breplyno") int breplyno,
                       @RequestParam(name="word", defaultValue = "") String word,
                       @RequestParam(name="now_page", defaultValue="1") int now_page) {
    
    BreplyVO breplyVO = this.breplyProc.read(breplyno);
    System.out.println("breplyno : " + breplyno);
    model.addAttribute("BreplyVO", breplyVO);
    
    return "/breply/delete";
  }
  
  @PostMapping(value = "/delete")
  public String delete(Model model,
                      RedirectAttributes ra,
                      @RequestParam(name = "breplyno", defaultValue = "") int breplyno,
                      @RequestParam(name="word", defaultValue = "") String word,
                      @RequestParam(name="now_page", defaultValue="1") int now_page) {
  BreplyVO breplyVO = this.breplyProc.read(breplyno);
  System.out.println(breplyVO);
  System.out.println(breplyno);

  int cnt = this.breplyProc.delete(breplyno);
  model.addAttribute("cnt", cnt);
  System.out.println("cnt : " + cnt);
  if(cnt == 1) {
    return "redirect:/breply/list";
  } else {
    model.addAttribute("code", "delete_fail");
      return "breply/msg";
  }
    
    // BreplyVO BreplyVO_read = new BreplyVO();                    
    // BreplyVO_read = breplyProc.read(breplyno);
        
    // String file1saved = BreplyVO_read.getBreplysaved();
    // String thumb1 = BreplyVO_read.getBreplythumb();
    
    // String uploadDir = Breply.getUploadDir();
    // Tool.deleteFile(uploadDir, file1saved);
    // Tool.deleteFile(uploadDir, thumb1);
        
    // this.breplyProc.delete(breplyno);
    
    // HashMap<String, Object> hashMap = new HashMap<String, Object>();
    // hashMap.put("word", word);

    // ra.addAttribute("word", word);
    // ra.addAttribute("now_page", now_page);
    
    // return "redirect:/breply/list";
  }   
}