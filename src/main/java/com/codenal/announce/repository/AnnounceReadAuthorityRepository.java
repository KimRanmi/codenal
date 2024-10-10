package com.codenal.announce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codenal.announce.domain.AnnounceReadAuthority;

public interface AnnounceReadAuthorityRepository extends JpaRepository<AnnounceReadAuthority, Integer> {

    // Announce 엔티티와 연결된 모든 AnnounceReadAuthority 삭제
	void deleteByAnnounce_AnnounceNo(int no);

	List<AnnounceReadAuthority> findByAnnounce_AnnounceNo(int announceNo);


//    @Query("SELECT ar FROM AnnounceReadAuthority ar WHERE ar.announce.announceNo = :announceNo") 
//    List<AnnounceReadAuthority> findByAnnounceNo(@Param("announceNo") Long announceNo);
}
