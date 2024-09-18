package com.codenal.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.chat.domain.ChatRead;

public interface ChatReadRepository extends JpaRepository<ChatRead, Integer>{

}
