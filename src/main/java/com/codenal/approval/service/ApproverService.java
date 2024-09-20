package com.codenal.approval.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.codenal.approval.domain.Approval;
import com.codenal.approval.domain.Approver;
import com.codenal.approval.repository.ApprovalRepository;
import com.codenal.approval.repository.ApproverRepository;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.repository.EmployeeRepository;
	
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
									.Employee(emp)
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
									.Employee(emp)
									.build();
			
			approverRepository.save(approvers);
		}
		
		
	}
}
