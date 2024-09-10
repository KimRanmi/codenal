package com.codenal.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codenal.admin.repository.EmployeeListRepository;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;
@Service
public class EmployeeListService {

    private final EmployeeListRepository employeeListRepository;

    @Autowired
    public EmployeeListService(EmployeeListRepository employeeListRepository) {
        this.employeeListRepository = employeeListRepository;
    }  

    // 직원 목록 검색 1 (재직 or 퇴사)
    public Page<EmployeeDto> searchByStatus(EmployeeDto searchDto, Pageable pageable) {
        Page<Employee> employeeListOne = null;

        String empStatus = searchDto.getEmpStatus();
        System.out.println("(서비스1) 받은 empStatus: " + empStatus);

        if (empStatus != null && !"".equals(empStatus)) {
            switch (empStatus) {
                case "Y":  // empStatus가 'Y'일 경우 (재직)
                    System.out.println("(서비스2) 재직 상태 검색 실행");
                    employeeListOne = employeeListRepository.findByEmpStatus("Y", pageable);
                    break;
                case "N":  // empStatus가 'N'일 경우 (퇴사)
                    System.out.println("(서비스3) 퇴사 상태 검색 실행");
                    employeeListOne = employeeListRepository.findByEmpStatus("N", pageable);
                    break;
            }
        } else {
            System.out.println("(서비스4) 재직 상태가 없으므로 전체 검색 실행");
            employeeListOne = employeeListRepository.findAll(pageable);
        }

        System.out.println("(서비스5) 검색된 직원 수: " + employeeListOne.getTotalElements());

        List<EmployeeDto> employeeDtoList = new ArrayList<>();
        for (Employee e : employeeListOne) {
            EmployeeDto dto = new EmployeeDto().fromEntity(e);
            employeeDtoList.add(dto);
        }

        return new PageImpl<>(employeeDtoList, pageable, employeeListOne.getTotalElements());
    }

    // 직원 목록 검색 2 (직원 정보)
    public Page<EmployeeDto> searchByEmployeeInfo(EmployeeDto searchDto, Pageable pageable) {
        Page<Employee> employeeListTwo = null;

        String searchText = searchDto.getSearch_text();
        System.out.println("(서비스6) 받은 검색어: " + searchText);

        if (searchText != null && !"".equals(searchText)) {
            int searchType = searchDto.getSearch_type();
            System.out.println("(서비스7) 검색 타입: " + searchType);

            switch (searchType) {
                case 0:
                    System.out.println("(서비스8) 전체 검색 실행");
                    employeeListTwo = employeeListRepository.findAll(pageable);
                    break;
                case 1:
                    System.out.println("(서비스8) 사번으로 검색");
                    employeeListTwo = employeeListRepository.findByEmpIdContaining(searchText, pageable);
                    break;
                case 2:
                    System.out.println("(서비스9) 직원명으로 검색");
                    employeeListTwo = employeeListRepository.findByEmpNameContaining(searchText, pageable);
                    break;
                case 3:
                    System.out.println("(서비스10) 부서명으로 검색");
                    employeeListTwo = employeeListRepository.findByDepartment_DeptNameContaining(searchText, pageable);
                    break;
                case 4:
                    System.out.println("(서비스11) 직급명으로 검색");
                    employeeListTwo = employeeListRepository.findByJob_JobNameContaining(searchText, pageable);
                    break;
                case 5:
                    System.out.println("(서비스12) 전화번호로 검색");
                    employeeListTwo = employeeListRepository.findByEmpPhoneContaining(searchText, pageable);
                    break;
            }
        } else {
            System.out.println("(서비스13) 검색어가 없으므로 전체 검색 실행");
            employeeListTwo = employeeListRepository.findAll(pageable);
        }

        System.out.println("(서비스14) 검색된 직원 수: " + employeeListTwo.getTotalElements());

        List<EmployeeDto> employeeDtoList = new ArrayList<EmployeeDto>();
        for (Employee e : employeeListTwo) {
            EmployeeDto dto = new EmployeeDto().fromEntity(e);
            employeeDtoList.add(dto);
        }

        return new PageImpl<>(employeeDtoList, pageable, employeeListTwo.getTotalElements());
    }

    // 직원 검색 통합 (재직/퇴사 + 직원 정보)
    public Page<EmployeeDto> searchAll(EmployeeDto searchDto, Pageable pageable) {
        System.out.println("(서비스15) 통합 검색 시작");
        System.out.println("(서비스16) 검색 타입: " + searchDto.getSearch_type());

        if (searchDto.getSearch_type() == 0) {
            // 재직 상태 검색
            System.out.println("(서비스17) 재직 상태 검색 실행");
            return searchByStatus(searchDto, pageable);
        } else {
            // 직원 정보 검색
            System.out.println("(서비스18) 직원 정보 검색 실행");
            return searchByEmployeeInfo(searchDto, pageable);
        }
    }








	// 직원 정보 상세 조회
	//	public EmployeeDto selectEmployeeListDetail(Long employeeId) {
	//	Employee announce = employeeListRepository.findByEmployeeId(employeeId);
	//	EmployeeDto dto = new EmployeeDto().toDto(announce);
	//     return dto;
	//    }


	// 직원 정보 수정
	//@Transactional
	//	public Employee selectEmployeeListUpdate(EmployeeDto dto) { 
	//	EmployeeDto temp = selectEmployeeOne(dto.get());
	//	temp.setBoard_title(dto.getBoard_title());
	//	temp.setBoard_content(dto.getBoard_content());
	//	if(dto.getOri_thumbnail() != null && "".equals(dto.getOri_thumbnail()) == false) {
	//		temp.setOri_thumbnail(dto.getOri_thumbnail());
	//		temp.setNew_thumbnail(dto.getNew_thumbnail());
	//	}

	//	Board board = temp.toEntity();
	//	Board result = boardRepository.save(board);
	//	return result;
	//}
}
