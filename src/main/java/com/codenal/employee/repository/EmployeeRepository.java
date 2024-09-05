package com.codenal.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codenal.employee.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee findByEmpId(int empId);
}