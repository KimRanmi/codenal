package com.codenal.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.employee.domain.Departments;

public interface DepartmentsRepository extends JpaRepository<Departments, Long>{
	
	Departments findByDeptNo(Long deptNo);

}
