package com.codenal.announce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnnounceController {

	
	@GetMapping("/announce")
	public String apps_tasks_list_view() {
		return "apps/announce";
	}
	
	@GetMapping("/announce_detail")
	public String apps_projects_overview() {
		return "apps/announce_detail";
	}

	
//	@GetMapping("/apps-projects-list")
//	public String apps_projects_list() {
//		return "apps/projects-list";
//	}
//	
//	
//	@GetMapping("/apps-projects-create")
//	public String apps_projects_create() {
//		return "apps/projects-create";
//	}
	
	
}
