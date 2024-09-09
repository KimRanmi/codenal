package com.codenal.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
    @GetMapping("/admin/employeeList")
    public String selectEmployeeList(Model model,
            @PageableDefault(page = 0, size = 10, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable,
            @ModelAttribute("searchDto") EmployeeDto searchDto) { 
        
        if (searchDto == null) {
            searchDto = new EmployeeDto();
        }
    	
        Page<EmployeeDto> employeeList = employeeListService.selectEmployeeListOne(searchDto, pageable);
      
        LOGGER.debug(employeeList.toString());
        
        model.addAttribute("employeeList", employeeList);
        model.addAttribute("searchDto", searchDto);
        
        return "admin/employeeList";
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
