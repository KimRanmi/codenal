package com.codenal.calendar.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codenal.calendar.domain.Calendar;

public interface CalendarRepository extends JpaRepository<Calendar, Long>{
	
//	@Query(value=" SELECT c.calendar_schedule_no , c.calendar_schedule_category , c.calendar_schedule_title , c.calendar_schedule_content , c.employee , e.emp_name , c.calendar_schedule_start_date , c.calendar_schedule_color , c.calendar_schedule_location"
//			+ " FROM Calendar c"
//			+ " JOIN employee e ON c.employee = e.empId")
//	List<Calendar> findByCalendar();

}
