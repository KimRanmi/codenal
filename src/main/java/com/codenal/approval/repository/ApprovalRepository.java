package com.codenal.approval.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.approval.domain.Approval;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
		
		Approval findByapprovalNo(Long approvalNo);
}
