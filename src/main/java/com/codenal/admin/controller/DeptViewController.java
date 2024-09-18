package com.codenal.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.addressBook.domain.TreeMenuDto;
import com.codenal.addressBook.service.AddressBookService;

@Controller
@RequestMapping("/admin/dept")
public class DeptViewController {

	private final AddressBookService addressBookService;

	@Autowired
	public DeptViewController(AddressBookService addressBookService) {
		this.addressBookService = addressBookService;
	}

	// 부서 관리
	@GetMapping("")
	public String DeptPage() {
		return "admin/dept"; 
	}

	// TreeMenu
	@GetMapping("/tree-menu")
	@ResponseBody
	public List<TreeMenuDto> getTreeMenu() {
		List<TreeMenuDto> treeMenu = addressBookService.getTreeMenu();
		System.out.println("Tree Menu Data: " + treeMenu);  // 로그 추가
		return addressBookService.getTreeMenu();
	}
} 
