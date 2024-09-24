package com.codenal.meeting.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.codenal.meeting.domain.MeetingRoom;
import com.codenal.meeting.domain.MeetingRoomDto;
import com.codenal.meeting.domain.MeetingRoomReserve;
import com.codenal.meeting.domain.MeetingRoomReserveDto;
import com.codenal.meeting.domain.MeetingRoomTimeDto;
import com.codenal.meeting.service.MeetingRoomImgFileService;
import com.codenal.meeting.service.MeetingRoomService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MeetingController {
	
	private MeetingRoomService meetingRoomService;
	private MeetingRoomImgFileService fileService;
	
	@Autowired
	public MeetingController(MeetingRoomService meetingRoomService, MeetingRoomImgFileService fileService) {
		this.meetingRoomService = meetingRoomService;
		this.fileService = fileService;
	}
	
	@DeleteMapping("/meetingRoomDelete/{meetingRoomNo}")
	public Map<String, String> MeetingRoomDelete(@PathVariable("meetingRoomNo") Long roomNo){
		Map<String, String> result = new HashMap<String, String>();
		result.put("msg", "삭제 중 문제가 발생했습니다.");
		if(meetingRoomService.MeetingRoomDelete(roomNo) > 0) {
			result.put("msg", "삭제되었습니다.");
		}
		System.out.println(result);
		return result;
	}
	
	@ResponseBody
	@PostMapping("/meetingRoomCreate")
	public Map<String, String> MeetingRoomCreate(MeetingRoomDto dto, @RequestParam("file") MultipartFile file){
		Map<String, String> resultMap = new HashMap<String, String>();
		
		String saveFile = fileService.Img(file);
		String path = "/meetingRoomImg/"+saveFile;
		
		if(saveFile != null) {
			dto.setMeeting_room_img(path);
			if(meetingRoomService.MeetingRoomCreate(dto) != null) {
				resultMap.put("res_code", "200");
				resultMap.put("res_msg", "회의실이 추가되었습니다.");
			}
		} else {
			resultMap.put("res_msg", "회의실 추가 중 문제가 발생했습니다.");
		}
		System.out.println(dto);
		return resultMap;
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
