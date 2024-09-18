package com.codenal.addressBook.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codenal.addressBook.domain.TreeMenuDto;
import com.codenal.addressBook.respository.AddressBookRepository;
import com.codenal.admin.domain.Departments;
import com.codenal.admin.domain.DepartmentsDto;
import com.codenal.admin.repository.AdminRepository;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;

@Service
public class AddressBookService {

	private final AddressBookRepository addressBookRepository;
	private final AdminRepository adminRepository;

	@Autowired
	public AddressBookService(AddressBookRepository addressBookRepository, AdminRepository adminRepository) {
		this.addressBookRepository = addressBookRepository;
		this.adminRepository = adminRepository;
	}

	// ------------------직원 목록 검색 2 (직원 정보)
	public Page<EmployeeDto> searchByEmployeeInfo(EmployeeDto searchDto, Pageable pageable) {
		Page<Employee> addressBookSearch = null;

		String searchText = searchDto.getSearch_text();

		if (searchText != null && !"".equals(searchText)) {
			int searchType = searchDto.getSearch_type();

			switch (searchType) {
			case 1:
				addressBookSearch = adminRepository.findAllByEmpAuth("USER", pageable);
				break;
			case 2: 
				addressBookSearch = adminRepository.findByEmpNameContainingAndEmpAuth(searchText, "USER", pageable);
				break;
			case 3:
				addressBookSearch = adminRepository.findByDepartments_DeptNameContainingAndEmpAuth(searchText, "USER", pageable);
				break;
			case 4:
				addressBookSearch = adminRepository.findByJobs_JobNameContainingAndEmpAuth(searchText, "USER", pageable);
				break;
			case 5:
				addressBookSearch = adminRepository.findByEmpPhoneContainingAndEmpAuth(searchText, "USER", pageable);
				break;
			}
		} else {
			addressBookSearch = adminRepository.findAllByEmpAuth("USER", pageable);
		}


		List<EmployeeDto> addressBookSearchList = new ArrayList<EmployeeDto>();
		for (Employee e : addressBookSearch) {
			EmployeeDto dto = EmployeeDto.fromEntity(e);
			addressBookSearchList.add(dto);
		}

		return new PageImpl<>(addressBookSearchList, pageable, addressBookSearch.getTotalElements());
	}


	// TreeMenu
	public List<TreeMenuDto> getTreeMenu() {
	    List<Departments> departments = addressBookRepository.findAll();
	    System.out.println("Departments Fetched: " + departments);  // 부서 리스트 출력

	    return departments.stream().map(department -> {
	        // 직원(자식) 리스트를 만들고 부서(부모)가 생성 -> 노드에 들어갈 데이터 미리 준비
	        List<TreeMenuDto> employeeNodes = adminRepository.findByDepartments_DeptNo(department.getDeptNo()).stream()
	            .map(employee -> {
	                TreeMenuDto employeeNode = TreeMenuDto.builder()
	                        .nodeId(employee.getEmpId())    // 직원의 ID를 노드 ID로 설정
	                        .nodeName(employee.getEmpName() + " (" + employee.getJobs().getJobName() + ")") // 직원(자식) + 직급명
	                        .nodeState(TreeMenuDto.NodeState.builder().opened(false).build()) // 직원은 닫혀있기
	                        .build();
	     
	                return employeeNode;
	            })
	            .collect(Collectors.toList());

	        // 부서를 TreeMenuDto로 변환하고, 자식으로 직원 노드 추가
	        TreeMenuDto departmentNode = TreeMenuDto.builder()
	                .nodeId(department.getDeptNo())  // 부서의 ID를 노드 ID로 설정
	                .nodeName(department.getDeptName())    // 부서명을 노드 이름으로
	                .nodeState(TreeMenuDto.NodeState.builder().opened(true).build())    // 부서는 열려있기
	                .nodeChildren(employeeNodes)    // 부서 노드의 자식 = 해당 부서의 직원들
	                .build();

	        return departmentNode;
	    }).collect(Collectors.toList());
	}
}