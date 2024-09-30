package com.codenal.approval.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codenal.approval.domain.ApprovalCategory;
import com.codenal.approval.domain.ApprovalForm;
import com.codenal.approval.domain.ApprovalFormDto;

public interface ApprovalFormRepository extends JpaRepository<ApprovalForm, Integer>{
   
   List<ApprovalForm> findByApprovalBaseFormType_BaseFormCode(int no);
   
   ApprovalForm findByFormCode(int cateCode);
   
   
}