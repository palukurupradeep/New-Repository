package com.shaw.claims.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.dto.NoteAndTaskRequestDTO;
import com.shaw.claims.dto.SampleRequestDTO;
import com.shaw.claims.dto.SampleReviewDTO;
import com.shaw.claims.enums.StatusTypes;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.Claim;
import com.shaw.claims.model.ClaimNote;
import com.shaw.claims.model.ClaimReasonDefinition;
import com.shaw.claims.model.NoteTemplate;
import com.shaw.claims.model.TraceTask;
import com.shaw.claims.repo.ClaimReasonDefinitionRepository;
import com.shaw.claims.repo.ClaimRepository;
import com.shaw.claims.repo.LookupRepository;
import com.shaw.claims.repo.NoteTemplateRepository;
import com.shaw.claims.repo.TraceTypeRepository;
import com.shaw.claims.repo.WorkStatusRepository;

@Service
public class SampleReviewServiceImpl implements SampleReviewService {

	@Autowired
	private ClaimRepository claimRepository;
	@Autowired
	ClaimReasonDefinitionRepository claimReasonDefinitionRepository;
	@Autowired
	LookupRepository lookupRepository;
	@Autowired
	WorkStatusRepository workStatusRepository;
	@Autowired
	NoteTemplateRepository noteTemplateRepository;
	@Autowired
	TraceTypeRepository traceTypeRepository;
	
	
	@Override
	public void sampleRequest(SampleRequestDTO sampleRequestDTO) {
		Claim claim = findByClaimId(sampleRequestDTO.getClaimId());
		int workStatusId=workStatusRepository.findWorkStatusIdByWorkStatusCode("014");
		claim.setWorkStatusId(workStatusId);
		
		ClaimNote claimNote = new ClaimNote();
		NoteTemplate noteTemplate =null;
		 noteTemplate = noteTemplateRepository.findByNoteTemplateName(CommonConstant.SAMPLE_REQUESTED);
		if(noteTemplate==null)
		{
		 noteTemplate = noteTemplateRepository.findByNoteTemplateName("UNKN");
		}
		claimNote.setNoteType(noteTemplate.getNoteType());
		claimNote.setNoteGroup(noteTemplate.getNoteGroup());
		claimNote.setNoteTemplate(noteTemplate);
		claimNote.setClaim(claim);
		claimNote.setClaimNoteText(noteTemplate.getNoteTemplateText());
		claimNote.setLookup(lookupRepository.findByLookupCode("P"));
		claimNote.setStatusId(1);
		claimNote.setCreatedByUserId(sampleRequestDTO.getCreatedByUserId());
		claimNote.setModifiedByUserId(sampleRequestDTO.getCreatedByUserId());
		TraceTask traceTask = new TraceTask();
		NoteAndTaskRequestDTO noteAndTaskRequestDTO = new NoteAndTaskRequestDTO();
		noteAndTaskRequestDTO.setTraceTypeId(4);
		noteAndTaskRequestDTO.setClaimId(claim.getClaimId());
		noteAndTaskRequestDTO.setCreatedByUserId(sampleRequestDTO.getCreatedByUserId());
		noteAndTaskRequestDTO.setModifiedByUserId(sampleRequestDTO.getModifiedByUserId());
		setTraceTask(noteAndTaskRequestDTO, claimNote, traceTask);
		claim.getClaimNotes().add(claimNote);
		
		claim.setSampleRequested(CommonConstant.SAMPLE_REQUESTED_VALUE);
		claimRepository.save(claim);
		
	}

	@Override
	public ClaimReasonDefinition fetchSampleSizeRequirementAndTesting(String reasonCode) {
		ClaimReasonDefinition claimReasonDefinition=claimReasonDefinitionRepository.findByClaimReasonCode(reasonCode);
		if(claimReasonDefinition==null)
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),"Record not found with this reasonCode :: " + reasonCode);
		return claimReasonDefinition;
	}
	
