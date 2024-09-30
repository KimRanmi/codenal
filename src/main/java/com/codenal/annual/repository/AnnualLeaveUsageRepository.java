package com.codenal.annual.repository;



import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.annual.domain.AnnualLeaveUsage;

public interface AnnualLeaveUsageRepository extends JpaRepository <AnnualLeaveUsage ,Long> {
	
	AnnualLeaveUsage getAnnualLeaveUsageByApproval_ApprovalNo(Long approvalNo);
	
	 List<AnnualLeaveUsage> findByEmployee_EmpIdAndAnnualUsageStartDateLessThanEqualAndAnnualUsageEndDateGreaterThanEqual(
		        Long empId, LocalDate startDate, LocalDate endDate);
}
