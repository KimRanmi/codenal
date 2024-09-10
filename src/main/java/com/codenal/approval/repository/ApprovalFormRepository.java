package com.codenal.approval.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codenal.approval.domain.ApprovalCategory;
import com.codenal.approval.domain.ApprovalForm;
import com.codenal.approval.domain.ApprovalFormDto;

public interface ApprovalFormRepository extends JpaRepository<ApprovalForm, Integer>{
	
	@Query(value="select f from ApprovalForm f "
			+ "where f.approvalBaseFormType.baseFormCode  = ?1")
	List<ApprovalForm> findByApprovalBaseFormType_BaseFormCode(int no);
	
	@Query(value="select f from ApprovalForm f where f.formCode = ?1")
	ApprovalForm findByApprovalFormCode(int cateCode);
	
	
}
