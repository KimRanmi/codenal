package com.codenal.attendance.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codenal.attendance.domain.Attendance;
import com.codenal.attendance.domain.WorkHistory;
import com.codenal.attendance.domain.WorkHistoryDto;
import com.codenal.attendance.repository.AttendanceRepository;
import com.codenal.attendance.repository.WorkHistoryRepository;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.repository.EmployeeRepository;

@Service
public class WorkHistoryService {

    private final WorkHistoryRepository workHistoryRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;

    @Autowired
    public WorkHistoryService(WorkHistoryRepository workHistoryRepository, EmployeeRepository employeeRepository,AttendanceRepository attendanceRepository) {
        this.workHistoryRepository = workHistoryRepository;
        this.employeeRepository = employeeRepository;
		this.attendanceRepository = attendanceRepository;
    }
    
    // 근무 시간 기록 생성
    @Transactional
    public WorkHistoryDto startWork(Long empId, LocalDate date) {
        // Attendance에서 출근 시간 데이터를 가져옴
        Attendance attendance = attendanceRepository.findByEmpIdAndWorkDate(empId, date)
                .orElseThrow(() -> new IllegalStateException("출근 기록이 없습니다."));

        // 근무 내역 생성
        WorkHistory workHistory = WorkHistory.builder()
                .workHistoryDate(date)
                .workHistoryTotalTime(BigDecimal.ZERO)  // 초기 값 설정
                .employee(getEmployeeById(empId))
                .build();

        return WorkHistoryDto.fromEntity(workHistoryRepository.save(workHistory));
    }

    // 퇴근 시 Attendance 데이터를 기반으로 근무 시간과 초과근무 시간 계산
    @Transactional
    public WorkHistoryDto endWork(Long empId, LocalDate date) {
        // Attendance에서 출퇴근 시간 데이터를 가져옴
        Attendance attendance = attendanceRepository.findByEmpIdAndWorkDate(empId, date)
                .orElseThrow(() -> new IllegalStateException("출근 기록이 없습니다."));

        LocalTime startTime = attendance.getAttendStartTime();
        LocalTime endTime = attendance.getAttendEndTime();

        // 근무 시간 및 초과 근무 시간 계산
        BigDecimal workingMinutes = calculateWorkingMinutes(startTime, endTime);
        BigDecimal overtime = calculateOvertime(endTime);

        // 근무 기록이 존재하는지 확인하고 업데이트
        WorkHistory workHistory = workHistoryRepository.findByEmployee_EmpIdAndWorkHistoryDate(empId, date)
                .orElseGet(() -> new WorkHistory());  // 근무 기록이 없으면 새로 생성

        workHistory.setWorkHistoryDate(date);
        workHistory.setEmployee(getEmployeeById(empId));
        workHistory.setWorkHistoryTotalTime(workingMinutes != null ? workingMinutes : BigDecimal.ZERO);
        workHistory.setWorkHistoryOverTime(overtime != null ? overtime : BigDecimal.ZERO); // null 방지를 위한 기본값 설정

        return WorkHistoryDto.fromEntity(workHistoryRepository.save(workHistory));
    }

    
    // 근무 시간 및 초과 근무 시간 계산 및 업데이트
    private void updateWorkingAndOvertime(WorkHistory workHistory, LocalTime startTime, LocalTime endTime) {
        BigDecimal workingMinutes = calculateWorkingMinutes(startTime, endTime);
        workHistory.setWorkHistoryTotalTime(workingMinutes);

        BigDecimal overtime = calculateOvertime(endTime);
        workHistory.setWorkHistoryOverTime(overtime);
    }
    
    // 근무 시간 계산 (총 분 단위)
    private BigDecimal calculateWorkingMinutes(LocalTime startTime, LocalTime endTime) {
        if (startTime != null && endTime != null) {
            if (endTime.isBefore(startTime)) {
                endTime = endTime.plusHours(24);
            }

            Duration duration = Duration.between(startTime, endTime);
            long totalMinutes = duration.toMinutes();
            return BigDecimal.valueOf(totalMinutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);  // 시간 단위로 변환
        }
        return BigDecimal.ZERO;
    }

 // 초과 근무 시간 계산
    private BigDecimal calculateOvertime(LocalTime endTime) {
        LocalTime standardEndTime = LocalTime.of(18, 0);  // 오후 6시 기준
        if (endTime.isAfter(standardEndTime)) {
            Duration overtimeDuration = Duration.between(standardEndTime, endTime);
            long overtimeMinutes = overtimeDuration.toMinutes();
            return BigDecimal.valueOf(overtimeMinutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);  // 시간 단위로 변환
        }
        return BigDecimal.ZERO; // 초과 근무가 없으면 0 반환
    }

    // Employee ID를 이용해 Employee 조회
    private Employee getEmployeeById(Long empId) {
        return employeeRepository.findById(empId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Employee ID: " + empId));
    }
    // 현재 사용자의 근무 내역 페이징 조회
    public Page<WorkHistoryDto> getHistories(Long empId, Pageable pageable) {
        Page<WorkHistory> workHistoryPage = workHistoryRepository.findByEmployee_EmpId(empId, pageable);
        return workHistoryPage.map(WorkHistoryDto::fromEntity);
    }

    // 특정 날짜 범위의 근무 내역 조회
    public Page<WorkHistoryDto> getHistoriesByRange(Long empId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<WorkHistory> workHistoryPage = workHistoryRepository.findByEmployee_EmpIdAndWorkHistoryDateBetween(empId, startDate, endDate, pageable);
        return workHistoryPage.map(WorkHistoryDto::fromEntity);
    }
     	
    // WorkHistory 엔트리 찾거나 생성
    public WorkHistoryDto findOrCreateWorkHistoryByEmpIdAndDate(Long empId, LocalDate date) {
        Optional<WorkHistory> workHistoryOptional = workHistoryRepository.findByEmployee_EmpIdAndWorkHistoryDate(empId, date);
        if (workHistoryOptional.isPresent()) {
            return WorkHistoryDto.fromEntity(workHistoryOptional.get());
        } else {
            // 새로운 WorkHistory 생성
            Employee employee = getEmployeeById(empId);
            WorkHistory workHistory = WorkHistory.builder()
                    .workHistoryDate(date)
                    .employee(employee)
                    .build();
            return WorkHistoryDto.fromEntity(workHistoryRepository.save(workHistory));
        }
    }

    // WorkHistory 저장
    public WorkHistoryDto save(WorkHistoryDto workHistoryDto) {
        Employee employee = getEmployeeById(workHistoryDto.getEmpId());
        WorkHistory workHistory = workHistoryDto.toEntity(employee);
        return WorkHistoryDto.fromEntity(workHistoryRepository.save(workHistory));
    }
}
