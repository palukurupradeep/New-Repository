package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.dto.NoteTypeDTO;
import com.shaw.claims.dto.TraceTypeDTO;

public interface NoteTypeService {
	
	public List<NoteTypeDTO> fetchNoteType();

	public List<TraceTypeDTO> fetchTraceType();

}
