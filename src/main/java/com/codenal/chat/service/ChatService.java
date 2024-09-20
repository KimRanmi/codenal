package com.codenal.chat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codenal.chat.domain.ChatMsg;
import com.codenal.chat.domain.ChatMsgDto;
import com.codenal.chat.domain.ChatParticipants;
import com.codenal.chat.domain.ChatParticipantsDto;
import com.codenal.chat.domain.ChatRead;
import com.codenal.chat.domain.ChatRoom;
import com.codenal.chat.domain.ChatRoomDto;
import com.codenal.chat.repository.ChatMsgRepository;
import com.codenal.chat.repository.ChatParticipantsRepository;
import com.codenal.chat.repository.ChatReadRepository;
import com.codenal.chat.repository.ChatRoomRepository;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

@Service
public class ChatService {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatParticipantsRepository chatParticipantsRepository;
	private final EmployeeRepository employeeRepository;
	private final ChatMsgRepository chatMsgRepository;
	private final ChatReadRepository chatReadRepository;
	
	@Autowired
	public ChatService(ChatRoomRepository chatRoomRepository, ChatParticipantsRepository chatParticipantsRepository
			, EmployeeRepository employeeRepository, ChatMsgRepository chatMsgRepository, ChatReadRepository chatReadRepository) {
		this.chatRoomRepository=chatRoomRepository;
		this.chatParticipantsRepository=chatParticipantsRepository;
		this.chatMsgRepository=chatMsgRepository;
		this.chatReadRepository=chatReadRepository;
		this.employeeRepository=employeeRepository;
	}
	
	@Transactional
	public ChatRoom createChat(ChatRoomDto roomDto, List<String> empIds) {
		// 채팅방 이름 저장할 직원 이름 정보
		List<String> empName = new ArrayList<String>();
		for(String empId : empIds) {
		Employee emp = employeeRepository.findByEmpId(Long.parseLong(empId));
			empName.add(emp.getEmpName());
		}

		// 1. 채팅방 정보 save
		ChatRoom chatRoom = ChatRoom.builder()
				.chatName(empName.toString().substring(1, empName.toString().length()-1))
				.roomType(empIds.size()<=2?1:2)
				.roomStatus('Y')
				.build();
		ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
			
		for(String empId : empIds) {
			// 2. 참여자 정보 save
			ChatParticipants participant = ChatParticipants.builder()
				.chatRoom(savedChatRoom)
				.employee(employeeRepository.findByEmpId(Long.parseLong(empId)))
				.switchStatus('Y')
				.participateStatus('Y')
				.build();
			chatParticipantsRepository.save(participant);
		}
		return savedChatRoom;
	}
	
	// 활성 상태의 참여중인 채팅 목록 조회
	public List<ChatParticipantsDto> chatListSelect(String username){
		Long empId = Long.parseLong(username);
		List<ChatParticipants> chatLists = chatParticipantsRepository.findByChatRoom(empId);
		List<ChatParticipantsDto> chatListDto = new ArrayList<ChatParticipantsDto>();
		for(ChatParticipants chatList :chatLists) {
			ChatParticipantsDto dto = new ChatParticipantsDto().toDto(chatList);
			chatListDto.add(dto);
		}
			
//		for(ChatParticipants chatList : chatLists) {
//		ChatRoom chatRoomList = chatRoomRepository.findAllById(chatList.getChatRoom());
//		}
		
		return chatListDto;
	}
	
	
	public void chatRoomDetail(int roomNo, Long userId) {
		// 채팅 참여자 정보와 채팅 메시지 불러오기
		// 채팅방 입장 순간 알림온 메시지 읽은 상태로 update
		ChatRoom chatRoom = chatRoomRepository.findByRoomNo(roomNo);
		ChatParticipants chatParticipantNo = chatParticipantsRepository.findById(roomNo, userId);
		// 메시지 상태들이 'Y'인 메시지 불러오기  --> 필요없는거 같긴 함
		List<ChatMsg> msgNo = chatMsgRepository.findAllById(roomNo);
		chatReadRepository.findAllById(chatParticipantNo);  // --> roomNo 마다 내 참가자 번호가 다르니까 위에서 찾은 참가자 번호로만 업데이트하면 
	}
	
	
	// 메시지 전송시 생성
	@Transactional
	public int createChatMsg(ChatMsgDto dto, Long userId) {
		int result = -1; 
		try {
			ChatRoom room = chatRoomRepository.findByRoomNo(dto.getRoom_no());
			Employee emp = employeeRepository.findByEmpId(dto.getSender_id());
			ChatMsg target = ChatMsg.builder()
					.msgContent(dto.getMsg_content())
					.chatRoom(room)
					.employee(emp)
					.msgStatus('Y')
					.msgType(dto.getMsg_type())
					.build();

			ChatMsg savedMsg = chatMsgRepository.save(target); // 채팅 메시지 정보 저장
			
			List<ChatParticipants> participants = chatParticipantsRepository.findByChatRoom(room.getRoomNo(), userId); // 본인을 제외한 참여자 정보
			for(ChatParticipants participant : participants) { // 읽지 않은 상태를 저장
				ChatRead read = ChatRead.builder()
						.chatMsg(savedMsg)
						.chatParticipant(participant)
						.isReceiverRead('N')
						.build();
				chatReadRepository.save(read);
				result = 1;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
