package com.codenal.document.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codenal.document.domain.Documents;

@Repository
public interface DocumentRepository extends JpaRepository<Documents, Long> {

    // findByDocName으로 수정
    List<Documents> findByDocNameContainingIgnoreCaseAndDocStatus(String keyword, int status);

    // findByDocName으로 수정
    List<Documents> findByDocNameContainingIgnoreCase(String keyword);

    // 상태로 문서 찾기
    List<Documents> findByDocStatus(int status);
    
    
   
        List<Documents> findAllByDocStatusNot(int docStatus);  // 휴지통 상태가 아닌 문서 조회
        
        // 공유한 문서 조회 (등록자 기준)
        List<Documents> findByDocEmpIdAndDocStatus(Long docEmpId, int docStatus);

      
       


    
}
