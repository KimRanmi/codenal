package com.codenal.meeting.domain;

import java.sql.Time;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class MeetingRoomReserveDto {
	
	private Long meeting_room_reserve_no;
	private Long meeting_room_no;
	private Long emp_id;
	private LocalDate meeting_room_reserve_date;
	private Time meeting_room_start_time;
	private Time meeting_room_end_time;
	
	public MeetingRoomReserve toEntity() {
		return MeetingRoomReserve.builder()
				.meetingRoomReserveNo(meeting_room_reserve_no)
				.meetingRoomNo(meeting_room_no)
				.empId(emp_id)
				.meetingRoomReserveDate(meeting_room_reserve_date)
				.meetingRoomStartTime(meeting_room_start_time)
				.meetingRoomEndTime(meeting_room_end_time)
				.build();
	}
	
	public MeetingRoomReserveDto toDto(MeetingRoomReserve reserve) {
		return MeetingRoomReserveDto.builder()
				.meeting_room_reserve_no(reserve.getMeetingRoomReserveNo())
				.meeting_room_no(reserve.getMeetingRoomNo())
				.emp_id(reserve.getEmpId())
				.meeting_room_reserve_date(reserve.getMeetingRoomReserveDate())
				.meeting_room_start_time(reserve.getMeetingRoomStartTime())
				.meeting_room_end_time(reserve.getMeetingRoomEndTime())
				.build();
	}

}
