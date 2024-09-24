package com.codenal.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codenal.admin.domain.Departments;
import com.codenal.admin.repository.DepartmentsRepository;

@Service
public class DepartmentsService {
	
	private DepartmentsRepository departmentsRepository;
	
	
	public DepartmentsService(DepartmentsRepository departmentsRepository) {
		this.departmentsRepository = departmentsRepository;
	}
	
	public List<Departments> findJobAll(){
		return departmentsRepository.findAll();
	}
}
