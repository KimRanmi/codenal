package com.codenal.approval.domain;

import java.util.Date;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="approval")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Approval {

    @Id
    @Column(name="approval_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long approvalNo;
    
    @Column(name="emp_id")
    private Long empId;
    
    @Column(name="cate_code")
    private int cateCode;
    
    @Column(name="approval_reg_date")
    @UpdateTimestamp
    private Date approvalRegDate;
    
    @Column(name="approval_title")
    private String approvalTitle;
    
    @Column(name="approval_content")
    private String approvalContent;
    
    @Column(name="approval_status")
    private int approvalStatus;
    
    @Column(name="annual_usage_no")
    private Long annualUsageNo;
    
}

