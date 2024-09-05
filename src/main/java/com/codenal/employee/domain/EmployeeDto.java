package com.codenal.employee.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class EmployeeDto {

    private Long empId;
    private String empPw;
    private String empName;
    private String empSsn;
    private String empPhone;
    private String empAddress;
    private String empAddressDetail;
    private LocalDate empHire;
    private LocalDate empEnd;
    private String empStatus;
    private String empProfilePicture;
    private String empSignImage;
    private Integer deptNo;
    private Integer jobNo;
    private String empAuth;

    private List<GrantedAuthority> authorities;

    // 엔터티를 DTO로 변환하는 메서드
    public static EmployeeDto fromEntity(Employee employee) {
        return EmployeeDto.builder()
                .empId(employee.getEmpId())
                .empPw(employee.getEmpPw())
                .empName(employee.getEmpName())
                .empSsn(employee.getEmpSsn())
                .empPhone(employee.getEmpPhone())
                .empAddress(employee.getEmpAddress())
                .empAddressDetail(employee.getEmpAddressDetail())
                .empHire(employee.getEmpHire()) 
                .empEnd(employee.getEmpEnd())
                .empStatus(employee.getEmpStatus())
                .empProfilePicture(employee.getEmpProfilePicture())
                .empSignImage(employee.getEmpSignImage())
                .deptNo(employee.getDeptNo())
                .jobNo(employee.getJobNo())
                .empAuth(employee.getEmpAuth())
                .build();
    }

 // DTO를 엔터티로 변환하는 메서드
    public Employee toEntity() {
        return Employee.builder()
                .empId(empId)
                .empPw(empPw)
                .empName(empName)  
                .empSsn(empSsn)
                .empPhone(empPhone)
                .empAddress(empAddress)
                .empAddressDetail(empAddressDetail)
                .empHire(empHire)
                .empEnd(empEnd)
                .empStatus(empStatus)
                .empProfilePicture(empProfilePicture)
                .empSignImage(empSignImage)
                .deptNo(deptNo)
                .jobNo(jobNo)
                .empAuth(empAuth)
                .build();
    }

}
