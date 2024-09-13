package com.codenal.security.vo;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;

import com.codenal.employee.domain.EmployeeDto;

import lombok.Getter;

@Getter
public class SecurityUser extends User {

    private static final long serialVersionUID = 1L;
    
    private EmployeeDto employeeDto;

    public SecurityUser(EmployeeDto employeeDto) {
        // 권한이 없으면 빈 리스트를 전달하도록 설정
        super(String.valueOf(employeeDto.getEmpId()), 
              employeeDto.getEmpPw(), 
              employeeDto.getAuthorities() != null ? employeeDto.getAuthorities() : Collections.emptyList());
        this.employeeDto = employeeDto;
    }
}