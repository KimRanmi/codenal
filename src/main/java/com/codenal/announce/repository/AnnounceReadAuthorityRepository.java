package com.codenal.announce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codenal.announce.domain.AnnounceReadAuthority;

public interface AnnounceReadAuthorityRepository extends JpaRepository<AnnounceReadAuthority, Long> {

//    @Query("SELECT ar FROM AnnounceReadAuthority ar WHERE ar.announce.announceNo = :announceNo") 
//    List<AnnounceReadAuthority> findByAnnounceNo(@Param("announceNo") Long announceNo);
}
