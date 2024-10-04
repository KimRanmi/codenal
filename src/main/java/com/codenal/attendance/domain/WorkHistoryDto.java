	package com.codenal.attendance.domain;
	
	import java.math.BigDecimal;
	import java.time.LocalDate;
	
	import com.codenal.employee.domain.Employee;
	
	import lombok.AllArgsConstructor;
	import lombok.Builder;
	import lombok.Getter;
	import lombok.NoArgsConstructor;
	import lombok.Setter;
	
	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
	@Builder
	public class WorkHistoryDto {
	
	    private Long workHistoryNo;
	
	   
	    private LocalDate workHistoryDate;
	
	    
	    private BigDecimal workHistoryTotalTime;
	
	   
	    private BigDecimal workHistoryOverTime;
	
	 
	    private Long empId;
	
	    // DTO를 엔티티로 변환하는 메서드 (Employee는 별도로 조회 필요)
	    public WorkHistory toEntity(Employee employee) {
	        return WorkHistory.builder()
	                .workHistoryDate(this.workHistoryDate)
	                .workHistoryTotalTime(this.workHistoryTotalTime)
	                .workHistoryOverTime(this.workHistoryOverTime)
	                .employee(employee)
	                .build();
	    }
	
	    // 엔티티를 DTO로 변환하는 메서드
	    public static WorkHistoryDto fromEntity(WorkHistory workHistory) {
	        return WorkHistoryDto.builder()
	                .workHistoryNo(workHistory.getWorkHistoryNo())
	                .workHistoryDate(workHistory.getWorkHistoryDate())
	                .workHistoryTotalTime(workHistory.getWorkHistoryTotalTime())
	                .workHistoryOverTime(workHistory.getWorkHistoryOverTime())
	                .empId(workHistory.getEmployee().getEmpId())
	                .build();
	    }
	   
	}
