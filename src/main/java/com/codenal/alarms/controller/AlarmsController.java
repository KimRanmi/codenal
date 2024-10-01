package com.codenal.alarms.controller;

import com.codenal.alarms.domain.Alarms;
import com.codenal.alarms.domain.AlarmsDto;
import com.codenal.alarms.service.AlarmsService;
import com.codenal.alarms.repository.EmpAlarmsRepository;
import com.codenal.employee.domain.Employee;

import lombok.RequiredArgsConstructor;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alarms")
@RequiredArgsConstructor
public class AlarmsController {

    private final AlarmsService alarmsService;
    private final EmpAlarmsRepository empAlarmsRepository;

    // 새로운 알림을 생성
    @PostMapping
    public ResponseEntity<AlarmsDto> createAlarm(@RequestBody AlarmsDto alarmDto) { 
        Employee employee = null;
        if (alarmDto.getEmpId() != null) {
            employee = empAlarmsRepository.findByEmpId(alarmDto.getEmpId())
                    .orElseThrow(() -> new IllegalArgumentException("사원 ID " + alarmDto.getEmpId() + "에 해당하는 사원을 찾을 수 없습니다."));
        }

        Alarms alarm = alarmDto.toEntity(employee);
        // 알림 저장
        Alarms createdAlarm = alarmsService.createAlarm(alarm);
        
        AlarmsDto responseDto = AlarmsDto.fromEntity(createdAlarm);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 특정 사원의 모든 알림을 조회
    @GetMapping
    public ResponseEntity<List<AlarmsDto>> getAlarmsByEmployee(@RequestParam Long empId) {
        // 직원의 모든 알림 조회
        List<Alarms> alarms = alarmsService.findAlarmsByEmployee(empId);

        List<AlarmsDto> alarmsDto = alarms.stream()
                .map(AlarmsDto::fromEntity)
                .collect(Collectors.toList()); 

        return new ResponseEntity<>(alarmsDto, HttpStatus.OK);
    }

    // 특정 알림을 읽음 상태로 변경
    @PutMapping("/{alarmNo}/read")
    public ResponseEntity<AlarmsDto> markAlarmAsRead(@PathVariable Long alarmNo) {
        // 알림 상태 변경
        Alarms updatedAlarm = alarmsService.markAlarmAsRead(alarmNo);
        
        AlarmsDto responseDto = AlarmsDto.fromEntity(updatedAlarm);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    
    @GetMapping("/alarms")
    public String getAlarmsPage(Model model, Authentication authentication) {
        // 현재 로그인한 사용자 정보 가져오기
        Employee employee = (Employee) authentication.getPrincipal();
        Long empId = employee.getEmpId();

        // 알림 데이터 조회
        List<AlarmsDto> alarmsList = alarmsService.getAlarmsByEmpId(empId);
        model.addAttribute("alarms", alarmsList);

        // 뷰 이름 반환 (HTML 템플릿 파일명)
        return "alarms"; // alarms.html
    }
}
