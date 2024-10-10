package com.codenal.announce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.announce.domain.Announce;
import com.codenal.announce.domain.AnnounceFile;

public interface AnnounceFileRepository extends JpaRepository<AnnounceFile, Integer> {
	

//    @Query("SELECT af FROM AnnounceFile af WHERE af.announce.announceNo = :announceNo")
//    List<AnnounceFile> findByAnnounceNo(@Param("announceNo") Long announceNo);

	AnnounceFile findByAnnounceAnnounceNo(int announceNo);

	void deleteByAnnounce_AnnounceNo(int announceNo);

	List<AnnounceFile> findByAnnounce_AnnounceNo(int announceNo);

	void deleteById(int announceNo);
}
