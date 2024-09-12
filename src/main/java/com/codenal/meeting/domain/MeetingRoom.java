package com.codenal.meeting.domain;

import java.time.LocalDateTime;

import com.codenal.calendar.domain.Calendar;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "meetingRoom")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class MeetingRoom {
	
	@Id
	@Column(name = "meeting_room_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long meetingRoomNo;
	
	@Column(name = "meeting_room_name")
	private String meetingRoomName;
	
	@Column(name = "meeting_room_place")
	private String meetingRoomPlace;
	
	@Column(name = "meeting_room_info")
	private String meetingRoomInfo;
	
	@Column(name = "meeting_room_img")
	private String meetingRoomImg;

}