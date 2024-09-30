package com.codenal.annual.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.annual.domain.AnnualLeaveUsage;

public interface AnnualLeaveUsageRepository extends JpaRepository <AnnualLeaveUsage ,Long> {
	
	AnnualLeaveUsage getAnnualLeaveUsageByApproval_ApprovalNo(Long approvalNo);
	
}
