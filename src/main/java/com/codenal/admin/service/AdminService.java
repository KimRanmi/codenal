package com.codenal.admin.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codenal.admin.domain.Departments;
import com.codenal.admin.domain.Jobs;
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

	// ------------------ 신규 직원 등록
	public int createEmployee(EmployeeDto dto) {

		int result = -1;
		try {
			// 기본 비밀번호 'work1234'암호화
			if (dto.getEmpPw() == null || dto.getEmpPw().isEmpty()) {
				String defaultPassword = passwordEncoder.encode("work1234");
				dto.setEmpPw(defaultPassword);
			} else {
				// 비밀번호가 제공되었을 경우 암호화
				dto.setEmpPw(passwordEncoder.encode(dto.getEmpPw()));
			}

			// 1. emp_hire 날짜를 기반으로 6자리 값 생성 (YYMMDD)
			LocalDate hireDate = dto.getEmpHire();  // DTO에서 `emp_hire` 값을 가져옴
			String hireDateString = hireDate.format(DateTimeFormatter.ofPattern("yyMMdd"));

			// 2. 랜덤 숫자 4자리 생성
			String randomDigits = String.format("%04d", new Random().nextInt(10000));

			// 3. emp_hire 6자리 + 랜덤 숫자 4자리로 emp_id 생성
			Long empId = Long.parseLong(hireDateString + randomDigits);

			dto.setEmpId(empId);

			Employee employee = dto.toEntity();
			adminRepository.save(employee); // DB 저장

			result = 1;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}


	// ---------------- 직원 목록 검색 1 (재직 or 퇴사)
	public Page<EmployeeDto> searchByStatus(EmployeeDto searchDto, Pageable pageable) {
		Page<Employee> adminSearchOne = null;

		String searchText = searchDto.getEmpStatus();

		if (searchText != null && !"".equals(searchText)) {
			switch (searchText) {
			case "ALL" :	// 전체
				adminSearchOne = adminRepository.findAllByEmpAuth("USER", pageable);
				break;
			case "Y" :  // empStatus가 'Y'일 경우 (재직)
				adminSearchOne = adminRepository.findByEmpStatusAndEmpAuth("Y", "USER", pageable);
				break;
			case "N":  // empStatus가 'N'일 경우 (퇴사)
				adminSearchOne = adminRepository.findByEmpStatusAndEmpAuth("N", "USER", pageable);
				break;
			}
		} else {
			adminSearchOne = adminRepository.findAllByEmpAuth("USER", pageable);
		}


		List<EmployeeDto> adminSearchOneList = new ArrayList<>();
		for (Employee e : adminSearchOne) {
			EmployeeDto dto = EmployeeDto.fromEntity(e);
			adminSearchOneList.add(dto);
		}

		return new PageImpl<>(adminSearchOneList, pageable, adminSearchOne.getTotalElements());
	}


	// ------------------ 직원 목록 검색 2 (직원 정보)
	public Page<EmployeeDto> searchByEmployeeInfo(EmployeeDto searchDto, Pageable pageable) {
		Page<Employee> adminSearchTwo = null;

		String searchText = searchDto.getSearch_text();

		if (searchText != null && !"".equals(searchText)) {
			int searchType = searchDto.getSearch_type();

			switch (searchType) {
			case 1:
				adminSearchTwo = adminRepository.findAllByEmpAuth("USER", pageable);
				break;
			case 2:
				adminSearchTwo = adminRepository.findByEmpIdContainingAndEmpAuth(searchText, "USER", pageable);
				break;
			case 3:
				adminSearchTwo = adminRepository.findByEmpNameContainingAndEmpAuth(searchText, "USER", pageable);
				break;
			case 4:
				adminSearchTwo = adminRepository.findByDepartments_DeptNameContainingAndEmpAuth(searchText, "USER", pageable);
				break;
			case 5:
				adminSearchTwo = adminRepository.findByJobs_JobNameContainingAndEmpAuth(searchText, "USER", pageable);
				break;
			case 6:
				adminSearchTwo = adminRepository.findByEmpPhoneContainingAndEmpAuth(searchText, "USER", pageable);
				break;
			}
		} else {
			adminSearchTwo = adminRepository.findAllByEmpAuth("USER", pageable);
		}


		List<EmployeeDto> adminSearchTwoList = new ArrayList<EmployeeDto>();
		for (Employee e : adminSearchTwo) {
			EmployeeDto dto = EmployeeDto.fromEntity(e);
			adminSearchTwoList.add(dto);
		}

		return new PageImpl<>(adminSearchTwoList, pageable, adminSearchTwo.getTotalElements());
	}


	// ---------------- 직원 검색 통합 (재직/퇴사 + 직원 정보)
	public Page<EmployeeDto> searchAll(EmployeeDto searchDto, Pageable pageable) {

		if (searchDto.getSearch_type() == 0) {
			// 재직 상태 검색
			return searchByStatus(searchDto, pageable);
		} else {
			// 직원 정보 검색
			return searchByEmployeeInfo(searchDto, pageable);
		}
	}


	// ---------------- 직원 정보 상세 조회
	public EmployeeDto employeeDetail(Long empId) {

		Employee e = adminRepository.findByEmpId(empId);
		return EmployeeDto.fromEntity(e);
	}


	// ---------------- 직원 정보 수정
	@Transactional
	public Employee employeeUpdate(EmployeeDto dto) { 
	
		EmployeeDto temp = selectUpdate(dto.getEmpId());

		temp.setEmpName(dto.getEmpName());
		temp.setDeptNo(dto.getDeptNo());
		temp.setJobNo(dto.getJobNo());

		Employee e = temp.toEntity();
		return adminRepository.save(e);
	}

	public EmployeeDto selectUpdate(Long empId) {
		
		Employee e = adminRepository.findByEmpId(empId);

		EmployeeDto dto = EmployeeDto.builder()
				.empId(e.getEmpId())
				.empName(e.getEmpName())
				.deptNo(e.getDepartments().getDeptNo())
				// .deptName(e.getDepartments().getDeptName()) 
				.jobNo(e.getJobs().getJobNo())
				// .jobName(e.getJobs().getJobName()) 
				.build();

		return dto;
	}





}