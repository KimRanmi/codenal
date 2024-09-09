package com.codenal.approval.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.approval.domain.ApprovalCategory;

public interface ApprovalCategoryRepository extends JpaRepository<ApprovalCategory, Integer> {

	ApprovalCategory findByCateCode(int cateCode);
}
