package com.codenal.meeting.domain;

import java.sql.Time;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "meetingRoomReserve")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MeetingRoomReserve {
	
	@Id
	@Column(name = "meeting_room_reserve_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long meetingRoomReserveNo;
	
	@Column(name = "meeting_room_no")
	private Long meetingRoomNo;
	
	@Column(name = "emp_id")
	private Long empId;
	
	@Column(name = "meeting_room_reserve_date")
	private LocalDate meetingRoomReserveDate;

	@Column(name = "meeting_room_start_time")
	private Time meetingRoomStartTime;
	
	@Column(name = "meeting_room_end_time")
	private Time meetingRoomEndTime;

}
