package com.codenal.employee.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="employee")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private int empId;

    @Column(name = "emp_pw", nullable = false)
    private String empPw;

    @Column(name = "emp_name", nullable = false)
    private String empName;

    @Column(name = "emp_ssn", nullable = false)
    private String empSsn;

    @Column(name = "emp_phone", nullable = false)
    private String empPhone;

    @Column(name = "emp_address")
    private String empAddress;

    @Column(name = "emp_address_detail")
    private String empAddressDetail;

    @Column(name = "emp_hire", nullable = false)
    private Date empHire;

    @Column(name = "emp_end")
    private Date empEnd;

    @Column(name = "emp_status", nullable = false)
    private String empStatus;

    @Column(name = "emp_profile_picture")
    private String empProfilePicture;

    @Column(name = "emp_sign_image")
    private String empSignImage;

    @Column(name = "dept_no", nullable = false)
    private Integer deptNo;

    @Column(name = "job_no", nullable = false)
    private Integer jobNo;

    @Column(name = "emp_auth", nullable = false)
    private String empAuth;
}