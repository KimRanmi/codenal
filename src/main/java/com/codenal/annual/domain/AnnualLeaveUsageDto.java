package com.codenal.annual.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString

public class AnnualLeaveUsageDto {
   
   private Long annual_usage_no;
   private LocalDate annual_usage_start_date;
   private LocalDate annual_usage_end_date;
   private Long emp_id;
   private int annual_type;
   private float total_day;
   private String time_period;
   
   public AnnualLeaveUsage toEntity() {
      return AnnualLeaveUsage.builder()
            .annualUsageNo(annual_usage_no)
            .annualUsageStartDate(annual_usage_start_date)
            .annualUsageEndDate(annual_usage_end_date)
            .annualType(annual_type)
            .timePeriod(time_period)
            .totalDay(total_day)
            .build();
   }
   
   public AnnualLeaveUsageDto toDto(AnnualLeaveUsage alu) {
      return AnnualLeaveUsageDto.builder()
            .annual_usage_no(alu.getAnnualUsageNo())
            .annual_usage_start_date(alu.getAnnualUsageStartDate())
            .annual_usage_end_date(alu.getAnnualUsageEndDate())
            .emp_id(alu.getEmployee().getEmpId()) // 내가 멋대로 추가한 거 허락 받을 것
            .annual_type(alu.getAnnualType())
            .time_period(alu.getTimePeriod())
            .total_day(alu.getTotalDay())
            .build();
   }
   
}
