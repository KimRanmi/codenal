package com.codenal.websocket;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.codenal.alarms.domain.AlarmType;
import com.codenal.alarms.domain.Alarms;
import com.codenal.alarms.service.AlarmsService;
import com.codenal.approval.domain.Approval;
import com.codenal.approval.domain.Approver;
import com.codenal.approval.service.ApprovalService;
import com.codenal.approval.service.ApproverService;

@Component
public class NotificationWebSocketHandler extends TextWebSocketHandler{
		
		private final AlarmsService alarmsService;
		
		@Autowired
		public NotificationWebSocketHandler(AlarmsService alarmsService) {
			this.alarmsService = alarmsService;
		}
		

		 private Map<String,WebSocketSession> clients = new
		 HashMap<String,WebSocketSession>();
		 
			
		// WebSocket 세션을 관리하기 위한 리스트
		 private static List<WebSocketSession> notificationSessions = new CopyOnWriteArrayList<>();
		 
		 
			// 클라이언트가 연결되었을 때 설정
			@Override
			public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			    System.out.println("New connection established: " + session.getId());
			    notificationSessions.add(session);
			}
		
			// 클라이언트가 웹소켓 서버로 메시지를 전송했을 때 설정
			@Override
			protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
				
			    clients.put(session.getPrincipal().getName(), session); // 클라이언트 등록
//			    System.out.println("=== 등록 확인 ===");
			 //   System.out.println("현재 접속중인 클라이언트: " + clients);
			    
			    
			    
			    // 클라이언트로부터 받은 메시지 처리
			    String receivedMessage = message.getPayload();
				System.out.println("알림Received payload: " + receivedMessage);

			    System.out.println("받은 메시지: " + receivedMessage);
			    
			    // 받은 메시지에 따라 처리 후 클라이언트로 응답 전송
			    if (receivedMessage.equals("New chat message")) {
			        // 클라이언트에 알림 메시지 전송
			        String notificationMessage = "You have 3 new notifications!";
			        session.sendMessage(new TextMessage(notificationMessage));
			    }
			    
			    
			    
			    
//				// 클라이언트가 보낸 메시지 
//				String payload = message.getPayload();
//				System.out.println("Received payload: " + payload);
//				// Jackson ObjectMapper 객체 생성
//				ObjectMapper objectMapper = new ObjectMapper();
//				
//				Map<String,String> resultMap = new HashMap<String,String>();
//				
//				JSONObject json = new JSONObject(message.getPayload());
//				
//				switch(){
//					case "noti":
//						
//						  
//						        
//						        for (WebSocketSession client : clients.values()) {
//						            if (client.isOpen()) {  // 세션이 열려있는지 확인
//						                // 클라이언트에게 메시지 전송
//						                client.sendMessage(new TextMessage(objectMapper.writeValueAsString(resultMap)));
//						            }
//						        }
//							  
//						  
//					break;
//				}
			}

			// 알림 핸들러로 메시지 전송 메서드
			public static void sendNotificationToAll(String notificationMessage) throws Exception {
				for (WebSocketSession session : notificationSessions) {
					if (session.isOpen()) {
						System.out.println("알림 1");
						session.sendMessage(new TextMessage(notificationMessage));
					}
				}
			}
			
			// 알림 핸들러로 메시지 전송 메서드
			public void sendNotification(String notificationMessage, Approval approval) throws Exception {
			    
				String formattedMessage = notificationMessage + ": " + approval.getApprovalTitle() + " (" + approval.getApprovalNo() + ")";
			    
			    String message = "";
		    	
		    	switch(notificationMessage) {
		    		case "authorization" :  
		    			message = "승인되었습니다.";
		        		break;
		    	
		    	}
		    	
		    	Alarms alarms = Alarms.builder()
		    					.employee(approval.getEmployee())
		    					.alarmTitle(message)
		    					.alarmContext(approval.getApprovalTitle()+"의 결재가 "+message)
		    					.alarmType(AlarmType.APPROVAL)
		    					.alarmReferenceNo(approval.getApprovalNo())
		    					.alarmCreateTime(LocalDateTime.now())
		    					.alarmIsRead("N")
		    					.alarmIsDeleted("N")
		    					.alarmUrl("/approval/"+approval.getApprovalNo())
		    					.build();
		    	alarmsService.createAlarm(alarms);
			     
			    // 승인 당사자의 세션을 찾고 메시지 전송
			    WebSocketSession recipientSession = clients.get(approval.getEmployee().getEmpId().toString());
			    if (recipientSession != null && recipientSession.isOpen()) {
			        System.out.println("특정 사용자에게 알림 전송");
			        recipientSession.sendMessage(new TextMessage(formattedMessage)); // 제목 및 번호 전송
			    } else {
			        System.out.println("세션이 없거나 닫혀 있습니다.");
			    }
			    
			    
			    
			}

			
			// 클라이언트가 연결을 끊었을 때 설정
			@Override
			public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
				// 나간 사용자의 세션을 clients 맵에서 제거
			    // 나머지 사용자들의 세션은 여전히 clients 맵에 남아있으므로, 채팅 가능
				clients.values().removeAll(Arrays.asList(session));
				System.out.println("=== 삭제 확인 ===");
				for(String senderNo : clients.keySet()) {
					System.out.println(senderNo +" : "+clients.get(senderNo));
				}		
			}
			


}
