package com.codenal.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.codenal.admin.service.EmployeeListService;

@Controller
public class EmployeeListViewController {

    private final EmployeeListService employeeListService;

    public EmployeeListViewController(EmployeeListService employeeListService) {
        this.employeeListService = employeeListService;
    }

    @GetMapping("/admin/employeeList")
    public String showEmployeeList(Model model) {
        model.addAttribute("employees", employeeListService.getAllEmployees());
        return "admin/employeeList";
    }
}