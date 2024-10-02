package com.codenal.ref;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.codenal.announce.domain.Announce;
import com.codenal.announce.domain.AnnounceDto;
import com.codenal.announce.service.AnnounceService;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;
import com.codenal.employee.service.EmployeeService;
import com.codenal.meeting.domain.MeetingRoomReserveDto;
import com.codenal.meeting.repository.MeetingRoomReserveRepository;
import com.codenal.meeting.service.MeetingRoomService;

import jakarta.servlet.http.HttpSession;

@Controller
public class Dashboard {
	
	private final EmployeeService employeeService;
	private final AnnounceService announceService;
	private final MeetingRoomService meetingRoomService;
	
	@Autowired
	public Dashboard(EmployeeService employeeService, AnnounceService announceService, MeetingRoomService meetingRoomService) {
		this.employeeService = employeeService;
		this.announceService = announceService;
		this.meetingRoomService = meetingRoomService;
	}

	@GetMapping("/")
	public String index(Model model, HttpSession session) {
		Long empId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
		model.addAttribute("empId" , empId);
		
		// emp 데이터 가져오기
		Employee emp = employeeService.getEmployeeById(empId);
		EmployeeDto empDto = new EmployeeDto();
		empDto = EmployeeDto.fromEntity(emp);
		
		// 게시판 데이터 가져오기
		List<AnnounceDto> announce = announceService.nopageableAnnounceList();
		int count = announceService.announceCount();
		model.addAttribute("announce" , announce);
		model.addAttribute("count" , count);
		
		// 회의실 예약 데이터 가져오기
		List<MeetingRoomReserveDto> reserve = meetingRoomService.MeetingRoomReserveList(empId);
		model.addAttribute("reserve" , reserve);
		
		// 전자결제 데이터 가져오기
		
		// 알림 데이터 가져오기
		
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
