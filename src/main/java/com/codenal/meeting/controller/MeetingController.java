package com.codenal.meeting.controller;

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
	public Map<String, Object> meetingRoomDetail(MeetingRoomReserveDto dto){
		Map<String, Object> result = new HashMap<String, Object>();
		System.out.println(dto);
		return result;
	}

}
