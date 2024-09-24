package com.codenal.meeting.service;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codenal.meeting.domain.MeetingRoom;
import com.codenal.meeting.domain.MeetingRoomDto;
import com.codenal.meeting.domain.MeetingRoomReserve;
import com.codenal.meeting.domain.MeetingRoomReserveDto;
import com.codenal.meeting.domain.MeetingRoomTime;
import com.codenal.meeting.domain.MeetingRoomTimeDto;
import com.codenal.meeting.repository.MeetingRoomRepository;
import com.codenal.meeting.repository.MeetingRoomReserveRepository;
import com.codenal.meeting.repository.MeetingRoomTimeRepository;

@Service
public class MeetingRoomService {
	
	private MeetingRoomRepository meetingRoomRepository;
	private MeetingRoomTimeRepository meetingRoomTimeRepository;
	private MeetingRoomReserveRepository meetingRoomReserveRepository;
	
	@Autowired
	public MeetingRoomService(MeetingRoomRepository meetingRoomRepository, MeetingRoomTimeRepository meetingRoomTimeRepository, MeetingRoomReserveRepository meetingRoomReserveRepository) {
		this.meetingRoomRepository = meetingRoomRepository;
		this.meetingRoomTimeRepository = meetingRoomTimeRepository;
		this.meetingRoomReserveRepository = meetingRoomReserveRepository;
	}
	
	// 회의실 삭제
	public int MeetingRoomDelete(Long roomNo) {
		int result = 0;
		
		try {
			meetingRoomRepository.deleteById(roomNo);
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	// 회의실 추가
	public MeetingRoom MeetingRoomCreate(MeetingRoomDto dto) {
		MeetingRoom create = MeetingRoom.builder()
				.meetingRoomName(dto.getMeeting_room_name())
				.meetingRoomPlace(dto.getMeeting_room_place())
				.meetingRoomAmenity(dto.getMeeting_room_amenity())
				.meetingRoomImg(dto.getMeeting_room_img())
				.build();
		return meetingRoomRepository.save(create);
	}
	
	// 회의실 예약
	public MeetingRoomReserve meetingRoomReserve(MeetingRoomReserveDto dto) {
		int result = 0;
		MeetingRoomReserve reserve = MeetingRoomReserve.builder()
				.meetingRoomNo(dto.getMeeting_room_no())
				.empId(dto.getEmp_id())
				.meetingRoomReserveDate(dto.getMeeting_room_reserve_date())
				.meetingRoomReserveTimeNo(dto.getMeeting_room_reserve_time_no())
				.build();
		return meetingRoomReserveRepository.save(reserve);
	}
	
	// 회의실 리스트 뽑기
	public List<MeetingRoomDto> meetingRoomList() {
		List<MeetingRoom> mr = meetingRoomRepository.findAll();
		System.out.println(mr);
		List<MeetingRoomDto> mrDto = new ArrayList<MeetingRoomDto>();
		for(MeetingRoom m : mr) {
			MeetingRoomDto mrToDto = new MeetingRoomDto().toDto(m);
			mrDto.add(mrToDto);
		}
//		MeetingRoomTime meetingTime = meetingRoomTimeRepository.findByMeetingRoomTimeNo(mrDto.get(0).getMeeting_room_no());
		return mrDto;
	}
	
	// 회의실 예약 시간 리스트 뽑기
	public List<MeetingRoomTimeDto> meetingRoomTimeList(){
		List<MeetingRoomTime> time = meetingRoomTimeRepository.findAll();
		List<MeetingRoomTimeDto> timeDto = new ArrayList<MeetingRoomTimeDto>();
		for(MeetingRoomTime t : time) {
			MeetingRoomTimeDto timeToDto = new MeetingRoomTimeDto().toDto(t);
			SimpleDateFormat format = new SimpleDateFormat("hh:mm");
			String hh = format.format(timeToDto.getMeeting_room_start_time());
			System.out.println(hh);
			timeDto.add(timeToDto);
		}
		System.out.println(timeDto);
		return timeDto;
	}

}
