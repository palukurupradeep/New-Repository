package com.shaw.claims.services;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.dto.AllowanceToKeepDTO;
import com.shaw.claims.dto.AssociateDocumensDto;
import com.shaw.claims.dto.ClaimCrmHeaderDTO;
import com.shaw.claims.dto.ClaimCrmLineDTO;
import com.shaw.claims.dto.ClaimRgaHeaderDTO;
import com.shaw.claims.dto.CrmIssueResponseDTO;
import com.shaw.claims.dto.CustomerDetailsDTO;
import com.shaw.claims.dto.DeclinCreditMemoResponseDTO;
import com.shaw.claims.dto.DispositionTypeDTO;
import com.shaw.claims.dto.NoteAndTaskRequestDTO;
import com.shaw.claims.dto.UserApprovalLimitDTO;
import com.shaw.claims.dto.UserGroupMappingResponseDTO;
import com.shaw.claims.dto.UsersDTO;
import com.shaw.claims.enums.StatusTypes;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.AuditHistory;
import com.shaw.claims.model.Claim;
import com.shaw.claims.model.ClaimCrmHeader;
import com.shaw.claims.model.ClaimCrmLine;
import com.shaw.claims.model.ClaimDispositionHeader;
import com.shaw.claims.model.ClaimDocument;
import com.shaw.claims.model.ClaimLineDetail;
import com.shaw.claims.model.ClaimNote;
import com.shaw.claims.model.ClaimReasonDefinition;
import com.shaw.claims.model.ClaimRgaHeader;
import com.shaw.claims.model.CrmStatus;
import com.shaw.claims.model.DispositionType;
import com.shaw.claims.model.NoteTemplate;
import com.shaw.claims.model.NoteType;
import com.shaw.claims.model.ServiceToKeepType;
import com.shaw.claims.model.TraceTask;
import com.shaw.claims.repo.ApprovalLimitTypeRepository;
import com.shaw.claims.repo.ClaimCrmHeaderRepository;
import com.shaw.claims.repo.ClaimDispositionHeaderRepository;
import com.shaw.claims.repo.ClaimDocumentRepository;
import com.shaw.claims.repo.ClaimLineDetailsRepository;
import com.shaw.claims.repo.ClaimNoteRepository;
import com.shaw.claims.repo.ClaimReasonDefinitionRepository;
import com.shaw.claims.repo.ClaimRepository;
import com.shaw.claims.repo.ClaimRgaHeaderRepository;
import com.shaw.claims.repo.CrmStatusRepository;
import com.shaw.claims.repo.DispositionTypeRepository;
import com.shaw.claims.repo.DocumentTypeRepository;
import com.shaw.claims.repo.LookupRepository;
import com.shaw.claims.repo.NoteGroupRepository;
import com.shaw.claims.repo.NoteTemplateRepository;
import com.shaw.claims.repo.NoteTypeRepository;
import com.shaw.claims.repo.RgaStatusRepository;
import com.shaw.claims.repo.StatusRepository;
import com.shaw.claims.repo.TraceTaskRepository;
import com.shaw.claims.repo.TraceTypeRepository;
import com.shaw.claims.repo.UserApprovalLimitRepository;
import com.shaw.claims.repo.WorkStatusRepository;

import jakarta.transaction.Transactional;

@Service
public class CrmServiceImpl implements CrmService {

	Logger log = LogManager.getLogger(CrmServiceImpl.class);

	@Autowired
	ClaimCrmHeaderRepository claimCrmHeaderRepository;
	@Autowired
	private ClaimRepository claimRepository;
	@Autowired
	ClaimReasonDefinitionRepository claimReasonDefinitionRepository;
	@Autowired
	CrmStatusRepository crmStatusRepository;
	@Autowired
	private DocumentTypeRepository documentTypeRepository;
	@Autowired
	ClaimDocumentRepository claimDocumentRepository;
	@Autowired
	ClaimRgaHeaderRepository claimRgaHeaderRepository;
	@Autowired
	RgaStatusRepository rgaStatusRepository;
	@Autowired
	ClaimNoteRepository claimNoteRepository;
	@Autowired
	NoteTypeRepository noteTypeRepository;
	@Autowired
	NoteTemplateRepository noteTemplateRepository;
	@Autowired
	LookupRepository lookupRepository;
	@Autowired
	SampleReviewServiceImpl sampleReviewServiceImpl;
	@Autowired
	ClaimDetailsServiceImpl claimDetailsServiceImpl;
	@Autowired
	DispositionTypeRepository dispositionTypeRepository;
	@Autowired
	TraceTaskRepository traceTaskRepository;
	@Autowired
	ClaimServiceImpl claimServiceImpl;
	@Autowired
	ClaimLineDetailsRepository claimLineDetailsRepository;
	@Autowired
	private RestTemplate restTemplate;
	@Value(("${claim-security.rest.url}"))
	private String restSecurityUrl;
	@Autowired
	ClaimDispositionHeaderRepository claimDispositionHeaderRepository;
	@Autowired
	WorkHistoryServiceImpl workHistoryServiceImpl;
	@Autowired
	StatusRepository statusRepository;
	
	@Autowired
	private UserApprovalLimitRepository userApprovalLimitRepository;
	@Autowired
	private ApprovalLimitTypeRepository approvalLimitTypeRepository;
	
	@Autowired
	private NoteGroupRepository noteGroupRepository;
	
	@Autowired
	TraceTypeRepository traceTypeRepository;
	
	@Autowired
	private ClaimServiceImpl claimService;
	
	@Autowired
	WorkStatusRepository workStatusRepository;

