package com.codenal.approval.controller;


import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.codenal.approval.domain.ApprovalDto;
import com.codenal.approval.service.ApprovalService;

@Controller
public class ApprovalViewController {
	
	private final ApprovalService approvalService;
	private static final Logger LOGGER = LoggerFactory.getLogger(ApprovalViewController.class);
	
	@Autowired
	public ApprovalViewController(ApprovalService approvalService) {
		this.approvalService = approvalService;
	}
	
	// 생성 nav에서 숫자값을 받아와서 create로 전달
	@GetMapping("/approval/create/{no}")
	public String createApproval(Model model, @PathVariable("no")int no) {
		LocalDate ldt = LocalDate.now();
		model.addAttribute("ldt",ldt);
		model.addAttribute("no",no);
		return "apps/approval_create";
	}
	
	// 리스트 조회 (수정해야함)
	@GetMapping("/approval/list")
	public String listApproval(Model model,
			@PageableDefault(page=0,size=10, sort="approvalNo",
			direction=Sort.Direction.DESC) Pageable pageable,
			ApprovalDto approvalDto){
		Page<ApprovalDto> resultList = approvalService.selectApprovalList(pageable);
		LOGGER.debug(resultList.toString());
		System.out.println(resultList);
		model.addAttribute("resultList",resultList);
		return "apps/approval_list";
	}
	
	// 상세 조회
	@GetMapping("/approval/{approval_no}")
	public String selectApprovalOne(Model model,@PathVariable("approval_no")Long approval_no) {
		ApprovalDto dto = approvalService.selectApprovalNo(approval_no);
		model.addAttribute("dto",dto);
		return "apps/approval_detail";
	}
}
