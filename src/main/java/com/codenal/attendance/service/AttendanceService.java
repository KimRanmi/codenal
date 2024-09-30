package com.codenal.attendance.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codenal.attendance.domain.Attendance;
import com.codenal.attendance.domain.AttendanceDto;
import com.codenal.attendance.repository.AttendanceRepository;
import com.codenal.security.vo.SecurityUser;

@Service
public class AttendanceService {

	private final AttendanceRepository attendanceRepository;
	
	// 출근 기준 시간과 퇴근 기준 시간 설정
    private final LocalTime standardStartTime = LocalTime.of(9, 0); // 오전 9시
    private final LocalTime standardEndTime = LocalTime.of(18, 0); // 오후 6시
    

	@Autowired
	public AttendanceService(AttendanceRepository attendanceRepository) {
		this.attendanceRepository = attendanceRepository;
	}

	  // 현재 사용자의 empId를 가져오는 메서드
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
            SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
            return userDetails.getEmpId(); // empId를 반환
        } else {
            throw new IllegalStateException("인증된 사용자가 아닙니다.");
        }
    }
 // 출근하기 메서드
    @Transactional
    public void checkIn() {
        Long empId = getCurrentUserId();
        LocalDate today = LocalDate.now();

        // 이미 출근 기록이 있는지 확인
        boolean exists = attendanceRepository.existsByEmpIdAndWorkDate(empId, today);
        if (exists) {
            throw new IllegalStateException("이미 오늘 출근 처리가 되어 있습니다.");
        }

        LocalTime now = LocalTime.now();

        Attendance attendance = Attendance.builder()
                .empId(empId)
                .workDate(today) // LocalDate 타입 사용
                .attendStartTime(now)
                .attendStatus(determineStatus(now))
                .build();

        attendanceRepository.save(attendance);
    }
	
    // 퇴근하기 메서드
    @Transactional
    public void checkOut() {
        Long empId = getCurrentUserId();
        LocalDate today = LocalDate.now();

        // 오늘 출근 기록이 있는지 확인
        Attendance attendance = attendanceRepository.findByEmpIdAndWorkDate(empId, today)
                .orElseThrow(() -> new IllegalStateException("출근 기록이 없습니다. 먼저 출근 체크를 해주세요."));

        // 이미 퇴근한 경우 예외 처리
        if (attendance.getAttendEndTime() != null) {
            throw new IllegalStateException("이미 퇴근 처리가 완료되었습니다.");
        }

        LocalTime now = LocalTime.now();

        attendance.setAttendEndTime(now);

        // 근무 시간 계산 (시간 단위로 계산)
        BigDecimal workingHours = calculateWorkingHours(attendance.getAttendStartTime(), now);
        attendance.setAttendWorkingTime(workingHours);

        attendanceRepository.save(attendance);
    }
    
    // 근무 상태 결정 메서드
    private String determineStatus(LocalTime checkInTime) {
        if (checkInTime.isBefore(standardStartTime) || checkInTime.equals(standardStartTime)) {
            return "정상출근";
        } else {
            return "지각";
        }
    }
    
    // 근무 시간 계산 메서드
    private BigDecimal calculateWorkingHours(LocalTime startTime, LocalTime endTime) {
        long seconds = java.time.Duration.between(startTime, endTime).getSeconds();
        BigDecimal hours = BigDecimal.valueOf(seconds).divide(BigDecimal.valueOf(3600), 2, BigDecimal.ROUND_HALF_UP);
        return hours;
    }
    
 // 모든 출퇴근 기록을 조회하는 메서드
    public Page<AttendanceDto> getAllAttendances(Pageable pageable) {
        Long empId = getCurrentUserId();
        Page<Attendance> attendances = attendanceRepository.findByEmpId(empId, pageable);
        return attendances.map(AttendanceDto::fromEntity);
    }

    // 특정 날짜의 출퇴근 기록을 조회하는 메서드
    public Page<AttendanceDto> getAttendancesByDate(LocalDate date, Pageable pageable) {
        Long empId = getCurrentUserId();

        // 날짜 기준으로 조회
        Page<Attendance> attendances = attendanceRepository.findByEmpIdAndWorkDate(empId, date, pageable);
        return attendances.map(AttendanceDto::fromEntity);
    }

    // 특정 날짜 범위의 출퇴근 기록을 조회하는 메서드
    public Page<AttendanceDto> getAttendancesByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Long empId = getCurrentUserId();

        Page<Attendance> attendances = attendanceRepository.findByEmpIdAndWorkDateBetween(empId, startDate, endDate, pageable);
        return attendances.map(AttendanceDto::fromEntity);
    }
	
	
}
