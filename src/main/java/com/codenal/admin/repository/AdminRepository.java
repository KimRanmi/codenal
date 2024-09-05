package com.codenal.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.employee.domain.Employee;
// 데이터베이스에 접근하는 역할
// CRUD
public interface AdminRepository extends JpaRepository<Employee, Integer>{

	
}