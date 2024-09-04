package com.codenal.announce.domain;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="announce")
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor(access=AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
public class Announce {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="announce_no")
	private Long announceNo;
	
//	@OneToMany(mappedBy = "announce")
//	private List<AnnounceFile> announces1;
//	
//	@OneToMany(mappedBy = "announce")
//	private List<AnnounceReadAuthority> announces2;

//	FK 연결하기! many, 다대일
	@Column(name="announce_writer")
	private int announceWriter;
	
	@Column(name="announce_title")
	private String announceTitle;

	@Column(name="announce_content")
	private String announceContent;
	
	@Column(name="reg_date")
	private LocalDateTime regDate;
	
	@Column(name="mod_date")
	private LocalDateTime modDate;
	
	@Column(name="view_count")
	private int viewCount;
	
	@Column(name="read_authority_status")
	private char readAuthorityStatus;
	
}
