package com.codenal.annual.domain;

import com.codenal.employee.domain.Employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Builder
@Setter
@Table(name="annual_leave_manage")

public class AnnualLeaveManage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="annual_manage_no")
	private Long annualManageNo;
	
	@Column(name="annual_total_day")
	private float annualTotalDay;
	
	@Column(name="annual_used_day")
	private float annualUsedDay;
	
	@Column(name="annual_remain_day")
	private float annualRemainDay;
	
	@ManyToOne
	@JoinColumn(name="emp_id")
	private Employee employee;
	
	
}
