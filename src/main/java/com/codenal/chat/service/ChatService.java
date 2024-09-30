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
	public ChatRoom createChat(ChatRoomDto roomDto, List<String> empIds, Long userId) {
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
			ChatParticipants participantNo = chatParticipantsRepository.save(participant);
		}
		
		//  초대 정보 메시지 추가
		Employee emp = employeeRepository.findByEmpId(userId);
		ChatParticipants userNo = chatParticipantsRepository.findByEmpId(savedChatRoom, emp);
		String empNames = "";
		for(String empId : empIds) {
			Employee employee = employeeRepository.findByEmpId(Long.parseLong(empId));
			if(!employee.getEmpId().equals(emp.getEmpId())) {
			empNames += employee.getEmpName()+"님,";
			}
		}
		String empname = empNames.substring(0,empNames.length()-1);
		ChatMsg target = ChatMsg.builder()
				.msgContent(userNo.getEmployee().getEmpName()+"님이 "+empname+"을 초대했습니다.")
				.chatRoom(savedChatRoom)
				.chatParticipant(userNo)
				.msgStatus('Y')
				.msgType('1')
				.build();

		ChatMsg savedMsg = chatMsgRepository.save(target);
		
		
		return savedChatRoom;
	}
	
	// 본인의 활성 상태인 채팅 참여 테이블 조회
	public List<ChatParticipants> participantListSelect(String username){
		Long empId = Long.parseLong(username);
		Employee emp = employeeRepository.findByEmpId(empId);
		List<ChatParticipants> chatLists = chatParticipantsRepository.findByChatRoom(emp);
		List<ChatParticipantsDto> chatListDto = new ArrayList<ChatParticipantsDto>();
		for(ChatParticipants chatList :chatLists) {
			ChatParticipantsDto dto = new ChatParticipantsDto().toDto(chatList);
			dto.setRoom_no(chatList.getChatRoom().getRoomNo());
			dto.setEmp_id(chatList.getEmployee().getEmpId());
			chatListDto.add(dto);
		}
		return chatLists;
	}
	
	// 본인의 활성 상태의 참여중인 채팅방 목록 조회
//	public List<ChatRoomDto> chatListSelect(String username){
//		Long empId = Long.parseLong(username);
//		Employee emp = employeeRepository.findByEmpId(empId);
//		List<ChatRoom> chatLists = chatRoomRepository.findByEmpId(emp);
//		List<ChatRoomDto> chatListDto = new ArrayList<ChatRoomDto>();
//		for(ChatRoom chatList :chatLists) {
//			ChatRoomDto dto = new ChatRoomDto().toDto(chatList);
//			chatListDto.add(dto);
//		}
//		
//		return chatListDto;
//	}
	
	
	// chat detail 부분
	@Transactional
	public ChatRoom selectChatRoomOne(int roomNo, Long empId) {
		// 채팅 참여자 정보와 채팅 메시지 불러오기
		// 채팅방 입장 순간 알림온 메시지 읽은 상태로 update
		ChatRoom chatRoom = chatRoomRepository.findByRoomNo(roomNo);
		Employee employee = employeeRepository.findByEmpId(empId);
		// 내 참가자 번호 불러오기
		ChatParticipants chatParticipant = chatParticipantsRepository.findByEmpId(chatRoom, employee);
		// 메시지 상태들이 'Y'인 메시지 불러오기  --> 필요없는거 같긴 함
//		List<ChatMsg> msgNo = chatMsgRepository.findAllById(roomNo);
		chatReadRepository.updateByRead(chatParticipant.getParticipantNo());  // --> roomNo 마다 내 참가자 번호가 다르니까 위에서 찾은 참가자 번호로만 업데이트하면 됨
		
		return chatRoom;
	}
	
	
	// 메시지 전송시 생성
	@Transactional
	public int createChatMsg(ChatMsgDto dto) {
		int result = -1; 
		try {
			ChatRoom room = chatRoomRepository.findByRoomNo(dto.getRoom_no());
			ChatParticipants senderNo = chatParticipantsRepository.findByParticipants(room,dto.getSender_no());
			ChatMsg target = ChatMsg.builder()					.chatRoom(room)
					.chatParticipant(senderNo)
					.msgContent(dto.getMsg_content())
					.msgStatus('Y')
					.msgType(dto.getMsg_type())
					.build();

			ChatMsg savedMsg = chatMsgRepository.save(target); // 채팅 메시지 정보 저장
			
			ChatParticipants participantNo = chatParticipantsRepository.findByParticipants(room,dto.getParticipant_no());
//			List<ChatParticipants> participants = chatParticipantsRepository.findByChatRoom(room, userNo); // 본인을 제외한 참여자 정보
//			for(ChatParticipants participant : participants) { // 읽지 않은 상태를 저장
				ChatRead read = ChatRead.builder()
						.chatMsg(savedMsg)
						.chatParticipant(participantNo)
						.isReceiverRead('N')
						.build();
				chatReadRepository.save(read);
				result = 1;
//			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
