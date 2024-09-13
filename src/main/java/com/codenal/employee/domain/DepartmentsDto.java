package com.codenal.employee.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.codenal.calendar.domain.CalendarDto;

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
public class DepartmentsDto {
	
	private Long dept_no;
	private String dept_name;
	private LocalDate dept_create_date;
	
	public Departments toEntity() {
		return Departments.builder()
				.deptNo(dept_no)
				.deptName(dept_name)
				.deptCreateDate(dept_create_date)
				.build();
	}
	
	public DepartmentsDto toDto(Departments dept) {
		return DepartmentsDto.builder()
				.dept_no(dept.getDeptNo())
				.dept_name(dept.getDeptName())
				.dept_create_date(dept.getDeptCreateDate())
				.build();
	}

}
