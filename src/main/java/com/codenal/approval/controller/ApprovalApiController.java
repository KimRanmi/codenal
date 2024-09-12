package com.codenal.approval.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.codenal.approval.domain.Approval;
import com.codenal.approval.service.ApprovalFileService;
import com.codenal.approval.service.ApprovalService;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.service.EmployeeService;

@Controller
public class ApprovalApiController {

   private final ApprovalService approvalService;
   private final ApprovalFileService approvalFileService;
   private final EmployeeService employeeService;
   
   @Autowired
   public ApprovalApiController(ApprovalService approvalService,ApprovalFileService approvalFileService,EmployeeService employeeService) {
      this.approvalService = approvalService;
      this.approvalFileService = approvalFileService;
      this.employeeService = employeeService;
   }
   
   
   // 전자결재 등록 (요청서, 품의서)
   @ResponseBody
   @PostMapping("/approval")
   public Map<String,String> createApproval(
         @RequestPart("approval_content") String approvalContent,
            @RequestParam("approval_title") String approvalTitle,
            @RequestParam("emp_id") String empId,
            @RequestParam("approval_reg_date") String approvalRegDate,
            @RequestParam("form_code") String formCode,
            @RequestPart(value="file",required = false) MultipartFile file){
      Map<String,String> resultMap = new HashMap<String,String>();
      System.out.println("시작");
      resultMap.put("res_code", "404");
      resultMap.put("res_msg", "전자결재 등록 중 오류가 발생했습니다.");
      
      // 타입 형변환
      Long emp_id = Long.parseLong(empId);
      Integer form_code = Integer.parseInt(formCode);
      
      Map<String,Object> list = new HashMap<String,Object>();
      list.put("제목", approvalTitle);
      list.put("내용", approvalContent);
      list.put("이름", emp_id);
      list.put("폼코드", form_code);
      
      Approval createdApproval = approvalService.createApproval(list);
      
      if(createdApproval != null) {
    	  
    	  if(file != null) {
    		  if(approvalFileService.upload(file,createdApproval) != null) {
    			  resultMap.put("res_code","200"); 
    			  resultMap.put("res_msg", "전자결재 등록 성공"); 
    			  }  
    	  	}
    	  		resultMap.put("res_code","200"); 
    	  		resultMap.put("res_msg", "전자결재 등록 성공"); 
    	  	
    	  
    	  }
      return resultMap;
   }
   
   // 전자결재 등록 (휴가신청서)
   @ResponseBody
   @PostMapping("/approval_leave")
   public Map<String,String> createApprovalLeave(
		   	@RequestParam("approval_content") String approvalContent,
            @RequestParam("approval_title") String approvalTitle,
            @RequestParam("emp_id") String empId,
            @RequestParam("approval_reg_date") String approvalRegDate,
            @RequestParam("start_date") LocalDate startDate,
            @RequestParam(value="end_date", required=false) LocalDate endDate,
            @RequestParam("form_code") String formCode,
            @RequestParam(value="file",required = false) MultipartFile file){
      
	  Map<String,String> resultMap = new HashMap<String,String>();
      System.out.println("시작");
      resultMap.put("res_code", "404");
      resultMap.put("res_msg", "전자결재 등록 중 오류가 발생했습니다.");
      
      // 타입 형변환
      Long emp_id = Long.parseLong(empId);
      Integer form_code = Integer.parseInt(formCode);
      
      Map<String,Object> list = new HashMap<String,Object>();
      list.put("제목", approvalTitle);
      list.put("내용", approvalContent);
      list.put("이름", emp_id);
      list.put("폼코드", form_code);
      list.put("시작일자",startDate);
      list.put("종료일자",endDate);
      
      Approval createdApproval = approvalService.createApprovalLeave(list);
      
      if(createdApproval != null) {
    	  
    	  if(file != null) {
    		  if(approvalFileService.upload(file,createdApproval) != null) {
    			  resultMap.put("res_code","200"); 
    			  resultMap.put("res_msg", "전자결재 등록 성공"); 
    			  }  
    	  	}
    	  		resultMap.put("res_code","200"); 
    	  		resultMap.put("res_msg", "전자결재 등록 성공"); 
    	  }
      return resultMap;
   }
   
   // 파일 다운로드
   @GetMapping("/download/{approval_no}")
   public ResponseEntity<Object> approvalImgDownload(
		   @PathVariable("approval_no")Long approvalNo){
	   return approvalFileService.download(approvalNo);
   }
   
   // 상신함 대기 -> 회수로 수정
   @ResponseBody
   @PostMapping("/approval/revoke")
   public Map<String,String> revokeApproval(@RequestBody Map<String, String> request) {
	   Map<String,String> resultMap = new HashMap<String,String>();
	   resultMap.put("res_code", "404");
	   String no = request.get("approvalNo");
       Long num = Long.parseLong(no);
	   
	   System.out.println("번호 : "+num);
	   
	   if(approvalService.revoke(num) == 1) {
		   resultMap.put("res_code", "200");
	   }
	   
	   return resultMap;
   }
   
   // 결재서 수정 (품의서, 요청서)
   @ResponseBody
   @PutMapping("/approval/update/{approvalNo}")
   public Map<String,String> updateApproval(@RequestPart("approval_content") String approvalContent,
           @RequestParam("approval_title") String approvalTitle,
           @RequestParam("emp_id") String empId,
           @RequestParam("approval_reg_date") String approvalRegDate,
           @RequestParam("form_code") String formCode,
           @PathVariable("approvalNo")Long no,
		   @RequestPart(name="file", required=false)MultipartFile file){
	   Map<String,String> resultMap = new HashMap<String,String>();
	   resultMap.put("res_code", "404");
	   resultMap.put("res_msg", "전자결재 수정 중 오류가 발생했습니다.");
	   
	   
	// 타입 형변환
	   Employee e = (Employee)employeeService.selectEmpId(empId);
	   Integer form_code = Integer.parseInt(formCode);
	      
	   Map<String,Object> list = new HashMap<String,Object>();
	   list.put("제목", approvalTitle);
	   list.put("내용", approvalContent);
	   list.put("이름", e.getEmpId());
	   list.put("폼코드", form_code);
	      
	   Approval updateApproval = approvalService.updateApproval(list,no);
	      
	      if(updateApproval != null) {
	    	  
	    	  if(file != null) {
	    		  if(approvalFileService.upload(file,updateApproval) != null) {
	    			  resultMap.put("res_code","200"); 
	    			  resultMap.put("res_msg", "전자결재 등록 성공"); 
	    			  }  
	    	  	}
	    	  		resultMap.put("res_code","200"); 
	    	  		resultMap.put("res_msg", "전자결재 등록 성공"); 
	    	  }
	      return resultMap;
   }
   
}