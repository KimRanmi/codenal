package com.codenal.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.admin.domain.Departments;
import com.codenal.admin.domain.DepartmentsDto;
import com.codenal.admin.service.DeptService;

@Controller
public class DeptApiController {

	private final DeptService deptService;

	public DeptApiController(DeptService deptService) {
		this.deptService = deptService;
	}

	// 부서 추가
	@ResponseBody
	@PostMapping("/api/addDepartments")
	public Map<String, String> addDepartment(@RequestBody DepartmentsDto dto) {
		Map<String, String> resultMap = new HashMap<>();
		try {
			int result = deptService.addDepartment(dto);
			if (result > 0) {
				resultMap.put("res_code", "200");
				resultMap.put("res_msg", "부서 추가가 성공적으로 등록되었습니다.");
			} else {
				resultMap.put("res_code", "404");
				resultMap.put("res_msg", "이미 존재하는 부서명입니다.");
			}
		} catch (Exception e) {
			resultMap.put("res_code", "500");
			resultMap.put("res_msg", "추가 중 오류가 발생했습니다: " + e.getMessage());
		}

		return resultMap;
	}
	
	
	// 부서명 수정
	@PostMapping("/api/editDepartment")
	public ResponseEntity<String> editDepartment(@RequestBody DepartmentsDto departmentsDto) {
	    System.out.println("Received DTO: " + departmentsDto); // 디버깅용

	    try {
	        // 부서 수정 로직 호출
	        deptService.editDepartment(departmentsDto);
	        return ResponseEntity.ok("부서 수정이 완료되었습니다.");
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    }
	}



}