package com.codenal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.codenal.employee.domain.Employee;
import com.codenal.employee.repository.EmployeeRepository;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class CodenalApplication {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(CodenalApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // 모든 직원의 비밀번호를 확인하고 암호화하여 저장
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            if (!employee.getEmpPw().startsWith("$2a$")) { // 비밀번호가 이미 암호화되지 않았다면
                String rawPassword = employee.getEmpPw();
                String encodedPassword = passwordEncoder.encode(rawPassword);
                employee.setEmpPw(encodedPassword);
                employeeRepository.save(employee);	
            }
        }
    }
}
