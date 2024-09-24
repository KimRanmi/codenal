package com.codenal.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.meeting.domain.MeetingRoomReserve;

public interface MeetingRoomReserveRepository extends JpaRepository<MeetingRoomReserve, Long>{

}
