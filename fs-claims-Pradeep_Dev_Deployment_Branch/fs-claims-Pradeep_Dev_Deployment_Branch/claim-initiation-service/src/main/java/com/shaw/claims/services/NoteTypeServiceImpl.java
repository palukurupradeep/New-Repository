package com.shaw.claims.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shaw.claims.dto.NoteGroupDTO;
import com.shaw.claims.dto.NoteTypeDTO;
import com.shaw.claims.dto.TraceTypeDTO;
import com.shaw.claims.model.Lookup;
import com.shaw.claims.model.NoteType;
import com.shaw.claims.model.TraceType;
import com.shaw.claims.repo.LookupRepository;
import com.shaw.claims.repo.NoteTypeRepository;
import com.shaw.claims.repo.TraceTypeRepository;

@Service
public class NoteTypeServiceImpl implements NoteTypeService {

	@Autowired
	NoteTypeRepository noteTypeRepository;
	@Autowired
	LookupRepository lookupRepository;
	@Autowired
	TraceTypeRepository traceTypeRepository;
	
	@Override
	public List<NoteTypeDTO> fetchNoteType() {
		List<NoteType> noteTypeList = noteTypeRepository.findActiveNoteType();
		List<NoteTypeDTO> noteTypeDtoList=new ArrayList<>();
		
		for (NoteType noteType : noteTypeList) {
			NoteGroupDTO noteGroupDto = new NoteGroupDTO();
			NoteTypeDTO noteTypeDto = new NoteTypeDTO();
			Optional<Lookup> lookup = lookupRepository.findById(noteType.getLookup().getLookupId());
			if (lookup.isPresent()) {
				noteTypeDto.setLookupCode(lookup.get().getLookupCode());
				noteTypeDto.setLookupDescription(lookup.get().getLookupDescription());
				noteTypeDto.setLookupId(lookup.get().getLookupId());
			}

			noteTypeDto.setNoteTypeId(noteType.getNoteTypeId());
			noteTypeDto.setNoteTypeCode(noteType.getNoteTypeCode());
			noteTypeDto.setNoteTypeDescription(noteType.getNoteTypeDescription());
			BeanUtils.copyProperties(noteType.getNoteGroup(), noteGroupDto);
			noteTypeDto.setNoteGroupDTO(noteGroupDto);
			noteTypeDto.setStatusId(noteType.getStatusId());
			noteTypeDto.setIsManual(noteType.getIsManual());
			noteTypeDto.setCreatedByUserId(noteType.getCreatedByUserId());
			noteTypeDto.setModifiedByUserId(noteType.getModifiedByUserId());
			noteTypeDtoList.add(noteTypeDto);
	}
		
		return noteTypeDtoList;
	}

	@Override
	public List<TraceTypeDTO> fetchTraceType() {
	List<TraceType> traceTypeList=	traceTypeRepository.findAllTraceTypes();
	List<TraceTypeDTO> traceTypeDtoList=new ArrayList<>();
	for (TraceType traceType : traceTypeList) {
		TraceTypeDTO traceTypeDto=new TraceTypeDTO();
		traceTypeDto.setTraceTypeId(traceType.getTraceTypeId());
		traceTypeDto.setTraceTypeCode(traceType.getTraceTypeCode());
		traceTypeDto.setTraceTypeDescription(traceType.getTraceTypeDescription());
		traceTypeDto.setTraceTypeDays(traceType.getTraceTypeDays());
		traceTypeDto.setCreatedByUserId(traceType.getCreatedByUserId());
		traceTypeDto.setModifiedByUserId(traceType.getModifiedByUserId());
		traceTypeDtoList.add(traceTypeDto);
	}
		return traceTypeDtoList;
	}

}
