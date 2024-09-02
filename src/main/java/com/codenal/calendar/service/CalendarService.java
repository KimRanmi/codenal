package com.codenal.calendar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codenal.calendar.domain.Calendar;
import com.codenal.calendar.domain.CalendarDto;
import com.codenal.calendar.repository.CalendarRepository;

@Service
public class CalendarService {
	
	private final CalendarRepository calendarRepository;
	
	@Autowired
	public CalendarService(CalendarRepository calendarRepository) {
		this.calendarRepository = calendarRepository;
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
