package com.codenal.employee.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class JobsDto {
	
	private Long job_no;
	private String job_name;
	private Long job_priority;
	
	public Jobs toEntity() {
		return Jobs.builder()
				.jobNo(job_no)
				.jobName(job_name)
				.jobPriority(job_priority)
				.build();
	}
	
	public JobsDto toDto(Jobs job) {
		return JobsDto.builder()
				.job_no(job.getJobNo())
				.job_name(job.getJobName())
				.job_priority(job.getJobPriority())
				.build();
	}

}
