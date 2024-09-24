package com.codenal.approval.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.codenal.approval.domain.Approval;
import com.codenal.approval.domain.ApprovalCategory;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
		
		// 상신리스트
		@Query(value="SELECT a,f FROM Approval a "
				 + "JOIN a.employee e "
		         + "JOIN a.approvalCategory c "
		         + "JOIN c.approvalForm f "
		         + "where a.approvalStatus = ?1 and e.empId = ?2",
		         countQuery = "SELECT count(DISTINCT a) FROM Approval a "
		         +"JOIN a.employee e "
		         +"JOIN a.approvalCategory c "
		         +"JOIN c.approvalForm f "
		         +"WHERE a.approvalStatus = ?1 and e.empId = ?2 ")
		Page<Object[]> findList(int num,Long id,Pageable pageable);
		
		
		// 수신리스트
		@Query(value = "SELECT a, f, v FROM Approval a "
	             + "JOIN a.employee e "
	             + "JOIN a.approvalCategory c "
	             + "JOIN c.approvalForm f "
	             + "JOIN a.approver v "
	             + "WHERE v.approvalStatus = ?1 AND v.employee.empId = ?2 and a.approvalStatus = 1",
	       countQuery = "SELECT count(DISTINCT a) FROM Approval a "
	                  + "JOIN a.employee e "
	                  + "JOIN a.approvalCategory c "
	                  + "JOIN c.approvalForm f "
	                  + "JOIN a.approver v "
	                  + "WHERE v.approvalStatus = ?1 AND v.employee.empId = ?2 and a.approvalStatus = 1")
		Page<Object[]> findinboxList(int status, Long empId, Pageable pageable);

		
		// 수신참조 리스트
		@Query(value = "SELECT a, f, v FROM Approval a "
	             + "JOIN a.employee e "
	             + "JOIN a.approvalCategory c "
	             + "JOIN c.approvalForm f "
	             + "JOIN a.referrer r "
	             + "JOIN a.approver v "
	             + "WHERE r.employee.empId = ?1",
	       countQuery = "SELECT count(DISTINCT a) FROM Approval a "
	                  + "JOIN a.employee e "
	                  + "JOIN a.approvalCategory c "
	                  + "JOIN c.approvalForm f "
	                  + "JOIN a.approver r "
	                  + "JOIN a.approver v "
	                  + "WHERE r.employee.empId = ?1")
		Page<Object[]> findReferrerList(Long empId, Pageable pageable);
		
		
		
		
		// usage값이 null일 수도 있어서 left join 처리
		@Query("SELECT a, e, t, u, f " +
			       "FROM Approval a " +
			       "JOIN a.employee e " +
			       "JOIN a.approvalCategory c " +
			       "JOIN c.approvalForm f "+
			       "JOIN f.approvalBaseFormType t "+
			       "LEFT JOIN a.annualLeaveUsage u "+
			       "WHERE a.approvalNo = ?1")
		List<Object[]> selectByapprovalNo(Long approvalNo);
		
		
		Approval findByApprovalNo(Long approvalNo);
		
		@Modifying
		@Query(value="update Approval a set a.approvalStatus = ?1 where a.approvalNo = ?2")
		int updateStatus(int status,Long approvalNo);
}
