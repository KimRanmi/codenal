package com.codenal.annual.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codenal.annual.domain.AnnualLeaveManage;
import com.codenal.annual.domain.AnnualLeaveUsage;
import com.codenal.annual.domain.AnnualLeaveUsageDto;
import com.codenal.annual.repository.AnnualLeaveUsageRepository;
import com.codenal.approval.repository.ApprovalCategoryRepository;
import com.codenal.employee.repository.EmployeeRepository;

@Service
public class AnnualLeaveUsageService {
    private final EmployeeRepository employeeRepository;
    private final ApprovalCategoryRepository approvalCategoryRepository;
    private final AnnualLeaveUsageRepository annualLeaveUsageRepository;
    private final AnnualLeaveManageService annualLeaveManageService; 

    @Autowired
    public AnnualLeaveUsageService(
            EmployeeRepository employeeRepository,
            ApprovalCategoryRepository approvalCategoryRepository,
            AnnualLeaveUsageRepository annualLeaveUsageRepository, AnnualLeaveManageService annualLeaveManageService) {
        this.employeeRepository = employeeRepository;
        this.approvalCategoryRepository = approvalCategoryRepository;
        this.annualLeaveUsageRepository = annualLeaveUsageRepository;
        this.annualLeaveManageService = annualLeaveManageService; 
    }
    
    // 특정 사용자의 특정 날짜 범위의 연차 사용 내역 조회 (페이지네이션 적용)
    public Page<AnnualLeaveUsageDto> getAnnualLeaveUsageByDateRange(Long empId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<AnnualLeaveUsage> usages = annualLeaveUsageRepository
                .findByEmployee_EmpIdAndAnnualUsageStartDateLessThanEqualAndAnnualUsageEndDateGreaterThanEqual(
                    empId, startDate, endDate, pageable); // 올바른 순서

        return usages.map(AnnualLeaveUsageDto::toDto);
    }

    // 특정 사용자의 모든 연차 사용 내역 조회 (페이지네이션 적용)
    public Page<AnnualLeaveUsageDto> getAllAnnualLeaveUsage(Long empId, Pageable pageable) {
        Page<AnnualLeaveUsage> usages = annualLeaveUsageRepository.findByEmployee_EmpId(empId, pageable);
        return usages.map(AnnualLeaveUsageDto::toDto);
    }

    public void useAnnualLeave(Long empId, LocalDate startDate, LocalDate endDate, int annualType, String timePeriod) {
        // 사용 일수 계산
        Double daysToUse = calculateDaysToUse(startDate, endDate, annualType, timePeriod); // Double로 변경

        // 잔여 연차 확인 및 업데이트
        AnnualLeaveManage manage = annualLeaveManageService.getAnnualLeaveManageById(empId);
        
        if (manage == null) {
            // 필요한 경우 예외 처리 또는 초기화
            throw new IllegalStateException("해당 직원의 연차 관리 정보가 없습니다.");
        }

        if (manage.getAnnualRemainDay() >= daysToUse) {
            // 연차 사용 내역 저장
            AnnualLeaveUsage usage = AnnualLeaveUsage.builder()
                .employee(employeeRepository.findById(empId).orElseThrow(() -> new IllegalArgumentException("Invalid empId")))
                .annualUsageStartDate(startDate)
                .annualUsageEndDate(endDate)
                .annualType(annualType)
                .timePeriod(timePeriod)
                .totalDay(daysToUse) // Double 타입 전달
                .build();
            annualLeaveUsageRepository.save(usage);

            // 사용 연차 및 잔여 연차 업데이트
            manage.setAnnualUsedDay(manage.getAnnualUsedDay() + daysToUse);
            manage.setAnnualRemainDay(manage.getAnnualRemainDay() - daysToUse);
            annualLeaveManageService.save(manage);
        } else {
            throw new IllegalStateException("잔여 연차가 부족합니다.");
        }
    }
    private Double calculateDaysToUse(LocalDate startDate, LocalDate endDate, int annualType, String timePeriod) {
        if (annualType == 1) {
            // 반차의 경우 0.5일
            return 0.5;
        } else {
            // 연차의 경우 시작일과 종료일 사이의 일수 계산
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
            return (double) daysBetween;
        }
    }
}
