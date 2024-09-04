package com.codenal.announce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codenal.announce.domain.Announce;
import com.codenal.announce.domain.AnnounceDto;
import com.codenal.announce.repository.AnnounceRepository;

@Service
public class AnnounceService {

	
	private final AnnounceRepository announceRepository;
	
	@Autowired
	public AnnounceService(AnnounceRepository announceRepository) {
		this.announceRepository = announceRepository;
	}
	
	public List<AnnounceDto> selectAnnounceList(AnnounceDto searchDto){
		List<Announce> announceList = announceRepository.findAll();
		List<AnnounceDto> announceDtoList = new ArrayList<AnnounceDto>();
		for(Announce announce : announceList) {
			AnnounceDto announceDto = new AnnounceDto().toDto(announce);
			announceDtoList.add(announceDto);
		}
		
		return announceDtoList;
	}
}