	@Override
	@Transactional
	public CrmIssueResponseDTO crmHoldOrIssue(ClaimCrmHeaderDTO claimCrmHeaderDTO) {
		Optional<ClaimCrmHeader> claimCrmHeaderData = claimCrmHeaderRepository
				.findById(claimCrmHeaderDTO.getClaimCrmHeaderId());
		ClaimCrmHeader claimCrmHeader = new ClaimCrmHeader();
		CrmIssueResponseDTO crmIssueResponseDTO = new CrmIssueResponseDTO();
		String crmNumberData = "";
		Claim claim = findByClaimId(claimCrmHeaderDTO.getClaimId());
		if (claimCrmHeaderData.isPresent()) {

			claimCrmHeader = claimCrmHeaderData.get();
			crmNumberData = claimCrmHeader.getCrmNumber();
			
		}
		else
		{
			 String crmNumber = String.format("%06d",claimCrmHeaderRepository.getNextCrmNumber());
				claimCrmHeader.setCrmNumber(crmNumber);
				ClaimDocument claimDocumentData = getClaimDocument(claim, claimCrmHeaderDTO.getAmountUsd(),
						claimCrmHeaderDTO.getModifiedByUserId(), claimCrmHeaderDTO.getCreatedByUserId(),
						claimCrmHeaderDTO.getClaimReasonCode(), claimCrmHeader.getCrmNumber(), "CRM");
				ClaimDocument claimDocument = claimDocumentRepository.save(claimDocumentData);
				claimCrmHeader.setClaimDocument(claimDocument);
				crmNumberData = crmNumber;
		}
		BeanUtils.copyProperties(claimCrmHeaderDTO, claimCrmHeader);
		ServiceToKeepType serviceToKeepType = new ServiceToKeepType();
		serviceToKeepType.setServiceToKeepTypeId(claimCrmHeaderDTO.getServiceToKeepTypeId());
		claimCrmHeader.setServiceToKeepType(serviceToKeepType);
		claimCrmHeader.setCurrentUserId(claimCrmHeaderDTO.getCurrentUserId());
		claimCrmHeader.setPreparedByUserId(claimCrmHeaderDTO.getPreparedByUserId());
		crmIssueResponseDTO.setManagerApprovalFlag(false);
		if(claimCrmHeaderDTO.getCrmStatusCode().equalsIgnoreCase("I")) {
		validateUserLimit(claimCrmHeaderDTO,claim,crmNumberData,claimCrmHeader,crmIssueResponseDTO);
	
		}
		claimCrmHeader.setClaim(claim);
		claimCrmHeader.setClaimCustomerNumber(claimCrmHeaderDTO.getClaimCustomerNumber());
		claimCrmHeader.setClaimReasonDefinition(
				claimReasonDefinitionRepository.findByClaimReasonCode(claimCrmHeaderDTO.getClaimReasonCode()));
		
		claimCrmHeader.setCrmStatusId(
				crmStatusRepository.findByCrmStatusCode(claimCrmHeaderDTO.getCrmStatusCode()).getCrmStatusId());
		List<ClaimCrmLine> crmLinesListData = getClaimCrmLines(claim, claimCrmHeaderDTO.getClaimCrmLineDTO(),
				claimCrmHeader, claimCrmHeaderDTO.getCrmStatusCode());
		claimCrmHeader.setClaimCrmLine(crmLinesListData);
		claimCrmHeader.setIssuedDate(LocalDateTime.now());
		ClaimCrmHeader claimCrmHeaderResponse = claimCrmHeaderRepository.save(claimCrmHeader);
		if (null != claimCrmHeaderDTO.getComments() && !claimCrmHeaderDTO.getComments().isEmpty()) {

			ClaimNote claimNote = getClaimNote(claim, claimCrmHeaderDTO.getComments());
			claimNoteRepository.save(claimNote);

		}
		crmIssueResponseDTO.setCrmNumber(crmNumberData);
		crmIssueResponseDTO.setCrmHeaderId(claimCrmHeaderResponse.getClaimCrmHeaderId());
		crmIssueResponseDTO.setAmountUsd(claimCrmHeaderDTO.getAmountUsd());
		return crmIssueResponseDTO;
	}

