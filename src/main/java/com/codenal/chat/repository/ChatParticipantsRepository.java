package com.codenal.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codenal.chat.domain.ChatParticipants;

public interface ChatParticipantsRepository extends JpaRepository<ChatParticipants, Integer> {

	// 활성 상태의 본인이 참여중인 채팅 목록 조회
	@Query(value="SELECT p FROM ChatParticipants p WHERE empId = ?1 AND participateStatus = 'Y' ")
	List<ChatParticipants> findByChatRoom(Long empId);

	// 본인을 제외한 참여자 정보 조회
	@Query(value="SELECT p FROM ChatParticipants p WHERE roomNo = ?1 AND participateStatus = 'Y' AND employee != ?2 ")
	List<ChatParticipants> findByChatRoom(int roomNo, Long userId);

}
