package com.codenal.security.vo;



import org.springframework.security.core.userdetails.User;

import com.codenal.employee.domain.EmployeeDto;

import lombok.Getter;

@Getter
public class SecurityUser extends User {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private EmployeeDto employeeDto;

    public SecurityUser(EmployeeDto employeeDto) {
        super(String.valueOf(employeeDto.getEmpId()), employeeDto.getEmpPw(), employeeDto.getAuthorities());
        this.employeeDto = employeeDto;
    }
}
