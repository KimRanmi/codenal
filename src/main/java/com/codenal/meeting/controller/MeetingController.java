package com.codenal.meeting.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.meeting.domain.MeetingRoom;
import com.codenal.meeting.domain.MeetingRoomDto;
import com.codenal.meeting.domain.MeetingRoomReserve;
import com.codenal.meeting.domain.MeetingRoomReserveDto;
import com.codenal.meeting.domain.MeetingRoomTimeDto;
import com.codenal.meeting.service.MeetingRoomService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MeetingController {
	
	private MeetingRoomService meetingRoomService;
	
	@Autowired
	public MeetingController(MeetingRoomService meetingRoomService) {
		this.meetingRoomService = meetingRoomService;
	}
	
	@ResponseBody
	@PostMapping("/meetingRoom")
	public Map<String, Object> meetingRoomList(Model model) {
		List<MeetingRoomDto> mr = meetingRoomService.meetingRoomList();
		List<MeetingRoomTimeDto> time = meetingRoomService.meetingRoomTimeList();
		System.out.println(mr);
		Map<String, Object> resultMr = new HashMap<String, Object>();
		if(mr != null) {
			resultMr.put("meetingRoom", mr);
			resultMr.put("meetingRoomTime", time);
			model.addAttribute("meetingRoom", mr);
			model.addAttribute("meetingRoomTime", time);
		}
		return resultMr;
		
	}
	
	@ResponseBody
	@PostMapping("/meetingRoomReserve")
	public void meetingRoomDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MeetingRoomReserveDto reserveDto = new MeetingRoomReserveDto();
		reserveDto.setMeeting_room_no(Long.parseLong(request.getParameter("meetingRoomNo")));
		reserveDto.setMeeting_room_reserve_date(LocalDate.parse(request.getParameter("reserveDate")));
		reserveDto.setMeeting_room_reserve_time_no(Long.parseLong(request.getParameter("reserveTime")));
		reserveDto.setEmp_id(Long.parseLong(request.getParameter("reserveEmpId")));
		if(meetingRoomService.meetingRoomReserve(reserveDto) != null) {
			
			System.out.println("성공");
		};
	}

}
