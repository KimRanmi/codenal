package com.codenal.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

		System.out.println("(서비스)부서 수: " + deptNameSearch.getContent().size());

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

}