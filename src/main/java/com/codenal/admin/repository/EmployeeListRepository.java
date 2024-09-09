package com.codenal.admin.repository;

import com.codenal.employee.domain.Employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeListRepository extends JpaRepository<Employee, Long> {

	// 직원 목록 검색 1 (재직 or 퇴사)
    Page<Employee> findByEmpStatus(String empStatus, Pageable pageable);	// 재직 상태
	
	
	// 직원 목록 검색 2 (직원 정보)
    Page<Employee> findByEmpIdContaining(String empId, Pageable pageable);	// 사번

    Page<Employee> findByEmpNameContaining(String empName, Pageable pageable);	// 이름

    Page<Employee> findByDepartment_DeptNameContaining(String deptName, Pageable pageable);	// 부서명

    Page<Employee> findByJob_JobNameContaining(String jobName, Pageable pageable);	// 직급명

    Page<Employee> findByEmpPhoneContaining(String empPhone, Pageable pageable);	// 전화번호

    Page<Employee> findAll(Pageable pageable);	// 전체
    
    // 키워드로 직원 정보 검색
    @Query(value = "SELECT e FROM Employee e " +
            "JOIN e.department d " +
            "JOIN e.job j " +
            "WHERE e.empName LIKE CONCAT('%', :keyword, '%') " +
            "OR d.deptName LIKE CONCAT('%', :keyword, '%') " +
            "OR j.jobName LIKE CONCAT('%', :keyword, '%') " +
            "ORDER BY e.empHire DESC",
        countQuery = "SELECT COUNT(e) FROM Employee e " +
            "JOIN e.department d " +
            "JOIN e.job j " +
            "WHERE e.empName LIKE CONCAT('%', :keyword, '%') " +
            "OR d.deptName LIKE CONCAT('%', :keyword, '%') " +
            "OR j.jobName LIKE CONCAT('%', :keyword, '%')")
    Page<Employee> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
}