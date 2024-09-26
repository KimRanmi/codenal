package com.codenal.attendance.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codenal.attendance.domain.Attendance;
import com.codenal.attendance.domain.AttendanceDto;
import com.codenal.attendance.repository.AttendanceRepository;
import com.codenal.attendance.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceApiController {	

    @Autowired
    private AttendanceService attendanceService;
    
    @Autowired
    private AttendanceRepository attendanceRepository;

    /**
     * 특정 날짜 범위의 출퇴근 기록을 조회하는 API
     * @param startDate 시작 날짜 (yyyy-MM-dd 형식)
     * @param endDate 종료 날짜 (yyyy-MM-dd 형식)
     * @return 출퇴근 기록 목록
     */
    @GetMapping("/records")
    public ResponseEntity<Page<AttendanceDto>> getAttendancesByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 10) Pageable pageable) {
        Page<AttendanceDto> attendances = attendanceService.getAttendancesByDateRange(startDate, endDate, pageable);
        return ResponseEntity.ok(attendances);
    }

    /**
     * 사용자의 전체 출퇴근 기록을 조회하는 API
     * @return 출퇴근 기록 목록
     */
    @GetMapping("/all")
    public ResponseEntity<Page<AttendanceDto>> getAllAttendances(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<AttendanceDto> attendances = attendanceService.getAllAttendances(pageable);
        return ResponseEntity.ok(attendances);
    }
    
 // 출근하기
    @PostMapping("/check-in")
    public ResponseEntity<String> checkIn() {
        try {
            attendanceService.checkIn();
            return ResponseEntity.ok("출근 처리가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 퇴근하기
    @PostMapping("/check-out")
    public ResponseEntity<String> checkOut() {
        try {
            attendanceService.checkOut();
            return ResponseEntity.ok("퇴근 처리가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 출퇴근 상태 확인
    @GetMapping("/status")
    public ResponseEntity<String> getAttendanceStatus() {
        Long empId = getCurrentUserId();
        LocalDate today = LocalDate.now();

        Optional<Attendance> attendanceOpt = attendanceRepository.findByEmpIdAndWorkDate(empId, today.atStartOfDay());

        if (attendanceOpt.isPresent()) {
            Attendance attendance = attendanceOpt.get();
            if (attendance.getAttendEndTime() != null) {
                return ResponseEntity.ok("퇴근완료");
            } else {
                return ResponseEntity.ok("출근완료");
            }
        } else {
            return ResponseEntity.ok("미출근");
        }
    }

    // 현재 사용자 ID를 가져오는 메서드
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String empIdString = authentication.getName(); // 현재 사용자의 empId를 가져옴
        return Long.parseLong(empIdString); // empId를 Long 타입으로 변환하여 반환
    }
}
