package com.codenal.annual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codenal.annual.repository.AnnualLeaveUsageRepository;
import com.codenal.approval.repository.ApprovalCategoryRepository;
import com.codenal.employee.repository.EmployeeRepository;

@Service
public class AnnualLeaveUsageService {
    private final EmployeeRepository employeeRepository;
    private final ApprovalCategoryRepository approvalCategoryRepository;
    private final AnnualLeaveUsageRepository annualLeaveUsageRepository;

    @Autowired
    public AnnualLeaveUsageService(
            EmployeeRepository employeeRepository,
            ApprovalCategoryRepository approvalCategoryRepository,
            AnnualLeaveUsageRepository annualLeaveUsageRepository) {
        this.employeeRepository = employeeRepository;
        this.approvalCategoryRepository = approvalCategoryRepository;
        this.annualLeaveUsageRepository = annualLeaveUsageRepository;
    }
    
    

    
}

