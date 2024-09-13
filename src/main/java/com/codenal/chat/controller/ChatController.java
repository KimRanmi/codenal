package com.codenal.chat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.employee.domain.EmployeeDto;
import com.codenal.employee.service.EmployeeService;

@Controller
public class ChatController {

private final EmployeeService employeeService;
	
	@Autowired
	public ChatController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	@GetMapping("/chatList")
	public String chat(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
        
		List<EmployeeDto> employeeList = employeeService.getActiveEmployeeList();
		model.addAttribute("employeeList",employeeList);
		return "apps/chat";
	}
	
	@ResponseBody
	@PostMapping("/chatList/chatRoom/create")
	public Map<String, String> chatRoomCreate(Model model, ) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
        
        Map<String, String> resultMap = new HashMap<>();
        
        
        return resultMap;
	}
	
}
