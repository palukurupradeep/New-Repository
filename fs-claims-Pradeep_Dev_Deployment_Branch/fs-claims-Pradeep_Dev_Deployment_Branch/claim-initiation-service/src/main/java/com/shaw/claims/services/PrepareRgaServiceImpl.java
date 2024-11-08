package com.shaw.claims.services;


import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.dto.*;
import com.shaw.claims.model.*;
import com.shaw.claims.repo.*;
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
import org.springframework.web.client.RestTemplate;

import com.shaw.claims.enums.StatusTypes;
import com.shaw.claims.exception.CommonException;

import jakarta.transaction.Transactional;


@Service
public class PrepareRgaServiceImpl implements RgaService {
	Logger log = LogManager.getLogger(PrepareRgaServiceImpl.class);

	@Autowired
	private ClaimRepository claimRepository;
	@Autowired
	ClaimReasonDefinitionRepository claimReasonDefinitionRepository;
	@Autowired
	ClaimRgaLineRepository claimRgaLineRepository;
	@Autowired
	ClaimRgaHeaderRepository claimRgaHeaderRepository;
	@Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;
	@Autowired
	RgaStatusRepository rgaStatusRepository;
	@Autowired
	AddressTypeRepository addressTypeRepository;
	@Autowired
	private RestTemplate restTemplate;
	@Value(("${claim-security.rest.url}"))
	private String restSecurityUrl;
	

	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	AuditHistoryRepository auditHistoryRepo;

	@Autowired
	NoteTemplateRepository noteTemplateRepository;
	
	@Autowired
	LookupRepository lookupRepository;
	@Autowired
	private ClaimServiceImpl claimService;
	@Autowired
	TraceTaskRepository traceTaskRepository;
	@Autowired
	private UserApprovalLimitRepository userApprovalLimitRepository;
	@Autowired
	private ApprovalLimitTypeRepository approvalLimitTypeRepository;
	@Autowired
	TraceTypeRepository traceTypeRepository;
	
	@Autowired
	WorkQueueServiceImpl workQueueServiceImpl;
	
	@Autowired
	RgaStatusRepository rgaStatusRepo;
	
	@Autowired
	NoteTypeRepository noteTypeRepository;

	@Autowired
	ClaimNoteRepository claimNoteRepository;
	@Autowired
	ClaimLineDetailsRepository claimLineDetailsRepository;
	@Autowired
	WorkStatusRepository workStatusRepository;
	@Autowired
	ServiceToKeepTypeRepository serviceToKeepTypeRepository; 
	@Autowired
	ClaimDocumentRepository claimDocumentRepository;
	@Autowired
	SellingCompanyRepository sellingCompanyRepository;
	@Autowired
	ClaimAddressRepository claimAddressRepository;
	@Autowired
	CrmServiceImpl crmServiceImpl;
	@Autowired
	ClaimAreaRepository claimAreaRepository;
	@Autowired
	CountryRepository countryRepository;
	@Autowired
	StateRepository stateRepository;
	@Autowired
	private NoteGroupRepository noteGroupRepository;

	@Override
	@Transactional
	public RgaIssueResponseDTO rgaDraftORSave(ClaimRgaHeaderDTO claimRgaHeaderDTO) {
		Optional<ClaimRgaHeader> claimRgaHeaderData = claimRgaHeaderRepository
				.findById(claimRgaHeaderDTO.getClaimRgaHeaderId());
		ClaimRgaHeader claimRgaHeader = new ClaimRgaHeader();
		RgaIssueResponseDTO rgaIssueResponseDTO=new RgaIssueResponseDTO();
		RgaAddress rgaAddress = new RgaAddress();
		String rgaNumberData="";
		Claim claim = findByClaimId(claimRgaHeaderDTO.getClaimId());
		if (claimRgaHeaderData.isPresent()) {
			issueRGAValidation(claimRgaHeaderDTO.getClaimRgaHeaderId());
			claimRgaHeader = claimRgaHeaderData.get();
			rgaNumberData=claimRgaHeader.getRgaNumber();
		}
		else {
			 String rgaNumber = String.format("%06d", claimRgaHeaderRepository.getNextRgaNumber());
				claimRgaHeader.setRgaNumber(rgaNumber);
				rgaNumberData=rgaNumber;
				ClaimDocument claimDocumentData =crmServiceImpl.getClaimDocument(claim, claimRgaHeaderDTO.getAmountUsd(), claimRgaHeaderDTO.getModifiedByUserId(), claimRgaHeaderDTO.getCreatedByUserId(), claimRgaHeaderDTO.getClaimReasonCode(),claimRgaHeader.getRgaNumber(), "RET");
				if(claimDocumentData != null) {
				 claimDocumentRepository.save(claimDocumentData);
				}
		}
		setRgaAddress(rgaAddress, claim, claimRgaHeaderDTO);
		if(claimRgaHeaderDTO.getRgaStatusCode().equalsIgnoreCase("I")) {
			claim.setWorkStatusId(3);
		}
		validateUserLimit(claimRgaHeaderDTO,claim,rgaNumberData);
		claimRgaHeader.setCustomerNumber(claimRgaHeaderDTO.getCustomerNumber());
		claimRgaHeader.setClaimReasonDefinition(
				claimReasonDefinitionRepository.findByClaimReasonCode(claimRgaHeaderDTO.getClaimReasonCode()));
		claimRgaHeader.setAmountUsd(claimRgaHeaderDTO.getAmountUsd());
		claimRgaHeader.setClaimAmountForeign(claimRgaHeaderDTO.getClaimAmountForeign());
		claimRgaHeader.setRgaStatusId(
				rgaStatusRepository.findByRgaStatusCode(claimRgaHeaderDTO.getRgaStatusCode()).getRgaStatusId());
		claimRgaHeader.setCurrentUserId(claimRgaHeaderDTO.getCreatedByUserId());
		claimRgaHeader.setPreparedByUserId(claimRgaHeaderDTO.getCreatedByUserId());
		claimRgaHeader.setCreatedByUserId(claimRgaHeaderDTO.getCreatedByUserId());
		claimRgaHeader.setModifiedByUserId(claimRgaHeaderDTO.getModifiedByUserId());
		claimRgaHeader.setIssuedByUserId(claimRgaHeaderDTO.getIssuedByUserId());
		claimRgaHeader.setIssuedDate(LocalDateTime.now());
		List<ClaimRgaLine> rgaLinesListData = getClaimRgaLines(claim, claimRgaHeaderDTO.getClaimRgaLineDTO(),	claimRgaHeader, claimRgaHeaderDTO.getRgaStatusCode());
		claimRgaHeader.setClaimRgaLine(rgaLinesListData);
		rgaAddress.setClaimRgaHeader(claimRgaHeader);
		claimRgaHeader.setRgaAddress(rgaAddress);
		claimRgaHeader.setClaim(claim);
		ClaimRgaHeader claimRgaHeaderResponse=claimRgaHeaderRepository.save(claimRgaHeader);
		if (null != claimRgaHeaderDTO.getComments() && !claimRgaHeaderDTO.getComments().isEmpty()) {

			ClaimNote claimNote = getClaimNote(claim, claimRgaHeaderDTO.getComments());
			claimNoteRepository.save(claimNote);

		}
		rgaIssueResponseDTO.setRgaNumber(rgaNumberData);
		rgaIssueResponseDTO.setRgaHeaderId(claimRgaHeaderResponse.getClaimRgaHeaderId());
		return rgaIssueResponseDTO;
	}

