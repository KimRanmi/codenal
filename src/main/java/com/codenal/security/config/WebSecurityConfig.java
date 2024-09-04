package com.codenal.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.codenal.security.service.SecurityService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
   

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityService securityService) throws Exception {
        http
            .cors(Customizer.withDefaults())  // CORS 설정 추가
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((requests) -> requests
            		  .requestMatchers("/auth-signin-basic", "/assets/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(login ->
                login.loginPage("/auth-signin-basic")
                .loginProcessingUrl("/auth-signin-basic")
                .usernameParameter("emp_id")
                .passwordParameter("emp_pw")
                .permitAll()
                .defaultSuccessUrl("/", true)
                .successHandler(myLoginSuccessHandler())
                .failureHandler(myLoginFailureHandler())
            )
            .logout((logout) -> logout
                .logoutUrl("/auth-logout-basic")
                .logoutSuccessUrl("/auth-signin-basic?auth-logout-basic=true")
                .permitAll()
            )
            .rememberMe((rememberMe) -> rememberMe
                .key("uniqueAndSecret")
                .tokenValiditySeconds(86400)
                .userDetailsService(securityService)
            );
            

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
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