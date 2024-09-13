package com.codenal.document.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codenal.document.domain.DocumentDto;
import com.codenal.document.domain.Documents;
import com.codenal.document.repository.DocumentRepository;
import com.codenal.employee.domain.Employee;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    // 모든 문서를 조회하는 메서드
    public List<DocumentDto> getDocumentList() {
        List<Documents> documents = documentRepository.findAll();
        return documents.stream()
                .map(document -> DocumentDto.builder()
                        .docName(document.getDocName())
                        .docNewName(document.getDocNewName())
                        .docStatus(document.getDocStatus())
                        .docCreatedDate(document.getDocCreatedDate())
                        .docPath(document.getDocPath())
                        .docSize(document.getDocSize())
                        .docEmpId(document.getDocEmpId())
                        .build())
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
    
    
  
}
