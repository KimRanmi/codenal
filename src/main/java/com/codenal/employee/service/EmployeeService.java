package com.codenal.employee.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;
import com.codenal.employee.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository; 
        }

    public void registerEmployee(EmployeeDto employeeDto) {
        // 비밀번호 암호화 제거, 원시 비밀번호를 그대로 사용
        Employee employee = employeeDto.toEntity();
        employeeRepository.save(employee);
    }

//    public Employee selectEmpId(String empId) {
//    	Employee emp = employeeRepository.findByEmpName(empId);
//    	return emp;
//    }
    

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee); // 데이터베이스에 변경 사항 저장
    }
    
    
    public Employee getEmployeeById(Long empId) {
        return employeeRepository.findByEmpId(empId);
    }

    // USER 권한이면서 본인을 제외한 재직중인 직원 모두 조회
    public List<EmployeeDto> getActiveEmployeeList(String username){
    	Long empId = Long.parseLong(username);
    	List<Employee> empList = employeeRepository.findAllActiveEmployees(empId);
    	List<EmployeeDto> dtoList = new ArrayList<EmployeeDto>();
    	for(Employee e : empList) {
    		EmployeeDto dto = new EmployeeDto().fromEntity(e);
    		dtoList.add(dto);
    	}
    	return dtoList;
    }
    
    
   
    public EmployeeDto getEmployeeDtoById(Long empId) {
        Employee employee = employeeRepository.findByEmpId(empId);
        if (employee != null) {
            return EmployeeDto.fromEntity(employee);
        }
        return null;
    }

}