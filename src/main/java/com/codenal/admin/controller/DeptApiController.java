package com.codenal.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.admin.domain.DepartmentsDto;
import com.codenal.admin.service.DeptService;

@Controller
@RequestMapping("/api")
public class DeptApiController {

	private final DeptService deptService;

	public DeptApiController(DeptService deptService) {
		this.deptService = deptService;
	}

	// 부서 추가
	@ResponseBody
	@PostMapping("/addDepartments")
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


	// 부서 삭제
	  @DeleteMapping("/dept/{deptNo}")
	   public ResponseEntity<Map<String, String>> deleteDepartment(@PathVariable("deptNo") Long deptNo) {
	        Map<String, String> response = new HashMap<>();

	        try {
	            int result = deptService.deleteDept(deptNo);
	            if (result > 0) {
	                response.put("res_code", "200");
	                response.put("res_msg", "정상적으로 부서가 삭제되었습니다.");
	                return ResponseEntity.ok(response);
	            } else {
	                response.put("res_code", "400");
	                response.put("res_msg", "부서 삭제에 실패했습니다.");
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	            }
	        } catch (IllegalStateException e) {
	            response.put("res_code", "400");
	            response.put("res_msg", e.getMessage());
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        } catch (Exception e) {
	            response.put("res_code", "500");
	            response.put("res_msg", "서버 오류가 발생했습니다.");
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }
	  }

	
	
	// 부서명 수정
	@PostMapping("/dept/{dept_no}")
	public ResponseEntity<Map<String, String>> editDeptName(@PathVariable("dept_no") Long deptNo, DepartmentsDto dto) {
	    Map<String, String> resultMap = new HashMap<>();
	    try {
	        if (deptService.editDepartment(dto) != null) {
	            resultMap.put("res_msg", "부서명이 성공적으로 수정되었습니다.");
	            return ResponseEntity.ok(resultMap);
	        } else {
	            throw new Exception("부서 수정 중 오류가 발생했습니다.");
	        }
	    } catch (IllegalArgumentException e) {
	        resultMap.put("res_msg", e.getMessage());
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
	    } catch (Exception e) {
	        resultMap.put("res_msg", "부서 수정 중 오류가 발생했습니다.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
	    }
	
	}
	// 부서명 수정
	/*
	 * @PostMapping("/api/editDepartment") public ResponseEntity<String>
	 * editDepartment(@RequestBody DepartmentsDto departmentsDto) {
	 * System.out.println("Received DTO: " + departmentsDto); // 디버깅용
	 * 
	 * try { // 부서 수정 로직 호출 deptService.editDepartment(departmentsDto); return
	 * ResponseEntity.ok("부서 수정이 완료되었습니다."); } catch (IllegalArgumentException e) {
	 * return ResponseEntity.badRequest().body(e.getMessage()); } }
	 */



}