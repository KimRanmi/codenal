package com.codenal.approval.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.codenal.annual.domain.AnnualLeaveManage;
import com.codenal.annual.domain.AnnualLeaveUsage;
import com.codenal.annual.repository.AnnualLeaveManageRepository;
import com.codenal.annual.repository.AnnualLeaveUsageRepository;
import com.codenal.approval.domain.Approval;
import com.codenal.approval.domain.Approver;
import com.codenal.approval.domain.Referrer;
import com.codenal.approval.repository.ApprovalRepository;
import com.codenal.approval.repository.ApproverRepository;
import com.codenal.approval.repository.ReferrerRepository;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

@Service
public class ApproverService {

	private final ApproverRepository approverRepository;
	private final ApprovalRepository approvalRepository;
	private final EmployeeRepository employeeRepository;
	private final ReferrerRepository referrerRepository;
	private final AnnualLeaveUsageRepository annualLeaveUsageRepository;
	private final AnnualLeaveManageRepository annualLeaveManageRepository;

	public ApproverService(ApproverRepository approverRepository, ApprovalRepository approvalRepository,
			EmployeeRepository employeeRepository, ReferrerRepository referrerRepository,
			AnnualLeaveUsageRepository annualLeaveUsageRepository,
			AnnualLeaveManageRepository annualLeaveManageRepository) {
		this.approverRepository = approverRepository;
		this.approvalRepository = approvalRepository;
		this.employeeRepository = employeeRepository;
		this.referrerRepository = referrerRepository;
		this.annualLeaveUsageRepository = annualLeaveUsageRepository;
		this.annualLeaveManageRepository = annualLeaveManageRepository;
	}

	// 결재자, 합의자 등록 (상태 수정)
	@Transactional
	public void createApprover(Map<String, List<Long>> obj, Approval createdApproval) {

		// 합의자, 결재자 담아오기
		List<Long> agree = (List<Long>) obj.get("합의자");
		List<Long> approver = (List<Long>) obj.get("결재자");

		// 합의자 등록 (합의자는 없을 수도 있음)
		if (!agree.isEmpty()) {
			Long firstId = agree.get(0);
			for (int i = 0; i < agree.size(); i++) {
				Long id = agree.get(i);
				System.out.println(id);
				Employee emp = employeeRepository.findByEmpId(id);
				Approver approvers = Approver.builder().approval(createdApproval).approverPriority(i + 1)
						.approverType("합의자").employee(emp).build();
				approverRepository.save(approvers);
			}
			approverRepository.firstUpdateStatus(firstId, createdApproval.getApprovalNo());

			// 결재자 등록
			for (int i = 0; i < approver.size(); i++) {
				Long id = approver.get(i);
				Employee emp = employeeRepository.findByEmpId(id);
				Approver approvers = Approver.builder().approval(createdApproval).approverPriority(agree.size() + i + 1)
						.approverType("결재자").employee(emp).build();

				approverRepository.save(approvers);
			}

		} else { // 합의자가 없을경우 결재자를 우선순위 1로 두기
			Long firstId = approver.get(0);
			for (int i = 0; i < approver.size(); i++) {
				Long id = approver.get(i);
				Employee emp = employeeRepository.findByEmpId(id);
				Approver approvers = Approver.builder().approval(createdApproval).approverPriority(agree.size() + i + 1)
						.approverType("결재자").employee(emp).build();

				approverRepository.save(approvers);
			}
			approverRepository.firstUpdateStatus(firstId, createdApproval.getApprovalNo());
		}

	}

