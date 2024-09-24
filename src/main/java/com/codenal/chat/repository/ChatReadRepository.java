package com.codenal.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.codenal.chat.domain.ChatMsg;
import com.codenal.chat.domain.ChatParticipants;
import com.codenal.chat.domain.ChatRead;

public interface ChatReadRepository extends JpaRepository<ChatRead, Integer>{

	@Query(value="SELECT r FROM ChatRead r WHERE r.chatMsg = ?1 AND r.chatParticipant = ?2")
	List<ChatRead> findAllById(int chatMsg, int participantNo);

	@Modifying
	@Query(value="UPDATE ChatRead r SET r.isReceiverRead = 'Y' WHERE r.chatParticipant.participantNo = ?1")
	void updateByRead(int participantNo);

}
