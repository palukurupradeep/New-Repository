package com.shaw.claims.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.NoteGroup;
import com.shaw.claims.model.NoteType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.dto.ClaimNoteDTO;
import com.shaw.claims.dto.NoteAndTaskRequestDTO;
import com.shaw.claims.dto.NoteGroupDTO;
import com.shaw.claims.dto.NoteTemplateDTO;
import com.shaw.claims.dto.NoteTypeDTO;
import com.shaw.claims.dto.PendingNotesAndTaskRequestDTO;
import com.shaw.claims.dto.TraceTaskDTO;
import com.shaw.claims.dto.TraceTypeDTO;
import com.shaw.claims.dto.UsersDTO;
import com.shaw.claims.enums.StatusTypes;
import com.shaw.claims.model.Claim;
import com.shaw.claims.model.ClaimNote;
import com.shaw.claims.model.TraceTask;
import com.shaw.claims.model.TraceType;
import com.shaw.claims.repo.ClaimNoteRepository;
import com.shaw.claims.repo.ClaimRepository;
import com.shaw.claims.repo.LookupRepository;
import com.shaw.claims.repo.NoteGroupRepository;
import com.shaw.claims.repo.NoteTemplateRepository;
import com.shaw.claims.repo.NoteTypeRepository;
import com.shaw.claims.repo.StatusRepository;
import com.shaw.claims.repo.TraceTaskRepository;
import com.shaw.claims.repo.TraceTypeRepository;
import org.springframework.web.client.RestTemplate;

@Service
public class ClaimNotesAndTaskServiceImpl implements ClaimNotesAndTaskService{

    Logger log = LogManager.getLogger(ClaimNotesAndTaskServiceImpl.class);

    @Autowired
    private NoteTypeRepository noteTypeRepository;
    @Autowired
    private NoteGroupRepository noteGroupRepository;
    @Autowired
    private NoteTemplateRepository noteTemplateRepository;
    @Autowired
    private TraceTypeRepository traceTypeRepository;
    @Autowired
    private ClaimNoteRepository claimNoteRepository;
    @Autowired
    private ClaimRepository claimRepository;
    @Autowired
    private LookupRepository lookupRepository;
    @Autowired
    TraceTaskRepository traceTaskRepository;
    @Autowired
    WorkQueueServiceImpl workQueueServiceImpl;
    @Autowired
    private RestTemplate restTemplate;
    @Value(("${claim-security.rest.url}"))
    private String restSecurityUrl;
    @Autowired
    StatusRepository statusRepository;
    @Override
    public void notesAndTask(NoteAndTaskRequestDTO noteAndTaskRequestDTO) {
        ClaimNote claimNote = new ClaimNote();
        TraceTask traceTask = new TraceTask();
        BeanUtils.copyProperties(noteAndTaskRequestDTO,claimNote);
        if(noteAndTaskRequestDTO.getTraceDate() != null) {
        	 claimNote.setNoteType(noteTypeRepository.findById(6).get());
        } else if(noteAndTaskRequestDTO.getNoteTypeId() != null){
        	claimNote.setNoteType(noteTypeRepository.findById(noteAndTaskRequestDTO.getNoteTypeId()).get());
        } else {
        	claimNote.setNoteType(noteTypeRepository.findById(1).get());
        }
       
        claimNote.setNoteGroup(noteGroupRepository.findById(noteAndTaskRequestDTO.getNoteGroupId()).get());
        if(noteAndTaskRequestDTO.getNoteTemplateId() == 0){
            claimNote.setNoteTemplate(noteTemplateRepository.findById(1).get());
            claimNote.setClaimNoteText("");
        }else {
            claimNote.setNoteTemplate(noteTemplateRepository.findById(noteAndTaskRequestDTO.getNoteTemplateId()).get());
        }
        claimNote.setClaim(claimRepository.findByClaimId(noteAndTaskRequestDTO.getClaimId()));
        claimNote.setLookup(lookupRepository.findByLookupCode(noteAndTaskRequestDTO.getLookupCode()));
        claimNote.setStatusId(StatusTypes.ACTIVE.getStatusId());
        setTraceTask(noteAndTaskRequestDTO, claimNote, traceTask);
        getCreatedUserIdFromOldNotesAndTask(noteAndTaskRequestDTO, claimNote, traceTask);
        claimNoteRepository.save(claimNote);
    }

