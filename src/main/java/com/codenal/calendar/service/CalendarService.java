package com.codenal.calendar.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codenal.employee.domain.Departments;
import com.codenal.employee.domain.DepartmentsDto;
import com.codenal.employee.domain.Jobs;
import com.codenal.employee.repository.DepartmentsRepository;
import com.codenal.employee.repository.JobsRepository;
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
	private final DepartmentsRepository departmentsRepository;
	private final JobsRepository jobsRepository;
	
	@Autowired
	public CalendarService(CalendarRepository calendarRepository, EmployeeRepository employeeRepository, DepartmentsRepository departmentsRepository, JobsRepository jobsRepository) {
		this.calendarRepository = calendarRepository;
		this.employeeRepository = employeeRepository;
		this.departmentsRepository = departmentsRepository;
		this.jobsRepository = jobsRepository;
	}
	
	public String[] eventWriter(Long empId) {
		Employee emp = employeeRepository.findByEmpId(empId);
		EmployeeDto dto = EmployeeDto.fromEntity(emp);
		
		int detpNo = dto.getDeptNo();
		Departments dept = departmentsRepository.findByDeptNo((long) detpNo);
		
		int jodNo = dto.getJobNo();
		Jobs job = jobsRepository.findByJobNo((long) jodNo);
		System.out.println(job.getJobName());
		String[] str = {dto.getEmpName(), dept.getDeptName(),job.getJobName()};
		// jobs, jobsDto 생성 후 부서명, 직급명 각자 레포지토리에서 가져온 후 String 객체 만들어서 거기다 넣어서 리턴 후 js에서 출력하기
		
		return str;
	}
	
	public void modifyEvent(CalendarDto dto) {
		CalendarDto res = selectOneEvent(dto.getCalendar_schedule_no());
		res.setCalendar_schedule_category(dto.getCalendar_schedule_category());
		res.setCalendar_schedule_title(dto.getCalendar_schedule_title());
		res.setCalendar_schedule_content(dto.getCalendar_schedule_content());
		res.setCalendar_schedule_writer(dto.getCalendar_schedule_writer());
		res.setCalendar_schedule_start_date(dto.getCalendar_schedule_start_date());
		res.setCalendar_schedule_end_date(dto.getCalendar_schedule_end_date());
		res.setCalendar_schedule_location(dto.getCalendar_schedule_location());
		res.setCalendar_schedule_color(dto.getCalendar_schedule_color());
		Calendar calendar = res.toEntity();
		Calendar result = calendarRepository.save(calendar);
	}
	
	public CalendarDto selectOneEvent(Long eventNo) {
		Calendar cal = calendarRepository.findBycalendarScheduleNo(eventNo);
		CalendarDto dto = CalendarDto.builder()
				.calendar_schedule_no(cal.getCalendarScheduleNo())
				.calendar_schedule_category(cal.getCalendarScheduleCategory())
				.calendar_schedule_title(cal.getCalendarScheduleTitle())
				.calendar_schedule_content(cal.getCalendarScheduleContent())
				.calendar_schedule_writer(cal.getCalendarScheduleWriter())
				.calendar_schedule_start_date(cal.getCalendarScheduleStartDate())
				.calendar_schedule_end_date(cal.getCalendarScheduleEndDate())
				.calendar_schedule_color(cal.getCalendarScheduleColor())
				.calendar_schedule_location(cal.getCalendarScheduleLocation())
				.build();
		return dto;
		
	}
	
	public int deleteEvent(Long eventNo) {
		int result = 0;
		try {
			calendarRepository.deleteById(eventNo);
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<CalendarDto> selectEvent(Long writerId){
		List<Calendar> eventList = calendarRepository.findAll();
		List<CalendarDto> eventDtoList = new ArrayList<CalendarDto>();
		for(Calendar c : eventList) {
			CalendarDto calendarDto = new CalendarDto().toDto(c);
			Long writer = c.getCalendarScheduleWriter();
			Employee empName = employeeRepository.findByEmpId(writer);
			EmployeeDto empNameDto = EmployeeDto.fromEntity(empName);
			calendarDto.setCalendar_schedule_writer_name(empNameDto.getEmpName());
			System.out.println("확인"+writerId);
			if((calendarDto.getCalendar_schedule_category() != 1) || (calendarDto.getCalendar_schedule_category() == 1 && calendarDto.getCalendar_schedule_writer().equals(writerId))) {
				eventDtoList.add(calendarDto);
				
			}
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
		return calendarRepository.save(calendar);
	}

}
