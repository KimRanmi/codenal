package com.codenal.admin.domain;

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
@Builder
@ToString
public class DepartmentsDto {

	private Long deptNo; // 부서 번호
    private String deptName; // 부서명
    private LocalDate deptCreateDate; // 부서 개설일

    public static DepartmentsDto fromEntity(Departments departments) {
        return DepartmentsDto.builder()
                .deptNo(departments.getDeptNo())
                .deptName(departments.getDeptName())
                .deptCreateDate(departments.getDeptCreateDate())
                .build();
    }

    public Departments toEntity() {
        return Departments.builder()
                .deptNo(deptNo)
                .deptName(deptName)
                .deptCreateDate(deptCreateDate != null ? deptCreateDate : LocalDate.now())
                .build();
    }
}