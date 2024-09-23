package com.codenal.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codenal.admin.domain.Departments;

@Repository
public interface DepartmentsRepository extends JpaRepository<Departments, Long> {

	// 1. 직원 정보 수정
	Departments findByDeptNo(Long deptNo);
	
	// 2. JsTree
    List<Departments> findAll();
}
    
