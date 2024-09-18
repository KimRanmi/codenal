package com.codenal.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.admin.domain.Departments;

public interface DepartmentsRepository extends JpaRepository<Departments, Integer> {

}
