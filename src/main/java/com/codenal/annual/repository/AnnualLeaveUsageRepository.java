package com.codenal.annual.repository;



import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.annual.domain.AnnualLeaveUsage;

public interface AnnualLeaveUsageRepository extends JpaRepository <AnnualLeaveUsage ,Long> {
	
	AnnualLeaveUsage getAnnualLeaveUsageByApproval_ApprovalNo(Long approvalNo);

	AnnualLeaveUsage findByAnnualUsageNo(Long annual_leave_usage_no);
	
	 List<AnnualLeaveUsage> findByEmployee_EmpIdAndAnnualUsageStartDateLessThanEqualAndAnnualUsageEndDateGreaterThanEqual(
		        Long empId, LocalDate startDate, LocalDate endDate);
}
