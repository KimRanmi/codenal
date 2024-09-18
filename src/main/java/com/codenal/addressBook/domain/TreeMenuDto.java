package com.codenal.addressBook.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import com.codenal.admin.domain.Departments;
import com.codenal.employee.domain.Employee;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TreeMenuDto {
    private long nodeId; // 노드 ID
    private String nodeName; // 노드 이름
    private NodeState nodeState; // 노드 상태 (opened만 사용)
    private List<TreeMenuDto> nodeChildren; // 자식 노드
    private String nodeIcon; // 노드 아이콘

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class NodeState {
        private boolean opened; // 노드가 열려 있는지 여부
        // selected 나 disabled 같은거 추가 하고 싶은면 여기에 추가
    }

    public static TreeMenuDto fromDepartment(Departments department, List<TreeMenuDto> employeeNodes) {
        return TreeMenuDto.builder()
                .nodeId(department.getDeptNo())
                .nodeName(department.getDeptName())
                .nodeState(NodeState.builder().opened(false).build())
                .nodeChildren(employeeNodes != null ? employeeNodes : new ArrayList<>()) 
                .build();
    }

    public static TreeMenuDto fromEmployee(Employee employee) {
        return TreeMenuDto.builder()
                .nodeId(employee.getEmpId())
                .nodeName(employee.getEmpName() + " (" + employee.getJobs().getJobName() + ")")
                .nodeState(NodeState.builder().opened(false).build())
                .build();
    }
}
