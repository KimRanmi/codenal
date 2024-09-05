package com.codenal.admin.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	public AdminService(PasswordEncoder passwordEncoder,
			AdminRepository adminRepository) {
		this.passwordEncoder = passwordEncoder;
		this.adminRepository = adminRepository;
	}

	// 신규 직원 등록
	public int createEmployee(EmployeeDto dto) {
		System.out.println("Received hireDate: 1. " + dto.getEmpHire());

		int result = -1;
		// for. 메서드 실행 중 예외가 발생하거나, 예상치 못한 상황에서 기본적으로 실패 나타냄

		try {
			System.out.println("Received hireDate: 2. " + dto.getEmpHire());

			if (dto.getDeptNo() == null) {
				dto.setDeptNo(1); // 적절한 기본 부서 번호 설정
			}

			if (dto.getJobNo() == null) {
				dto.setJobNo(1); // 적절한 기본 직책 번호 설정
			}

			 // 비밀번호가 제공되었을 때만 암호화 (비밀번호가 null이 아닌 경우)
	        if (dto.getEmpPw() != null && !dto.getEmpPw().isEmpty()) {
	            String encodedPassword = passwordEncoder.encode(dto.getEmpPw());
	            dto.setEmpPw(encodedPassword);
	        }

	        // 1. emp_hire 날짜를 기반으로 6자리 값 생성 (YYMMDD)
	        LocalDate hireDate = dto.getEmpHire();  // DTO에서 `emp_hire` 값을 가져옴
	        String hireDateString = hireDate.format(DateTimeFormatter.ofPattern("yyMMdd"));

	        // 2. 랜덤 숫자 4자리 생성
	        String randomDigits = String.format("%04d", new Random().nextInt(10000));

	        // 3. emp_hire 6자리 + 랜덤 숫자 4자리로 emp_id 생성
	        Long empId = Long.parseLong(hireDateString + randomDigits);

	        // DTO를 엔티티로 변환하기 전에 emp_id 설정
	        dto.setEmpId(empId);
	        
			// DTO를 엔티티로 변환하고 저장
			Employee employee = dto.toEntity();
			adminRepository.save(employee); // DB 저장

			result = 1;


		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
		
	}

	// 입사일 datePicker DB에 넣기
//	public void saveHireDate(LocalDate hireDate) {
	//	Employee employee = Employee.builder()
		//							.empHire(hireDate) // 고용일 설정
			//						.build();
		//adminRepository.save(employee);  // JPA를 통해 DB에 저장
	//}
}