package com.codenal.attendance.controller;

import java.time.LocalDate;

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

import com.codenal.attendance.domain.WorkHistoryDto;
import com.codenal.attendance.service.WorkHistoryService;
import com.codenal.security.vo.SecurityUser;

@Controller
public class WorkHistoryViewController {

    @Autowired
    private WorkHistoryService workHistoryService;

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

    // 근무 내역 조회 페이지
    @GetMapping("/apps-work-history")
    public String showWorkHistoryPage(
            @RequestParam(value = "startDate", required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @PageableDefault(size = 10, sort = "workHistoryDate", direction = Sort.Direction.DESC) Pageable pageable, 
            Model model) {

        Long empId = getCurrentUserId();
        Page<WorkHistoryDto> workHistories;

        // 날짜 범위가 지정된 경우와 그렇지 않은 경우 
        if (startDate != null && endDate != null) {
            workHistories = workHistoryService.getHistoriesByRange(empId, startDate, endDate, pageable);
        } else {
            workHistories = workHistoryService.getHistories(empId, pageable);
        }

        // 모델에 데이터를 추가하여 HTML에서 사용
        model.addAttribute("workHistories", workHistories);
        model.addAttribute("currentPage", workHistories.getNumber());
        model.addAttribute("totalPages", workHistories.getTotalPages());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "apps/workhistory"; 
    }
}