	private void validateUserLimit(ClaimRgaHeaderDTO claimRgaHeaderDTO, Claim claim, String rgaNumber) {
		log.info("PrepareRgaServiceImpl.rgaDraftORSave.validateUserLimit");
		int approvalLimitTypeId = approvalLimitTypeRepository.findApprovalLimitTypeIdByApprovalLimitTypeCode(CommonConstant.RGA_APPROVAL_LIMIT_TYPE);
		BigDecimal userLimit = userApprovalLimitRepository.findApprovalLimitByUserIdAndApprovalLimitTypeId(claim.getClaimUserId(),approvalLimitTypeId);
		if(userLimit.compareTo(claimRgaHeaderDTO.getAmountUsd()) < 0 && !claimRgaHeaderDTO.getClaimReasonCode().equalsIgnoreCase("L00")){
			claim.setClaimRoutes(getRoute(claim,claimRgaHeaderDTO));
			claim.setClaimNotes(getNotesAndTask(claim,claimRgaHeaderDTO,rgaNumber));
			claim.setAuditHistory(getAuditHistory(claim,rgaNumber,claimRgaHeaderDTO));
			claim.setWorkStatusId(workStatusRepository.findWorkStatusIdByWorkStatusCode(CommonConstant.RGA_WORK_STATUS_CODE));
		}
	}

	private List<AuditHistory> getAuditHistory(Claim claim, String rgaNumber,ClaimRgaHeaderDTO claimRgaHeaderDTO) {
		log.info("PrepareRgaServiceImpl.rgaDraftORSave.validateUserLimit.getAuditHistory");
		List<AuditHistory> auditHistories = new ArrayList<>();
		AuditHistory auditHistory = new AuditHistory();
		auditHistory.setNoteTemplate(noteTemplateRepository.findByNoteTemplateName(CommonConstant.ISSUE_RGA_NOTE_TEMPLATE_NAME));
		auditHistory.setClaim(claim);
		auditHistory.setAuditHistoryText(getRgaAuditHistoryText(auditHistory.getNoteTemplate().getAuditHistoryTemplateText(),rgaNumber));
		auditHistory.setStatusId(StatusTypes.ACTIVE.getStatusId());
		auditHistory.setCreatedByUserId(claimRgaHeaderDTO.getCreatedByUserId());
		auditHistory.setModifiedByUserId(claimRgaHeaderDTO.getModifiedByUserId());
		auditHistories.add(auditHistory);
		return auditHistories;
	}

	private String getRgaAuditHistoryText(String auditHistoryTemplateText, String rgaNumber) {
		return auditHistoryTemplateText
				.replace("#RGANo",rgaNumber);
	}

	private List<ClaimNote> getNotesAndTask(Claim claim, ClaimRgaHeaderDTO claimRgaHeaderDTO, String rgaNumber) {
		log.info("PrepareRgaServiceImpl.rgaDraftORSave.validateUserLimit.setNotesAndTask");
		List<ClaimNote> claimNotes = new ArrayList<>();
		List<TraceTask> traceTasks = new ArrayList<>();
		ClaimNote claimNote = new ClaimNote();
		TraceTask traceTask = new TraceTask();
		claimNote.setNoteType(noteTypeRepository.fetchNoteTypeByNoteTypeCode(CommonConstant.ISSUE_RGA_NOTE_TYPE_CODE));
		claimNote.setNoteGroup(noteGroupRepository.findByNoteGroupCode(claimNote.getNoteType().getNoteGroup().getNoteGroupCode()));
		claimNote.setNoteTemplate(noteTemplateRepository.findByNoteTemplateName(CommonConstant.ISSUE_RGA_NOTE_TEMPLATE_NAME));
		claimNote.setClaim(claim);
		claimNote.setLookup(lookupRepository.findByLookupCode("P"));
		claimNote.setCallLog(false);
		claimNote.setClaimNoteText(getRGAClaimNoteText(claimNote.getNoteTemplate().getNoteTemplateText(),rgaNumber,claim.getClaimUserId(),claimRgaHeaderDTO));
		claimNote.setStatusId(StatusTypes.ACTIVE.getStatusId());
		claimNote.setCreatedByUserId(claimRgaHeaderDTO.getCreatedByUserId());
		claimNote.setModifiedByUserId(claimRgaHeaderDTO.getModifiedByUserId());
		traceTask.setClaimNote(claimNote);
		traceTask.setTraceType(traceTypeRepository.findByTraceTypeCode(CommonConstant.RETURN_AUTHORIZATION_ISSUED));
		traceTask.setClaim(claim);
		traceTask.setAssignedUserId(claim.getClaimUserId());
		traceTask.setTraceActionLink("");
		traceTask.setTraceReference("");
		traceTask.setStatusId(StatusTypes.ACTIVE.getStatusId());
		traceTask.setTraceDate(LocalDateTime.now().plusDays(traceTask.getTraceType().getTraceTypeDays()));
		traceTask.setCreatedByUserId(claimRgaHeaderDTO.getCreatedByUserId());
		traceTask.setModifiedByUserId(claimRgaHeaderDTO.getModifiedByUserId());
		claimNotes.add(claimNote);
		traceTasks.add(traceTask);
		claim.setTraceTasks(traceTasks);
		return claimNotes;
	}

