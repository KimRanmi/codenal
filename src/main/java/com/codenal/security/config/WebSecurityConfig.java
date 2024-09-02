package com.codenal.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import com.codenal.security.service.SecurityService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers( "/auth-signin-basic", "/assets/**", "/assets/css/**", "/assets/js/**", "/assets/images/**").permitAll()  
                .anyRequest().authenticated()
            )
            .formLogin(login ->
                login.loginPage("/auth-signin-basic")
                .loginProcessingUrl("/auth-signin-basic")
                .usernameParameter("emp_id")
                .passwordParameter("emp_pw")
                .permitAll()
                .defaultSuccessUrl("/", true)
                .successHandler(myLoginSuccessHandler())  // 빈으로 등록된 핸들러 사용
                .failureHandler(myLoginFailureHandler())  // 빈으로 등록된 핸들러 사용
            )
            .logout((logout) -> logout
                .logoutUrl("/auth-logout-basic")
                .logoutSuccessUrl("/auth-signin-basic?auth-logout-basic=true")
                .permitAll()
            )
            .rememberMe((rememberMe) -> rememberMe
                .key("uniqueAndSecret")
                .tokenValiditySeconds(86400)
                .userDetailsService(userDetailsService)  // 여기에서 UserDetailsService를 설정
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new SecurityService();  
    }

    @Bean
    public MyLoginSuccessHandler myLoginSuccessHandler() {
        return new MyLoginSuccessHandler();
    }

    @Bean
    public MyLoginFailureHandler myLoginFailureHandler() {
        return new MyLoginFailureHandler();
    }
}
