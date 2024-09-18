package com.codenal.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.chat.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {

//	ChatRoom findAllById(ChatRoom chatRoom);
	ChatRoom findByRoomNo(int roomNo);
}
