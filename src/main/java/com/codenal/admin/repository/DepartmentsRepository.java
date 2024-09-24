package com.codenal.admin.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codenal.admin.domain.Departments;
import com.codenal.admin.domain.DepartmentsCount;

@Repository
public interface DepartmentsRepository extends JpaRepository<Departments, Long> {

	//  직원 정보 수정
	Departments findByDeptNo(Long deptNo);


	// 주소록
	// 1. JsTree
	List<Departments> findAll();

	// 2. 부서명 검색
	Page<Departments> findByDeptNameContaining(String deptName, Pageable pageable);


	// 3. 부서별 직원 수
	// 부서별 직원 수를 포함하여 전체 조회 (empStatus가 'Y'인 직원만 집계)
	@Query("SELECT d.deptNo AS deptNo, d.deptName AS deptName, d.deptCreateDate AS deptCreateDate, COUNT(e) AS empCount " +
	       "FROM Departments d LEFT JOIN Employee e ON e.departments = d AND e.empStatus = 'Y' " +
	       "GROUP BY d.deptNo, d.deptName, d.deptCreateDate")
	Page<DepartmentsCount> findAllWithEmployeeCount(Pageable pageable);

	// 부서명으로 검색하면서 직원 수를 포함하여 조회 (empStatus가 'Y'인 직원만 집계)
	@Query("SELECT d.deptNo AS deptNo, d.deptName AS deptName, d.deptCreateDate AS deptCreateDate, COUNT(e) AS empCount " +
	       "FROM Departments d LEFT JOIN Employee e ON e.departments = d AND e.empStatus = 'Y' " +
	       "WHERE d.deptName LIKE %:deptName% " +
	       "GROUP BY d.deptNo, d.deptName, d.deptCreateDate")
	Page<DepartmentsCount> findByDeptNameContainingWithEmployeeCount(@Param("deptName") String deptName, Pageable pageable);

	
	// 부서 추가
	boolean existsByDeptName(String deptName);

}
