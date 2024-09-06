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
public class AnnounceFileDto {

	private Long file_no;
	
	private Long announce_no;
	private String announce_writer;
	private String announce_title;
	
	private String file_ori_name;
	private String file_new_name;
	private String file_path;
	
	
	public AnnounceFile toEntity() {
		return AnnounceFile.builder()
				.fileNo(file_no)
				.fileOriName(file_ori_name)
				.fileNewName(file_new_name)
				.filePath(file_path)
				.build();
		 
	}
	
	public AnnounceFileDto toDto(AnnounceFile announceFile) {
		return AnnounceFileDto.builder()
				.file_no(getFile_no())
				.announce_no(getAnnounce_no())
				.file_ori_name(getFile_ori_name())
				.file_new_name(getFile_new_name())
				.file_path(getFile_path())
				.build();
	}
}
