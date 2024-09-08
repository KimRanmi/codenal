package com.codenal.announce.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="announce_read_authority")
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor(access=AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
public class AnnounceReadAuthority {
	
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name="read_no")
//	private Long readNo;
	
	@Id
    private Long id;
	
	@MapsId
	@ManyToOne
	@JoinColumn(name="announce_no")
	private Announce announce;
	
	
//	FK 연결하기!
	@Column(name="dept_no")
	private int deptNo;
//	FK 연결하기!
	@Column(name="job_no")
	private int jobNo;
}
