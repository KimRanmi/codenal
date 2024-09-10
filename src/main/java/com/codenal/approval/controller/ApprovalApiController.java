package com.codenal.approval.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.approval.service.ApprovalService;
import com.codenal.employee.service.EmployeeService;

@Controller
public class ApprovalApiController {

	private final ApprovalService approvalService;
	private final EmployeeService employeeService;
	
	@Autowired
	public ApprovalApiController(ApprovalService approvalService,EmployeeService employeeService) {
		this.approvalService = approvalService;
		this.employeeService = employeeService;
	}
	
	
	// 등록
	@ResponseBody
	@PostMapping("/approval")
	public Map<String,String> createApproval(
			@RequestPart("approval_content") String approvalContent,
            @RequestParam("approval_title") String approvalTitle,
            @RequestParam("emp_id") String empId,
            @RequestParam("approval_reg_date") String approvalRegDate,
            @RequestParam("form_code") String formCode){
		Map<String,String> resultMap = new HashMap<String,String>();
		System.out.println("시작");
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "전자결재 등록 중 오류가 발생했습니다.");
		
		// 타입 형변환
		Long emp_id = Long.parseLong(empId);
		Integer form_code = Integer.parseInt(formCode);
		
		Map<String,Object> list = new HashMap<String,Object>();
		list.put("제목", approvalTitle);
		list.put("내용", approvalContent);
		list.put("이름", emp_id);
		list.put("폼코드", form_code);
		
		
		 if(approvalService.createApproval(list) != null) {
		 resultMap.put("res_code","200"); resultMap.put("res_msg", "전자결재 등록 성공"); }
		 
		return resultMap;
	}
	
	// 전자결재 휴가신청서 등록
	@ResponseBody
	@PostMapping("/approval_leave")
	public Map<String,String> createApprovalLeave(
			@RequestPart("approval_content") String approvalContent,
            @RequestParam("approval_title") String approvalTitle,
            @RequestParam("emp_id") String empId,
            @RequestParam("approval_reg_date") String approvalRegDate,
            @RequestParam("start_date") LocalDate startDate,
            @RequestParam("end_date") LocalDate endDate,
            @RequestParam("form_code") String formCode){
		Map<String,String> resultMap = new HashMap<String,String>();
		System.out.println("시작");
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "전자결재 등록 중 오류가 발생했습니다.");
		
		// 타입 형변환
		Long emp_id = Long.parseLong(empId);
		Integer form_code = Integer.parseInt(formCode);
		
		Map<String,Object> list = new HashMap<String,Object>();
		list.put("제목", approvalTitle);
		list.put("내용", approvalContent);
		list.put("이름", emp_id);
		list.put("폼코드", form_code);
		list.put("시작일자",startDate);
		list.put("종료일자",endDate);
		
		
		 if(approvalService.createApprovalLeave(list) != null) {
		 resultMap.put("res_code","200"); resultMap.put("res_msg", "전자결재 등록 성공"); }
		 
		return resultMap;
	}
}
