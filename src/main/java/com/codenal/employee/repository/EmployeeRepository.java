package com.codenal.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codenal.employee.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee findByEmpId(int empId);
    
    @Query(value = "select e from Employee e where e.empId = ?1")
    Employee findByempName(Long empId);
}