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
	private Departments departments;
	private Jobs jobs;
	
	public AnnounceReadAuthority toEntity() {
		return AnnounceReadAuthority.builder()
				.announce(announce)
				.departments(departments)
				.jobs(jobs)
				.build();
		 
	}
	
	public AnnounceReadAuthorityDto toDto(AnnounceReadAuthority announceReadAuthority) {
		return AnnounceReadAuthorityDto.builder()
				.announce(announceReadAuthority.getAnnounce())
				.departments(announceReadAuthority.getDepartments())
				.jobs(announceReadAuthority.getJobs())
				.build();
	}
}