	private Claim findByClaimId(Integer claimId) {
		Optional<Claim> claim = claimRepository.findById(claimId);
		if (!claim.isPresent()) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found In This ClaimId :: " + claimId);
		}
		return claim.get();
	}

	public List<ClaimCrmLine> getClaimCrmLines(Claim claim, List<ClaimCrmLineDTO> claimCrmLineDTO,
			ClaimCrmHeader claimCrmHeader, String crmStatusCode) {
		List<ClaimCrmLine> claimCrmLine = null;
		if (claimCrmLineDTO != null) {
			claimCrmLine = new ArrayList<>();
			for (ClaimCrmLineDTO claimCrmLinedto : claimCrmLineDTO) {
				claimCrmLine.add(getClaimCrmLinesDetails(claim, claimCrmLinedto, claimCrmHeader, crmStatusCode));
			}

		}
		return claimCrmLine;
	}

	private ClaimCrmLine getClaimCrmLinesDetails(Claim claim, ClaimCrmLineDTO claimCrmLineDTO,
			ClaimCrmHeader claimCrmHeader, String crmStatusCode) {
		ClaimCrmLine claimCrmLine = new ClaimCrmLine();
		BeanUtils.copyProperties(claimCrmLineDTO, claimCrmLine);
		if (claimCrmLineDTO.getClaimCrmLineId() != 0) {
			claimCrmLine.setClaimCrmLineId(claimCrmLineDTO.getClaimCrmLineId());
		}

		CrmStatus crmStatus = new CrmStatus();
		crmStatus.setCrmStatusId(crmStatusRepository.findByCrmStatusCode(crmStatusCode).getCrmStatusId());

		ClaimLineDetail claimLineDetails = claimLineDetailsRepository
				.findByClaimLineId(claimCrmLineDTO.getClaimLineId());
		claimCrmLine.setClaimLineDetail(claimLineDetails);
		claimCrmLine.setClaim(claim);
		claimCrmLine.setCrmStatus(crmStatus);
		claimCrmLine.setClaimCrmHeader(claimCrmHeader);
		claimCrmLine.setCreatedByUserId(claimCrmHeader.getCreatedByUserId());
		claimCrmLine.setModifiedByUserId(claimCrmHeader.getModifiedByUserId());

		return claimCrmLine;
	}

	public ClaimDocument getClaimDocument(Claim claim, BigDecimal amountUsd, Integer modifiedByUserId,
			Integer createdByUserId, String reasonCode, String crmNumber, String documentTypeCode) {
		ClaimDocument claimDocument = new ClaimDocument();
		claimDocument.setDocumentType(documentTypeRepository.findByDocumentTypeCode(documentTypeCode));
		claimDocument.setClaimReasonDefinition(claimReasonDefinitionRepository.findByClaimReasonCode(reasonCode));
		claimDocument.setCreatedByUserId(createdByUserId);
		claimDocument.setModifiedByUserId(modifiedByUserId);
		claimDocument.setClaim(claim);
		claimDocument.setDocumentNumber(crmNumber);
		claimDocument.setAssociate(true);
		claimDocument.setStatusId(StatusTypes.ACTIVE.getStatusId());
		claimDocument.setExchangeRate(BigDecimal.ZERO);
		claimDocument.setDocumentDate(LocalDateTime.now());
		claimDocument.setAmountUsd(amountUsd);
		return claimDocument;
	}

	@Override
	public ClaimCrmHeaderDTO fetchCrmDetailsByClaimIdAndCrmNumber(int claimId, String crmNumber) {
		Optional<ClaimCrmHeader> claimCrmHeaderData = claimCrmHeaderRepository
				.findClaimCrmHeaderByClaimIdAndCrmNumber(claimId, crmNumber);
		if (claimCrmHeaderData.isPresent()) {

			return getCrmHeaderDetails(claimCrmHeaderData.get());

		} else
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found With This ClaimId  " + claimId);
	}

	private ClaimCrmHeaderDTO getCrmHeaderDetails(ClaimCrmHeader claimCrmHeader) {
		ClaimCrmHeaderDTO crmHeaderDto = new ClaimCrmHeaderDTO();
		BeanUtils.copyProperties(claimCrmHeader, crmHeaderDto);
		crmHeaderDto.setClaimId(claimCrmHeader.getClaim().getClaimId());
		crmHeaderDto.setClaimReasonCode(claimCrmHeader.getClaimReasonDefinition().getClaimReasonCode());
		crmHeaderDto.setCrmStatusCode(
				crmStatusRepository.findById(claimCrmHeader.getCrmStatusId()).get().getCrmStatusCode());
		crmHeaderDto.setServiceToKeepTypeId(claimCrmHeader.getServiceToKeepType().getServiceToKeepTypeId());
		crmHeaderDto.setCreditMemoDate(claimCrmHeader.getCreatedDateTime());
		crmHeaderDto.setClaimNumber(claimCrmHeader.getClaim().getClaimNumber());
		crmHeaderDto.setClaimsArea(claimCrmHeader.getClaim().getClaimAreaId());
		crmHeaderDto.setClaimsAreaType(claimCrmHeader.getClaim().getClaimAreaTypeId());
		crmHeaderDto.setCustomerNumber(claimCrmHeader.getClaim().getCustomerNumber());
		crmHeaderDto.setDiv(claimCrmHeader.getClaim().getDivision());
		crmHeaderDto.setSalesPerson(claimCrmHeader.getClaim().getTerritoryManagerName());
		crmHeaderDto.setCrmNum(claimCrmHeader.getCrmNumber());
		UsersDTO usersDTO=getUsersDetailsByUserId(claimCrmHeader.getCreatedByUserId());
		crmHeaderDto.setUserName(usersDTO.getFirstName()+" "+usersDTO.getLastName());
		crmHeaderDto.setCustomerOrderNumber(claimCrmHeader.getClaim().getClaimDocuments().stream().map(ClaimDocument::getOrderNumber).findAny().orElse(""));
		List<ClaimCrmLineDTO> ls = new ArrayList<>();
		for (ClaimCrmLine crmLine : claimCrmHeader.getClaimCrmLine()) {
			ClaimCrmLineDTO claimCrmLineDTO = new ClaimCrmLineDTO();
			BeanUtils.copyProperties(crmLine, claimCrmLineDTO);
			claimCrmLineDTO.setClaimLineId(crmLine.getClaimLineDetail().getClaimLineId());
			claimCrmLineDTO.setReasonCode(crmLine.getClaimLineDetail().getClaimReasonDefinition().getClaimReasonCode());
			claimCrmLineDTO.setInvNumber(crmLine.getClaimLineDetail().getClaimDocument().getDocumentNumber());
			claimCrmLineDTO.setInvDate(crmLine.getClaimLineDetail().getClaimDocument().getDocumentDate());
			claimCrmLineDTO.setOrderNumber(crmLine.getClaimLineDetail().getClaimDocument().getOrderNumber());
			claimCrmLineDTO.setBolNumber(crmLine.getClaimLineDetail().getClaimDocument().getBillofLading());
			claimCrmLineDTO.setPoNumber(crmLine.getClaimLineDetail().getClaimDocument().getPurchaseOrderNumber());
			claimCrmLineDTO.setUnitOfMeasureCode(crmLine.getClaimLineDetail().getUnitOfMeasure().getUnitOfMeasureCode());
			claimCrmLineDTO.setLineNumber(crmLine.getClaimLineDetail().getLineNumber());
			claimCrmLineDTO.setInvoiceLineNumber(crmLine.getClaimLineDetail().getInvoiceLineNumber());
			claimCrmLineDTO.setLot(crmLine.getClaimLineDetail().getDyeLot());
			claimCrmLineDTO.setType(crmLine.getClaimLineDetail().getRcsCodes().getRcsCode());
			claimCrmLineDTO.setGrade(crmLine.getClaimLineDetail().getGrade());
			claimCrmLineDTO.setUnitPriceUsd(crmLine.getClaimLineDetail().getUnitPriceUsd());
			ls.add(claimCrmLineDTO);

		}
		crmHeaderDto.setClaimCrmLineDTO(ls);
		List<ClaimNote> claimNotes = claimNoteRepository.getClaimNotesByclaimIdAndnoteTypeId(
				claimCrmHeader.getClaim().getClaimId(),
				noteTypeRepository.fetchNoteTypeByNoteTypeCode("CREDMEMO").getNoteTypeId());
		
		
		if(null!=claimNotes && claimNotes.size()>0)
		{
		claimNotes.sort(Comparator.comparing(ClaimNote::getCreatedDateTime).reversed());
		String notes=claimNotes.stream().map(p->p.getClaimNoteText()).findFirst().orElse("");
	  
		crmHeaderDto.setComments(notes);
		}
		
		return crmHeaderDto;

	}

	@Override
	public List<AssociateDocumensDto> fetchDocumentsByClaimId(int claimId) {
		List<ClaimDocument> docList = claimDocumentRepository.getDocumentsByClaimId(claimId);
		if (!docList.isEmpty()) {
			List<AssociateDocumensDto> docDtoList = new ArrayList<>();
			for (ClaimDocument claimDocument : docList) {
				AssociateDocumensDto docDto = new AssociateDocumensDto();
				docDto.setDocumentNumber(claimDocument.getDocumentNumber());
				docDto.setDocumentTypeCode(claimDocument.getDocumentType().getDocumentTypeCode());
				docDto.setClaimDocumentId(claimDocument.getClaimDocumentId());
				docDto.setClaimId(claimDocument.getClaim().getClaimId());
				docDto.setDocumentDate(claimDocument.getDocumentDate());
				docDto.setAmountUsd(claimDocument.getAmountUsd());
				docDto.setOrderNumber(claimDocument.getOrderNumber());
				docDto.setPurchaseOrderNumber(claimDocument.getPurchaseOrderNumber());
				docDto.setBillofLading(claimDocument.getBillofLading());
				docDto.setMasterBillofLading(claimDocument.getMasterBillofLading());
				docDto.setStatus(statusRepository.findStatusStatusCodeByStatusId(claimDocument.getStatusId()));
				Optional<ClaimCrmHeader> claimCrmHeaderData = claimCrmHeaderRepository
						.findClaimCrmHeaderByClaimIdAndCrmNumber(claimId,
								claimDocument.getDocumentNumber());
				if (claimCrmHeaderData.isPresent()) {
					docDto.setStatus(crmStatusRepository.findByCrmStatusId(claimCrmHeaderData.get().getCrmStatusId())
							.getCrmStatusDescription());
					docDto.setHeaderId(claimCrmHeaderData.get().getClaimCrmHeaderId());
				}

				Optional<ClaimRgaHeader> claimRgaHeaderData = claimRgaHeaderRepository
						.findClaimRgaHeaderByClaimIdAndRgaNumber(claimId,
								claimDocument.getDocumentNumber());
				if (claimRgaHeaderData.isPresent()) {
					docDto.setStatus(rgaStatusRepository.findByRgaStatusId(claimRgaHeaderData.get().getRgaStatusId())
							.getRgaStatusDescription());
					docDto.setHeaderId(claimRgaHeaderData.get().getClaimRgaHeaderId());
				}
				
				Optional<ClaimDispositionHeader> claimDispositionHeader = 	claimDispositionHeaderRepository.findClaimDispositionHeaderByClaimIdAndDispositionNumber(claimId,
								claimDocument.getDocumentNumber());
				if (claimDispositionHeader.isPresent()) {
					docDto.setStatus(claimDispositionHeader.get().getDispositionStatus().getDispositionStatusDescription());
					docDto.setHeaderId(claimDispositionHeader.get().getClaimDispositionHeaderId());
				}
				
				docDtoList.add(docDto);

			}
			return docDtoList;
		} else
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found With This ClaimId :: " + claimId);
	}

	@Override
	public DeclinCreditMemoResponseDTO declineCreditMemo(int claimCrmHeaderId, int userId,String noteText) {
		Optional<ClaimCrmHeader> claimCrmHeaderData = claimCrmHeaderRepository.findById(claimCrmHeaderId);
		ClaimCrmHeader claimCrmHeader = new ClaimCrmHeader();
		if (claimCrmHeaderData.isPresent()) {

			claimCrmHeader = claimCrmHeaderData.get();
		}

		CrmStatus crmStatus = crmStatusRepository.findByCrmStatusCode(CommonConstant.DECLINE_STATUS_CODE);
		claimCrmHeader.setCrmStatusId(crmStatus.getCrmStatusId());

		Claim claim = findByClaimId(claimCrmHeader.getClaim().getClaimId());

		List<ClaimCrmLineDTO> claimCrmHeaderDTOList = claimCrmHeader.getClaimCrmLine().stream().map(data -> {
			ClaimCrmLineDTO claimCrmLineDTO = new ClaimCrmLineDTO();
			BeanUtils.copyProperties(data, claimCrmLineDTO);
			claimCrmLineDTO.setClaimLineId(data.getClaimLineDetail().getClaimLineId());
			return claimCrmLineDTO;
		}).collect(Collectors.toList());

		List<ClaimCrmLine> crmLinesListData = getClaimCrmLines(claim, claimCrmHeaderDTOList, claimCrmHeader,
				crmStatus.getCrmStatusCode());
		claimCrmHeader.setClaimCrmLine(crmLinesListData);
		claimCrmHeader.setModifiedByUserId(userId);
		claimCrmHeaderRepository.save(claimCrmHeader);
		if(!noteText.isEmpty())
		{
			ClaimNote claimNote= getClaimNote(claim,  noteText,null);
			claim.getClaimNotes().add(claimNote);
			claimRepository.save(claim);
		}
	    workHistoryServiceImpl.updateWorkHistory(claimCrmHeader.getClaim(),  claimCrmHeader.getModifiedByUserId());
		return new DeclinCreditMemoResponseDTO(claimCrmHeaderId,
				crmStatus.getCrmStatusDescription() + " status updated successfully");

	}

	private ClaimNote getClaimNote(Claim claim, String comments) {
		ClaimNote claimNote = new ClaimNote();
		NoteType noteType = noteTypeRepository.fetchNoteTypeByNoteTypeCode("CREDMEMO");
		NoteTemplate noteTemplate = noteTemplateRepository.findByNoteTemplateName("UNKN");
		claimNote.setClaimNoteText(comments);
		claimNote.setNoteType(noteType);
		claimNote.setNoteGroup(noteType.getNoteGroup());
		claimNote.setNoteTemplate(noteTemplate);
		claimNote.setClaim(claim);
		claimNote.setLookup(lookupRepository.findByLookupCode("P"));
		claimNote.setStatusId(StatusTypes.ACTIVE.getStatusId());
		claimNote.setCreatedByUserId(claim.getCreatedByUserId());
		claimNote.setModifiedByUserId(claim.getModifiedByUserId());

		return claimNote;
	}

	@Override
	public void updateCrmDetailsByAction(int claimCrmHeaderId, String action, int userId,String noteText) {
		Optional<ClaimCrmHeader> claimCrmHeaderData = claimCrmHeaderRepository.findById(claimCrmHeaderId);
		if (claimCrmHeaderData.isPresent()) {
			switch (action) {
			case "Void":
				claimCrmHeaderData.get().setCrmStatusId(crmStatusRepository.findByCrmStatusCode("V").getCrmStatusId());
				claimCrmHeaderData.get().getClaimCrmLine().stream()
						.forEach(p -> p.setCrmStatus(crmStatusRepository.findByCrmStatusCode("V")));
				if(!noteText.isEmpty())
				{
					Claim claim=claimCrmHeaderData.get().getClaim();
					ClaimNote claimNote= getClaimNote(claim,  noteText,null);
					claim.getClaimNotes().add(claimNote);
					claimRepository.save(claim);
				}
				break;

			case "Cancelled":
				claimCrmHeaderData.get().setCrmStatusId(crmStatusRepository.findByCrmStatusCode("C").getCrmStatusId());
				claimCrmHeaderData.get().getClaimCrmLine().stream()
						.forEach(p -> p.setCrmStatus(crmStatusRepository.findByCrmStatusCode("C")));
				break;
			}

			claimCrmHeaderData.get().setModifiedByUserId(userId);
			claimCrmHeaderRepository.save(claimCrmHeaderData.get());
			workHistoryServiceImpl.updateWorkHistory(claimCrmHeaderData.get().getClaim(), userId);
		} else
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found With This claimCrmHeaderId :: " + claimCrmHeaderId);

	}

	@Transactional
	public void creditMemoValidation(int claimCrmHeaderId, int userId, String customerNumber, Boolean isTraceTaskOpen) {
		Optional<ClaimCrmHeader> claimCrmHeaderData = claimCrmHeaderRepository.findById(claimCrmHeaderId);
		ClaimCrmHeader claimCrmHeader = new ClaimCrmHeader();
		if (claimCrmHeaderData.isPresent()) {

			claimCrmHeader = claimCrmHeaderData.get();
		}

		CrmStatus crmStatus = crmStatusRepository.findByCrmStatusCode(CommonConstant.DECLINE_STATUS_CODE);
		claimCrmHeader.setCrmStatusId(crmStatus.getCrmStatusId());
		List<ClaimCrmLineDTO> claimCrmHeaderDTOList = null;
		if(claimCrmHeader.getClaimCrmLine() != null) {
			   claimCrmHeaderDTOList = claimCrmHeader.getClaimCrmLine().stream().map(data -> {
				log.info("Claim crm Line id :: " + data.getClaimCrmLineId());
				log.info("Claim crm Line id :: " + data.getClaimLineDetail().getClaimLineId());
				ClaimCrmLineDTO claimCrmLineDTO = new ClaimCrmLineDTO();
				BeanUtils.copyProperties(data, claimCrmLineDTO);
				claimCrmLineDTO.setClaimLineId(data.getClaimLineDetail().getClaimLineId());
				return claimCrmLineDTO;
			}).collect(Collectors.toList());	
		}

		/* active user validation */
		String url = "/customer/v1/getCustomerDetails?customerNumber=";
		List<CustomerDetailsDTO> customerDetailsList = claimDetailsServiceImpl.fetchData(customerNumber, url,
				CustomerDetailsDTO.class);

		if (customerDetailsList != null && !customerDetailsList.get(0).getCustomerStatus().isEmpty()
				&& !customerDetailsList.get(0).getCustomerStatus().equalsIgnoreCase("Active")) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Customer is not active :: " + customerNumber);
		}

		UserApprovalLimitDTO userApprovalLimitDTO = findUserApprovalLimit(userId, CommonConstant.CREDIT_MEMO_CODE);

		Claim claim = null;
		if(claimCrmHeader.getClaim() != null) {
			claim = findByClaimId(claimCrmHeader.getClaim().getClaimId());

		}
		/* Reasoncode validation */
		
		List<ClaimCrmLine> crmLinesListData = getClaimCrmLines(claim, claimCrmHeaderDTOList, claimCrmHeader,
				crmStatus.getCrmStatusCode());
		AtomicReference<BigDecimal> crmLineTotalAmount = new AtomicReference<>(BigDecimal.ZERO);
		if(crmLinesListData != null) {
			crmLinesListData.stream().forEach(data -> {
				if (data.getClaimLineDetail().getClaimReasonDefinition() == null) {
					throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
							"ReasonCode Not Associated With This Claim Line :: " + data.getClaimCrmLineId());
				}
				crmLineTotalAmount.updateAndGet(value -> value.add(data.getAmountUsd()));

			});
		}
		
		if(isTraceTaskOpen.equals(true)) {
		/* Trace Task Validation */
		List<TraceTask> traceTaskList = traceTaskRepository.findByClaim_ClaimId(claim.getClaimId());
		
		if(traceTaskList != null) {
			traceTaskList.stream().forEach(traceTask -> {
				if (traceTask != null && traceTask.getStatusId() == CommonConstant.ACTIVE) {
					throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
							"There is an open Trace Task for this claim :: " + traceTask.getClaim().getClaimId());
				}	
			});
		}
		
		}
		/* credimemo approval limit validation */
		if (userApprovalLimitDTO != null
				&& crmLineTotalAmount.get().doubleValue() > userApprovalLimitDTO.getApprovalLimit()) {
			createTraceTask(claim, userId, claimCrmHeader, CommonConstant.APPROVAL_REQUIRED_CRM_TEMPLATE1);
		} else {
			createTraceTask(claim, userId, claimCrmHeader, CommonConstant.CREDID_MEMO);
		}

	}

	public UserApprovalLimitDTO findUserApprovalLimit(int userId, String approvalLimitType) {
		String url = "/users/v1/getApprovalLimitByUserId?userId=" + userId + "&approvalLimitType=" + approvalLimitType;
		String finalUrl = restSecurityUrl + url;
		log.info("Final Security Url :: " + finalUrl);
		ResponseEntity<UserApprovalLimitDTO> responseEntity = restTemplate.exchange(finalUrl, HttpMethod.GET, null,
				new ParameterizedTypeReference<UserApprovalLimitDTO>() {
				});
		UserApprovalLimitDTO response = responseEntity.getBody();
		if (response == null) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found with userId :: " + userId);
		}
		return response;
	}

	private void createTraceTask(Claim claim, int userId, ClaimCrmHeader claimCrmHeader, String noteTemplateName) {
		TraceTask traceTask = new TraceTask();
		NoteAndTaskRequestDTO noteAndTaskRequestDTO = new NoteAndTaskRequestDTO();
		noteAndTaskRequestDTO.setTraceTypeId(6);
		noteAndTaskRequestDTO.setClaimId(claim != null ? claim.getClaimId() : 0);
		noteAndTaskRequestDTO.setCreatedByUserId(userId);
		noteAndTaskRequestDTO.setModifiedByUserId(userId);
		NoteTemplate noteTemplate = claimServiceImpl.getNoteTemplate(claim, noteTemplateName);
		if (noteTemplate != null) {
			ClaimNote claimNote = getClaimNote(claim, noteTemplate, userId, claimCrmHeader);
			sampleReviewServiceImpl.setTraceTask(noteAndTaskRequestDTO, claimNote, traceTask);
			List<ClaimNote> claimNoteList = new ArrayList<>(Arrays.asList(claimNote));
			claim.setClaimNotes(claimNoteList);
			AuditHistory auditHistory = getAuditHistory(claim, noteTemplate, userId, claimCrmHeader);
			List<AuditHistory> auditHisotryList = new ArrayList<>(Arrays.asList(auditHistory));
			claim.setAuditHistory(auditHisotryList);
			claim.setWorkStatusId(CommonConstant.CRM_APPROVAL_IN_PROCESS);
			claimRepository.save(claim);
		}
	}

	private AuditHistory getAuditHistory(Claim claim, NoteTemplate noteTemplate, int userId,
			ClaimCrmHeader claimCrmHeader) {
		AuditHistory auditHistory = new AuditHistory();
		auditHistory.setClaim(claim);
		auditHistory.setNoteTemplate(noteTemplate);
		auditHistory.setStatusId(StatusTypes.ACTIVE.getStatusId());
		auditHistory.setCreatedByUserId(userId);
		auditHistory.setModifiedByUserId(userId);
		auditHistory.setAuditHistoryText(getAuditText(noteTemplate, claimCrmHeader));
		return auditHistory;

	}

	private String getAuditText(NoteTemplate noteTemplate, ClaimCrmHeader claimCrmHeader) {
		switch (noteTemplate.getNoteTemplateName()) {
		case CommonConstant.CREDID_MEMO:
			return getAuditTextForCRM(noteTemplate, claimCrmHeader);
		case CommonConstant.APPROVAL_REQUIRED_CRM_TEMPLATE1:
			return getClaimNoteTextForCRMApprovalRequired(noteTemplate, claimCrmHeader);
		default:
			return "Template not in this Claim";
		}
	}

	private String getAuditTextForCRM(NoteTemplate noteTemplate, ClaimCrmHeader claimCrmHeader) {
		return noteTemplate.getAuditHistoryTemplateText()
				.replace("#CRMNUM", String.valueOf(claimCrmHeader.getCrmNumber()))
				.replace("#INVNUM", claimCrmHeader.getClaimDocument().getDocumentNumber());
	}

	private ClaimNote getClaimNote(Claim claim, NoteTemplate noteTemplate, int userId, ClaimCrmHeader claimCrmHeader) {
		ClaimNote claimNote = new ClaimNote();
		if (noteTemplate.getNoteType() != null) {
			NoteType noteType = noteTypeRepository
					.fetchNoteTypeByNoteTypeCode(noteTemplate.getNoteType().getNoteTypeCode());
			claimNote.setNoteGroup(noteType.getNoteGroup());
		}
		claimNote.setClaimNoteText(getClaimNoteText(noteTemplate, claimCrmHeader));
		claimNote.setNoteType(noteTemplate.getNoteType());
		claimNote.setNoteTemplate(noteTemplate);
		claimNote.setClaim(claim);
		claimNote.setLookup(lookupRepository.findByLookupCode("P"));
		claimNote.setStatusId(StatusTypes.ACTIVE.getStatusId());
		claimNote.setCreatedByUserId(userId);
		claimNote.setModifiedByUserId(userId);
		return claimNote;
	}

	private String getClaimNoteText(NoteTemplate noteTemplate, ClaimCrmHeader claimCrmHeader) {
		switch (noteTemplate.getNoteTemplateName()) {
		case CommonConstant.CREDID_MEMO:
			return getClaimNoteTextForCRM(noteTemplate, claimCrmHeader);
		case CommonConstant.APPROVAL_REQUIRED_CRM_TEMPLATE1:
			return getClaimNoteTextForCRMApprovalRequired(noteTemplate, claimCrmHeader);
		default:
			return "Template not in this Claim";
		}
	}

	private String getClaimNoteTextForCRMApprovalRequired(NoteTemplate noteTemplate, ClaimCrmHeader claimCrmHeader) {
		return noteTemplate.getNoteTemplateText().replace("#CRD_MEMO_NUMBER",
				String.valueOf(claimCrmHeader.getCrmNumber()));
	}

	private String getClaimNoteTextForCRM(NoteTemplate noteTemplate, ClaimCrmHeader claimCrmHeader) {
		return noteTemplate.getNoteTemplateText().replace("#CRMNUM", String.valueOf(claimCrmHeader.getCrmNumber()))
				.replace("#INVNUM", claimCrmHeader.getClaimDocument() != null ? claimCrmHeader.getClaimDocument().getDocumentNumber(): "")
				.replace("#ONUM",claimCrmHeader.getClaimDocument() != null ?  claimCrmHeader.getClaimDocument().getOrderNumber() : "")
				.replace("#PONUM", claimCrmHeader.getClaimDocument() != null ?  claimCrmHeader.getClaimDocument().getPurchaseOrderNumber(): "")
				.replace("#RGA", claimCrmHeader.getAssociatedRgaNumber() + "")
				.replace("#DBT", claimCrmHeader.getAssociatedDebitNumber() != null? claimCrmHeader.getAssociatedDebitNumber() : "")
				.replace("#CHARBCK", claimCrmHeader.getAssociatedCcbNumber() != null ? claimCrmHeader.getAssociatedCcbNumber() : "");
	}

	@Override
	public List<DispositionTypeDTO> getAllDispositionTypes() {

		List<DispositionType> dispositionTypeList = dispositionTypeRepository.findAll();
		List<DispositionTypeDTO> dispositionTypeDTOList = null;
		if (dispositionTypeList != null) {
			dispositionTypeDTOList = dispositionTypeList.stream().map(data -> {
				DispositionTypeDTO dispositionTypeDTO = new DispositionTypeDTO();
				BeanUtils.copyProperties(data, dispositionTypeDTO);
				return dispositionTypeDTO;
			}).collect(Collectors.toList());
		}
		return dispositionTypeDTOList;
	}
	
	public UsersDTO getUsersDetailsByUserId(int userId) {
		String url = "/users/v1/findUserById?userId=" + userId;
		String finalUrl = restSecurityUrl + url;
		log.info("Final Security Url :: " + finalUrl);
		ResponseEntity<UsersDTO> responseEntity = restTemplate.exchange(
				finalUrl,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<UsersDTO>() {});
		UsersDTO response = responseEntity.getBody();
		if (response == null) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found with userId :: " + userId);
		}
		return response;
	}

	 private ClaimNote getClaimNote(Claim claim, String comments,String templateName) {
	        ClaimNote claimNote = new ClaimNote();
	        NoteType noteType = noteTypeRepository.fetchNoteTypeByNoteTypeCode("CREDMEMO");
	      //  NoteTemplate noteTemplate = noteTemplateRepository.findByNoteTemplateName("UNKN");
	        NoteTemplate noteTemplate=null;
			 noteTemplate = noteTemplateRepository.findByNoteTemplateName(templateName);
			if(noteTemplate==null)
			{
				 noteTemplate = noteTemplateRepository.findByNoteTemplateName("UNKN");	
			}
	        
	        claimNote.setClaimNoteText(comments);
	        claimNote.setNoteType(noteType);
	        claimNote.setNoteGroup(noteType.getNoteGroup());
	        claimNote.setNoteTemplate(noteTemplate);
	        claimNote.setClaim(claim);
	        claimNote.setLookup(lookupRepository.findByLookupCode("P"));
	        claimNote.setStatusId(StatusTypes.ACTIVE.getStatusId());
	        claimNote.setCreatedByUserId(claim.getCreatedByUserId());
	        claimNote.setModifiedByUserId(claim.getModifiedByUserId());
	       
	        return claimNote;
	    }

	@Override
	public List<ClaimReasonDefinition> getAllowanceToKeepData(AllowanceToKeepDTO allowanceToKeepDTO) {
	List<ClaimReasonDefinition> claimReasonDefList=	claimReasonDefinitionRepository.findByclaimReasonCodeIn(allowanceToKeepDTO.getReasonCodes());
		return claimReasonDefList;
	}
	
	private void validateUserLimit(ClaimCrmHeaderDTO claimCrmHeaderDTO, Claim claim, String crmNumber,ClaimCrmHeader CrmHeader,CrmIssueResponseDTO crmIssueResponseDTO) {
		log.info("CrmServiceImpl.crmDraftORSave.validateUserLimit");
		int approvalLimitTypeId = approvalLimitTypeRepository.findApprovalLimitTypeIdByApprovalLimitTypeCode(CommonConstant.CREDIT_MEMO_CODE);
		BigDecimal userLimit = userApprovalLimitRepository.findApprovalLimitByUserIdAndApprovalLimitTypeId(claim.getClaimUserId(),approvalLimitTypeId);
		if(userLimit.compareTo(claimCrmHeaderDTO.getAmountUsd()) < 0 || claimCrmHeaderDTO.getClaimReasonCode().equalsIgnoreCase("W02")){
			
			claimCrmHeaderDTO.setCrmStatusCode("P");
			crmIssueResponseDTO.setManagerApprovalFlag(true);
			claim.setClaimNotes(getNotesAndTask(claim,claimCrmHeaderDTO,crmNumber,CrmHeader));
			claim.setAuditHistory(getAuditHistory(claim,crmNumber,claimCrmHeaderDTO));
			claim.setWorkStatusId(workStatusRepository.findWorkStatusIdByWorkStatusCode("017"));
			
		}
	}
	
	
	private List<ClaimNote> getNotesAndTask(Claim claim, ClaimCrmHeaderDTO claimCrmHeaderDTO, String crmNumber,ClaimCrmHeader CrmHeader) {
		log.info("CrmServiceImpl.crmDraftORSave.validateUserLimit.setNotesAndTask");
		List<ClaimNote> claimNotes = new ArrayList<>();
		List<TraceTask> traceTasks = new ArrayList<>();
		ClaimNote claimNote = new ClaimNote();
		TraceTask traceTask = new TraceTask();
		claimNote.setNoteType(noteTypeRepository.fetchNoteTypeByNoteTypeCode(CommonConstant.ISSUE_RGA_NOTE_TYPE_CODE));
		claimNote.setNoteGroup(noteGroupRepository.findByNoteGroupCode(claimNote.getNoteType().getNoteGroup().getNoteGroupCode()));
		claimNote.setNoteTemplate(noteTemplateRepository.findByNoteTemplateName(CommonConstant.APPROVAL_REQUIRED_CRM_TEMPLATE1));
		claimNote.setClaim(claim);
		claimNote.setLookup(lookupRepository.findByLookupCode("P"));
		claimNote.setCallLog(false);
		claimNote.setClaimNoteText(getCRMClaimNoteText(claimNote.getNoteTemplate().getNoteTemplateText(),crmNumber,claim.getClaimUserId(),claimCrmHeaderDTO,CrmHeader,traceTask));
		claimNote.setStatusId(StatusTypes.ACTIVE.getStatusId());
		claimNote.setCreatedByUserId(claimCrmHeaderDTO.getCreatedByUserId());
		claimNote.setModifiedByUserId(claimCrmHeaderDTO.getModifiedByUserId());
		traceTask.setClaimNote(claimNote);
		traceTask.setTraceType(traceTypeRepository.findByTraceTypeCode("005"));
		traceTask.setClaim(claim);
	
		traceTask.setTraceActionLink("");
		traceTask.setTraceReference("");
		traceTask.setStatusId(StatusTypes.ACTIVE.getStatusId());
		traceTask.setTraceDate(LocalDateTime.now().plusDays(traceTask.getTraceType().getTraceTypeDays()));
		traceTask.setCreatedByUserId(claimCrmHeaderDTO.getCreatedByUserId());
		traceTask.setModifiedByUserId(claimCrmHeaderDTO.getModifiedByUserId());
		claimNotes.add(claimNote);
		traceTasks.add(traceTask);
		claim.setTraceTasks(traceTasks);
		return claimNotes;
	}
	
	
	private List<AuditHistory> getAuditHistory(Claim claim, String crmNumber,ClaimCrmHeaderDTO claimCrmHeaderDTO) {
		log.info("PrepareRgaServiceImpl.rgaDraftORSave.validateUserLimit.getAuditHistory");
		List<AuditHistory> auditHistories = new ArrayList<>();
		AuditHistory auditHistory = new AuditHistory();
		auditHistory.setNoteTemplate(noteTemplateRepository.findByNoteTemplateName(CommonConstant.APPROVAL_REQUIRED_CRM_TEMPLATE1));
		auditHistory.setClaim(claim);
		auditHistory.setAuditHistoryText(getRgaAuditHistoryText(auditHistory.getNoteTemplate().getAuditHistoryTemplateText(),crmNumber));
		auditHistory.setStatusId(StatusTypes.ACTIVE.getStatusId());
		auditHistory.setCreatedByUserId(claimCrmHeaderDTO.getCreatedByUserId());
		auditHistory.setModifiedByUserId(claimCrmHeaderDTO.getModifiedByUserId());
		auditHistories.add(auditHistory);
		return auditHistories;
	}
	private String getCRMClaimNoteText(String noteTemplateText, String crmNumber, int userId,ClaimCrmHeaderDTO claimCrmHeaderDTO,ClaimCrmHeader CrmHeader,TraceTask traceTask ) {
		UserGroupMappingResponseDTO userGroupMappingResponseDTO = getUserGroupList(userId,claimService.getUserGroupId(CommonConstant.CLM_USER_GROUP_CODE)).get(0);
		UsersDTO usersDTO = getUsersDetailsByUserId(userGroupMappingResponseDTO.getManagerId());
		CrmHeader.setCurrentUserId(userGroupMappingResponseDTO.getManagerId());
		traceTask.setAssignedUserId(userGroupMappingResponseDTO.getManagerId());
		return noteTemplateText
				.replace("#CRD_MEMO_NUMBER",crmNumber)
				.replace("#managername", "\"" + MessageFormat.format("{0} {1}", usersDTO.getFirstName(), usersDTO.getLastName()) + "\"");

	}
	
	private String getRgaAuditHistoryText(String auditHistoryTemplateText, String crmNumber) {
		return auditHistoryTemplateText
				.replace("#CRD_MEMO_NUMBER",crmNumber);
	}
	
	private List<UserGroupMappingResponseDTO> getUserGroupList(int userId, int userGroupId) {
		log.info("PrepareRgaServiceImpl.getUserGroupList");
		String url = "/users/v1/userGroupByUserAndUserGroupID?userId=" + userId + "&userGroupId=" + userGroupId;
		String finalUrl = restSecurityUrl + url;
		log.info("final url  :: " + finalUrl);
		ResponseEntity<List<UserGroupMappingResponseDTO>> responseEntity = restTemplate.exchange(
				finalUrl,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<UserGroupMappingResponseDTO>>() {
				});
		List<UserGroupMappingResponseDTO> response = responseEntity.getBody();
		if (response == null) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found with this userId :: " + userId + " And UserGroupId :: ");
		}
		return response;
	}
}
