package com.codenal.approval.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.addressBook.domain.TreeMenuDto;
import com.codenal.addressBook.service.AddressBookService;
import com.codenal.annual.domain.AnnualLeaveManage;
import com.codenal.annual.domain.AnnualLeaveUsage;
import com.codenal.annual.service.AnnualLeaveManageService;
import com.codenal.approval.domain.ApprovalBaseFormType;
import com.codenal.approval.domain.ApprovalFormDto;
import com.codenal.approval.service.ApprovalService;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.service.EmployeeService;
import com.codenal.security.service.SecurityService;

@Controller
public class ApprovalViewController {

	private final ApprovalService approvalService;
	private final EmployeeService employeeService;
	private final AnnualLeaveManageService annualLeaveManageService;
	private final AddressBookService addressBookService;

	@Autowired
	public ApprovalViewController(ApprovalService approvalService, SecurityService securityService,
			EmployeeService employeeService, AnnualLeaveManageService annualLeaveManageService,
			AddressBookService addressBookService) {
		this.approvalService = approvalService;
		this.annualLeaveManageService = annualLeaveManageService;
		this.employeeService = employeeService;
		this.addressBookService = addressBookService;
	}

	// 생성 nav에서 숫자값을 받아와서 create로 전달(휴가신청서는 따로 관리)
	@GetMapping("/approval/create/{no}")
	public String createApproval(Model model, @PathVariable("no") int no) {
		LocalDate ldt = LocalDate.now();

		List<ApprovalFormDto> cateList = new ArrayList<ApprovalFormDto>();
		cateList = approvalService.selectApprovalCateList(no);
		model.addAttribute("ldt", ldt);
		model.addAttribute("no", no);
		model.addAttribute("cateList", cateList);
		return "apps/approval_create";
	}

	// 휴가신청서로 이동
	@GetMapping("/approval/leave_create/{no}")
	public String createLeaveApproval(Model model, @PathVariable("no") int no) {
		LocalDate ldt = LocalDate.now();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Long id = Long.parseLong(username);

		Employee emp = employeeService.getEmployeeById(id);
		AnnualLeaveManage alm = annualLeaveManageService.getAnnualLeaveManageById(emp.getEmpId());

		List<ApprovalFormDto> cateList = new ArrayList<ApprovalFormDto>();
		cateList = approvalService.selectApprovalCateList(no);

		model.addAttribute("ldt", ldt);
		model.addAttribute("no", no);
		model.addAttribute("cateList", cateList);
		model.addAttribute("remainDay", alm.getAnnualRemainDay());
		return "apps/approval__leave_create";
	}

	// 상신 리스트
	@GetMapping("/approval/list")
	public String listApproval(Model model, @RequestParam(value = "num", defaultValue = "0") int num,
			@PageableDefault(page = 0, size = 10, sort = "approvalNo", direction = Sort.Direction.DESC) Pageable pageable) {
		// 현재 인증된 사용자 정보 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Long id = Long.parseLong(username);

		Page<Map<String, Object>> resultList = approvalService.selectApprovalList(pageable, num, id);

		model.addAttribute("resultList", resultList);
		model.addAttribute("num", num);

		return "apps/approval_list";
	}

	// 상세 조회
	@GetMapping("/approval/{approval_no}")
	public String selectApprovalOne(Model model, @PathVariable("approval_no") Long approval_no) {
		String returnResult = null;

		System.out.println("상세조회 시작");

		Map<String, Object> resultList = approvalService.detailApproval(approval_no);

		// 타입에 따라 리턴값 다르게 처리 -> 1이면 근태신청서 2면 품의서 3이면 요청서
		ApprovalBaseFormType af = (ApprovalBaseFormType) resultList.get("baseForm");
		int typeInt = af.getBaseFormCode();

		if (typeInt == 1) {
			returnResult = "approval__leave_detail";
		} else {
			returnResult = "approval_detail";
		}

		// annualLeaveUsage가 null인지 확인하여 처리
		AnnualLeaveUsage annualLeaveUsage = (AnnualLeaveUsage) resultList.get("annualLeaveUsage");
		if (annualLeaveUsage == null) {
			model.addAttribute("annualLeaveUsage", ""); // null일 경우 기본 메시지
		} else {
			model.addAttribute("annualLeaveUsage", annualLeaveUsage); // null이 아닐 경우 그대로 사용
		}

		List<ApprovalFormDto> cateList = new ArrayList<ApprovalFormDto>();
		cateList = approvalService.selectApprovalCateList(typeInt);

		model.addAttribute("dto", resultList);
		model.addAttribute("type", typeInt);
		model.addAttribute("cateList", cateList);
		return "apps/" + returnResult;
	}

	// 회수함에서 상세조회
	@GetMapping("/approval/detail/{approval_no}")
	public String selectApproval(Model model, @PathVariable("approval_no") Long approval_no) {
		String returnResult = null;

		System.out.println("상세조회 시작");

		Map<String, Object> resultList = approvalService.detailApproval(approval_no);

		// 타입에 따라 리턴값 다르게 처리 -> 1이면 근태신청서 2면 품의서 3이면 요청서
		ApprovalBaseFormType af = (ApprovalBaseFormType) resultList.get("baseForm");
		int typeInt = af.getBaseFormCode();

		if (typeInt == 1) {
			returnResult = "approval__leave_update";
		} else {
			returnResult = "approval_update";
		}

		// annualLeaveUsage가 null인지 확인하여 처리
		AnnualLeaveUsage annualLeaveUsage = (AnnualLeaveUsage) resultList.get("annualLeaveUsage");
		if (annualLeaveUsage == null) {
			model.addAttribute("annualLeaveUsage", ""); // null일 경우 기본 메시지
		} else {
			model.addAttribute("annualLeaveUsage", annualLeaveUsage); // null이 아닐 경우 그대로 사용
		}

		List<ApprovalFormDto> cateList = new ArrayList<ApprovalFormDto>();
		cateList = approvalService.selectApprovalCateList(typeInt);

		model.addAttribute("dto", resultList);
		model.addAttribute("type", typeInt);
		model.addAttribute("cateList", cateList);
		return "apps/" + returnResult;
	}

	
	// 모달 데이터 보내기
	
	@GetMapping("/approval/modal")
	@ResponseBody
	public List<TreeMenuDto> getTreeMenu(Model model) {
	    return addressBookService.getTreeMenu();
	}
	
}