package com.codenal.approval.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApprovalController {
	
	@GetMapping("/approval/create")
	public String createApproval() {
		return "apps/approval/approval_create";
	}
	
	@GetMapping("/approval/list")
	public String listApproval() {
		return "apps/approval/approval_list";
	}
}
