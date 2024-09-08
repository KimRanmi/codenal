package com.codenal.approval.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codenal.approval.domain.Approval;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
		
		@Query(value="SELECT a,f FROM Approval a "
				 + "JOIN a.employee e "
		         + "JOIN a.approvalCategory c "
		         + "JOIN c.approvalForm f ",
		         countQuery = "SELECT count(a) FROM Approval a "
		         +"JOIN a.employee e "
		         +"JOIN a.approvalCategory c "
		         +"JOIN c.approvalForm f ")
		Page<Object[]> findList(Pageable pageable);
	
		@Query("SELECT a, e, t " +
			       "FROM Approval a " +
			       "JOIN a.employee e " +
			       "JOIN a.approvalCategory c " +
			       "JOIN c.approvalForm f "+
			       "JOIN f.approvalBaseFormType t "+
			       "WHERE a.approvalNo = ?1")
		List<Object[]> selectByapprovalNo(Long approvalNo);


}
