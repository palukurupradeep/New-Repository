package com.shaw.claims.services;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.shaw.claims.builder.ClaimBuilder;
import com.shaw.claims.constant.ErrorCodes;
import com.shaw.claims.dto.*;
import com.shaw.claims.enums.ClaimType;
import com.shaw.claims.exception.CustomValidationException;
import com.shaw.claims.repo.*;
import com.shaw.claims.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.client.RestTemplate;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.constant.ManageCodes;
import com.shaw.claims.enums.MaterialType;
import com.shaw.claims.enums.ServiceTypes;
import com.shaw.claims.enums.StatusTypes;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.AuditHistory;
import com.shaw.claims.model.Claim;
import com.shaw.claims.model.ClaimAddress;
import com.shaw.claims.model.ClaimCategory;
import com.shaw.claims.model.ClaimDetailRecord;
import com.shaw.claims.model.ClaimDocument;
import com.shaw.claims.model.ClaimLineDetail;
import com.shaw.claims.model.ClaimMerge;
import com.shaw.claims.model.ClaimNote;
import com.shaw.claims.model.ClaimReasonDefinition;
import com.shaw.claims.model.ClaimRoute;
import com.shaw.claims.model.ClaimStatus;
import com.shaw.claims.model.ClaimWatchlist;
import com.shaw.claims.model.Country;
import com.shaw.claims.model.DialCode;
import com.shaw.claims.model.EndUserInformation;
import com.shaw.claims.model.LineSource;
import com.shaw.claims.model.NoteTemplate;
import com.shaw.claims.model.NoteType;
import com.shaw.claims.model.State;
import com.shaw.claims.model.TraceTask;
import com.shaw.claims.model.TraceType;
import com.shaw.claims.model.WorkStatus;
import com.shaw.claims.util.DateUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class ClaimServiceImpl implements ClaimService {
    Logger log = LogManager.getLogger(ClaimServiceImpl.class);
    @Autowired
    StateRepository stateRepository;
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    DialCodeRepository dialCodeRepository;
    @Autowired
    private ClaimRepository claimRepository;
    @Autowired
    private ClaimBuilder claimBuilder;
    @Autowired
    private RcsCodesRepository rcsCodesRepository;
    @Autowired
    private ClaimDocumentRepository claimDocumentRepository;
    @Autowired
    private EndUserInformationRepository endUserInformationRepository;
    @Autowired
    private ClaimAddressRepository claimAddressRepository;
    @Autowired
    private AddressTypeRepository addressTypeRepository;
    @Autowired
    private DeclineReasonRepository declineReasonRepository;
    @Autowired
    private LineSourceRepository lineSourceRepository;
    @Autowired
    private ClaimReasonDefinitionRepository claimReasonDefinitionRepository;
    @Autowired
    private DetailTypeRepository detailTypeRepository;
    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    @Autowired
    private DetailRecordTypeRepository detailRecordTypeRepository;
    @Autowired
    private DetailStatusTypeRepository detailStatusTypeRepository;
    @Autowired
    private LineAddReasonRepository lineAddReasonRepository;
    @Autowired
    private ClaimLineDetailsRepository claimLineDetailsRepository;
    @Autowired
    private ClaimDetailRecordRepository claimDetailRecordRepository;
    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;
    @Autowired
    private SalesReviewStatusRepository salesReviewStatusRepository;
    @Autowired
    ClaimStatusRepository claimStatusRepository;
    @Autowired
    private ResponseUtil responseUtil;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LookupRepository lookupRepository;
    @Autowired
    private AreaTypeRepository areaTypeRepository;
    @Autowired
    private ClaimAreaRepository claimAreaRepository;
    @Autowired
    private ClaimWatchlistRepository watchlistRepository;
    @Autowired
    WorkStatusRepository workStatusRepository;
    @Autowired
    ClaimCategoryRepository claimCategoryRepository;
    @Autowired
    private ClaimUserAreaRepository claimUserAreaRepository;
    @Autowired
    private ClaimMergeRepository mergeRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    EntityManager entityManager;
    @Value("${claim-integration.rest.url}")
    private String restUrl;
    @Value(("${claim-security.rest.url}"))
    private String restSecurityUrl;
    @Autowired
    private WorkHistoryServiceImpl workHistory;
    @Autowired
    private ClaimNoteRepository claimNoteRepository;
    @Autowired
    DateUtil dateUtil;

    @Autowired
	NoteTemplateRepository noteTemplateRepository;
    @Autowired
    private NoteGroupRepository noteGroupRepository;
    @Autowired
    @Lazy
    private ClaimBuilderService claimBuilderService;

    @Autowired
    NoteTypeRepository noteTypeRepository;

    @Autowired
    AuditHistoryRepository auditHistoryRepo;

    @Autowired
    private ClaimRouteRepository claimRouteRepository;
    @Autowired
    private ClaimBatchHeaderServiceImpl claimBatchHeaderService;
    @Autowired
    @Lazy
    private ClaimDetailsService claimDetailsService;
    
    @Autowired
    TraceTypeRepository traceTypeRepo;

    @Override
    public ClaimInitiationResponseDTO initiate(ClaimDTO claimDTO) {
        ClaimInitiationResponseDTO claimInitiationResponseDTO = new ClaimInitiationResponseDTO();
        Claim claimResponse = claimRepository.save(claimInitiation(claimDTO));
        setClaimInitiationResponseDTO(claimInitiationResponseDTO, claimResponse);
        return claimInitiationResponseDTO;
    }

    private List<ClaimAddress> convertClaimAddressDTOToClaimAddress(List<ClaimAddressDTO> claimAddressDTOS, Claim claim) {
        return claimAddressDTOS.stream()
                .map(data -> {
                    ClaimAddress claimAddress = new ClaimAddress();
                    claimAddress.setClaim(claim);
                    claimAddress.setAddressTypeId(addressTypeRepository.findAddressTypeIdByAddressTypeCode(data.getAddressTypeCode()));
                    claimAddress.setAddressLine1(data.getAddressLine1());
                    claimAddress.setAddressLine2(data.getAddressLine2());
                    claimAddress.setFullName(data.getFullName());
                    claimAddress.setCity(data.getCity());
                    int countryId = countryRepository.findCountryIdByIsoCountryCode(data.getCountry());
                    claimAddress.setStateId(stateRepository.findStateIdByIsoStateCodeAndCountryId(data.getState(), countryId));
                    claimAddress.setCountryId(countryId);
                    claimAddress.setPostalCode(data.getZipCode());
                    claimAddress.setCreatedByUserId(claim.getCreatedByUserId());
                    claimAddress.setModifiedByUserId(claim.getModifiedByUserId());
                    return claimAddress;
                }).collect(Collectors.toList());
    }

    private void setAddWatchList(ClaimDTO claimDto, Claim claim) {
        List<ClaimWatchlist> clmWatchlist = new ArrayList<>();
        if(claimDto.getAddToWatchList()) {
            ClaimWatchlist claimWatchlist = new ClaimWatchlist();
            claimWatchlist.setClaim(claim);
            claimWatchlist.setStatusId(statusRepository.findStatusIdByStatusCode("Active"));
            claimWatchlist.setCreatedByUserId(claimDto.getCreatedByUserId());
            claimWatchlist.setModifiedByUserId(claimDto.getModifiedByUserId());
            clmWatchlist.add(claimWatchlist);
            claim.setClaimWatchlist(clmWatchlist);
        }
    }

    private void setClaimAmount(Claim claim, List<ClaimDocumentDTO> claimDocumentDTOS) {
        if (claimDocumentDTOS != null && (claim.getCustomerNumber() != null && !claim.getCustomerNumber().isEmpty())) {
            claim.setClaimAmountUsd(sumDocumentsAmountUsd(claim.getClaimDocuments()));
            claim.setClaimAmountForeign(sumDocumentsAmountForeign(claim.getClaimDocuments()));
        }
    }


    public String getAreaTypeCode(String customerNumber) {
        // claim assignment data should be single
        String areaTypeCode = "";
        if (customerNumber != null && !customerNumber.isEmpty()) {
            ClaimAssignmentDataResponseDTO[] claimAssignmentDataResponse = getClaimAssignmentDataResponse(customerNumber);
            assert claimAssignmentDataResponse.length == 1;
            areaTypeCode = claimAssignmentDataResponse[0].getClaimsArea();
        } else {
            areaTypeCode = "000";
        }
        return areaTypeCode;
    }

    public List<ClaimDocument> getClaimDocuments(Claim claim, List<ClaimDocumentDTO> claimDocumentDTOS) {
        List<ClaimDocument> claimDocuments = null;
        if (claimDocumentDTOS != null) {
            if (claim.getCustomerNumber() != null && !claim.getCustomerNumber().isEmpty()) {
                claimDocuments = new ArrayList<>();
                for (ClaimDocumentDTO claimDocumentDTO : claimDocumentDTOS) {
                    claimDocuments.add(getClaimDocument(claim, claimDocumentDTO));
                }
            } else {
                throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Customer number must be while adding invoice details");
            }
        }
        return claimDocuments;
    }

    private ClaimDocument getClaimDocument(Claim claim, ClaimDocumentDTO claimDocumentDTO) {
        ClaimDocument claimDocument = new ClaimDocument();
        BeanUtils.copyProperties(claimDocumentDTO, claimDocument);
        if (claimDocumentDTO.getDocumentTypeCode() != null && !claimDocumentDTO.getDocumentTypeCode().isEmpty()) {
            claimDocument.setDocumentType(documentTypeRepository.findByDocumentTypeCode(claimDocumentDTO.getDocumentTypeCode()));
        } else {
            claimDocument.setDocumentType(documentTypeRepository.findByDocumentTypeCode(""));
        }
        claimDocument.setClaimReasonDefinition(getClaimReasonDefinitionByCode(claimDocumentDTO.getClaimReasonCode()));
        claimDocument.setClaimLineDetails(getClaimLineDetails(claim, claimDocumentDTO.getClaimLineDetailDTOS(), claimDocument));
        claimDocument.setAmountUsd(sumLineDetailsAmountUsd(claimDocument.getClaimLineDetails()));
        claimDocument.setAmountForeign(sumLineDetailsAmountForeign(claimDocument.getClaimLineDetails()));
        claimDocument.setCreatedByUserId(claim.getCreatedByUserId());
        claimDocument.setModifiedByUserId(claim.getModifiedByUserId());
        claimDocument.setDocumentDate(claimDocumentDTO.getInvoiceDate().isEmpty() ? null : dateUtil.getLocalDateTime(claimDocumentDTO.getInvoiceDate()));
        claimDocument.setClaim(claim);
        return claimDocument;
    }

    public BigDecimal sumLineDetailsAmountForeign(List<ClaimLineDetail> claimLineDetails) {
        return claimLineDetails
                .stream()
                .map(ClaimLineDetail::getLineAmountForeign)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal sumLineDetailsAmountUsd(List<ClaimLineDetail> claimLineDetails) {
        // All the line items have the same exchangeRate so we are picking the index(0)
        BigDecimal exchangeRate = claimLineDetails.get(0).getExchangeRate();
        return claimLineDetails
                .stream()
                .map(ClaimLineDetail::getLineAmountUsd)
                .reduce(BigDecimal.ZERO, BigDecimal::add).multiply(exchangeRate);
    }

    public BigDecimal sumDocumentsAmountForeign(List<ClaimDocument> claimDocuments) {
        return claimDocuments
                .stream()
                .map(ClaimDocument::getAmountForeign)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal sumDocumentsAmountUsd(List<ClaimDocument> claimDocuments) {
        return claimDocuments
                .stream()
                .map(ClaimDocument::getAmountUsd)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateSquareFeet(BigDecimal quantity, String uom) {

        switch (uom) {
            case "SF":
                return quantity;

            case "SY":
                return quantity.multiply(BigDecimal.valueOf(9));

            default:
                return quantity;
        }
    }

    public List<ClaimLineDetail> getClaimLineDetails(Claim claim, List<ClaimLineDetailDTO> claimLineDetailDTOS, ClaimDocument claimDocument) {
        List<ClaimLineDetail> claimLineDetails = new ArrayList<>();
        for (ClaimLineDetailDTO claimLineDetailDTO : claimLineDetailDTOS) {
            claimLineDetails.add(getClaimLineDetail(claim, claimLineDetailDTO, claimDocument));
        }
        return claimLineDetails;
    }

    private ClaimLineDetail getClaimLineDetail(Claim claim, ClaimLineDetailDTO claimLineDetailDTO, ClaimDocument claimDocument) {
        ClaimLineDetail claimLineDetail = new ClaimLineDetail();
        if(!claimLineDetailDTO.getDyeLot().isEmpty() && claimLineDetailDTO.getDyeLot().length()>8)
        {
        	String deylot=claimLineDetailDTO.getDyeLot().substring(0, 8);
        	claimLineDetailDTO.setDyeLot(deylot);
        }
        BeanUtils.copyProperties(claimLineDetailDTO, claimLineDetail);
        claimLineDetail.setClaimReasonDefinition(getClaimReasonDefinitionByCode(claimLineDetailDTO.getClaimReasonCode()));
        if (claimLineDetailDTO.getUnitOfMeasureCode() != null && !claimLineDetailDTO.getUnitOfMeasureCode().isEmpty()) {
            claimLineDetail.setUnitOfMeasure(unitOfMeasureRepository.findByUnitOfMeasureCode(claimLineDetailDTO.getUnitOfMeasureCode()));
        } else {
            claimLineDetail.setUnitOfMeasure(unitOfMeasureRepository.findByUnitOfMeasureCode(""));
        }
        if(claimLineDetailDTO.getSalesDiscount() != null)
        {
            claimLineDetail.setSalesDiscount(claimLineDetailDTO.getSalesDiscount());
        }
        else {
        	 claimLineDetail.setSalesDiscount(BigDecimal.valueOf(0.00));
        }
        claimLineDetail.setSalesReviewStatus(salesReviewStatusRepository.findBySalesReviewStatusCode(""));
        claimLineDetail.setSquareFeet(claimLineDetailDTO.getSquareFeet() == null ? calculateSquareFeet(claimLineDetailDTO.getQuantity(), claimLineDetailDTO.getUnitOfMeasureCode()) : claimLineDetailDTO.getSquareFeet());
        if (claimLineDetailDTO.getRcsCode() != null && !claimLineDetailDTO.getRcsCode().isEmpty()) {
            claimLineDetail.setRcsCodes(rcsCodesRepository.findByRcsCode(claimLineDetailDTO.getRcsCode()));
        } else {
            claimLineDetail.setRcsCodes(rcsCodesRepository.findByRcsCode(""));
        }
        if (claimLineDetailDTO.getDetailTypeCode() != null && !claimLineDetailDTO.getDetailTypeCode().isEmpty()) {
            claimLineDetail.setDetailType(detailTypeRepository.findByDetailTypeCode(claimLineDetailDTO.getDetailTypeCode()));
        } else {
            claimLineDetail.setDetailType(detailTypeRepository.findByDetailTypeCode(" "));
        }
        claimLineDetail.setDetailStatusType(claimLineDetailDTO.getDetailStatusTypeCode() == null||
        		claimLineDetailDTO.getDetailStatusTypeCode().isEmpty() ?detailStatusTypeRepository.findByDetailStatusTypeCode(" ") :
                detailStatusTypeRepository.findByDetailStatusTypeCode(claimLineDetailDTO.getDetailStatusTypeCode()));
        if (claimLineDetailDTO.getLineSourceCode() != null && !claimLineDetailDTO.getLineSourceCode().isEmpty()) {
            claimLineDetail.setLineSource(lineSourceRepository.findByLineSourceCode(claimLineDetailDTO.getLineSourceCode()));
        } else {
            claimLineDetail.setLineSource(lineSourceRepository.findByLineSourceCode(" "));
        }
        claimLineDetail.setLineAddReasonId(lineAddReasonRepository.findLineAddReasonIdByLineAddReasonCode(" "));
        claimLineDetail.setDetailStatusType(detailStatusTypeRepository.findByDetailStatusTypeCode("A"));
//        /*hardcoe value neeed to remove currency code and currency code name*/
//        claimLineDetail.setCurrencyCodeDesignation("USD");
//        claimLineDetail.setCurrencyCodeName("UNITED STATES $");
        claimLineDetail.setCreatedByUserId(claim.getCreatedByUserId());
        claimLineDetail.setModifiedByUserId(claim.getModifiedByUserId());
        if (claimLineDetailDTO.getClaimDetailRecordDTOS() != null) {
            claimLineDetail.setClaimDetailRecords(getClaimDetailRecords(claimLineDetailDTO, claimLineDetail));
        }
        claimLineDetail.setClaimDocument(claimDocument);
        claimLineDetail.setClaim(claim);
        return claimLineDetail;
    }

    public ClaimReasonDefinition getClaimReasonDefinitionByCode(String claimReasonCode) {
        return claimReasonDefinitionRepository.findByClaimReasonCode((claimReasonCode != null && !claimReasonCode.isEmpty()) ? claimReasonCode : "ALL");
    }

    private List<ClaimDetailRecord> getClaimDetailRecords(ClaimLineDetailDTO claimLineDetailDTO, ClaimLineDetail claimLineDetail) {
        List<ClaimDetailRecord> claimDetailRecords = new ArrayList<>();
        for (ClaimDetailRecordDTO claimDetailRecordDTO : claimLineDetailDTO.getClaimDetailRecordDTOS()) {
            ClaimDetailRecord claimDetailRecord = new ClaimDetailRecord();
            BeanUtils.copyProperties(claimDetailRecordDTO, claimDetailRecord);
            if (!claimDetailRecordDTO.getDetailRecordTypeCode().isEmpty()) {
                claimDetailRecord.setDetailRecordType(detailRecordTypeRepository.findByDetailRecordTypeCode(claimDetailRecordDTO.getDetailRecordTypeCode()));
            } else {
                claimDetailRecord.setDetailRecordType(detailRecordTypeRepository.findByDetailRecordTypeCode("INV"));
            }
            claimDetailRecord.setAmountUsd(
                    (claimDetailRecordDTO.getAmountUsd().multiply(claimDetailRecordDTO.getExchangeRate())));
            claimDetailRecord.setAmountForeign(claimDetailRecordDTO.getAmountUsd());

            claimDetailRecord.setUnitPriceUsd(
                    claimDetailRecordDTO.getUnitPriceUsd().multiply(claimDetailRecordDTO.getExchangeRate()));
            claimDetailRecord.setUnitPriceForeign(claimDetailRecordDTO.getUnitPriceUsd());

            if (claimDetailRecordDTO.getExchangeRate() != null) {
                claimDetailRecord.setExchangeRate(claimDetailRecordDTO.getExchangeRate());
            } else {
                claimDetailRecord.setExchangeRate(BigDecimal.ZERO);
            }
            claimDetailRecord.setClaimLineDetail(claimLineDetail);
            claimDetailRecord.setCreatedByUserId(claimLineDetail.getCreatedByUserId());
            claimDetailRecord.setModifiedByUserId(claimLineDetail.getModifiedByUserId());
            claimDetailRecords.add(claimDetailRecord);
        }
        return claimDetailRecords;
    }

    public List<ClaimAddress> getContactDetail(Claim claim) {
        List<ClaimAddress> claimAddresses = null;
        if (claim.getCustomerNumber() != null && !claim.getCustomerNumber().isEmpty()) {
            Set<ContactDetailsDTO> contactDetailsDTOS = getContactDetails(claim.getCustomerNumber());
            claimAddresses = new ArrayList<>();
            contactDetailsDTOS.stream()
                    .map(contactDetailsDTO -> getContactDetail(claim, contactDetailsDTO))
                    .forEach(claimAddresses::add);
        } else if (claim.getCustomerNumber().isEmpty() && claim.getEndUserInformation() != null) {
            claimAddresses = new ArrayList<>();
            claimAddresses.add(getContactDetailFromEndUserInformation(claim));
        }
        return claimAddresses;
    }

    private ClaimAddress getContactDetailFromEndUserInformation(Claim claim) {
        ClaimAddress claimAddress = new ClaimAddress();
        BeanUtils.copyProperties(claim.getEndUserInformation(), claimAddress);
        claimAddress.setFullName(claim.getEndUserInformation().getFirstName() + claim.getEndUserInformation().getMiddleInitial() + claim.getEndUserInformation().getLastName());
        claimAddress.setAddressTypeId(addressTypeRepository.findAddressTypeIdByAddressTypeCode("CON"));
        claimAddress.setPhoneNumber(claim.getEndUserInformation().getBusinessPhoneNumber());
        claimAddress.setPhoneDialCodeId(claim.getEndUserInformation().getBusinessPhoneDialCodeId());
        claimAddress.setPhoneExtension(claim.getEndUserInformation().getBusinessPhoneExtension());
        claimAddress.setCreatedByUserId(claim.getCreatedByUserId());
        claimAddress.setModifiedByUserId(claim.getModifiedByUserId());
        claimAddress.setClaim(claim);
        return claimAddress;
    }

    private ClaimAddress getContactDetail(Claim claim, ContactDetailsDTO contactDetailsDTO) {
        ClaimAddress claimAddress = new ClaimAddress();
        String firstLetter = "C", categoryCode = "", addressTypeCode = "";
        int addressTypeID = 0, countryId = 0, stateId = 0, businessPhoneDialId = 0, homePhoneDialId = 0, cellPhoneDialId = 0;
        BeanUtils.copyProperties(contactDetailsDTO, claimAddress);
        if (!contactDetailsDTO.getAddressType().isBlank()) {
            categoryCode = String.valueOf(contactDetailsDTO.getCategory().charAt(0));
            addressTypeCode = String.valueOf(contactDetailsDTO.getAddressType().charAt(0));
            addressTypeID = addressTypeRepository.findAddressTypeIdByAddressTypeCode(firstLetter + categoryCode + addressTypeCode);
        }
        if (!contactDetailsDTO.getCountry().isBlank())
            countryId = countryRepository.findCountryIdByIsoCountryCode(contactDetailsDTO.getCountry());
        if (!contactDetailsDTO.getStateProvince().isBlank() && !contactDetailsDTO.getStateProvince().equalsIgnoreCase("None") && !contactDetailsDTO.getCountry().isBlank())
            stateId = stateRepository.findStateIdByIsoStateCodeAndCountryId(contactDetailsDTO.getStateProvince(), countryId);
        claimAddress.setFullName(contactDetailsDTO.getFirstName() + contactDetailsDTO.getMiddleName() + contactDetailsDTO.getLastName());
        claimAddress.setAddressTypeId(addressTypeID);
        claimAddress.setCountryId(countryId);
        claimAddress.setStateId(stateId);
        claimAddress.setPostalCode(contactDetailsDTO.getZipCode());
        claimAddress.setPhoneNumber(contactDetailsDTO.getPhoneNumber());
       // claimAddress.setPhoneNumber((contactDetailsDTO.getAreaCode() != null ? contactDetailsDTO.getAreaCode()+"-" : "") +contactDetailsDTO.getPhoneNumber());
        claimAddress.setEmailAddress(contactDetailsDTO.getEmail());
        claimAddress.setCreatedByUserId(claim.getCreatedByUserId());
        claimAddress.setModifiedByUserId(claim.getModifiedByUserId());
        claimAddress.setClaim(claim);
        return claimAddress;
    }

    private String getNextClaimNumber(ClaimDTO claimDto) {
        String claimNumber = String.format("%06d", claimRepository.getNextClaimNumber());
        List<Claim> claimList = claimRepository.findByClaimNumber(claimNumber);
        if (claimList.size() > 0) {
            for (Claim clm : claimList) {
                if (StringUtils.isNotEmpty(claimDto.getCustomerNumber())
                        && StringUtils.isNotEmpty(clm.getCustomerNumber())
                        && claimDto.getCustomerNumber().equalsIgnoreCase(clm.getCustomerNumber())) {
                    claimNumber = String.format("%06d", claimRepository.getNextClaimNumber());
                }
            }
        }
        return claimNumber;
    }

    private void setClaimInitiationResponseDTO(ClaimInitiationResponseDTO claimInitiationResponseDTO, Claim claimResponse) {
        claimInitiationResponseDTO.setClaimId(claimResponse.getClaimId());
        claimInitiationResponseDTO.setClaimNumber(claimResponse.getClaimNumber());
        claimInitiationResponseDTO.setCustomerNumber(claimResponse.getCustomerNumber());
        claimInitiationResponseDTO.setClaimCategoryCode(claimResponse.getClaimCategory().getClaimCategoryCode());
        claimInitiationResponseDTO.setReasonCode(claimResponse.getClaimReasonDefinition().getClaimReasonCode());
        claimInitiationResponseDTO.setClaimAmount(claimResponse.getClaimAmountUsd());
        claimInitiationResponseDTO.setJobStopped(claimResponse.getJobStopped());
        claimInitiationResponseDTO.setPriorityClaim(claimResponse.getPriorityClaim());
    }

    private EndUserInformation getEndUserInformation(Claim claim, EndUserInformationDTO endUserInformationDTO) {
        EndUserInformation endUserInformation = null;
        if (endUserInformationDTO == null && claim.getCustomerNumber().isEmpty()) {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Required customer number with out sending end user information");
        } else {
            if (endUserInformationDTO != null) {
                endUserInformation = new EndUserInformation();
                BeanUtils.copyProperties(endUserInformationDTO, endUserInformation);
                endUserInformation.setCreatedByUserId(claim.getCreatedByUserId());
                endUserInformation.setModifiedByUserId(claim.getModifiedByUserId());
                endUserInformation.setClaim(claim);
            }
        }
        return endUserInformation;
    }

    @Override
    public ResponseDTO claimSubmittal(ClaimSubmittalDTO claimSubmittalDTO) {
        log.info("ClaimServiceImpl.claimSubmittal");
        ClaimBuilderDTO claimBuilderDTO = new ClaimBuilderDTO();
        ClaimInitiationResponseDTO claimInitiationResponseDTO = new ClaimInitiationResponseDTO();
            if (claimSubmittalDTO.getClaimDocumentDTOS() != null) {
                if (claimSubmittalDTO.getSubmitterCode().equalsIgnoreCase("DEBIT")) {
                    boolean checkIsDebit = claimSubmittalDTO.getClaimDocumentDTOS().stream()
                            .anyMatch(doc -> doc.getDocumentTypeCode().equalsIgnoreCase("DBT"));
                    if (!checkIsDebit)
                        throw new RuntimeException("SubmitterCode 'DEBIT' requires at least one documentType is 'DEBIT'.");
                }
            }
            convertClaimSubmittalDTOToClaimBuilderDTO(claimSubmittalDTO, claimBuilderDTO);
            Claim claim = claimBuilder.getClaim(claimBuilderDTO, ClaimType.SUBMITTAL.getType());
            Claim response = claimRepository.save(claim);
            setClaimInitiationResponseDTO(claimInitiationResponseDTO, response);
        return responseUtil.getResponse(claimInitiationResponseDTO);
    }

    private static void setError(ResponseDTO errorDTO, Boolean status, String message) {
        errorDTO.setIsSuccess(status);
        errorDTO.setMessage( message);
    }

    private void convertClaimSubmittalDTOToClaimBuilderDTO(ClaimSubmittalDTO claimSubmittalDTO, ClaimBuilderDTO claimBuilderDTO) {
        ClaimNoteDTO claimNoteDTO = null;
        BeanUtils.copyProperties(claimSubmittalDTO, claimBuilderDTO);
        ClaimBuilderEndUserInformationDTO endUserInformationDTO = null;
        ClaimBuilderInstalledFlooringDataDTO claimBuilderInstalledFlooringDataDTO = null;
        if(claimSubmittalDTO.getClaimNoteDTO() != null)
            BeanUtils.copyProperties(claimSubmittalDTO.getClaimNoteDTO(),claimNoteDTO = new ClaimNoteDTO());
        if(claimSubmittalDTO.getEndUserInformationDTO() != null)
            BeanUtils.copyProperties(claimSubmittalDTO.getEndUserInformationDTO(),endUserInformationDTO = new ClaimBuilderEndUserInformationDTO());
        if(claimSubmittalDTO.getInstalledFlooringDataDTO() != null)
            BeanUtils.copyProperties(claimSubmittalDTO.getInstalledFlooringDataDTO(),claimBuilderInstalledFlooringDataDTO = new ClaimBuilderInstalledFlooringDataDTO());
        if(claimSubmittalDTO.getClaimDocumentDTOS() != null) {
            List<ClaimBuilderDocumentDTO> claimDocumentDTOS = claimSubmittalDTO.getClaimDocumentDTOS().stream().map(doc -> {
                ClaimBuilderDocumentDTO claimDocumentDTO = new ClaimBuilderDocumentDTO();
                BeanUtils.copyProperties(doc, claimDocumentDTO);
                if (claimSubmittalDTO.getSubmitterCode().equalsIgnoreCase("DEBIT") && doc.getDocumentTypeCode().equalsIgnoreCase("DBT")) {
                    claimDocumentDTO.setDocumentStatusCode(doc.getDocumentStatusCode());
                    claimDocumentDTO.setDocumentStatusDescription(doc.getDocumentStatusDescription());
                } else {
                    claimDocumentDTO.setDocumentStatusCode("");
                    claimDocumentDTO.setDocumentStatusDescription("");
                }
                if(doc.getClaimLineDetailDTOS() != null) {
                    claimDocumentDTO.setClaimLineDetailDTOS(doc.getClaimLineDetailDTOS().stream().map(line -> {
                        ClaimBuilderLineDetailDTO claimLineDetailDTO = new ClaimBuilderLineDetailDTO();
                        BeanUtils.copyProperties(line, claimLineDetailDTO);
                        if(line.getClaimDetailRecordDTOS() != null) {
                            claimLineDetailDTO.setClaimDetailRecordDTOS(line.getClaimDetailRecordDTOS().stream().map(record -> {
                                ClaimBuilderDetailRecordDTO claimDetailRecordDTO = new ClaimBuilderDetailRecordDTO();
                                BeanUtils.copyProperties(record, claimDetailRecordDTO);
                                return claimDetailRecordDTO;
                            }).collect(Collectors.toList()));
                        }
                        return claimLineDetailDTO;
                    }).collect(Collectors.toList()));
                }
                return claimDocumentDTO;
            }).collect(Collectors.toList());
            claimBuilderDTO.setClaimDocumentDTOS(claimDocumentDTOS);
        }
        if(claimSubmittalDTO.getClaimAddressDTOS() != null) {
            List<ClaimBuilderAddressDTO> claimAddressDTOS = claimSubmittalDTO.getClaimAddressDTOS().stream().map(add -> {
                ClaimBuilderAddressDTO claimAddressDTO = new ClaimBuilderAddressDTO();
                BeanUtils.copyProperties(add, claimAddressDTO);
                return claimAddressDTO;
            }).collect(Collectors.toList());
            claimBuilderDTO.setClaimAddressDTOS(claimAddressDTOS);
        }
        claimBuilderDTO.setEndUserInformationDTO(endUserInformationDTO);
        claimBuilderDTO.setClaimNoteDTO(claimNoteDTO);
        claimBuilderDTO.setInstalledFlooringDataDTO(claimBuilderInstalledFlooringDataDTO);
    }


    private Claim claimInitiation(ClaimDTO claimDto) {
        Claim claim = new Claim();
        BeanUtils.copyProperties(claimDto, claim);
        claim.setClaimCategory(getClaimCategory(claimDto.getClaimCategoryCode()));
        claim.setClaimDocuments(getClaimDocuments(claim, claimDto.getClaimDocumentDTOS()));
        claim.setEndUserInformation(getEndUserInformation(claim, claimDto.getEndUserInformationDTO()));
        claim.setClaimStatusId(claimStatusRepository.findClaimStatusIdByClaimStatusCode(Arrays.asList("O")).get(0));
        claim.setWorkStatusId(workStatusRepository.findWorkStatusIdByWorkStatusCode("001"));
        claim.setDeclineReasonId(declineReasonRepository.findDeclineReasonIdByDeclineReasonCode("000"));
        claim.setClaimAreaTypeId(areaTypeRepository.findAreaTypeIdByAreaTypeCode("000"));
        claim.setClaimAreaId(claimAreaRepository.findClaimAreaIdByClaimAreaCode(getAreaTypeCode(claim.getCustomerNumber())));
        claim.setLookup(lookupRepository.findByLookupCode(claimDto.getBusinessMarketIndicatorCode()));

        List<ClaimAddress> claimAddress= getContactDetail(claim);
        List<ClaimAddress> billAndShipAddress= setBillAndShipAddress(claimDto,claim);
        claimAddress.addAll(billAndShipAddress);
        List<ClaimAddress> removedDuplicateclaimAddress = claimAddress.stream().collect(Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(ClaimAddress::getAddressTypeId)))).stream().collect(Collectors.toList());
        claim.setClaimAddresses(removedDuplicateclaimAddress);

        setClaimAmount(claim, claimDto.getClaimDocumentDTOS());
        claim.setClaimNumber(getNextClaimNumber(claimDto));
        claim.setClaimReasonDefinition(getClaimReasonDefinitionByCode(claimDto.getClaimReasonCode()));
        claim.setExchangeRate(claimDto.getExchangeRate() != null ? claimDto.getExchangeRate() : BigDecimal.ZERO);

        setAddWatchList(claimDto, claim);
        NoteTemplate noteTemplate = getNoteTemplate(claim,claimDto.getClaimNoteDTO().getNoteTypeCode().equals(CommonConstant.NOTE_TEMPLATE_CLAIM_SUBMITTAL) ? CommonConstant.NOTE_TEMPLATE_CLAIM_SUBMITTAL : CommonConstant.NOTE_TEMPLATE_CLAIM_INITIATE);
        claim.setClaimNotes(List.of(getClaimNote(claim, claimDto.getClaimNoteDTO().getNoteTypeCode(), claimDto.getClaimNoteDTO().getClaimNoteText(), noteTemplate)));
        claim.setAuditHistory(List.of(getAuditHistory(claim, noteTemplate)));
        if(claimDto.getClaimReasonCode().equalsIgnoreCase("P01")) {
            claim.setClaimRoutes(inStoreRouting(claimDto, claim));
        }
        return claim;
    }

    private List<ClaimRoute> inStoreRouting(ClaimDTO claimSubmittalDTO,Claim claim) {
        ClaimRoutedDTO claimRoutedDTO  = new ClaimRoutedDTO();
        int userGroupId = getUserGroupsMapping(Integer.parseInt(claimSubmittalDTO.getTerritory())).get(0).getUserGroupId();
        claimRoutedDTO.setUserGroupCode(claimBatchHeaderService.getUserGroups(userGroupId).getUserGroupCode());
        claimRoutedDTO.setRoutedUserId(Integer.parseInt(claimSubmittalDTO.getTerritory()));
        claimRoutedDTO.setUserId(claimSubmittalDTO.getCreatedByUserId());
        claimRoutedDTO.setNote("InStore Claim Routing");
        return claimDetailsService.routedClaim(claimRoutedDTO,claim,true);
    }

//    private void convertClaimSubmittalDTOToClaimDTO(ClaimSubmittalDTO claimSubmittalDTO, ClaimDTO claimDTO) {
//        ClaimNoteDTO claimNoteDTO = new ClaimNoteDTO();
//        BeanUtils.copyProperties(claimSubmittalDTO, claimDTO);
//        claimDTO.setBusinessMarketIndicatorCode(lookupRepository.findByLookupCode(claimSubmittalDTO.getBusinessMarketIndicatorCode()).getLookupCode());
//        BeanUtils.copyProperties(claimSubmittalDTO.getClaimNoteDTO(),claimNoteDTO);
//        EndUserInformationDTO endUserInformationDTO = new EndUserInformationDTO();
//        BeanUtils.copyProperties(claimSubmittalDTO.getEndUserInformationDTO(),endUserInformationDTO);
//        endUserInformationDTO.setCountryId(countryRepository.findCountryIdByIsoCountryCode(claimSubmittalDTO.getEndUserInformationDTO().getCountryCode()));
//        endUserInformationDTO.setStateId(stateRepository.findStateIdByIsoStateCodeAndCountryId(claimSubmittalDTO.getEndUserInformationDTO().getStateCode(),endUserInformationDTO.getCountryId()));
//        endUserInformationDTO.setBusinessPhoneDialCodeId(dialCodeRepository.findIsoDialingCodeIdByCountryDialCode(claimSubmittalDTO.getEndUserInformationDTO().getBusinessPhoneDialCode()));
//        endUserInformationDTO.setCellPhoneDialCodeId(dialCodeRepository.findIsoDialingCodeIdByCountryDialCode(claimSubmittalDTO.getEndUserInformationDTO().getCellPhoneDialCode()));
//        endUserInformationDTO.setHomePhoneDialCodeId(dialCodeRepository.findIsoDialingCodeIdByCountryDialCode(claimSubmittalDTO.getEndUserInformationDTO().getHomePhoneDialCode()));
//        int userId = getUserID(claimSubmittalDTO.getSubmitterId());
//        claimDTO.setCreatedByUserId(userId);
//        claimDTO.setModifiedByUserId(userId);
//        List<ClaimDocumentDTO> claimDocumentDTOS = claimSubmittalDTO.getClaimDocumentDTOS().stream().map(doc -> {
//            ClaimDocumentDTO claimDocumentDTO = new ClaimDocumentDTO();
//            BeanUtils.copyProperties(doc,claimDocumentDTO);
//            if(claimSubmittalDTO.getSubmitterCode().equalsIgnoreCase("DEBIT") && doc.getDocumentTypeCode().equalsIgnoreCase("DBT")){
//                claimDocumentDTO.setDocumentStatusCode(doc.getDocumentStatusCode());
//                claimDocumentDTO.setDocumentStatusDescription(doc.getDocumentStatusDescription());
//            }else{
//                claimDocumentDTO.setDocumentStatusCode("");
//                claimDocumentDTO.setDocumentStatusDescription("");
//            }
//            claimDocumentDTO.setClaimLineDetailDTOS(doc.getClaimLineDetailDTOS().stream().map(line -> {
//                        ClaimLineDetailDTO claimLineDetailDTO = new ClaimLineDetailDTO();
//                        BeanUtils.copyProperties(line,claimLineDetailDTO);
//                        claimLineDetailDTO.setClaimDetailRecordDTOS(line.getClaimDetailRecordDTOS().stream().map(record -> {
//                            ClaimDetailRecordDTO claimDetailRecordDTO = new ClaimDetailRecordDTO();
//                            BeanUtils.copyProperties(record,claimDetailRecordDTO);
//                            return claimDetailRecordDTO;
//                        }).collect(Collectors.toList()));
//                        return claimLineDetailDTO;
//                    }).collect(Collectors.toList()));
//            return claimDocumentDTO;
//        }).collect(Collectors.toList());
//        List<ClaimAddressDTO> claimAddressDTOS = claimSubmittalDTO.getClaimAddressesDTOS().stream().map(add -> {
//            ClaimAddressDTO claimAddressDTO = new ClaimAddressDTO();
//            BeanUtils.copyProperties(add,claimAddressDTO);
//            return claimAddressDTO;
//        }).collect(Collectors.toList());
//        claimDTO.setClaimDocumentDTOS(claimDocumentDTOS);
//        claimDTO.setClaimAddressDTO(claimAddressDTOS);
//        claimDTO.setEndUserInformationDTO(endUserInformationDTO);
//        claimDTO.setClaimNoteDTO(claimNoteDTO);
//    }

    public ClaimCategory getClaimCategory(String claimCategoryCode) {
        ClaimCategory claimCategory = new ClaimCategory();
        if (claimCategoryCode != null) {
            claimCategory = claimCategoryRepository.findByClaimCategoryCode(claimCategoryCode);
        } else {
            claimCategory = claimCategoryRepository.findByClaimCategoryCode("");
        }
        return claimCategory;
    }

    @Override
    public List<State> getStatesByCountryId(Integer countryId) {
        return stateRepository.getStatesByCountryId(countryId);
    }

    @Override
    public List<Country> getCountries() {
        return countryRepository.getCountries();
    }

    @Override
    public List<DialCode> getDialCodes() {
        return dialCodeRepository.getDialCodes();
    }

    @Override
    public List<PriorClaimSummaryResponseDTO> getPriorClaimSummary(PClaimDTO claim) throws CommonException {
        List<PriorClaimSummaryResponseDTO> claimSummaryList = null;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<?> query = null;
        Root<Claim> claimRoot = null;
        Root<EndUserInformation> endUserInformationRoot = null;
        List<Predicate> predicates = new ArrayList<>();

if ((claim.getClaimDocumentDTO() == null || claim.getClaimDocumentDTO().isEmpty() || claim.getClaimDocumentDTO().size()==0) && claim.getCustomerNumber() != null && !claim.getCustomerNumber().isEmpty()) {
        	throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Record Not Found!!");
        }
        if (claim.getCustomerNumber() != null && !claim.getCustomerNumber().isEmpty()) {
            query = cb.createQuery(Claim.class);
            claimRoot = query.from(Claim.class);
            predicates.add(cb.equal(claimRoot.get("customerNumber"), claim.getCustomerNumber()));
        }
        if (claim.getEndUserInformationDTO() != null) {
            if (claim.getCustomerNumber() != null && !claim.getCustomerNumber().isEmpty()) {
                Join<Claim, EndUserInformation> endUserJoin = claimRoot.join("endUserInformation");
                if (claim.getEndUserInformationDTO().getFirstName() != null && !claim.getEndUserInformationDTO().getFirstName().isEmpty()) {
                    predicates.add(cb.equal(endUserJoin.get("firstName"), claim.getEndUserInformationDTO().getFirstName()));
                }
                if (claim.getEndUserInformationDTO().getLastName() != null && !claim.getEndUserInformationDTO().getLastName().isEmpty()) {
                    predicates.add(cb.equal(endUserJoin.get("lastName"), claim.getEndUserInformationDTO().getLastName()));
                }
                if (claim.getEndUserInformationDTO().getCompanyName() != null && !claim.getEndUserInformationDTO().getCompanyName().isEmpty()) {
                    predicates.add(cb.equal(endUserJoin.get("companyName"), claim.getEndUserInformationDTO().getCompanyName()));
                }
                if (claim.getEndUserInformationDTO().getAddressLine1() != null && !claim.getEndUserInformationDTO().getAddressLine1().isEmpty()) {
                    predicates.add(cb.equal(endUserJoin.get("addressLine1"), claim.getEndUserInformationDTO().getAddressLine1()));
                }
                if (claim.getEndUserInformationDTO().getAddressLine2() != null && !claim.getEndUserInformationDTO().getAddressLine2().isEmpty()) {
                    predicates.add(cb.equal(endUserJoin.get("addressLine2"), claim.getEndUserInformationDTO().getAddressLine2()));
                }
                if (claim.getEndUserInformationDTO().getBusinessPhoneNumber() != null && !claim.getEndUserInformationDTO().getBusinessPhoneNumber().isEmpty()) {
                    predicates.add(cb.equal(endUserJoin.get("businessPhoneNumber"), claim.getEndUserInformationDTO().getBusinessPhoneNumber()));
                }
                if (claim.getEndUserInformationDTO().getHomePhoneNumber() != null && !claim.getEndUserInformationDTO().getHomePhoneNumber().isEmpty()) {
                    predicates.add(cb.equal(endUserJoin.get("homePhoneNumber"), claim.getEndUserInformationDTO().getHomePhoneNumber()));
                }
                if (claim.getEndUserInformationDTO().getCellPhoneNumber() != null && !claim.getEndUserInformationDTO().getCellPhoneNumber().isEmpty()) {
                    predicates.add(cb.equal(endUserJoin.get("cellPhoneNumber"), claim.getEndUserInformationDTO().getCellPhoneNumber()));
                }
            } else {
                query = cb.createQuery(EndUserInformation.class);
                endUserInformationRoot = query.from(EndUserInformation.class);
                if (claim.getEndUserInformationDTO().getFirstName() != null && !claim.getEndUserInformationDTO().getFirstName().isEmpty()) {
                    predicates.add(cb.equal(endUserInformationRoot.get("firstName"), claim.getEndUserInformationDTO().getFirstName()));
                }
                if (claim.getEndUserInformationDTO().getLastName() != null && !claim.getEndUserInformationDTO().getLastName().isEmpty()) {
                    predicates.add(cb.equal(endUserInformationRoot.get("lastName"), claim.getEndUserInformationDTO().getLastName()));
                }
                if (claim.getEndUserInformationDTO().getCompanyName() != null && !claim.getEndUserInformationDTO().getCompanyName().isEmpty()) {
                    predicates.add(cb.equal(endUserInformationRoot.get("companyName"), claim.getEndUserInformationDTO().getCompanyName()));
                }
                if (claim.getEndUserInformationDTO().getAddressLine1() != null && !claim.getEndUserInformationDTO().getAddressLine1().isEmpty()) {
                    predicates.add(cb.equal(endUserInformationRoot.get("addressLine1"), claim.getEndUserInformationDTO().getAddressLine1()));
                }
                if (claim.getEndUserInformationDTO().getAddressLine2() != null && !claim.getEndUserInformationDTO().getAddressLine2().isEmpty()) {
                    predicates.add(cb.equal(endUserInformationRoot.get("addressLine2"), claim.getEndUserInformationDTO().getAddressLine2()));
                }
                if (claim.getEndUserInformationDTO().getBusinessPhoneNumber() != null && !claim.getEndUserInformationDTO().getBusinessPhoneNumber().isEmpty()) {
                    predicates.add(cb.equal(endUserInformationRoot.get("businessPhoneNumber"), claim.getEndUserInformationDTO().getBusinessPhoneNumber()));
                }
                if (claim.getEndUserInformationDTO().getHomePhoneNumber() != null && !claim.getEndUserInformationDTO().getHomePhoneNumber().isEmpty()) {
                    predicates.add(cb.equal(endUserInformationRoot.get("homePhoneNumber"), claim.getEndUserInformationDTO().getHomePhoneNumber()));
                }
                if (claim.getEndUserInformationDTO().getCellPhoneNumber() != null && !claim.getEndUserInformationDTO().getCellPhoneNumber().isEmpty()) {
                    predicates.add(cb.equal(endUserInformationRoot.get("cellPhoneNumber"), claim.getEndUserInformationDTO().getCellPhoneNumber()));
                }
            }
        }
        if (claim.getClaimDocumentDTO() != null && !claim.getClaimDocumentDTO().isEmpty()) {
            Join<Claim, ClaimDocument> claimDocumentsJoin = claimRoot.join("claimDocuments");
            Expression<String> documentNumberExpression = claimDocumentsJoin.get("documentNumber");           
            List<String> docNumLst = claim.getClaimDocumentDTO().stream().map(cd -> cd.getDocumentNumber()).collect(Collectors.toList());
            predicates.add(documentNumberExpression.in(docNumLst));
        }
        query.where(predicates.toArray(new Predicate[0]));
        List<Claim> claimList = null;
        if (claim.getCustomerNumber() != null && !claim.getCustomerNumber().isEmpty()) {
            claimList = (List<Claim>) entityManager.createQuery(query).getResultList();
        } else {
            List<EndUserInformation> endUserInformationList = (List<EndUserInformation>) entityManager.createQuery(query).getResultList();
            claimList = endUserInformationList.stream().map(e -> e.getClaim()).collect(Collectors.toList());
        }
        claimSummaryList = getPriorClaimSummaryResponseList(claimList, CommonConstant.SYSTEM_USER_ID,"");
        if (claimSummaryList == null)
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Record Not Found!!");
        return claimSummaryList;
    }

    @Override
    public void claimAssignment(Integer claimId, Integer claimUserId) {
        log.info("Inside ClaimServiceImpl.claimAssignment");
        Claim claim = findClaimById(claimId);
        claim.setClaimUserId(claimUserId);
        claim.setModifiedDateTime(LocalDateTime.now());
        claimRepository.save(claim);
    }

	private void assignUserToClaim(Integer claimId){
		Boolean cca = null, alignAccount = null;
		String companyType = "", areaTypeCode = "", claimsAreaCode = "",
                serviceType = ServiceTypes.FINANCIALSERVICE.toString(),
                defaultUser = "Dellis", shawOnlineUser = "cesmith";
		int userId = 0, claimAreaId = 0, areaTypeId = 0;
		ClaimLineDetail claimLineDetails = null;
        AreaTypeResponseDTO areaTypeResponseDTO = new AreaTypeResponseDTO();
		Claim claim = findClaimById(claimId);
		try {
			if(!claim.getCustomerNumber().isEmpty()) {
				ClaimAssignmentDataResponseDTO[] claimAssignmentDataResponse = getClaimAssignmentDataResponse(claim.getCustomerNumber());
				serviceType = getServiceType(serviceType, claim.getClaimCategory().getClaimCategoryName());
				// TODO implement exceptional logic with lookup type table
				for (ClaimAssignmentDataResponseDTO dataResponse : claimAssignmentDataResponse) {
					cca = dataResponse.getCcaAccount().equalsIgnoreCase("Yes") ? true : false;
                    areaTypeResponseDTO.setHomeCenterInd(dataResponse.getHomeCenter().trim().isEmpty() ? false : true);
                    areaTypeResponseDTO.setHomeCenterNbr(dataResponse.getHomeCenter().trim());
					cca = validateCcaExceptional(cca, dataResponse.getCustomerReferenceNumber());
					alignAccount = dataResponse.getAlignedAccount().equalsIgnoreCase("Yes") ? true : false;
					companyType = dataResponse.getBusinessSegment();
					claimsAreaCode = dataResponse.getClaimsArea();
				}
				if (claim.getClaimDocuments().size() != 0) {
					claimLineDetails = claim.getClaimDocuments().get(0).getClaimLineDetails().get(0);
					areaTypeCode = getAreaTypeCode(claimLineDetails.getSellingCompany(), claimLineDetails.getProductCode(), cca, alignAccount, companyType,areaTypeResponseDTO);
					claimAreaId = claimAreaRepository.findClaimAreaIdByClaimAreaCode(claimsAreaCode);
					areaTypeId = areaTypeRepository.findAreaTypeIdByAreaTypeCode(areaTypeCode);
				}
				if (!serviceType.equalsIgnoreCase(ServiceTypes.NONFINANCIALSERVICE.name())) {
					if (!claimsAreaCode.isBlank() && !areaTypeCode.isBlank()) {
						userId = claimUserAreaRepository.findUserIdByClaimAreaIdAndAreaTypeId(claimAreaId, areaTypeId);
					} else {
						userId = defaultAssignment(defaultUser);
					}
				} else {
					if (claimLineDetails.getSellingCompany().isBlank() || companyType.isBlank()) {
						userId = defaultAssignment(defaultUser);
					} else {
						userId = claimUserAreaRepository.findUserIdByClaimAreaIdAndAreaTypeId(claimAreaId, areaTypeId);
					}
				}
                if(claim.getCreatedByUserId() == defaultAssignment("Shawonline"))
                    if((claimLineDetails.getSellingCompany().equals("32") || claimLineDetails.getSellingCompany().equals("34"))
                        && !claim.getClaimCategory().getClaimCategoryName().equalsIgnoreCase("PRICING"))
                            userId = defaultAssignment(shawOnlineUser);
			}else{
				userId = defaultAssignment(defaultUser);
			}
			claim.setClaimUserId(userId);
			claim.setModifiedDateTime(LocalDateTime.now());
			claimRepository.save(claim);
		}catch (Exception e){
			userId = defaultAssignment(defaultUser);
			claim.setClaimUserId(userId);
			claim.setModifiedDateTime(LocalDateTime.now());
			claimRepository.save(claim);
		}
	}

    @Override
    public void automaticAssignment(List<Integer> claimIds) {
        log.info("Inside ClaimServiceImpl.automaticAssignment");
        for (Integer claimId : claimIds) {
            assignUserToClaim(claimId);
        }

    }

    private int defaultAssignment(String user) {
        int userId;
        userId = getUserID(user);
        return userId;
    }


    private Boolean validateCcaExceptional(Boolean cca, String customerReferenceNumber) {
        if (cca) {
            String lookupCode = lookupRepository.findLookupCodeByLookupDescription("CCA");
            if (lookupCode.equalsIgnoreCase(customerReferenceNumber))
                cca = false;
        }
        return cca;
    }

    private ClaimAssignmentDataResponseDTO[] getClaimAssignmentDataResponse(String customerNumber) {
        String url = "/customer/v1/getClaimAssignmentData?customerNumber=" + customerNumber;
        String finalUrl = restUrl + url;
        log.info("Final Url :: " + finalUrl);
        ClaimAssignmentDataResponseDTO[] claimAssignmentDataResponse = restTemplate.getForObject(finalUrl, ClaimAssignmentDataResponseDTO[].class);
        return claimAssignmentDataResponse;
    }

    private static String getServiceType(String serviceType, String claimCategoryName) {
        for (ServiceTypes serviceTypes : ServiceTypes.values()) {
            if (serviceTypes.name().equalsIgnoreCase(claimCategoryName))
                serviceType = ServiceTypes.NONFINANCIALSERVICE.name();
        }
        return serviceType;
    }

    public List<UserGroupMappingResponseDTO> getUserGroupsMapping(int userId) {
        String url = "/users/v1/getUserGroupById?userId=" + userId;
        String finalUrl = restSecurityUrl + url;
        log.info("Final Security Url :: " + finalUrl);
        ResponseEntity<List<UserGroupMappingResponseDTO>> responseEntity = restTemplate.exchange(
                finalUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserGroupMappingResponseDTO>>() {
                });
        List<UserGroupMappingResponseDTO> response = responseEntity.getBody();
        if (response == null || response.isEmpty()) {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Record Not Found with userId :: " + userId);
        }
        return response;
    }


    public int getUserGroupId(String userGroupCode) {
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

    public int getUserID(String activeDirectoryId) {
        int userId = 0;
        String url = "/users/v1/getUserIdByActiveDirectoryId?activeDirectoryId=" + activeDirectoryId;
        String finalUrl = restSecurityUrl + url;
        log.info("Final Security Url :: " + finalUrl);
        Integer response = restTemplate.getForObject(finalUrl, Integer.class);
        if (response != null) {
            userId = response;
        }
        return userId;
    }

    private String getAreaTypeCode(String sellingCompanyCode, String productCode, Boolean cca,
                                         Boolean alignAccount, String sellingCompanyType,AreaTypeResponseDTO areaTypeResponseDTO) {
// TODO need to be replaced with db query
        List<String> rugList = ManageCodes.getRugList();
        List<String> tuftexrugList = ManageCodes.getTuftexRugList();
        List<String> promotionalGoodsList = ManageCodes.getPromotionalGoodsList();
        List<String> shawgrassList = ManageCodes.getShawgrassList();
        List<String> ecommerceRugList = ManageCodes.getEcommerceRugList();
        List<String> ecommerceRugProductCodeList = ManageCodes.getEcommerceRugProductCodeList();
        List<String> floorigamiList = ManageCodes.getFloorigamiList();
        List<String> floorigamiProductCodeList = ManageCodes.getFloorigamiProductCodeList();
        List<String> resilientList1 = ManageCodes.getResilientList1();
        List<String> resilientList2 = ManageCodes.getResilientList2();
        List<String> resilientProductCodeList = ManageCodes.getResilientProductCodeList();
        List<String> hardwoodList = ManageCodes.getHardwoodList();
        List<String> hardwoodProductCodeList = ManageCodes.getHardwoodProductCodeList();
        List<String> laminateList = ManageCodes.getLaminateList();
        List<String> laminateProductCodeList = ManageCodes.getLaminateProductCodeList();
        List<String> ceramicList = ManageCodes.getCeramicList();
        List<String> ceramicProductCodeList = ManageCodes.getCeramicProductCodeList();
        List<String> carpetList = ManageCodes.getCarpetList();

        MaterialType materialType = null;

        if (cca) {
            materialType = MaterialType.CCA;
        } else if (alignAccount) {
            materialType = MaterialType.ALIGNED;
        } else if (sellingCompanyType.equalsIgnoreCase("C")) {
            materialType = MaterialType.COMMERCIAL;
        } else if (!sellingCompanyType.equalsIgnoreCase("C")) {
            materialType = MaterialType.RESIDENTIAL;
        }
        if (rugList.contains(sellingCompanyCode)) {
            materialType = MaterialType.RUG;
        } else if (tuftexrugList.contains(sellingCompanyCode)) {
            materialType = MaterialType.TUFTEX_RUG;
        } else if (promotionalGoodsList.contains(sellingCompanyCode)) {
            materialType = MaterialType.PROMOTIONAL_GOODS;
        } else if (shawgrassList.contains(sellingCompanyCode)) {
            materialType = MaterialType.SHAW_GRASS;
        } else if (ecommerceRugList.contains(sellingCompanyCode)) {
            if (ecommerceRugProductCodeList.contains(productCode)) {
                materialType = MaterialType.ECOMMERCE_RUG;
            }
        } else if (floorigamiList.contains(sellingCompanyCode)) {
            if (floorigamiProductCodeList.contains(productCode)) {
                materialType = MaterialType.FLOORIGAMI;
            }
        }
        if (!sellingCompanyType.equalsIgnoreCase("C")) {
            if ((resilientList1.contains(sellingCompanyCode)) ||
                    ((resilientList2.contains(sellingCompanyCode)) && (resilientProductCodeList.contains(productCode)))) {
                if (cca) {
                    materialType = MaterialType.RESIDENTIAL_RESILIENT_CCA;
                } else if (alignAccount) {
                    materialType = MaterialType.RESIDENTIAL_RESILIENT_ALIGN;
                } else {
                    materialType = MaterialType.RESIDENTIAL_RESILIENT;
                }
            }
        }
        if (sellingCompanyType.equalsIgnoreCase("C")) {
            if ((resilientList1.contains(sellingCompanyCode)) ||
                    ((resilientList2.contains(sellingCompanyCode)) && (resilientProductCodeList.contains(productCode)))) {
                        materialType = MaterialType.COMMERCIAL_RESILIENT;
            }
        }
        if (!sellingCompanyType.equalsIgnoreCase("C")) {
            if (hardwoodList.contains(sellingCompanyCode)) {
                if (hardwoodProductCodeList.contains(productCode)) {
                    materialType = MaterialType.RESIDENTIAL_HARDWOOD;
                }
            } else if (laminateList.contains(sellingCompanyCode)) {
                if (laminateProductCodeList.contains(productCode)) {
                    materialType = MaterialType.RESIDENTIAL_LAMINATE;
                }
            } else if (ceramicList.contains(sellingCompanyCode)) {
                if (ceramicProductCodeList.contains(productCode)) {
                    materialType = MaterialType.RESIDENTIAL_CERAMIC;
                }
            } else if (carpetList.contains(sellingCompanyCode)) {
                materialType = MaterialType.RESIDENTIAL_CARPET;
            }
        }
        if (sellingCompanyType.equalsIgnoreCase("C")) {
            if (hardwoodList.contains(sellingCompanyCode)) {
                if (hardwoodProductCodeList.contains(productCode)) {
                    materialType = MaterialType.COMMERCIAL_HARDWOOD;
                }
            } else if (laminateList.contains(sellingCompanyCode)) {
                if (laminateProductCodeList.contains(productCode)) {
                    materialType = MaterialType.COMMERCIAL_LAMINATE;
                }
            } else if (ceramicList.contains(sellingCompanyCode)) {
                if (ceramicProductCodeList.contains(productCode)) {
                    materialType = MaterialType.COMMERCIAL_CERAMIC;
                }
            } else if (carpetList.contains(sellingCompanyCode)) {
                materialType = MaterialType.COMMERCIAL_CARPET;
            }
        }
        if(areaTypeResponseDTO.getHomeCenterInd()){
            if(areaTypeResponseDTO.getHomeCenterNbr().equalsIgnoreCase(CommonConstant.LOWES)){
                materialType = MaterialType.LOWES;
            }
            if(areaTypeResponseDTO.getHomeCenterNbr().equalsIgnoreCase(CommonConstant.HOME_DEPOT)){
                materialType = MaterialType.HOME_DEPOT;
            }
        }

        return materialType.getAreaTypeCode();
    }

    public ClaimStatus getClaimStatusById(Integer claimStatusId) {
        Optional<ClaimStatus> optClaimStatus = claimStatusRepository.findById(claimStatusId);
        if (optClaimStatus.isPresent())
            return optClaimStatus.get();
        else
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Record Not Found with ClaimStatusId :: " + claimStatusId);
    }

    @Override
    public WorkStatusDTO getWorkStatusById(Integer workStatusId, Integer userId) {

    	Optional<WorkStatus> optWorkStatus = workStatusRepository.findById(workStatusId);
    	if (optWorkStatus.isPresent()) {
    		WorkStatusDTO workStatusDTO = new WorkStatusDTO();
    		BeanUtils.copyProperties(optWorkStatus.get(), workStatusDTO);
    		if(userId != null) {
    			UsersDTO userDTO = getUsersDetailsByUserId(userId);
        		if (userDTO != null && optWorkStatus.get().getWorkStatusId() == 22) {
        			workStatusDTO.setWorkStatusName(optWorkStatus.get().getWorkStatusDescription() + " BY "
        					+ ((!userDTO.getFirstName().isEmpty()) ? (userDTO.getFirstName().toUpperCase() + " ") : "")
        					+ userDTO.getLastName().toUpperCase());
        		}
    		} else {
    			workStatusDTO.setWorkStatusName(optWorkStatus.get().getWorkStatusDescription());
    		}
    		return workStatusDTO;
    	} else {
    		throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
    				"Record Not Found with WorkStatusId :: " + workStatusId);
    	}
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

    @Override
    @Transactional(rollbackOn = {Exception.class})
    public void merge(MergedAndUnMergedRequestDTO mergeRequestDTO) {
        Claim pClaim = findClaimById(mergeRequestDTO.getParentClaimId());
        Claim mClaim = findClaimById(mergeRequestDTO.getMergedClaimId());
        if (mergeRequestDTO.getParentClaimId() == mergeRequestDTO.getMergedClaimId()) {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Parent Id and Merge Id should not be same");
        } else {
            validateMergedAndUnmergedClaims(pClaim, mClaim);
            updateParentClaim(mergeRequestDTO, pClaim, mClaim);
            updateMergedClaim(mergeRequestDTO, mClaim);
            mergeClaimDocuments(pClaim, mClaim.getClaimDocuments());
            claimRepository.saveAll(Arrays.asList(pClaim, mClaim));
            saveClaimMerge(mergeRequestDTO);
        }
    }

    private void saveClaimMerge(MergedAndUnMergedRequestDTO mergeRequestDTO) {
        ClaimMerge claimMerge = new ClaimMerge();
        claimMerge.setParentClaimId(mergeRequestDTO.getParentClaimId());
        claimMerge.setMergedClaimId(mergeRequestDTO.getMergedClaimId());
        claimMerge.setStatusId(StatusTypes.ACTIVE.getStatusId());
        claimMerge.setCreatedByUserId(mergeRequestDTO.getLoggedInUserId());
        claimMerge.setModifiedByUserId(mergeRequestDTO.getLoggedInUserId());
        mergeRepository.save(claimMerge);
    }

    private void updateMergedClaim(MergedAndUnMergedRequestDTO mergeRequestDTO, Claim mClaim) {
        mClaim.setClaimStatusId(claimStatusRepository.findClaimStatusIdByClaimStatusCode(Arrays.asList("C")).get(0));
        mClaim.setWorkStatusId(workStatusRepository.findWorkStatusIdByWorkStatusCode("061"));
        mClaim.setModifiedByUserId(mergeRequestDTO.getLoggedInUserId());
        mClaim.setModifiedDateTime(LocalDateTime.now());
    }

    private void updateParentClaim(MergedAndUnMergedRequestDTO mergeRequestDTO, Claim pClaim, Claim mClaim) {
        pClaim.setModifiedByUserId(mergeRequestDTO.getLoggedInUserId());
        pClaim.setModifiedDateTime(LocalDateTime.now());
        setParentClaimModifiedUserIs(pClaim);
        NoteTemplate noteTemplate = getNoteTemplate(pClaim,CommonConstant.NOTE_TEMPLATE_CLAIM_MERGE);
        List<ClaimNote> claimNotes = new ArrayList<>();
        List<AuditHistory> auditHistoryList = new ArrayList<>();
        // Creating record for parent claim
        claimNotes.add(getClaimNote(pClaim, mergeRequestDTO, noteTemplate, mClaim));
        auditHistoryList.add(getAuditHistory(pClaim, noteTemplate,mClaim,mergeRequestDTO.getLoggedInUserId()));
        // Creating record for merged claim
        claimNotes.add(getClaimNote(mClaim, mergeRequestDTO, noteTemplate, pClaim));
        auditHistoryList.add(getAuditHistory(mClaim, noteTemplate,pClaim,mergeRequestDTO.getLoggedInUserId()));
        pClaim.setClaimNotes(claimNotes);
        pClaim.setAuditHistory(auditHistoryList);
        pClaim.setClaimAmountUsd(pClaim.getClaimAmountUsd().add(mClaim.getClaimAmountUsd()));
        pClaim.setClaimAmountForeign(mClaim.getClaimAmountForeign().add(pClaim.getClaimAmountForeign()));
    }

    private void setParentClaimModifiedUserIs(Claim pClaim) {
        pClaim.getClaimDocuments().stream()
                .forEach(claimDocument -> {
                    claimDocument.setModifiedByUserId(pClaim.getModifiedByUserId());
                    claimDocument.setModifiedDateTime(LocalDateTime.now());
                    claimDocument.setClaimLineDetails(getParentClaimLineModifiedUserId(claimDocument));
                });
    }

    private List<ClaimLineDetail> getParentClaimLineModifiedUserId(ClaimDocument claimDocument) {
        List<ClaimLineDetail> claimLineDetails = claimDocument.getClaimLineDetails();
        claimLineDetails.stream()
                .map(claimLineDetail -> {
                    claimLineDetail.setModifiedByUserId(claimDocument.getModifiedByUserId());
                    claimLineDetail.setModifiedDateTime(LocalDateTime.now());
                    claimLineDetail.setClaimDetailRecords(getParentClaimRecordDetailModifiedUserId(claimLineDetail));
                    return  claimLineDetail;
                }).collect(Collectors.toList());
        return claimLineDetails;
    }

    private List<ClaimDetailRecord> getParentClaimRecordDetailModifiedUserId(ClaimLineDetail claimLineDetail) {
        List<ClaimDetailRecord> claimDetailRecords = claimLineDetail.getClaimDetailRecords();
        claimDetailRecords.stream()
                .map(claimDetailRecord -> {
                    claimDetailRecord.setModifiedByUserId(claimLineDetail.getModifiedByUserId());
                    claimDetailRecord.setModifiedDateTime(LocalDateTime.now());
                    return claimDetailRecord;
                }).collect(Collectors.toList());
        return claimDetailRecords;
    }

    private void validateMergedAndUnmergedClaims(Claim pClaim, Claim mClaim) {
       // return pClaim.getClaimReasonDefinition().getClaimReasonCode() != "ALL" && mClaim.getClaimReasonDefinition().getClaimReasonCode() != "ALL";
    	boolean openTask = false;
		boolean rga = false;
    	if(pClaim!=null && pClaim.getClaimReasonDefinition().getClaimReasonCode().equalsIgnoreCase("ALL") &&
    			mClaim.getClaimReasonDefinition().getClaimReasonCode().equalsIgnoreCase("ALL"))
    	{
    		throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Reason Code not available");
    	}
    	if (pClaim != null
				&& pClaim.getWorkStatusId() == workStatusRepository.findWorkStatusIdByWorkStatusCode("009")) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Parent Claim Work status should not be Assigned to ISC.");
		}

		if (mClaim.getCreditMemoApproved()) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Credit memo has been issued on child claim, it can not be merged");
		}

		if (mClaim.getWorkStatusId() == workStatusRepository.findWorkStatusIdByWorkStatusCode("004")) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Request Inspection has been raised on child claim, it can not be merged");
		}

		if (rga) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"RGA has been raised on child claim, it can not be merged");
		}

		if (openTask) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Open task is associated with the child claim, it can not be merged");
		}
    }


    private void mergeClaimDocuments(Claim pClaim, List<ClaimDocument> mergeClaimDocuments) {
        for (ClaimDocument mergeClaimDocument : mergeClaimDocuments) {
            pClaim.getClaimDocuments().add(getMergeClaimDocument(pClaim, mergeClaimDocument));
        }
    }

    @Override
    public ClaimUpdateAddressDTO updateClaimAddress(ClaimUpdateAddressDTO claimUpdateAddressDTO) {
        ClaimAddress claimAddress = claimAddressRepository.findById(claimUpdateAddressDTO.getClaimAddressId()).get();
        if(null!=claimUpdateAddressDTO && !claimUpdateAddressDTO.getPhoneNumber().isEmpty()) {
        String phoneFormat=claimUpdateAddressDTO.getPhoneNumber().replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1)$2-$3");
        claimUpdateAddressDTO.setPhoneNumber(phoneFormat);
        }
        BeanUtils.copyProperties(claimUpdateAddressDTO, claimAddress);
        ClaimAddress savedClaimAddress = claimAddressRepository.save(claimAddress);
        BeanUtils.copyProperties(savedClaimAddress, claimUpdateAddressDTO);
        workHistory.updateWorkHistory(savedClaimAddress.getClaim(),  savedClaimAddress.getClaim().getModifiedByUserId());
        return claimUpdateAddressDTO;
    }

	private ClaimDocument getMergeClaimDocument(Claim pClaim, ClaimDocument mergeClaimDocument) {
		ClaimDocument parentClaimDocument = new ClaimDocument();
		BeanUtils.copyProperties(mergeClaimDocument,parentClaimDocument);
        mergeClaimDocument.setModifiedByUserId(pClaim.getModifiedByUserId());
        mergeClaimDocument.setModifiedDateTime(LocalDateTime.now());
        claimDocumentRepository.save(mergeClaimDocument);
		parentClaimDocument.setClaimDocumentId(0);
		parentClaimDocument.setClaim(pClaim);
		parentClaimDocument.setClaimLineDetails(getMergeClaimDocumentLineDetails(pClaim, parentClaimDocument));
		parentClaimDocument.setCreatedByUserId(pClaim.getModifiedByUserId());
		parentClaimDocument.setModifiedByUserId(pClaim.getModifiedByUserId());
        parentClaimDocument.setModifiedDateTime(LocalDateTime.now());
		return parentClaimDocument;
	}

	private List<ClaimLineDetail> getMergeClaimDocumentLineDetails(Claim pClaim, ClaimDocument parentClaimDocument) {
		List<ClaimLineDetail> claimLineDetails = new ArrayList<>();
		for (ClaimLineDetail claimLineDetail : parentClaimDocument.getClaimLineDetails()) {
			ClaimLineDetail parentClaimLineDetail = new ClaimLineDetail();
			BeanUtils.copyProperties(claimLineDetail,parentClaimLineDetail);
            claimLineDetail.setModifiedByUserId(pClaim.getModifiedByUserId());
            claimLineDetail.setModifiedDateTime(LocalDateTime.now());
            claimLineDetailsRepository.save(claimLineDetail);
			parentClaimLineDetail.setClaimLineId(0);
			parentClaimLineDetail.setLineSource(lineSourceRepository.findByLineSourceCode("MERG"));
			parentClaimLineDetail.setClaimDocument(parentClaimDocument);
			parentClaimLineDetail.setClaimDetailRecords(getMergeClaimDetailRecords(parentClaimLineDetail, pClaim.getModifiedByUserId()));
			parentClaimLineDetail.setCreatedByUserId(pClaim.getModifiedByUserId());
			parentClaimLineDetail.setModifiedByUserId(pClaim.getModifiedByUserId());
            parentClaimLineDetail.setModifiedDateTime(LocalDateTime.now());
			parentClaimLineDetail.setClaim(pClaim);
			claimLineDetails.add(parentClaimLineDetail);
		}
		return  claimLineDetails;
	}

	private List<ClaimDetailRecord> getMergeClaimDetailRecords(ClaimLineDetail parentClaimLineDetail, int loggedInUserId) {
		List<ClaimDetailRecord> claimDetailRecords = new ArrayList<>();
		for(ClaimDetailRecord claimDetailRecord : parentClaimLineDetail.getClaimDetailRecords()){
			ClaimDetailRecord parentClaimDetailRecord = new ClaimDetailRecord();
			BeanUtils.copyProperties(claimDetailRecord,parentClaimDetailRecord);
            claimDetailRecord.setModifiedByUserId(loggedInUserId);
            claimDetailRecord.setModifiedDateTime(LocalDateTime.now());
            claimDetailRecordRepository.save(claimDetailRecord);
			parentClaimDetailRecord.setClaimDetailRecordId(0);
			parentClaimDetailRecord.setClaimLineDetail(parentClaimLineDetail);
			parentClaimDetailRecord.setCreatedByUserId(loggedInUserId);
			parentClaimDetailRecord.setModifiedByUserId(loggedInUserId);
            parentClaimDetailRecord.setModifiedDateTime(LocalDateTime.now());
			claimDetailRecords.add(parentClaimDetailRecord);
		}
		return claimDetailRecords;
	}

	@Override
	public void unMerge(MergedAndUnMergedRequestDTO unMergedRequestDTO) {
		Claim pClaim = findClaimById(unMergedRequestDTO.getParentClaimId());
		Claim mClaim = findClaimById(unMergedRequestDTO.getUnMergedClaimId());
		validateMergedAndUnmergedClaims(null, mClaim);
		ClaimMerge claimUnMerge = mergeRepository.findByParentClaimIdAndMergedClaimId(unMergedRequestDTO.getParentClaimId(),
                unMergedRequestDTO.getUnMergedClaimId(), StatusTypes.ACTIVE.getStatusId());
		if(claimUnMerge != null) {
            updateUnMergedParentClaim(unMergedRequestDTO, pClaim, mClaim);
            updateUnMergedClaim(unMergedRequestDTO, mClaim);
            claimRepository.saveAll(Arrays.asList(pClaim,mClaim));
			setClaimUnMerge(unMergedRequestDTO.getLoggedInUserId(), claimUnMerge);
		}else{
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "There is no merge claim details found in that parent and merged Ids" + unMergedRequestDTO.getParentClaimId()
                    + "-" + unMergedRequestDTO.getUnMergedClaimId());
		}
	}

    private void updateUnMergedClaim(MergedAndUnMergedRequestDTO unMergedRequestDTO, Claim mClaim) {
        mClaim.setClaimStatusId(claimStatusRepository.findClaimStatusIdByClaimStatusCode(Arrays.asList("O")).get(0));
        mClaim.setWorkStatusId(workStatusRepository.findWorkStatusIdByWorkStatusCode("062"));
        mClaim.setModifiedByUserId(unMergedRequestDTO.getLoggedInUserId());
        mClaim.setModifiedDateTime(LocalDateTime.now());
    }

    private void updateUnMergedParentClaim(MergedAndUnMergedRequestDTO unMergedRequestDTO, Claim pClaim, Claim mClaim) {
        unmergeClaimDocuments(pClaim.getClaimId(), unMergedRequestDTO.getLoggedInUserId());
        pClaim.setClaimAmountUsd(pClaim.getClaimAmountUsd().subtract(mClaim.getClaimAmountUsd()));
        pClaim.setClaimAmountForeign(pClaim.getClaimAmountForeign().subtract(mClaim.getClaimAmountForeign()));
        NoteTemplate noteTemplate = getNoteTemplate(pClaim,CommonConstant.NOTE_TEMPLATE_CLAIM_UN_MERGE);
        List<ClaimNote> claimNotes = new ArrayList<>();
        List<AuditHistory> auditHistoryList = new ArrayList<>();
        // Creating record for parent claim
        claimNotes.add(getClaimNote(pClaim, unMergedRequestDTO, noteTemplate, mClaim));
        auditHistoryList.add(getAuditHistory(pClaim, noteTemplate,mClaim,unMergedRequestDTO.getLoggedInUserId()));
        // Creating record for merged claim
        claimNotes.add(getClaimNote(mClaim, unMergedRequestDTO, noteTemplate, pClaim));
        auditHistoryList.add(getAuditHistory(mClaim, noteTemplate,pClaim,unMergedRequestDTO.getLoggedInUserId()));
        pClaim.setClaimNotes(claimNotes);
        pClaim.setAuditHistory(auditHistoryList);
        pClaim.setModifiedByUserId(unMergedRequestDTO.getLoggedInUserId());
        pClaim.setModifiedDateTime(LocalDateTime.now());
    }

    @Override
    public void deleteDocument(int claimDocumentId, int loggedInUserId) {
        ClaimDocument existClaimDocument = claimDocumentRepository.findById(claimDocumentId).get();
        Claim claim = existClaimDocument.getClaim();
		int statusId = statusRepository.findStatusIdByStatusCode("Active");
        // RGA flag set it here in future
        Boolean rga = false;
        if(existClaimDocument.getStatusId() == statusId) {
            if (existClaimDocument.isAssociate()) {
                updateDeleteDocumentDetail(claimDocumentId, loggedInUserId);
                calculateClaimAmount(loggedInUserId, existClaimDocument, claim);
            } else {
                if (!claim.getCreditMemoApproved() && !rga) {
                    updateDeleteDocumentDetail(claimDocumentId, loggedInUserId);
                    calculateClaimAmount(loggedInUserId, existClaimDocument, claim);
                }
            }
        }else{
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "The Document is not Active Status, DocumentNumber # " + existClaimDocument.getDocumentNumber() +
                            " Document ID # " + existClaimDocument.getClaimDocumentId());
        }
        claimRepository.save(claim);
    }

    @Override
    public ClaimDocument claimAssociateDocument(AssociateDocumentDTO associateDocumentDTO) {
        int claimDocumentId = 0;
        ClaimDocument claimDocument = new ClaimDocument();
        Claim claim = null;
        claim = findAlreadyAssociateDocument(associateDocumentDTO.getCustomerNumber(), associateDocumentDTO.getDocumentNumber(),associateDocumentDTO.getClaimId());
        if(claim == null) {
            claim = claimRepository.findByCustomerNumberAndDocumentNumberAndClaimId(associateDocumentDTO.getCustomerNumber(), associateDocumentDTO.getDocumentNumber(), associateDocumentDTO.getClaimId());
        }else{
            claimDocumentId = claimDocumentRepository.findClaimDocumentIdByDocumentNumberAndClaimIdAdIsAssociated(associateDocumentDTO.getDocumentNumber(),associateDocumentDTO.getClaimId());
        }
        if(claim != null) {
            claim.setClaimDocuments(getClaimDocuments(associateDocumentDTO, claimDocument, claim,claimDocumentId));
            claim.setClaimAmountUsd(claim.getClaimAmountUsd().add(claimDocument.getAmountUsd()));
            claim.setClaimAmountForeign(claim.getClaimAmountForeign().add(claimDocument.getAmountForeign()));
            claim.setModifiedByUserId(associateDocumentDTO.getModifiedByUserId());
            claim.setModifiedDateTime(LocalDateTime.now());
            Claim responseClaim = claimRepository.save(claim);
            List<ClaimDocument> claimDocuments = responseClaim.getClaimDocuments().stream()
                    .filter(document -> document.isAssociate() && document.getDocumentNumber().equalsIgnoreCase(associateDocumentDTO.getDocumentNumber()))
                    .collect(Collectors.toList());
            claimDocument = claimDocuments.get(claimDocuments.size() - 1);
        }else{
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Searched Inv # " + associateDocumentDTO.getDocumentNumber() + " is not found for the customer " + associateDocumentDTO.getCustomerNumber());
        }
        return claimDocument;
    }

    private Claim findAlreadyAssociateDocument(String customerNumber, String documentNumber, int claimId) {
        return claimRepository.findByCustomerNumberAndDocumentNumberAndClaimIdAndIsAssociated(customerNumber,documentNumber,claimId);
    }

    private void calculateClaimAmount(int loggedInUserId, ClaimDocument existClaimDocument, Claim claim) {
        claim.setClaimAmountUsd(claim.getClaimAmountUsd().subtract(existClaimDocument.getAmountUsd()));
        claim.setClaimAmountForeign(claim.getClaimAmountForeign().subtract(existClaimDocument.getAmountForeign()));
        claim.setModifiedByUserId(loggedInUserId);
        claim.setModifiedDateTime(LocalDateTime.now());
    }

    private void updateDeleteDocumentDetail(int claimDocumentId, int loggedInUserId) {
        int statusId = statusRepository.findStatusIdByStatusCode("Deleted");
        claimDocumentRepository.updateDeleteDocumentStatus(claimDocumentId, statusId, loggedInUserId);
    }

    public List<MergedUnMergedResponseDTO> unMergedClaims(int parentClaimId, String customerNumber) {
        List<Integer> claimStatusIds = claimStatusRepository.findClaimStatusIdByClaimStatusCode(Arrays.asList("O", "P"));
        List<Claim> claimList = claimRepository.findByClaimIdAndCustomerNumberAndClaimStatusId(parentClaimId, customerNumber, claimStatusIds);
        List<MergedUnMergedResponseDTO> unMergedResponseDTOS = getMergedUnMergedResponse(claimList,parentClaimId);
        return unMergedResponseDTOS;
    }

    @Override
    public List<MergedUnMergedResponseDTO> mergedClaims(int parentClaimId) {
        int statusId = statusRepository.findStatusIdByStatusCode("Active");
        List<Integer> mergedClaimId = mergeRepository.findMergedClaimIdByParentClaimId(parentClaimId, statusId);
        List<Claim> claimList = claimRepository.findAllById(mergedClaimId);
        List<MergedUnMergedResponseDTO> mergedResponseDTOS = getMergedUnMergedResponse(claimList,parentClaimId);
        return mergedResponseDTOS;
    }

    private List<MergedUnMergedResponseDTO> getMergedUnMergedResponse(List<Claim> claimList,int parentClaimId) {
        List<MergedUnMergedResponseDTO> mergedUnMergedResponseDTOS = claimList.stream()
                .map(claims -> {
                    MergedUnMergedResponseDTO mergedUnMergedResponseDTO = new MergedUnMergedResponseDTO();
                    mergedUnMergedResponseDTO.setClaimId(claims.getClaimId());
                    mergedUnMergedResponseDTO.setClaimNumber(claims.getClaimNumber());
                    mergedUnMergedResponseDTO.setClaimReasonCode(claims.getClaimReasonDefinition().getClaimReasonCode());
                    mergedUnMergedResponseDTO.setParentClaimId(parentClaimId);
                    return mergedUnMergedResponseDTO;
                }).collect(Collectors.toList());
        return mergedUnMergedResponseDTOS;
    }

    private List<ClaimDocument> getClaimDocuments(AssociateDocumentDTO associateDocumentDTO, ClaimDocument claimDocument, Claim claim,int documentTypeId) {
        List<InvoiceDetailsDTO> invoiceDetailsDTOS = getInvoiceDetails(associateDocumentDTO.getDocumentNumber(), associateDocumentDTO.getDocumentDate());
        List<SearchClaimInvoiceDetailsResponseDTO> searchClaimInvoiceDetailsResponseDTOS = getSearchClaimInvoiceDetails(associateDocumentDTO.getCustomerNumber(),associateDocumentDTO.getDocumentNumber());
        List<InvoiceHeaderResponseDTO> invoiceHeaderResponseDTOS = getInvoiceHeaders(associateDocumentDTO.getCustomerNumber(),associateDocumentDTO.getDocumentNumber(),associateDocumentDTO.getDocumentDate());
        InvoiceDetailsDTO invoiceDetailsDTO = getInvoiceDetail(invoiceDetailsDTOS, associateDocumentDTO.getDocumentNumber(), associateDocumentDTO.getDocumentDate());
        SearchClaimInvoiceDetailsResponseDTO searchClaimInvoiceDetailsResponseDTO = getSearchClaimInvoiceDetail(searchClaimInvoiceDetailsResponseDTOS,associateDocumentDTO.getDocumentNumber(),associateDocumentDTO.getCustomerNumber());
        InvoiceHeaderResponseDTO invoiceHeaderResponseDTO = getInvoiceHeader(invoiceHeaderResponseDTOS,associateDocumentDTO.getDocumentNumber(), associateDocumentDTO.getCustomerNumber());
        List<ClaimDocument> claimDocuments = new ArrayList<>();
        claimDocument.setClaimDocumentId(documentTypeId);
        claimDocument.setClaim(claim);
        claimDocument.setDocumentType(documentTypeRepository.findByDocumentTypeCode(associateDocumentDTO.getDocumentType()));
        claimDocument.setDocumentNumber(associateDocumentDTO.getDocumentNumber());
        claimDocument.setAssociate(true);
        claimDocument.setClaimReasonDefinition(claimReasonDefinitionRepository.findById(claim.getClaimReasonDefinition().getClaimReasonId()).get());
        claimDocument.setDocumentDate(associateDocumentDTO.getDocumentDate() != null ? parseStringToLocalDateTime(associateDocumentDTO.getDocumentDate()) : parseStringToLocalDateTime(invoiceDetailsDTO.getInvoiceDate()));
        claimDocument.setAmountUsd(associateDocumentDTO.getAmount() != null ? associateDocumentDTO.getAmount().get() : BigDecimal.ZERO);
        claimDocument.setAmountForeign(associateDocumentDTO.getAmount() != null ? associateDocumentDTO.getAmount().get() : BigDecimal.ZERO);
        claimDocument.setExchangeRate(BigDecimal.valueOf(invoiceHeaderResponseDTO.getExchangeRate()));
        claimDocument.setOrderNumber(associateDocumentDTO.getDocumentType().equalsIgnoreCase("INV") ? searchClaimInvoiceDetailsResponseDTO.getOrderNumber() : "");
        claimDocument.setPurchaseOrderNumber(associateDocumentDTO.getDocumentType().equalsIgnoreCase("INV") ? searchClaimInvoiceDetailsResponseDTO.getPONbr() : "");
        claimDocument.setBillofLading(associateDocumentDTO.getDocumentType().equalsIgnoreCase("INV") ? searchClaimInvoiceDetailsResponseDTO.getBolNumber() : "");
        claimDocument.setMasterBillofLading(associateDocumentDTO.getDocumentType().equalsIgnoreCase("INV") ? invoiceDetailsDTO.getMasterBolNbr() : "");
        claimDocument.setCreatedByUserId(associateDocumentDTO.getCreatedByUserId());
        claimDocument.setModifiedByUserId(associateDocumentDTO.getModifiedByUserId());
        claimDocument.setStatusId(StatusTypes.ACTIVE.getStatusId());
        claimDocuments.add(claimDocument);
        return claimDocuments;
    }

    private LocalDateTime parseStringToLocalDateTime(String documentDate) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(documentDate, dateFormatter);
        LocalTime localTime = LocalTime.MIDNIGHT;
        return LocalDateTime.of(localDate, localTime);
    }

    private SearchClaimInvoiceDetailsResponseDTO getSearchClaimInvoiceDetail(List<SearchClaimInvoiceDetailsResponseDTO> searchClaimInvoiceDetailsResponseDTOS, String documentNumber, String customerNumber) {
        return searchClaimInvoiceDetailsResponseDTOS.stream()
                .filter(searchClaimInvoiceDetailsResponseDTO -> searchClaimInvoiceDetailsResponseDTO.getCustomerNumber().equals(customerNumber)
                && searchClaimInvoiceDetailsResponseDTO.getInvoiceNbr().equals(documentNumber))
                .findFirst()
                .orElseThrow(() -> new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Searched claim Invoice Detail not fount in this customer number # "
                        + customerNumber + " And Document number # " + documentNumber));
    }

    private List<SearchClaimInvoiceDetailsResponseDTO> getSearchClaimInvoiceDetails(String customerNumber, String documentNumber) {
        List<SearchClaimInvoiceDetailsResponseDTO> searchClaimInvoiceDetailsResponseDTOS = null;
        try {
            String url = "/customer/v1/getSearchInvoice";
            String finalUrl = restUrl + url;
            log.info("Final Url :: " + finalUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            InvoiceRequestDTO invoiceRequestDTO = new InvoiceRequestDTO(customerNumber, documentNumber);
            HttpEntity<InvoiceRequestDTO> requestEntity = new HttpEntity<>(invoiceRequestDTO, headers);

            ResponseEntity<List<SearchClaimInvoiceDetailsResponseDTO>> responseEntity = restTemplate.exchange(
                    finalUrl,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<List<SearchClaimInvoiceDetailsResponseDTO>>() {}
            );

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                searchClaimInvoiceDetailsResponseDTOS = responseEntity.getBody();
            }
        } catch (Exception e) {
            log.error("Error fetching search claim invoice details", e);
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Search Claim Invoice details not found for documentNumber # " + documentNumber + " and customerNumber # " + customerNumber);
        }
        return searchClaimInvoiceDetailsResponseDTOS;
    }


    private InvoiceHeaderResponseDTO getInvoiceHeader(List<InvoiceHeaderResponseDTO> invoiceHeaderResponseDTOS, String documentNumber, String customerNumber) {
        return invoiceHeaderResponseDTOS.stream()
                .filter(invoiceHeaderResponseDTO -> invoiceHeaderResponseDTO.getCustomerNumber().equals(customerNumber)
                && invoiceHeaderResponseDTO.getInvoiceNbr().equals(documentNumber))
                .findFirst()
                .orElseThrow(() -> new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Invoice Detail not fount in this customer number # "
                        + customerNumber + " And Document number # " + documentNumber));
    }

    private List<InvoiceHeaderResponseDTO> getInvoiceHeaders(String customerNumber, String documentNumber, String documentDate) {
        List<InvoiceHeaderResponseDTO> invoiceHeaderResponseDTOS = null;
        try {
            String url = "";
            if(customerNumber != null)
                url = String.format("/customer/v1/getInvoiceHeader?customerNumber=%s",customerNumber);
            url += documentNumber != null ? String.format("&invoiceNumber=%s", documentNumber) : String.format("&invoiceNumber=%s", "");
            url += documentDate != null ? String.format("&invoiceDate=%s", documentDate) : String.format("&invoiceDate=%s", "");
            String finalUrl = restUrl + url;
            log.info("Final Url :: " + finalUrl);
            ResponseEntity<List<InvoiceHeaderResponseDTO>> responseEntity = restTemplate.exchange(
                    finalUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<InvoiceHeaderResponseDTO>>() {
                    }
            );
            if (responseEntity.getStatusCode().is2xxSuccessful())
                invoiceHeaderResponseDTOS = responseEntity.getBody();
        } catch (Exception e) {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Invoice Header details not found in this documentNumber # " + documentNumber + " and CustomerNumber # " + customerNumber);
        }
        return invoiceHeaderResponseDTOS;
    }

    private InvoiceDetailsDTO getInvoiceDetail(List<InvoiceDetailsDTO> invoiceDetailsDTOS, String docNbr, String invDate) {
        return invoiceDetailsDTOS.stream()
                .filter(invoiceDetailsDTO -> invoiceDetailsDTO.getInvoiceNbr().equalsIgnoreCase(docNbr)
                && invoiceDetailsDTO.getInvoiceDate().equals(invDate))
                .findFirst()
                .orElseThrow(() -> new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Invoice Detail not fount in Document number # "
                        + docNbr + " And Document date # " + invDate));
    }

    private void setClaimUnMerge(Integer loggedInUserId, ClaimMerge claimUnMerge) {
        int statusId = statusRepository.findStatusIdByStatusCode("Inactive");
        claimUnMerge.setStatusId(statusId);
        claimUnMerge.setModifiedByUserId(loggedInUserId);
        claimUnMerge.setModifiedDateTime(LocalDateTime.now());
        mergeRepository.save(claimUnMerge);
    }

    private BigDecimal getClaimAmountForeign(List<ClaimDocument> claimDocuments, BigDecimal parentAmountForeign) {
        BigDecimal totalLineAmountForeign = BigDecimal.ZERO;
        for (ClaimDocument claimDocument : claimDocuments) {
            BigDecimal lineAmountForeign = sumMergeLineDetailsAmountForeign(claimDocument.getClaimLineDetails());
            totalLineAmountForeign = totalLineAmountForeign.add(lineAmountForeign);
        }
        return parentAmountForeign.subtract(totalLineAmountForeign);
    }

    private BigDecimal sumMergeLineDetailsAmountForeign(List<ClaimLineDetail> claimLineDetails) {
        return claimLineDetails
                .stream()
                .map(ClaimLineDetail::getLineAmountForeign)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getClaimAmountUsd(List<ClaimDocument> claimDocuments, BigDecimal parentAmountUsd) {
        BigDecimal totalLineAmountUsd = BigDecimal.ZERO;
        for (ClaimDocument claimDocument : claimDocuments) {
            BigDecimal lineAmountUsd = sumMergeLineDetailsAmountUsd(claimDocument.getClaimLineDetails());
            totalLineAmountUsd = totalLineAmountUsd.add(lineAmountUsd);
        }
        return parentAmountUsd.subtract(totalLineAmountUsd);
    }

    private BigDecimal sumMergeLineDetailsAmountUsd(List<ClaimLineDetail> claimLineDetails) {
        return claimLineDetails
                .stream()
                .map(ClaimLineDetail::getLineAmountUsd)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void unmergeClaimDocuments(int parentClaimId, int loggedInUserId) {
        LineSource lineSource = lineSourceRepository.findByLineSourceCode("MERG");
        int statusId = statusRepository.findStatusIdByStatusCode("Inactive");
        Set<Integer> documentId = claimLineDetailsRepository.findByClaimIdAndLineSourceId(parentClaimId, lineSource.getLineSourceId());
        List<ClaimDocument> claimDocuments = documentId.stream()
                .map(id -> claimDocumentRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .peek(claimDocument -> {
                    claimDocument.setStatusId(statusId);
                    claimDocument.setCreatedByUserId(loggedInUserId);
                    claimDocument.setModifiedByUserId(loggedInUserId);
                    claimDocument.setModifiedDateTime(LocalDateTime.now());
                }).collect(Collectors.toList());
        claimDocumentRepository.saveAll(claimDocuments);
    }

    private Claim findClaimById(Integer claimId) {
        Optional<Claim> claim = claimRepository.findById(claimId);
        if (claim.isPresent()) {
            return claim.get();
        } else {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Record Not Found In This ClaimId :: " + claimId);
        }
    }


    private ClaimReasonDefinition findByReasonId(int claimReasonId) {
        Optional<ClaimReasonDefinition> claimReasonDefinition = claimReasonDefinitionRepository.findById(claimReasonId);
        if (claimReasonDefinition.isPresent()) {
            return claimReasonDefinition.get();
        } else {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Record Not Found In This ReasonId :: " + claimReasonId);
        }
    }

    public List<PriorClaimSummaryResponseDTO> getPriorClaimSummaryResponseList(List<Claim> claimList, Integer userId,String documentNumber) {
        List<PriorClaimSummaryResponseDTO> claimSummaryList = null;
        if (claimList != null && !claimList.isEmpty()) {
            claimSummaryList = claimList.stream().filter(p->p.getClaimStatusId()!=4).map(clm -> {
                return setClaimInfo(clm, userId,documentNumber);
            }).collect(Collectors.toList());
        }
        return claimSummaryList;
    }

	private PriorClaimSummaryResponseDTO setClaimInfo(Claim claimSummaryInfo, Integer userId,String documentNumber) {
        PriorClaimSummaryResponseDTO claimSummary = new PriorClaimSummaryResponseDTO();
        claimSummary.setClaimNumber(claimSummaryInfo.getClaimNumber());
        claimSummary.setClaimDate(claimSummaryInfo.getCreatedDateTime());
        claimSummary.setClaimCategory(claimSummaryInfo.getClaimCategory().getClaimCategoryName());
        claimSummary.setCustomerNumber(claimSummaryInfo.getCustomerNumber());
        claimSummary.setTotalAmount(claimSummaryInfo.getClaimAmountUsd());
        claimSummary.setStatus(claimStatusRepository.findById(claimSummaryInfo.getClaimStatusId()).get().getClaimStatusDescription());
        if (claimSummaryInfo.getEndUserInformation() != null) {
            String fName = claimSummaryInfo.getEndUserInformation().getFirstName() != null ? claimSummaryInfo.getEndUserInformation().getFirstName() : "";
            String lName = claimSummaryInfo.getEndUserInformation().getLastName() != null ? claimSummaryInfo.getEndUserInformation().getLastName() : "";
            claimSummary.setName(fName + " " + lName);
        }

        if (claimSummaryInfo.getClaimDocuments() != null && !claimSummaryInfo.getClaimDocuments().isEmpty()) {
        	if(documentNumber.isEmpty()) {
        	List<ClaimDocumentResponseDTO> invoiceData = claimSummaryInfo.getClaimDocuments().stream().map(cd -> {
        		log.info("Claim Document id :: {}", cd.getClaimDocumentId());
        		return new ClaimDocumentResponseDTO(cd.getDocumentNumber(), cd.getCreatedDateTime());
        	}).collect(Collectors.toList());
        	
        	invoiceData.forEach(invoice -> {
           		log.info("invoice id :: {}", invoice.getInvoiceNumber());
           		setPriorityNoteText(claimSummaryInfo, String.valueOf(invoice.getInvoiceNumber()), String.valueOf(invoice.getInvoiceNumber()),"PRIOR CLAIM INVOICE TEMPLATE", "#INV_NUM", 13, userId,null);
           	});
          	claimSummary.setInvoiceData(invoiceData);
          	}
        	else {
        		List<ClaimDocumentResponseDTO> invoiceData = claimSummaryInfo.getClaimDocuments().stream().filter(d->d.getDocumentNumber().equalsIgnoreCase(documentNumber)).map(cd -> {
            		log.info("Claim Document id :: {}", cd.getClaimDocumentId());
            		return new ClaimDocumentResponseDTO(cd.getDocumentNumber(), cd.getCreatedDateTime());
            	}).collect(Collectors.toList());
        		
        		
              	claimSummary.setInvoiceData(invoiceData);
        	}
        }
        
      

        if (claimSummaryInfo.getClaimReasonDefinition() != null) {
        	claimSummary.setReasonCode(claimSummaryInfo.getClaimReasonDefinition().getClaimReasonCode());
        	claimSummary.setReasonDescription(claimSummaryInfo.getClaimReasonDefinition().getClaimReasonDescription());
        }
        
        if(claimSummaryInfo.getClaimCrmHeader() != null) {
        	List<ClaimCrmNumberResponseDTO> creditMemoList = claimSummaryInfo.getClaimCrmHeader().stream().map(crmHeader -> {
        		log.info("CRM Number :: {}", crmHeader.getCrmNumber());
        		return new ClaimCrmNumberResponseDTO(crmHeader.getCrmNumber(), crmHeader.getCreatedDateTime());
        	}).collect(Collectors.toList());
          	claimSummary.setCrmNumberList(creditMemoList);
        }

        return claimSummary;
    }


    @Override
    public Set<ContactDetailsDTO> getContactDetails(String customerNumber) {
        Set<ContactDetailsDTO> contactDetailsDTOSet = null;
        String url = "/contacts/v1/getContactDetails?customerNumber=" + customerNumber;
        String finalUrl = restUrl + url;
        log.info("Final Url :: " + finalUrl);
        ResponseEntity<Set<ContactDetailsDTO>> response = restTemplate.exchange(finalUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<Set<ContactDetailsDTO>>() {
                }
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            contactDetailsDTOSet = response.getBody();
        } else {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Record Not Found with CustomerNumber :: " + customerNumber);
        }
        return contactDetailsDTOSet;
    }

    public List<InvoiceDetailsDTO> getInvoiceDetails(String invNumber, String invDate) {
        List<InvoiceDetailsDTO> invoiceDetailsDTOS = null;
        try {
            String url = "";
            if(invNumber != null)
                url = String.format("/customer/v1/getInvoiceDetail?invNum=%s",invNumber);
            url += invDate != null ? String.format("&invDt=%s", invDate) : String.format("&invDt=%s", "");
            String finalUrl = restUrl + url;
            log.info("Final Url :: " + finalUrl);
            ResponseEntity<List<InvoiceDetailsDTO>> responseEntity = restTemplate.exchange(
                    finalUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<InvoiceDetailsDTO>>() {
                    }
            );
            if (responseEntity.getStatusCode().is2xxSuccessful())
                invoiceDetailsDTOS = responseEntity.getBody();
        } catch (Exception e) {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Invoice details not found in this documentNumber # " + invNumber);
        }
        return invoiceDetailsDTOS;
    }

	@Override
	public List<WorkStatus> getWorkStatus() {
		// TODO Auto-generated method stub
		return workStatusRepository.findAll();
	}

    @Override
    public void claimRoute(ClaimRouteRequestDTO claimRouteRequestDTO) {
        ClaimRoute claimRoute = claimRouteRepository.findByClaimId(claimRouteRequestDTO.getClaimId());
    if(claimRoute == null){
        claimRoute = new ClaimRoute();
        claimRoute.setClaim(findClaimById(claimRouteRequestDTO.getClaimId()));
    }else{
        claimRoute.setModifiedDateTime(LocalDateTime.now());
    }
        claimRoute.setRoutedToUserId(claimRouteRequestDTO.getUserId());
        claimRoute.setRoutedToUserGroupId(getUserGroupId(claimRouteRequestDTO.getUserGroupCode()));
        claimRoute.setStatusId(StatusTypes.ACTIVE.getStatusId());
        claimRoute.setCreatedByUserId(claimRouteRequestDTO.getCreatedByUserId());
        claimRoute.setModifiedByUserId(claimRouteRequestDTO.getModifiedByUserId());
        claimRouteRepository.save(claimRoute);
    }

    @Override
    public Object getClaim(String claimNumber, String type) {
        return claimBuilderService.getClaim(claimNumber,type);
    }

    @Override
    public ClaimInitiationResponseDTO claimInitiate(ClaimDTO claimDTO) {
        ClaimBuilderDTO claimBuilderDTO = new ClaimBuilderDTO();
        convertClaimDTOToClaimBuilderDTO(claimDTO,claimBuilderDTO);
        ClaimInitiationResponseDTO claimInitiationResponseDTO = new ClaimInitiationResponseDTO();
        Claim claimResponse = claimRepository.save(claimBuilder.getClaim(claimBuilderDTO,ClaimType.INITIATION.getType()));
        setClaimInitiationResponseDTO(claimInitiationResponseDTO, claimResponse);
        return claimInitiationResponseDTO;
    }

    private void convertClaimDTOToClaimBuilderDTO(ClaimDTO claimDTO, ClaimBuilderDTO claimBuilderDTO) {
        ClaimNoteDTO claimNoteDTO = null;
        BeanUtils.copyProperties(claimDTO, claimBuilderDTO);
        if(claimDTO.getClaimNoteDTO() != null)
            BeanUtils.copyProperties(claimDTO.getClaimNoteDTO(),claimNoteDTO = new ClaimNoteDTO());
        ClaimBuilderEndUserInformationDTO endUserInformationDTO = null;
        if(claimDTO.getEndUserInformationDTO() != null)
            BeanUtils.copyProperties(claimDTO.getEndUserInformationDTO(),endUserInformationDTO = new ClaimBuilderEndUserInformationDTO());
        if(claimDTO.getClaimDocumentDTOS() != null) {
            List<ClaimBuilderDocumentDTO> claimDocumentDTOS = claimDTO.getClaimDocumentDTOS().stream().map(doc -> {
                ClaimBuilderDocumentDTO claimDocumentDTO = new ClaimBuilderDocumentDTO();
                BeanUtils.copyProperties(doc, claimDocumentDTO);
                claimDocumentDTO.setClaimLineDetailDTOS(doc.getClaimLineDetailDTOS().stream().map(line -> {
                    ClaimBuilderLineDetailDTO claimLineDetailDTO = new ClaimBuilderLineDetailDTO();
                    BeanUtils.copyProperties(line, claimLineDetailDTO);
                    claimLineDetailDTO.setClaimDetailRecordDTOS(line.getClaimDetailRecordDTOS().stream().map(record -> {
                        ClaimBuilderDetailRecordDTO claimDetailRecordDTO = new ClaimBuilderDetailRecordDTO();
                        BeanUtils.copyProperties(record, claimDetailRecordDTO);
                        return claimDetailRecordDTO;
                    }).collect(Collectors.toList()));
                    return claimLineDetailDTO;
                }).collect(Collectors.toList()));
                return claimDocumentDTO;
            }).collect(Collectors.toList());
            claimBuilderDTO.setClaimDocumentDTOS(claimDocumentDTOS);
        }
        if(claimDTO.getClaimAddressDTO() != null) {
            List<ClaimBuilderAddressDTO> claimAddressDTOS = claimDTO.getClaimAddressDTO().stream().map(add -> {
                ClaimBuilderAddressDTO claimAddressDTO = new ClaimBuilderAddressDTO();
                BeanUtils.copyProperties(add, claimAddressDTO);
                return claimAddressDTO;
            }).collect(Collectors.toList());
            claimBuilderDTO.setClaimAddressDTOS(claimAddressDTOS);
        }
        claimBuilderDTO.setEndUserInformationDTO(endUserInformationDTO);
        claimBuilderDTO.setClaimNoteDTO(claimNoteDTO);
    }

    private AuditHistory getAuditHistory(Claim claim, NoteTemplate noteTemplate) {
        AuditHistory auditHistory = new AuditHistory();
        auditHistory.setClaim(claim);
        auditHistory.setNoteTemplate(noteTemplate);
        auditHistory.setStatusId(StatusTypes.ACTIVE.getStatusId());
        auditHistory.setCreatedByUserId(claim.getCreatedByUserId());
        auditHistory.setModifiedByUserId(claim.getModifiedByUserId());
        auditHistory.setAuditHistoryText(getAuditText(claim, noteTemplate));
        return auditHistory;
    }
    private AuditHistory getAuditHistory(Claim claim, NoteTemplate noteTemplate,Claim mergedOrUnmergedClaim,int loggedInUserId) {
        AuditHistory auditHistory = new AuditHistory();
        auditHistory.setClaim(mergedOrUnmergedClaim);
        auditHistory.setNoteTemplate(noteTemplate);
        auditHistory.setStatusId(StatusTypes.ACTIVE.getStatusId());
        auditHistory.setCreatedByUserId(loggedInUserId);
        auditHistory.setModifiedByUserId(loggedInUserId);
        auditHistory.setAuditHistoryText(getAuditText(claim, noteTemplate));
        return auditHistory;
    }

    private ClaimNote getClaimNote(Claim claim, String noteTypeCode, String userNotes, NoteTemplate noteTemplate) {
        ClaimNote claimNote = new ClaimNote();
        NoteType noteType = noteTypeRepository.fetchNoteTypeByNoteTypeCode(noteTypeCode);
        claimNote.setClaimNoteText(getClaimNoteText(claim, userNotes, noteTemplate));
        claimNote.setNoteType(noteTemplate.getNoteType());
        claimNote.setNoteGroup(noteType.getNoteGroup());
        claimNote.setNoteTemplate(noteTemplate);
        claimNote.setClaim(claim);
        claimNote.setLookup(lookupRepository.findByLookupCode("P"));
        claimNote.setStatusId(StatusTypes.ACTIVE.getStatusId());
        claimNote.setCreatedByUserId(claim.getCreatedByUserId());
        claimNote.setModifiedByUserId(claim.getModifiedByUserId());
        return claimNote;
    }
    private ClaimNote getClaimNote(Claim claim, MergedAndUnMergedRequestDTO mergeRequestDTO, NoteTemplate noteTemplate, Claim mergedOrUnmergedClaim) {
        ClaimNote claimNote = new ClaimNote();
        NoteType noteType = noteTypeRepository.fetchNoteTypeByNoteTypeCode(noteTemplate.getNoteType().getNoteTypeCode());
        claimNote.setClaimNoteText(getClaimNoteText(claim, mergeRequestDTO.getComment(), noteTemplate));
        claimNote.setNoteType(noteTemplate.getNoteType());
        claimNote.setNoteGroup(noteType.getNoteGroup());
        claimNote.setNoteTemplate(noteTemplate);
        claimNote.setClaim(mergedOrUnmergedClaim);
        claimNote.setLookup(lookupRepository.findByLookupCode("P"));
        claimNote.setStatusId(StatusTypes.ACTIVE.getStatusId());
        claimNote.setCreatedByUserId(mergeRequestDTO.getLoggedInUserId());
        claimNote.setModifiedByUserId(mergeRequestDTO.getLoggedInUserId());
        return claimNote;
    }
	
	public void setPriorityNoteText(Claim claim, String noteText,String auditNoteText, String templateName, String auditHistoryTemplateName, int  noteTypeId, Integer userId, String traceDate) {
		NoteTemplate noteTemplate = saveClaimNote(claim, noteText, templateName, noteTypeId, userId, traceDate);
		saveAuditHistory(claim, noteTemplate, auditHistoryTemplateName, auditNoteText, userId);
	}

	private NoteTemplate saveClaimNote(Claim claim, String noteText, String templateName, int noteTypeId, Integer userId, String traceDate) {
		ClaimNote claimNote = new ClaimNote();
		NoteTemplate noteTemplate = noteTemplateRepository.findByNoteTemplateName(templateName);
		noteTemplate.getNoteType().setNoteTypeId(noteTypeId);
		claimNote.setNoteType(noteTemplate.getNoteType());
		claimNote.setNoteGroup(noteTemplate.getNoteGroup());
		claimNote.setNoteTemplate(noteTemplate);
		claimNote.setClaim(claim);
		//below one need to clarify from Swati/Rasheed
		claimNote.setLookup(lookupRepository.findByLookupCode("P"));
		claimNote.setStatusId(1);
		if(noteTemplate.getNoteTemplateText().contains("#") && noteText != null) {
			claimNote.setClaimNoteText(noteTemplate.getNoteTemplateText().replaceAll("#", noteText));
		} else if(noteText != null && !noteText.isBlank()) {
			claimNote.setClaimNoteText(noteText);
		} else {
			claimNote.setClaimNoteText(noteTemplate.getNoteTemplateText());
		}
		if(traceDate != null) {
			TraceTask traceTask = new TraceTask();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	        LocalDateTime traceDateLocal = LocalDateTime.parse(traceDate, formatter);
	        traceTask.setTraceDate(traceDateLocal);
	        traceTask.setClaim(claim);
	        traceTask.setClaimNote(claimNote);
	        traceTask.setCreatedByUserId(userId);
	        traceTask.setModifiedByUserId(userId);	
	        traceTask.setModifiedDateTime(LocalDateTime.now());
	        traceTask.setTraceActionLink("");
	        traceTask.setTraceReference("");
	        TraceType traceType = traceTypeRepo.findByTraceTypeCode("006");
	        traceTask.setTraceType(traceType);
	        claimNote.setTraceTask(traceTask);
		}
		
		claimNote.setCreatedByUserId(userId);
		claimNote.setModifiedByUserId(userId);		
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

    public NoteTemplate getNoteTemplate(Claim claim, String noteTemplateName) {
        NoteTemplate noteTemplate = new NoteTemplate();
        if ((claim != null && claim.getCreatedByUserId().equals(defaultAssignment("Shawonline"))
                && noteTemplateName.equalsIgnoreCase(CommonConstant.NOTE_TEMPLATE_CLAIM_INITIATE))
                || noteTemplateName.equalsIgnoreCase(CommonConstant.NOTE_TEMPLATE_CLAIM_SUBMITTAL)) {
            noteTemplate = noteTemplateRepository.findByNoteTemplateName(CommonConstant.CLAIM_SUBMITTAL);
        } else if(noteTemplateName.equalsIgnoreCase(CommonConstant.NOTE_TEMPLATE_CLAIM_INITIATE)){
            noteTemplate =  noteTemplateRepository.findByNoteTemplateName(CommonConstant.INITIATION_NOTES);
        }else if(noteTemplateName.equalsIgnoreCase(CommonConstant.NOTE_TEMPLATE_CLAIM_MERGE)){
            noteTemplate = noteTemplateRepository.findByNoteTemplateName(CommonConstant.CLAIM_MERGE);
        } else if(noteTemplateName.equalsIgnoreCase(CommonConstant.NOTE_TEMPLATE_CLAIM_UN_MERGE)){
            noteTemplate = noteTemplateRepository.findByNoteTemplateName(CommonConstant.CLAIM_UNMERGE);
        } else if(noteTemplateName.equalsIgnoreCase(CommonConstant.APPROVAL_REQUIRED_CRM_TEMPLATE1)){
        	  noteTemplate = noteTemplateRepository.findByNoteTemplateName(CommonConstant.APPROVAL_REQUIRED_CRM_TEMPLATE1);	
        }else if(noteTemplateName.equalsIgnoreCase(CommonConstant.CREDID_MEMO)){
      	  noteTemplate = noteTemplateRepository.findByNoteTemplateName(CommonConstant.CREDID_MEMO);	
      }
        return noteTemplate;
    }

    private String getAuditText(Claim claim, NoteTemplate noteTemplate) {
        switch (noteTemplate.getNoteTemplateName()){
            case CommonConstant.SUBMITTAL_NOTES, CommonConstant.CLAIM_SUBMITTAL:
                return getClaimSubmittalAuditText(claim, noteTemplate);
            case CommonConstant.INITIATION_NOTES:
                return getClaimInitiateAuditText(claim, noteTemplate);
            case CommonConstant.CLAIM_MERGE,CommonConstant.CLAIM_UNMERGE:
                return getClaimMergeAndUnmergeAuditText(claim, noteTemplate);
            default:
                return "Template not in this Claim";
        }
    }

    private static String getClaimMergeAndUnmergeAuditText(Claim claim, NoteTemplate noteTemplate) {
        return noteTemplate.getAuditHistoryTemplateText()
                .replace("#CID", String.valueOf(claim.getClaimNumber()));
    }

    private static String getClaimInitiateAuditText(Claim claim, NoteTemplate noteTemplate) {
        return noteTemplate.getAuditHistoryTemplateText()
                .replace("#CAT", claim.getClaimCategory().getClaimCategoryName())
                .replace("#RC", claim.getClaimReasonDefinition().getClaimReasonCode());
    }

    private static String getClaimSubmittalAuditText(Claim claim, NoteTemplate noteTemplate) {
        return noteTemplate.getAuditHistoryTemplateText()
                .replace("#ORGCTYPE", claim.getClaimCategory().getClaimCategoryName())
                .replace("#INVNUM", claim.getClaimDocuments().stream()
                        .map(ClaimDocument::getDocumentNumber)
                        .findFirst()
                        .orElse("Not Document In This Claim "));
    }

    private String getClaimNoteText(Claim claim, String userNotes,NoteTemplate noteTemplate) {
        switch (noteTemplate.getNoteTemplateName()){
            case CommonConstant.SUBMITTAL_NOTES:
                return getClaimSubmittalNoteTemplateText(claim, noteTemplate);
            case CommonConstant.INITIATION_NOTES:
                return userNotes;
            case CommonConstant.CLAIM_MERGE,CommonConstant.CLAIM_UNMERGE:
                return getClaimMergeAndUnmergeNoteTemplateText(claim, userNotes, noteTemplate);
            case CommonConstant.CLAIM_SUBMITTAL:
                return noteTemplate.getNoteTemplateText();
            default:
                return "Template not in this Claim";
        }
    }

    private static String getClaimMergeAndUnmergeNoteTemplateText(Claim claim, String userNotes, NoteTemplate noteTemplate) {
        return noteTemplate.getNoteTemplateText()
                .replace("#CID", String.valueOf(claim.getClaimNumber()))
                .replace("#CMT", userNotes);
    }

    private static String getClaimSubmittalNoteTemplateText(Claim claim, NoteTemplate noteTemplate) {
        return noteTemplate.getNoteTemplateText()
                .replace("#CREASON", claim.getClaimReasonDefinition().getClaimReasonDescription());
    }

    private List<ClaimAddress> setBillAndShipAddress(ClaimDTO claimDto,Claim claim) {
    	List<ClaimAddress> claimAddressList=new ArrayList<>();
    if(null!=claimDto.getClaimAddressDTO())
    	for (ClaimAddressDTO addressDto : claimDto.getClaimAddressDTO()) {
    		  ClaimAddress claimAddress = new ClaimAddress();
              BeanUtils.copyProperties(addressDto,claimAddress);
    		  claimAddress.setClaim(claim);
    		  claimAddress.setAddressTypeId(addressTypeRepository.findAddressTypeIdByAddressTypeCode(addressDto.getAddressTypeCode()));
    		  claimAddress.setAddressLine1(addressDto.getAddressLine1());
    		  claimAddress.setAddressLine2(addressDto.getAddressLine2());
    		  claimAddress.setFullName(addressDto.getFullName());
    		  claimAddress.setCity(addressDto.getCity());
    		  //we will update stateId and Country Id  fields once we get country code from TDV service
    		  claimAddress.setStateId(stateRepository.findStateIdByIsoStateCodeAndCountryId("AL", 240));
    		  claimAddress.setCountryId(countryRepository.findCountryIdByIsoCountryCode("US"));
    		  claimAddress.setPostalCode(addressDto.getZipCode());
    		  claimAddress.setCreatedByUserId(claim.getCreatedByUserId());
    	      claimAddress.setModifiedByUserId(claim.getModifiedByUserId());
    	      claimAddressList.add(claimAddress);
		}
        return claimAddressList;
    }
}
