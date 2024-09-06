package com.codenal.approval.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codenal.approval.domain.Approval;
import com.codenal.approval.domain.ApprovalDto;
import com.codenal.approval.repository.ApprovalRepository;

@Service
public class ApprovalService {
	
	private final ApprovalRepository approvalRepository;
	
	@Autowired
	public ApprovalService(ApprovalRepository approvalRepository) {
		this.approvalRepository = approvalRepository;
	}
	
	public Page<ApprovalDto> selectApprovalList (Pageable pageable){
		Page<Approval> approvalList = approvalRepository.findAll(pageable);
		
		List<ApprovalDto> approvalDtoList = new ArrayList<ApprovalDto>();
		for(Approval a : approvalList.getContent()) {
			ApprovalDto dto = new ApprovalDto().toDto(a);
			approvalDtoList.add(dto);
		}
		
		return new PageImpl<>(approvalDtoList,pageable,approvalList.getTotalElements());
	}
	
	public ApprovalDto  selectApprovalNo(Long approval_no) {
		Approval approval = approvalRepository.findByapprovalNo(approval_no);
		
		ApprovalDto dto = ApprovalDto.builder()
							.approval_no(approval.getApprovalNo())
							.approval_title(approval.getApprovalTitle())
							.approval_content(approval.getApprovalContent())
							.approval_reg_date(approval.getApprovalRegDate())
							.approval_status(approval.getApprovalStatus())
							.build();
		return dto;
	}
}
