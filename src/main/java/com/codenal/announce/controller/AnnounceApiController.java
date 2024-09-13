package com.codenal.announce.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.announce.domain.AnnounceDto;

@Controller
public class AnnounceApiController {
	
	@ResponseBody
	@PostMapping("/announce/createEnd")
	public Map<String,String> createAnnounce(@RequestBody AnnounceDto dto){
		// dto 파싱 후 resultMap에 코드 담아서 보내는 작업 
		Map<String,String> resultMap = new HashMap<String,String>();
//		resultMap.put("res_code", "404");
//		resultMap.put("res_msg", "생성중 오류가 발생하였습니다.");
		
//		if(chatService.createChatRoom(dto)>0) {
//			resultMap.put("res_code", "200");
//			resultMap.put("res_msg", "성공적으로 생성이 완료되었습니다.");
//		}
		System.out.println(dto);
		
		return resultMap;
	}
	
}
