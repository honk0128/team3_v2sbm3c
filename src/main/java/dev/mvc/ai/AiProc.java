package dev.mvc.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dev.mvc.ai.AiProc")
public class AiProc implements AiProcInter {
    @Autowired
    
    private AiDAOInter AiDAO;
    
    public AiProc() {
        // System.out.println("-> AiProc created.");  
    }
    
    @Override
    public int create(AiVO aiVO) {
        int cnt = this.AiDAO.create(aiVO);
        return cnt;
    }
}
