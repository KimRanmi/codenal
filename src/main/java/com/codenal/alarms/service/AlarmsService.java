package com.codenal.alarms.service;


import com.codenal.alarms.domain.Alarms;
import com.codenal.alarms.repository.AlarmsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmsService {

    private final AlarmsRepository alarmsRepository;

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

}