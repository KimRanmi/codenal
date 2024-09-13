package com.codenal.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codenal.employee.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // 필요시 커스텀 쿼리 추가 가능
    Employee findByEmpId(Long empId);

}