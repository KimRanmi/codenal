package com.codenal.attendance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.codenal.attendance.domain.Attendance;
import com.codenal.attendance.domain.AttendanceDto;
import com.codenal.attendance.repository.AttendanceRepository;

@Service
public class AttendanceService {

	private final AttendanceRepository attendanceRepository;

	@Autowired
	public AttendanceService(AttendanceRepository attendanceRepository) {
		this.attendanceRepository = attendanceRepository;
	}

	// 현재 사용자의 empId를 가져오는 메서드
	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String empIdString = authentication.getName();  // 현재 사용자의 empId를 가져옴
		return Long.parseLong(empIdString);  // empId를 Long 타입으로 변환하여 반환
	}
	
	
	// 모든 출퇴근 기록을 조회하는 메서드 (추가된 부분)
	 public Page<AttendanceDto> getAllAttendances(Pageable pageable) {
	        Long empId = getCurrentUserId();
	        Page<Attendance> attendances = attendanceRepository.findByEmpId(empId, pageable);
	        return attendances.map(AttendanceDto::fromEntity);
	    }

	// 특정 날짜의 출퇴근 기록을 조회하는 메서드
	 public Page<AttendanceDto> getAttendancesByDate(LocalDate date, Pageable pageable) {
		    Long empId = getCurrentUserId();

		    LocalDateTime startOfDay = date.atStartOfDay();
		    LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

		    Page<Attendance> attendances = attendanceRepository.findByEmpIdAndWorkDateBetween(empId, startOfDay, endOfDay, pageable);
		    return attendances.map(AttendanceDto::fromEntity);
		}

	// 특정 날짜 범위의 출퇴근 기록을 조회하는 메서드
	public Page<AttendanceDto> getAttendancesByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Long empId = getCurrentUserId();
        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);
        Page<Attendance> attendances = attendanceRepository.findByEmpIdAndWorkDateBetween(empId, startOfDay, endOfDay, pageable);
        return attendances.map(AttendanceDto::fromEntity);
    }
	
	
	
}
