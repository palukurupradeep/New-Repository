package com.shaw.claims.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.constant.ErrorConstants;
import com.shaw.claims.dto.ClaimDTO;
import com.shaw.claims.dto.ClaimDocumentDTO;
import com.shaw.claims.dto.ClaimInitiationResponseDTO;
import com.shaw.claims.dto.SmartCodeMessageDTO;
import com.shaw.claims.dto.ClaimLineDetailDTO;
import com.shaw.claims.dto.ClaimLevelRulesDTO;
import com.shaw.claims.dto.ClaimRateDTO;
import com.shaw.claims.dto.ClaimRateDetailResponseDTO;
import com.shaw.claims.dto.ClaimRoutedDTO;
import com.shaw.claims.dto.ClaimWatchlistDTO;
import com.shaw.claims.dto.CustomerDetailsDTO;
import com.shaw.claims.dto.EndUserInfoUpdateDTO;
import com.shaw.claims.dto.EndUserInformationDTO;
import com.shaw.claims.dto.NoteAndTaskRequestDTO;
import com.shaw.claims.dto.PrepareRGAResponseDTO;
import com.shaw.claims.dto.PriorClaimSummaryResponseDTO;
import com.shaw.claims.dto.RDCPlantIDResponseDTO;
import com.shaw.claims.dto.RGARequestDTO;
import com.shaw.claims.dto.RGARequestLineDetailsDTO;
import com.shaw.claims.dto.RGAResponseCodeAndErrorMsgDTO;
import com.shaw.claims.dto.RGAResponseDTO;
import com.shaw.claims.dto.SalesCustomerDetailsDTO;
import com.shaw.claims.dto.SendEmailDTO;
import com.shaw.claims.dto.UpdateClaimStatusResponseDTO;
import com.shaw.claims.dto.UpdatePauseClaimNoteRequestDTO;
import com.shaw.claims.dto.UpdateReasonCodeDTO;
import com.shaw.claims.dto.UsersDTO;
import com.shaw.claims.enums.StatusTypes;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.AuditHistory;
import com.shaw.claims.model.Claim;
import com.shaw.claims.model.ClaimDetailRecord;
import com.shaw.claims.model.ClaimDocument;
import com.shaw.claims.model.ClaimLineDetail;
import com.shaw.claims.model.ClaimNote;
import com.shaw.claims.model.ClaimReasonDefinition;
import com.shaw.claims.model.ClaimRoute;
import com.shaw.claims.model.ClaimStatus;
import com.shaw.claims.model.ClaimWatchlist;
import com.shaw.claims.model.DeclineReason;
import com.shaw.claims.model.DetailType;
import com.shaw.claims.model.DocumentType;
import com.shaw.claims.model.EndUserInformation;
import com.shaw.claims.model.LineAddReason;
import com.shaw.claims.model.Locations;
import com.shaw.claims.model.NoteTemplate;
import com.shaw.claims.model.NoteType;
import com.shaw.claims.model.SellingCompany;
import com.shaw.claims.model.TraceTask;
import com.shaw.claims.model.UnitOfMeasure;
import com.shaw.claims.model.WorkStatus;
import com.shaw.claims.repo.ClaimDeclineReasonRepository;
import com.shaw.claims.repo.ClaimDocumentRepository;
import com.shaw.claims.repo.ClaimLineDetailsRepository;
import com.shaw.claims.repo.ClaimNoteRepository;
import com.shaw.claims.repo.ClaimRateDetailRepository;
import com.shaw.claims.repo.ClaimReasonDefinitionRepository;
import com.shaw.claims.repo.ClaimRepository;
import com.shaw.claims.repo.ClaimRouteRepository;
import com.shaw.claims.repo.ClaimWatchlistRepository;
import com.shaw.claims.repo.DetailTypeRepository;
import com.shaw.claims.repo.DocumentTypeRepository;
import com.shaw.claims.repo.EndUserInformationRepository;
import com.shaw.claims.repo.LineAddReasonRepository;
import com.shaw.claims.repo.LocationRepository;
import com.shaw.claims.repo.LookupRepository;
import com.shaw.claims.repo.NoteGroupRepository;
import com.shaw.claims.repo.NoteTemplateRepository;
import com.shaw.claims.repo.NoteTypeRepository;
import com.shaw.claims.repo.SellingCompanyRepository;
import com.shaw.claims.repo.TraceTypeRepository;
import com.shaw.claims.repo.UnitOfMeasureRepository;
import com.shaw.claims.repo.WorkStatusRepository;
import com.shaw.claims.util.ObjectMapperUtil;

import jakarta.transaction.Transactional;

@Service
public class ClaimDetailsServiceImpl implements ClaimDetailsService {
	Logger log = LogManager.getLogger(ClaimDetailsServiceImpl.class);
	@Autowired
	private ClaimRepository claimRepository;
	@Autowired
	ClaimReasonDefinitionRepository claimReasonDefinitionRepository;
	@Autowired
	ClaimWatchlistRepository claimWatchlistRepository;
	@Autowired
	SellingCompanyRepository sellingCompanyRepository;
	@Autowired
	EndUserInformationRepository endUserInformationRepository;
	@Autowired
	ClaimServiceImpl claimServiceImpl;
	@Autowired
	ClaimDeclineReasonRepository claimDeclineReasonRepository;
	@Autowired
	WorkStatusRepository workStatusRepository;
	@Autowired
	ClaimLineDetailsRepository claimLineDetailsRepository;
	@Autowired
	ClaimDocumentRepository claimDocumentRepository;
	@Autowired
	UnitOfMeasureRepository unitOfMeasureRepository;
	@Autowired
	ClaimRateDetailRepository claimRateDetailRepository;
	@Autowired
	NoteTemplateRepository noteTemplateRepository;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ClaimNoteRepository claimNoteRepository;
	@Autowired
	private ClaimLevelValidateServiceImpl claimLevelValidateServiceImpl;
	@Autowired
	ObjectMapperUtil mapper;
	@Value("${claim-integration.rest.url}")
	private String restUrl;
	@Value("${prepare-RGA.error-messages.residential-carpet}")
	private String residentialCarpet;
	@Value("${prepare-RGA.error-messages.commercial-carpet}")
	private String commercialCarpet;
	@Value("${prepare-RGA.error-messages.residential-hard-surface}")
	private String residentialHardSurface;
	@Value("${prepare-RGA.error-messages.outSourced}")
	private String outSourced;
	@Value("${prepare-RGA.error-messages.allowanceToKeepServiceToKeep}")
	private String allowanceServiceToKeep;
	@Value("${prepare-RGA.error-messages.directShippedProduct}")
	private String directShippedProduct;
	@Autowired
	DocumentTypeRepository documentTypeRepository;
	@Autowired
	DetailTypeRepository detailTypeRepository;
	@Autowired
	LineAddReasonRepository lineAddReasonRepository;
	@Autowired
	@Lazy
	private ClaimServiceImpl claimService;
	@Autowired
    WorkHistoryService workHistory;
	@Autowired
	LookupRepository lookupRepository;
	@Autowired private LocationRepository locationRepository;
	@Value("${claim-notification.rest.url}")
	private String restNotificationUrl;
	@Autowired
	TraceTypeRepository traceTypeRepository;
	@Value(("${claim-security.rest.url}"))
	private String restSecurityUrl;
	@Autowired
	ClaimRouteRepository claimRouteRepository;
	@Autowired
	NoteTypeRepository noteTypeRepository;
	@Autowired
	private NoteGroupRepository noteGroupRepository;
	
	@Override
	@Transactional
	public ClaimInitiationResponseDTO jobStopToggle(int claimId, boolean jobStopped, int userId) {
		return jobAndPriorityClaimToggle(claimId,jobStopped,"jobStop", "", userId);
	}
	@Override
	@Transactional
	public ClaimInitiationResponseDTO priorityClaimToggle(int claimId, boolean priorityClaim, String noteText, int userId) {
		return jobAndPriorityClaimToggle(claimId,priorityClaim,"priorityClaim", noteText, userId);
	}

	@Override
	public List<ClaimReasonDefinition> getAllReasonCodes() {
		return claimReasonDefinitionRepository.getAllReasonCodes();
	}

	@Override
	public ClaimReasonDefinition findByClaimReasonCode(String reasonCode) {
		return claimReasonDefinitionRepository.findByClaimReasonCode(reasonCode);
	}

	@Override
	public Claim getClaimByClaimNumber(String claimNumber, int userId) {
		List<Claim> claimList = claimRepository.findByClaimNumber(claimNumber);
		setPriorClaimForInvoiceFlag(claimList);
		Claim claim = null;
		if (!claimList.isEmpty()) {
			claim = claimList.get(0);
			updateWorkHistory(claim,userId);
			
			return claim;
		} else {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found with claimNumber :: " + claimNumber);
		}
	}

	private void setClaimInitiationResponseDTO(ClaimInitiationResponseDTO claimInitiationResponseDTO,Claim claimResponse) {
		claimInitiationResponseDTO.setClaimId(claimResponse.getClaimId());
		claimInitiationResponseDTO.setClaimNumber(claimResponse.getClaimNumber());
		claimInitiationResponseDTO.setCustomerNumber(claimResponse.getCustomerNumber());
		claimInitiationResponseDTO.setClaimCategoryCode(claimResponse.getClaimCategory().getClaimCategoryCode());
		claimInitiationResponseDTO.setReasonCode(claimResponse.getClaimReasonDefinition().getClaimReasonCode());
		claimInitiationResponseDTO.setClaimAmount(claimResponse.getClaimAmountUsd());
		claimInitiationResponseDTO.setJobStopped(claimResponse.getJobStopped());
		claimInitiationResponseDTO.setPriorityClaim(claimResponse.getPriorityClaim());
	}
	
	private ClaimInitiationResponseDTO jobAndPriorityClaimToggle(int claimId, boolean claimToggleStatus,String toggleName, String noteText, int userId) {
		Claim claim = findByClaimId(claimId);
		ClaimInitiationResponseDTO claimInitiationResponseDTO = new ClaimInitiationResponseDTO();
		if (toggleName.equals("jobStop")) {
			claim.setJobStopped(claimToggleStatus);
		} else {
			claim.setPriorityClaim(claimToggleStatus);
			if(claimToggleStatus) {
			NoteTemplate noteTemplate = noteTemplateRepository.findByNoteTemplateName(CommonConstant.PRIORITY_NOTE_TEMPLATE);
			setNoteAndAuditText(claim, noteTemplate,noteTemplate.getNoteTemplateText().replaceAll("#", noteText),noteTemplate.getAuditHistoryTemplateText(),"P");
			}
		}
		claim.setModifiedDateTime(LocalDateTime.now());
		Claim claimResponse = claimRepository.save(claim);
		setClaimInitiationResponseDTO(claimInitiationResponseDTO, claimResponse);
		workHistory.updateWorkHistory(claimResponse, userId);
		return claimInitiationResponseDTO;
	}


