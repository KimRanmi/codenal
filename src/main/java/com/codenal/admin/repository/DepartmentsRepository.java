package com.codenal.admin.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codenal.admin.domain.Departments;

@Repository
public interface DepartmentsRepository extends JpaRepository<Departments, Long> {

	//  직원 정보 수정
	Departments findByDeptNo(Long deptNo);

	// 주소록
	// 1. JsTree
	List<Departments> findAll();

	// 2. 부서명 검색
	Page<Departments> findByDeptNameContaining(String deptName, Pageable pageable);
}


