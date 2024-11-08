package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.NoteType;

@Repository
public interface NoteTypeRepository extends JpaRepository<NoteType, Integer>{

	@Query("select nt from NoteType nt where nt.statusId=1 and nt.isManual=true")
	List<NoteType> findNoteType();
	
	@Query("select nt from NoteType nt where nt.statusId=1")
	List<NoteType> findActiveNoteType();
	
	@Query("select nt from NoteType nt where nt.noteTypeCode=:noteTypeCode and nt.statusId=1")
	NoteType fetchNoteTypeByNoteTypeCode(@Param("noteTypeCode") String noteTypeCode);
}
