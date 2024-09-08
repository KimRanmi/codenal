package com.codenal.approval.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codenal.approval.domain.Approval;
import com.codenal.approval.domain.ApprovalBaseFormType;
import com.codenal.approval.domain.ApprovalForm;
import com.codenal.approval.repository.ApprovalRepository;
import com.codenal.employee.domain.Employee;

@Service
public class ApprovalService {
	
	private final ApprovalRepository approvalRepository;
	
	@Autowired
	public ApprovalService(ApprovalRepository approvalRepository) {
		this.approvalRepository = approvalRepository;
	}
	
	// 리스트 조회
	public Page<Map<String, Object>> selectApprovalList(Pageable pageable) {
	    Page<Object[]> approvalList = approvalRepository.findList(pageable);

	    List<Map<String, Object>> responseList = new ArrayList<>();
	    for (Object[] objects : approvalList.getContent()) {
	        Approval approval = (Approval) objects[0];
	        ApprovalForm approvalForm = (ApprovalForm) objects[1];

	        Map<String, Object> map = new HashMap<>();
	        map.put("approval", approval);
	        map.put("formType", approvalForm);
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
		result.put("baseForm",baseForm);

		return result;
	}

}
