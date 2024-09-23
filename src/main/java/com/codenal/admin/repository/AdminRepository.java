package com.codenal.admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
	
	
	// 2. 퇴사
	 // 직원의 상태(empStatus)를 'N'으로 업데이트하는 메서드
    @Modifying
    @Query("UPDATE Employee e SET e.empStatus = 'N' WHERE e.empId = :employeeId")
    void updateEmployeeStatusToN(@Param("employeeId") Long employeeId);
	
	
	// 3. 상세 조회 // 정보 수정
    Employee findByEmpId(Long empId);
  
 
	// 4. TreeMenu
    // 부서 번호로 직원 조회
    List<Employee> findByDepartments_DeptNoAndEmpAuthAndEmpStatus(Long deptNo, String empAuth, String empStatus);
    
    // 5. 주소록 (재직 중인 사람만)
    Page<Employee> findAllByEmpAuthAndEmpStatus(String empAuth, String empStatus, Pageable pageable);

    Page<Employee> findByEmpNameContainingAndEmpAuthAndEmpStatus(String empName, String empAuth, String empStatus, Pageable pageable);

    Page<Employee> findByDepartments_DeptNameContainingAndEmpAuthAndEmpStatus(String deptName, String empAuth, String empStatus, Pageable pageable);

    Page<Employee> findByJobs_JobNameContainingAndEmpAuthAndEmpStatus(String jobName, String empAuth, String empStatus, Pageable pageable);

    Page<Employee> findByEmpPhoneContainingAndEmpAuthAndEmpStatus(String empPhone, String empAuth, String empStatus, Pageable pageable);

}
