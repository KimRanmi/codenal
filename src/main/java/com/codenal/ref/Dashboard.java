package com.codenal.ref;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;
import com.codenal.employee.service.EmployeeService;

import jakarta.servlet.http.HttpSession;

@Controller
public class Dashboard {
	
	private final EmployeeService employeeService;
	
	@Autowired
	public Dashboard(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping("/")
	public String index(Model model, HttpSession session) {
		Long empId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
		model.addAttribute("empId" , empId);
		Employee emp = employeeService.getEmployeeById(empId);
		EmployeeDto empDto = new EmployeeDto();
		empDto = EmployeeDto.fromEntity(emp);
		model.addAttribute("empDto" , empDto);
		session.setAttribute("profileImage", emp.getEmpProfilePicture());
		return "dashboard/projects";
	}
	
	@GetMapping("/dashboard-analytics")
	public String dashboard_analytics() {
		return "dashboard/analytics";
	}
	
	@GetMapping("/dashboard-crm")
	public String dashboard_crm() {
		return "dashboard/crm";
	}
	
	@GetMapping("/dashboard-crypto")
	public String dashboard_crypto() {
		return "dashboard/crypto";
	}
	
	@GetMapping("/dashboard-projects")
	public String dashboard_projects() {
		return "dashboard/projects";
	}
	
	@GetMapping("/dashboard-nft")
	public String dashboard_nft() {
		return "dashboard/nft";
	}
	
	@GetMapping("/dashboard-job")
	public String dashboard_job() {
		return "dashboard/job";
	}
}
