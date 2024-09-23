package com.codenal.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.codenal.admin.domain.DepartmentsDto;
import com.codenal.admin.domain.JobsDto;
import com.codenal.admin.service.AdminService;
import com.codenal.employee.domain.EmployeeDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/api")
public class AdminApiController {

	private final AdminService adminService;

	@Autowired
	public AdminApiController(AdminService adminService) {
		this.adminService = adminService;
	} 

	// 신규 직원 등록
	@ResponseBody 
	@PostMapping("/join")
	public Map<String,String> joinEmployee(@RequestBody EmployeeDto dto) {

		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "등록 중 오류가 발생했습니다.");

		if(adminService.createEmployee(dto) > 0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "신규 직원이 성공적으로 등록되었습니다.");
		};

		return resultMap;
		
		
	}
	
	// 직원 상세 정보
	@GetMapping("/detail/{empId}")
	public String employeeListDetail(@PathVariable("empId") Long empId, 
	                                 @RequestParam(value = "modify", required = false, defaultValue = "false") boolean modify, 
	                                 Model model) {
	    
	    EmployeeDto employeeDetail = adminService.employeeDetail(empId); 
	    List<DepartmentsDto> departments = adminService.getAllDepartments();
	    List<JobsDto> jobs = adminService.getAllJobs();
	    
	    model.addAttribute("employeeDetail", employeeDetail);
	    model.addAttribute("departments", departments);
	    model.addAttribute("jobs", jobs);
	    model.addAttribute("modifyMode", modify); // 수정 모드 여부를 모델에 추가

	    return "admin/detail";
	}


	// 직원 정보 수정
	@ResponseBody
	@PostMapping("/update")
	public Map<String, String> employeeUpdate(EmployeeDto dto) {
	    Map<String, String> resultMap = new HashMap<>();
	    resultMap.put("res_code", "404");
	    resultMap.put("res_msg", "직원 정보 수정 중 오류가 발생했습니다.");

	    // 서비스 호출 (dto에 empId가 포함되어 있다고 가정)
	    if (adminService.employeeUpdate(dto.getEmpId(), dto) != null) {
	        resultMap.put("res_code", "200");
	        resultMap.put("res_msg", "직원 정보가 성공적으로 수정되었습니다.");
	    }

	    return resultMap;
	}




}

