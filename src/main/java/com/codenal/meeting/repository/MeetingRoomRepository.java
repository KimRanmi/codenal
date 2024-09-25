package com.codenal.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.meeting.domain.MeetingRoom;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long>{

}