    private void getCreatedUserIdFromOldNotesAndTask(NoteAndTaskRequestDTO noteAndTaskRequestDTO, ClaimNote claimNote, TraceTask traceTask) {
        if(noteAndTaskRequestDTO.getClaimNoteId() != 0){
            ClaimNote oldClaimNote = claimNoteRepository.findById(noteAndTaskRequestDTO.getClaimNoteId()).get();
            claimNote.setCreatedByUserId(oldClaimNote.getCreatedByUserId());
            claimNote.setCreatedDateTime(oldClaimNote.getCreatedDateTime());
            TraceTask oldTask = traceTaskRepository.findById(noteAndTaskRequestDTO.getTraceTaskId()).get();
            traceTask.setCreatedByUserId(oldTask.getCreatedByUserId());
            traceTask.setCreatedDateTime(oldTask.getCreatedDateTime());
            claimNote.setTraceTask(traceTask);
        }
    }

    public void setTraceTask(NoteAndTaskRequestDTO noteAndTaskRequestDTO, ClaimNote claimNote, TraceTask traceTask) {
      //  if(noteAndTaskRequestDTO.getTraceTypeId() != null) {
            BeanUtils.copyProperties(noteAndTaskRequestDTO, traceTask);
            traceTask.setClaimNote(claimNote);
            if(noteAndTaskRequestDTO.getTraceDate() != null) {
            	   traceTask.setTraceType(traceTypeRepository.findById(5).get());
            	   traceTask.setTraceDate(noteAndTaskRequestDTO.getTraceDate());
            } else {
            	 traceTask.setTraceType(traceTypeRepository.findById(1).get());
            	traceTask.setTraceDate(LocalDateTime.now().plusDays(traceTypeRepository.findById(1).get().getTraceTypeDays()));
            }
         
            traceTask.setClaim(claimNote.getClaim());
            traceTask.setStatusId(StatusTypes.ACTIVE.getStatusId());
            traceTask.setAssignedUserId(noteAndTaskRequestDTO.getCreatedByUserId());
            claimNote.setTraceTask(traceTask);
        //}
    }

    @Override
    public void pendNotesAndTask(PendingNotesAndTaskRequestDTO pendingNotesAndTaskRequestDTO) {
        ClaimNote claimNote = new ClaimNote();
        TraceTask traceTask = new TraceTask();
        BeanUtils.copyProperties(pendingNotesAndTaskRequestDTO,claimNote);
        claimNote.setNoteType(noteTypeRepository.findById(pendingNotesAndTaskRequestDTO.getNoteTypeId()).get());
        claimNote.setNoteGroup(noteGroupRepository.findById(pendingNotesAndTaskRequestDTO.getNoteGroupId()).get());
        claimNote.setClaim(claimRepository.findByClaimId(pendingNotesAndTaskRequestDTO.getClaimId()));
        if(pendingNotesAndTaskRequestDTO.getNoteTemplateId() == 0){
            claimNote.setNoteTemplate(noteTemplateRepository.findById(1).get());
            claimNote.setClaimNoteText("");
        }else {
            claimNote.setNoteTemplate(noteTemplateRepository.findById(pendingNotesAndTaskRequestDTO.getNoteTemplateId()).get());
        }
        claimNote.setLookup(lookupRepository.findByLookupCode(pendingNotesAndTaskRequestDTO.getLookupCode()));
        claimNote.setStatusId(StatusTypes.PENDING.getStatusId());
        BeanUtils.copyProperties(pendingNotesAndTaskRequestDTO,traceTask);
        traceTask.setClaimNote(claimNote);
        traceTask.setTraceType(traceTypeRepository.findByTraceTypeCode("036"));
        traceTask.setClaim(claimNote.getClaim());
        traceTask.setTraceDate(LocalDateTime.now().plusDays(traceTask.getTraceType().getTraceTypeDays()));
        traceTask.setStatusId(StatusTypes.PENDING.getStatusId());
        traceTask.setTraceReference("");
        traceTask.setTraceActionLink("");
        traceTask.setAssignedUserId(pendingNotesAndTaskRequestDTO.getCreatedByUserId());
        claimNote.setTraceTask(traceTask);
        claimNoteRepository.save(claimNote);
    }

