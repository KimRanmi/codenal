	package com.codenal.attendance.controller;
	
	import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codenal.attendance.domain.AttendanceDto;
import com.codenal.attendance.service.AttendanceService;
import com.codenal.security.vo.SecurityUser;
	
	@Controller
	
	public class AttendanceViewController {
	
		
		 	@Autowired
		    private AttendanceService attendanceService;
		 	
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
		
		    @GetMapping("/apps-attendance")
		    public String showAttendancePage(
		            @RequestParam(value = "year", required = false) Integer year,
		            @RequestParam(value = "month", required = false) Integer month,
		            @PageableDefault(size = 10, sort = "workDate", direction = Sort.Direction.DESC) Pageable pageable, 
		            Model model) {

		         Long empId = getCurrentUserId();

		        // 선택된 연도와 월이 없으면 현재 연도와 월로 설정
		        YearMonth selectedYearMonth;
		        if (year != null && month != null) {
		            selectedYearMonth = YearMonth.of(year, month);
		        } else {
		            selectedYearMonth = YearMonth.now();
		        }

		        // 시작일과 종료일 계산
		        LocalDate startDate = selectedYearMonth.atDay(1);
		        LocalDate endDate = selectedYearMonth.atEndOfMonth();

		        // 출퇴근 기록 조회
		        Page<AttendanceDto> attendances = attendanceService.getAttendancesByDateRange(empId, startDate, endDate, pageable);

		        // 모델에 데이터 추가
		        model.addAttribute("attendances", attendances);
		        model.addAttribute("currentPage", attendances.getNumber());
		        model.addAttribute("totalPages", attendances.getTotalPages());
		        model.addAttribute("selectedYear", selectedYearMonth.getYear());
		        model.addAttribute("selectedMonth", selectedYearMonth.getMonthValue());

		        // 현재 선택된 연도와 월을 "yyyy.MM" 형식으로 포맷
		        String currentMonth = selectedYearMonth.format(DateTimeFormatter.ofPattern("yyyy.MM"));
		        model.addAttribute("currentMonth", currentMonth);

		        return "apps/attendance";
		    }
	}