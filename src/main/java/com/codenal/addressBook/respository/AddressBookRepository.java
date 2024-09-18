package com.codenal.addressBook.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codenal.admin.domain.Departments;

@Repository
public interface AddressBookRepository extends JpaRepository<Departments, Long> {

	// 전 부서
    List<Departments> findAll();

   
 
}