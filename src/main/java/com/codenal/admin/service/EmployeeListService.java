package com.codenal.admin.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.codenal.admin.repository.EmployeeListRepository;
import com.codenal.employee.domain.Employee;

@Service
public class EmployeeListService {

    private final EmployeeListRepository employeeListRepository;

    public EmployeeListService(EmployeeListRepository employeeListRepository) {
        this.employeeListRepository = employeeListRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeListRepository.findAll();
    }
}