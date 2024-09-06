package com.codenal.announce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codenal.announce.domain.Announce;
import com.codenal.announce.domain.AnnounceDto;
import com.codenal.announce.domain.AnnounceFile;
import com.codenal.announce.domain.AnnounceFileDto;
import com.codenal.announce.repository.AnnounceFileRepository;
import com.codenal.announce.repository.AnnounceRepository;

@Service
public class AnnounceService {

	
	private final AnnounceRepository announceRepository;
	private final AnnounceFileRepository announceFileRepository;
	
	@Autowired
	public AnnounceService(AnnounceRepository announceRepository, AnnounceFileRepository announceFileRepository) {
		this.announceRepository = announceRepository;
		this.announceFileRepository = announceFileRepository;
	}
	
	public Page<AnnounceDto> selectAnnounceList(AnnounceDto searchDto, Pageable pageable){
		Page<Announce> announceList = null;
		String searchText = searchDto.getSearch_text();
		if(searchText != null && "".equals(searchText) == false) {
			int searchType = searchDto.getSearch_type();
			switch(searchType) {
			case 1:
				announceList = announceRepository.findByAnnounceTitleContaining(searchText, pageable);
				break;
//			case 2:
//				announceList = announceRepository.findByAnnounceWriterContaining(searchText, pageable);
//				break;
//			case 3:
//				announceList = announceRepository.findByAnnounceTitleOrAnnounceWriterContaining(searchText, pageable);
			}
			
		} else {
			announceList = announceRepository.findAll(pageable);
		}
		
		List<AnnounceDto> announceDtoList = new ArrayList<AnnounceDto>();
		for(Announce a : announceList) {
			AnnounceDto dto = new AnnounceDto().toDto(a);
			announceDtoList.add(dto);
		}
		
		return new PageImpl<>(announceDtoList, pageable, announceList.getTotalElements());
	}
	
	
	public List<AnnounceDto> selectAnnounceDetail(Long no){
		
		List<Announce> announceList = announceRepository.findByAnnounceListOne(no);  // 작성자 이름하고 권한, 파일 가져와야함
		List<AnnounceDto> announceDtoList = new ArrayList<AnnounceDto>();
		AnnounceFile aFile = announceFileRepository.findByAnnounceNo(no);
		System.out.println("aFile 정보: "+aFile);
//		List<Object[]> aFile = announceFileRepository.findFilesWithAnnounceTitleByAnnounceNo(no);
//		System.out.println("aFile 정보: "+aFile);
		
		for(Announce a : announceList) {
			AnnounceDto dto = new AnnounceDto().toDto(a);
			announceDtoList.add(dto);
		}
		System.out.println("dtoList 최종 정보: "+announceDtoList);
		

		return announceDtoList;
	}

//	public List<AnnounceFile> findByannounceNo(Long announceNo) {
//        return announceFileRepository.findByannounceNo(announceNo);
//
//	}
}
