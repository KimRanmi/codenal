package com.codenal.alarms.repository;

import com.codenal.alarms.domain.Alarms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmsRepository extends JpaRepository<Alarms, Long> {
	
    List<Alarms> findByEmployee_EmpIdAndAlarmIsDeleted(Long empId, String alarmIsDeleted);
    
    // 특정 사원의 모든 알림 조회
    List<Alarms> findByEmployee_EmpId(Long empId);

    // 특정 사원의 읽지 않은 알림 조회
    List<Alarms> findByEmployee_EmpIdAndAlarmIsReadFalse(Long empId);
}
