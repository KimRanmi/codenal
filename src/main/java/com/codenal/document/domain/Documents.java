package com.codenal.document.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "documents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Setter
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_no")  // 문서 코드
    private Long docNo;

    @Column(name = "doc_name")  // 문서명
    private String docName;

    @Column(name = "doc_new_name")  // 문서 새로운 명
    private String docNewName;

    @Column(name = "doc_status")  // 문서 상태
    private int docStatus;

    @Temporal(TemporalType.DATE)
    @Column(name = "doc_created_date")  // 문서 등록일
    private Date docCreatedDate;

    @Column(name = "doc_path")  // 문서 경로
    private String docPath;

    @Column(name = "doc_size")  // 문서 사이즈
    private BigDecimal docSize;

    @Column(name = "doc_emp_id")  // 문서 등록한 직원 ID
    private Long docEmpId;
}
