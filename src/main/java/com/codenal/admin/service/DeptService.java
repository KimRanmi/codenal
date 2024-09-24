package com.codenal.admin.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codenal.admin.domain.Departments;
import com.codenal.admin.domain.DepartmentsCount;
import com.codenal.admin.domain.DepartmentsDto;
import com.codenal.admin.repository.DepartmentsRepository;

@Service
public class DeptService {

	private final DepartmentsRepository departmentsRepository;

	@Autowired
	public DeptService (DepartmentsRepository departmentsRepository) {
		this.departmentsRepository = departmentsRepository;
	}


	// 부서 목록
	public Page<DepartmentsDto> searchDeptName(DepartmentsDto searchDto, Pageable pageable) {

		Page<DepartmentsCount> deptNameSearch;

		if (searchDto.getDeptName() == null || searchDto.getDeptName().isEmpty()) {
			// 부서명 검색어가 없을 경우 전체 조회
			deptNameSearch = departmentsRepository.findAllWithEmployeeCount(pageable);
		} else {
			// 부서명으로 검색
			deptNameSearch = departmentsRepository.findByDeptNameContainingWithEmployeeCount(searchDto.getDeptName(), pageable);
		}

		//System.out.println("(서비스)부서 수: " + deptNameSearch.getContent().size());

		Page<DepartmentsDto> deptNameSearchList = deptNameSearch.map(projection -> {
			DepartmentsDto dto = new DepartmentsDto();
			dto.setDeptNo(projection.getDeptNo());
			dto.setDeptName(projection.getDeptName());
			dto.setDeptCreateDate(projection.getDeptCreateDate());
			dto.setEmpCount(projection.getEmpCount() != null ? projection.getEmpCount().intValue() : 0); 
			return dto;
		});

		return deptNameSearchList;
	}


	// 부서 추가
	public int addDepartment(DepartmentsDto dto) {
		try {
			// 중복 체크 로직
			if (departmentsRepository.existsByDeptName(dto.getDeptName())) {
				throw new IllegalArgumentException("이미 존재하는 부서명입니다.");
			}

			Departments d = Departments.builder()
					.deptName(dto.getDeptName())
					.deptCreateDate(LocalDate.now())
					.build();
			departmentsRepository.save(d);

			return 1;

		} catch (IllegalArgumentException e) {
			// 중복된 부서명이 있을 때 처리
		//	System.out.println("Error: " + e.getMessage());
			return 0;
		} catch (Exception e) {
			// 그 외의 일반적인 에러 처리
			//System.out.println("Error: " + e.getMessage());
			return 0;
		}
	}
	
	
	// 부서명 수정만 처리하는 메서드
	public void editDepartment(DepartmentsDto departmentsDto) {
        // 부서 번호로 엔티티를 생성하여 업데이트 작업
        Departments department = departmentsRepository.findByDeptNo(departmentsDto.getDeptNo());

        if (department == null) {
            throw new IllegalArgumentException("존재하지 않는 부서입니다.");
        }

        // 부서명 수정
        department.setDeptName(departmentsDto.getDeptName());
        departmentsRepository.save(department); // 변경된 부서 정보 저장
    }
}
