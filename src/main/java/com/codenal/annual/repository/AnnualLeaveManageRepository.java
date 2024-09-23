package com.codenal.annual.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codenal.annual.domain.AnnualLeaveManage;
import com.codenal.employee.domain.Employee;

public interface AnnualLeaveManageRepository extends JpaRepository <AnnualLeaveManage, Long>{
	
	AnnualLeaveManage findByEmployee_EmpId(Long empId);
}
