package com.codenal.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class JobsDto {

    private Integer jobNo; // 직급 번호
    private String jobName; // 직급명
    private Integer jobPriority; // 우선순위

    // 엔티티를 DTO로 변환하는 메서드
    public static JobsDto fromEntity(Jobs jobs) {
        return JobsDto.builder()
                .jobNo(jobs.getJobNo())
                .jobName(jobs.getJobName())
                .jobPriority(jobs.getJobPriority())
                .build();
    }

    // DTO를 엔터티로 변환하는 메서드
    public Jobs toEntity() {
        return Jobs.builder()
                .jobNo(jobNo)
                .jobName(jobName)
                .jobPriority(jobPriority)
                .build();
    }
}
