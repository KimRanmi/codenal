package com.codenal.employee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codenal.employee.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // 필요시 커스텀 쿼리 추가 가능
    Employee findByEmpId(Long empId);
    
    @Query("SELECT e FROM Employee e WHERE e.empStatus = 'Y'")
    List<Employee> findAllActiveEmployees();
}