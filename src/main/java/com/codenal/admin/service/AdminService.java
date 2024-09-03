package com.codenal.admin.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.codenal.admin.repository.AdminRepository;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;

@Service
public class AdminService {
	
	private final PasswordEncoder passwordEncoder;
	// 암호화
	// Spring Security 설정 클래스에서 PasswordEncoder를 빈으로 등록
	private final AdminRepository adminRepository;
	
	public AdminService(PasswordEncoder passwordEncoder, AdminRepository adminRepository) {
		this.passwordEncoder = passwordEncoder;
		this.adminRepository = adminRepository;
	}

	public int createEmployee(EmployeeDto dto) {
		
		int result = -1;
		// for. 메서드 실행 중 예외가 발생하거나, 예상치 못한 상황에서 기본적으로 실패 나타냄
		
		try {
			dto.setEmpPw(passwordEncoder.encode(dto.getEmpPw()));
			Employee employee = dto.toEntity();
			adminRepository.save(employee);
			result = 1;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
