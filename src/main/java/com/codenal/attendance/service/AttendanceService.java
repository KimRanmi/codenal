package com.codenal.attendance.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codenal.annual.domain.AnnualLeaveUsage;
import com.codenal.annual.repository.AnnualLeaveUsageRepository;
import com.codenal.attendance.domain.Attendance;
import com.codenal.attendance.domain.AttendanceDto;
import com.codenal.attendance.repository.AttendanceRepository;
import com.codenal.security.vo.SecurityUser;

@Service
public class AttendanceService {

	private final AttendanceRepository attendanceRepository;
	
	private final AnnualLeaveUsageRepository annualLeaveUsageRepository;
	
	// 출근 기준 시간과 퇴근 기준 시간 설정
    private final LocalTime standardStartTime = LocalTime.of(9, 0); // 오전 9시
    private final LocalTime standardEndTime = LocalTime.of(18, 0); // 오후 6시
    

	@Autowired
	public AttendanceService(AttendanceRepository attendanceRepository, AnnualLeaveUsageRepository annualLeaveUsageRepository) {
		 this.attendanceRepository = attendanceRepository;
	        this.annualLeaveUsageRepository = annualLeaveUsageRepository;
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
                .attendStatus(determineStatus(empId, today, now))
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

 		// 근무 시간 계산 (BigDecimal로 계산)
 		BigDecimal workingHours = calculateWorkingHoursAsBigDecimal(attendance.getAttendStartTime(), now);
 		attendance.setAttendWorkingTime(workingHours);

 		attendanceRepository.save(attendance);
 	}
        
 // 근무시간을 "HH:mm" 형식으로 변환하는 메서드 (출력 시 사용)
 	public String formatHoursMinutes(BigDecimal totalHours) {
 	    long hours = totalHours.longValue();
 	    long minutes = totalHours.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(60)).longValue();
 	    return String.format("%02d:%02d", hours, minutes);
 	}

 	// 근무시간을 BigDecimal로 계산하는 메서드
 	private BigDecimal calculateWorkingHoursAsBigDecimal(LocalTime startTime, LocalTime endTime) {
 	    if (startTime != null && endTime != null) {
 	        Duration duration = Duration.between(startTime, endTime);
 	        long seconds = duration.getSeconds(); // 총 초로 계산
 	        BigDecimal hours = BigDecimal.valueOf(seconds).divide(BigDecimal.valueOf(3600), 2, BigDecimal.ROUND_HALF_UP);
 	        return hours;
 	    } else {
 	        return BigDecimal.ZERO;
 	    }
 	}
    
    // 연차 여부 확인 및 상태 결정
    public String determineStatus(Long empId, LocalDate workDate, LocalTime attendStartTime) {
        // 연차 사용 내역 확인
        List<AnnualLeaveUsage> annualLeaveList = annualLeaveUsageRepository
                .findByEmployee_EmpIdAndAnnualUsageStartDateLessThanEqualAndAnnualUsageEndDateGreaterThanEqual(
                        empId, workDate, workDate);

        if (!annualLeaveList.isEmpty()) {  // 연차 내역이 존재하는지 확인
            return "연차";  // 연차일 경우 연차로 상태 설정
        } else if (attendStartTime != null) {
            if (attendStartTime.isBefore(standardStartTime)) {
                return "정상출근";
            } else {
                return "지각";
            }
        } else if (workDate.isBefore(LocalDate.now())) {
            return "결근";
        }
        return "미출근";
    }
    
 // 해당 날짜의 출근 기록이 있는지 확인하고 없으면 결근으로 처리
    public String checkAttendanceStatus(Long empId, LocalDate workDate) {
        Optional<Attendance> attendanceOpt = attendanceRepository.findByEmpIdAndWorkDate(empId, workDate);
        if (attendanceOpt.isPresent()) {
            Attendance attendance = attendanceOpt.get();
            return determineStatus(empId, workDate, attendance.getAttendStartTime());
        } else if (workDate.isBefore(LocalDate.now())) {
            // 해당 날짜의 기록이 없고 날짜가 지났다면 결근 처리
            return "결근";
        }
        return "미출근";
    }
    
 // 근무 시간 계산 메서드
    private String calculateWorkingHours(LocalTime startTime, LocalTime endTime) {
        // 출근 시간과 퇴근 시간이 모두 존재할 때만 계산
        if (startTime != null && endTime != null) {
            Duration duration = Duration.between(startTime, endTime);
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();  // Java 9 이상에서 사용 가능

            // 결과를 "HH:mm" 형식으로 반환
            return String.format("%02d:%02d", hours, minutes);
        } else {
            // 퇴근 시간이 없는 경우, 근무 시간을 "-"로 표시
            return "-";
        }
    }
    
    public Page<AttendanceDto> getAllAttendances(Long empId, Pageable pageable) {
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
    
    // 현재 월의 시작일과 종료일을 계산하는 메서드
    private LocalDate getStartOfCurrentMonth() {
        YearMonth currentMonth = YearMonth.now();
        return currentMonth.atDay(1);
    }

    private LocalDate getEndOfCurrentMonth() {
        YearMonth currentMonth = YearMonth.now();
        return currentMonth.atEndOfMonth();
    }


    
    
    // 정상 출근 횟수 조회
public long getNormalAttendanceCount(Long empId) {
    LocalDate startDate = getStartOfCurrentMonth();
    LocalDate endDate = getEndOfCurrentMonth();
    return attendanceRepository.countByEmpIdAndAttendStatusAndWorkDateBetween(empId, "정상출근", startDate, endDate);
}

    // 지각 횟수 조회
public long getLateAttendanceCount(Long empId) {
    LocalDate startDate = getStartOfCurrentMonth();
    LocalDate endDate = getEndOfCurrentMonth();
    return attendanceRepository.countByEmpIdAndAttendStatusAndWorkDateBetween(empId, "지각", startDate, endDate);
}


    // 연차 횟수 조회
public long getAnnualLeaveCount(Long empId) {
    LocalDate startDate = getStartOfCurrentMonth();
    LocalDate endDate = getEndOfCurrentMonth();
    return attendanceRepository.countByEmpIdAndAttendStatusAndWorkDateBetween(empId, "연차", startDate, endDate);
}
//    결근한 횟수 가져오기 (출근 기록이 없거나 결근으로 표시된 경우)
//    public Long getAbsentCount(Long empId) {
//        return attendanceRepository.countByEmpIdAndAttendStatus(empId, Attendance.ABSENT);
//    }
    // 연차 처리 메서드
    @Transactional
    public void applyAnnualLeave(Long empId, LocalDate date) {
        Attendance attendance = Attendance.builder()
                .empId(empId)
                .workDate(date)
                .attendStatus(Attendance.ANNUAL_LEAVE)
                .build();
        
        attendanceRepository.save(attendance);
    }

    // 결근 상태 자동 처리 (출근 기록이 없는 경우)
    public void markAbsent(Long empId, LocalDate date) {
        if (!attendanceRepository.existsByEmpIdAndWorkDate(empId, date)) {
            Attendance attendance = Attendance.builder()
                    .empId(empId)
                    .workDate(date)
                    .attendStatus(Attendance.ABSENT)
                    .build();
            attendanceRepository.save(attendance);
        }
    }
	
}