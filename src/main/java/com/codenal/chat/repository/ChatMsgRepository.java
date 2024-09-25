package com.codenal.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codenal.chat.domain.ChatMsg;

public interface ChatMsgRepository extends JpaRepository<ChatMsg, Integer>{

	@Query(value="SELECT m FROM ChatMsg m WHERE m.chatRoom = ?1 AND m.msgStatus = 'Y' ")
	List<ChatMsg> findAllById(int roomNo);

}