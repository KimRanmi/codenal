package com.codenal.alarms.controller;

import com.codenal.alarms.domain.AlarmsDto;
import com.codenal.alarms.service.AlarmsService;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/alarms")
public class AlarmsViewController {

    private final AlarmsService alarmsService;
    
    @Autowired
	public AlarmsViewController(AlarmsService alarmsService) {
		this.alarmsService = alarmsService;
	}

    // 알림 리스트를 뷰에 전달
    @GetMapping("/topbar/{empId}")
    public String getAlarmsForTopbar(@PathVariable Long empId, Model model) {
    	
        List<AlarmsDto> alarms = alarmsService.getAlarmsByEmp(empId);
        
        model.addAttribute("alarms", alarms);
        return "partials/topbar"; // 알림 목록을 보여줄 HTML 경로
    }
}

