package com.codenal.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	public Page<EmployeeDto> selectEmployeeListOne(EmployeeDto searchDto, Pageable pageable) {
		Page<Employee> employeeList = null;

		String empStatus = searchDto.getEmpStatus();
		String statusCode = "재직".equals(empStatus) ? "Y" : "N";

		if(empStatus != null && "".equals(empStatus) == false) {
			int empStatusType = searchDto.getSearch_type();
			switch(empStatusType) {
			case 1 : 
				employeeList = employeeListRepository.findByEmpStatus(statusCode, pageable);
				break;
			}
		} else {
			employeeList = employeeListRepository.findAll(pageable);
		}
		List<EmployeeDto> employeeDtoList = new ArrayList<EmployeeDto>();
		for(Employee e : employeeList) {
			EmployeeDto dto = new EmployeeDto().fromEntity(e);
			employeeDtoList.add(dto);
		}
		return new PageImpl<>(employeeDtoList,pageable,employeeList.getTotalElements());
	}


	// 직원 목록 검색 2 (직원 정보)
	public Page<EmployeeDto> selectEmployeeListTwo(EmployeeDto searchDto, Pageable pageable) {
		Page<Employee> employeeList = null;

		String searchText = searchDto.getSearch_text();

		if (searchText != null && !searchText.isEmpty()) {
			int searchType = searchDto.getSearch_type();
			switch (searchType) {
			case 1:
				employeeList = employeeListRepository.findByEmpIdContaining(searchText, pageable);
				break;
			case 2:
				employeeList = employeeListRepository.findByEmpNameContaining(searchText, pageable);
				break;
			case 3:
				employeeList = employeeListRepository.findByDepartment_DeptNameContaining(searchText, pageable);
				break;
			case 4:
				employeeList = employeeListRepository.findByJob_JobNameContaining(searchText, pageable);
				break;
			case 5:
				employeeList = employeeListRepository.findByEmpPhoneContaining(searchText, pageable);
				break;
			default:
				employeeList = employeeListRepository.findAll(pageable);
				break;
			}
		} else {
			employeeList = employeeListRepository.findAll(pageable);
		}
		List<EmployeeDto> employeeDtoList = new ArrayList<EmployeeDto>();
		for(Employee e : employeeList) {
			EmployeeDto dto = new EmployeeDto().fromEntity(e);
			employeeDtoList.add(dto);
		}
		return new PageImpl<>(employeeDtoList,pageable,employeeList.getTotalElements());
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
