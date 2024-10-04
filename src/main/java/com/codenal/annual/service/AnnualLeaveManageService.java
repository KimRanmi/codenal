package com.codenal.annual.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.codenal.annual.domain.AnnualLeaveManage;
import com.codenal.annual.domain.AnnualLeaveUsage;
import com.codenal.annual.repository.AnnualLeaveManageRepository;
import com.codenal.annual.repository.AnnualLeaveUsageRepository;
import com.codenal.approval.domain.ApprovalDto;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.repository.EmployeeRepository;

@Service
@EnableScheduling
public class AnnualLeaveManageService {
	
	private AnnualLeaveManageRepository annualLeaveManageRepository;
	
	private EmployeeRepository employeeRepository;
	
	private AnnualLeaveUsageRepository annualLeaveUsageRepository;
	
	 private final RestTemplate restTemplate;
		
	 @Autowired
	 public AnnualLeaveManageService(
	         RestTemplate restTemplate,
	         AnnualLeaveManageRepository annualLeaveManageRepository,
	         EmployeeRepository employeeRepository,
	         AnnualLeaveUsageRepository annualLeaveUsageRepository) {
	     this.restTemplate = restTemplate; 
	     this.annualLeaveManageRepository = annualLeaveManageRepository;
	     this.employeeRepository = employeeRepository;
	     this.annualLeaveUsageRepository = annualLeaveUsageRepository;
	 }
	
	public AnnualLeaveManage getAnnualLeaveManageById(Long empId) {
	    return annualLeaveManageRepository.findByEmployee_EmpId(empId);
	}
	
	// 연차 발생 및 소멸을 처리하는 메서드
    public void updateAnnualLeaveBalances() {
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            updateAnnualLeaveForEmployee(employee);
        }
    }

    private void updateAnnualLeaveForEmployee(Employee employee) {
        Long empId = employee.getEmpId();
        AnnualLeaveManage manage = annualLeaveManageRepository.findByEmployee_EmpId(empId);

        if (manage == null) {
            // 신규 직원인 경우 AnnualLeaveManage 생성
            manage = new AnnualLeaveManage();
            manage.setEmployee(employee);
            manage.setAnnualUsedDay(0f);
            annualLeaveManageRepository.save(manage);
        }

        // 총 연차 계산
        int totalAnnualLeave = calculateTotalAnnualLeave(employee);

        // 사용 연차 및 잔여 연차 계산
        float usedAnnualLeave = manage.getAnnualUsedDay();
        float remainingAnnualLeave = totalAnnualLeave - usedAnnualLeave;

        // 잔여 연차가 음수가 되지 않도록 조정
        remainingAnnualLeave = Math.max(remainingAnnualLeave, 0);

        // 업데이트
        manage.setAnnualTotalDay((float) totalAnnualLeave);
        manage.setAnnualRemainDay(remainingAnnualLeave);

        annualLeaveManageRepository.save(manage);
    }

    private int calculateTotalAnnualLeave(Employee employee) {
        LocalDate hireDate = employee.getEmpHire();
        LocalDate today = LocalDate.now();

        // 완료된 월 수 계산
        long monthsBetween = ChronoUnit.MONTHS.between(hireDate, today);
        if (today.getDayOfMonth() < hireDate.getDayOfMonth()) {
            monthsBetween -= 1;
        }
        monthsBetween = Math.max(monthsBetween, 0); // 음수 방지

        if (monthsBetween < 12) {
            // 첫 1년 동안 매월 1일씩 연차 발생 (최대 11일)
            return (int) Math.min(monthsBetween, 11);
        } else {
            // 1년 근무 시 연차 15일 발생
            int totalAnnualLeave = 15;

            // 이후 매 2년마다 1일씩 추가 연차 발생
            long additionalYears = (monthsBetween - 12) / 24;
            totalAnnualLeave += additionalYears;

            return totalAnnualLeave;
        }
    }

    public void resetAnnualLeaveIfAnniversary() {
        List<Employee> employees = employeeRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Employee employee : employees) {
            LocalDate hireDate = employee.getEmpHire();
            if (hireDate != null && hireDate.getMonthValue() == today.getMonthValue() && hireDate.getDayOfMonth() == today.getDayOfMonth()) {
                AnnualLeaveManage manage = annualLeaveManageRepository.findByEmployee_EmpId(employee.getEmpId());
                if (manage != null) {
                    // 연차 소멸: 사용하지 않은 연차를 0으로 설정
                    manage.setAnnualUsedDay(0f);
                    manage.setAnnualRemainDay(0f);

                    // 새로운 총 연차 계산 및 업데이트
                    int totalAnnualLeave = calculateTotalAnnualLeave(employee);
                    manage.setAnnualTotalDay((float) totalAnnualLeave);
                    manage.setAnnualRemainDay((float) totalAnnualLeave);

                    annualLeaveManageRepository.save(manage);
                }
            }
        }
    }
    // 매일 자정에 연차 정보를 업데이트
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledUpdateAnnualLeaveBalances() {
        updateAnnualLeaveBalances();
    }

    // 매일 자정에 연차 소멸 처리
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduledResetAnnualLeaveIfAnniversary() {
        resetAnnualLeaveIfAnniversary();
    }
    public void save(AnnualLeaveManage manage) {
        annualLeaveManageRepository.save(manage);
    }
    // 스케줄러 설정
    @Scheduled(fixedDelay = 60000) // 1분마다 실행
    public void processApprovedAnnualLeaves() {
        String url = "http://localhost:8083/api/approved-annual-leaves"; // Approval 시스템의 API URL

        ResponseEntity<List<ApprovalDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ApprovalDto>>() {}
        );

        List<ApprovalDto> approvals = response.getBody();
        if (approvals != null) {
            for (ApprovalDto approvalDto : approvals) {
                // 연차 사용 내역 가져오기
                Optional<AnnualLeaveUsage> optionalAlu = annualLeaveUsageRepository.findById(approvalDto.getAnnual_leave_usage_no());
                if (optionalAlu.isPresent()) {
                    AnnualLeaveUsage alu = optionalAlu.get();
                    if (!alu.isProcessed()) { // 처리 여부 확인
                        updateAnnualLeaveUsage(alu.getEmployee().getEmpId(), alu.getTotalDay());
                        alu.setProcessed(true); // 처리 완료 표시
                        annualLeaveUsageRepository.save(alu);
                    }
                }
            }
        }
    }
            
        
    

    public void updateAnnualLeaveUsage(Long empId, float usedDay) {
        AnnualLeaveManage manage = annualLeaveManageRepository.findByEmployee_EmpId(empId);

        if (manage != null) {
            manage.setAnnualUsedDay(manage.getAnnualUsedDay() + usedDay);
            manage.setAnnualRemainDay(manage.getAnnualRemainDay() - usedDay);
            // 잔여 연차가 음수가 되지 않도록 처리
            if (manage.getAnnualRemainDay() < 0) {
                manage.setAnnualRemainDay(0);
            }
            annualLeaveManageRepository.save(manage);
        } else {
            // 연차 관리 정보가 없을 경우 생성
            Employee employee = employeeRepository.findByEmpId(empId);
            manage = new AnnualLeaveManage();
            manage.setEmployee(employee);
            manage.setAnnualTotalDay(usedDay);
            manage.setAnnualUsedDay(usedDay);
            manage.setAnnualRemainDay(0);
            annualLeaveManageRepository.save(manage);
        }
    }
}
    

