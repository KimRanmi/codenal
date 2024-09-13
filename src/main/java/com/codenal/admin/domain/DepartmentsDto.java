package com.codenal.admin.domain;

import java.time.LocalDate;
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
public class DepartmentsDto {

    private Integer deptNo; // 부서 번호
    private String deptName; // 부서명
    private LocalDate deptCreateDate; // 부서 개설일

    // 엔티티를 DTO로 변환하는 메서드
    public static DepartmentsDto fromEntity(Departments departments) {
        return DepartmentsDto.builder()
                .deptNo(departments.getDeptNo())
                .deptName(departments.getDeptName())
                .deptCreateDate(departments.getDeptCreateDate())
                .build();
    }

    // DTO를 엔터티로 변환하는 메서드
    public Departments toEntity() {
        return Departments.builder()
                .deptNo(deptNo)
                .deptName(deptName)
                .deptCreateDate(deptCreateDate != null ? deptCreateDate : LocalDate.now())
                .build();
    }
}