package com.codenal.approval.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codenal.annual.AnnualLeaveUsageRepositroy;
import com.codenal.annual.domain.AnnualLeaveUsage;
import com.codenal.annual.domain.AnnualLeaveUsageDto;
import com.codenal.approval.domain.Approval;
import com.codenal.approval.domain.ApprovalBaseFormType;
import com.codenal.approval.domain.ApprovalCategory;
import com.codenal.approval.domain.ApprovalForm;
import com.codenal.approval.domain.ApprovalFormDto;
import com.codenal.approval.repository.ApprovalBaseFormTypeRepository;
import com.codenal.approval.repository.ApprovalCategoryRepository;
import com.codenal.approval.repository.ApprovalFormRepository;
import com.codenal.approval.repository.ApprovalRepository;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.repository.EmployeeRepository;

@Service
public class ApprovalService {

	private final ApprovalRepository approvalRepository;
	private final EmployeeRepository employeeRepository;
	private final ApprovalCategoryRepository approvalCategoryRepository;
	private final ApprovalFormRepository approvalFormRepository;
	private final ApprovalBaseFormTypeRepository approvalBaseFormTypeRepository;
	private final AnnualLeaveUsageRepositroy annualLeaveUsageRepositroy;

	@Autowired
	public ApprovalService(ApprovalRepository approvalRepository, 
			EmployeeRepository employeeRepository,
			ApprovalCategoryRepository approvalCategoryRepository,
			ApprovalFormRepository approvalFormRepository,
			ApprovalBaseFormTypeRepository approvalBaseFormTypeRepository,
			AnnualLeaveUsageRepositroy annualLeaveUsageRepositroy) {
		this.approvalRepository = approvalRepository;
		this.employeeRepository = employeeRepository;
		this.approvalCategoryRepository = approvalCategoryRepository;
		this.approvalFormRepository = approvalFormRepository;
		this.approvalBaseFormTypeRepository = approvalBaseFormTypeRepository;
		this.annualLeaveUsageRepositroy = annualLeaveUsageRepositroy;
	}

	// 리스트 조회
	public Page<Map<String, Object>> selectApprovalList(Pageable pageable,int num2) {
		Page<Object[]> approvalList = approvalRepository.findList(num2,pageable);

		List<Map<String, Object>> responseList = new ArrayList<>();
		for (Object[] objects : approvalList.getContent()) {
			Approval approval = (Approval) objects[0];
			ApprovalForm approvalForm = (ApprovalForm) objects[1];
			int num = approval.getApprovalStatus();
			Map<String, Object> map = new HashMap<>();
			map.put("approval", approval);
			map.put("formType", approvalForm);
			map.put("num", num);
			responseList.add(map);
		}

		return new PageImpl<>(responseList, pageable, approvalList.getTotalElements());
	}

	// 상세조회
	public Map<String, Object> detailApproval(Long approval_no) {
		List<Object[]> object = approvalRepository.selectByapprovalNo(approval_no);
		Map<String, Object> result = new HashMap<>();

		Object[] obj = object.get(0);
		Approval approval = (Approval) obj[0];
		Employee employee = (Employee) obj[1];
		ApprovalBaseFormType baseForm = (ApprovalBaseFormType) obj[2];

		result.put("approval", approval);
		result.put("employee", employee);
		result.put("baseForm", baseForm);

		return result;
	}

	// 전자결재 저장
	public Approval createApproval(Map<String,Object> obj) {
		
		Employee emp = employeeRepository.findByempName((Long)obj.get("이름"));
		System.out.println("출력 해 제발 !!!"+obj.get("폼코드"));
		
		ApprovalCategory ac = approvalCategoryRepository.findByCateCode((Integer)obj.get("폼코드"));
		System.out.println("카테고리 출력 : "+ac);
		Approval approval = Approval.builder()
							.approvalTitle((String)obj.get("제목"))
							.approvalContent((String)obj.get("내용"))
							.approvalCategory(ac)
							.employee(emp)
							.build();

		return approvalRepository.save(approval);
	}
	
	// 전자결재(휴가신청서) 저장
	public Approval createApprovalLeave(Map<String,Object> obj) {
			
			Employee emp = employeeRepository.findByempName((Long)obj.get("이름"));
			System.out.println("출력 해 제발 !!!"+obj.get("폼코드"));
			
			ApprovalForm af = approvalFormRepository.findByApprovalFormCode((Integer)obj.get("폼코드"));
			
			int type = 0;
			switch (af.getFormName()) {
            case "반차":
                type = 1;
                break;
            case "연차":
                type = 2;
                break;
            case "경조사휴가":
                type = 3;
                break;
            case "병가":
                type = 4;
                break; 
			}
			
			AnnualLeaveUsage annual = AnnualLeaveUsage.builder()
										.annualType(type)
										.employee(emp)
										.annualUsageStartDate((LocalDate)obj.get("시작일자"))
										.annualUsageEndDate((LocalDate)obj.get("종료일자"))
										.build();
										
			
			AnnualLeaveUsage au = annualLeaveUsageRepositroy.save(annual);
			ApprovalCategory ac = approvalCategoryRepository.findByCateCode((Integer)obj.get("폼코드"));
			System.out.println("카테고리 출력 : "+ac);
			Approval approval = Approval.builder()
								.approvalTitle((String)obj.get("제목"))
								.approvalContent((String)obj.get("내용"))
								.approvalCategory(ac)
								.employee(emp)
								.annualLeaveUsage(annual)
								.build();
	
			return approvalRepository.save(approval);
		}
	
	
	// 카테고리 값 가져오기
	public List<ApprovalFormDto> selectApprovalCateList(int no){
		
		ApprovalBaseFormType at = approvalBaseFormTypeRepository.findByBaseFormCode(no);
		
		List<ApprovalForm> ac = approvalFormRepository.findByApprovalBaseFormType_BaseFormCode(at.getBaseFormCode());
		
		List<ApprovalFormDto> list = new ArrayList<ApprovalFormDto>();
		
		
		 for(ApprovalForm a : ac) { 
			 ApprovalFormDto dto = ApprovalFormDto.builder()
					 					.form_code(a.getFormCode())
					 					.base_form_code(at.getBaseFormCode())
					 					.form_name(a.getFormName())
					 					.form_content(a.getFormContent())
					 					.build();
			 		list.add(dto); 					
		 }
		 
		 
		
		return list;
	}
	 

}
