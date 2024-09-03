package com.codenal.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

	
	@GetMapping("/chat")
	public String chat(Model model) {
		model.addAttribute("page","appchat");
		return "apps/chat";
	}
}
