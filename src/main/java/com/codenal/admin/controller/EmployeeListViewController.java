package com.codenal.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.codenal.admin.service.EmployeeListService;
import com.codenal.employee.domain.EmployeeDto;


@Controller
public class EmployeeListViewController {


	private final EmployeeListService employeeListService;
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeListViewController.class);

	@Autowired
	public EmployeeListViewController(EmployeeListService employeeListService) {
		this.employeeListService = employeeListService;
	}

	// 직원 목록 검색 (재직/퇴사 + 직원 정보)
	@GetMapping("/admin/list")
	public String searchAll(Model model,
			@PageableDefault(page = 0, size = 10, sort = "empHire", direction = Sort.Direction.DESC) Pageable pageable,
			@ModelAttribute("searchDto") EmployeeDto searchDto) { 
		
		System.out.println("냥1 : " + searchDto);

		// 셀렉트 박스 통합
		Page<EmployeeDto> resultList = employeeListService.searchAll(searchDto, pageable);

		  System.out.println("냥2 : " + resultList);
		  
		LOGGER.debug(resultList.toString());

		model.addAttribute("resultList", resultList);
		model.addAttribute("searchDto", searchDto);
		return "admin/list";
	}





	// 직원 정보 상세 조회
	//@GetMapping("/admin/employeeListDetail/{employee_id}")
	//	public String employeeListDetail(@PathVariable("id") Long id, Model model) {
	//    EmployeeDto employeeList = employeeListService.selectEmployeeListDetail(id);
	//    model.addAttribute("employeeList", employeeList);

	//    return "admin/employeeListDetail";
	//}

	// 직원 정보 수정
	//@GetMapping("/admin/employeeListUpdate/{employee_id}")
	//	public String employeeListUpdate(@PathVariable("id") Long id, Model model) {

	//	EmployeeDto employeeList = employeeListService.selectEmployeeListUpdate(id);
	//    model.addAttribute("employeeList", employeeList);

	//    return "admin/employeeListDetail";
	//}
}
