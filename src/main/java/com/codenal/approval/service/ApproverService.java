package com.codenal.approval.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.codenal.approval.domain.Approval;
import com.codenal.approval.domain.Approver;
import com.codenal.approval.repository.ApprovalRepository;
import com.codenal.approval.repository.ApproverRepository;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.repository.EmployeeRepository;

import jakarta.transaction.Transactional;
	
@Service
public class ApproverService {
	
	private final ApproverRepository approverRepository;
	private final ApprovalRepository approvalRepository;
	private final EmployeeRepository employeeRepository;
	
	public ApproverService(ApproverRepository approverRepository,ApprovalRepository approvalRepository,EmployeeRepository employeeRepository) {
		this.approverRepository = approverRepository;
		this.approvalRepository = approvalRepository;
		this.employeeRepository = employeeRepository;
	}
	
	public void createApprover(Map<String, List<Long>> obj, Approval createdApproval) {
		
		// 합의자, 결재자 담아오기
		List<Long> agree = (List<Long>)obj.get("합의자");
		List<Long> approver = (List<Long>)obj.get("결재자");
		
		// 합의자 등록
		for(int i=0; i<agree.size();i++) {
			Long id = agree.get(i);
			System.out.println(id);
			Employee emp = employeeRepository.findByEmpId(id);
			Approver approvers = Approver.builder()
									.approval(createdApproval)
									.approverPriority(i+1)
									.approverType("합의자")
									.employee(emp)
									.build();
			
			approverRepository.save(approvers);
		}
		
		// 결재자 등록
		for(int i=0; i<approver.size();i++) {
			Long id = approver.get(i);
			Employee emp = employeeRepository.findByEmpId(id);
			Approver approvers = Approver.builder()
									.approval(createdApproval)
									.approverPriority(agree.size()+i+1)
									.approverType("결재자")
									.employee(emp)
									.build();
			
			approverRepository.save(approvers);
		}
		
		
	}
	
	
	// approver 1순위 -> 2순위
	@Transactional
	public int consentApprover(Long no, Long loginId) {
		int status = 1;
		// 전자결재 테이블 상태 변경
		int ape = approvalRepository.updateStatus(status, no);

		// 현재 approver 우선순위 상태 확인
		int currentPriority = approverRepository.findApproverPriority(no, loginId);

		// 결재자 카운트
		Long approverCount = approverRepository.countApprover(no);

		// 결재자 테이블 상태 변경
		LocalDateTime ldt = LocalDateTime.now();
		int app = approverRepository.updateStatus(status, ldt, no, loginId);

		if (app == 0) {
			// 다음 우선순위 결재자가 있는지 확인
			if (currentPriority < approverCount) {
				int nextPriority = currentPriority + 1;
				int nextApproverCount = approverRepository.findByApprovalNoAndPriority(no, nextPriority, loginId);

				if (nextApproverCount > 0) {
					// 다음 우선순위 결재자 상태 업데이트
					approverRepository.updateStatus(status, ldt, no, loginId);
				}
			}else {
				approverRepository.updateStatus(status, ldt, no, loginId);
				status = 3;
				approvalRepository.updateStatus(status,no);
			}
		}
		System.out.println("ape : " + ape);
		System.out.println("app" + app);

		return app;
	}
}
