package com.codenal.approval.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.codenal.approval.domain.Approver;

public interface ApproverRepository extends JpaRepository<Approver, Long>{

	// 합의자
	@Query(value = "select a from Approver a where a.approval.approvalNo = ?1 and a.approverType= '합의자'")
	List<Approver> findByApproverNo_Agree(Long approvalNo);
	
	// 결재자
	@Query(value = "select a from Approver a where a.approval.approvalNo = ?1 and a.approverType= '결재자'")
	List<Approver> findByApproverNo_Approver(Long approvalNo);
	
	// 결재 승인 시 상태 변경
	@Modifying
	@Query("UPDATE Approver a SET a.approvalStatus = ?1, a.approvalDate = ?2 "
	      + "WHERE a.approval.approvalNo = ?3 AND a.employee.empId = ?4")
	int updateStatus(int status, LocalDateTime ldt, Long approvalNo, Long loginId);
	
	// 현재 우선순위 확인 
	@Query("select a.approverPriority from Approver a where a.approval.approvalNo = ?1 and a.employee.empId = ?2")
	int findApproverPriority(Long approvalNo, Long empId);
	
	// 결재자 몇순위까지 있는지 카운트
	@Query("select count(a) from Approver a where a.approval.approvalNo =?1")
	Long countApprover(Long approvalNo);
	
	//
	@Query("select a from Approver a where a.employee.empId =?1")
	Approver findByEmpId(Long empId);
	
	// 결재 처음 등록 시 첫번째 합의자 또는 결재자 상태 업데이트
	@Modifying
	@Query("update Approver a set a.approvalStatus = 1 where a.employee.empId =?1 and a.approval.approvalNo = ?2")
	int firstUpdateStatus(Long id, Long approvalNo);
	
	// 다음 결재자 status 변경
	@Modifying
	@Query("update Approver a set a.approvalStatus = 1 where a.approval.approvalNo =?1 and a.approverPriority = ?2")
	int updateNextEmpIdStatus(Long approvalNo, int nextCurrentPriority);
	
}
