package com.codenal.employee.domain;

import java.time.LocalDate;

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
@Table(name = "jobs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Jobs {
	
	@Id
	@Column(name = "job_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long jobNo;
	
	@Column(name = "job_name")
	private String jobName;
	
	@Column(name = "job_priority")
	private Long jobPriority;

}