	@Override
	public List<ClaimNoteDTO> fetchClaimNotes(String claimId) {
		return fetchAllClaimNotes(claimId);
	}

    @Override
    public String fetchClaimNoteText(String claimNoteId) {
        return claimNoteRepository.fetchClaimNoteText(claimNoteId);
    }

    @Override
    public List<NoteGroupDTO> noteGroup() {
        List<NoteGroup> noteGroups = noteGroupRepository.findAllNoteGroup();
        return noteGroups.stream().map(ClaimNotesAndTaskServiceImpl::mapNoteGroupDTO).collect(Collectors.toList());
    }

    @Override
    public List<String> userNamesForClaimNotes(String claimId) {
        List<Integer> userIdList = claimNoteRepository.getDistinctUsers(claimId);
        String userIds = userIdList.stream().map(Object::toString).collect(Collectors.joining(","));
        return findUserNames(userIds);
    }

    public List<String> findUserNames(String userIds) {
        String url = "/users/v1/findUserNames?userIds=" + userIds;
        String finalUrl = restSecurityUrl + url;
        log.info("Final Security Url :: " + finalUrl);
        ResponseEntity<List<String>> responseEntity = restTemplate.exchange(
                finalUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<String>>() {});
        List<String> response = responseEntity.getBody();
        if (response == null) {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Record Not Found with userIds :: " + userIds);
        }
        return response;
    }

    private static NoteGroupDTO mapNoteGroupDTO(NoteGroup noteGroup) {
        NoteGroupDTO noteGroupDTO = new NoteGroupDTO();
        BeanUtils.copyProperties(noteGroup, noteGroupDTO);
        return noteGroupDTO;
    }

