package com.codenal.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codenal.employee.domain.Employee;

public interface EmployeeListRepository extends JpaRepository<Employee, Long> {
}
