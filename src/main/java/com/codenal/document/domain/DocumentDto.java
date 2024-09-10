package com.codenal.document.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class DocumentDto {

    private Long docNo;
    private String docName;
    private String docNewName;
    private String docStatus;
    private Date docCreatedDate;
    private String docPath;
    private BigDecimal docSize;
    private Long docEmpId;
    private Long docShareEmpId;

    // 엔터티를 DTO로 변환하는 메서드
    public static DocumentDto fromEntity(Document document) {
        return DocumentDto.builder()
                .docNo(document.getDocNo())
                .docName(document.getDocName())
                .docNewName(document.getDocNewName())
                .docStatus(document.getDocStatus())
                .docCreatedDate(document.getDocCreatedDate())
                .docPath(document.getDocPath())
                .docSize(document.getDocSize())
                .docEmpId(document.getDocEmpId())
                .docShareEmpId(document.getDocShareEmpId())
                .build();
    }

    // DTO를 엔터티로 변환하는 메서드
    public Document toEntity() {
        return Document.builder()
                .docNo(docNo)
                .docName(docName)
                .docNewName(docNewName)
                .docStatus(docStatus)
                .docCreatedDate(docCreatedDate)
                .docPath(docPath)
                .docSize(docSize)
                .docEmpId(docEmpId)
                .docShareEmpId(docShareEmpId)
                .build();
    }
}
