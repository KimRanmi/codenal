package com.codenal.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codenal.calendar.domain.Calendar;
import com.codenal.employee.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByEmpId(Long empId);
    
}