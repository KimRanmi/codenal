package com.codenal.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.employee.domain.Employee;

public interface AdminRepository extends JpaRepository<Employee, Long> {

	// 재직 상태
	Page<Employee> findByEmpStatusAndEmpAuth(String empStatus, String empAuth, Pageable pageable);	

	// 사번
	Page<Employee> findByEmpIdContainingAndEmpAuth(String empId, String empAuth, Pageable pageable);

	// 이름
	Page<Employee> findByEmpNameContainingAndEmpAuth(String empName, String empAuth, Pageable pageable);

	// 부서명
	Page<Employee> findByDepartments_DeptNameContainingAndEmpAuth(String deptName, String empAuth, Pageable pageable);	

	// 직급명
	Page<Employee> findByJobs_JobNameContainingAndEmpAuth(String jobName, String empAuth, Pageable pageable);

	// 전화번호
	Page<Employee> findByEmpPhoneContainingAndEmpAuth(String empPhone, String empAuth, Pageable pageable);	

	// 전체
	Page<Employee> findAllByEmpAuth(String empStatus, Pageable pageable);

}