	// 합의자 결재자 수정하기
	@Transactional
	public void updateApprover(Map<String, List<Long>> obj, Approval updateApproval) {
		// 합의자, 결재자 담아오기
		List<Long> agree = (List<Long>) obj.get("합의자");
		List<Long> approver = (List<Long>) obj.get("결재자");

		// approver 지우기
		List<Approver> approverList = approverRepository.findByApproval(updateApproval);
		for (Approver apv : approverList) {
			approverRepository.delete(apv);
		}

		// 합의자 등록 (합의자는 없을 수도 있음)
		if (!agree.isEmpty()) {
			Long firstId = agree.get(0);
			for (int i = 0; i < agree.size(); i++) {
				Long id = agree.get(i);
				System.out.println(id);
				Employee emp = employeeRepository.findByEmpId(id);
				Approver approvers = Approver.builder().approval(updateApproval).approverPriority(i + 1)
						.approverType("합의자").employee(emp).build();
				approverRepository.save(approvers);
			}
			approverRepository.firstUpdateStatus(firstId, updateApproval.getApprovalNo());

			// 결재자 등록
			for (int i = 0; i < approver.size(); i++) {
				Long id = approver.get(i);
				Employee emp = employeeRepository.findByEmpId(id);
				Approver approvers = Approver.builder().approval(updateApproval).approverPriority(agree.size() + i + 1)
						.approverType("결재자").employee(emp).build();

				approverRepository.save(approvers);
			}

		} else { // 합의자가 없을경우 결재자를 우선순위 1로 두기
			Long firstId = approver.get(0);
			for (int i = 0; i < approver.size(); i++) {
				Long id = approver.get(i);
				Employee emp = employeeRepository.findByEmpId(id);
				Approver approvers = Approver.builder().approval(updateApproval).approverPriority(agree.size() + i + 1)
						.approverType("결재자").employee(emp).build();

				approverRepository.save(approvers);
			}
			approverRepository.firstUpdateStatus(firstId, updateApproval.getApprovalNo());
		}

	}

	// 수신참조자 등록
	public void createReferences(List<Long> references, Approval createdApproval) {
		if (!references.isEmpty()) {
			for (int i = 0; i < references.size(); i++) {
				Long id = references.get(i);
				Employee emp = employeeRepository.findByEmpId(id);
				Referrer referrer = Referrer.builder().approval(createdApproval).employee(emp).build();

				System.out.println(referrer);
				referrerRepository.save(referrer);
			}
		}
	}

	// 수신참조자 수정
	public void updateReferrer(List<Long> referrers, Approval updateApproval) {

		// 기존 수신참조자 지우기
		List<Referrer> referrerList = referrerRepository.findByApproval(updateApproval);
		for (Referrer ref : referrerList) {
			referrerRepository.delete(ref);
		}

		// 생성 메서드 호출
		if (!referrers.isEmpty()) {
			createReferences(referrers, updateApproval);

		}
	}

	// 결재자 승인
	@Transactional
	public int consentApprover(Long no, Long loginId) {
		int status = 1;
		int approverStatus = 2;
		int app = 0;
		
		// 결재 시간
		LocalDateTime ldt = LocalDateTime.now();

		// 현재 approver 우선순위 상태 확인
		int currentPriority = approverRepository.findApproverPriority(no, loginId);

		// 결재자 카운트
		Long approverCount = approverRepository.countApprover(no);

		// 전자결재 상태 변경 (첫 번째 결재자일 경우)
		if (currentPriority == 1) {
			approvalRepository.updateStatus(status, no);
		}

		// 결재자 상태 변경
		app = approverRepository.updateStatus(approverStatus, ldt, no, loginId);

		// 다음 결재자 상태 변경
		approverRepository.updateNextEmpIdStatus(no, currentPriority + 1);

		// 마지막 결재자인 경우 전자결재 완료 처리
		if (currentPriority == approverCount) {
			
			// 휴가신청서인 경우 ..  이건 내일 다시 물어보기 !
			AnnualLeaveUsage alu = annualLeaveUsageRepository.getAnnualLeaveUsageByApproval_ApprovalNo(no);
			if(alu != null) {
				int result = annualLeaveManageRepository.updateDay(alu.getEmployee().getEmpId(),alu.getTotalDay());
				System.out.println("결과 : "+result);
			}
			approvalRepository.updateStatus(2, no);
		}

		return app;
	}
	
	// 결재자 반려
		@Transactional
		public int rejectApprover(Long no, Long loginId,String rejectText) {
			int approverStatus = 3;
			int app = 0;

			// 결재 시간
			LocalDateTime ldt = LocalDateTime.now();


			// 결재자 상태 변경
			app = approverRepository.updateReject(approverStatus, rejectText,ldt, no, loginId);
			
			
			if(app > 0) {
				
				approvalRepository.updateStatus(3, no);
			}

			return app;
		}
		
	// 반려 상태 가져오기
		public Approver findReject(Long approvalNo){
			return approverRepository.findByApprovalApprovalNoAndApprovalStatus(approvalNo,3);
		}
}