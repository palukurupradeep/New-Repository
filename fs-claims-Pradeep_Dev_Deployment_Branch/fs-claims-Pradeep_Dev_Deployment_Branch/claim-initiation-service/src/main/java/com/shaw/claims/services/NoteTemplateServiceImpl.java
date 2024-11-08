package com.shaw.claims.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.shaw.claims.dto.NoteTemplateDTO;
import com.shaw.claims.enums.StatusTypes;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.NoteGroup;
import com.shaw.claims.model.NoteTemplate;
import com.shaw.claims.model.NoteType;
import com.shaw.claims.repo.NoteTemplateRepository;
import com.shaw.claims.repo.NoteTypeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NoteTemplateServiceImpl implements NoteTemplateService{
	
	@Autowired
	NoteTemplateRepository noteTemplateRepository;
	@Autowired
	NoteTypeRepository noteTypeRepository;

	@Override
	public void saveNoteTemplate(NoteTemplateDTO noteTemplateDTO) {

		validateTemplateTexts(noteTemplateDTO);

		NoteTemplate noteTemplate = new NoteTemplate();
		if (noteTemplateDTO.getNoteTemplateId() != 0) {
			noteTemplate.setNoteTemplateId(noteTemplateDTO.getNoteTemplateId());
		}
		
		Optional<NoteTemplate> noteTemplateData = noteTemplateRepository.findNoteTemplateByNoteTypeAndNoteGroup(
				noteTemplateDTO.getNoteTypeId(), noteTemplateDTO.getNoteGroupId(), true);
		if (noteTemplateData.isPresent() && noteTemplateDTO.getIsDefault()==true) {

			noteTemplateRepository.updateNoteTemplateForIsDefault(noteTemplateData.get().getNoteTemplateId(), false);
		}

		NoteGroup noteGroup = new NoteGroup();
		NoteType noteType = new NoteType();
		noteType.setNoteTypeId(noteTemplateDTO.getNoteTypeId());
		noteGroup.setNoteGroupId(noteTemplateDTO.getNoteGroupId());
		noteTemplate.setNoteTemplateName(noteTemplateDTO.getNoteTemplateName());
		noteTemplate.setNoteTemplateText(Optional.ofNullable(noteTemplateDTO.getNoteTemplateText()).orElse(""));
		noteTemplate.setAuditHistoryTemplateText(Optional.ofNullable(noteTemplateDTO.getAuditHistoryTemplateText()).orElse(""));
		noteTemplate.setIsManual(noteTemplateDTO.getIsManual());
		noteTemplate.setNoteGroup(noteGroup);
		noteTemplate.setNoteType(noteType);
		noteTemplate.setDefault(noteTemplateDTO.getIsDefault());
		noteTemplate.setEditable(noteTemplateDTO.getEditable());
		noteTemplate.setStatusId(StatusTypes.ACTIVE.getStatusId());
		noteTemplate.setCreatedByUserId(noteTemplateDTO.getCreatedByUserId());
		noteTemplate.setModifiedByUserId(noteTemplateDTO.getModifiedByUserId());
		noteTemplateRepository.save(noteTemplate);
	}

	private static void validateTemplateTexts(NoteTemplateDTO noteTemplateDTO) {
		if (Optional.ofNullable(noteTemplateDTO.getNoteTemplateText()).orElse("").isEmpty()
				&& Optional.ofNullable(noteTemplateDTO.getAuditHistoryTemplateText()).orElse("").isEmpty()) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Note Template Text or AuditHistoryTemplate Text is Required");
		}
	}

	@Override
	public List<NoteTemplateDTO> fetchNoteTemplateByNoteTypeAndNoteGroup(int noteTypeId, int noteGroupId) {
		log.info("Inside Note Template ServiceImpl noteTypeId ::  , noteGroupId:: {},{}", noteTypeId, noteGroupId);

		List<NoteTemplate> noteTemplateList = noteTemplateRepository
				.findByNoteType_NoteTypeIdAndNoteGroup_NoteGroupIdAndStatusId(noteTypeId, noteGroupId, 1);
		List<NoteTemplateDTO> noteTemplateDTOs = new ArrayList<>();
		if (noteTemplateList != null) {
			noteTemplateDTOs = noteTemplateList.stream().map(data -> {
				NoteTemplateDTO noteTemplateDTO = new NoteTemplateDTO();
				BeanUtils.copyProperties(data, noteTemplateDTO);
				noteTemplateDTO.setNoteGroupId(data.getNoteGroup().getNoteGroupId());
				noteTemplateDTO.setNoteGroupName(data.getNoteGroup().getNoteGroupDescription());
				noteTemplateDTO.setNoteTypeId(data.getNoteType().getNoteTypeId());
				noteTemplateDTO.setNoteTypeName(data.getNoteType().getNoteTypeDescription());
				noteTemplateDTO.setIsDefault(data.isDefault());
				return noteTemplateDTO;
			}).collect(Collectors.toList());
		} else {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND),
					"Record not found with noteTypeId :: " + noteTypeId + " and noteGroupId :: " + noteGroupId);
		}
		return noteTemplateDTOs;
	}

	@Override
	public List<NoteTemplateDTO> fetchAllNoteTemplates() {
		List<NoteTemplate> noteTemplateList = noteTemplateRepository.findAllNoteTemplates();
		List<NoteTemplateDTO> noteTemplateDTOs = new ArrayList<>();
		if (noteTemplateList != null) {
			noteTemplateDTOs = noteTemplateList.stream().map(data -> {
				NoteTemplateDTO noteTemplateDTO = new NoteTemplateDTO();
				BeanUtils.copyProperties(data, noteTemplateDTO);
				noteTemplateDTO.setNoteGroupId(data.getNoteGroup().getNoteGroupId());
				noteTemplateDTO.setNoteGroupName(data.getNoteGroup().getNoteGroupDescription());
				noteTemplateDTO.setNoteTypeId(data.getNoteType().getNoteTypeId());
				noteTemplateDTO.setNoteTypeName(data.getNoteType().getNoteTypeDescription());
				noteTemplateDTO.setIsDefault(data.isDefault());
				noteTemplateDTO.setEditable(data.isEditable());
				return noteTemplateDTO;
			}).collect(Collectors.toList());
		}
		return noteTemplateDTOs;
	}

	@Override
	public void deleteNoteTemplate(Integer noteTemplateId) {
	Optional<NoteTemplate> noteTemplate=noteTemplateRepository.findById(noteTemplateId);
	if(noteTemplate.isPresent())
	{
		noteTemplate.get().setStatusId(StatusTypes.DELETE.getStatusId());
		noteTemplateRepository.save(noteTemplate.get());
	}
	else
	{
		throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND),
				"Record not found with noteTemplateId :: "+noteTemplateId);
	}
		
	}

	@Override
	public List<NoteTemplateDTO> fetchNoteTemplateBynoteTypeCode(String noteTypeCode) {
	NoteType noteType=noteTypeRepository.fetchNoteTypeByNoteTypeCode(noteTypeCode);
	List<NoteTemplateDTO> noteTemplateDTOList = null;
	if(noteType!=null)
	{
		List<NoteTemplate> noteTemplatesList=noteTemplateRepository.findByNoteType_NoteTypeIdAndNoteGroup_NoteGroupIdAndStatusId(noteType.getNoteTypeId(), noteType.getNoteGroup().getNoteGroupId(),1);
		noteTemplateDTOList = noteTemplatesList.stream().map(data -> {
			NoteTemplateDTO noteTemplateDTO = new NoteTemplateDTO();
			BeanUtils.copyProperties(data, noteTemplateDTO);
			return noteTemplateDTO;
		}).collect(Collectors.toList());
		
		
	} else {
		throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND),
				"Record not found with noteTypeCode :: "+noteTypeCode);
	}
		return noteTemplateDTOList;
	}

}
