package com.codenal.employee.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.codenal.calendar.domain.Calendar;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "calendar")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Departments {
	
	@Id
	@Column(name = "dept_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long deptNo;
	
	@Id
	@Column(name = "dept_name")
	private String deptName;
	
	@Id
	@Column(name = "dept_create_date")
	private LocalDate deptCreateDate;

}
