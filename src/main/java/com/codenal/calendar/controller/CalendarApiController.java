package com.codenal.calendar.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.calendar.domain.CalendarDto;
import com.codenal.calendar.service.CalendarService;

@Controller
public class CalendarApiController {
	
	private final CalendarService calendarService;
	
	@Autowired
	public CalendarApiController(CalendarService calendarService) {
		this.calendarService = calendarService;
	}
	
	@ResponseBody
	@PostMapping("/create/event")
	public Map<String, String> createEvent(CalendarDto dto){
		Map<String, String> resultEvent = new HashMap<String, String>();
		System.out.println(dto);
//		resultEvent.put("res_msg", "일정을 추가하지 못했습니다.");
//		if(calendarService.createEvent(dto) != null) {
//			resultEvent.put("res_msg", "일정이 추가되었습니다.");
//		} else {
//			resultEvent.put("res_msg", "일정을 추가하지 못했습니다.");
//		}
		
		return resultEvent;	
	}

}
