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
	
	// 카운트
	int countByEmployee_EmpId(Long empId);
	
	// 상신리스트
	@Query(value = "SELECT a, f, v FROM Approval a "
            + "JOIN a.employee e "
            + "JOIN a.approvalCategory c "
            + "JOIN c.approvalForm f "
            + "LEFT JOIN a.approver v "
            + "WHERE a.approvalStatus = ?1 AND e.empId = ?2 "
            + "AND (v.approvalDate = ( "
            + "      SELECT MAX(v2.approvalDate) "
            + "      FROM Approver v2 "
            + "      WHERE v2.approval.approvalNo = v.approval.approvalNo) "
            + "		or v.approvalDate IS NULL "
            + "  ) " // v가 null인 경우도 포함
            + "GROUP BY v.approval.approvalNo "
            + "ORDER BY v.approvalDate DESC",
            countQuery = "SELECT count(DISTINCT a) FROM Approval a "
                       + "JOIN a.employee e "
                       + "JOIN a.approvalCategory c "
                       + "JOIN c.approvalForm f "
                       + "LEFT JOIN a.approver v "
                       + "WHERE a.approvalStatus = ?1 AND e.empId = ?2 "
                       + "GROUP BY v.approval.approvalNo")
		Page<Object[]> findList(int num, Long id, Pageable pageable);


		
		// 수신리스트
		@Query(value = "SELECT a, f, v FROM Approval a "
				 + "JOIN a.approver v "
	             + "JOIN a.employee e "
	             + "JOIN a.approvalCategory c "
	             + "JOIN c.approvalForm f "
	             + "WHERE v.approvalStatus = ?1 AND v.employee.empId = ?2 AND a.approvalStatus != 4",
	       countQuery = "SELECT count(distinct a) FROM Approval a "
	    		   	  + "JOIN a.approver v "
	                  + "JOIN a.employee e "
	                  + "JOIN a.approvalCategory c "
	                  + "JOIN c.approvalForm f "
	                  + "WHERE v.approvalStatus = ?1 AND v.employee.empId = ?2 AND a.approvalStatus != 4")
		Page<Object[]> findinboxList(int status, Long empId, Pageable pageable);


		// 수신참조 리스트
		@Query(value = "SELECT a, f, r FROM Approval a "
	             + "JOIN a.employee e "
	             + "JOIN a.approvalCategory c "
	             + "JOIN c.approvalForm f "
	             + "JOIN a.referrer r "
	             + "WHERE r.employee.empId = ?1",
	       countQuery = "SELECT count(DISTINCT a) FROM Approval a "
	                  + "JOIN a.employee e "
	                  + "JOIN a.approvalCategory c "
	                  + "JOIN c.approvalForm f "
	                  + "JOIN a.referrer r "
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
		
		// 상태 수정
		@Modifying
		@Query(value="update Approval a set a.approvalStatus = ?1 where a.approvalNo = ?2")
		int updateStatus(int status,Long approvalNo);
		
}
