package com.codenal.approval.controller;


import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.codenal.approval.domain.ApprovalBaseFormType;
import com.codenal.approval.service.ApprovalService;
import com.codenal.employee.domain.Employee;
import com.codenal.ref.Authentication;
import com.codenal.security.service.SecurityService;

@Controller
public class ApprovalViewController {
	
	private final ApprovalService approvalService;
	private final SecurityService securityService;
	
	@Autowired
	public ApprovalViewController(ApprovalService approvalService, SecurityService securityService) {
		this.approvalService = approvalService;
		this.securityService = securityService;
	}
	
	
	
	// 생성 nav에서 숫자값을 받아와서 create로 전달(휴가신청서는 따로 관리)
	@GetMapping("/approval/create/{no}")
	public String createApproval(Model model, @PathVariable("no")int no) {
		LocalDate ldt = LocalDate.now();
		/*
		 * Authentication authentication =
		 * SecurityContextHolder.getContext().getAuthentication(); Employee emp =
		 * (Employee)authentication.getPrincipal(); String memId = emp.getUsername();
		 */
		model.addAttribute("ldt",ldt);
		model.addAttribute("no",no);
		return "apps/approval_create";
	}
	
	// 휴가신청서로 이동
	@GetMapping("/approval/leave_create")
	public String createLeaveApproval(Model model) {
		LocalDate ldt = LocalDate.now();
		model.addAttribute("ldt",ldt);
		return "apps/approval_leave_create";
	} 
	
	// 리스트 조회 (수정해야함)
	@GetMapping("/approval/list")
	public String listApproval(Model model,
	        @PageableDefault(page=0, size=10, sort="approvalNo",
	        direction=Sort.Direction.DESC) Pageable pageable) {
	    Page<Map<String, Object>> resultList = approvalService.selectApprovalList(pageable);
	    model.addAttribute("resultList", resultList);
	    return "apps/approval_list";
	}

	
	// 상세 조회
	@GetMapping("/approval/{approval_no}")
	public String selectApprovalOne(Model model,@PathVariable("approval_no")Long approval_no) {
		String returnResult = null;
		Map<String, Object> resultList = approvalService.detailApproval(approval_no);
		
		ApprovalBaseFormType af = (ApprovalBaseFormType) resultList.get("baseForm");
		int typeInt = af.getBaseFormCode();
		
		if(typeInt == 1) {
			returnResult = "approval_leave_detail";
		}else {
			returnResult = "approval_detail";
		}
		model.addAttribute("dto",resultList);
		model.addAttribute("type",typeInt);
		return "apps/"+returnResult;
	}
}
