package com.codenal.announce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.codenal.announce.domain.AnnounceDto;
import com.codenal.announce.service.AnnounceService;

@Controller
public class AnnounceController {

	
	private final AnnounceService announceService;
	
	@Autowired
	public AnnounceController(AnnounceService announceService) {
		this.announceService = announceService;
	}
	
	@GetMapping("/announce")
	public String announceList_view(Model model
			, @PageableDefault(page=0, size=10, sort="regDate", direction  = Sort.Direction.DESC) Pageable pageable
			, AnnounceDto searchDto) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
        
		Page<AnnounceDto> announceList = announceService.selectAnnounceList(searchDto, pageable);
		model.addAttribute("announceList",announceList);
		model.addAttribute("searchDto",searchDto);
		return "apps/announce";
	}
	

	
	@GetMapping("/announce/detail/{no}")
	public String announceListDetail_view(@PathVariable("no") Long no, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
        announceService.updateViewCount(no, username);
		AnnounceDto announceDetail = announceService.selectAnnounceDetail(no);
        model.addAttribute("announceList", announceDetail);
        return "apps/announce_detail";
	}

	
	@GetMapping("/announce/update/{no}")
	public String announceListUpdate_view(@PathVariable("no") Long no, Model model) {
		AnnounceDto announceList = announceService.selectAnnounceUpdateView(no);
		model.addAttribute("announceList",announceList);
		return "apps/announce_update";
	}
	
	
	@GetMapping("/announce/create")
	public String announceList_create_view(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
        System.out.println(username);
		return "apps/announce_create";
	}
	
	

	
	
}
