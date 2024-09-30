package com.codenal.attendance.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	 	        @RequestParam(value = "startDate", required = false)
	 	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

	 	        @RequestParam(value = "endDate", required = false)
	 	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

	 	        @PageableDefault(size = 10, sort = "workDate", direction = Sort.Direction.DESC) Pageable pageable, // 정렬 추가
	 	        Model model) {

	 	    Page<AttendanceDto> attendances;

	 	    if (startDate != null && endDate != null) {
	 	        attendances = attendanceService.getAttendancesByDateRange(startDate, endDate, pageable);
	 	    } else {
	 	        attendances = attendanceService.getAllAttendances(pageable);
	 	    }

	 	    model.addAttribute("attendances", attendances);
	 	    model.addAttribute("currentPage", attendances.getNumber());
	 	    model.addAttribute("totalPages", attendances.getTotalPages());
	 	    model.addAttribute("startDate", startDate);
	 	    model.addAttribute("endDate", endDate);

	 	    return "apps/attendance";
	 	}
}