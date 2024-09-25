package com.codenal.meeting.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;
import com.codenal.employee.service.EmployeeService;
import com.codenal.meeting.domain.MeetingRoomDto;
import com.codenal.meeting.domain.MeetingRoomReserveDto;
import com.codenal.meeting.domain.MeetingRoomTimeDto;
import com.codenal.meeting.service.MeetingRoomService;

@Controller
public class MeetingRoomApiController {
	
	private MeetingRoomService meetingRoomService;
	private EmployeeService employeeService;
	
	@Autowired
	public MeetingRoomApiController(MeetingRoomService meetingRoomService, EmployeeService employeeService) {
		this.meetingRoomService = meetingRoomService;
		this.employeeService = employeeService;
	}
	
	// 회의실 예약

	@GetMapping("/apps-ecommerce-product-details")
	public String apps_ecommerce_product_details(Model model) {
		List<MeetingRoomDto> mr = meetingRoomService.meetingRoomList();
		List<MeetingRoomTimeDto> time = meetingRoomService.meetingRoomTimeList();
		Long empId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
		model.addAttribute("meetingRoom", mr);
		model.addAttribute("meetingRoomTime", time);
		model.addAttribute("empId", SecurityContextHolder.getContext().getAuthentication().getName());
		Employee emp = employeeService.getEmployeeById(empId);
		EmployeeDto empDto = EmployeeDto.fromEntity(emp);
		model.addAttribute("empAuth", empDto.getEmpAuth());
		System.out.println(empDto);
		return "apps/ecommerce-product-details";
	}
	
	// 회의실 추가
	@GetMapping("/apps-meeting-room-check")
	public String apps_chat() {
		return "apps/meeting-room-check";
	}
	
	// 회의실 예약 내역
	@GetMapping("/apps-ecommerce-cart")
	public String apps_ecommerce_cart(Model model) {
		Long empId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
		model.addAttribute("empId", empId);
		
		List<MeetingRoomReserveDto> dto = meetingRoomService.MeetingRoomReserveList(empId);
		Map<String, Object> result = new HashMap<String, Object>();
		if(dto != null) {
			model.addAttribute("reserveList", dto);
			result.put("reserveList", dto);
		}
		return "apps/ecommerce-cart";
	}

}
