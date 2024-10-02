package com.codenal.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codenal.chat.domain.ChatRoom;
import com.codenal.employee.domain.Employee;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {

//	ChatRoom findAllById(ChatRoom chatRoom);
	ChatRoom findByRoomNo(int roomNo);


//	@Query(value="SELECT r FROM ChatRoom r WHERE r.participants = ?1")
//	List<ChatRoom> findByEmpId(Employee emp);
}
