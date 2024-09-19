package com.codenal.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codenal.admin.service.AdminService;
import com.codenal.employee.domain.EmployeeDto;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    private final AdminService adminService;

    @Autowired
    public AdminViewController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 신규 직원 등록
    @GetMapping("/join")
    public String joinPage() {
        return "admin/join";
    }
    
//    @GetMapping("/list")
//    public String listPage() {
//        return "admin/list";
//    }

    // 직원 목록 검색 (재직/퇴사 + 직원 정보)
    @GetMapping("/list")
    public String searchAll(Model model, Pageable pageable) {
        Page<EmployeeDto> resultList = adminService.findAllEmployees(pageable);
        model.addAttribute("resultList", resultList);
        return "admin/list";
    }


    // 직원 정보 상세 조회
    @GetMapping("/detail/{empId}")
    public String employeeListDetail(@PathVariable("empId") Long empId, 
                                     Model model) {
        EmployeeDto employeeDetail = adminService.employeeDetail(empId);
        model.addAttribute("employeeDetail", employeeDetail);

        return "admin/detail";
    }

    // 직원 정보 수정
    @PostMapping("/update/{empId}")
    public String updateEmployee(@PathVariable("empId") Long empId, EmployeeDto employeeDto) {
        adminService.employeeUpdate(empId, employeeDto);
        return "redirect:/admin/detail/" + empId; // 상세 페이지로 리다이렉트
    }
}
