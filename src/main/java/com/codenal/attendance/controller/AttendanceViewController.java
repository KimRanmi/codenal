package com.codenal.attendance.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codenal.attendance.domain.AttendanceDto;
import com.codenal.attendance.service.AttendanceService;

@Controller

public class AttendanceViewController {

	
	 	@Autowired
	    private AttendanceService attendanceService;
	
	 	@GetMapping("/apps-attendance")
	    public String showAttendancePage(
	            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
	            @PageableDefault(size = 10) Pageable pageable,
	            Model model) {

	        LocalDate targetDate = (date != null) ? date : LocalDate.now();
	        Page<AttendanceDto> attendances = attendanceService.getAttendancesByDate(targetDate, pageable);

	        model.addAttribute("attendances", attendances);
	        model.addAttribute("currentPage", attendances.getNumber());
	        model.addAttribute("totalPages", attendances.getTotalPages());
	        model.addAttribute("date", targetDate);

	        return "apps/attendance";  // Thymeleaf 템플릿 경로
	    }
}