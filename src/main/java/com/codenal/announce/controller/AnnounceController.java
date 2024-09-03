package com.codenal.announce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnnounceController {

	
	@GetMapping("/announce")
	public String apps_tasks_list_view() {
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
	
	
//	@GetMapping("/apps-projects-list")
//	public String apps_projects_list() {
//		return "apps/projects-list";
//	}
//	

	
	
}
