package com.codenal.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codenal.admin.domain.Departments;
import com.codenal.admin.domain.DepartmentsDto;
import com.codenal.admin.repository.DepartmentsRepository;

@Service
public class DeptService {
	
	private final DepartmentsRepository departmentsRepository;
	
	@Autowired
	public DeptService (DepartmentsRepository departmentsRepository) {
		this.departmentsRepository = departmentsRepository;
	}
	

	public Page<DepartmentsDto> searchDeptName(DepartmentsDto searchDto, Pageable pageable) {
		
		// 부서명 검색
		Page<Departments> deptNameSearch = departmentsRepository.findByDeptNameContaining(searchDto.getDeptName(), pageable);

		List<DepartmentsDto> deptNameSearchList = new ArrayList<>();
		for (Departments d : deptNameSearch) {
			DepartmentsDto dto = DepartmentsDto.fromEntity(d);
			deptNameSearchList.add(dto);
		}

		return new PageImpl<>(deptNameSearchList, pageable, deptNameSearch.getTotalElements());
	}
}