	private String getRGAClaimNoteText(String noteTemplateText, String rgaNumber, int userId, ClaimRgaHeaderDTO claimRgaHeaderDTO) {
		UserGroupMappingResponseDTO userGroupMappingResponseDTO = getUserGroupList(userId,claimService.getUserGroupId(CommonConstant.CLM_USER_GROUP_CODE)).get(0);
		UsersDTO usersDTO = crmServiceImpl.getUsersDetailsByUserId(userGroupMappingResponseDTO.getManagerId());
		claimRgaHeaderDTO.setCurrentUserId(userGroupMappingResponseDTO.getManagerId());
		
		return noteTemplateText
				.replace("#RGANo",rgaNumber)
				.replace("#managername", "\"" + MessageFormat.format("{0} {1}", usersDTO.getFirstName(), usersDTO.getLastName()) + "\"");

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

	private List<ClaimRoute> getRoute(Claim claim,ClaimRgaHeaderDTO claimRgaHeaderDTO) {
		log.info("PrepareRgaServiceImpl.rgaDraftORSave.validateUserLimit.setRoute");
		List<ClaimRoute> claimRoutes = new ArrayList<>();
		ClaimRoute claimRoute = new ClaimRoute();
		claimRoute.setClaim(claim);
		claimRoute.setRoutedToUserId(claim.getClaimUserId());
		claimRoute.setRoutedToUserGroupId(claimService.getUserGroupId(CommonConstant.RGA_USER_GROUP_CODE));
		claimRoute.setStatusId(StatusTypes.ACTIVE.getStatusId());
		claimRoute.setCreatedByUserId(claimRgaHeaderDTO.getCreatedByUserId());
		claimRoute.setModifiedByUserId(claimRgaHeaderDTO.getModifiedByUserId());
		claimRoutes.add(claimRoute);
		return claimRoutes;
	}

	public List<ClaimRgaLine> getClaimRgaLines(Claim claim, List<ClaimRgaLineDTO> claimRgaLineDTO, ClaimRgaHeader claimRgaHeader,String rgaStatusCode) {
		List<ClaimRgaLine> claimRgaLine = null;
		if (claimRgaLineDTO != null) {
			claimRgaLine = new ArrayList<>();
			for (ClaimRgaLineDTO claimRgaLinedto : claimRgaLineDTO) {
				claimRgaLine.add(getClaimRgaLinesDetails(claim, claimRgaLinedto, claimRgaHeader,rgaStatusCode));
			}

		}
		return claimRgaLine;
    }

    private ClaimRgaLine getClaimRgaLinesDetails(Claim claim, ClaimRgaLineDTO claimRgaLineDTO, ClaimRgaHeader claimRgaHeader,String rgaStatusCode) {
    	ClaimRgaLine claimRgaLine = new ClaimRgaLine();
    	if(claimRgaLineDTO.getClaimRgaLineId()==0)
    	{
    	List<String> pendingRgaNumbersList=claimRgaLineRepository.findPendingRgaNumbers(claimRgaLineDTO.getClaimLineId());
    	if(null!=pendingRgaNumbersList && pendingRgaNumbersList.size()>0 && !pendingRgaNumbersList.contains(claimRgaHeader.getRgaNumber()))
    	{
    		throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"This line have active RGA Numbers:: "+pendingRgaNumbersList);
    	}
    	}
    	if(claimRgaLineDTO.getClaimRgaLineId()!=0)
    	{
    		claimRgaLine.setClaimRgaLineId(claimRgaLineDTO.getClaimRgaLineId());
    	}
    	UnitOfMeasure unitOfMeasure=new UnitOfMeasure();
    	RgaStatus rgaStatus=new RgaStatus();
    	ClaimLineDetail claimLineDetail=new ClaimLineDetail();
    	claimLineDetail.setClaimLineId(claimRgaLineDTO.getClaimLineId());
    //	claimLineDetail.setClaimDocument(claimDocument);
        unitOfMeasure.setUnitOfMeasureId(unitOfMeasureRepository.findByUnitOfMeasureCode(claimRgaLineDTO.getUnitOfMeasureCode()).getUnitOfMeasureId());
        rgaStatus.setRgaStatusId(rgaStatusRepository.findByRgaStatusCode(rgaStatusCode).getRgaStatusId());
        claimRgaLine.setClaimLineDetail(claimLineDetail);
        claimRgaLine.setClaim(claim);
        claimRgaLine.setUnitOfMeasure(unitOfMeasure);
        claimRgaLine.setClaimRgaHeader(claimRgaHeader);
        claimRgaLine.setCreatedByUserId(claimRgaHeader.getCreatedByUserId());
        claimRgaLine.setModifiedByUserId(claimRgaHeader.getModifiedByUserId());
        claimRgaLine.setRgaStatus(rgaStatus);
        claimRgaLine.setRgaLineNumber(claimRgaLineDTO.getRgaLineNumber());
        claimRgaLine.setRollNumber(claimRgaLineDTO.getRollNumber());
        claimRgaLine.setStyleNumber(claimRgaLineDTO.getStyleNumber());
        claimRgaLine.setColorNumber(claimRgaLineDTO.getColorNumber());
        claimRgaLine.setIssuedLengthInFeet(claimRgaLineDTO.getIssuedLengthInFeet());
        claimRgaLine.setIssuedWidthInFeet(claimRgaLineDTO.getIssuedWidthInFeet());
        claimRgaLine.setIssuedWidthInInches(claimRgaLineDTO.getIssuedWidthInInches());
        claimRgaLine.setIssuedLengthInInches(claimRgaLineDTO.getIssuedLengthInInches());
        claimRgaLine.setIssuedQuantity(claimRgaLineDTO.getIssuedQuantity());
        claimRgaLine.setIssuedAmount(claimRgaLineDTO.getIssuedAmount());
        claimRgaLine.setIssuedSquareFeet(claimRgaLineDTO.getIssuedSquareFeet());
        claimRgaLine.setIssuedSquareYards(claimRgaLineDTO.getIssuedSquareYards());
        claimRgaLine.setIssuedQuantity(claimRgaLineDTO.getIssuedQuantity());
        claimRgaLine.setRdcLocationId(claimRgaLineDTO.getRdcLocationId());
        claimRgaLine.setReceivedQuantity(claimRgaLineDTO.getReceivedQuantity());
        claimRgaLine.setReceivedInFull(claimRgaLineDTO.getReceivedInFull());
        claimRgaLine.setIsPrepaid(claimRgaLineDTO.getIsPrepaid());
        if(claimRgaLineDTO.getFreightDate()!=null)
         claimRgaLine.setFreightDate(claimRgaLineDTO.getFreightDate());
        if(claimRgaLineDTO.getReceivedDate()!=null)
        	claimRgaLine.setReceivedDate(claimRgaLineDTO.getReceivedDate());
        claimRgaLine.setFreightBillNumber(claimRgaLineDTO.getFreightBillNumber());
        claimRgaLine.setFreightAmount(claimRgaLineDTO.getFreightAmount());
        claimRgaLine.setBinLocation(claimRgaLineDTO.getBinLocation());
        return claimRgaLine;
    }
    
    private void setRgaAddress(RgaAddress rgaAddress, Claim claim,ClaimRgaHeaderDTO claimRgaHeaderDTO) {
    	BeanUtils.copyProperties(claimRgaHeaderDTO.getRgaAddressDTO(), rgaAddress);
		rgaAddress.setClaim(claim);
    	rgaAddress.setAddressTypeId(addressTypeRepository.findAddressTypeIdByAddressTypeCode("PKP"));
		rgaAddress.setCreatedByUserId(claimRgaHeaderDTO.getCreatedByUserId());
		rgaAddress.setModifiedByUserId(claimRgaHeaderDTO.getModifiedByUserId());
		rgaAddress.setStatusId(StatusTypes.ACTIVE.getStatusId());
    }
	
