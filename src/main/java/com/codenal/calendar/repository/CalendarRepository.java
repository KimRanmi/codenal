package com.codenal.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.calendar.domain.Calendar;

public interface CalendarRepository extends JpaRepository<Calendar, Long>{

}
