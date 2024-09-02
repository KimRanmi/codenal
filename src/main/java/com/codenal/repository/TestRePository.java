package com.codenal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.domain.Calendar;

public interface TestRePository extends JpaRepository<Calendar, Long> {

}
