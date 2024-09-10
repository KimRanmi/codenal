package com.codenal.announce.service;

import java.io.File;
import java.net.URLDecoder;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.codenal.announce.domain.Announce;
import com.codenal.announce.domain.AnnounceDto;
import com.codenal.announce.domain.AnnounceFile;
import com.codenal.announce.repository.AnnounceFileRepository;
import com.codenal.announce.repository.AnnounceRepository;

@Service
public class FileService {
	
	private final AnnounceRepository announceRepository;
	private final AnnounceFileRepository announceFileRepository;
	
	@Autowired
	public FileService(AnnounceRepository announceRepository, AnnounceFileRepository announceFileRepository) {
		this.announceRepository = announceRepository;
		this.announceFileRepository = announceFileRepository;
	}
	
	private String fileDir = "C:\\codenal\\announce\\upload\\";

	public String upload(MultipartFile file) {

		String newFileName = null;

		try {
			// 1. 파일 원래 이름
			String oriFileName = file.getOriginalFilename();
			// 2. 파일 자르기
			String fileExt = oriFileName.substring(oriFileName.lastIndexOf("."),oriFileName.length());
			// 3. 파일 명칭 바꾸기
			UUID uuid = UUID.randomUUID();
			// 4. 8자리마다 포함되는 하이픈 제거
			String uniqueName = uuid.toString().replace("-", "");
			// 5. 새로운 파일명
			newFileName = uniqueName+fileExt;
			// 6. 파일 저장 경로 설정 -> 상단에 위치함
			// 7. 파일 껍데기 생성
			File saveFile = new File(fileDir+newFileName);
			// 8. 경로 존재 여부 확인
			if(!saveFile.exists()) {
				saveFile.mkdirs();
			}
			// 9. 껍데기에 파일 정보 복제
			file.transferTo(saveFile);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return newFileName;
	}

	public int deletefile(Long announceNo) {
		int result = -1;
		
		Announce announce = announceRepository.findByAnnounceNo(announceNo);
		try {
			String newFileName = announce.getFiles().get(0).getFileNewName();	// UUID
			String oriFileName = announce.getFiles().get(0).getFileOriName();	// 사용자가 아는 파일명
			String resultDir = fileDir + URLDecoder.decode(newFileName,"UTF-8");
			if(resultDir != null && resultDir.isEmpty() == false) {
				File file = new File(resultDir);
				if(file.exists()) {
					file.delete();
					result = 1;
				}	
			}	
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
}
