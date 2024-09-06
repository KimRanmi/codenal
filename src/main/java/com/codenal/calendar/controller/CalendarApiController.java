package com.codenal.calendar.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.calendar.domain.Calendar;
import com.codenal.calendar.domain.CalendarDto;
import com.codenal.calendar.service.CalendarService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CalendarApiController {
	
	private final CalendarService calendarService;
	
	@Autowired
	public CalendarApiController(CalendarService calendarService) {
		this.calendarService = calendarService;
	}
	
	@ResponseBody
	@PostMapping("/eventList")
	public Map<String, Object> selectEvent() {
		Map<String, Object> resultEvent = new HashMap<String, Object>();
		List<CalendarDto> eventList = calendarService.selectEvent();
		System.out.println(eventList.get(0));
//		String[][] str = null;
//		for(int i=0; i<eventList.size(); i++) {
//			str[]
//		}
		if(eventList != null) {
			resultEvent.put("eventList", eventList);
		}
		
		return resultEvent;
	}
	
	@ResponseBody
	@PostMapping("/create/event")
	public Map<String, String> createEvent(CalendarDto dto) {
		Map<String, String> resultEvent = new HashMap<String, String>();
		dto.setCalendar_schedule_category((long) 1);
		dto.setCalendar_schedule_title("title");
		dto.setCalendar_schedule_location("location");
		dto.setCalendar_schedule_content("content");
		dto.setCalendar_schedule_writer((long) 12345678);
		dto.setCalendar_schedule_start_date(LocalDateTime.of(2024,9,4,0,0));
		dto.setCalendar_schedule_end_date(LocalDateTime.of(2024,9,6,0,0));
		System.out.println(dto);
		resultEvent.put("res_msg", "일정을 추가하지 못했습니다.");
		if(calendarService.createEvent(dto) != null) {
			resultEvent.put("res_msg", "일정이 추가되었습니다.");
		} else {
			resultEvent.put("res_msg", "일정을 추가하지 못했습니다.");
		}
		
		return resultEvent;
	}

}
