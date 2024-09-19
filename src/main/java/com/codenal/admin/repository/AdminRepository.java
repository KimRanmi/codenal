package com.codenal.admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.employee.domain.Employee;

public interface AdminRepository extends JpaRepository<Employee, Long> {

	// 1. 검색
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
	
	
	// 2. 상세 조회 // 정보 수정
    Employee findByEmpId(Long empId);

    
	// 3. TreeMenu
	// 특정 부서에 속한 직원
  //  List<Employee> findByDepartmentsAndEmpAuthAndEmpStatus(Departments department, String empAuth, String empStatus);
    
    // 부서 번호로 직원 조회
    List<Employee> findByDepartments_DeptNoAndEmpAuthAndEmpStatus(Long deptNo, String empAuth, String empStatus);

}
