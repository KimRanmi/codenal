package com.codenal.employee.domain;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.codenal.admin.domain.Departments;
import com.codenal.admin.domain.Jobs;

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
    private int deptNo;
    private String deptName; // 추가
    private int jobNo;
    private String jobName;  // 추가
    private String empAuth;
    
    private int search_type = 1; // int는 null 인식X
    private String search_text;

    private List<GrantedAuthority> authorities;

//    // 생성자
//    public EmployeeDto(Long empId, String empName, String deptName, String jobName, String empPhone) {
//        this.empId = empId;
//        this.empName = empName;
//        this.deptName = deptName;
//        this.jobName = jobName;
//        this.empPhone = empPhone;
//    }


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
                .empStatus(empAuth != null ? empAuth : "Y") 
                .empProfilePicture(empProfilePicture)
                .empSignImage(empSignImage)
                .department(Departments.builder().deptNo(deptNo).build()) // 부서 객체 설정
                .job(Jobs.builder().jobNo(jobNo).build())  // 직급 객체 설정
                .empAuth(empAuth != null ? empAuth : "USER")
                .build();
    }
    
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
    			.empStatus(employee.getEmpStatus() != null ? employee.getEmpStatus() : "Y")
    			.empProfilePicture(employee.getEmpProfilePicture())
    			.empSignImage(employee.getEmpSignImage())
    			.deptNo(employee.getDepartment() != null ? employee.getDepartment().getDeptNo() : null)
    			.jobNo(employee.getJob() != null ? employee.getJob().getJobNo() : null)
    			.build();
    }
}

