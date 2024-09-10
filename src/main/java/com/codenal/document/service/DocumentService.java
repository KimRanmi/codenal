package com.codenal.document.service;

import com.codenal.document.domain.Document;
import com.codenal.document.domain.DocumentDto;
import com.codenal.document.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;	
import java.util.UUID;

@Service
public class DocumentService {

    private static final String UPLOAD_DIR = "/uploads/";

    @Autowired
    private DocumentRepository documentRepository;

    public DocumentDto saveFile(MultipartFile file, Long empId) throws IOException {
        // 파일이 비어있는지 확인
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 없습니다. 파일을 선택해 주세요.");
        }

        // 원본 파일명
        String originalFileName = file.getOriginalFilename();
        // 새로운 고유 파일명 생성
        String newFileName = UUID.randomUUID().toString() + "_" + originalFileName;
        // 파일 저장 경로
        Path filePath = Paths.get(UPLOAD_DIR + newFileName);

        // 파일을 지정된 경로에 저장
        Files.createDirectories(filePath.getParent()); // 경로가 없으면 생성
        file.transferTo(filePath.toFile());

        // 파일 정보를 Document 엔티티에 저장
        Document document = Document.builder()
                .docName(originalFileName)
                .docNewName(newFileName)
                .docStatus("0") // 기본 상태
                .docCreatedDate(new Date())
                .docPath(filePath.toString())
                .docSize(BigDecimal.valueOf(file.getSize()))
                .docEmpId(empId)
                .build();

        // 엔티티를 데이터베이스에 저장
        document = documentRepository.save(document);

        // 저장된 엔티티를 DTO로 변환하여 반환
        return DocumentDto.fromEntity(document);
    }
}
