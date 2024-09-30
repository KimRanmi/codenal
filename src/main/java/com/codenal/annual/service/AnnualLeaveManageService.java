package com.codenal.annual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codenal.annual.domain.AnnualLeaveManage;
import com.codenal.annual.repository.AnnualLeaveManageRepository;
import com.codenal.employee.domain.Employee;

@Service
public class AnnualLeaveManageService {
	
	private AnnualLeaveManageRepository annualLeaveManageRepository;
	
	@Autowired
	public AnnualLeaveManageService(AnnualLeaveManageRepository annualLeaveManageRepository) {
		this.annualLeaveManageRepository = annualLeaveManageRepository;
	}
	
	public AnnualLeaveManage getAnnualLeaveManageById(Long empId) {
		return annualLeaveManageRepository.findByEmployee_EmpId(empId);
	}
}
