package com.codenal.attendance.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codenal.attendance.domain.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	
	// 로그인한 사용자의 모든 출퇴근 기록 조회
    Page<Attendance> findByEmpId(Long empId, Pageable pageable);

    // 로그인한 사용자의 특정 날짜 범위의 출퇴근 기록 조회
    Page<Attendance> findByEmpIdAndWorkDateBetween(Long empId, LocalDateTime startOfDay, LocalDateTime endOfDay, Pageable pageable);

    // 로그인한 사용자의 특정 날짜의 출근 기록 존재 여부 확인
    boolean existsByEmpIdAndWorkDate(Long empId, LocalDateTime workDate);

    // 로그인한 사용자의 특정 날짜의 출퇴근 기록 조회
    Optional<Attendance> findByEmpIdAndWorkDate(Long empId, LocalDateTime workDate);

    
}