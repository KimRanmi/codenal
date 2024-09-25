package com.codenal.approval.domain;

import com.codenal.employee.domain.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder

public class ReferrerDto {
	
	private Long reffere_no;
	private Approval approval;
	private Employee employee;
	
	public Referrer toEntity() {
		return Referrer.builder()
				.approval(approval)
				.employee(employee)
				.referrerNo(reffere_no)
				.build();
	}
	
	public ReferrerDto toDto(Referrer r) {
		return ReferrerDto.builder()
				.approval(approval)
				.employee(employee)
				.reffere_no(r.getReferrerNo())
				.build();
	}

	
}
