package com.codenal.security.config;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.codenal.security.service.SecurityService;

import jakarta.servlet.MultipartConfigElement;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private final DataSource dataSource;

	@Autowired
	public WebSecurityConfig(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityService securityService) throws Exception {
		http
		// CORS 설정 적용
		.cors(cors -> cors.configurationSource(corsConfigurationSource()))
		.authorizeHttpRequests((requests) -> requests

				.requestMatchers("/auth-signin-basic", "/assets/**").permitAll()
				.requestMatchers("/admin/list").permitAll()
				.requestMatchers("/announce/createEnd").permitAll()
				.requestMatchers("/announce/delete/**").permitAll()
				.requestMatchers("/announce/updateEnd/**").permitAll()
				.requestMatchers("/list").permitAll() 
				.requestMatchers("/mypage/**").authenticated()
				.requestMatchers("/documents/**").authenticated() 
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
				.tokenValiditySeconds(86400*7)
				.userDetailsService(securityService)
				);


		return http.build();
	}

	// CORS 설정을 위한 Bean 정의
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:3000")); // 허용할 도메인 설정
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // 허용할 메서드 설정
		configuration.setAllowedHeaders(List.of("*")); // 허용할 헤더 설정
		configuration.setAllowCredentials(true); // 자격 증명 허용 설정

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration); // 모든 경로에 적용

		return source;
	}
	@Bean
	public MyLoginSuccessHandler myLoginSuccessHandler() {
		return new MyLoginSuccessHandler();
	}

	@Bean
	public MyLoginFailureHandler myLoginFailureHandler() {
		return new MyLoginFailureHandler();
	}

	@Bean
	public PersistentTokenRepository tokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
		jdbcTokenRepository.setDataSource(dataSource);
		return jdbcTokenRepository;
	}



	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}