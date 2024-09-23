package com.codenal.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.meeting.domain.MeetingRoomTime;

public interface MeetingRoomTimeRepository extends JpaRepository<MeetingRoomTime, Long>{
	
	MeetingRoomTime findByMeetingRoomTimeNo(Long meetingRoomTimeNo);

}
