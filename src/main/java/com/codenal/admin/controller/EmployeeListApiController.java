package com.codenal.admin.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codenal.admin.service.EmployeeListService;
import com.codenal.employee.domain.Employee;

@RestController
public class EmployeeListApiController {

    private final EmployeeListService employeeListService;

    public EmployeeListApiController(EmployeeListService employeeListService) {
        this.employeeListService = employeeListService;
    }

    @GetMapping("/api/employees")
    public List<Employee> getEmployeeList() {
        return employeeListService.getAllEmployees();
    }
}
