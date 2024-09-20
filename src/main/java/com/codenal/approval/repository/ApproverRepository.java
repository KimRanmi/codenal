package com.codenal.approval.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.approval.domain.Approver;

public interface ApproverRepository extends JpaRepository<Approver, Long>{

}
