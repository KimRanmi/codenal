package com.codenal.annual.domain;

import java.time.LocalDate;
import java.util.List;

import com.codenal.approval.domain.Approval;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Table(name="annual_leave_usage")

public class AnnualLeaveUsage {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name="annual_usage_no")
	private Long annualUsageNo;
	
	@Column(name="annual_usage_start_date")
	private LocalDate annualUsageStartDate;
	
	@Column(name="annual_usage_end_date")
	private LocalDate annualUsageEndDate;
	
	@Column(name="emp_id")
	private Long empId;
	
	// 반차인지 연차인지 나타내는 컬럼
	@Column(name="annual_type")
	private int annualType;
	
	@OneToMany(mappedBy="annualLeaveUsage")
	private List<Approval> approvals;
}
