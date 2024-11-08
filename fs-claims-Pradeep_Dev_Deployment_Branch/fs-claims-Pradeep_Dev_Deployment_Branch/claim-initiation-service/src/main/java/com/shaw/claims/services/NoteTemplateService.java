package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.dto.NoteTemplateDTO;

public interface NoteTemplateService {

	public void saveNoteTemplate(NoteTemplateDTO noteTemplateDTO);
	List<NoteTemplateDTO> fetchNoteTemplateByNoteTypeAndNoteGroup(int noteTypeId, int noteGroupId);
	public List<NoteTemplateDTO> fetchAllNoteTemplates();
	public void deleteNoteTemplate(Integer noteTemplateId);
	public List<NoteTemplateDTO> fetchNoteTemplateBynoteTypeCode(String noteTypeCode);
}