	private void setNoteAndAuditText(Claim claim,NoteTemplate noteTemplate,String noteData,String auditData,String lookUpCode) {
		ClaimNote claimNote = new ClaimNote();
		AuditHistory auditHistory = new AuditHistory();
		claimNote.setNoteType(noteTemplate.getNoteType());
		claimNote.setNoteGroup(noteTemplate.getNoteGroup());
		claimNote.setNoteTemplate(noteTemplate);
		claimNote.setClaim(claim);
		claimNote.setLookup(lookupRepository.findByLookupCode(lookUpCode));
		claimNote.setStatusId(StatusTypes.ACTIVE.getStatusId());
		claimNote.setCreatedByUserId(claim.getCreatedByUserId());
		claimNote.setModifiedByUserId(claim.getModifiedByUserId());
		auditHistory.setCreatedByUserId(claim.getCreatedByUserId());
		auditHistory.setModifiedByUserId(claim.getModifiedByUserId());
		claimNote.setClaimNoteText(noteData);
		claim.getClaimNotes().add(claimNote);
		auditHistory.setClaim(claim);
		auditHistory.setNoteTemplate(noteTemplate);
		auditHistory.setStatusId(StatusTypes.ACTIVE.getStatusId());
		auditHistory.setAuditHistoryText(auditData);
		claim.getAuditHistory().add(auditHistory);
	}
	private void setNoteAndAuditText(Claim claim,NoteTemplate noteTemplate,String noteData,String auditData,String lookUpCode, int loggedInUserId) {
		ClaimNote claimNote = new ClaimNote();
		AuditHistory auditHistory = new AuditHistory();
		claimNote.setNoteType(noteTemplate.getNoteType());
		claimNote.setNoteGroup(noteTemplate.getNoteGroup());
		claimNote.setNoteTemplate(noteTemplate);
		claimNote.setClaim(claim);
		claimNote.setLookup(lookupRepository.findByLookupCode(lookUpCode));
		claimNote.setStatusId(StatusTypes.ACTIVE.getStatusId());
		claimNote.setCreatedByUserId(loggedInUserId);
		claimNote.setModifiedByUserId(loggedInUserId);
		claimNote.setClaimNoteText(noteData);
		claim.getClaimNotes().add(claimNote);
		auditHistory.setClaim(claim);
		auditHistory.setNoteTemplate(noteTemplate);
		auditHistory.setStatusId(StatusTypes.ACTIVE.getStatusId());
		auditHistory.setCreatedByUserId(loggedInUserId);
		auditHistory.setModifiedByUserId(loggedInUserId);
		auditHistory.setAuditHistoryText(auditData);
		claim.getAuditHistory().add(auditHistory);
	}


