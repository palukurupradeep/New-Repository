package com.shaw.claims.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.shaw.claims.dto.ClaimCrmHeaderDTO;
import com.shaw.claims.dto.ClaimCrmLineDTO;
import com.shaw.claims.dto.ClaimDispositionHeaderDTO;
import com.shaw.claims.dto.ClaimDispositionLineDTO;
import com.shaw.claims.enums.StatusTypes;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.Claim;
import com.shaw.claims.model.ClaimCrmHeader;
import com.shaw.claims.model.ClaimCrmLine;
import com.shaw.claims.model.ClaimDispositionHeader;
import com.shaw.claims.model.ClaimDispositionLine;
import com.shaw.claims.model.ClaimDocument;
import com.shaw.claims.model.ClaimLineDetail;
import com.shaw.claims.model.ClaimNote;
import com.shaw.claims.model.NoteTemplate;
import com.shaw.claims.model.NoteType;
import com.shaw.claims.repo.ClaimDispositionHeaderRepository;
import com.shaw.claims.repo.ClaimDispositionLineRepository;
import com.shaw.claims.repo.ClaimDocumentRepository;
import com.shaw.claims.repo.ClaimNoteRepository;
import com.shaw.claims.repo.ClaimReasonDefinitionRepository;
import com.shaw.claims.repo.ClaimRepository;
import com.shaw.claims.repo.DispositionStatusRepository;
import com.shaw.claims.repo.DispositionTypeRepository;
import com.shaw.claims.repo.DocumentTypeRepository;
import com.shaw.claims.repo.LookupRepository;
import com.shaw.claims.repo.NoteTemplateRepository;
import com.shaw.claims.repo.NoteTypeRepository;
import com.shaw.claims.repo.UnitOfMeasureRepository;

import jakarta.transaction.Transactional;

@Service
public class DispositionServiceImpl implements DispositionService {
	
	@Autowired
	DispositionTypeRepository dispositionTypeRepository;
	@Autowired
	ClaimDispositionHeaderRepository claimDispositionHeaderRepository;
	@Autowired
	ClaimDispositionLineRepository claimDispositionLineRepository;
	@Autowired
	DispositionStatusRepository dispositionStatusRepository;
	@Autowired
	private DocumentTypeRepository documentTypeRepository;
	@Autowired
	ClaimDocumentRepository claimDocumentRepository;
	@Autowired
	private ClaimRepository claimRepository;
	
