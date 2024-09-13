package com.codenal.announce.domain;

import com.codenal.admin.domain.Departments;
import com.codenal.admin.domain.Jobs;

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
public class AnnounceReadAuthorityDto {

	private Announce announce;
	private Departments department;
	private Jobs job;
	
	public AnnounceReadAuthority toEntity() {
		return AnnounceReadAuthority.builder()
				.announce(announce)
				.department(department)
				.job(job)
				.build();
		 
	}
	
	public AnnounceReadAuthorityDto toDto(AnnounceReadAuthority announceReadAuthority) {
		return AnnounceReadAuthorityDto.builder()
				.announce(announceReadAuthority.getAnnounce())
				.department(announceReadAuthority.getDepartment())
				.job(announceReadAuthority.getJob())
				.build();
	}
}
