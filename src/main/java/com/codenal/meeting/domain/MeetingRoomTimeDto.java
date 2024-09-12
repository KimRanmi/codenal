package com.codenal.meeting.domain;

import java.sql.Time;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MeetingRoomTimeDto {
	
	private Long meeting_room_time_no;
	private Long meeting_room_no;
	private Time meeting_room_start_time;
	private Time meeting_room_end_time;
	
	public MeetingRoomTime toEntity() {
		return MeetingRoomTime.builder()
				.meetingRoomTimeNo(meeting_room_time_no)
				.meetingRoomNo(meeting_room_no)
				.meetingRoomStartTime(meeting_room_start_time)
				.meetingRoomEndTime(meeting_room_end_time)
				.build();
	}
	
	public MeetingRoomTimeDto toDto(MeetingRoomTime time) {
		return MeetingRoomTimeDto.builder()
				.meeting_room_time_no(time.getMeetingRoomTimeNo())
				.meeting_room_no(time.getMeetingRoomNo())
				.meeting_room_start_time(time.getMeetingRoomStartTime())
				.meeting_room_end_time(time.getMeetingRoomEndTime())
				.build();
	}

}