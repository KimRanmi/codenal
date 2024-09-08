package com.codenal.announce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codenal.announce.domain.AnnounceFile;

public interface AnnounceFileRepository extends JpaRepository<AnnounceFile, Long> {
	

//    @Query("SELECT af FROM AnnounceFile af WHERE af.announce.announceNo = :announceNo")
//    List<AnnounceFile> findByAnnounceNo(@Param("announceNo") Long announceNo);

}
