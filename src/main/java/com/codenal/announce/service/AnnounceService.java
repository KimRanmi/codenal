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
import com.codenal.announce.repository.AnnounceReadAuthorityRepository;
import com.codenal.announce.repository.AnnounceRepository;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.repository.EmployeeRepository;

import jakarta.transaction.Transactional;


@Service
public class AnnounceService {

	
	private final AnnounceRepository announceRepository;
	private final AnnounceFileRepository announceFileRepository;
	private final AnnounceReadAuthorityRepository announceReadAuthorityRepository;
	private final EmployeeRepository employeeRepository;
	
	@Autowired
	public AnnounceService(AnnounceRepository announceRepository, AnnounceFileRepository announceFileRepository
			,AnnounceReadAuthorityRepository announceReadAuthorityRepository, EmployeeRepository employeeRepository) {
		this.announceRepository = announceRepository;
		this.announceFileRepository = announceFileRepository;
		this.announceReadAuthorityRepository = announceReadAuthorityRepository;
		this.employeeRepository = employeeRepository;
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
	
	
    public AnnounceDto selectAnnounceDetail(Long announceNo, String username) {
        Announce announce = announceRepository.findByAnnounceNo(announceNo);
        AnnounceDto dto = new AnnounceDto().toDto(announce);
//        if(!announce.getEmployee().getEmpId().equals(username)) {
//        	announceRepository.updateViewCount(announceNo);
//        	announce = announceRepository.findByAnnounceNo(announceNo);
//            dto = new AnnounceDto().toDto(announce);
//        } else {
//        	dto = new AnnounceDto().toDto(announce);
//        }
        return dto;
    }
    
    
    public AnnounceDto selectAnnounceUpdateView(Long announceNo) {
        Announce announce = announceRepository.findByAnnounceNo(announceNo);
        AnnounceDto dto = new AnnounceDto().toDto(announce);
        return dto;
    }
    
    
    @Transactional
    public Announce createAnnounce(AnnounceDto dto, AnnounceFileDto fileDto) {
        // Announce 객체 생성 및 저장
        int announceWriter = dto.getAnnounce_writer();
        Employee emp = employeeRepository.findByEmpId(announceWriter);
        Announce announce = Announce.builder()
                .announceTitle(dto.getAnnounce_title())
                .announceContent(dto.getAnnounce_content())
                .readAuthorityStatus(dto.getRead_authority_status())
                .employee(emp)
                .build();
        
        // Announce 저장
        Announce savedAnnounce = announceRepository.save(announce);
        
        // AnnounceFile 객체 생성 및 저장
        AnnounceFile aFile = AnnounceFile.builder()
                .announce(savedAnnounce) // Announce와 연관 설정
                .fileOriName(fileDto.getFile_ori_name())
                .fileNewName(fileDto.getFile_new_name())
                .filePath(fileDto.getFile_path())
                .build();
        
        // AnnounceFile 저장
        announceFileRepository.save(aFile);
        
        // 저장된 Announce 객체 반환
        return savedAnnounce;
    }

	public int deleteAnnounce(Long no) {
		int result = 0;
		try {
			announceRepository.deleteById(no);
			result = 1;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
    
}
