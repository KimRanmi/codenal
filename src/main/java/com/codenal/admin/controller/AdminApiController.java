package com.codenal.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.admin.service.AdminService;
import com.codenal.employee.domain.EmployeeDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class AdminApiController {

	private final AdminService adminService;

	@Autowired
	public AdminApiController(AdminService adminService) {
		this.adminService = adminService;
	}
 
	// 신규 직원 등록
	@ResponseBody 
	@PostMapping("/admin/join")
	public Map<String,String> joinEmployee(@RequestBody EmployeeDto dto) {
		System.out.println("Received DTO: " + dto);
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "등록 중 오류가 발생했습니다.");

		if(adminService.createEmployee(dto) > 0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "성공적으로 등록되었습니다.");
		};

		return resultMap;
	}
	
	
	
}

