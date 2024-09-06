package com.codenal.announce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codenal.announce.domain.AnnounceFile;

public interface AnnounceFileRepository extends JpaRepository<AnnounceFile, Long> {
	
//    @Query(value = "SELECT af.* FROM announce_file af JOIN announce a ON af.announce_no = a.announce_no WHERE a.announce_no = :announceNo", nativeQuery = true)
//	List<AnnounceFile> findByannounceNo(@Param("announceNo") Long announceNo);

	
//	@Query(value="SELECT af FROM AnnounceFile af "
//			+ "WHERE af.announce = ?1")
//	List<AnnounceFile> findByAnnounceNo(Long announce);
	
//	List<AnnounceFile> findByAnnounceNoId(Long announceNo);

    @Query("SELECT af FROM AnnounceFile af WHERE af.announce.announceNo = :announceNo")
    AnnounceFile findByAnnounceNo(@Param("announceNo") Long announceNo);
	
//	 @Query("SELECT af.fileNo, af.fileNewName, af.fileOriName, af.filePath, a.announceTitle " +
//			 "FROM AnnounceFile af JOIN af.announce a WHERE af.announceNo = :announceNo")
//	 List<Object[]> findFilesWithAnnounceTitleByAnnounceNo(@Param("announceNo") Long announceNo);
	 
}
