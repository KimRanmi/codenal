package com.codenal.approval.domain;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import com.codenal.annual.domain.AnnualLeaveUsage;
import com.codenal.employee.domain.Employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    
    @Column(name="approval_reg_date")
    @UpdateTimestamp
    private LocalDate approvalRegDate;
    
    @Column(name="approval_title")
    private String approvalTitle;
    
    @Column(name="approval_content")
    private String approvalContent;
    
    @Column(name="approval_status")
    private int approvalStatus;
    
    // 외래키
    @ManyToOne
    @JoinColumn(name="emp_id")
    private Employee employee;
    
    @ManyToOne
    @JoinColumn(name="cate_code")
    private ApprovalCategory approvalCategory;
    
    @ManyToOne
    @JoinColumn(name="annual_usage_no")
    private AnnualLeaveUsage annualLeaveUsage;
    
    @OneToMany(mappedBy="approval")
    private List<ApprovalFile> approvalFiles;
    
    @OneToMany(mappedBy="approval")
    private List<Approver> approvers;
    
    @OneToMany(mappedBy="approval")
    private List<Referrer> referrers;
    
}

