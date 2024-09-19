package com.codenal.document.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "document_shared_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(DocumentSharedUsersId.class)
public class DocumentSharedUsers {

	@Id
    @Column(name = "doc_shared_no")
    private Long docSharedNo;

    @Id
    @Column(name = "doc_shared_with_emp_id") // 복합키의 일부로 설정
    private Long docSharedWithEmpId;

    @ManyToOne
    @JoinColumn(name = "doc_shared_no", referencedColumnName = "doc_no")
    private Documents documents;
}
