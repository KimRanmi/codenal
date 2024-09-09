package com.codenal.announce.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
		
		// dto 파싱 후 resultMap에 코드 담아서 보내는 작업 
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "생성중 오류가 발생하였습니다.");
		
		String savedFileName = fileService.upload(file);
		if(savedFileName != null) {
			AnnounceFileDto fileDto = new AnnounceFileDto();
			fileDto.setFile_ori_name(file.getOriginalFilename());
			fileDto.setFile_new_name(savedFileName);
			fileDto.setFile_path("C:\\codenal\\announce\\upload\\");
			if(announceService.createAnnounce(dto, fileDto) != null) {
				resultMap.put("res_code", "200");
				resultMap.put("res_msg", "성공적으로 게시글을 작성했습니다.");
			}
		} else {
			resultMap.put("res_msg", "파일 업로드에 실패했습니다.");
		}
		return resultMap;
	}
	
	
	
}
