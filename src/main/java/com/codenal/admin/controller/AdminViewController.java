package com.codenal.admin.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.domain.Sort;

import com.codenal.admin.domain.DepartmentsDto;
import com.codenal.admin.domain.JobsDto;
import com.codenal.admin.service.AdminService;
import com.codenal.employee.domain.EmployeeDto;


@Controller
@RequestMapping("/admin")
public class AdminViewController {

	private final AdminService adminService;

	@Autowired
	public AdminViewController(AdminService adminService) {
		this.adminService = adminService;
	}

	// 신규 직원 등록
	@GetMapping("/join")
	public String joinPage() {
		return "admin/join";
	}


	// 직원 목록 검색 (재직/퇴사 + 직원 정보)
	@GetMapping("/list")
	public String searchAll(Model model,
			@PageableDefault(page = 0, size = 10, sort = "empId", direction = Sort.Direction.DESC) Pageable pageable,
			@ModelAttribute("searchDto") EmployeeDto searchDto) {

		// 셀렉트 박스 통합
		Page<EmployeeDto> resultList = adminService.searchAll(searchDto, pageable);

		model.addAttribute("resultList", resultList);
		model.addAttribute("searchDto", searchDto);

		return "admin/list";
	}


	// 직원 정보 상세 조회
	@GetMapping("/detail/{empId}")
	
	public String employeeListDetail(@PathVariable("empId") Long empId, 
			Model model) {
		EmployeeDto employeeDetail = adminService.employeeDetail(empId);
		model.addAttribute("employeeDetail", employeeDetail);

		List<DepartmentsDto> departments = adminService.getAllDepartments();
		model.addAttribute("departments", departments);

		return "admin/detail";
	}
	
	// 직원 정보 상세 조회 (퇴사하고 JSON 반환)
	@GetMapping("/detail/{empId}/json")
	@ResponseBody
	public ResponseEntity<?> employeeListDetailJson(@PathVariable("empId") Long empId) {
	    EmployeeDto employeeDetail = adminService.employeeDetail(empId);

	    Map<String, Object> response = new HashMap<>();
	    response.put("success", true);
	    response.put("employeeDetail", employeeDetail);

	    return ResponseEntity.ok(response); // JSON 응답 반환
	    }
	
	
	// 직원 비밀번호 변경 (work1234)
	@PostMapping("/reset-password")
	@ResponseBody
	public Map<String, Object> resetPassword(@RequestBody Map<String, String> requestData) {
	    Map<String, Object> response = new HashMap<>();
	    
	    String adminPw = requestData.get("adminPw");
	    Long empId = Long.parseLong(requestData.get("empId"));
	    
	    // 관리자 비밀번호 확인 
	    if (!adminPw.equals("work1234")) {
	        response.put("success", false);
	        response.put("message", "관리자 비밀번호가 일치하지 않습니다.");
	        return response;
	    }
	    
	    // 직원 비밀번호 변경
	    try {
	        adminService.resetEmployeePassword(empId, "work1234");
	        response.put("success", true);
	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "비밀번호 변경 중 오류가 발생했습니다.");
	    }
	    
	    return response;
	}
	
	
	// 직원 퇴사 
		@PostMapping("/emp-end")
		@ResponseBody
		public Map<String, Object> emdEndDatePwa(@RequestBody Map<String, String> requestData) {
		    Map<String, Object> response = new HashMap<>();
		    
		    String adminPw= requestData.get("adminPw");
		    Long empId = Long.parseLong(requestData.get("empId"));
		    String empEndDateStr = requestData.get("empEnd");
		
		    // 관리자 비밀번호 확인 
		    if (!adminPw.equals("work1234")) {
		        response.put("success", false);
		        response.put("message", "관리자 비밀번호가 일치하지 않습니다.");
		        return response;
		    }
		    
		    if (empEndDateStr == null || empEndDateStr.isEmpty()) {
		        response.put("success", false);
		        response.put("message", "퇴사일이 입력되지 않았습니다.");
		        return response;
		    }
	
		    try {
		    	// 퇴사일 String -> Date
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		        LocalDate empEndDate = LocalDate.parse(empEndDateStr, formatter);

		        // empStatus 'N' 변경
		        boolean success = adminService.emdEndDate(empId, empEndDate);

		        if (success) {
		            response.put("success", true);
		            response.put("message", "직원이 성공적으로 퇴사 처리되었습니다.");
		        } else {
		            response.put("success", false);
		            response.put("message", "퇴사 처리 중 오류가 발생했습니다.");
		        }
		    } catch (DateTimeParseException e) {
		        response.put("success", false);
		        response.put("message", "퇴사일 형식이 올바르지 않습니다.");
		    }
		    
		    return response;
		}


	// 직원 정보 수정
	@GetMapping("/update/{empId}")
	public String employeeUpdate(@PathVariable("empId") Long empId, Model model) {
		
	    EmployeeDto employeeUpdate = adminService.employeeDetail(empId);
	    
	    System.out.println("employeeUpdate deptNo: " + employeeUpdate.getDeptNo());
	    model.addAttribute("employeeUpdate", employeeUpdate);
	    
	    // 부서 셀렉트
	    List<DepartmentsDto> departments = adminService.getAllDepartments();
	    model.addAttribute("departments", departments);

	    // 직급 목록 추가
	    List<JobsDto> jobs = adminService.getAllJobs();
	    model.addAttribute("jobs", jobs);

	    return "admin/update";
	}

	// 직원 정보 수정 (POST 요청)
	@PostMapping("/update/{empId}")
	public String saveEmployeeUpdate(@PathVariable("empId") Long empId, EmployeeDto employeeDto) {
	    System.out.println("Received empId: " + empId);
	    System.out.println("Received deptNo: " + employeeDto.getDeptNo());

	    try {
	        adminService.employeeUpdate(empId, employeeDto);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return "redirect:/admin/detail/" + empId;
	}


}
