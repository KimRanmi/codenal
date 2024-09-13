package com.codenal.document.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codenal.document.domain.DocumentDto;
import com.codenal.document.service.DocumentService;

@RestController
@RequestMapping("/api/documents")
public class DocumentApiController {

    @Autowired
    private DocumentService documentService;

    // 파일 업로드 엔드포인트
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 비어 있습니다.");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String empIdString = authentication.getName(); // empId를 String으로 받아옴 (username 대신 empId로 가정)
        Long empId = Long.parseLong(empIdString);
        // 파일 저장 경로 설정 (예: 파일을 로컬에 저장)
        Path uploadDir = Paths.get("uploads/");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        String originalFilename = file.getOriginalFilename();
        Path filePath = uploadDir.resolve(originalFilename);
        Files.copy(file.getInputStream(), filePath);

        // 파일 정보를 DocumentDto로 변환하여 서비스에 넘김
        DocumentDto documentDto = DocumentDto.builder()
                .docName(originalFilename)
                .docNewName(originalFilename) // 새로운 파일명 처리 가능
                .docStatus('0')  // 상태를 기본값 '0'으로 설정
                .docCreatedDate(new Date())
                .docPath(filePath.toString())
                .docSize(BigDecimal.valueOf(file.getSize()))
                .docEmpId(empId) // 임시 사용자 ID 설정 (실제 사용자의 ID로 변경 필요)
                .build();

        documentService.saveDocument(documentDto);

        return ResponseEntity.ok("파일이 성공적으로 업로드되었습니다.");
    }
    
 // 문서 목록 반환 엔드포인트 (JSON)
    @GetMapping("/list")
    public ResponseEntity<List<DocumentDto>> getDocumentList() {
        List<DocumentDto> documents = documentService.getDocumentList();
        return ResponseEntity.ok(documents);  // JSON 형식으로 문서 목록을 반환
    }
}
