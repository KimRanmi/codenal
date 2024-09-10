package com.codenal.announce.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.codenal.announce.domain.Announce;
import com.codenal.announce.domain.AnnounceDto;
import com.codenal.announce.domain.AnnounceFileDto;
import com.codenal.announce.service.AnnounceService;
import com.codenal.announce.service.FileService;

@Controller
public class AnnounceApiController {
	
	private final AnnounceService announceService;
	private final FileService fileService;
	
	@Autowired
	public AnnounceApiController(AnnounceService announceService, FileService fileService) {
		this.announceService = announceService;
		this.fileService = fileService;
	}
	
	
	
	@ResponseBody
	@PostMapping("/announce/createEnd")
	public Map<String, String> createAnnounce(AnnounceDto dto,
            @RequestParam("file") MultipartFile file) {
		
	    Map<String, String> resultMap = new HashMap<>();
	    
	    try {
	        // 파일 업로드 처리
	        String savedFileName = fileService.upload(file);
	        if (savedFileName == null) {
	            resultMap.put("res_code", "500");
	            resultMap.put("res_msg", "파일 업로드에 실패했습니다.");
	            return resultMap;
	        }
	        
	        // AnnounceFileDto 생성
	        AnnounceFileDto fileDto = new AnnounceFileDto();
	        fileDto.setFile_ori_name(file.getOriginalFilename());
	        fileDto.setFile_new_name(savedFileName);
	        fileDto.setFile_path("C:\\codenal\\announce\\upload\\");
	        
	        // Announce 및 AnnounceFile 저장
	        Announce savedAnnounce = announceService.createAnnounce(dto, fileDto);
	        if (savedAnnounce == null) {
	            resultMap.put("res_code", "500");
	            resultMap.put("res_msg", "Announce 저장에 실패했습니다.");
	            return resultMap;
	        }
	        
	        // 성공 응답
	        resultMap.put("res_code", "200");
	        resultMap.put("res_msg", "성공적으로 게시글을 작성했습니다.");
	        resultMap.put("announceNo", savedAnnounce.getAnnounceNo().toString()); // Long을 String으로 변환하여 추가
	    } catch (Exception e) {
	        resultMap.put("res_code", "500");
	        resultMap.put("res_msg", "서버 오류: " + e.getMessage());
	        e.printStackTrace();
	    }
	    
	    return resultMap;
	}
	
	
	
	@ResponseBody
	@DeleteMapping("/announce/delete/{announceNo}")
	public Map<String,String> deleteAnnounce(@PathVariable("announceNo") Long announceNo, Model model){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
        
		Map<String,String> map = new HashMap<String,String>();
		map.put("res_code", "404");
		map.put("res_msg", "게시글 삭제 중 오류가 발생했습니다.");
		if(announceService.selectAnnounceDetail(announceNo).getAnnounceFile() != null) {
			if(fileService.deletefile(announceNo) > 0) {
				map.put("res_msg", "기존 파일이 정상적으로 삭제되었습니다.");
					if(announceService.deleteAnnounce(announceNo) > 0) {
						map.put("res_code", "200");
						map.put("res_msg", "정상적으로 게시글이 삭제되었습니다.");
					}	
				}
		} else {
			if(announceService.deleteAnnounce(announceNo) > 0) {
				map.put("res_code", "200");
				map.put("res_msg", "정상적으로 게시글이 삭제되었습니다.");
			}
		}
		return map;
	}
	
}
