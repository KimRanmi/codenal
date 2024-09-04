package com.codenal.announce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.codenal.announce.domain.Announce;
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
	public String apps_tasks_list_view(Model model
			, @PageableDefault(page=0, size=10, sort="regDate", direction  = Sort.Direction.DESC) Pageable pageable
			, AnnounceDto searchDto) {
		Page<AnnounceDto> announceList = announceService.selectAnnounceList(searchDto, pageable);
		System.out.println(announceList);
		model.addAttribute("announceList",announceList);
		model.addAttribute("searchDto",searchDto);
		return "apps/announce";
	}
	
	@GetMapping("/announce/detail")
	public String apps_projects_overview() {
		return "apps/announce_detail";
	}

	
	
	@GetMapping("/announce/create")
	public String apps_projects_create() {
		return "apps/announce_create";
	}
	
	

	
	
}
