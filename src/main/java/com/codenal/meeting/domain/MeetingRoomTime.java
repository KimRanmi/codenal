package com.codenal.meeting.domain;

import java.sql.Time;

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
@Table(name = "meetingRoomTime")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MeetingRoomTime {
	
	@Id
	@Column(name = "meeting_room_time_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long meetingRoomTimeNo;
	
	@Id
	@Column(name = "meeting_room_no")
	private Long meetingRoomNo;
	
	@Id
	@Column(name = "meeting_room_start_time")
	private Time meetingRoomStartTime;
	
	@Id
	@Column(name = "meeting_room_end_time")
	private Time meetingRoomEndTime;

}
