package com.shaw.claims.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.shaw.claims.model.NoteTemplate;

@Repository
public interface NoteTemplateRepository extends JpaRepository<NoteTemplate, Integer>{

	@Query("select nt from NoteTemplate nt where nt.noteType.noteTypeId=:noteTypeId"
			+ " and nt.noteGroup.noteGroupId=:noteGroupId and nt.isDefault=:isDefault and nt.statusId=1")
	Optional<NoteTemplate> findNoteTemplateByNoteTypeAndNoteGroup(@Param("noteTypeId") Integer noteTypeId, @Param("noteGroupId") Integer noteGroupId, @Param("isDefault") Boolean isDefault);

	List<NoteTemplate> findByNoteType_NoteTypeIdAndNoteGroup_NoteGroupIdAndStatusId(int noteTypeId, int noteGroupId, int statusId);
	@Query("select nt from NoteTemplate nt where nt.statusId=1")
	List<NoteTemplate> findAllNoteTemplates();
	
	@Modifying
	@Transactional
	@Query("update NoteTemplate set isDefault=:isDefault where noteTemplateId=:noteTemplateId")
	void updateNoteTemplateForIsDefault(@Param("noteTemplateId") Integer noteTemplateId,@Param("isDefault") Boolean isDefault);

	NoteTemplate findByNoteTemplateName(@Param("templateName") String templateName);
}