	private Claim findByClaimId(Integer claimId){
		Optional<Claim> claim = claimRepository.findById(claimId);
		if (!claim.isPresent()) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found In This ClaimId :: " + claimId);
		}
		return claim.get();
	}
	
	@Override
	public List<ClaimRgaHeaderDTO> fetchRgaDetails(int claimId) {
		List<ClaimRgaHeader> claimRgaHeaderData = claimRgaHeaderRepository.findClaimRgaHeaderByClaimId(claimId);
		if (claimRgaHeaderData.size()>0) {

			return getRgaHeaderDetails(claimRgaHeaderData);
		} else

			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"RGA Not Found With This ClaimId :: " + claimId);
	}

	@Override
	public UpdateRGAStatusResponseDTO updateRGAStatusByAction(int claimRgaHeaderId, String action, int loggedInUserId,String noteText) {
		UpdateRGAStatusResponseDTO response = null;
		switch (action) {
		case "Decline":
			response = updateRGAStatus(claimRgaHeaderId, "D", loggedInUserId,noteText);
			break;
		case "Close":
			response = updateRGAStatus(claimRgaHeaderId, "C", loggedInUserId,"");
			break;
		case "Void":
			response = updateRGAStatus(claimRgaHeaderId, "V", loggedInUserId,noteText);
			break;
		case "Redeliver":
			response = redeliverRGA(claimRgaHeaderId, loggedInUserId);
			break;
		case "Reopen":
			response = reopneRGA(claimRgaHeaderId, loggedInUserId);
			break;
		}
		return response;
	}


	@Override
	public List<ClaimRgaReturnReceiptProcessResponseDTO> returnReceiptProcess(String locationCode, String rgaNumber) {
		log.info("PrepareRgaServiceImpl.returnReceiptProcess");
		List<ClaimRgaHeader> claimRgaHeaderList;
		int locationId = locationRepository.findLocationIdByLocationCode(locationCode);
		if(rgaNumber.isEmpty() || rgaNumber.isBlank()){
			 claimRgaHeaderList = claimRgaHeaderRepository.findByLocationId(locationId);

		}else{
			 claimRgaHeaderList = claimRgaHeaderRepository.findByLocationIdAndRgaNumber(locationId,rgaNumber);
		}
		return setClaimRgaReturnReceiptProcessResponseDTOS(claimRgaHeaderList);
	}

	private List<ClaimRgaReturnReceiptProcessResponseDTO> setClaimRgaReturnReceiptProcessResponseDTOS(List<ClaimRgaHeader> claimRgaHeaderList) {
		return claimRgaHeaderList.stream()
				.map(claimRgaHeader -> {
					ClaimRgaReturnReceiptProcessResponseDTO claimRgaReturnReceiptProcessResponseDTO = new ClaimRgaReturnReceiptProcessResponseDTO();
					List<ClaimRgaLine> claimRgaLines = claimRgaLineRepository.findByClaimRgaHeaderId(claimRgaHeader.getClaimRgaHeaderId());
					claimRgaReturnReceiptProcessResponseDTO.setRgaNumber(claimRgaHeader.getRgaNumber());
					claimRgaReturnReceiptProcessResponseDTO.setDate(claimRgaHeader.getModifiedDateTime());
					claimRgaReturnReceiptProcessResponseDTO.setStatus(rgaStatusRepository.findById(claimRgaHeader.getRgaStatusId()).get().getRgaStatusDescription());
					claimRgaReturnReceiptProcessResponseDTO.setOpenQuantity(getOpenQuantity(claimRgaLines));
					claimRgaReturnReceiptProcessResponseDTO.setReceivedQuantity(getReceivedQuantity(claimRgaLines));
					return claimRgaReturnReceiptProcessResponseDTO;
				}).collect(Collectors.toList());
	}

	private BigDecimal getReceivedQuantity(List<ClaimRgaLine> claimRgaLines) {
		return claimRgaLines.stream()
				.map(ClaimRgaLine::getReceivedQuantity)
				.reduce(BigDecimal.ZERO,BigDecimal::add);
	}

	private BigDecimal getOpenQuantity(List<ClaimRgaLine> claimRgaLines) {
		return claimRgaLines.stream()
				.map(ClaimRgaLine::getIssuedQuantity)
				.reduce(BigDecimal.ZERO,BigDecimal::add);
	}


	private UpdateRGAStatusResponseDTO reopneRGA(int claimRgaHeaderId, int loggedInUserId) {
		Optional<ClaimRgaHeader> claimRgaHeaderData = claimRgaHeaderRepository.findById(claimRgaHeaderId);
		UpdateRGAStatusResponseDTO response = null;
		if (claimRgaHeaderData.isPresent()) {
			if(claimRgaHeaderData.get().getRgaStatusId() == 5) {
				claimRgaHeaderData.get().setRgaStatusId(rgaStatusRepository.findByRgaStatusCode("I").getRgaStatusId());
				RgaStatus rgaStatus = rgaStatusRepository.findByRgaStatusCode("I");
				claimRgaHeaderData.get().getClaimRgaLine().stream().forEach(r -> r.setRgaStatus(rgaStatus));
				if(claimRgaHeaderData.get().getClaim().getClaimStatusId() == 2) {
					claimRgaHeaderData.get().getClaim().setClaimStatusId(6);
				}
				claimRgaHeaderData.get().setModifiedByUserId(loggedInUserId);
				claimRgaHeaderRepository.save(claimRgaHeaderData.get());
				response = setSuccessMessage(claimRgaHeaderId, rgaStatus.getRgaStatusDescription());
			}
			
		} else {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"RGA Not Found With This claimRgaHeaderId :: " + claimRgaHeaderId);
		}
		return response;
	}

	private UpdateRGAStatusResponseDTO redeliverRGA(int claimRgaHeaderId, int loggedInUserId) {
		sendCommunicationNotification(claimRgaHeaderId);
		return updateRGAStatus(claimRgaHeaderId, "RD", loggedInUserId,"");
		
	}

	private void sendCommunicationNotification(int claimRgaHeaderId) {
		//need to implemenet after notification api completed
	}
	
	public UpdateRGAStatusResponseDTO updateRGAStatus(int claimRgaHeaderId, String status, int loggedInUserId,String noteText) {
		Optional<ClaimRgaHeader> claimRgaHeaderData = claimRgaHeaderRepository.findById(claimRgaHeaderId);
		UpdateRGAStatusResponseDTO response ;
		if (claimRgaHeaderData.isPresent()) {
			claimRgaHeaderData.get().setRgaStatusId(rgaStatusRepository.findByRgaStatusCode(status).getRgaStatusId());
			RgaStatus rgaStatus = rgaStatusRepository.findByRgaStatusCode(status);
			claimRgaHeaderData.get().getClaimRgaLine().stream().forEach(r -> r.setRgaStatus(rgaStatus));
			claimRgaHeaderData.get().setModifiedByUserId(loggedInUserId);
			claimRgaHeaderRepository.save(claimRgaHeaderData.get());
			if(!noteText.isEmpty())
			{
				Claim claim=claimRgaHeaderData.get().getClaim();
				ClaimNote claimNote= getClaimNote(claim,  noteText,null);
				claim.getClaimNotes().add(claimNote);
				claimRepository.save(claim);
			}
			
			
			response = setSuccessMessage(claimRgaHeaderId, rgaStatus.getRgaStatusDescription());
		} else {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"RGA Not Found With This claimRgaHeaderId :: " + claimRgaHeaderId);
		}
		return response;
	}


	private UpdateRGAStatusResponseDTO setSuccessMessage(int claimRgaHeaderId, String status) {
		return new UpdateRGAStatusResponseDTO(claimRgaHeaderId, status + " status updated successfully");
	}

	@Override
	public void issueRGAValidation(int claimRgaHeaderId) {
		Optional<ClaimRgaHeader> claimRgaHeaderData = claimRgaHeaderRepository.findById(claimRgaHeaderId);
		if(claimRgaHeaderData.isPresent()) {
			UserApprovalLimitDTO approvalLimit = findUserApprovalLimit(claimRgaHeaderData.get().getPreparedByUserId(), "RETAUTH");
			if(approvalLimit != null) {
				if((BigDecimal.valueOf(approvalLimit.getApprovalLimit()).compareTo(claimRgaHeaderData.get().getAmountUsd()) < 0) && !claimRgaHeaderData.get().getClaimReasonDefinition().getClaimReasonCode().equalsIgnoreCase("L00")) {
					setPriorityNoteText(claimRgaHeaderData.get().getClaim(), claimRgaHeaderData.get().getRgaNumber() + "", claimRgaHeaderData.get().getRgaNumber()+"", "RGA Template 1", "#RGANo",  claimRgaHeaderData.get().getPreparedByUserId());
				}
			}
		}
	}
	
	 public UserApprovalLimitDTO findUserApprovalLimit(int userId, String approvalLimitType) {
	        String url = "/users/v1/getApprovalLimitByUserId?userId=" + userId + "&approvalLimitType="+approvalLimitType;
	        String finalUrl = restSecurityUrl + url;
	        log.info("Final Security Url :: " + finalUrl);
	        ResponseEntity<UserApprovalLimitDTO> responseEntity = restTemplate.exchange(
	                finalUrl,
	                HttpMethod.GET,
	                null,
	                new ParameterizedTypeReference<UserApprovalLimitDTO>() {});
	        UserApprovalLimitDTO response = responseEntity.getBody();
	        if (response == null) {
	            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
	                    "Record Not Found with userId :: " + userId);
	        }
	        return response;
	    }
	 public void setPriorityNoteText(Claim claim, String noteText,String auditNoteText, String templateName, String auditHistoryTemplateName, Integer userId) {
			NoteTemplate noteTemplate = saveClaimNote(claim, noteText, templateName, userId);
			saveAuditHistory(claim, noteTemplate, auditHistoryTemplateName, auditNoteText, userId);
		}

		private NoteTemplate saveClaimNote(Claim claim, String noteText, String templateName, Integer userId) {
			ClaimNote claimNote = new ClaimNote();
			NoteTemplate noteTemplate = noteTemplateRepository.findByNoteTemplateName(templateName);
			//noteTemplate.getNoteType().setNoteTypeId(note);
			claimNote.setNoteType(noteTemplate.getNoteType());
			claimNote.setNoteGroup(noteTemplate.getNoteGroup());
			claimNote.setNoteTemplate(noteTemplate);
			claimNote.setClaim(claim);
			//UsersDTO userDTO = workQueueServiceImpl.getUsersDetailsByUserId(userId);
			UserGroupMappingDTO userDTO = getManagerDetailsByUserId(userId);
			String managerName = "";
			if(userDTO != null) {
				//managerName = (((!userDTO.getFirstName().isEmpty()) ?  (userDTO.getFirstName() +" ") : "") + userDTO.getLastName());
				managerName=userDTO.getUserGroupDTO().get(0).getManagerName();
			}
			//below one need to clarify from Swati/Rasheed
			claimNote.setLookup(lookupRepository.findByLookupCode("P"));
			claimNote.setStatusId(1);
			if(noteTemplate.getNoteTemplateText().contains("#") && noteText != null) {
				claimNote.setClaimNoteText(noteTemplate.getNoteTemplateText().replaceAll("#RGANo", noteText).replaceAll("#managername", managerName));
			}else {
				claimNote.setClaimNoteText(noteTemplate.getNoteTemplateText());
			}
			claimNote.setCreatedByUserId(userId);
			claimNote.setModifiedByUserId(userId);
			
			if(templateName.equalsIgnoreCase("RGA Template 1")) {
				TraceTask traceTask = new TraceTask();
				NoteAndTaskRequestDTO noteAndTaskRequestDTO = new NoteAndTaskRequestDTO();
				noteAndTaskRequestDTO.setTraceTypeId(48);
				noteAndTaskRequestDTO.setClaimId(claim.getClaimId());
				noteAndTaskRequestDTO.setCreatedByUserId(userId);
				noteAndTaskRequestDTO.setModifiedByUserId(userId);
				if(userDTO != null) {
					noteAndTaskRequestDTO.setTaskAssignedUserId(userDTO.getUserGroupDTO().get(0).getManagerId());
					}else
					{
						noteAndTaskRequestDTO.setTaskAssignedUserId(userId);
					}
				setTraceTask(noteAndTaskRequestDTO, claimNote, traceTask);
			}
			
			claim.getClaimNotes().add(claimNote);
			claimRepository.save(claim);
			
			return noteTemplate;
		}

		private void saveAuditHistory(Claim claim, NoteTemplate noteTemplate, String auditHistoryTemplateName,
				String noteText, Integer userId) {
			AuditHistory auditHistory = new AuditHistory();
			auditHistory.setClaim(claim);
			auditHistory.setNoteTemplate(noteTemplate);
			auditHistory.setStatusId(StatusTypes.ACTIVE.getStatusId());
			auditHistory.setCreatedByUserId(claim.getCreatedByUserId());
			auditHistory.setModifiedByUserId(claim.getModifiedByUserId());
			if(noteText != null) {
				String auditText = noteTemplate.getAuditHistoryTemplateText().replace(auditHistoryTemplateName, noteText);
				auditHistory.setAuditHistoryText(auditText);
			}else {
				auditHistory.setAuditHistoryText(noteTemplate.getAuditHistoryTemplateText());
			}
			auditHistory.setCreatedByUserId(userId);
			auditHistory.setModifiedByUserId(userId);
			auditHistoryRepo.save(auditHistory);
		}
		
		public void setTraceTask(NoteAndTaskRequestDTO noteAndTaskRequestDTO, ClaimNote claimNote, TraceTask traceTask) {
	        if(noteAndTaskRequestDTO.getTraceTypeId() != 0) {
	            //BeanUtils.copyProperties(noteAndTaskRequestDTO, traceTask);
	            traceTask.setClaimNote(claimNote);
	            traceTask.setTraceType(traceTypeRepository.findById(noteAndTaskRequestDTO.getTraceTypeId()).get());
	            traceTask.setClaim(claimNote.getClaim());
	            traceTask.setStatusId(StatusTypes.ACTIVE.getStatusId());
	           // traceTask.setAssignedUserId(noteAndTaskRequestDTO.getCreatedByUserId());
	            traceTask.setAssignedUserId(noteAndTaskRequestDTO.getTaskAssignedUserId());
	            traceTask.setCreatedByUserId(noteAndTaskRequestDTO.getCreatedByUserId());
	            traceTask.setModifiedByUserId(noteAndTaskRequestDTO.getModifiedByUserId());
	            traceTask.setTraceDate(LocalDateTime.now().plusDays(traceTask.getTraceType().getTraceTypeDays()));
	            traceTask.setTraceActionLink("");
	            traceTask.setTraceReference("");
	            claimNote.setTraceTask(traceTask);
	        }
	    }

	
	
	private List<ClaimRgaHeaderDTO> getRgaHeaderDetails(List<ClaimRgaHeader> claimRgaHeaderList) {
		List<ClaimRgaHeaderDTO> rgaHeaderDtoList = new ArrayList<>();
		for (ClaimRgaHeader claimRgaHeader : claimRgaHeaderList) {

			ClaimRgaHeaderDTO rgaHeaderDto = new ClaimRgaHeaderDTO();
			RgaAddressDTO rgaAddressDTO = new RgaAddressDTO();
			BeanUtils.copyProperties(claimRgaHeader, rgaHeaderDto);
			rgaHeaderDto.setClaimId(claimRgaHeader.getClaim().getClaimId());
			if(claimRgaHeader.getClaim().getEndUserInformation() != null) {
				rgaHeaderDto.setEndUserName(claimRgaHeader.getClaim().getEndUserInformation().getFirstName() + " " + claimRgaHeader.getClaim().getEndUserInformation().getLastName());
			}
			List<ClaimAddress> claimAddressList = claimAddressRepository.findByClaim_ClaimId(claimRgaHeader.getClaim().getClaimId()+"");
			if(claimAddressList != null && !claimAddressList.isEmpty()) {
				claimAddressList.stream().forEach(data -> {
					if(data.getAddressTypeId() == 6) {
						rgaHeaderDto.setPhoneNumber(data.getCellPhoneNumber());
						rgaHeaderDto.setFaxNumber(data.getFaxNumber());
						rgaHeaderDto.setEmail(data.getEmailAddress());
						rgaHeaderDto.setAttentionName(data.getFirstName() + (data.getLastName() != null ? " " + data.getLastName() : ""));
					}
				});
			}
			
			
		
			rgaHeaderDto.setClaimReasonCode(claimRgaHeader.getClaimReasonDefinition().getClaimReasonCode());
			rgaHeaderDto.setReasonDesc(claimRgaHeader.getClaimReasonDefinition().getClaimReasonDescription());
			rgaHeaderDto.setRgaStatusCode(
					rgaStatusRepository.findById(claimRgaHeader.getRgaStatusId()).get().getRgaStatusCode());
			
			BeanUtils.copyProperties(claimRgaHeader.getRgaAddress(), rgaAddressDTO);
			List<ClaimRgaLineDTO> ls = new ArrayList<>();
			for (ClaimRgaLine rgaLine : claimRgaHeader.getClaimRgaLine()) {
				ClaimRgaLineDTO claimRgaLineDTO = new ClaimRgaLineDTO();
			 Optional<ClaimLineDetail> claimLineDetail=claimLineDetailsRepository.findById(rgaLine.getClaimLineDetail().getClaimLineId());
				BeanUtils.copyProperties(rgaLine, claimRgaLineDTO);
				claimRgaLineDTO.setClaimLineId(rgaLine.getClaimLineDetail().getClaimLineId());
				claimRgaLineDTO.setQuantity(rgaLine.getClaimLineDetail().getQuantity());
				claimRgaLineDTO.setRgaLineNumber(rgaLine.getClaimLineDetail().getLineNumber());
				claimRgaLineDTO.setClaimLineAmountUsd(rgaLine.getClaimLineDetail().getLineAmountUsd());
				claimRgaLineDTO.setClaimLineForeignAmountUsd(rgaLine.getClaimLineDetail().getLineAmountForeign());
				claimRgaLineDTO.setUnitOfMeasureCode(rgaLine.getUnitOfMeasure().getUnitOfMeasureCode());
				if(claimLineDetail.isPresent()) {
				claimRgaLineDTO.setType(claimLineDetail.get().getRcsCodes().getRcsCode());
				claimRgaLineDTO.setInvNumber(claimLineDetail.get().getClaimDocument().getDocumentNumber());
				claimRgaLineDTO.setInvDate(claimLineDetail.get().getClaimDocument().getDocumentDate());
				claimRgaLineDTO.setBolNumber(claimLineDetail.get().getClaimDocument().getBillofLading());
				claimRgaLineDTO.setPoNumber(claimLineDetail.get().getClaimDocument().getPurchaseOrderNumber());
				claimRgaLineDTO.setOrderNumber(claimLineDetail.get().getClaimDocument().getOrderNumber());
				}
				ls.add(claimRgaLineDTO);

			}

			rgaHeaderDto.setClaimRgaLineDTO(ls);
			
	        List<ClaimNote> claimNotes=	claimNoteRepository.getClaimNotesByclaimIdAndnoteTypeId(claimRgaHeader.getClaim().getClaimId(), noteTypeRepository.fetchNoteTypeByNoteTypeCode("RETAUTH").
					getNoteTypeId());
	        String notes=claimNotes.stream().map(p->p.getClaimNoteText()).collect(Collectors.joining("|"));
	        rgaHeaderDto.setComments(notes);
			rgaAddressDTO.setAddressTypeCode(addressTypeRepository
					.findAddressTypeCodeByAddressTypeId(claimRgaHeader.getRgaAddress().getAddressTypeId()));
			rgaHeaderDto.setRgaAddressDTO(rgaAddressDTO);
			rgaHeaderDto.setSellingCompany(claimRgaHeader.getClaim().getSellingCompany());
			rgaHeaderDto.setSellingCompanyName(sellingCompanyRepository.findSellingCompnayNameBySellingCompanyCode(claimRgaHeader.getClaim().getSellingCompany()));
			rgaHeaderDto.setRegion(claimRgaHeader.getClaim().getRegion());
			rgaHeaderDto.setSalesPerson(claimRgaHeader.getClaim().getTerritoryManagerName());
			rgaHeaderDto.setDivision(claimRgaHeader.getClaim().getDivision());
			rgaHeaderDto.setClaimAreaId(claimRgaHeader.getClaim().getClaimAreaId());
			
			rgaHeaderDtoList.add(rgaHeaderDto);
		}

		return rgaHeaderDtoList;
	}

	@Override
	public ClaimRgaHeaderDTO findRgaDetailsByRgaNumber(String rgaNumber) {
		Optional<ClaimRgaHeader> claimRgaHeaderData = claimRgaHeaderRepository.findClaimRgaHeaderByRgaNumber(rgaNumber);
		if (claimRgaHeaderData.isPresent()) {
			return getRgaHeaderDetails(claimRgaHeaderData.get());
		} else
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"RGA Not Found With This RgaNumber :: " + rgaNumber);
	}

	@Override
	public void updateReceiptOfGoods(UpdateReceiptGoodsDTO updateReceiptGoodsDTO) {
		Optional<ClaimRgaHeader> claimRgaHeaderData =claimRgaHeaderRepository.findById(updateReceiptGoodsDTO.getClaimRgaHeaderId());
		if (claimRgaHeaderData.isPresent()) {
			ClaimRgaHeader claimRgaHeader=claimRgaHeaderData.get();
		List<ClaimRgaLine> rgaLinesListData = updateClaimRgaLines(updateReceiptGoodsDTO.getClaimRgaLineDTO());
		claimRgaHeader.setClaimRgaLine(rgaLinesListData);
		claimRgaHeaderRepository.save(claimRgaHeader);
	}
	else
	 throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
	 "RGA Not Found With This ClaimRgaHeaderId :: " + updateReceiptGoodsDTO.getClaimRgaHeaderId());

}

	 private List<ClaimRgaLine> updateClaimRgaLines(List<ClaimRgaLineDTO> claimRgaLineDto) {

			List<ClaimRgaLine> claimRgaLineList = new ArrayList<>();
			for (ClaimRgaLineDTO claimRgaLineData : claimRgaLineDto) {
				Optional<ClaimRgaLine> claimRgaLines = claimRgaLineRepository
						.findById(claimRgaLineData.getClaimRgaLineId());
				ClaimRgaLine claimRgaLine = claimRgaLines.get();
				claimRgaLine.setRollNumber(claimRgaLineData.getRollNumber());
				claimRgaLine.setStyleNumber(claimRgaLineData.getStyleNumber());
				claimRgaLine.setColorNumber(claimRgaLineData.getColorNumber());
				claimRgaLine.setIssuedLengthInFeet(claimRgaLineData.getIssuedLengthInFeet());
				claimRgaLine.setIssuedWidthInFeet(claimRgaLineData.getIssuedWidthInFeet());
				claimRgaLine.setIssuedWidthInInches(claimRgaLineData.getIssuedWidthInInches());
				claimRgaLine.setIssuedLengthInInches(claimRgaLineData.getIssuedLengthInInches());
				claimRgaLine.setIssuedQuantity(claimRgaLineData.getIssuedQuantity());
				claimRgaLine.setIssuedAmount(claimRgaLineData.getIssuedAmount());
				claimRgaLine.setIssuedSquareFeet(claimRgaLineData.getIssuedSquareFeet());
				claimRgaLine.setIssuedSquareYards(claimRgaLineData.getIssuedSquareYards());
				claimRgaLine.setIssuedQuantity(claimRgaLineData.getIssuedQuantity());
				claimRgaLine.setRdcLocationId(claimRgaLineData.getRdcLocationId());
				claimRgaLine.setIsPrepaid(claimRgaLineData.getIsPrepaid());
				if (claimRgaLineData.getFreightDate() != null)
					claimRgaLine.setFreightDate(claimRgaLineData.getFreightDate());
				if (claimRgaLineData.getReceivedDate() != null)
					claimRgaLine.setReceivedDate(claimRgaLineData.getReceivedDate());
				claimRgaLine.setFreightBillNumber(claimRgaLineData.getFreightBillNumber());
				claimRgaLine.setFreightAmount(claimRgaLineData.getFreightAmount());
				claimRgaLine.setBinLocation(claimRgaLineData.getBinLocation());
				claimRgaLine.setCarrierCode(claimRgaLineData.getCarrierCode());
				claimRgaLineList.add(claimRgaLine);
			}
	return claimRgaLineList;

		}
	 private ClaimNote getClaimNote(Claim claim, String comments) {
	        ClaimNote claimNote = new ClaimNote();
	        NoteType noteType = noteTypeRepository.fetchNoteTypeByNoteTypeCode("RETAUTH");
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
	public ClaimRgaHeaderDTO fetchRgaDetailsByClaimIdAndRgaNumber(int claimId, String rgaNumber) {
		Optional<ClaimRgaHeader> claimRgaHeaderData = claimRgaHeaderRepository.findClaimRgaHeaderByClaimIdAndRgaNumber(claimId,rgaNumber);
		if (claimRgaHeaderData.isPresent()) {

			return getRgaHeaderDetails(claimRgaHeaderData.get());
			
		} else
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"RGA Not Found With This ClaimId :: " + claimId);
	}
	
	private ClaimRgaHeaderDTO getRgaHeaderDetails(ClaimRgaHeader claimRgaHeader) {
	
		ClaimRgaHeaderDTO rgaHeaderDto = new ClaimRgaHeaderDTO();
		RgaAddressDTO rgaAddressDTO = new RgaAddressDTO();
		BeanUtils.copyProperties(claimRgaHeader, rgaHeaderDto);
		rgaHeaderDto.setClaimId(claimRgaHeader.getClaim().getClaimId());
		rgaHeaderDto.setClaimNumber(claimRgaHeader.getClaim().getClaimNumber());
		rgaHeaderDto.setCreditMemoDate(claimRgaHeader.getCreatedDateTime());
		rgaHeaderDto.setClaimReasonCode(claimRgaHeader.getClaimReasonDefinition().getClaimReasonCode());
		rgaHeaderDto.setPreparedByUserId(claimRgaHeader.getPreparedByUserId());
		rgaHeaderDto.setCurrentUserId(claimRgaHeader.getCurrentUserId());
		rgaHeaderDto.setClaimReasonCodeDescription(claimRgaHeader.getClaimReasonDefinition().getClaimReasonDescription());
		if(claimRgaHeader.getClaim().getSellingCompany() != null) {
			rgaHeaderDto.setSellingCompany(claimRgaHeader.getClaim().getSellingCompany());
			rgaHeaderDto.setSellingCompanyName(sellingCompanyRepository.findSellingCompnayNameBySellingCompanyCode(claimRgaHeader.getClaim().getSellingCompany()));
			SellingCompany sellingCompany=sellingCompanyRepository.findBySellingCompanyCode(claimRgaHeader.getClaim().getSellingCompany());
			if(null!=sellingCompany) {
			 rgaHeaderDto.setSellingCompanyType(sellingCompanyRepository.findBySellingCompanyCode(claimRgaHeader.getClaim().getSellingCompany()).getCompanyType());}
		}
		rgaHeaderDto.setRegion(claimRgaHeader.getClaim().getRegion());
		rgaHeaderDto.setSalesPerson(claimRgaHeader.getClaim().getTerritoryManagerName());
		rgaHeaderDto.setDivision(claimRgaHeader.getClaim().getDivision());
		rgaHeaderDto.setClaimAreaId(claimRgaHeader.getClaim().getClaimAreaId());
		ClaimArea claimArea = claimAreaRepository.findByClaimAreaId(claimRgaHeader.getClaim().getClaimAreaId());
		if(claimArea != null) {
			rgaHeaderDto.setClaimAreaCode(claimArea.getClaimAreaCode());
		}
		
		rgaHeaderDto.setRgaStatusCode(
				rgaStatusRepository.findById(claimRgaHeader.getRgaStatusId()).get().getRgaStatusCode());
		if(claimRgaHeader.getRgaAddress() != null) {
			BeanUtils.copyProperties(claimRgaHeader.getRgaAddress(), rgaAddressDTO);
			rgaAddressDTO.setAddressTypeCode(addressTypeRepository
					.findAddressTypeCodeByAddressTypeId(claimRgaHeader.getRgaAddress().getAddressTypeId()));
			rgaHeaderDto.setRgaAddressDTO(rgaAddressDTO);
			rgaAddressDTO.setCountryName(countryRepository.findById(rgaAddressDTO.getCountryId()).get().getIsoCountryName());
			rgaAddressDTO.setStateName(stateRepository.findById(rgaAddressDTO.getStateId()).get().getIsoStateName());
		}
		
		List<ClaimRgaLineDTO> ls = new ArrayList<>();
		for (ClaimRgaLine rgaLine : claimRgaHeader.getClaimRgaLine()) {
			ClaimRgaLineDTO claimRgaLineDTO = new ClaimRgaLineDTO();
			BeanUtils.copyProperties(rgaLine, claimRgaLineDTO);
			claimRgaLineDTO.setClaimLineId(rgaLine.getClaimLineDetail().getClaimLineId());
			claimRgaLineDTO.setUnitOfMeasureCode(rgaLine.getUnitOfMeasure().getUnitOfMeasureCode());
			if(null!=rgaLine.getClaimLineDetail()) {
				claimRgaLineDTO.setInvNumber(rgaLine.getClaimLineDetail().getClaimDocument().getDocumentNumber());
				claimRgaLineDTO.setReasonCode(rgaLine.getClaimLineDetail().getClaimReasonDefinition().getClaimReasonCode());
				claimRgaLineDTO.setReasonCodeDescription(rgaLine.getClaimLineDetail().getClaimReasonDefinition().getClaimReasonDescription());
				claimRgaLineDTO.setGrade(rgaLine.getClaimLineDetail().getGrade());
				claimRgaLineDTO.setPrice(rgaLine.getClaimLineDetail().getUnitPriceUsd());
				claimRgaLineDTO.setLot(rgaLine.getClaimLineDetail().getDyeLot());
				claimRgaLineDTO.setType(rgaLine.getClaimLineDetail().getRcsCodes().getRcsCode());
				claimRgaLineDTO.setInvDate(rgaLine.getClaimLineDetail().getClaimDocument().getDocumentDate());
				claimRgaLineDTO.setStyleName(rgaLine.getStyleName());
				claimRgaLineDTO.setColorNumber(rgaLine.getColorNumber());
				claimRgaLineDTO.setColorName(rgaLine.getColorName());
				claimRgaLineDTO.setRollNumber(rgaLine.getRollNumber());
				claimRgaLineDTO.setOrderNumber(rgaLine.getClaimLineDetail().getClaimDocument().getOrderNumber());
				claimRgaLineDTO.setBolNumber(rgaLine.getClaimLineDetail().getClaimDocument().getBillofLading());
				claimRgaLineDTO.setPoNumber(rgaLine.getClaimLineDetail().getClaimDocument().getPurchaseOrderNumber());
				claimRgaLineDTO.setLineAmount(rgaLine.getClaimLineDetail().getLineAmountUsd());
				claimRgaLineDTO.setLocationCode(locationRepository.findById(rgaLine.getRdcLocationId()).get().getLocationCode());
				claimRgaLineDTO.setLineNumber(rgaLine.getClaimLineDetail().getLineNumber());
				claimRgaLineDTO.setInvoiceLineNumber(rgaLine.getClaimLineDetail().getInvoiceLineNumber());
				}
			
			ls.add(claimRgaLineDTO);

		}

		rgaHeaderDto.setClaimRgaLineDTO(ls);
		List<ClaimNote> claimNotes=	claimNoteRepository.getClaimNotesByclaimIdAndnoteTypeId(claimRgaHeader.getClaim().getClaimId(), noteTypeRepository.fetchNoteTypeByNoteTypeCode("RETAUTH").
				getNoteTypeId());
		if(null!=claimNotes && claimNotes.size()>0)
		{
		claimNotes.sort(Comparator.comparing(ClaimNote::getCreatedDateTime).reversed());
		String notes=claimNotes.stream().map(p->p.getClaimNoteText()).findFirst().orElse("");
	  
	  rgaHeaderDto.setComments(notes);
		}
		return rgaHeaderDto;

	}

	@Override
	public void cancelRga(int claimId, String rgaNumber) {
		Optional<ClaimRgaHeader> claimRgaHeader = claimRgaHeaderRepository
				.findClaimRgaHeaderByClaimIdAndRgaNumber(claimId, rgaNumber);
		if (claimRgaHeader.isPresent()) {
			RgaStatus rgaStatus = rgaStatusRepo.findByRgaStatusCode("CAN");
			claimRgaHeader.get().setRgaStatusId(rgaStatus.getRgaStatusId());
			claimRgaHeader.get().getClaimRgaLine().stream().forEach(p -> {
				p.setRgaStatus(rgaStatus);
			});
			claimRgaHeaderRepository.save(claimRgaHeader.get());
		} else
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"RGA Not Found With This rgaNumber :: " + rgaNumber);
	}

	@Override
	public List<ServiceToKeepTypeDTO> fetchServiceToKeepType() {
		
		List<ServiceToKeepType> serviceToKeepTypeList = serviceToKeepTypeRepository.findAll();
		List<ServiceToKeepTypeDTO> serviceKeepDTOList = serviceToKeepTypeList.stream().map(data -> {
			ServiceToKeepTypeDTO serviceToKeepTypeDTO = new ServiceToKeepTypeDTO();
			BeanUtils.copyProperties(data, serviceToKeepTypeDTO);
			return serviceToKeepTypeDTO;
		}).collect(Collectors.toList());
		
		return serviceKeepDTOList;
	}

	@Override
	public RgaAddressDTO fetchCountryAndState(int countryId, int stateId) {
		RgaAddressDTO dto=new RgaAddressDTO();
		dto.setCountryName(countryRepository.findById(countryId).get().getIsoCountryName());
		dto.setStateName(stateRepository.findById(stateId).get().getIsoStateName());
		return dto;
	}
	
	public UserGroupMappingDTO getManagerDetailsByUserId(int userId) {
		String url = "/users/v1/getUserGroupAndManager?userId=" + userId;
		String finalUrl = restSecurityUrl + url;
		log.info("Final Security Url :: " + finalUrl);
		ResponseEntity<UserGroupMappingDTO> responseEntity = restTemplate.exchange(
				finalUrl,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<UserGroupMappingDTO>() {});
		UserGroupMappingDTO response = responseEntity.getBody();
		if (response == null) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found with userId :: " + userId);
		}
		return response;
	}
	
	
	 private ClaimNote getClaimNote(Claim claim, String comments,String templateName) {
	        ClaimNote claimNote = new ClaimNote();
	        NoteType noteType = noteTypeRepository.fetchNoteTypeByNoteTypeCode("RETAUTH");
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
}