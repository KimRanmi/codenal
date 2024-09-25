package com.codenal.attendance.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codenal.attendance.domain.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	
	Page<Attendance> findByEmpId(Long empId, Pageable pageable);
	
	    Page<Attendance> findByEmpIdAndWorkDateBetween(Long empId, LocalDateTime startOfDay, LocalDateTime endOfDay, Pageable pageable);
}