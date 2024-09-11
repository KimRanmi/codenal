package com.codenal.approval.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codenal.approval.domain.Approval;
import com.codenal.approval.domain.ApprovalCategory;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
		
		@Query(value="SELECT a,f FROM Approval a "
				 + "JOIN a.employee e "
		         + "JOIN a.approvalCategory c "
		         + "JOIN c.approvalForm f "
		         + "where a.approvalStatus = ?1 ",
		         countQuery = "SELECT count(DISTINCT a) FROM Approval a "
		         +"JOIN a.employee e "
		         +"JOIN a.approvalCategory c "
		         +"JOIN c.approvalForm f "
		         + "where a.approvalStatus = ?1")
		Page<Object[]> findList(int num,Pageable pageable);
		
		
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
}
