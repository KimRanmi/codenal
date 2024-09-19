package com.codenal.document.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codenal.document.domain.DocumentDto;
import com.codenal.document.domain.DocumentSharedUsers;
import com.codenal.document.domain.Documents;
import com.codenal.document.repository.DocumentRepository;
import com.codenal.document.repository.DocumentSharedUsersRepository;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private DocumentSharedUsersRepository documentSharedUsersRepository;

    // 모든 문서를 조회하는 메서드
    public List<DocumentDto> getDocumentList() {
        List<Documents> documents = documentRepository.findAll();
        return documents.stream()
                .map(DocumentDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 문서를 저장하는 메서드
    public void saveDocument(DocumentDto documentDto) {
        Documents document = Documents.builder()
                .docName(documentDto.getDocName())
                .docNewName(documentDto.getDocNewName())
                .docStatus(documentDto.getDocStatus())
                .docCreatedDate(documentDto.getDocCreatedDate())
                .docPath(documentDto.getDocPath())
                .docSize(documentDto.getDocSize())
                .docEmpId(documentDto.getDocEmpId())
                .build();

        documentRepository.save(document);
    }

    // 키워드(파일명)로만 문서를 검색하는 메서드
    public List<DocumentDto> searchDocumentsByKeyword(String keyword) {
        List<Documents> documents = documentRepository.findByDocNameContainingIgnoreCase(keyword);
        return documents.stream()
                .map(DocumentDto::fromEntity)
                .collect(Collectors.toList());
    }

 // 문서 상태를 2로 변경하여 휴지통으로 이동시키는 메서드
    public void moveDocumentsToTrash(List<Long> docIds) {
        List<Documents> documents = documentRepository.findAllById(docIds);
        documents.forEach(document -> document.setDocStatus(2)); // 모든 문서를 휴지통(2)로 이동
        documentRepository.saveAll(documents);
    }

    // 상태별 문서 목록 조회
    public List<DocumentDto> getDocumentsByStatus(int status) {
        List<Documents> documents = documentRepository.findByDocStatus(status);
        return documents.stream()
                .map(DocumentDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 문서를 복원하는 메서드 (휴지통에서 개인 문서로 복원, 상태를 0으로 변경)
    public void restoreDocuments(List<Long> docIds) {
        List<Documents> documents = documentRepository.findAllById(docIds);
        documents.forEach(document -> document.setDocStatus(0)); // 상태를 개인 문서(0)로 변경
        documentRepository.saveAll(documents);
    }


    // 문서를 영구적으로 삭제하는 메서드 (휴지통에서 완전히 삭제)
    public void deleteDocumentsPermanently(List<Long> docIds) {
        documentRepository.deleteAllById(docIds); // 문서 영구 삭제
    }
    
    public List<DocumentDto> getDocumentsByStatusNot(int status) {
        List<Documents> documents = documentRepository.findAllByDocStatusNot(status);
        return documents.stream()
                        .map(DocumentDto::fromEntity)
                        .collect(Collectors.toList());
    }
    
    // 공유한 문서 조회
    public List<DocumentDto> getDocumentsSharedByMe(Long empId) {
        List<Documents> documents = documentRepository.findByDocEmpIdAndDocStatus(empId, 1);  // 상태 1: 공유 문서
        return documents.stream()
                        .map(DocumentDto::fromEntity)
                        .collect(Collectors.toList());
    }

 // 공유받은 문서 조회
    public List<DocumentDto> getDocumentsSharedWithMe(Long empId) {
        // 공유받은 문서를 조회하려면 DocumentSharedUsers 테이블을 통해 문서 ID를 가져와야 함
        List<DocumentSharedUsers> sharedUsers = documentSharedUsersRepository.findByDocSharedWithEmpId(empId);
        List<Documents> documents = sharedUsers.stream()
                .map(DocumentSharedUsers::getDocuments)
                .collect(Collectors.toList());

        return documents.stream()
                .map(DocumentDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    
 // 파일 ID로 문서를 조회하는 메서드
    public Documents getDocumentById(Long docId) {
        return documentRepository.findById(docId)
            .orElseThrow(() -> new RuntimeException("해당 문서를 찾을 수 없습니다: " + docId));
    }
}
