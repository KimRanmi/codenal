package com.codenal.attendance.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codenal.attendance.domain.WorkHistoryDto;
import com.codenal.attendance.service.WorkHistoryService;
import com.codenal.security.vo.SecurityUser;

@RestController
@RequestMapping("/api/work-history")
public class WorkHistoryApiController {	

    @Autowired
    private WorkHistoryService workHistoryService;

    // 현재 사용자의 empId를 가져오는 메서드
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
            SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
            return userDetails.getEmpId(); // SecurityUser에서 empId를 가져옴
        } else {
            throw new IllegalStateException("인증된 사용자가 아닙니다.");
        }
    }

    /**
     * 모든 근무 내역을 페이징하여 조회하는 API
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @param sort 정렬 필드
     * @return 페이징된 근무 내역 목록
     */
    @GetMapping("/all")
    public ResponseEntity<Page<WorkHistoryDto>> getAllHistories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "workHistoryDate") String sort) { 
        Long empId = getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<WorkHistoryDto> workHistories = workHistoryService.getHistories(empId, pageable);
        return ResponseEntity.ok(workHistories);
    }

    /**
     * 특정 날짜 범위의 근무 내역을 페이징하여 조회하는 API
     * @param startDate 시작 날짜 (yyyy-MM-dd 형식)
     * @param endDate 종료 날짜 (yyyy-MM-dd 형식)
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @param sort 정렬 필드
     * @return 페이징된 근무 내역 목록
     */
    @GetMapping("/records")
    public ResponseEntity<Page<WorkHistoryDto>> getHistoriesByRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 10, sort = "workHistoryDate", direction = Sort.Direction.DESC) Pageable pageable) { 
        Long empId = getCurrentUserId();
        Page<WorkHistoryDto> workHistories = workHistoryService.getHistoriesByRange(empId, startDate, endDate, pageable);
        return ResponseEntity.ok(workHistories);
    }
}
