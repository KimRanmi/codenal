package com.codenal.alarms.schedular;

import com.codenal.alarms.domain.AlarmType;
import com.codenal.alarms.domain.Alarms;
import com.codenal.alarms.service.AlarmsService;
import com.codenal.meeting.domain.MeetingRoomReserve;
import com.codenal.meeting.service.MeetingRoomService;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MeetingRoomAlarmScheduler {

	private final AlarmsService alarmsService;
	private final MeetingRoomService meetingRoomService;
	private final EmployeeService employeeService;

	// 매일 아침 9시에 실행
	// 해당 날짜에 예약된 회의실을 조회하고 알림을 생성
	@Scheduled(fixedRate = 60000)  // 60초마다 실행 알림 구현되면 오전 9시로 변경

	public void sendMeetingRoomAlarms() {
		log.info("회의실 예약 알림 스케줄러 실행 시작");

		try {
			LocalDate today = LocalDate.now();
			List<MeetingRoomReserve> reservations = meetingRoomService.meetingAlarms(today);

			for (MeetingRoomReserve reservation : reservations) {
				// 사원 ID로 사원 객체 조회
				Employee employee = employeeService.findByEmpId(reservation.getEmpId());
				if (employee == null) {
					log.warn("사원 ID {}에 해당하는 사원을 찾을 수 없습니다.", reservation.getEmpId());
					continue;
				}

				// 알림 생성
				Alarms alarm = Alarms.builder()
						.employee(employee) // 예약한 사원
						.alarmTitle("회의실 예약 알림")
						.alarmContext(reservation.getMeetingRoom().getMeetingRoomName() + " 회의실 예약이 있습니다.")
						.alarmType(AlarmType.MEETING) // 알림 유형 설정
						.alarmReferenceNo(reservation.getMeetingRoomReserveNo()) // 예약 ID
						.alarmCreateTime(LocalDateTime.now())
						.alarmIsRead("N")
						.alarmIsDeleted("N")
						.alarmUrl("/meeting-room/reservations/" + reservation.getMeetingRoomReserveNo())
						.build();

				alarmsService.createAlarm(alarm);
				log.info("알림 생성: {}", alarm);
			}
		} catch (Exception e) {
			log.error("회의실 예약 알림 스케줄러 실행 중 오류 발생", e);
		}

		log.info("회의실 예약 알림 스케줄러 실행 완료");
	}
}