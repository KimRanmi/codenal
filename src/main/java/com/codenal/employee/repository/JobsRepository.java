package com.codenal.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.employee.domain.Jobs;

public interface JobsRepository extends JpaRepository<Jobs, Long>{
	
	Jobs findByJobNo(Long jobNo);

}
