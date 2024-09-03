package com.codenal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CodenalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodenalApplication.class, args);
		
		// 비밀번호 인코더 생성
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		// 두 개의 원시 비밀번호를 배열로 준비
		String[] rawPasswords = {"work1234", "work1234"};
		
		// 각 비밀번호를 인코딩하여 출력
		for (int i = 0; i < rawPasswords.length; i++) {
			String encodedPassword = encoder.encode(rawPasswords[i]);
			System.out.println("Encoded password for account " + (i+1) + ": " + encodedPassword);
			
			
		}
	}
	
	
}
