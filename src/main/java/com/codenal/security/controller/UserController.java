package com.codenal.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    // 로그인 페이지를 GET 요청으로 표시
    @GetMapping("/auth-signin")
    public String loginPage(@RequestParam(value = "error", required = false) String error, 
                            @RequestParam(value = "exception", required = false) String exception, 
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 잘못 입력되었습니다.");
        }
        if (exception != null) {
            model.addAttribute("exceptionMessage", exception);
        }

        return "auth-signin-basic"; // 로그인 페이지 템플릿으로 이동
    }

  
}
