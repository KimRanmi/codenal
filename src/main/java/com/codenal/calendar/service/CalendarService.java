package com.codenal.calendar.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codenal.calendar.domain.Calendar;
import com.codenal.calendar.domain.CalendarDto;
import com.codenal.calendar.repository.CalendarRepository;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;
import com.codenal.employee.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;

@Service
public class CalendarService {
	
	private final CalendarRepository calendarRepository;
	private final EmployeeRepository employeeRepository;
	
	@Autowired
	public CalendarService(CalendarRepository calendarRepository, EmployeeRepository employeeRepository) {
		this.calendarRepository = calendarRepository;
		this.employeeRepository = employeeRepository;
	}
	
	public List<CalendarDto> selectEvent(){
		List<Calendar> eventList = calendarRepository.findAll();
		List<CalendarDto> eventDtoList = new ArrayList<CalendarDto>();
		for(Calendar c : eventList) {
			CalendarDto calendarDto = new CalendarDto().toDto(c);
			int writer = c.getCalendarScheduleWriter();
			Employee empName = employeeRepository.findByEmpId(writer);
			EmployeeDto empNameDto = EmployeeDto.fromEntity(empName);
			calendarDto.setCalendar_schedule_writer_name(empNameDto.getEmpName());
			eventDtoList.add(calendarDto);
		}
		System.out.println("확인"+eventDtoList);
		return eventDtoList;
	}
	
	public Calendar createEvent(CalendarDto dto) {
		Calendar calendar = Calendar.builder()
				.calendarScheduleCategory(dto.getCalendar_schedule_category())
				.calendarScheduleTitle(dto.getCalendar_schedule_title())
				.calendarScheduleLocation(dto.getCalendar_schedule_location())
				.calendarScheduleContent(dto.getCalendar_schedule_content())
				.calendarScheduleWriter(dto.getCalendar_schedule_writer())
				.calendarScheduleStartDate(dto.getCalendar_schedule_start_date())
				.calendarScheduleEndDate(dto.getCalendar_schedule_end_date())
				.build();
		System.out.println(calendar.getCalendarScheduleTitle());
		return calendarRepository.save(calendar);
	}

}
