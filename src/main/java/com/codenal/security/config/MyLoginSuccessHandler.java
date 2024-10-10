package com.codenal.security.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.codenal.employee.service.EmployeeService;
import com.codenal.security.vo.SecurityUser;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MyLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final EmployeeService employeeService;

    public MyLoginSuccessHandler(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 사용자 정보 가져오기
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        // 세션 타임아웃을 설정할 필요 없음, remember-me는 Spring Security에서 쿠키로 관리

        // 인증 성공 후 리다이렉트
        response.sendRedirect("/");
    }
}
