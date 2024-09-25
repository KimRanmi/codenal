package com.codenal.attendance.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
import lombok.Setter;

@Entity
@Table(name = "attendance")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter	
@Setter
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attend_no")
    private Long attendNo;

    @Column(name = "attend_start_time")
    private LocalDateTime attendStartTime;

    @Column(name = "attend_end_time")
    private LocalDateTime attendEndTime;

    @Column(name = "work_date")
    private LocalDateTime workDate;

    @Column(name = "attend_working_time")
    private BigDecimal attendWorkingTime;

    @Column(name = "attend_status")
    private String attendStatus;

    @Column(name = "emp_id")
    private Long empId;
}
