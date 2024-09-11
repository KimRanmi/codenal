package com.codenal.approval.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codenal.approval.domain.ApprovalFile;

public interface ApprovalFileRepository extends JpaRepository <ApprovalFile, Long> {

	ApprovalFile findByApprovalApprovalNo(Long approvalNo);
}
