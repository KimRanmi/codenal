package com.codenal.alarms.service;


import com.codenal.alarms.domain.AlarmType;
import com.codenal.alarms.domain.Alarms;
import com.codenal.alarms.domain.AlarmsDto;
import com.codenal.alarms.repository.AlarmsRepository;
import com.codenal.meeting.domain.MeetingRoomReserve;
import com.codenal.meeting.service.MeetingRoomService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmsService {

    private final AlarmsRepository alarmsRepository;
    private final MeetingRoomService meetingRoomService;

    // 새로운 알림을 생성하고 저장
    @Transactional
    public Alarms createAlarm(Alarms alarm) {
        return alarmsRepository.save(alarm);
    }

    // 특정 사원에 대한 모든 알림을 조회
    public List<Alarms> findAlarmsByEmployee(Long empId) {
        return alarmsRepository.findByEmployee_EmpIdAndAlarmIsDeleted(empId, "N");
    }

    // 특정 알림을 읽음 상태로 변경
    @Transactional
    public Alarms markAlarmAsRead(Long alarmNo) {
        Alarms alarm = alarmsRepository.findById(alarmNo)
                .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다."));
        alarm.setAlarmIsRead("Y");
        return alarmsRepository.save(alarm);
    }

    public List<AlarmsDto> getAlarmsByEmpId(Long empId) {
        List<Alarms> alarmsList = alarmsRepository.findByEmployeeEmpId(empId);
        List<AlarmsDto> alarmsDtoList = new ArrayList<>();
 
        for (Alarms alarm : alarmsList) {
            String roomName = null;
            String roomLocation = null;
            String alarmTime = null;

            if (alarm.getAlarmType().equals(AlarmType.MEETING)) {
            	MeetingRoomReserve reserve = meetingRoomService.findById(alarm.getAlarmReferenceNo());
                if (reserve != null) {
                    roomName = reserve.getMeetingRoom().getMeetingRoomName();
                    roomLocation = reserve.getMeetingRoom().getMeetingRoomPlace();
                    alarmTime = reserve.getMeetingRoomReserveDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                }
            }

            AlarmsDto alarmsDto = AlarmsDto.fromEntity(alarm, roomName, roomLocation, alarmTime);
            alarmsDtoList.add(alarmsDto);
        }

        return alarmsDtoList;
    }

}