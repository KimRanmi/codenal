package com.codenal.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.chat.domain.ChatMsg;

public interface ChatMsgRepository extends JpaRepository<ChatMsg, Integer>{

}
