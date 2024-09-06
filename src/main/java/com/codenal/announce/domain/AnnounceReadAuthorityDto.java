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

	private Announce announce_no;
	private int dept_no;
	private int job_no;
	
	
	public AnnounceReadAuthority toEntity() {
		return AnnounceReadAuthority.builder()
				.announce(announce_no)
				.deptNo(dept_no)
				.jobNo(job_no)
				.build();
		 
	}
	
	public AnnounceReadAuthorityDto toDto(AnnounceReadAuthority announceReadAuthority) {
		return AnnounceReadAuthorityDto.builder()
				.announce_no(getAnnounce_no())
				.dept_no(getDept_no())
				.job_no(getJob_no())
				.build();
	}
}
