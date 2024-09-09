package com.codenal.approval.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.approval.domain.ApprovalDto;
import com.codenal.approval.service.ApprovalService;

@Controller
public class ApprovalApiController {

	private final ApprovalService approvalService;
	
	@Autowired
	public ApprovalApiController(ApprovalService approvalService) {
		this.approvalService = approvalService;
	}
	
	@ResponseBody
	@PostMapping("/approval")
	public Map<String,String> createApproval(ApprovalDto dto){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "전자결재 등록 중 오류가 발생했습니다.");
		
		if(approvalService.createApproval(dto) != null) {
			resultMap.put("res_code","200");
			resultMap.put("res_msg", "전자결재 등록 성공");
		}
		return resultMap;
	}
}
