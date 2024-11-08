package com.shaw.claims.services;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.constant.ErrorCodes;
import com.shaw.claims.dto.*;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.exception.CustomValidationException;
import com.shaw.claims.model.*;
import com.shaw.claims.repo.*;
import com.shaw.claims.util.DateUtil;

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

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InStoreClaimServiceImpl implements InStoreClaimService {

    Logger log = LogManager.getLogger(InStoreClaimServiceImpl.class);

    @Autowired
    private NoteTypeRepository noteTypeRepository;
    @Autowired
    private NoteGroupRepository noteGroupRepository;
    @Autowired
    private ClaimRepository claimRepository;
    @Autowired
    private NoteTemplateRepository noteTemplateRepository;
    @Autowired
    private LookupRepository lookupRepository;
    @Autowired
    private ClaimStatusRepository claimStatusRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private StatusRepository statusRepository;
    @Value("${claim-integration.rest.url}")
    private String restUrl;
    @Autowired
    private ClaimDetailsServiceImpl claimDetailsService;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private AddressTypeRepository addressTypeRepository;
    @Autowired
    private InstoreClaimRepository instoreClaimRepository;

    @Override
    public List<InStoreClaimsResponseDTO> inStoreClaimsList(String region, String territory, String division) {
        log.info("InStoreClaimServiceImpl.inStoreClaimsList");
        List<Claim> claims = division == null ? claimRepository.findByRegionAndTerritory(region,territory,CommonConstant.CLAIM_CATEGORY_CODE) : claimRepository.findByRegionAndTerritoryAndDivision(region,territory,division,CommonConstant.CLAIM_CATEGORY_CODE);
       return getInStoreClaimsList(claims);
    }

    @Override
    public InStoreClaimResponseDTO inStoreClaimByClaimNumber(String claimNumber) {
        log.info("InStoreClaimServiceImpl.inStoreClaimByClaimNumber");
        InStoreClaimResponseDTO inStoreClaimResponseDTO = new InStoreClaimResponseDTO();
        Claim claim = claimRepository.findClaimByClaimNumber(claimNumber);
        UsersDTO usersDTO = claimDetailsService.getUsersDetailsByUserId(claim.getCreatedByUserId());
        BeanUtils.copyProperties(claim,inStoreClaimResponseDTO);
        inStoreClaimResponseDTO.setClaimReason(claim.getClaimReasonDefinition().getClaimReasonCode());
        inStoreClaimResponseDTO.setClaimStatus(getClaimStatus(claim.getClaimStatusId()));
        inStoreClaimResponseDTO.setAccountNumber(claim.getCustomerNumber());
        inStoreClaimResponseDTO.setAccountName(getInStoreClaimAccountName(claim.getCustomerNumber()));
        inStoreClaimResponseDTO.setPriceClaimed(claim.getClaimAmountUsd());
        inStoreClaimResponseDTO.setSubmittedBy(usersDTO.getFirstName() + " " + usersDTO.getLastName());
        inStoreClaimResponseDTO.setSubmittedEmailId(usersDTO.getEmailAddress());
        inStoreClaimResponseDTO.setInStoreClaimDetailsResponseDTOS(getInStoreClaimDetails(claim));
        return inStoreClaimResponseDTO;
    }


    @Override
    public InStoreClaimsDTO getClaim(String claimNumber) {
        Claim claim;
        List<Claim> claims = claimRepository.findClaimByClaimNumberOrderByDate(claimNumber);
        if(claims.isEmpty()) {
            throw new CustomValidationException(ErrorCodes.NO_RECORD_FOUND, "Claim not found for this claimNumber : " +claimNumber);
        }else {
            claim = claims.get(0);
        }

        InStoreClaimsDTO inStoreClaimsDTO = new InStoreClaimsDTO();
        BeanUtils.copyProperties(claim,inStoreClaimsDTO);
        inStoreClaimsDTO.setClaimStatus(claimStatusRepository.findClaimStatusDescriptionByClaimStatusId(claim.getClaimStatusId()));
        inStoreClaimsDTO.setClaimCategory(claim.getClaimCategory().getClaimCategoryCode());
        inStoreClaimsDTO.setReasonCode(claim.getClaimReasonDefinition().getClaimReasonCode());
        inStoreClaimsDTO.setEndUserInformationDTO(getEndUserInformation(claim.getEndUserInformation()));
        inStoreClaimsDTO.setClaimAddressDTOS(getClaimAddresses(claim.getClaimAddresses()));
        inStoreClaimsDTO.setClaimDocumentDTOS(getInStoreClaimDocuments(claim.getClaimDocuments()));
        inStoreClaimsDTO.setClaimNoteDTOS(getInStoreClaimNotes(claim.getClaimNotes()));
        return inStoreClaimsDTO;
    }

    private List<InStoreClaimNoteDTO> getInStoreClaimNotes(List<ClaimNote> claimNotes) {
        return claimNotes.stream()
                .map(note -> {
                    InStoreClaimNoteDTO inStoreClaimNoteDTO = new InStoreClaimNoteDTO();
                    BeanUtils.copyProperties(note,inStoreClaimNoteDTO);
                    return inStoreClaimNoteDTO;
                }).collect(Collectors.toList());
    }

    private List<InStoreClaimDocumentDTO> getInStoreClaimDocuments(List<ClaimDocument> claimDocuments) {
        return claimDocuments.stream()
                .map(doc -> {
                    InStoreClaimDocumentDTO inStoreClaimDocumentDTO = new InStoreClaimDocumentDTO();
                    BeanUtils.copyProperties(doc,inStoreClaimDocumentDTO);
                    inStoreClaimDocumentDTO.setDocumentType(doc.getDocumentType().getDocumentTypeCode());
                    inStoreClaimDocumentDTO.setInStoreClaimLineDTOS(getInStoreLineDetails(doc.getClaimLineDetails()));
                    return inStoreClaimDocumentDTO;
                }).collect(Collectors.toList());
    }

    private List<InStoreClaimLineDTO> getInStoreLineDetails(List<ClaimLineDetail> claimLineDetails) {
        return claimLineDetails.stream()
                .map(line -> {
                    InStoreClaimLineDTO inStoreClaimLineDTO = new InStoreClaimLineDTO();
                    BeanUtils.copyProperties(line,inStoreClaimLineDTO);
                    inStoreClaimLineDTO.setUom(line.getUnitOfMeasure().getUnitOfMeasureCode());
                    inStoreClaimLineDTO.setDetailType(line.getDetailType().getDetailTypeCode());
                    return inStoreClaimLineDTO;
                }).collect(Collectors.toList());
    }

    private List<ClaimAddressDTO> getClaimAddresses(List<ClaimAddress> claimAddresses) {
        return claimAddresses.stream()
                .map(address -> {
                    ClaimAddressDTO claimAddressDTO = new ClaimAddressDTO();
                    BeanUtils.copyProperties(address,claimAddressDTO);
                    claimAddressDTO.setFullName(address.getFirstName() + " " + address.getLastName());
                    claimAddressDTO.setAddressTypeCode(addressTypeRepository.findAddressTypeCodeByAddressTypeId(address.getAddressTypeId()));
                    claimAddressDTO.setCountry(countryRepository.findIsoCountryNameByIsoCountryId(address.getCountryId()));
                    claimAddressDTO.setState(stateRepository.findIsoStateNameByIsoStateIdAndIsoCountryId(address.getStateId(),address.getCountryId()));
                    claimAddressDTO.setZipCode(address.getPostalCode());
                    return claimAddressDTO;
                }).collect(Collectors.toList());
    }

    private InStoreClaimEndUserInformationDTO getEndUserInformation(EndUserInformation endUserInformation) {
        if(endUserInformation != null) {
            InStoreClaimEndUserInformationDTO inStoreClaimEndUserInformationDTO = new InStoreClaimEndUserInformationDTO();
            BeanUtils.copyProperties(endUserInformation, inStoreClaimEndUserInformationDTO);
            inStoreClaimEndUserInformationDTO.setCountry(countryRepository.findIsoCountryNameByIsoCountryId(endUserInformation.getCountryId()));
            inStoreClaimEndUserInformationDTO.setState(stateRepository.findIsoStateNameByIsoStateIdAndIsoCountryId(endUserInformation.getStateId(), endUserInformation.getCountryId()));
            return inStoreClaimEndUserInformationDTO;
        }
        return null;
    }

    private List<InStoreClaimsResponseDTO> getInStoreClaimsList(List<Claim> claims) {
        return claims.stream().map(instoreClaim -> {
            try {
                InStoreClaimsResponseDTO inStoreClaimsResponseDTO = new InStoreClaimsResponseDTO();
                BeanUtils.copyProperties(instoreClaim,inStoreClaimsResponseDTO);
                inStoreClaimsResponseDTO.setClaimDate(instoreClaim.getCreatedDateTime());
                inStoreClaimsResponseDTO.setDueDate(setDueDate(instoreClaim));
                inStoreClaimsResponseDTO.setAccountNumber(instoreClaim.getCustomerNumber());
                inStoreClaimsResponseDTO.setAccountName(getInStoreClaimAccountName(instoreClaim.getCustomerNumber()));
                inStoreClaimsResponseDTO.setType(instoreClaim.getClaimCategory().getClaimCategoryName());
                inStoreClaimsResponseDTO.setStatus(getInStoreClaimStatus(instoreClaim.getClaimStatusId()));
                inStoreClaimsResponseDTO.setSubmitterName("FINANCIAL SERVICE");
                return inStoreClaimsResponseDTO;
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    private Date setDueDate(Claim instoreClaim) throws ParseException {
        if(instoreClaim.getInstoreClaim() != null){
            return instoreClaim.getInstoreClaim().getDueDate() == null ? dateUtil.getDate("2099-12-31") : instoreClaim.getInstoreClaim().getDueDate();
        }else{
            return dateUtil.getDate("2099-12-31");
        }
    }

    private String getInStoreClaimStatus(int claimStatusId) {
        return claimStatusRepository.findClaimStatusDescriptionByClaimStatusId(claimStatusId);
    }

    private String getInStoreClaimAccountName(String customerNumber) {
        List<CustomerDetailsDTO> customerDetailsDTOS = null;
        String url = "/customer/v1/getCustomerDetails?customerNumber=" + customerNumber;
        String finalUrl = restUrl + url;
        log.info("Final URL :: " + finalUrl);
        ResponseEntity<List<CustomerDetailsDTO>> response = restTemplate.exchange(finalUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CustomerDetailsDTO>>() {
                });
        if(response.getStatusCode().is2xxSuccessful()){
            customerDetailsDTOS = response.getBody();
        }else {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Customer Details Not Found For This Customer Number:: " + customerNumber);
        }
        return customerDetailsDTOS.get(0).getCustomerName();
    }

    private String getClaimStatus(int claimStatusId) {
        return claimStatusRepository.findClaimStatusDescriptionByClaimStatusId(claimStatusId);
    }

    private List<InStoreClaimDetailsResponseDTO> getInStoreClaimDetails(Claim claim) {
        return claim.getClaimDocuments().stream()
                .map(claimDocument -> {
                    InStoreClaimDetailsResponseDTO inStoreClaimDetailsResponseDTO = new InStoreClaimDetailsResponseDTO();
                    BeanUtils.copyProperties(claimDocument,inStoreClaimDetailsResponseDTO);
                    inStoreClaimDetailsResponseDTO.setInvoiceNumber(claimDocument.getDocumentNumber());
                    inStoreClaimDetailsResponseDTO.setInvoiceDate(claimDocument.getDocumentDate());
                    inStoreClaimDetailsResponseDTO.setOrderDate(claimDocument.getCreatedDateTime());
                    inStoreClaimDetailsResponseDTO.setPoNumber(claimDocument.getPurchaseOrderNumber());
                    inStoreClaimDetailsResponseDTO.setShippingPoint(claimDocument.getShipVia());
                    inStoreClaimDetailsResponseDTO.setInStoreClaimLineDetailsResponseDTOS(getInStoreClaimLineDetails(claimDocument));
                    return inStoreClaimDetailsResponseDTO;
                }).collect(Collectors.toList());
    }

    private List<InStoreClaimLineDetailsResponseDTO> getInStoreClaimLineDetails(ClaimDocument claimDocument) {
        return claimDocument.getClaimLineDetails().stream()
                .map(claimLineDetail -> {
                    InStoreClaimLineDetailsResponseDTO inStoreClaimLineDetailsResponseDTO = new InStoreClaimLineDetailsResponseDTO();
                    BeanUtils.copyProperties(claimLineDetail,inStoreClaimLineDetailsResponseDTO);
                    inStoreClaimLineDetailsResponseDTO.setUom(claimLineDetail.getUnitOfMeasure().getUnitOfMeasureCode());
                    inStoreClaimLineDetailsResponseDTO.setUnitPrice(claimLineDetail.getUnitPriceUsd());
                    inStoreClaimLineDetailsResponseDTO.setStatus(getStatus(claimLineDetail.getStatusId()));
                    return inStoreClaimLineDetailsResponseDTO;
                }).collect(Collectors.toList());
    }

    private String getStatus(int statusId) {
        return statusRepository.findStatusStatusCodeByStatusId(statusId);
    }

}
