package com.codenal.announce.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="announce_file")
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor(access=AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
public class AnnounceFile {

//	FK 연결하기!
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="announce_no")
	private Long announceNo;
	

	@Column(name="file_ori_name")
	private String fileOriName;
	
	@Column(name="announce_title")
	private String announceTitle;

	@Column(name="file_new_name")
	private String fileNewName;
	
	@Column(name="file_path")
	private String filePath;
	
}
