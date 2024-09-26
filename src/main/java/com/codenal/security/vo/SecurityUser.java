package com.codenal.security.vo;

import java.util.Collections;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.codenal.employee.domain.EmployeeDto;

import lombok.Getter;

@Getter
public class SecurityUser extends User {

    private static final long serialVersionUID = 1L;
    
    private EmployeeDto employeeDto;
    
    public Long getEmpId() {
        return employeeDto.getEmpId();
    }

    public SecurityUser(EmployeeDto employeeDto) {
        // 권한이 없으면 빈 리스트를 전달하도록 설정
        super(String.valueOf(employeeDto.getEmpId()), 
              employeeDto.getEmpPw(), 
              employeeDto.getAuthorities() != null ? employeeDto.getAuthorities() : Collections.emptyList());
        this.employeeDto = employeeDto;
    }
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
            SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
            return userDetails.getEmpId(); // SecurityUser에서 empId를 가져옴
        } else {
            throw new IllegalStateException("인증된 사용자가 아닙니다.");
        }
}
}