package com.codenal.chat.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.codenal.chat.domain.ChatMsgDto;
import com.codenal.chat.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler{
	
	private final ChatService chatService;
	
	@Autowired
	public ChatWebSocketHandler(ChatService chatService) {
		this.chatService = chatService;
	}
	
		private Map<Long,WebSocketSession> clients 
			= new HashMap<Long,WebSocketSession>();
		
		// 클라이언트가 연결되었을 때 설정
		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {}
	
		// 클라이언트가 웹소켓 서버로 메시지를 전송했을 때 설정
		@Override
		protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
			// 클라이언트가 보낸 메시지 
			String payload = message.getPayload();
			// Jackson ObjectMapper 객체 생성
			ObjectMapper objectMapper = new ObjectMapper();
			// JSON -> SendMessage 형태 변환
			ChatMsgDto msg = objectMapper.readValue(payload, ChatMsgDto.class);
	
			switch(msg.getChat_type()){
				case "open":
					clients.put(msg.getSender_id(), session); // 클라이언트 등록
					System.out.println("=== 등록 확인 ===");
					for(Long userId : clients.keySet()) {
						System.out.println(userId +" : "+clients.get(userId));
					}		
				break;
				case "msg":
					for(Long userId : clients.keySet()) {
					chatService.createChatMsg(msg, userId);  // 메시지 처리
					}
				break;
			}
			
		}
	
		// 클라이언트가 연결을 끊었을 때 설정
		@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
			// 나간 사용자의 세션을 clients 맵에서 제거
		    // 나머지 사용자들의 세션은 여전히 clients 맵에 남아있으므로, 채팅 가능
			clients.values().removeAll(Arrays.asList(session));
			System.out.println("=== 삭제 확인 ===");
			for(Long userId : clients.keySet()) {
				System.out.println(userId +" : "+clients.get(userId));
			}		
		}
		
}
