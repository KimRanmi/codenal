package com.codenal.admin.controller;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codenal.admin.service.AdminService;
import com.codenal.employee.domain.EmployeeDto;



@Controller
@RequestMapping("/admin")
public class AdminViewController {

	private final AdminService adminService;
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminViewController.class);

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

		LOGGER.debug(resultList.toString());

		model.addAttribute("resultList", resultList);
		model.addAttribute("searchDto", searchDto);

		return "admin/list";
	}
	
	// 직원 정보 수정
	//@GetMapping("/update")
//	public String updatePage() {
//		return "admin/update";
//	}

	
	// 직원 정보 상세 조회
	@GetMapping("/detail/{empId}")
	public String employeeListDetail(@PathVariable("empId") Long empId, 
	                                 @RequestParam(value = "modify", required = false, defaultValue = "false") boolean modify, 
	                                 Model model) {
		
	    EmployeeDto employeeDetail = adminService.employeeDetail(empId); 
	    model.addAttribute("employeeDetail", employeeDetail);
	    model.addAttribute("modifyMode", modify); // 수정 모드 여부를 모델에 추가

	    return "admin/detail";
	}

	
	// 직원 정보 수정
	@PostMapping("/update/{empId}")
	public String updateEmployee(@PathVariable("empId") Long empId, EmployeeDto employeeDto) {
		
	    adminService.employeeUpdate(empId, employeeDto);
	    
	    return "/admin/detail/";
	}


 





}