	@Override
	public ClaimWatchlist getClaimWatchlist(int claimId, int userId){
		Optional<ClaimWatchlist> claimWatchlistData = claimWatchlistRepository.findClaimWatchlist(claimId, userId);
		ClaimWatchlist claimWatchlist = null;
		if(claimWatchlistData.isPresent())
			claimWatchlist = claimWatchlistData.get();
		return claimWatchlist;
	}
	@Override
	@Transactional
	public ClaimWatchlistDTO claimWatchlistToggle(ClaimWatchlistDTO claimWatchlistDTO) {
		ClaimWatchlist claimWatchlist = getClaimWatchlist(claimWatchlistDTO.getClaimId(), claimWatchlistDTO.getUserId());
		Claim claim = claimRepository.findById(claimWatchlistDTO.getClaimId()).get();
		if (claimWatchlist == null) {
			claimWatchlist = new ClaimWatchlist();
			claimWatchlist.setCreatedByUserId(claimWatchlistDTO.getUserId());
			claimWatchlist.setModifiedByUserId(claimWatchlistDTO.getUserId());
		} else {
			claimWatchlist.setModifiedDateTime(LocalDateTime.now());
		}
		int statusId=(claimWatchlistDTO.isAddtoWatchList()) ? 1 : 0;
		claimWatchlist.setClaim(claim);
		claimWatchlist.setStatusId(statusId);
		ClaimWatchlist claimWatchlistResponse = claimWatchlistRepository.save(claimWatchlist);
		claimWatchlistDTO.setClaimId(claimWatchlistResponse.getClaim().getClaimId());
		claimWatchlistDTO.setAddtoWatchList(claimWatchlistResponse.getStatusId() == 1);
		workHistory.updateWorkHistory(claim,  claimWatchlistDTO.getUserId());
		return claimWatchlistDTO;
	}
	@Override
	public List<SellingCompany> getAllSellingCompanyData() {
		return sellingCompanyRepository.findByStatusId(1);
	}
	@Override
	public EndUserInformationDTO addorUpdateEndUserInfo(EndUserInfoUpdateDTO endUserInfoUpdateDTO) {
		if (endUserInfoUpdateDTO.getEndUserInformation().getEndUserInformationId() == 0) {
			List<EndUserInformation> endUserInformationListData = endUserInformationRepository
					.findByClaimId(endUserInfoUpdateDTO.getClaimId());
			if (!endUserInformationListData.isEmpty())
				throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
						"Record Already exist with ClaimId :: " + endUserInfoUpdateDTO.getClaimId());
		}
		EndUserInformationDTO endUserInfoDto = new EndUserInformationDTO();
		EndUserInformation responseData = endUserInformationRepository
				.save(setEndUserInformation(endUserInfoUpdateDTO));
		BeanUtils.copyProperties(responseData, endUserInfoDto);
		updateWorkHistory(responseData.getClaim(), responseData.getModifiedByUserId());
		return endUserInfoDto;
	}

	private EndUserInformation setEndUserInformation(EndUserInfoUpdateDTO endUserInfoUpdateDTO)
	{
		EndUserInformation endUserInfoData = new EndUserInformation();
		Claim claim = new Claim();
		BeanUtils.copyProperties(endUserInfoUpdateDTO.getEndUserInformation(), endUserInfoData);
		claim.setClaimId(endUserInfoUpdateDTO.getClaimId());
		endUserInfoData.setClaim(claim);
		return endUserInfoData;
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
	public void updateCustomerDetails(Integer claimId, String customerNumber) {
		log.info("Inside ClaimServiceImpl.updateCustomerDetails");
		Claim claim = findByClaimId(claimId);
		claim.setCustomerNumber(customerNumber);
		claim.setModifiedDateTime(LocalDateTime.now());
		claimRepository.save(claim);
		updateWorkHistory(claim,claim.getModifiedByUserId());
	}
	
	@Override
	public UpdateClaimStatusResponseDTO updateClaimStatusByAction(Integer claimId, String action, Integer declineReasonId,Integer userId,String territoryManagerName,String noteText) {
		UpdateClaimStatusResponseDTO claimStatusResponse = new UpdateClaimStatusResponseDTO();
		switch (action) {
		case "Pause":
			claimStatusResponse = pauseClaim(claimId, action,userId);
			break;
		case "Void":
			claimStatusResponse = voidClaim(claimId, action,userId);
			break;
		case "Decline":
			claimStatusResponse = declineClaim(claimId, declineReasonId, action,userId,noteText);
			break;
		case "Reopen":
			claimStatusResponse = reopenClaim(claimId, action,userId);
			break;
		case "Close":
			claimStatusResponse = closeClaim(claimId, action,userId);
			break;
		case "Request-Sample":
			claimStatusResponse = requestSampleClaim(claimId, action,userId);
			break;
		case "AssignedToPricing":
			claimStatusResponse = assignedToPricing(claimId, action,userId,territoryManagerName);
			break;
		case "RemovedFromPricing":
			claimStatusResponse = removedFromPricing(claimId, action,userId,territoryManagerName);
			break;
		case CommonConstant.ACTION_REQUEST_FOR_INFORMATION:
			claimStatusResponse = requestForInformation(claimId, action,userId,territoryManagerName,noteText);
			break;
		}
		return claimStatusResponse;
	}
	private UpdateClaimStatusResponseDTO requestForInformation(Integer claimId, String action, Integer userId,
			String territoryManagerName, String noteText) {
		Claim claim = updateClaimStatus(claimId,  CommonConstant.CLAIM_DEFAULT_STATUS, 0, action,territoryManagerName, userId,null, null);
		UpdateClaimStatusResponseDTO updatedClaimStatus = setClaimStatusResponse(claimId, claim,userId, action);
		updateWorkHistory(claim, updatedClaimStatus.getModifiedByUserId());

		return updatedClaimStatus;
	}
	private void updateWorkHistory(Claim claim, Integer modifiedUserId){
		CompletableFuture.supplyAsync(() -> {
			workHistory.updateWorkHistory(claim, modifiedUserId);
			return "Work History updated";
		});
	}
	private UpdateClaimStatusResponseDTO removedFromPricing(Integer claimId, String action,Integer userId,String territoryManagerName) {
		Claim claim = updateClaimStatus(claimId, CommonConstant.CLAIM_DEFAULT_STATUS, 0, action,territoryManagerName, userId,"", null);
		UpdateClaimStatusResponseDTO updatedClaimStatus = setClaimStatusResponse(claimId, claim,userId, action);
		updateWorkHistory(claim, updatedClaimStatus.getModifiedByUserId());

		return updatedClaimStatus;
	}
	private UpdateClaimStatusResponseDTO assignedToPricing(Integer claimId, String action,Integer userId,String territoryManagerName) {
		Claim claim = updateClaimStatus(claimId, CommonConstant.CLAIM_DEFAULT_STATUS, 0, action,territoryManagerName,userId,"", null);
		UpdateClaimStatusResponseDTO updatedClaimStatus = setClaimStatusResponse(claimId, claim,userId, action);
		updateWorkHistory(claim, updatedClaimStatus.getModifiedByUserId());
		return updatedClaimStatus;
	}
	private UpdateClaimStatusResponseDTO requestSampleClaim(Integer claimId, String action,Integer userId) {
		Claim claim = updateClaimStatus(claimId, CommonConstant.CLAIM_DEFAULT_STATUS, 0, action,"",userId,"", null);
		UpdateClaimStatusResponseDTO updatedClaimStatus = setClaimStatusResponse(claimId, claim,userId, action);
		updateWorkHistory(claim, updatedClaimStatus.getModifiedByUserId());
		return updatedClaimStatus;
	}
	private UpdateClaimStatusResponseDTO closeClaim(Integer claimId, String action,Integer userId) {
		Claim claim = updateClaimStatus(claimId, CommonConstant.CLAIM_CLOSE, 0, action,"",userId,"", null);
		UpdateClaimStatusResponseDTO updatedClaimStatus = setClaimStatusResponse(claimId, claim,userId, action);
		updateWorkHistory(claim, updatedClaimStatus.getModifiedByUserId());
		return updatedClaimStatus;
	}
	private UpdateClaimStatusResponseDTO reopenClaim(Integer claimId, String action,Integer userId) {
		Claim claim = updateClaimStatus(claimId, CommonConstant.CLAIM_REOPEN, 0, action,"",userId,"", null);
		UpdateClaimStatusResponseDTO updatedClaimStatus = setClaimStatusResponse(claimId, claim,userId, action);
		updateWorkHistory(claim, updatedClaimStatus.getModifiedByUserId());
		return updatedClaimStatus;
	}

	private UpdateClaimStatusResponseDTO declineClaim(Integer claimId,Integer declineReasonId, String action,Integer userId,String declineReasonText) {
		if(action.equalsIgnoreCase(CommonConstant.ACTION_DECLINE) && declineReasonId == 0) {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND), "Decline Reason Id is not valid");
		}
		Claim claim = updateClaimStatus(claimId, CommonConstant.CLAIM_DECLINED_ID, declineReasonId, action,"",userId,declineReasonText, null);
		UpdateClaimStatusResponseDTO updatedClaimStatus = setClaimStatusResponse(claimId, claim,userId, action);
		updateWorkHistory(claim, updatedClaimStatus.getModifiedByUserId());
		return updatedClaimStatus;
	}
	private UpdateClaimStatusResponseDTO voidClaim(Integer claimId, String action,Integer userId) {
		Claim claim = updateClaimStatus(claimId, CommonConstant.CLAIM_VOID_ID, 0, action,"",userId,"", null);
		UpdateClaimStatusResponseDTO updatedClaimStatus = setClaimStatusResponse(claimId, claim,userId, action);
		updateWorkHistory(claim, updatedClaimStatus.getModifiedByUserId());
		return updatedClaimStatus;
	}
	private UpdateClaimStatusResponseDTO pauseClaim(Integer claimId, String action,Integer userId) {
		Claim claimInfo = findByClaimId(claimId);
		Integer oldClaimStatus = null;
		Integer oldWorkStatus = null;
		if(claimInfo != null) {
			oldClaimStatus = claimInfo.getClaimStatusId();
			oldWorkStatus = claimInfo.getWorkStatusId();
		}
		Claim claim = updateClaimStatus(claimId, CommonConstant.CLAIM_PAUSE_ID, 0, action,"",userId,"", claimInfo);
		UpdateClaimStatusResponseDTO updatedClaimStatus = setClaimStatusResponse(claimId, claim,userId, action);
		ClaimStatus claimStatus = claimServiceImpl.getClaimStatusById(oldClaimStatus);
		updatedClaimStatus.setOldClaimStatus(oldClaimStatus);
		updatedClaimStatus.setOldClaimStatusName(claimStatus.getClaimStatusDescription());
		updatedClaimStatus.setOldWorkStatus(oldWorkStatus);
		WorkStatus workStatus = getWorkStatusById(oldWorkStatus);
		updatedClaimStatus.setOldWorkStatusName(workStatus.getWorkStatusDescription());
		updateWorkHistory(claim, updatedClaimStatus.getModifiedByUserId());
		claimServiceImpl.setPriorityNoteText(claim, null,null, "Pause Claim", "#PAUSE_CLAIM", 23, userId, null);
		return updatedClaimStatus;
	}

	private UpdateClaimStatusResponseDTO setClaimStatusResponse(Integer claimId, Claim claim,Integer userId, String action) {
		ClaimStatus claimStatus = claimServiceImpl.getClaimStatusById(claim.getClaimStatusId());
		UpdateClaimStatusResponseDTO updatedClaimStatus = new UpdateClaimStatusResponseDTO();
		updatedClaimStatus.setClaimId(claimId);
		updatedClaimStatus.setStatusId(claimStatus.getClaimStatusId());
		updatedClaimStatus.setStatusName(claimStatus.getClaimStatusDescription());
		
		WorkStatus workStatus = getWorkStatusById(claim.getWorkStatusId());
		if(workStatus != null) {
			updatedClaimStatus.setWorkStatusId(claim.getWorkStatusId());
			if(action.equalsIgnoreCase("Void")) {
				UsersDTO userDTO = getUsersDetailsByUserId(userId);
				if(userDTO != null) {
					updatedClaimStatus.setWorkStatusName(workStatus.getWorkStatusDescription() + " BY " + ((!userDTO.getFirstName().isEmpty()) ?  (userDTO.getFirstName() +" ") : "") + userDTO.getLastName());
				}	
			}
			updatedClaimStatus.setWorkStatusGroupDescription(workStatus.getWorkStatusGroupDescription().getWorkStatusGroupDescription());
		}
		
		if(action.equalsIgnoreCase(CommonConstant.ACTION_PAUSE)) {
			updatedClaimStatus.setTraceDate(LocalDateTime.now().plusDays(traceTypeRepository.findById(37).get().getTraceTypeDays()));
		}
		updatedClaimStatus.setCreatedByUserId(claim.getCreatedByUserId());
		updatedClaimStatus.setModifiedByUserId(userId);
		return updatedClaimStatus;
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

	public WorkStatus getWorkStatusById(int workStatusId) {
		Optional<WorkStatus> workStatus = workStatusRepository.findById(workStatusId);
		if(workStatus.isPresent()) {
			return workStatus.get();
		} else {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND), "Work status not found for Id :: {}" + workStatusId);
		}
	}
	private Claim updateClaimStatus(Integer claimId, Integer claimStatus, Integer declineReasonId, String action,String territoryManagerName, int loggedInUserId,String noteText, Claim claim) {
		Claim claimInfo;
		if(claim != null) {
			claimInfo = claim;
		}else {
			claimInfo = findByClaimId(claimId);
		}
		 
		if(claimStatus != 0) {
			claimInfo.setClaimStatusId(claimStatus);
		}
		if (action.equalsIgnoreCase(CommonConstant.ACTION_DECLINE)) {
			claimInfo.setDeclineReasonId(declineReasonId);
			claimInfo.setWorkStatusId(CommonConstant.WORK_STATUS_DECLINE);
			String declineReason="Claim declined due to";
			if(!noteText.isEmpty()) {
				noteText=declineReason+" "+StringUtils.capitalize(noteText.toLowerCase());
			ClaimNote claimNote = getClaimNote(claimInfo,noteText,loggedInUserId);
			List<ClaimNote> claimNoteList=new ArrayList<>();
			claimNoteList.add(claimNote);
			claimInfo.setClaimNotes(claimNoteList);
			}
			else
			{
				
				Optional<DeclineReason> declineReasonData=claimDeclineReasonRepository.findById(declineReasonId);
				declineReason=declineReason+" "+StringUtils.capitalize(declineReasonData.get().getDeclineReasonDescription().toLowerCase());
				ClaimNote claimNote = getClaimNote(claimInfo,declineReason,loggedInUserId);
				List<ClaimNote> claimNoteList=new ArrayList<>();
				claimNoteList.add(claimNote);
				claimInfo.setClaimNotes(claimNoteList);
			}
		} else if (action.equalsIgnoreCase(CommonConstant.ACTION_CLOSE)) {
			claimInfo.setWorkStatusId(CommonConstant.WORK_STATUS_CLOSED_WITHOUT_CREDIT);
		} else if (action.equalsIgnoreCase(CommonConstant.ACTION_REQUEST_SAMPLE)) {
			claimInfo.setWorkStatusId(CommonConstant.WORK_STATUS_SAMPLE_REQUESTED);
		} else if (action.equalsIgnoreCase(CommonConstant.ACTION_ASSIGNED_TO_PRICING)) {
			claimInfo.setWorkStatusId(CommonConstant.WORK_STATUS_SENDTOISC);
			String tmName[]=territoryManagerName.split(" ");
			NoteTemplate noteTemplate = noteTemplateRepository.findByNoteTemplateName(CommonConstant.ASSIGNED_TO_PRICING_TEMPLATE);
			setNoteAndAuditText(claimInfo, noteTemplate,noteTemplate.getNoteTemplateText().replaceAll("#TM", tmName[0]),noteTemplate.getAuditHistoryTemplateText().replaceAll("#TM", tmName[1]),"P",loggedInUserId);
			
		} else if (action.equalsIgnoreCase(CommonConstant.ACTION_REMOVE_FROM_PRICING)) {
			claimInfo.setWorkStatusId(CommonConstant.WORK_STATUS_REMOVEFROMISC);
			
			NoteTemplate noteTemplate = noteTemplateRepository.findByNoteTemplateName(CommonConstant.REMOVED_FROM_PRICING_TEMPLATE);
			String auditText=noteTemplate.getAuditHistoryTemplateText().replaceAll("#TM", territoryManagerName);
			auditText=auditText.replaceAll("#DATE", LocalDateTime.now().toString());
			setNoteAndAuditText(claimInfo, noteTemplate,noteTemplate.getNoteTemplateText().replaceAll("#TM", territoryManagerName),auditText,"C");
		} else if (action.equalsIgnoreCase(CommonConstant.ACTION_REOPEN)) {
			claimInfo.setWorkStatusId(CommonConstant.WORK_STATUS_REOPEN);
		} else if (action.equalsIgnoreCase(CommonConstant.ACTION_PAUSE)) {
			claimInfo.setWorkStatusId(CommonConstant.WORK_STATUS_PAUSE);
		} else if (action.equalsIgnoreCase(CommonConstant.ACTION_VOID)) {
			claimInfo.setWorkStatusId(CommonConstant.WORK_STATUS_VOID);
		} else if (action.equalsIgnoreCase(CommonConstant.ACTION_REQUEST_FOR_INFORMATION)) {
			claimInfo.setWorkStatusId(CommonConstant.WORK_STATUS_ACTION_REQUEST_FOR_INFORMATION);
			List<TraceTask> traceTaskList = new ArrayList<>();
			TraceTask traceTask = new TraceTask();
			ClaimNote claimNote = claimNoteRepository.findByClaimNoteId("0");
			if(claimNote!= null) {
				traceTask.setClaimNote(claimNote);	
			}
			
            traceTask.setTraceType(traceTypeRepository.findById(22).get());
            traceTask.setClaim(claimInfo);
            traceTask.setStatusId(StatusTypes.ACTIVE.getStatusId());
            traceTask.setTraceDate(LocalDateTime.now().plusDays(traceTask.getTraceType().getTraceTypeDays()));
            traceTask.setTraceActionLink("");
            traceTask.setTraceReference("");
            traceTask.setCreatedByUserId(loggedInUserId);
            traceTask.setModifiedByUserId(loggedInUserId);
            traceTask.setCreatedDateTime(LocalDateTime.now());
			traceTaskList.add(traceTask);
			claimInfo.setTraceTasks(traceTaskList);
		}
		claimInfo.setModifiedDateTime(LocalDateTime.now());
		return claimRepository.save(claimInfo);
	}
	public List<DeclineReason> getAllDeclinedReasons() {
		List<DeclineReason> declineReasonList = claimDeclineReasonRepository.findByStatusId(CommonConstant.ACTIVE);
		if (declineReasonList != null && !declineReasonList.isEmpty()) {
			return declineReasonList;
		} else {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND), "No active decline reasons");
		}
	}
	private ClaimDocument findByClaimDocumentId(Integer claimDocId){
		Optional<ClaimDocument> claimDocumentOpt=claimDocumentRepository.findById(claimDocId);
		if(claimDocumentOpt.isEmpty()){
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Record Not Found with claimDocId :: " + claimDocId);
		}
		return claimDocumentOpt.get();
	}
	@Override
	public void addInvoice(ClaimDTO claimDTO){
		Claim claim = findByClaimId(claimDTO.getClaimId());
		List<ClaimDocument> claimDocumentList = claimService.getClaimDocuments(claim, claimDTO.getClaimDocumentDTOS());
		claim.getClaimDocuments().addAll(claimDocumentList);
		claim.setClaimAmountUsd(claimService.sumDocumentsAmountUsd(claim.getClaimDocuments()));
		claim.setClaimAmountForeign(claimService.sumDocumentsAmountForeign(claim.getClaimDocuments()));
		claim.setModifiedDateTime(LocalDateTime.now());
		claim.setModifiedByUserId(claimDTO.getModifiedByUserId());
		claim.setSellingCompany(claimDTO.getSellingCompany());
		
		SmartCodeMessageDTO smartCodeMessageDTO = new SmartCodeMessageDTO();
	    try {
			smartCodeMessageDTO = claimLevelValidateServiceImpl.validateFrghtaddInVoice(claimDTO, smartCodeMessageDTO);
		} catch (Exception e) {
			log.info("Failed to validate smartcode :: {}", e.getMessage());
		}
		claimRepository.save(claim);
	}
	
	@Override
	public void updateLineItem(List<ClaimDocumentDTO> claimDocumentDTOList) {
		//lineItemActions(claimDocumentDTO, "update");
		updateMultiplelineItemActions(claimDocumentDTOList);
	}
	@Override
	public void duplicateLineItem(ClaimDocumentDTO claimDocumentDTO) {
		lineItemAction(claimDocumentDTO);
	}

	private void lineItemAction(ClaimDocumentDTO claimDocumentDTO) {
		List<ClaimDocument> claimDocumentList = new ArrayList<>();
		ClaimDocument claimDocument = findByClaimDocumentId(claimDocumentDTO.getClaimDocumentId());
		Claim claim = findByClaimId(claimDocument.getClaim().getClaimId());
		List<ClaimLineDetail> claimLineDetails = claimService.getClaimLineDetails(claim, claimDocumentDTO.getClaimLineDetailDTOS(),claimDocument);
		List<ClaimLineDetail> filteredLineList = claimLineDetails.stream()
				.map(lineItem -> {
					List<ClaimDetailRecord> filteredDetailRecords = lineItem.getClaimDetailRecords().stream()
							.filter(detailRecordType -> !detailRecordType.getDetailRecordType().getDetailRecordTypeCode()
									.equalsIgnoreCase("INV"))
							.collect(Collectors.toList());
					lineItem.setClaimDetailRecords(filteredDetailRecords);
					return lineItem;
				}).collect(Collectors.toList());
		claimDocument.setClaimLineDetails(filteredLineList);
		claimDocument.setAmountUsd((claimServiceImpl.sumLineDetailsAmountUsd(claimLineDetails)).add(claimDocument.getAmountUsd()));
		claimDocument.setAmountForeign((claimServiceImpl.sumLineDetailsAmountForeign(claimLineDetails)).add(claimDocument.getAmountForeign()));
		claimDocument.setModifiedDateTime(LocalDateTime.now());
		claimDocumentList.add(claimDocument);
		claim.setClaimAmountUsd(claimServiceImpl.sumLineDetailsAmountUsd(claimLineDetails).add(claim.getClaimAmountUsd()));
		claim.setClaimAmountForeign(claimServiceImpl.sumLineDetailsAmountForeign(claimLineDetails).add(claim.getClaimAmountForeign()));
		claim.setClaimDocuments(claimDocumentList);
		claim.setModifiedDateTime(LocalDateTime.now());
		claim.setModifiedByUserId(claimDocument.getModifiedByUserId());
		workHistory.setWorkHistory(claim);
		claimRepository.save(claim);
	}

	@Override
	public void addMoreLineItems(ClaimDocumentDTO claimDocumentDTO) {
		lineItemActions(claimDocumentDTO, "add");
	}
	private void lineItemActions(ClaimDocumentDTO claimDocumentDTO, String action){
		ClaimDocument clmDocument = findByClaimDocumentId(claimDocumentDTO.getClaimDocumentId());
		clmDocument.getClaimLineDetails().removeAll(clmDocument.getClaimLineDetails().stream().filter(cld -> cld.getStatusId() != 1).collect(Collectors.toList()));
		List<ClaimLineDetail> clmLneDtlList = claimService.getClaimLineDetails(clmDocument.getClaim(), claimDocumentDTO.getClaimLineDetailDTOS(),clmDocument);
		if(!action.equalsIgnoreCase("update")) {
			clmDocument.getClaimLineDetails().addAll(clmLneDtlList);
			calculateAmounts(clmDocument);
		} else {
			List<ClaimLineDetail> updatedClmLneDtlList = new ArrayList<>();
			clmLneDtlList.forEach(cld-> updatedClmLneDtlList.add(getClaimLineDetailById(cld.getClaimLineId())));
			List<ClaimLineDetail> activeClmLneDtlList = clmDocument.getClaimLineDetails();
			activeClmLneDtlList.removeAll(updatedClmLneDtlList);
			clmLneDtlList = clmLneDtlList.stream().map(lineDetail -> {
				ClaimLineDetail claimLineDetail = getClaimLineDetailById(lineDetail.getClaimLineId());
				lineDetail.setCreatedDateTime(claimLineDetail.getCreatedDateTime());
				return lineDetail;
			}).collect(Collectors.toList());
			activeClmLneDtlList.addAll(clmLneDtlList);
		}
		clmDocument.setModifiedDateTime(LocalDateTime.now());
		clmDocument.getClaim().setModifiedDateTime(LocalDateTime.now());
		clmDocument.getClaim().setModifiedByUserId(clmDocument.getModifiedByUserId());
		clmDocument = claimDocumentRepository.save(clmDocument);
		updateWorkHistory(clmDocument.getClaim(), claimDocumentDTO.getModifiedByUserId());
	}
	private ClaimLineDetail getClaimLineDetailById(int claimLineId){
		Optional<ClaimLineDetail> claimLineDetailData = claimLineDetailsRepository.findById(claimLineId);
		if (claimLineDetailData.isEmpty()) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found with claimLineId :: " + claimLineId);
		}
		return claimLineDetailData.get();
	}
	@Override
	public void deleteLineItem(List<Integer> claimLineIds) {
		
		claimLineIds.forEach(lineid-> {
			ClaimLineDetail claimLineDetail = getClaimLineDetailById(lineid);
			ClaimDocument claimDocument = claimLineDetail.getClaimDocument();
			List<ClaimLineDetail> claimLineDetailList = claimDocument.getClaimLineDetails().stream().filter(cld->cld.getStatusId()==1).collect(Collectors.toList());
			List<ClaimLineDetail> clmLineDetailList = new ArrayList<>();
			for (ClaimLineDetail clmLineDetail:claimLineDetailList) {
				if(clmLineDetail.getClaimLineId() != lineid) {
					clmLineDetailList.add(clmLineDetail);
				}else{
					clmLineDetail.setStatusId(3);
					clmLineDetail.setModifiedDateTime(LocalDateTime.now());
				}
			}
			
			claimDocument.setAmountUsd(!clmLineDetailList.isEmpty() ? claimService.sumLineDetailsAmountUsd(clmLineDetailList) : BigDecimal.ZERO);
			claimDocument.setAmountForeign(!clmLineDetailList.isEmpty() ? claimService.sumLineDetailsAmountForeign(clmLineDetailList) : BigDecimal.ZERO);
			claimDocument.setModifiedDateTime(LocalDateTime.now());
			Claim clm = claimDocument.getClaim();
			clm.setClaimAmountUsd(claimService.sumDocumentsAmountUsd(clm.getClaimDocuments()));
			clm.setClaimAmountForeign(claimService.sumDocumentsAmountForeign(clm.getClaimDocuments()));
			clm.setModifiedDateTime(LocalDateTime.now());
			claimDocument.setClaim(clm);
			claimDocumentRepository.save(claimDocument);
			List<ClaimLineDetail> claimLineDetailListData = claimLineDetailsRepository.findByClaimIdAndDocumentId(
					claimDocument.getClaim().getClaimId(), claimDocument.getClaimDocumentId());

			if (claimLineDetailListData.size() == 0) {

				Optional<ClaimDocument> claimDocumentData = claimDocumentRepository
						.findById(claimDocument.getClaimDocumentId());
				if (claimDocumentData.isPresent()) {
					claimDocumentData.get().setStatusId(3);
					claimDocumentRepository.save(claimDocumentData.get());
				}
			}
		});

	}
	private void calculateAmounts(ClaimDocument clmDocument){
		//claimService.setCalculatedTaxAmount(clmDocument.getClaimLineDetails(),taxPercentage);
		clmDocument.setAmountUsd(claimService.sumLineDetailsAmountUsd(clmDocument.getClaimLineDetails()));
		clmDocument.setAmountForeign(claimService.sumLineDetailsAmountForeign(clmDocument.getClaimLineDetails()));
		
		Claim clm = clmDocument.getClaim();
		clm.setClaimAmountUsd(claimService.sumDocumentsAmountUsd(clm.getClaimDocuments()));
		clm.setClaimAmountForeign(claimService.sumDocumentsAmountForeign(clm.getClaimDocuments()));
		clmDocument.setClaim(clm);
	}

	@Override
	public ClaimRateDetailResponseDTO getClaimRateDetails(String customerNumber) {
		String url = "/customer/v1/getCustomerDetails?customerNumber=";
		List<CustomerDetailsDTO> customerDetailsList = fetchData(customerNumber, url, CustomerDetailsDTO.class);
		String custNumorPrimeCustNum = "";
		String custNumber = "";
		String buyingGroup="";
		ClaimRateDetailResponseDTO claimRateResponse = new ClaimRateDetailResponseDTO();
		if (customerDetailsList != null) {
			if (!customerDetailsList.get(0).getPrimaryCustNumber().isEmpty()) {
				custNumorPrimeCustNum = customerDetailsList.get(0).getPrimaryCustNumber();
				custNumber = customerDetailsList.get(0).getCustomerNumber();

			} else {
				custNumber = customerDetailsList.get(0).getCustomerNumber();
				custNumorPrimeCustNum = custNumber;
			}
		}
		url = "/customer/v1/getCustomerSalesCustomerServiceData?customerNumber=";
		List<SalesCustomerDetailsDTO> customerServiceListData = fetchData(customerNumber, url, SalesCustomerDetailsDTO.class);
		if(customerServiceListData!=null)
		{
			buyingGroup=customerServiceListData.get(0).getGroupAccount();
		}
		ClaimRateDTO claimRatesOfCustomer = claimRateDetailRepository.getClaimRatesOfCustomer(custNumber);
		ClaimRateDTO claimDollarAmountOfCustomer = claimRateDetailRepository
					.getClaimDollarAmountOfCustomer(custNumber);
		ClaimRateDTO shawClaimRateDetailDTO = claimRateDetailRepository
					.getClaimRatesAndDollarAmountOfShaw();
		ClaimRateDTO claimDollarAmountOfCumulative = claimRateDetailRepository
					.getClaimDollarAmountOfCumulative(custNumorPrimeCustNum);
		ClaimRateDTO claimDollarAmountOfBuyingGroup = claimRateDetailRepository
					.getClaimDollarAmountOfBuyingGroup(buyingGroup);

		ClaimRateDTO customerClaimRateDetailDTO = setCustomerClaimDetails(claimRatesOfCustomer,claimDollarAmountOfCustomer);
		ClaimRateDTO cumulativeClaimRateDetailDTO = setCumulativeClaimDetails(claimDollarAmountOfCumulative);
		ClaimRateDTO buyingGroupClaimRateDetailDTO = setBuyingGroupClaimDetails(claimDollarAmountOfBuyingGroup);
		claimRateResponse.setCustomerClaimRateDetailDTO(customerClaimRateDetailDTO);
		claimRateResponse.setShawClaimRateDetailDTO(shawClaimRateDetailDTO);
		claimRateResponse.setCumulativeClaimRateDetailDTO(cumulativeClaimRateDetailDTO);
		claimRateResponse.setBuyingGroupClaimRateDetailDTO(buyingGroupClaimRateDetailDTO);
		return claimRateResponse;
	}
	@Override
	public List<PriorClaimSummaryResponseDTO> getPriorClaimForInvoice(String documentNumber, String documentTypeId,  String claimId) {
		List<Claim> claimList = getClaimListByDocumentNumber(documentNumber, documentTypeId, claimId);
		return claimService.getPriorClaimSummaryResponseList(claimList, CommonConstant.SYSTEM_USER_ID,documentNumber);
	}
	private List<Claim> getClaimListByDocumentNumber(String documentNumber, String documentTypeId, String claimId) {
		Claim claimInfo = findByClaimId(Integer.parseInt(claimId));
		List<ClaimDocument> claimDocumentList = claimDocumentRepository.getPriorClaimForInvoice(documentNumber, documentTypeId, claimId,claimInfo.getCustomerNumber());
		List<Claim> claimList = new ArrayList<>();
		for (ClaimDocument claimDocument:claimDocumentList) {
			claimList.add(claimDocument.getClaim());
		}
		return claimList;
	}
	public ClaimRateDTO setCustomerClaimDetails(ClaimRateDTO claimRatesOfCustomer,ClaimRateDTO claimDollarAmountOfCustomer) {
		ClaimRateDTO customerClaimRate = new ClaimRateDTO();
		customerClaimRate.setOverallRate(claimRatesOfCustomer.getOverallRate());
		customerClaimRate.setDeffectRate(claimRatesOfCustomer.getDeffectRate());
		customerClaimRate.setAdminRate(claimRatesOfCustomer.getAdminRate());
		customerClaimRate.setOverallSalesAmount(claimDollarAmountOfCustomer.getOverallSalesAmount());
		customerClaimRate.setDeffectCreditAmount(claimDollarAmountOfCustomer.getDeffectCreditAmount());
		customerClaimRate.setAdminCreditAmount(claimDollarAmountOfCustomer.getAdminCreditAmount());
		customerClaimRate.setOverallCreditAmount(claimDollarAmountOfCustomer.getOverallCreditAmount());
		return customerClaimRate;
	}
	public ClaimRateDTO setCumulativeClaimDetails(ClaimRateDTO claimDollarAmountOfCumulative) {
		ClaimRateDTO cumulativeClaimRate = new ClaimRateDTO();
		BigDecimal cumulativeOverallRate=Optional.ofNullable(claimDollarAmountOfCumulative.getOverallCreditAmount()).orElse(BigDecimal.ZERO).divide(Optional.ofNullable(claimDollarAmountOfCumulative.getOverallSalesAmount()).orElse(BigDecimal.ZERO),MathContext.DECIMAL128);
		BigDecimal cumulativeDefectRate =Optional.ofNullable(claimDollarAmountOfCumulative.getDeffectCreditAmount()).orElse(BigDecimal.ZERO).divide(Optional.ofNullable(claimDollarAmountOfCumulative.getOverallSalesAmount()).orElse(BigDecimal.ZERO),MathContext.DECIMAL128);
		BigDecimal cumulativeAdminRate =Optional.ofNullable(claimDollarAmountOfCumulative.getAdminCreditAmount()).orElse(BigDecimal.ZERO).divide(Optional.ofNullable(claimDollarAmountOfCumulative.getOverallSalesAmount()).orElse(BigDecimal.ZERO),MathContext.DECIMAL128);
		cumulativeClaimRate.setOverallSalesAmount(claimDollarAmountOfCumulative.getOverallSalesAmount());
		cumulativeClaimRate.setDeffectCreditAmount(claimDollarAmountOfCumulative.getDeffectCreditAmount());
		cumulativeClaimRate.setAdminCreditAmount(claimDollarAmountOfCumulative.getAdminCreditAmount());
		cumulativeClaimRate.setOverallCreditAmount(claimDollarAmountOfCumulative.getOverallCreditAmount());
		cumulativeClaimRate.setOverallRate(cumulativeOverallRate.setScale(2, RoundingMode.DOWN));
		cumulativeClaimRate.setDeffectRate(cumulativeDefectRate.setScale(2, RoundingMode.DOWN));
		cumulativeClaimRate.setAdminRate(cumulativeAdminRate.setScale(2, RoundingMode.DOWN));
		return cumulativeClaimRate;
	}
	public ClaimRateDTO setBuyingGroupClaimDetails(ClaimRateDTO claimDollarAmountOfBuyingGroup) {
		ClaimRateDTO buyingGroupClaimRateDetailDTO=new ClaimRateDTO();
		BigDecimal buyingGroupeOverallRate=Optional.ofNullable(claimDollarAmountOfBuyingGroup.getOverallCreditAmount()).orElse(BigDecimal.ZERO).divide(Optional.ofNullable(claimDollarAmountOfBuyingGroup.getOverallSalesAmount()).orElse(BigDecimal.ZERO),MathContext.DECIMAL128);
		BigDecimal buyingGroupeDefectRate =Optional.ofNullable(claimDollarAmountOfBuyingGroup.getDeffectCreditAmount()).orElse(BigDecimal.ZERO).divide(Optional.ofNullable(claimDollarAmountOfBuyingGroup.getOverallSalesAmount()).orElse(BigDecimal.ZERO),MathContext.DECIMAL128);
		BigDecimal buyingGroupeAdminRate =Optional.ofNullable(claimDollarAmountOfBuyingGroup.getAdminCreditAmount()).orElse(BigDecimal.ZERO).divide(Optional.ofNullable(claimDollarAmountOfBuyingGroup.getOverallSalesAmount()).orElse(BigDecimal.ZERO),MathContext.DECIMAL128);
		buyingGroupClaimRateDetailDTO.setOverallSalesAmount(claimDollarAmountOfBuyingGroup.getOverallSalesAmount());
		buyingGroupClaimRateDetailDTO.setDeffectCreditAmount(claimDollarAmountOfBuyingGroup.getDeffectCreditAmount());
		buyingGroupClaimRateDetailDTO.setAdminCreditAmount(claimDollarAmountOfBuyingGroup.getAdminCreditAmount());
		buyingGroupClaimRateDetailDTO.setOverallCreditAmount(claimDollarAmountOfBuyingGroup.getOverallCreditAmount());
		buyingGroupClaimRateDetailDTO.setOverallRate(buyingGroupeOverallRate.setScale(2, RoundingMode.DOWN));
		buyingGroupClaimRateDetailDTO.setDeffectRate(buyingGroupeDefectRate.setScale(2, RoundingMode.DOWN));
		buyingGroupClaimRateDetailDTO.setAdminRate(buyingGroupeAdminRate.setScale(2, RoundingMode.DOWN));
		return buyingGroupClaimRateDetailDTO;
	}
	public <T> List<T> fetchData(String customerNumber, String url, Class<T> responseType) {
		List<T> responseObject = null;
		try {
			String finalUrl = restUrl + url + customerNumber;
			log.info("Final Url :: " + finalUrl);
			URI uri = new URI(finalUrl);
			ResponseEntity<List<T>> response = restTemplate.exchange(uri, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<T>>() {});
			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
						"Record Not Found with CustomerNumber :: " + customerNumber);
			}
			responseObject = response.getBody();
			responseObject = mapper.map(responseObject,responseType);
		}catch (URISyntaxException e) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"URI is not formed correctly");
		}
		return responseObject;
	}
	@Override
	public void reAssignUser(int claimId, int adminId, int userId) {
		Claim claimData = findByClaimId(claimId);
		claimData.setClaimUserId(adminId);
		claimData.setModifiedByUserId(userId);
		claimData.setModifiedDateTime(LocalDateTime.now());
	    claimRepository.save(claimData);
	    updateWorkHistory(claimData,claimData.getModifiedByUserId());
	}
	@Override
	public List<Claim> getClaims(Integer userId, String customerNumber) {
		List<Claim> claimList =null;
		
		if (userId != null && customerNumber != null) {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND),
					"Please provide only either userid or customer number  for search criteria");
		}
		
		if(userId!=null)
			claimList = claimRepository.findByUserId(userId);
		else if (customerNumber!=null && !customerNumber.isEmpty())
			claimList = claimRepository.findByCustomerNumber(customerNumber);
		else
			claimList = claimRepository.findAll();
		return claimList;
	}
	private void setPriorClaimForInvoiceFlag(List<Claim> claimList){
		claimList.forEach(claim -> {
			claim.getClaimDocuments().forEach(cd -> {
				List<Claim> clmList = getClaimListByDocumentNumber(cd.getDocumentNumber(), String.valueOf(cd.getDocumentType().getDocumentTypeId()),
						String.valueOf(cd.getClaim().getClaimId()));
				cd.setPriorClaim(!clmList.isEmpty());
			});
		});
	}

	@Override
	public List<Claim> getClaims(List<Integer> userIds) {
		return claimRepository.findAllByClaimUserId(userIds);
		
	}

	@Override
	public List<DocumentType> getAllActiveDocuments() {
		List<DocumentType> documentTypeList = documentTypeRepository.findByStatusId(CommonConstant.ACTIVE);
		if (documentTypeList != null && documentTypeList.size() > 0) {
			return documentTypeList;
		} else {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND), "No active document types");
		}
	}
	@Override
	public List<DetailType> getAllLineCategory() {
		List<DetailType> detailTypeList = detailTypeRepository.findByStatusId(CommonConstant.ACTIVE);
		if (detailTypeList != null && detailTypeList.size() > 0) {
			return detailTypeList;
		} else {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND), "No active detail types");
		}
	}
	@Override
	public String findDetailTypeCodeByDetailTypeId(String detailTypeId) {
		return detailTypeRepository.findDetailTypeCodeByDetailTypeId(detailTypeId);
	}
	@Override
	public List<LineAddReason> getAllClaimLineAddReason() {
		List<LineAddReason> claimAddReasonList = lineAddReasonRepository.findByStatusId(CommonConstant.ACTIVE);
		if (claimAddReasonList != null && !claimAddReasonList.isEmpty()) {
			return claimAddReasonList;
		} else {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND), "No active claim line add reasons");
		}
	}
	@Override
	public void laborLineItem(ClaimDocumentDTO claimDocumentDTO) {
		for (ClaimLineDetailDTO claimLineDetailDTO : claimDocumentDTO.getClaimLineDetailDTOS()) {
			if (claimLineDetailDTO.getDetailTypeCode().equalsIgnoreCase(CommonConstant.DETAIL_TYPE_CODE_MERCH)) {
				claimLineDetailDTO.setDetailTypeCode(CommonConstant.DETAIL_TYPE_CODE_LABOR);
				claimLineDetailDTO.setClaimReasonCode("Z91");
			}
		}
		lineItemAction(claimDocumentDTO);
	}
	@Override
	public void serviceLineItem(ClaimDocumentDTO claimDocumentDTO) {
		for (ClaimLineDetailDTO claimLineDetailDTO : claimDocumentDTO.getClaimLineDetailDTOS()) {
			if (claimLineDetailDTO.getDetailTypeCode().equalsIgnoreCase(CommonConstant.DETAIL_TYPE_CODE_MERCH)) {
				claimLineDetailDTO.setDetailTypeCode(CommonConstant.DETAIL_TYPE_CODE_SERVICE);
				claimLineDetailDTO.setClaimReasonCode("Z91");
			}
		}
		lineItemAction(claimDocumentDTO);
	}
	
	@Override
	public List<UnitOfMeasure> getClaimUnitOfMeasure() {
		List<UnitOfMeasure> unitMeasureList = unitOfMeasureRepository.findByStatusId(CommonConstant.ACTIVE);
		if (!unitMeasureList.isEmpty()) {
			return unitMeasureList;
		} else {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND), "No active claim Unit Measures");
		}
	}

	@Override
	public PrepareRGAResponseDTO prepareRGA(RGARequestDTO rgaRequestDTO) {
		PrepareRGAResponseDTO rgaResponseDTO = validateDecisionApp(rgaRequestDTO);
		return rgaResponseDTO;
	}

	private PrepareRGAResponseDTO validateDecisionApp(RGARequestDTO rgaRequestDTO) {
		PrepareRGAResponseDTO prepareRGAResponseDTO = new PrepareRGAResponseDTO();
		List<RGAResponseCodeAndErrorMsgDTO> codeAndErrorMsgDTOS = new ArrayList<>();
		Optional<Claim> claimInfo = claimRepository.findById(rgaRequestDTO.getClaimId());
		Claim claim;
		SellingCompany sellingCompany = null;
		if(claimInfo.isPresent()) {
			 claim = claimInfo.get();
			sellingCompany = getSellingCompany(claim.getSellingCompany());
		}
		for (RGARequestLineDetailsDTO rga : rgaRequestDTO.getRequestLineDetailsDTOS()) {
			ClaimLineDetail claimLineDetail = claimLineDetailsRepository.findById(rga.getClaimLineId()).isPresent() ? claimLineDetailsRepository.findById(rga.getClaimLineId()).get() : null;
			if (rga.getReasonCode().equalsIgnoreCase("L00"))
				continue;
			ClaimReasonDefinition reasonDefinition = getClaimReasonDefinition(rga.getReasonCode());
			if(!rgaRequestDTO.getContinueFrom().isEmpty())
				setClaimNote(rgaRequestDTO);
			switch (reasonDefinition.getCodeRuleReturnType().getCodeRuleReturnTypeDescription()) {
				case CommonConstant.ACCOMODATION:
						switch(rgaRequestDTO.getContinueFrom()) {
							case "":
								setMinRequirement(sellingCompany, rga, prepareRGAResponseDTO, codeAndErrorMsgDTOS,CommonConstant.ACCOMODATION);
							case ErrorConstants.MINIMUM_REQUIREMENT:
								if(isDirectShippedProduct(claimLineDetail)) {
									setRGAResponseDTO(false,prepareRGAResponseDTO, codeAndErrorMsgDTOS, rga.getReasonCode(), CommonConstant.ACCOMODATION,ErrorConstants.DIRECT_SHIPPED_PRODUCT,directShippedProduct);
									return prepareRGAResponseDTO;
								}
							case ErrorConstants.DIRECT_SHIPPED_PRODUCT:
								issueRGA(sellingCompany, rgaRequestDTO, rga, prepareRGAResponseDTO,CommonConstant.ACCOMODATION);
						}

					break;
				case CommonConstant.QUALITY:
					switch(rgaRequestDTO.getContinueFrom()) {
						case "":
							setMinRequirement(sellingCompany, rga, prepareRGAResponseDTO, codeAndErrorMsgDTOS,CommonConstant.QUALITY);
						case ErrorConstants.MINIMUM_REQUIREMENT:
							costAnalysis();
							if(!isOutsourced(claimLineDetail)) {
								setRGAResponseDTO(false,prepareRGAResponseDTO,codeAndErrorMsgDTOS, rga.getReasonCode(), CommonConstant.QUALITY,ErrorConstants.ALLOWANCE_SERVICE_TO_KEEP,outSourced);
								return prepareRGAResponseDTO;
							}
							if(!checkAllowanceServiceToKeep(reasonDefinition)){
								setRGAResponseDTO(false,prepareRGAResponseDTO,codeAndErrorMsgDTOS,rga.getReasonCode(),CommonConstant.QUALITY,ErrorConstants.ALLOWANCE_SERVICE_TO_KEEP,allowanceServiceToKeep);
								return prepareRGAResponseDTO;
							}
						case "COST_ANALYSIS", "OUTSOURCED", "ALLOWANCE_SERVICE_TO_KEEP":
							issueRGA(sellingCompany, rgaRequestDTO, rga,prepareRGAResponseDTO,CommonConstant.QUALITY);
					}
					break;
			}
		}
		return prepareRGAResponseDTO;
	}

	private void setClaimNote(RGARequestDTO rgaRequestDTO) {
		ClaimNote claimNote = new ClaimNote();
		claimNote.setNoteType(noteTypeRepository.fetchNoteTypeByNoteTypeCode(CommonConstant.NOTE_TYPE_CODE));
		claimNote.setNoteGroup(noteGroupRepository.findByNoteGroupCode(claimNote.getNoteType().getNoteGroup().getNoteGroupCode()));
		claimNote.setNoteTemplate(noteTemplateRepository.findByNoteTemplateName(CommonConstant.NOTE_TEMPLATE_NAME));
		claimNote.setClaim(findByClaimId(rgaRequestDTO.getClaimId()));
		claimNote.setLookup(lookupRepository.findByLookupCode(CommonConstant.LOOKUP_CODE));
		claimNote.setCallLog(false);
		claimNote.setStatusId(CommonConstant.ACTIVE);
		claimNote.setClaimNoteText(rgaRequestDTO.getContinueFrom());
		int userId = claimService.getUserID(rgaRequestDTO.getLoggedInUser());
		claimNote.setCreatedByUserId(userId);
		claimNote.setModifiedByUserId(userId);
		claimNoteRepository.save(claimNote);
	}

	private void setRGAResponseDTO(Boolean issueRGA,PrepareRGAResponseDTO rgaResponseDTO, List<RGAResponseCodeAndErrorMsgDTO> codeAndErrorMsgDTOS, String reasonCode, String codeRuleReturnType, String errorOccurred, String errorMsg) {
		codeAndErrorMsgDTOS.add(setReasonCodeAndErrorMsg(reasonCode,errorMsg));
		rgaResponseDTO.setIssueRGA(issueRGA);
		rgaResponseDTO.setCodeRuleReturnType(codeRuleReturnType);
		rgaResponseDTO.setErrorOccurred(errorOccurred);
		rgaResponseDTO.setCodeAndErrorMsgDTOS(codeAndErrorMsgDTOS);
	}

	private void costAnalysis() {

	}

	private boolean isOutsourced(ClaimLineDetail claimLineDetail) {
		return claimLineDetail.getVendorId().trim().equalsIgnoreCase(CommonConstant.SHAW) ? true : false;
	}

	private boolean checkAllowanceServiceToKeep(ClaimReasonDefinition reasonDefinition) {
		return reasonDefinition.isAllowanceToKeep() || reasonDefinition.isServiceToKeep() ? true : false;
	}

	private Locations isProductShippedFrom3PL(RGARequestDTO rgaRequestDTO, RGARequestLineDetailsDTO lineDetailsDTO, SellingCompany sellingCompany) {
		//get RDCloc attribute from invoiceHeader and pass the city to location table location name column
		String locationType = "";
		Locations location = null;
		Claim claim = claimRepository.findByClaimId(rgaRequestDTO.getClaimId());
		ClaimLineDetail claimLineDetail = claimLineDetailsRepository.findById(lineDetailsDTO.getClaimLineId()).get();
		ClaimDocument claimDocument = claimDocumentRepository.findById(claimLineDetail.getClaimDocument().getClaimDocumentId()).get();
		if(sellingCompany != null && sellingCompany.getHardSurface() && sellingCompany.getCarpet()){
			List productCodes = Arrays.asList(42,47);
			List UOMCodes = Arrays.asList("SY","ROLL");
			if(productCodes.contains(lineDetailsDTO.getProductCode())){
				locationType = CommonConstant.HARD;
			} else if(UOMCodes.contains(claimLineDetail.getUnitOfMeasure().getUnitOfMeasureCode())){
				locationType = CommonConstant.SOFT;
			}
		} else if(sellingCompany != null && sellingCompany.getCarpet()){
			locationType = CommonConstant.SOFT;
		} else if(sellingCompany != null && sellingCompany.getHardSurface()){
			locationType = CommonConstant.HARD;
		} else if(sellingCompany != null && !sellingCompany.getHardSurface() && !sellingCompany.getCarpet()){
			locationType = CommonConstant.HARD;
		}
		List<Locations> locations = locationRepository.findByCityAndStateCode(claimDocument.getRdcCity(),claimDocument.getRdcStateCode());
		if(!locations.isEmpty() && locations.size() > 1){
			location = locationRepository.findByCityAndStateCodeAndLocationType(claimDocument.getRdcCity(),claimDocument.getRdcStateCode(),locationType) != null ?
					locationRepository.findByCityAndStateCodeAndLocationType(claimDocument.getRdcCity(),claimDocument.getRdcStateCode(),locationType).get(0) : null;
		}
		if(!locations.isEmpty() && locations.size() == 1){
			location = locations.get(0);
		}
		if(location != null && location.getThreePl())
			return location;
		return null;
	}

	private boolean isDirectShippedProduct(ClaimLineDetail claimLineDetail) {
		return claimLineDetail.getVendorId().trim().equalsIgnoreCase(CommonConstant.SHAW) ? true : false;
	}

	private PrepareRGAResponseDTO issueRGA(SellingCompany sellingCompany, RGARequestDTO rgaRequestDTO, RGARequestLineDetailsDTO lineDetailsDTO, PrepareRGAResponseDTO prepareRGAResponseDTO,String codeRuleReturnType) {
		List<String> plant47SellingCompanyCodes = Arrays.asList("14", "22", "24");
		List<String> plantPZSellingCompanyCodes = Arrays.asList("04", "50", "51");
		List<String> plant34SellingCompanyCodes = Arrays.asList("05", "06", "55", "75");
		Locations rdcLocation = null;
		String locationCode = isZipCodeServicedByRDC();
		Locations location = isProductShippedFrom3PL(rgaRequestDTO,lineDetailsDTO,sellingCompany);
		if(location != null) {
			rdcLocation = location;
		} else if(sellingCompany != null && plant47SellingCompanyCodes.contains(sellingCompany.getSellingCompanyCode())) {
			rdcLocation = locationRepository.findByLocationCode("047");
		} else if(sellingCompany != null && plantPZSellingCompanyCodes.contains(sellingCompany.getSellingCompanyCode())) {
			rdcLocation = locationRepository.findByLocationCode("0PZ");
		} else if(sellingCompany != null && plant34SellingCompanyCodes.contains(sellingCompany.getSellingCompanyCode())) {
			rdcLocation = locationRepository.findByLocationCode("034");
		} else if(locationCode != null || !locationCode.isEmpty()) {
			rdcLocation = locationRepository.findByLocationCode(locationCode);
		} else {

		}
		return setIssueRGAResponseDTO(true,prepareRGAResponseDTO,rdcLocation, codeRuleReturnType);
	}

	private PrepareRGAResponseDTO setIssueRGAResponseDTO(boolean issueRGA, PrepareRGAResponseDTO prepareRGAResponseDTO, Locations location, String codeRuleReturnType) {
		prepareRGAResponseDTO.setIssueRGA(issueRGA);
		prepareRGAResponseDTO.setCodeRuleReturnType(codeRuleReturnType);
		prepareRGAResponseDTO.setRDCLocations(location);
		return prepareRGAResponseDTO;
	}

	private String isZipCodeServicedByRDC() {
		List<RDCPlantIDResponseDTO> rdcPlantIDResponseDTOS = getRDCPlant();
		return rdcPlantIDResponseDTOS.stream()
				.findFirst()
				.map(RDCPlantIDResponseDTO::getPlant)
				.map(plant -> "0" + plant)
				.orElseThrow(() -> new CommonException("RDCPlant Not Found For This RGA Zip Code:: "));
	}

	private List<RDCPlantIDResponseDTO> getRDCPlant() {
		List<RDCPlantIDResponseDTO> rdcPlantIDResponseDTOS = null;
		String rgaZipCode = "28217";
			String url = "/customer/v1/getRDCPlantID?RGAZipCode=" + rgaZipCode;
			String finalUrl = restUrl + url;
			log.info("Final URL :: " + finalUrl);
			ResponseEntity<List<RDCPlantIDResponseDTO>> response = restTemplate.exchange(finalUrl, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<RDCPlantIDResponseDTO>>() {
					});
			if(response.getStatusCode().is2xxSuccessful()){
				rdcPlantIDResponseDTOS = response.getBody();
			}else {
				throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
						"RDCPlant Not Found For This RGA Zip Code:: " + rgaZipCode);
			}
			return rdcPlantIDResponseDTOS;
	}

	private void getRgaPathAccommodation(RGAResponseDTO rgaResponseDTO, SellingCompany sellingCompany, int claimLineId) {
		int claimDocumentId = claimLineDetailsRepository.findClaimDocument_ClaimDocumentIdByClaimLineId(claimLineId);
		ClaimDocument claimDocument = claimDocumentRepository.findById(claimDocumentId).get();
	}

	private RGAResponseCodeAndErrorMsgDTO setReasonCodeAndErrorMsg(String reasonCode, String errorMsg) {
		RGAResponseCodeAndErrorMsgDTO rgaResponseCodeAndErrorMsgDTO = new RGAResponseCodeAndErrorMsgDTO();
		rgaResponseCodeAndErrorMsgDTO.setReasonCode(reasonCode);
		rgaResponseCodeAndErrorMsgDTO.setErrorMessage(errorMsg);
		return rgaResponseCodeAndErrorMsgDTO;
	}

	private ClaimReasonDefinition getClaimReasonDefinition(String reasonCode) {
		return claimReasonDefinitionRepository.findByClaimReasonCode(reasonCode);

	}

	private PrepareRGAResponseDTO setMinRequirement(SellingCompany sellingCompany, RGARequestLineDetailsDTO rga, PrepareRGAResponseDTO prepareRGAResponseDTO, List<RGAResponseCodeAndErrorMsgDTO> codeAndErrorMsgDTOS, String codeRuleReturnType) {
		if (sellingCompany != null && sellingCompany.getCarpet()) {
			if (sellingCompany.getCompanyType() == 'R') {
				if (rga.getSquareFeet().compareTo(BigDecimal.valueOf(50)) < 0) {
					setRGAResponseDTO(false,prepareRGAResponseDTO,codeAndErrorMsgDTOS,rga.getReasonCode(),codeRuleReturnType,ErrorConstants.MINIMUM_REQUIREMENT,residentialCarpet);
					
					//rgaResponseDTO.setAreaCode(sellingCompany.getSellingCompanyName());
				} else if (rga.getSquareFeet().compareTo(BigDecimal.valueOf(50)) > 0) {
					// Restock 25% plus freight both ways  - auto calculation
				}
			}
			if (sellingCompany.getCompanyType() == 'C') {
				if (rga.getSquareFeet().compareTo(BigDecimal.valueOf(75)) < 0) {
					setRGAResponseDTO(false,prepareRGAResponseDTO,codeAndErrorMsgDTOS, rga.getReasonCode(),codeRuleReturnType,ErrorConstants.MINIMUM_REQUIREMENT,commercialCarpet);
				} else if (rga.getSquareFeet().compareTo(BigDecimal.valueOf(75)) > 0) {
					// 50% restock or $200 minimum  - auto calculation
				}
			}
		}
		if (sellingCompany != null && sellingCompany.getHardSurface()) {
			if (sellingCompany.getCompanyType() == 'R') {
				if (rga.getSquareFeet().compareTo(BigDecimal.valueOf(250)) < 0) {
					setRGAResponseDTO(false,prepareRGAResponseDTO,codeAndErrorMsgDTOS, rga.getReasonCode(), codeRuleReturnType,ErrorConstants.MINIMUM_REQUIREMENT,residentialHardSurface);
				} else if (rga.getSquareFeet().compareTo(BigDecimal.valueOf(250)) > 0) {
					// Restock 25% of invoice plus freight charges (drop fees, fuel and freight surcharge)  - auto calculation
				}
			}
			if (sellingCompany.getCompanyType() == 'C') {
				if (rga.getSquareFeet().compareTo(BigDecimal.valueOf(5000)) >= 0) {
					// 50% restock - auto calculation
				}
			}
		}
		if (sellingCompany != null && sellingCompany.getCarpetTile()) {
			if (sellingCompany.getCompanyType() == 'R') {
				if (rga.getQuantity().compareTo(BigDecimal.valueOf(20)) >= 0) {
					// Minimum 60 boxes (only in boxes) - 25% restock - auto calculation
				}
			}
			if (sellingCompany.getCompanyType() == 'C') {
				if (rga.getQuantity().compareTo(BigDecimal.valueOf(60)) >= 0) {
					// Minimum 60 boxes (only in boxes) - 50% restock - auto calculation
				}
			}
		}
		if (sellingCompany == null && (rga.getProductCode() == "42" || rga.getProductCode() == "47")) {
			// Tile Mosaic/Bullnose
		}
		return prepareRGAResponseDTO;
	}

	public SellingCompany getSellingCompany(String sellingCompanyCode) {
		return sellingCompanyRepository.findBySellingCompanyCode(sellingCompanyCode);

	}

	@Override
	public void routed(ClaimRoutedDTO claimRoutedDTO) {
		Claim claim = findByClaimId(claimRoutedDTO.getClaimId());
		List<ClaimRoute> claimRouteList = routedClaim(claimRoutedDTO,claim,false);
		claim.setClaimRoutes(claimRouteList);
		claimRepository.save(claim);
		SendEmailDTO sendEmailDTO=new SendEmailDTO();
		sendEmailDTO.setToRecipients(claimRoutedDTO.getToRecipients());
		sendEmailDTO.setCcRecipients(claimRoutedDTO.getCcRecipients());
		sendEmailDTO.setSubject("Claim Routing");
		sendEmailDTO.setBody("Claim Routed to "+claimRoutedDTO.getRoutedUserName() + " Successfully");
		if (claimRoutedDTO.getCcRecipients() != null && claimRoutedDTO.getCcRecipients().length > 0) {
			sendEmailNotification(sendEmailDTO);
		}
	}
	@Override
	public List<ClaimRoute> routedClaim(ClaimRoutedDTO claimRoutedDTO,Claim claim, Boolean isInStoreClaim) {
		
		if(null !=claimRoutedDTO.getUserGroupCode() && claimRoutedDTO.getUserGroupCode().equalsIgnoreCase("CLM")){
			claim.setWorkStatusId(workStatusRepository.findWorkStatusIdByWorkStatusCode("044"));
			AuditHistory auditHistory=new AuditHistory();
			ClaimNote claimNote = new ClaimNote();
			setClaimNoteForRoute(claimNote,claim,claimRoutedDTO,CommonConstant.ROUTED_FOR_REVIEW_NOTE_TEMPLATE,auditHistory);
			TraceTask traceTask = new TraceTask();
			NoteAndTaskRequestDTO noteAndTaskRequestDTO = new NoteAndTaskRequestDTO();
//			noteAndTaskRequestDTO.setTraceTypeId(23);
			if (isInStoreClaim) {
				noteAndTaskRequestDTO.setTraceTypeId(traceTypeRepository.findTraceTypeIdByTraceTypeCode(CommonConstant.ASSIGNED_TO_PRICING_CODE));
			} else {
				noteAndTaskRequestDTO.setTraceTypeId(traceTypeRepository.findTraceTypeIdByTraceTypeCode(CommonConstant.CLAIM_REASON_CODE_APPROVAL));
			}
			noteAndTaskRequestDTO.setClaimId(claim.getClaimId());
			noteAndTaskRequestDTO.setCreatedByUserId(claimRoutedDTO.getUserId());
			noteAndTaskRequestDTO.setModifiedByUserId(claimRoutedDTO.getUserId());
			setTraceTask(noteAndTaskRequestDTO, claimNote, traceTask, claimRoutedDTO.getRoutedUserId());
			List<ClaimNote> claimNotes = new ArrayList<>();
			claimNotes.addAll(claim.getClaimNotes());
			claimNotes.add(claimNote);
			claim.setClaimNotes(claimNotes);
			List<AuditHistory> auditHistories = new ArrayList<>();
			auditHistories.addAll(claim.getAuditHistory());
			auditHistories.add(auditHistory);
			claim.setAuditHistory(auditHistories);
		}
		else if(null !=claimRoutedDTO.getUserGroupCode() && claimRoutedDTO.getUserGroupCode().equalsIgnoreCase("LAB")){
			claim.setWorkStatusId(workStatusRepository.findWorkStatusIdByWorkStatusCode("049"));
			ClaimNote claimNote = new ClaimNote();
			setClaimNoteForRoute(claimNote,claim,claimRoutedDTO,CommonConstant.SEND_TO_LAB,null);
			TraceTask traceTask = new TraceTask();
			NoteAndTaskRequestDTO noteAndTaskRequestDTO = new NoteAndTaskRequestDTO();
//			noteAndTaskRequestDTO.setTraceTypeId(23);
			noteAndTaskRequestDTO.setTraceTypeId(18);
			noteAndTaskRequestDTO.setClaimId(claim.getClaimId());
			noteAndTaskRequestDTO.setCreatedByUserId(claimRoutedDTO.getUserId());
			noteAndTaskRequestDTO.setModifiedByUserId(claimRoutedDTO.getUserId());
			setTraceTask(noteAndTaskRequestDTO, claimNote, traceTask, claimRoutedDTO.getRoutedUserId());
			claim.getClaimNotes().add(claimNote);
		}
		else{
			claim.setWorkStatusId(workStatusRepository.findWorkStatusIdByWorkStatusCode("015"));
			AuditHistory auditHistory=new AuditHistory();
			ClaimNote claimNote = new ClaimNote();
			setClaimNoteForRoute(claimNote,claim,claimRoutedDTO,CommonConstant.ROUTED_FOR_REVIEW_NOTE_TEMPLATE,auditHistory);
			TraceTask traceTask = new TraceTask();
			NoteAndTaskRequestDTO noteAndTaskRequestDTO = new NoteAndTaskRequestDTO();
//			noteAndTaskRequestDTO.setTraceTypeId(23);
			noteAndTaskRequestDTO.setTraceTypeId(18);
			noteAndTaskRequestDTO.setClaimId(claim.getClaimId());
			noteAndTaskRequestDTO.setCreatedByUserId(claimRoutedDTO.getUserId());
			noteAndTaskRequestDTO.setModifiedByUserId(claimRoutedDTO.getUserId());
			setTraceTask(noteAndTaskRequestDTO, claimNote, traceTask, claimRoutedDTO.getRoutedUserId());
			claim.getClaimNotes().add(claimNote);
			claim.getAuditHistory().add(auditHistory);
		}
		List<ClaimRoute> claimRouteList = new ArrayList<>();
		ClaimRoute  claimRoute=new ClaimRoute();
		claimRoute.setClaim(claim);
		int routeGroupId=getUserGroupId(claimRoutedDTO.getUserGroupCode());
		claimRoute.setRoutedToUserGroupId(routeGroupId);
        claimRoute.setStatusId(StatusTypes.ACTIVE.getStatusId());
        claimRoute.setCreatedByUserId(claimRoutedDTO.getUserId());
        claimRoute.setModifiedByUserId(claimRoutedDTO.getUserId());
        claimRoute.setRoutedToUserId(claimRoutedDTO.getRoutedUserId());
		claimRouteList.add(claimRoute);

        List<ClaimRoute>routedData= claimRouteRepository.findRoutedData(claimRoutedDTO.getRoutedUserId(), claim.getClaimId(), routeGroupId);
        for (ClaimRoute claimRouteResult : routedData) {
        	claimRouteRepository.deleteById(claimRouteResult.getClaimRoutingId());
		}
		return claimRouteList;
		//NoteTemplate noteTemplate = noteTemplateRepository.findByNoteTemplateName(CommonConstant.ROUTED_FOR_REVIEW_NOTE_TEMPLATE);
		//String auditText=noteTemplate.getAuditHistoryTemplateText().replaceAll("#FRNAME", claimRoutedDTO.getLoginUserName());
		//auditText=auditText.replaceAll("#TNAME", claimRoutedDTO.getRoutedUserName());
		//String noteText=noteTemplate.getNoteTemplateText();
		//noteText=noteText.replaceAll("#NAME",claimRoutedDTO.getRoutedUserName())+" "+claimRoutedDTO.getNote();
		//setNoteAndAuditText(claim, noteTemplate,noteText,auditText,"P",claimRoutedDTO);
	}
	private String sendEmailNotification(SendEmailDTO sendEmailDTO) {
	        String url = "/notification/v1/sendEmailNotification";
	        String finalUrl = restNotificationUrl + url;
	        log.info("Final Security Url :: " + finalUrl);
	        
	        HttpHeaders headers = new HttpHeaders();
	        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	        HttpEntity<SendEmailDTO> entity = new HttpEntity<SendEmailDTO>(sendEmailDTO,headers);
	          ResponseEntity<String> responseEntity = restTemplate.exchange(
	                finalUrl,
	                HttpMethod.POST,
	                entity,
	                String.class);
	      String response = responseEntity.getBody();
	     
	        if (response == null || response.isEmpty()) {
	            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
	                    "Unable to send email notification :: ");
	        }
	        return response;
	    }
	  
	  public void setTraceTask(NoteAndTaskRequestDTO noteAndTaskRequestDTO, ClaimNote claimNote, TraceTask traceTask, int assignedUserId) {
	        if(noteAndTaskRequestDTO.getTraceTypeId() != 0) {
	            traceTask.setClaimNote(claimNote);
	            traceTask.setTraceType(traceTypeRepository.findById(noteAndTaskRequestDTO.getTraceTypeId()).get());
	            traceTask.setClaim(claimNote.getClaim());
	            traceTask.setStatusId(StatusTypes.ACTIVE.getStatusId());
	            traceTask.setAssignedUserId(assignedUserId);
	            traceTask.setCreatedByUserId(noteAndTaskRequestDTO.getCreatedByUserId());
	            traceTask.setModifiedByUserId(noteAndTaskRequestDTO.getModifiedByUserId());
	            traceTask.setTraceDate(LocalDateTime.now().plusDays(traceTask.getTraceType().getTraceTypeDays()));
	            traceTask.setTraceActionLink("");
	            traceTask.setTraceReference("");
	            claimNote.setTraceTask(traceTask);
	        }
	    }

	 	private void setClaimNoteForRoute(ClaimNote claimNote,Claim claim,ClaimRoutedDTO claimRoutedDTO,String templateName,AuditHistory auditHistory)
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
			claimNote.setClaimNoteText(claimRoutedDTO.getNote());
			claimNote.setLookup(lookupRepository.findByLookupCode("P"));
			claimNote.setStatusId(1);
			claimNote.setCreatedByUserId(claimRoutedDTO.getUserId());
			claimNote.setModifiedByUserId(claimRoutedDTO.getUserId());
			if(null!=auditHistory)
			{
				auditHistory.setClaim(claim);
				auditHistory.setNoteTemplate(noteTemplate);
				auditHistory.setStatusId(StatusTypes.ACTIVE.getStatusId());
				auditHistory.setAuditHistoryText(noteTemplate.getAuditHistoryTemplateText());
				auditHistory.setCreatedByUserId(claimRoutedDTO.getUserId());
				auditHistory.setModifiedByUserId(claimRoutedDTO.getUserId());
			}
			
		}
	 	private int getUserGroupId(String userGroupCode) {
	        int userGroupId = 0;
	        String url = "/users/v1/getUserIdByGroupCode?userGroupCode=" + userGroupCode;
	        String finalUrl = restSecurityUrl + url;
	        log.info("Final Security Url :: " + finalUrl);
	        Integer response = restTemplate.getForObject(finalUrl, Integer.class);
	        if (response != null) {
	            userGroupId = response;
	        }
	        return userGroupId;
	    }
		@Override
		public void updateSellingCompanyAndReasoCode(UpdateReasonCodeDTO updateReasonCodeDTO) {
			
			Claim claim=findByClaimId(updateReasonCodeDTO.getClaimId());
			if(!updateReasonCodeDTO.getReasonCode().isEmpty())
			{
			ClaimReasonDefinition claimReasonDefinition=claimReasonDefinitionRepository.findByClaimReasonCode(updateReasonCodeDTO.getReasonCode());
			claim.setClaimReasonDefinition(claimReasonDefinition);
			claim.setClaimCategory(claimReasonDefinition.getClaimCategory());
			}
			if(!updateReasonCodeDTO.getSellingcompanyCode().isEmpty())
			{
			 claim.setSellingCompany(updateReasonCodeDTO.getSellingcompanyCode());  
			}
			claim.setModifiedByUserId(updateReasonCodeDTO.getModifiedByUserId());
			claim.setModifiedDateTime(LocalDateTime.now());
			claimRepository.save(claim);
		}

	private ClaimNote getClaimNote(Claim claim, String comments,int loggedInUserId) {
		        ClaimNote claimNote = new ClaimNote();
		        NoteType noteType = noteTypeRepository.fetchNoteTypeByNoteTypeCode("CLAIM");
		        NoteTemplate noteTemplate = noteTemplateRepository.findByNoteTemplateName("UNKN");
		        claimNote.setClaimNoteText(comments);
		        claimNote.setNoteType(noteType);
		        claimNote.setNoteGroup(noteType.getNoteGroup());
		        claimNote.setNoteTemplate(noteTemplate);
		        claimNote.setClaim(claim);
		        claimNote.setLookup(lookupRepository.findByLookupCode("P"));
		        claimNote.setStatusId(StatusTypes.ACTIVE.getStatusId());
		        claimNote.setCreatedByUserId(loggedInUserId);
		        claimNote.setModifiedByUserId(loggedInUserId);
		       
		        return claimNote;
		    }
	@Override
	public void addGenericDocument(ClaimDocumentDTO claimDocumentDTO) {
		    ClaimDocument claimDocument = new ClaimDocument();
		    Claim claim = findByClaimId(claimDocumentDTO.getClmId());
	        claimDocument.setDocumentNumber("7777777");
	        claimDocument.setDocumentType(documentTypeRepository.findByDocumentTypeCode("INV"));
	        if(!claimDocumentDTO.getClaimReasonCode().isEmpty())
	        {
	        	 claimDocument.setClaimReasonDefinition( claimReasonDefinitionRepository.findByClaimReasonCode(claimDocumentDTO.getClaimReasonCode()));
	        }
	        else
	        {
	        	 claimDocument.setClaimReasonDefinition( claimReasonDefinitionRepository.findByClaimReasonCode("ALL"));
	        }
	        List<ClaimLineDetail> clmLneDtlList =  claimService.getClaimLineDetails(claim, claimDocumentDTO.getClaimLineDetailDTOS(),claimDocument);
	   claimDocument.setClaim(claim);
	   claimDocument.setClaimLineDetails(clmLneDtlList);
		claimDocument.setModifiedDateTime(LocalDateTime.now());
		claimDocument.setCreatedByUserId(claimDocumentDTO.getModifiedByUserId());
		claimDocument.setExchangeRate(BigDecimal.valueOf(0));
		claimDocument.setDocumentDate(LocalDateTime.now());
		claimDocument.setModifiedByUserId(claimDocumentDTO.getModifiedByUserId());
		claim.setModifiedDateTime(LocalDateTime.now());
		claim.setModifiedByUserId(claimDocumentDTO.getModifiedByUserId());
		 claimDocumentRepository.save(claimDocument);

		
	}
	
	 private void updateMultiplelineItemActions(List<ClaimDocumentDTO> claimDocumentDTOList){
		 for (ClaimDocumentDTO claimDocumentDTO : claimDocumentDTOList) {
			
			ClaimDocument clmDocument = findByClaimDocumentId(claimDocumentDTO.getClaimDocumentId());
			clmDocument.getClaimLineDetails().removeAll(clmDocument.getClaimLineDetails().stream().filter(cld -> cld.getStatusId() != 1).collect(Collectors.toList()));
			List<ClaimLineDetail> clmLneDtlList = claimService.getClaimLineDetails(clmDocument.getClaim(), claimDocumentDTO.getClaimLineDetailDTOS(),clmDocument);
			
				List<ClaimLineDetail> updatedClmLneDtlList = new ArrayList<>();
				clmLneDtlList.forEach(cld-> updatedClmLneDtlList.add(getClaimLineDetailById(cld.getClaimLineId())));
				List<ClaimLineDetail> activeClmLneDtlList = clmDocument.getClaimLineDetails();
				activeClmLneDtlList.removeAll(updatedClmLneDtlList);
				clmLneDtlList = clmLneDtlList.stream().map(lineDetail -> {
					ClaimLineDetail claimLineDetail = getClaimLineDetailById(lineDetail.getClaimLineId());
					lineDetail.setCreatedDateTime(claimLineDetail.getCreatedDateTime());
					return lineDetail;
				}).collect(Collectors.toList());
				activeClmLneDtlList.addAll(clmLneDtlList);
			
			clmDocument.setModifiedDateTime(LocalDateTime.now());
			clmDocument.getClaim().setModifiedDateTime(LocalDateTime.now());
			clmDocument.getClaim().setModifiedByUserId(clmDocument.getModifiedByUserId());
			clmDocument = claimDocumentRepository.save(clmDocument);
			updateWorkHistory(clmDocument.getClaim(), claimDocumentDTO.getModifiedByUserId());
		}
	 }
	@Override
	public UpdateClaimStatusResponseDTO updatePauseClaimNote(UpdatePauseClaimNoteRequestDTO updatePauseClaimNoteRequestDTO) {
		Claim claim = claimRepository.findByClaimId(updatePauseClaimNoteRequestDTO.getClaimId());
		UpdateClaimStatusResponseDTO updateClaimStatusResponseDTO = null;
		if(claim != null) {
			claimServiceImpl.setPriorityNoteText(claim,  updatePauseClaimNoteRequestDTO.getNoteText(), null, "Pause Claim", null, 23, updatePauseClaimNoteRequestDTO.getUserId(), updatePauseClaimNoteRequestDTO.getTraceDate());
			updateClaimStatusResponseDTO = setClaimStatusResponse(updatePauseClaimNoteRequestDTO.getClaimId(), claim, updatePauseClaimNoteRequestDTO.getUserId(), "Pause");
		}
		return updateClaimStatusResponseDTO;
	}
}
