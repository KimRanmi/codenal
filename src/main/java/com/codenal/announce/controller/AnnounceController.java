package com.codenal.announce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnnounceController {

	
	@GetMapping("/announce")
	public String apps_tasks_list_view() {
		return "apps/announce";
	}
	
	
}