    private List<ClaimNoteDTO> fetchAllClaimNotes(String claimId) {
		List<ClaimNote> claimNoteList = claimNoteRepository.findByClaim_ClaimId(claimId);
		return claimNoteList.stream().map(data -> {
			ClaimNoteDTO claimNote = new ClaimNoteDTO();

			//claimNote.setClaimNoteText(data.getClaimNoteTextSubStr());
			claimNote.setClaimNoteText(data.getClaimNoteText());
			claimNote.setCallLog(data.isCallLog());
			claimNote.setConfidential(data.getLookup().getLookupCode());
			claimNote.setClaimNoteId(data.getClaimNoteId());
			claimNote.setStatusCode(statusRepository.findByStatusId(data.getStatusId()).getStatusCode());
			
			if(data.getNoteTemplate() != null) {
				NoteTemplateDTO noteTemplateDTO = new NoteTemplateDTO();
				noteTemplateDTO.setNoteTemplateId(data.getNoteTemplate().getNoteTemplateId());
				noteTemplateDTO.setNoteTemplateName(data.getNoteTemplate().getNoteTemplateName());
				claimNote.setNoteTemplate(noteTemplateDTO);
			}
			
			if(data.getNoteType() != null) {
				NoteTypeDTO noteTypeDTO = new NoteTypeDTO();
				noteTypeDTO.setNoteTypeId(data.getNoteType().getNoteTypeId());
				noteTypeDTO.setNoteTypeCode(data.getNoteType().getNoteTypeCode());
				noteTypeDTO.setNoteTypeDescription(data.getNoteType().getNoteTypeDescription());
				claimNote.setNoteType(noteTypeDTO);
			}

			if(data.getNoteGroup() != null) {
				NoteGroupDTO noteGroupDTO = new NoteGroupDTO();
				noteGroupDTO.setNoteGroupId(data.getNoteGroup().getNoteGroupId());
				noteGroupDTO.setNoteGroupCode(data.getNoteGroup().getNoteGroupCode());
				noteGroupDTO.setNoteGroupDescription(data.getNoteGroup().getNoteGroupDescription());
				claimNote.setNoteGroup(noteGroupDTO);
			}
			
			UsersDTO userDTO = workQueueServiceImpl.getUsersDetailsByUserId(data.getCreatedByUserId());
			
			if(userDTO != null) {
				claimNote.setCreatedUserName(((!userDTO.getFirstName().isEmpty()) ?  (userDTO.getFirstName() +" ") : "") + userDTO.getLastName());
				claimNote.setCreatedByUserId(data.getCreatedByUserId());
				claimNote.setCreatedUserTitle(userDTO.getTitle());
			}

			claimNote.setLabel(CommonConstant.NOTES);
			claimNote.setCreatedDate(data.getCreatedDateTime());
				if(data.getTraceTask() != null) {
					TraceTaskDTO traceTaskDTO = new TraceTaskDTO();
					traceTaskDTO.setTraceTaskType(data.getTraceTask().getTraceType().getTraceTypeDescription());
					traceTaskDTO.setTraceTaskId(data.getTraceTask().getTraceTaskId());
					traceTaskDTO.setAssignedUserId(data.getTraceTask().getAssignedUserId());
					traceTaskDTO.setCreatedByUserId(data.getTraceTask().getCreatedByUserId());
					traceTaskDTO.setTraceDate(data.getTraceTask().getTraceDate());
					UsersDTO assignedUserDTO =	workQueueServiceImpl.getUsersDetailsByUserId(data.getTraceTask().getAssignedUserId());
					if(assignedUserDTO != null) {
						traceTaskDTO.setAssignedUserName(((!assignedUserDTO.getFirstName().isEmpty()) ?  (assignedUserDTO.getFirstName() +" ") : "")+ assignedUserDTO.getLastName());
					}
					
					UsersDTO createdUserDTO =	workQueueServiceImpl.getUsersDetailsByUserId(data.getTraceTask().getCreatedByUserId());
					if(createdUserDTO != null) {
						traceTaskDTO.setCreatedUserName((!createdUserDTO.getFirstName().isEmpty() ?  (createdUserDTO.getFirstName() +" ") : "") + createdUserDTO.getLastName());
					}
					
					if(data.getTraceTask().getTraceType() != null) {
						TraceTypeDTO traceType = new TraceTypeDTO();
						traceType.setTraceTypeDays(data.getTraceTask().getTraceType().getTraceTypeDays());
						traceType.setTraceTypeId(data.getTraceTask().getTraceType().getTraceTypeId());
						traceTaskDTO.setTraceType(traceType);
					}
					
					traceTaskDTO.setLabel(CommonConstant.TASKS);
					claimNote.setTraceTask(traceTaskDTO);
				}
			return claimNote;
		}).collect(Collectors.toList());
	}

	@Override
	public void deleteNotesAndTask(String claimNoteId) {
		ClaimNote claimNote = claimNoteRepository.findByClaimNoteId(claimNoteId);
		if (claimNote != null) {
			if (claimNote.getTraceTask() != null) {
				claimNote.getTraceTask().setStatusId(3);
				traceTaskRepository.save(claimNote.getTraceTask());
			}
			claimNote.setStatusId(3);
			claimNoteRepository.save(claimNote);
		}
	}
}
