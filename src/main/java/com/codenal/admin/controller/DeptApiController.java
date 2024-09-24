package com.codenal.admin.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.admin.domain.DepartmentsDto;
import com.codenal.admin.service.DeptService;
import com.codenal.employee.domain.EmployeeDto;

@Controller
public class DeptApiController {

	private final DeptService deptService;

	public DeptApiController(DeptService deptService) {
		this.deptService = deptService;
	}

	/*
	 * @PostMapping("/api/addDepartments") public ResponseEntity<?>
	 * addDepartment(@RequestBody DepartmentsDto departmentsDto) { try {
	 * deptService.addDepartment(departmentsDto); return
	 * ResponseEntity.ok().body(Collections.singletonMap("success", true)); } catch
	 * (IllegalArgumentException e) { return
	 * ResponseEntity.status(HttpStatus.BAD_REQUEST)
	 * .body(Collections.singletonMap("message", e.getMessage())); } catch
	 * (Exception e) { e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	 * .body(Collections.singletonMap("success", false)); } }
	 */

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

}