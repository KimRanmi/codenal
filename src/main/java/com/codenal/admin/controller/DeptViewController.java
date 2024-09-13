package com.codenal.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DeptViewController {

	// 부서 관리
		@GetMapping("/admin/dept")
		public String DeptPage() {
		    return "admin/dept"; 
		}
		
		@GetMapping("/employee/addressBook")
		public String addressBookPage() {
		    return "employee/addressBook"; 
		}
} 
