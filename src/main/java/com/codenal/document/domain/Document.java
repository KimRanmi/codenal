package com.codenal.document.domain;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "document")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Setter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long docNo;

    @Column(name = "doc_name", nullable = false, length = 20)
    private String docName;

    @Column(name = "doc_new_name", nullable = false, length = 255)
    private String docNewName;

    @Column(name = "doc_status", nullable = false, length = 1)
    private String docStatus;

    @Temporal(TemporalType.DATE)
    @Column(name = "doc_created_date", nullable = false)
    private Date docCreatedDate;

    @Column(name = "doc_path", nullable = false, length = 100)
    private String docPath;

    @Column(name = "doc_size", nullable = false, precision = 10, scale = 2)
    private BigDecimal docSize;

    @Column(name = "doc_emp_id", nullable = false)
    private Long docEmpId;

    @Column(name = "doc_share_emp_id")
    private Long docShareEmpId;
}
