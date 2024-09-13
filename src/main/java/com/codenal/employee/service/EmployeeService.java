package com.codenal.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;
import com.codenal.employee.repository.EmployeeRepository;

@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository; }

    public void registerEmployee(EmployeeDto employeeDto) {
        // 비밀번호 암호화 제거, 원시 비밀번호를 그대로 사용
        Employee employee = employeeDto.toEntity();
        employeeRepository.save(employee);
    }

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee); // 데이터베이스에 변경 사항 저장
    }
    
    
    public Employee getEmployeeById(Long empId) {
        return employeeRepository.findByEmpId(empId);
    }

   


  

}