package com.shaw.claims.repo;

import com.shaw.claims.model.NoteGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteGroupRepository extends JpaRepository<NoteGroup,Integer> {

    @Query("SELECT ng FROM NoteGroup ng WHERE ng.statusId = 1 AND ng.noteGroupCode != 'AUDT'")
    List<NoteGroup> findAllNoteGroup();
    @Query("SELECT ng FROM NoteGroup ng WHERE ng.noteGroupCode = :noteGroupCode AND ng.statusId = 1")
    NoteGroup findByNoteGroupCode(@Param("noteGroupCode") String noteGroupCode);
}
