package com.codenal.calendar.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import jakarta.websocket.server.PathParam;

@Controller
public class CalendarApiController {
	
	private final CalendarService calendarService;
	
	@Autowired
	public CalendarApiController(CalendarService calendarService) {
		this.calendarService = calendarService;
	}
	
	@ResponseBody
	@PostMapping("/modify/event")
	public void modifyEvent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CalendarDto dto = new CalendarDto();
		dto.setCalendar_schedule_no(Long.parseLong(request.getParameter("eventId")));
		dto.setCalendar_schedule_category(Long.parseLong(request.getParameter("category")));
		dto.setCalendar_schedule_title(request.getParameter("title"));
		dto.setCalendar_schedule_location(request.getParameter("location"));
		dto.setCalendar_schedule_content(request.getParameter("content"));
		dto.setCalendar_schedule_writer(Long.parseLong(request.getParameter("writer")));
		String startDate = request.getParameter("start_date");
		String endDate = request.getParameter("end_date");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		dto.setCalendar_schedule_start_date(LocalDateTime.parse(startDate, dtf));
		if(endDate.equals("null") == false) {
			dto.setCalendar_schedule_end_date(LocalDateTime.parse(endDate, dtf));
		}
		calendarService.modifyEvent(dto);
	}
	
	@ResponseBody
	@DeleteMapping("/delete/event{id}")
	public Map<String, String> deleteEvent(@PathVariable("id") Long eventNo){
		Map<String, String> result = new HashMap<String, String>();
		
		calendarService.deleteEvent(eventNo);
		return result;
	}
	
	@ResponseBody
	@PostMapping("/eventList")
	public Map<String, Object> selectEvent() {
		Map<String, Object> resultEvent = new HashMap<String, Object>();
		List<CalendarDto> eventList = calendarService.selectEvent();
		System.out.println(eventList);
		JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
 
		
		if(eventList != null) {
			resultEvent.put("eventList", eventList);
//			for(int i=0; i<=eventList.size(); i++) {
//				resultEvent.put("id", eventList.get(i).getCalendar_schedule_no());
//				resultEvent.put("category", eventList.get(i).getCalendar_schedule_category());
//				resultEvent.put("title", eventList.get(i).getCalendar_schedule_title());
//				resultEvent.put("start", eventList.get(i).getCalendar_schedule_start_date());
//				resultEvent.put("end", eventList.get(i).getCalendar_schedule_end_date());
//				resultEvent.put("location", eventList.get(i).getCalendar_schedule_location());
//				resultEvent.put("description", eventList.get(i).getCalendar_schedule_content());
//				resultEvent.put("writer", eventList.get(i).getCalendar_schedule_writer_name());
//				resultEvent.put("color", eventList.get(i).getCalendar_schedule_color());
//				
//				obj = new JSONObject(resultEvent);
//	            arr.put(obj);
//			}
		}
		
		return resultEvent;
	}
	
	@ResponseBody
	@PostMapping("/create/event")
	public void createEvent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		CalendarDto dto = new CalendarDto();
		dto.setCalendar_schedule_category(Long.parseLong(request.getParameter("category")));
		dto.setCalendar_schedule_title(request.getParameter("title"));
		dto.setCalendar_schedule_location(request.getParameter("location"));
		dto.setCalendar_schedule_content(request.getParameter("content"));
		dto.setCalendar_schedule_writer(Long.parseLong(request.getParameter("writer")));
		String startDate = request.getParameter("start_date");
		String endDate = request.getParameter("end_date");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		dto.setCalendar_schedule_start_date(LocalDateTime.parse(startDate, dtf));
		if(endDate != null && endDate.equals("null") == false) {
			dto.setCalendar_schedule_end_date(LocalDateTime.parse(endDate, dtf));
		}
		calendarService.createEvent(dto);
	}

}
