package com.codenal.attendance.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codenal.attendance.domain.AttendanceDto;
import com.codenal.attendance.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceApiController {

    @Autowired
    private AttendanceService attendanceService;

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
}
