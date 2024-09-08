package com.codenal.announce.domain;

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
	private int dept_no;
	private int job_no;
	
	
	public AnnounceReadAuthority toEntity() {
		return AnnounceReadAuthority.builder()
				.announce(announce)
				.deptNo(dept_no)
				.jobNo(job_no)
				.build();
		 
	}
	
	public AnnounceReadAuthorityDto toDto(AnnounceReadAuthority announceReadAuthority) {
		return AnnounceReadAuthorityDto.builder()
				.announce(announceReadAuthority.getAnnounce())
				.dept_no(announceReadAuthority.getDeptNo())
				.job_no(announceReadAuthority.getJobNo())
				.build();
	}
}
