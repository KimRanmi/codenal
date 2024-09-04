package com.codenal.announce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codenal.announce.domain.Announce;

public interface AnnounceRepository extends JpaRepository<Announce, Integer>{

	@Query(value="SELECT * "
			+ "FROM announce"
			, nativeQuery = true)
	Announce findByAllAnnounceList();
	
	Announce findByAnnounceNo(Long announceNo);
}
