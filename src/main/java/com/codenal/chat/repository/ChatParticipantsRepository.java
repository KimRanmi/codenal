package com.codenal.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codenal.chat.domain.ChatParticipants;
import com.codenal.chat.domain.ChatRoom;
import com.codenal.employee.domain.Employee;

public interface ChatParticipantsRepository extends JpaRepository<ChatParticipants, Integer> {

	// 활성 상태의 본인이 참여중인 채팅 목록 조회
	@Query(value="SELECT p FROM ChatParticipants p WHERE p.employee = ?1 AND p.participateStatus = 'Y' ")
	List<ChatParticipants> findByChatRoom(Employee empId);

	// 본인을 제외한 참여자 정보 조회
	@Query(value="SELECT p FROM ChatParticipants p WHERE p.chatRoom = ?1 AND p.participateStatus = 'Y' AND p.participantNo != ?2 ")
	List<ChatParticipants> findByChatRoom(ChatRoom roomNo, int userNo);

	// 내가 선택한 채팅방의 참가번호
	@Query(value="SELECT p FROM ChatParticipants p WHERE p.chatRoom = ?1 AND p.participateStatus = 'Y' AND p.participantNo = ?2 ")
	ChatParticipants findByParticipants(ChatRoom roomNo, int userNo);
	
	// 내가 선택한 채팅방의 참가번호
	@Query(value="SELECT p FROM ChatParticipants p WHERE p.chatRoom = ?1 AND p.participateStatus = 'Y' AND p.employee = ?2 ")
	ChatParticipants findByEmpId(ChatRoom roomNo, Employee empId);

}
