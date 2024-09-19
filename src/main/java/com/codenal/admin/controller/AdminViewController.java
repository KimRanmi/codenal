package com.codenal.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Sort;

import com.codenal.admin.domain.DepartmentsDto;
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

	// 직원 정보 수정
	@GetMapping("/update/{empId}")
	public String employeeUpdate(@PathVariable("empId") Long empId, Model model) {

		EmployeeDto employeeUpdate = adminService.employeeDetail(empId);
		model.addAttribute("employeeUpdate", employeeUpdate);
		return "admin/update"; 
	}

	// 직원 정보 수정 (POST 요청)
	@PostMapping("/update/{empId}")
	public String saveEmployeeUpdate(@PathVariable("empId") Long empId, EmployeeDto employeeDto) {
		adminService.employeeUpdate(empId, employeeDto);
		return "redirect:/admin/detail/" + empId; // 수정 후 상세 페이지로 리다이렉트
	}



}
