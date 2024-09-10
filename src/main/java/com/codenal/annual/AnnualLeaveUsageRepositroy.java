package com.codenal.annual;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.annual.domain.AnnualLeaveUsage;

public interface AnnualLeaveUsageRepositroy extends JpaRepository <AnnualLeaveUsage, Long>{

}
