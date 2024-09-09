package com.codenal.admin.domain;

import java.time.LocalDate;
import java.util.List;

import com.codenal.announce.domain.AnnounceReadAuthority;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="departments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Departments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_no")
    private Integer deptNo; 

    @Column(name = "dept_name", nullable = false)
    private String deptName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dept_create_date")
    private LocalDate deptCreateDate;
    
    @OneToMany(mappedBy = "department")
    private List<AnnounceReadAuthority> readAuthorities;
}