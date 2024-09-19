package com.codenal.document.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codenal.document.domain.DocumentSharedUsers;
import com.codenal.document.domain.DocumentSharedUsersId; // 복합 키 클래스

@Repository
public interface DocumentSharedUsersRepository extends JpaRepository<DocumentSharedUsers, DocumentSharedUsersId> {
    // custom 메서드 추가
    List<DocumentSharedUsers> findByDocSharedWithEmpId(Long docSharedWithEmpId); // 필드명 수정
}
