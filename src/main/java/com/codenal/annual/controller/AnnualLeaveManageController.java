package com.codenal.annual.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codenal.annual.domain.AnnualLeaveManage;
import com.codenal.annual.domain.AnnualLeaveUsageDto;
import com.codenal.annual.service.AnnualLeaveManageService;
import com.codenal.annual.service.AnnualLeaveUsageService;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.service.EmployeeService;
import com.codenal.security.vo.SecurityUser;

@Controller
public class AnnualLeaveManageController {

    @Autowired
    private AnnualLeaveUsageService annualLeaveUsageService;

    @Autowired
    private AnnualLeaveManageService annualLeaveManageService;

    @Autowired
    private EmployeeService employeeService;

    // 현재 로그인한 사용자 ID를 가져오는 메서드
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
            SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
            return userDetails.getEmpId();
        } else {
            throw new IllegalStateException("인증된 사용자가 아닙니다.");
        }
    }

    @GetMapping("/apps-annual-leave")
    public String showAnnualLeavePage(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,
            @PageableDefault(size = 10, sort = "annualUsageStartDate", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        Long empId = getCurrentUserId();

        // 사용자 정보 가져오기
        Employee employee = employeeService.getEmployeeById(empId);

        // 연차 관리 정보 가져오기
        AnnualLeaveManage annualLeaveManage = annualLeaveManageService.getAnnualLeaveManageById(empId);

        // 선택된 연도와 월 설정
        YearMonth selectedYearMonth;
        if (year != null && month != null) {
            selectedYearMonth = YearMonth.of(year, month);
        } else {
            selectedYearMonth = YearMonth.now();
        }

        // 연차 사용 내역 조회
        Page<AnnualLeaveUsageDto> annualLeaveUsages;
        if (startDate != null && endDate != null) {
            // 날짜 범위가 지정된 경우 해당 기간의 데이터를 조회
            annualLeaveUsages = annualLeaveUsageService.getAnnualLeaveUsageByDateRange(empId, startDate, endDate, pageable);
        } else {
            // 날짜 범위가 지정되지 않은 경우, year와 month를 사용하여 해당 월의 데이터를 조회
            LocalDate monthStart = selectedYearMonth.atDay(1);
            LocalDate monthEnd = selectedYearMonth.atEndOfMonth();
            annualLeaveUsages = annualLeaveUsageService.getAnnualLeaveUsageByDateRange(empId, monthStart, monthEnd, pageable);
        }

        // 모델에 데이터 추가
        model.addAttribute("employee", employee);
        model.addAttribute("annualLeaveManage", annualLeaveManage);
        model.addAttribute("annualLeaveUsages", annualLeaveUsages);

        model.addAttribute("currentPage", annualLeaveUsages.getNumber());
        model.addAttribute("totalPages", annualLeaveUsages.getTotalPages());
        model.addAttribute("startDate", startDate);  // startDate가 null일 수 있음
        model.addAttribute("endDate", endDate);      // endDate도 null일 수 있음

        model.addAttribute("selectedYear", selectedYearMonth.getYear());
        model.addAttribute("selectedMonth", selectedYearMonth.getMonthValue());

        // 현재 선택된 연도와 월을 "yyyy.MM" 형식으로 포맷
        String currentMonthStr = selectedYearMonth.format(DateTimeFormatter.ofPattern("yyyy.MM"));
        model.addAttribute("currentMonth", currentMonthStr);

        return "apps/annual_manage";
    }
}
