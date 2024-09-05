package com.codenal.approval.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder

public class ApprovalDto {
	
	private Long approval_no;
	private Long emp_id;
	private int cate_code;
	private Date approval_reg_date;
	private String approval_title;
	private String approval_content;
	private int approval_status;
	private Long annual_usage_no;
	
	public Approval toEntity() {
		return Approval.builder()
				.approvalNo(approval_no)
				.empId(emp_id)
				.cateCode(cate_code)
				.approvalTitle(approval_title)
				.approvalContent(approval_content)
				.approvalRegDate(approval_reg_date)
				.approvalStatus(approval_status)
				.annualUsageNo(annual_usage_no)
				.build();
	}
	
	public ApprovalDto toDto(Approval approval) {
		return ApprovalDto.builder()
				.approval_no(approval.getApprovalNo())
				.emp_id(approval.getEmpId())
				.cate_code(approval.getCateCode())
				.approval_title(approval.getApprovalTitle())
				.approval_content(approval.getApprovalContent())
				.approval_reg_date(approval.getApprovalRegDate())
				.approval_status(approval.getApprovalStatus())
				.annual_usage_no(approval.getAnnualUsageNo())
				.build();
	}
}