/*	@Override
	public void updateSampleReviewByAction(SampleReviewDTO sampleReviewDTO) {
		Claim claim = findByClaimId(sampleReviewDTO.getClaimId());
		int workStatusId=workStatusRepository.findWorkStatusIdByWorkStatusCode("045");
		claim.setWorkStatusId(workStatusId);
		claim.setPriorityClaim(true);
		claimRepository.save(claim);
	}*/
	

	@Override
	public void updateSampleReviewByAction(SampleReviewDTO sampleReviewDTO) {
		Claim claim = findByClaimId(sampleReviewDTO.getClaimId());
		switch (sampleReviewDTO.getAction()) {
		case "Approved":
			claim.setPriorityClaim(true);
			setSampleReviewForApproveAndDeclinedAndSampleReceived(claim,sampleReviewDTO,"045",CommonConstant.SAMPLE_APPROVED, sampleReviewDTO.getNoteTemplateText());
			break;
			
		case "Declined":
			claim.setPriorityClaim(true);
			setSampleReviewForApproveAndDeclinedAndSampleReceived(claim,sampleReviewDTO,"045",CommonConstant.SAMPLE_DECLINED, sampleReviewDTO.getNoteTemplateText());
			break;
			
		case "SampleReceived":
			setSampleReviewForApproveAndDeclinedAndSampleReceived(claim,sampleReviewDTO,"043",CommonConstant.SAMPLE_RECEIVED, null);
			break;
		
		}
		
		
	}
	
	private void setSampleReviewForApproveAndDeclinedAndSampleReceived(Claim claim,SampleReviewDTO sampleReviewDTO,String workStatusCode,String templateName, String noteText)
	{
		int workStatusId=workStatusRepository.findWorkStatusIdByWorkStatusCode(workStatusCode);
		claim.setWorkStatusId(workStatusId);
		ClaimNote claimNote = new ClaimNote();
		setClaimNoteForSampleReview(claimNote,claim,sampleReviewDTO,templateName, noteText);
		claim.getClaimNotes().add(claimNote);
		claim.setSampleRequested(CommonConstant.SAMPLE_RECEIVED_VALUE);
		claimRepository.save(claim);
		
	}
	
	
	private void setClaimNoteForSampleReview(ClaimNote claimNote,Claim claim,SampleReviewDTO sampleReviewDTO,String templateName, String noteText)
	{
		NoteTemplate noteTemplate=null;
		 noteTemplate = noteTemplateRepository.findByNoteTemplateName(templateName);
		if(noteTemplate==null)
		{
			 noteTemplate = noteTemplateRepository.findByNoteTemplateName("UNKN");	
		}
		claimNote.setNoteType(noteTemplate.getNoteType());
		claimNote.setNoteGroup(noteTemplate.getNoteGroup());
		claimNote.setNoteTemplate(noteTemplate);
		claimNote.setClaim(claim);
		if(noteText != null) {
			claimNote.setClaimNoteText(noteText);
		} else {
			claimNote.setClaimNoteText(noteTemplate.getNoteTemplateText());
		}
		
		
		claimNote.setLookup(lookupRepository.findByLookupCode("P"));
		claimNote.setStatusId(1);
		claimNote.setCreatedByUserId(sampleReviewDTO.getCreatedByUserId());
		claimNote.setModifiedByUserId(sampleReviewDTO.getCreatedByUserId());
		
	}
	
	public void setTraceTask(NoteAndTaskRequestDTO noteAndTaskRequestDTO, ClaimNote claimNote, TraceTask traceTask) {
        if(noteAndTaskRequestDTO.getTraceTypeId() != 0) {
            traceTask.setClaimNote(claimNote);
            traceTask.setTraceType(traceTypeRepository.findById(noteAndTaskRequestDTO.getTraceTypeId()).get());
            traceTask.setClaim(claimNote.getClaim());
            traceTask.setStatusId(StatusTypes.ACTIVE.getStatusId());
            traceTask.setAssignedUserId(noteAndTaskRequestDTO.getCreatedByUserId());
            traceTask.setCreatedByUserId(noteAndTaskRequestDTO.getCreatedByUserId());
            traceTask.setModifiedByUserId(noteAndTaskRequestDTO.getModifiedByUserId());
            traceTask.setTraceDate(LocalDateTime.now().plusDays(traceTask.getTraceType().getTraceTypeDays()));
            traceTask.setTraceActionLink("");
            traceTask.setTraceReference("");
            claimNote.setTraceTask(traceTask);
        }
    }

	
	/*private Claim setSampleReviewForSendToLab(Claim claim,SampleReviewDTO sampleReviewDTO)
	{
		int workStatusId=workStatusRepository.findWorkStatusIdByWorkStatusCode("049");
		claim.setWorkStatusId(workStatusId);
		ClaimNote claimNote = new ClaimNote();
		setClaimNoteForSampleReview(claimNote,claim,sampleReviewDTO);
		TraceTask traceTask = new TraceTask();
		NoteAndTaskRequestDTO noteAndTaskRequestDTO = new NoteAndTaskRequestDTO();
		noteAndTaskRequestDTO.setTraceTypeId(23);
		noteAndTaskRequestDTO.setClaimId(claim.getClaimId());
		noteAndTaskRequestDTO.setCreatedByUserId(sampleReviewDTO.getCreatedByUserId());
		noteAndTaskRequestDTO.setModifiedByUserId(sampleReviewDTO.getModifiedByUserId());
		setTraceTask(noteAndTaskRequestDTO, claimNote, traceTask);
		claim.getClaimNotes().add(claimNote);
		claimRepository.save(claim);
		return claim;
	}
	
	private Claim setSampleReviewOfHeldforQualityReviewAndSendToPlant(Claim claim,SampleReviewDTO sampleReviewDTO)
	{
		int workStatusId=workStatusRepository.findWorkStatusIdByWorkStatusCode("015");
		claim.setWorkStatusId(workStatusId);
		ClaimNote claimNote = new ClaimNote();
		setClaimNoteForSampleReview(claimNote,claim,sampleReviewDTO);
		claim.getClaimNotes().add(claimNote);
		claimRepository.save(claim);
		return claim;
	}
	
	
	public void setTraceTask(NoteAndTaskRequestDTO noteAndTaskRequestDTO, ClaimNote claimNote, TraceTask traceTask) {
        if(noteAndTaskRequestDTO.getTraceTypeId() != 0) {
            traceTask.setClaimNote(claimNote);
            traceTask.setTraceType(traceTypeRepository.findById(noteAndTaskRequestDTO.getTraceTypeId()).get());
            traceTask.setClaim(claimNote.getClaim());
            traceTask.setStatusId(StatusTypes.ACTIVE.getStatusId());
            traceTask.setAssignedUserId(noteAndTaskRequestDTO.getCreatedByUserId());
            traceTask.setCreatedByUserId(noteAndTaskRequestDTO.getCreatedByUserId());
            traceTask.setModifiedByUserId(noteAndTaskRequestDTO.getModifiedByUserId());
            traceTask.setTraceDate(LocalDateTime.now().plusDays(traceTask.getTraceType().getTraceTypeDays()));
            traceTask.setTraceActionLink("");
            traceTask.setTraceReference("");
            claimNote.setTraceTask(traceTask);
        }
    }
	
	*/
	private Claim findByClaimId(Integer claimId){
		Optional<Claim> claim = claimRepository.findById(claimId);
		if (!claim.isPresent()) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found In This ClaimId :: " + claimId);
		}
		return claim.get();
	}

	
}
