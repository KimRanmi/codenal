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
import com.codenal.meeting.domain.MeetingRoomTime;
import com.codenal.meeting.domain.MeetingRoomTimeDto;
import com.codenal.meeting.repository.MeetingRoomRepository;
import com.codenal.meeting.repository.MeetingRoomTimeRepository;

@Service
public class MeetingRoomService {
	
	private MeetingRoomRepository meetingRoomRepository;
	private MeetingRoomTimeRepository meetingRoomTimeRepository;
	
	@Autowired
	public MeetingRoomService(MeetingRoomRepository meetingRoomRepository, MeetingRoomTimeRepository meetingRoomTimeRepository) {
		this.meetingRoomRepository = meetingRoomRepository;
		this.meetingRoomTimeRepository = meetingRoomTimeRepository;
	}
	
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
