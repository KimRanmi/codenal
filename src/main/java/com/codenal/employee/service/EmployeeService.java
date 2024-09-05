package com.codenal.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;
import com.codenal.employee.repository.EmployeeRepository;

@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;

    public void registerEmployee(EmployeeDto employeeDto) {
        // 비밀번호 암호화 제거, 원시 비밀번호를 그대로 사용
        Employee employee = employeeDto.toEntity();
        employeeRepository.save(employee);
    }
}