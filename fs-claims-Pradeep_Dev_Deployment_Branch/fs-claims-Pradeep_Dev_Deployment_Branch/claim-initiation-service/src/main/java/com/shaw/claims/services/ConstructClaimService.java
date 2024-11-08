package com.shaw.claims.services;

import com.shaw.claims.dto.*;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.*;
import com.shaw.claims.repo.*;
import com.shaw.claims.util.DateUtil;
import com.shaw.claims.util.TDVServiceUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class ConstructClaimService {

    @Autowired
    private ClaimCategoryRepository claimCategoryRepository;
    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;
    @Autowired
    private SalesReviewStatusRepository salesReviewStatusRepository;
    @Autowired
    private RcsCodesRepository rcsCodesRepository;
    @Autowired
    private DetailTypeRepository detailTypeRepository;
    @Autowired
    private LineAddReasonRepository lineAddReasonRepository;
    @Autowired
    private DetailStatusTypeRepository detailStatusTypeRepository;
    @Autowired
    private LineSourceRepository lineSourceRepository;
    @Autowired
    private ClaimReasonDefinitionRepository claimReasonDefinitionRepository;
    @Autowired
    private DetailRecordTypeRepository detailRecordTypeRepository;
    @Autowired
    private ClaimStatusRepository claimStatusRepository;
    @Autowired
    private WorkStatusRepository workStatusRepository;
    @Autowired
    private DeclineReasonRepository declineReasonRepository;
    @Autowired
    private AreaTypeRepository areaTypeRepository;
    @Autowired
    private ClaimAreaRepository claimAreaRepository;
    @Autowired
    private LookupRepository lookupRepository;
    @Autowired
    private ClaimRepository claimRepository;
    @Autowired
    @Lazy
    private ClaimServiceImpl claimService;
    @Autowired
    private AddressTypeRepository addressTypeRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private TDVServiceUtil tdvServiceUtil;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    DateUtil dateUtil;

    protected Claim constructClaim(ClaimBuilderDTO claimBuilderDTO){
        Claim claim = new Claim();
        BeanUtils.copyProperties(claimBuilderDTO, claim);
        claim.setClaimCategory(getClaimCategory(claimBuilderDTO.getClaimCategoryCode()));
        claim.setClaimDocuments(getClaimDocuments(claim, claimBuilderDTO.getClaimDocumentDTOS()));
        claim.setClaimStatusId(claimStatusRepository.findClaimStatusIdByClaimStatusCode(Arrays.asList("O")).get(0));
        claim.setWorkStatusId(workStatusRepository.findWorkStatusIdByWorkStatusCode("001"));
        claim.setDeclineReasonId(declineReasonRepository.findDeclineReasonIdByDeclineReasonCode("000"));
        claim.setClaimAreaTypeId(areaTypeRepository.findAreaTypeIdByAreaTypeCode("000"));
        claim.setClaimAreaId(claimAreaRepository.findClaimAreaIdByClaimAreaCode(claimService.getAreaTypeCode(claim.getCustomerNumber())));
        claim.setLookup(lookupRepository.findByLookupCode(claimBuilderDTO.getBusinessMarketIndicatorCode()));
        List<ClaimAddress> claimAddress = getContactDetail(claim);
        List<ClaimAddress> billAndShipAddress = setBillAndShipAddress(claimBuilderDTO,claim);
        claimAddress.addAll(billAndShipAddress);
        List<ClaimAddress> removedDuplicateClaimAddress = claimAddress.stream().collect(Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(ClaimAddress::getAddressTypeId)))).stream().collect(Collectors.toList());
        claim.setClaimAddresses(removedDuplicateClaimAddress);
        setClaimAmount(claim, claimBuilderDTO.getClaimDocumentDTOS());
        claim.setClaimNumber(getNextClaimNumber(claimBuilderDTO));
        claim.setClaimReasonDefinition(getClaimReasonDefinitionByCode(claimBuilderDTO.getClaimReasonCode()));
        claim.setExchangeRate(claimBuilderDTO.getExchangeRate() != null ? claimBuilderDTO.getExchangeRate() : BigDecimal.ZERO);
        return claim;
    }

    private List<ClaimAddress> setBillAndShipAddress(ClaimBuilderDTO claimBuilderDTO, Claim claim) {
        List<ClaimAddress> claimAddressList=new ArrayList<>();
        if(null!=claimBuilderDTO.getClaimAddressDTOS())
            for (ClaimBuilderAddressDTO addressDto : claimBuilderDTO.getClaimAddressDTOS()) {
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

    public List<ClaimAddress> getContactDetail(Claim claim) {
        List<ClaimAddress> claimAddresses = null;
        if (claim.getCustomerNumber() != null && !claim.getCustomerNumber().isEmpty()) {
            Set<ContactDetailsDTO> contactDetailsDTOS = tdvServiceUtil.getContactDetails(claim.getCustomerNumber());
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
    private ClaimAddress getContactDetail(Claim claim, ContactDetailsDTO contactDetailsDTO) {
        ClaimAddress claimAddress = new ClaimAddress();
        String firstLetter = "C", categoryCode = "", addressTypeCode = "";
        int addressTypeID = 0, countryId = 0, stateId = 0;
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

    private String getNextClaimNumber(ClaimBuilderDTO claimBuilderDTO) {
        String claimNumber = String.format("%06d", claimRepository.getNextClaimNumber());
        List<Claim> claimList = claimRepository.findByClaimNumber(claimNumber);
        if (claimList.size() > 0) {
            for (Claim clm : claimList) {
                if (StringUtils.isNotEmpty(claimBuilderDTO.getCustomerNumber())
                        && StringUtils.isNotEmpty(clm.getCustomerNumber())
                        && claimBuilderDTO.getCustomerNumber().equalsIgnoreCase(clm.getCustomerNumber())) {
                    claimNumber = String.format("%06d", claimRepository.getNextClaimNumber());
                }
            }
        }
        return claimNumber;
    }

    private void setClaimAmount(Claim claim, List<ClaimBuilderDocumentDTO> claimDocumentDTOS) {
        if (claimDocumentDTOS != null && (claim.getCustomerNumber() != null && !claim.getCustomerNumber().isEmpty())) {
            claim.setClaimAmountUsd(sumDocumentsAmountUsd(claim.getClaimDocuments()));
            claim.setClaimAmountForeign(sumDocumentsAmountForeign(claim.getClaimDocuments()));
        }
    }

    private BigDecimal sumDocumentsAmountForeign(List<ClaimDocument> claimDocuments) {
        return claimDocuments
                .stream()
                .map(ClaimDocument::getAmountForeign)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumDocumentsAmountUsd(List<ClaimDocument> claimDocuments) {
        return claimDocuments
                .stream()
                .map(ClaimDocument::getAmountUsd)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<ClaimDocument> getClaimDocuments(Claim claim, List<ClaimBuilderDocumentDTO> claimDocumentDTOS) {
        List<ClaimDocument> claimDocuments = null;
        if (claimDocumentDTOS != null) {
            if (claim.getCustomerNumber() != null && !claim.getCustomerNumber().isEmpty()) {
                claimDocuments = new ArrayList<>();
                for (ClaimBuilderDocumentDTO claimDocumentDTO : claimDocumentDTOS) {
                    claimDocuments.add(getClaimDocument(claim, claimDocumentDTO));
                }
            } else {
                throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Customer number must be while adding invoice details");
            }
        }
        return claimDocuments;
    }

    private ClaimDocument getClaimDocument(Claim claim, ClaimBuilderDocumentDTO claimDocumentDTO) {
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

    private List<ClaimLineDetail> getClaimLineDetails(Claim claim, List<ClaimBuilderLineDetailDTO> claimLineDetailDTOS, ClaimDocument claimDocument) {
        List<ClaimLineDetail> claimLineDetails = new ArrayList<>();
        for (ClaimBuilderLineDetailDTO claimLineDetailDTO : claimLineDetailDTOS) {
            claimLineDetails.add(getClaimLineDetail(claim, claimLineDetailDTO, claimDocument));
        }
        return claimLineDetails;
    }

    private ClaimLineDetail getClaimLineDetail(Claim claim, ClaimBuilderLineDetailDTO claimLineDetailDTO, ClaimDocument claimDocument) {
        ClaimLineDetail claimLineDetail = new ClaimLineDetail();
        BeanUtils.copyProperties(claimLineDetailDTO, claimLineDetail);
        claimLineDetail.setClaimReasonDefinition(getClaimReasonDefinitionByCode(claimLineDetailDTO.getClaimReasonCode()));
        claimLineDetail.setUnitOfMeasure(claimLineDetailDTO.getUnitOfMeasureCode() != null && !claimLineDetailDTO.getUnitOfMeasureCode().isEmpty() ?
                unitOfMeasureRepository.findByUnitOfMeasureCode(claimLineDetailDTO.getUnitOfMeasureCode()) : unitOfMeasureRepository.findByUnitOfMeasureCode(""));
        if(claimLineDetailDTO.getSalesDiscount() != null)
            claimLineDetail.setSalesDiscount(claimLineDetailDTO.getSalesDiscount());
        claimLineDetail.setSalesReviewStatus(salesReviewStatusRepository.findBySalesReviewStatusCode(""));
        claimLineDetail.setSquareFeet(claimLineDetailDTO.getSquareFeet() == null ? calculateSquareFeet(claimLineDetailDTO.getQuantity(), claimLineDetailDTO.getUnitOfMeasureCode()) : claimLineDetailDTO.getSquareFeet());
        claimLineDetail.setRcsCodes(claimLineDetailDTO.getRcsCode() != null && !claimLineDetailDTO.getRcsCode().isEmpty() ?
                rcsCodesRepository.findByRcsCode(claimLineDetailDTO.getRcsCode()) : rcsCodesRepository.findByRcsCode(""));
        claimLineDetail.setDetailType(claimLineDetailDTO.getDetailTypeCode() != null && !claimLineDetailDTO.getDetailTypeCode().isEmpty() ?
                detailTypeRepository.findByDetailTypeCode(claimLineDetailDTO.getDetailTypeCode()) : detailTypeRepository.findByDetailTypeCode(" "));
        claimLineDetail.setDetailStatusType(claimLineDetailDTO.getDetailStatusTypeCode().isEmpty()
                || claimLineDetailDTO.getDetailStatusTypeCode() == null ?
                detailStatusTypeRepository.findByDetailStatusTypeCode(" ") :
                detailStatusTypeRepository.findByDetailStatusTypeCode(claimLineDetailDTO.getDetailStatusTypeCode()));
        claimLineDetail.setLineSource(claimLineDetailDTO.getLineSourceCode() != null || !claimLineDetailDTO.getLineSourceCode().isEmpty() ?
                lineSourceRepository.findByLineSourceCode(claimLineDetailDTO.getLineSourceCode()) : lineSourceRepository.findByLineSourceCode(" "));
        claimLineDetail.setLineAddReasonId(lineAddReasonRepository.findLineAddReasonIdByLineAddReasonCode(" "));
        claimLineDetail.setDetailStatusType(detailStatusTypeRepository.findByDetailStatusTypeCode("A"));
        claimLineDetail.setCreatedByUserId(claim.getCreatedByUserId());
        claimLineDetail.setModifiedByUserId(claim.getModifiedByUserId());
        if (claimLineDetailDTO.getClaimDetailRecordDTOS() != null)
            claimLineDetail.setClaimDetailRecords(getClaimDetailRecords(claimLineDetailDTO.getClaimDetailRecordDTOS(), claimLineDetail));
        claimLineDetail.setClaimDocument(claimDocument);
        claimLineDetail.setClaim(claim);
        return claimLineDetail;
    }

    private List<ClaimDetailRecord> getClaimDetailRecords(List<ClaimBuilderDetailRecordDTO> claimDetailRecordDTOS, ClaimLineDetail claimLineDetail) {
        return claimDetailRecordDTOS.stream().map(claimDetailRecordDTO -> {
            ClaimDetailRecord claimDetailRecord = new ClaimDetailRecord();
            BeanUtils.copyProperties(claimDetailRecordDTO, claimDetailRecord);
            claimDetailRecord.setDetailRecordType(!claimDetailRecordDTO.getDetailRecordTypeCode().isEmpty() ?
                    detailRecordTypeRepository.findByDetailRecordTypeCode(claimDetailRecordDTO.getDetailRecordTypeCode()) : detailRecordTypeRepository.findByDetailRecordTypeCode("INV"));
            claimDetailRecord.setAmountUsd(claimDetailRecordDTO.getAmountUsd().multiply(claimDetailRecordDTO.getExchangeRate()));
            claimDetailRecord.setAmountForeign(claimDetailRecordDTO.getAmountUsd());
            claimDetailRecord.setUnitPriceUsd(claimDetailRecordDTO.getUnitPriceUsd().multiply(claimDetailRecordDTO.getExchangeRate()));
            claimDetailRecord.setUnitPriceForeign(claimDetailRecordDTO.getUnitPriceUsd());
            claimDetailRecord.setExchangeRate(claimDetailRecordDTO.getExchangeRate() != null ?
                    claimDetailRecordDTO.getExchangeRate() : BigDecimal.ZERO);
            claimDetailRecord.setClaimLineDetail(claimLineDetail);
            claimDetailRecord.setCreatedByUserId(claimLineDetail.getCreatedByUserId());
            claimDetailRecord.setModifiedByUserId(claimLineDetail.getModifiedByUserId());
            return claimDetailRecord;
        }).collect(Collectors.toList());
    }

    private BigDecimal calculateSquareFeet(BigDecimal quantity, String unitOfMeasureCode) {
        switch (unitOfMeasureCode) {
            case "SF":
                return quantity;

            case "SY":
                return quantity.multiply(BigDecimal.valueOf(9));

            default:
                return quantity;
        }
    }

    private BigDecimal sumLineDetailsAmountForeign(List<ClaimLineDetail> claimLineDetails) {
        return claimLineDetails
                .stream()
                .map(ClaimLineDetail::getLineAmountForeign)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumLineDetailsAmountUsd(List<ClaimLineDetail> claimLineDetails) {
        BigDecimal exchangeRate = claimLineDetails.get(0).getExchangeRate();
        return claimLineDetails
                .stream()
                .map(ClaimLineDetail::getLineAmountUsd)
                .reduce(BigDecimal.ZERO, BigDecimal::add).multiply(exchangeRate);
    }

    private ClaimReasonDefinition getClaimReasonDefinitionByCode(String claimReasonCode) {
        return claimReasonDefinitionRepository.findByClaimReasonCode((claimReasonCode != null && !claimReasonCode.isEmpty()) ? claimReasonCode : "ALL");
    }

    private ClaimCategory getClaimCategory(String claimCategoryCode) {
        return claimCategoryCode != null ? claimCategoryRepository.findByClaimCategoryCode(claimCategoryCode) : claimCategoryRepository.findByClaimCategoryCode(" ");
    }
}