	@Autowired
	ClaimReasonDefinitionRepository claimReasonDefinitionRepository;
	@Autowired
	ClaimNoteRepository claimNoteRepository;
	@Autowired
	NoteTypeRepository noteTypeRepository;
	@Autowired
	NoteTemplateRepository noteTemplateRepository;
	@Autowired
	LookupRepository lookupRepository;
	@Autowired
	UnitOfMeasureRepository unitOfMeasureRepository;
	
	
	@Override
	@Transactional
	public void dispositionHoldOrIssue(ClaimDispositionHeaderDTO claimDispositionHeaderDTO) {
	
		Optional<ClaimDispositionHeader> claimDispositionHeaderData = claimDispositionHeaderRepository
				.findById(claimDispositionHeaderDTO.getClaimDispositionHeaderId());
		ClaimDispositionHeader claimDispositionHeader = new ClaimDispositionHeader();
		Claim claim = findByClaimId(claimDispositionHeaderDTO.getClaimId());
		BeanUtils.copyProperties(claimDispositionHeaderDTO, claimDispositionHeader);
		if (claimDispositionHeaderData.isPresent()) {
			
			claimDispositionHeader = claimDispositionHeaderData.get();
			
		}
		else
		{
			 String dispositionNumber = String.format("%07d", claimDispositionHeaderRepository.getNextDispositionNumber());
			
			 claimDispositionHeader.setDispositionNumber(dispositionNumber);
			 ClaimDocument claimDocumentData =setDispositionClaimDocument(claim,claimDispositionHeaderDTO,claimDispositionHeader.getDispositionNumber());
			 claimDocumentRepository.save(claimDocumentData);
			 ClaimDocument claimDocument= claimDocumentRepository.findClaimDocumentIdByDocumentNumberAndClaimId(claimDispositionHeaderDTO.getDocumentNumber(), claimDispositionHeaderDTO.getClaimId());
			 claimDispositionHeader.setClaimDocument(claimDocument);
		}

		
		claimDispositionHeader.setClaim(claim);
		claimDispositionHeader.setCustomerNumber(claimDispositionHeaderDTO.getCustomerNumber());
		claimDispositionHeader.setDispositionType(dispositionTypeRepository.findByDispositionTypeDescription(claimDispositionHeaderDTO.getDispositionType()));
		claimDispositionHeader.setIssuedDate(LocalDateTime.now());
		claimDispositionHeader.setDispositionStatus(
		dispositionStatusRepository.findByDispositionStatusCode(claimDispositionHeaderDTO.getDispositionStatus()));
		 List<ClaimDispositionLine> dispositionLinesListData = getClaimDispositionLines(claim, claimDispositionHeaderDTO.getClaimDispositionLineDTO(),
				 claimDispositionHeader);
		 claimDispositionHeader.setClaimDispositionLine(dispositionLinesListData);
	      ClaimNote claimNote = getClaimNote(claim, claimDispositionHeaderDTO.getNoteTemplateId());
	  
	   ClaimNote claimNoteData= claimNoteRepository.save(claimNote);
	   claimDispositionHeader.setNoteTemplate(claimNote.getNoteTemplate());
	   claimDispositionHeader.setClaimNote(claimNoteData);
	   claimDispositionHeaderRepository.save(claimDispositionHeader);
	}
	
	
	private Claim findByClaimId(Integer claimId){
		Optional<Claim> claim = claimRepository.findById(claimId);
		if (!claim.isPresent()) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found In This ClaimId :: " + claimId);
		}
		return claim.get();
	}
	 private ClaimDocument setDispositionClaimDocument(Claim claim, ClaimDispositionHeaderDTO claimDispositionHeaderDTO,String dispositionNumber) {
	        ClaimDocument claimDocument = new ClaimDocument();
	        claimDocument.setDocumentType(documentTypeRepository.findByDocumentTypeCode("DSP"));
	        claimDocument.setClaimReasonDefinition(claimReasonDefinitionRepository.findByClaimReasonCode(claimDispositionHeaderDTO.getClaimReasonCode()));
	        claimDocument.setCreatedByUserId(claimDispositionHeaderDTO.getCreatedByUserId());
	        claimDocument.setModifiedByUserId(claimDispositionHeaderDTO.getModifiedByUserId());
	        claimDocument.setClaim(claim);
	        claimDocument.setDocumentNumber(dispositionNumber);
	        claimDocument.setAssociate(true);
	        claimDocument.setStatusId(StatusTypes.ACTIVE.getStatusId());
	        claimDocument.setExchangeRate(BigDecimal.ZERO);
	        claimDocument.setDocumentDate(LocalDateTime.now());
	        claimDocument.setAmountUsd(claimDispositionHeaderDTO.getAmountUsd());
	       
	        return claimDocument;
	    }
	 
	 public List<ClaimDispositionLine> getClaimDispositionLines(Claim claim, List<ClaimDispositionLineDTO> claimDispositionLineDTO, ClaimDispositionHeader claimDispositionHeader) {
			List<ClaimDispositionLine> claimDispositionLine = null;
			if (claimDispositionLineDTO != null) {
				claimDispositionLine = new ArrayList<>();
				for (ClaimDispositionLineDTO claimCrmLinedto : claimDispositionLineDTO) {
					claimDispositionLine.add(getClaimDispositionLinesDetails(claim, claimCrmLinedto, claimDispositionHeader));
				}

			}
			return claimDispositionLine;
	    }
	 
	 private ClaimDispositionLine getClaimDispositionLinesDetails(Claim claim, ClaimDispositionLineDTO claimDispositionLineDTO, ClaimDispositionHeader claimDispositionHeader) {
	    	ClaimDispositionLine claimDispositionLine = new ClaimDispositionLine();
	    	
	    	Optional<ClaimDispositionLine> claimDispositionLineData=claimDispositionLineRepository.findById(claimDispositionLineDTO.getClaimDispositionLineId());
	    	if(claimDispositionLineData.isPresent())
	    	{
	    		claimDispositionLine=claimDispositionLineData.get();
	    		
	    	}
	    	BeanUtils.copyProperties(claimDispositionLineDTO, claimDispositionLine);
	    	ClaimLineDetail claimLineDetail=new ClaimLineDetail();
	    	claimLineDetail.setClaimLineId(claimDispositionLineDTO.getClaimLineId());
	        
	    	claimDispositionLine.setClaimLineDetail(claimLineDetail);
	    	claimDispositionLine.setClaim(claim);
	    	
	    	claimDispositionLine.setClaimDispositionHeader(claimDispositionHeader);
	    	claimDispositionLine.setCreatedByUserId(claimDispositionHeader.getCreatedByUserId());
	    	claimDispositionLine.setModifiedByUserId(claimDispositionHeader.getModifiedByUserId());
	       
	        return claimDispositionLine;
	    }

	 private ClaimNote getClaimNote(Claim claim,int noteTemplateId) {
	        ClaimNote claimNote = new ClaimNote();
	        NoteTemplate noteTemplate=null;
	        Optional<NoteTemplate> noteTemplateData = noteTemplateRepository.findById(noteTemplateId);
	        if(noteTemplateData.isPresent())
	        {
	        	noteTemplate=noteTemplateData.get();
	       }
	        else
	        {
	         noteTemplate = noteTemplateRepository.findByNoteTemplateName("UNKN");
	        }
	        Optional<NoteType> noteType = noteTypeRepository.findById(noteTemplate.getNoteType().getNoteTypeId());
	        claimNote.setClaimNoteText(noteTemplate.getNoteTemplateText());
	        claimNote.setNoteType(noteType.get());
	        claimNote.setNoteGroup(noteType.get().getNoteGroup());
	        claimNote.setNoteTemplate(noteTemplate);
	        claimNote.setClaim(claim);
	        claimNote.setLookup(lookupRepository.findByLookupCode("C"));
	        claimNote.setStatusId(StatusTypes.ACTIVE.getStatusId());
	        claimNote.setCreatedByUserId(claim.getCreatedByUserId());
	        claimNote.setModifiedByUserId(claim.getModifiedByUserId());
	        return claimNote;
	    }


	@Override
	public void DispositonVoid(int claimDispositionHeaderId) {
		Optional<ClaimDispositionHeader> claimCrmHeaderData = claimDispositionHeaderRepository.findById(claimDispositionHeaderId);
		if(claimCrmHeaderData.isPresent())
		{
			claimCrmHeaderData.get().setDispositionStatus(dispositionStatusRepository.findByDispositionStatusCode("V"));
			claimDispositionHeaderRepository.save(claimCrmHeaderData.get());
		
		}
		else
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found With This claimDispositionHeaderId :: " + claimDispositionHeaderId);
		
	}


	@Override
	public ClaimDispositionHeaderDTO fetchDispositonDetailsByClaimIdAndDispositonNumber(int claimId,String dispositionNumber)
	{	
	Optional<ClaimDispositionHeader> claimDispositionHeaderData = claimDispositionHeaderRepository.findClaimDispositionHeaderByClaimIdAndDispositionNumber(claimId, dispositionNumber);
	if (claimDispositionHeaderData.isPresent()) {

		return getDispositionHeaderDetails(claimDispositionHeaderData.get());
		
	} else
		throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
				"Record Not Found With This ClaimId  " + claimId);
	}
	
	private ClaimDispositionHeaderDTO getDispositionHeaderDetails(ClaimDispositionHeader claimDispositionHeader) {
		ClaimDispositionHeaderDTO dispositionHeaderDto = new ClaimDispositionHeaderDTO();
		BeanUtils.copyProperties(claimDispositionHeader, dispositionHeaderDto);
		dispositionHeaderDto.setCreatedDate(claimDispositionHeader.getCreatedDateTime());
		dispositionHeaderDto.setClaimId(claimDispositionHeader.getClaim().getClaimId());
		dispositionHeaderDto.setClaimReasonCode(claimDispositionHeader.getClaim().getClaimReasonDefinition().getClaimReasonCode());
		
		dispositionHeaderDto.setDispositionStatus(claimDispositionHeader.getDispositionStatus().getDispositionStatusCode());
		dispositionHeaderDto.setNoteTemplateId(claimDispositionHeader.getNoteTemplate().getNoteTemplateId());
		dispositionHeaderDto.setStandardMessage(claimDispositionHeader.getNoteTemplate().getNoteTemplateText());
		dispositionHeaderDto.setDispositionType(claimDispositionHeader.getDispositionType().getDispositionTypeDescription());
		dispositionHeaderDto.setDocumentNumber(claimDispositionHeader.getClaimDocument().getDocumentNumber());
		dispositionHeaderDto.setDispositionNumber(claimDispositionHeader.getDispositionNumber());
		
		List<ClaimDispositionLineDTO> ls = new ArrayList<>();
		for (ClaimDispositionLine dispositionLine : claimDispositionHeader.getClaimDispositionLine()) {
			ClaimDispositionLineDTO claimDispositionLineDTO = new ClaimDispositionLineDTO();
			BeanUtils.copyProperties(dispositionLine, claimDispositionLineDTO);
			claimDispositionLineDTO.setClaimLineId(dispositionLine.getClaimLineDetail().getClaimLineId());
			claimDispositionLineDTO.setLineNumber(dispositionLine.getClaimLineDetail().getLineNumber());
			claimDispositionLineDTO.setInvNumber(dispositionLine.getClaimLineDetail().getClaimDocument().getDocumentNumber());
			claimDispositionLineDTO.setInvDate(dispositionLine.getClaimLineDetail().getClaimDocument().getDocumentDate());
			claimDispositionLineDTO.setOrderNUmber(dispositionLine.getClaimLineDetail().getClaimDocument().getOrderNumber());
			claimDispositionLineDTO.setPurchaseOrder(dispositionLine.getClaimLineDetail().getClaimDocument().getPurchaseOrderNumber());
			claimDispositionLineDTO.setUnitOfMeasure(dispositionLine.getClaimLineDetail().getUnitOfMeasure().getUnitOfMeasureCode());
			
			claimDispositionLineDTO.setRollNumber(dispositionLine.getRollNumber());
			claimDispositionLineDTO.setColorNumber(dispositionLine.getColorNumber());
			claimDispositionLineDTO.setColorName(dispositionLine.getColorName());
			claimDispositionLineDTO.setStyleName(dispositionLine.getStyleName());
			claimDispositionLineDTO.setStyleName(dispositionLine.getStyleName());
			ls.add(claimDispositionLineDTO);

		}
		dispositionHeaderDto.setClaimDispositionLineDTO(ls);
		
		return dispositionHeaderDto;

	}
	
}
