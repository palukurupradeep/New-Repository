package com.shaw.claims.services;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.dto.*;
import com.shaw.claims.model.*;
import com.shaw.claims.repo.*;
import com.shaw.claims.util.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ClaimVendorServiceImpl implements ClaimVendorService {

    Logger log = LogManager.getLogger(ClaimVendorServiceImpl.class);

    @Autowired
    private ClaimVendorRepository claimVendorRepository;
    @Autowired
    private AddressTypeRepository addressTypeRepository;
    @Autowired
    private CertifyingAgencyRepository certifyingAgencyRepository;
    @Autowired
    private InspectionServiceRepository inspectionServiceRepository;
    @Autowired
    private InspectionServiceTypeRepository inspectionServiceTypeRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private VendorAddressRepository vendorAddressRepository;
    @Autowired
    private VendorCertificateRepository vendorCertificateRepository;
    @Autowired
    private VendorServiceRepository vendorServiceRepository;
    @Autowired
    private VendorServiceTypeRepository vendorServiceTypeRepository;


    @Override
    public void addClaimVendor(AddClaimVendorDTO addClaimVendorDTO) throws ParseException {
        log.info("ClaimVendorServiceImpl.addClaimVendor");
        ClaimVendor claimVendor = new ClaimVendor();
        BeanUtils.copyProperties(addClaimVendorDTO, claimVendor);
        claimVendor.setClaimVendorNumber(getClaimVendorNumber(claimVendor));
        claimVendor.setStatusId(addClaimVendorDTO.getIsStatus() ? CommonConstant.ACTIVE : CommonConstant.INACTIVE);
        setClaimVendorDates(addClaimVendorDTO.getW9ReceivedDate(),addClaimVendorDTO.getPsVendorFormDate(),addClaimVendorDTO.getClaimsProfileFormDate(),addClaimVendorDTO.getProfileReviewDate(),addClaimVendorDTO.getBackgroundCheckDate(), claimVendor);
        claimVendor.setVendorAddresses(getVendorAddress(claimVendor, addClaimVendorDTO.getAddClaimVendorAddressDTOS()));
        claimVendor.setVendorCertificates(getVendorCertificates(claimVendor, addClaimVendorDTO.getAddClaimVendorCertificateDTOS()));
        claimVendor.setVendorServiceTypes(getVendorServiceType(claimVendor, addClaimVendorDTO.getInspectionServiceTypeCodes()));
        claimVendor.setVendorServices(getVendorService(claimVendor, addClaimVendorDTO.getInspectionServiceCodes()));
        claimVendorRepository.save(claimVendor);
    }

    private void setClaimVendorDates(String w9ReceivedDate, String psVendorFormDate, String claimsProfileFormDate, String profileReviewDate, String backgroundCheckDate, ClaimVendor claimVendor) throws ParseException {
        claimVendor.setW9ReceivedDate(w9ReceivedDate.isEmpty() ? null : dateUtil.getDate(w9ReceivedDate));
        claimVendor.setPsVendorFormDate(psVendorFormDate.isEmpty() ? null : dateUtil.getDate(psVendorFormDate));
        claimVendor.setClaimsProfileFormDate(claimsProfileFormDate.isEmpty() ? null : dateUtil.getDate(claimsProfileFormDate));
        claimVendor.setProfileReviewDate(profileReviewDate.isEmpty() ? null : dateUtil.getDate(profileReviewDate));
        claimVendor.setBackgroundCheckDate(backgroundCheckDate.isEmpty() ? null : dateUtil.getDate(backgroundCheckDate));
    }

    @Override
    public ClaimVendorDetailResponseDTO getClaimVendorDetail(String claimVendorNumber) {
        log.info("ClaimVendorServiceImpl.getClaimVendorDetail");
        ClaimVendorDetailResponseDTO claimVendorDetailResponseDTO = new ClaimVendorDetailResponseDTO();
        ClaimVendor claimVendor = claimVendorRepository.findByClaimVendorNumber(claimVendorNumber);
        if(claimVendor != null){
            BeanUtils.copyProperties(claimVendor,claimVendorDetailResponseDTO);
            claimVendorDetailResponseDTO.setIsStatus(claimVendor.getStatusId() == 1 ? true : false);
            claimVendorDetailResponseDTO.setClaimVendorAddressResponseDTOS(getClaimVendorAddresses(claimVendor.getVendorAddresses()));
            claimVendorDetailResponseDTO.setClaimVendorCertificateResponseDTOS(getClaimVendorCertificates(claimVendor.getVendorCertificates()));
            claimVendorDetailResponseDTO.setClaimVendorServiceTypeResponseDTOS(getClaimVendorServiceType(claimVendor.getVendorServiceTypes()));
            claimVendorDetailResponseDTO.setClaimVendorServiceResponseDTOS(getClaimVendorService(claimVendor.getVendorServices()));
        }
        return claimVendorDetailResponseDTO;
    }

    @Override
    public void updateClaimVendor(UpdateClaimVendorRequestDTO claimVendorRequestDTO) throws ParseException {
        log.info("ClaimVendorServiceImpl.updateClaimVendor");
        ClaimVendor claimVendor = claimVendorRepository.findById(claimVendorRequestDTO.getClaimVendorId()).get();
        BeanUtils.copyProperties(claimVendorRequestDTO, claimVendor);
        claimVendor.setStatusId(claimVendorRequestDTO.getIsStatus() ? CommonConstant.ACTIVE : CommonConstant.INACTIVE);
        setClaimVendorDates(claimVendorRequestDTO.getW9ReceivedDate(),claimVendorRequestDTO.getPsVendorFormDate(),claimVendorRequestDTO.getClaimsProfileFormDate(),
        claimVendorRequestDTO.getProfileReviewDate(),claimVendorRequestDTO.getBackgroundCheckDate(),claimVendor);
        claimVendor.setVendorCertificates(updateClaimVendorCertificate(claimVendorRequestDTO.getVendorCertificateRequestDTOS(),claimVendor));
        claimVendor.setVendorServices(updateClaimVendorServices(claimVendorRequestDTO.getVendorServiceRequestDTOS(),claimVendor));
        claimVendor.setVendorServiceTypes(updateClaimVendorServiceTypes(claimVendorRequestDTO.getVendorServiceTypeRequestDTOS(),claimVendor));
        claimVendor.setCreatedDateTime(claimVendorRepository.findById(claimVendor.getClaimVendorId()).get().getCreatedDateTime());
        claimVendorRepository.save(claimVendor);
    }

    private List<VendorServiceType> updateClaimVendorServiceTypes(List<UpdateClaimVendorServiceTypeRequestDTO> vendorServiceTypeRequestDTOS, ClaimVendor claimVendor) {
        List<VendorServiceType> existingVendorServiceTypes = vendorServiceTypeRepository.findByClaimVendor_ClaimVendorIdAndStatusId(claimVendor.getClaimVendorId(),CommonConstant.ACTIVE);
        Map<Integer,VendorServiceType> existingVendorServiceTypessMap = existingVendorServiceTypes.stream()
                .collect(Collectors.toMap(VendorServiceType::getVendorServiceTypeId, Function.identity()));
        List<VendorServiceType> updateVendorServiceTypeList = vendorServiceTypeRequestDTOS.stream()
                .map(serviceType -> {
                    if(existingVendorServiceTypessMap.containsKey(serviceType.getVendorServiceTypeId())){
                        VendorServiceType existingServiceType = existingVendorServiceTypessMap.get(serviceType.getVendorServiceTypeId());
                        BeanUtils.copyProperties(serviceType,existingServiceType);
                        existingServiceType.setInspectionServiceType(inspectionServiceTypeRepository.findByInspectionServiceTypeCode(serviceType.getInspectionServiceTypeCodes()));
                        existingServiceType.setModifiedDateTime(LocalDateTime.now());
                        existingVendorServiceTypessMap.remove(serviceType.getVendorServiceTypeId());
                        return existingServiceType;
                    } else {
                        VendorServiceType vendorServiceType = new VendorServiceType();
                        BeanUtils.copyProperties(serviceType,vendorServiceType);
                        vendorServiceType.setClaimVendor(claimVendor);
                        vendorServiceType.setInspectionServiceType(inspectionServiceTypeRepository.findByInspectionServiceTypeCode(serviceType.getInspectionServiceTypeCodes()));
                        vendorServiceType.setStatusId(CommonConstant.ACTIVE);
                        vendorServiceType.setCreatedByUserId(claimVendor.getCreatedByUserId());
                        vendorServiceType.setModifiedByUserId(claimVendor.getModifiedByUserId());
                        return vendorServiceType;
                    }
                }).collect(Collectors.toList());
        for(VendorServiceType vendorServiceType : existingVendorServiceTypessMap.values()){
            vendorServiceType.setStatusId(CommonConstant.DELETED_STATUS_ID);
            vendorServiceType.setModifiedDateTime(LocalDateTime.now());
            vendorServiceType.setCreatedByUserId(claimVendor.getCreatedByUserId());
            vendorServiceType.setModifiedByUserId(claimVendor.getModifiedByUserId());
            updateVendorServiceTypeList.add(vendorServiceType);
        }
        return updateVendorServiceTypeList;
    }

    private List<VendorService> updateClaimVendorServices(List<UpdateClaimVendorServiceRequestDTO> vendorServiceRequestDTOS, ClaimVendor claimVendor) {
        List<VendorService> existingVendorServices = vendorServiceRepository.findByClaimVendor_ClaimVendorIdAndStatusId(claimVendor.getClaimVendorId(),CommonConstant.ACTIVE);
        Map<Integer,VendorService> existingVendorServicessMap = existingVendorServices.stream()
                .collect(Collectors.toMap(VendorService::getVendorServiceId, Function.identity()));
        List<VendorService> updateVendorServiceList = vendorServiceRequestDTOS.stream()
                .map(service -> {
                    if(existingVendorServicessMap.containsKey(service.getVendorServiceId())){
                        VendorService existingService = existingVendorServicessMap.get(service.getVendorServiceId());
                        BeanUtils.copyProperties(service,existingService);
                        existingService.setInspectionService(inspectionServiceRepository.findByInspectionServiceCode(service.getInspectionServiceCode()));
                        existingService.setModifiedDateTime(LocalDateTime.now());
                        existingVendorServicessMap.remove(service.getVendorServiceId());
                        return existingService;
                    } else {
                        VendorService vendorService = new VendorService();
                        BeanUtils.copyProperties(service,vendorService);
                        vendorService.setClaimVendor(claimVendor);
                        vendorService.setInspectionService(inspectionServiceRepository.findByInspectionServiceCode(service.getInspectionServiceCode()));
                        vendorService.setStatusId(CommonConstant.ACTIVE);
                        vendorService.setCreatedByUserId(claimVendor.getCreatedByUserId());
                        vendorService.setModifiedByUserId(claimVendor.getModifiedByUserId());
                        return vendorService;
                    }
                }).collect(Collectors.toList());
        for(VendorService vendorService : existingVendorServicessMap.values()){
            vendorService.setStatusId(CommonConstant.DELETED_STATUS_ID);
            vendorService.setModifiedDateTime(LocalDateTime.now());
            vendorService.setCreatedByUserId(claimVendor.getCreatedByUserId());
            vendorService.setModifiedByUserId(claimVendor.getModifiedByUserId());
            updateVendorServiceList.add(vendorService);
        }
        return updateVendorServiceList;
    }

    private List<VendorCertificate> updateClaimVendorCertificate(List<UpdateClaimVendorCertificateRequestDTO> vendorCertificateRequestDTOS, ClaimVendor claimVendor) {
        List<VendorCertificate> existingVendorCertificates = vendorCertificateRepository.findByClaimVendor_ClaimVendorIdAndStatusId(claimVendor.getClaimVendorId(),CommonConstant.ACTIVE);
        Map<Integer,VendorCertificate> existingVendorCertificatesMap = existingVendorCertificates.stream()
                .collect(Collectors.toMap(VendorCertificate::getVendorCertificateId, Function.identity()));
        List<VendorCertificate> updateVendorCertificateList =  vendorCertificateRequestDTOS.stream()
                        .map(certificate -> {
                            if(existingVendorCertificatesMap.containsKey(certificate.getVendorCertificateId())){
                                try {
                                    VendorCertificate existingVendorCertificate = existingVendorCertificatesMap.get(certificate.getVendorCertificateId());
                                    BeanUtils.copyProperties(certificate,existingVendorCertificate);
                                    existingVendorCertificate.setCertifyingAgency(certifyingAgencyRepository.findByCertifyingAgencyCode(certificate.getCertifyingAgencyCode()));
                                    existingVendorCertificate.setInspectionServiceType(getInspectionServiceType(certificate.getServiceTypeCode()));
                                    existingVendorCertificate.setExpiryDate(certificate.getExpiryDate().isEmpty() ? null : dateUtil.getDate(certificate.getExpiryDate()));
                                    existingVendorCertificate.setModifiedDateTime(LocalDateTime.now());
                                    existingVendorCertificatesMap.remove(certificate.getVendorCertificateId());
                                    return existingVendorCertificate;
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                try{
                                    VendorCertificate vendorCertificate = new VendorCertificate();
                                    BeanUtils.copyProperties(certificate,vendorCertificate);
                                    vendorCertificate.setClaimVendor(claimVendor);
                                    vendorCertificate.setCertifyingAgency(certifyingAgencyRepository.findByCertifyingAgencyCode(certificate.getCertifyingAgencyCode()));
                                    vendorCertificate.setInspectionServiceType(getInspectionServiceType(certificate.getServiceTypeCode()));
                                    vendorCertificate.setExpiryDate(certificate.getExpiryDate().isEmpty() ? null : dateUtil.getDate(certificate.getExpiryDate()));
                                    vendorCertificate.setStatusId(CommonConstant.ACTIVE);
                                    vendorCertificate.setCreatedByUserId(claimVendor.getCreatedByUserId());
                                    vendorCertificate.setModifiedByUserId(claimVendor.getModifiedByUserId());
                                    return vendorCertificate;
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            }
                        }).collect(Collectors.toList());
        for(VendorCertificate vendorCertificate : existingVendorCertificatesMap.values()){
            vendorCertificate.setStatusId(CommonConstant.DELETED_STATUS_ID);
            vendorCertificate.setModifiedDateTime(LocalDateTime.now());
            vendorCertificate.setCreatedByUserId(claimVendor.getCreatedByUserId());
            vendorCertificate.setModifiedByUserId(claimVendor.getModifiedByUserId());
            updateVendorCertificateList.add(vendorCertificate);
        }
        return updateVendorCertificateList;
    }

    private List<ClaimVendorCertificateResponseDTO> getClaimVendorCertificates(List<VendorCertificate> vendorCertificates) {
        List<ClaimVendorCertificateResponseDTO> claimVendorCertificateResponseDTOS = null;
        if(vendorCertificates != null){
            claimVendorCertificateResponseDTOS = vendorCertificates.stream()
                    .filter(status -> status.getStatusId() ==1)
                    .map(certificate -> {
                ClaimVendorCertificateResponseDTO claimVendorCertificateResponseDTO = new ClaimVendorCertificateResponseDTO();
                BeanUtils.copyProperties(certificate,claimVendorCertificateResponseDTO);
                claimVendorCertificateResponseDTO.setCertifyingAgencyCode(certificate.getCertifyingAgency().getCertifyingAgencyCode());
                claimVendorCertificateResponseDTO.setInspectionServiceTypeCode(certificate.getInspectionServiceType().getInspectionServiceTypeCode());
                return claimVendorCertificateResponseDTO;
            }).collect(Collectors.toList());
        }
        return claimVendorCertificateResponseDTOS;
    }

    private List<ClaimVendorServiceResponseDTO> getClaimVendorService(List<VendorService> vendorServices) {
        List<ClaimVendorServiceResponseDTO> claimVendorServiceResponseDTOS = null;
        if(vendorServices != null) {
            claimVendorServiceResponseDTOS = vendorServices.stream()
                    .filter(status -> status.getStatusId() == 1)
                    .map(service -> {
                        ClaimVendorServiceResponseDTO claimVendorServiceResponseDTO = new ClaimVendorServiceResponseDTO();
                        claimVendorServiceResponseDTO.setInspectionServiceCode(service.getInspectionService().getInspectionServiceCode());
                        claimVendorServiceResponseDTO.setInspectionServiceName(service.getInspectionService().getInspectionServiceName());
                        return claimVendorServiceResponseDTO;
                    }).collect(Collectors.toList());
        }
        return claimVendorServiceResponseDTOS;
    }

    private List<ClaimVendorServiceTypeResponseDTO> getClaimVendorServiceType(List<VendorServiceType> vendorServiceTypes) {
        List<ClaimVendorServiceTypeResponseDTO> claimVendorServiceTypeResponseDTOS = null;
        if(vendorServiceTypes != null) {
            claimVendorServiceTypeResponseDTOS = vendorServiceTypes.stream()
                    .filter(status -> status.getStatusId() == 1 )
                    .map(vendorServiceType -> {
                        ClaimVendorServiceTypeResponseDTO claimVendorServiceTypeResponseDTO = new ClaimVendorServiceTypeResponseDTO();
                        claimVendorServiceTypeResponseDTO.setInspectionServiceTypeCode(vendorServiceType.getInspectionServiceType().getInspectionServiceTypeCode());
                        claimVendorServiceTypeResponseDTO.setInspectionServiceTypeName(vendorServiceType.getInspectionServiceType().getInspectionServiceTypeName());
                        return claimVendorServiceTypeResponseDTO;
                    }).collect(Collectors.toList());
        }
        return claimVendorServiceTypeResponseDTOS;
    }

    private List<ClaimVendorAddressResponseDTO> getClaimVendorAddresses(List<VendorAddress> vendorAddresses) {
        List<ClaimVendorAddressResponseDTO> claimVendorAddressResponseDTOS = null;
        if(vendorAddresses != null){
            claimVendorAddressResponseDTOS = vendorAddresses.stream()
                    .map(address -> {
                        ClaimVendorAddressResponseDTO claimVendorAddressResponseDTO = new ClaimVendorAddressResponseDTO();
                        BeanUtils.copyProperties(address,claimVendorAddressResponseDTO);
                        claimVendorAddressResponseDTO.setAddressTypeCode(addressTypeRepository.findAddressTypeCodeByAddressTypeId(address.getAddressTypeId()));
                        claimVendorAddressResponseDTO.setCountryName(countryRepository.findIsoCountryNameByIsoCountryId(address.getCountryId()));
                        claimVendorAddressResponseDTO.setStateName(stateRepository.findIsoStateNameByIsoStateIdAndIsoCountryId(address.getStateId(),address.getCountryId()));
                        return claimVendorAddressResponseDTO;
                    }).collect(Collectors.toList());
        }
        return claimVendorAddressResponseDTOS;
    }

    private String getClaimVendorNumber(ClaimVendor claimVendor) {
        return claimVendor.getClaimVendorId() == 0 ? String.format("%07d", claimVendorRepository.getNextClaimVendorNumber()) : claimVendorRepository.findClaimVendorNumberByClaimVendorId(claimVendor.getClaimVendorId());
    }

    private List<VendorService> getVendorService(ClaimVendor claimVendor, List<String> inspectionServiceCodes) {
        return inspectionServiceCodes.stream()
                .map(serviceCode -> {
                    VendorService vendorService = new VendorService();
                    vendorService.setClaimVendor(claimVendor);
                    vendorService.setInspectionService(inspectionServiceRepository.findByInspectionServiceCode(serviceCode));
                    vendorService.setStatusId(CommonConstant.ACTIVE);
                    vendorService.setCreatedByUserId(claimVendor.getCreatedByUserId());
                    vendorService.setModifiedByUserId(claimVendor.getModifiedByUserId());
                    return vendorService;
                }).collect(Collectors.toList());
    }

    private List<VendorServiceType> getVendorServiceType(ClaimVendor claimVendor, List<String> inspectionServiceTypeCodes) {
        return inspectionServiceTypeCodes.stream()
                .map(serviceTypeCode -> {
                    VendorServiceType vendorServiceType = new VendorServiceType();
                    vendorServiceType.setClaimVendor(claimVendor);
                    vendorServiceType.setInspectionServiceType(getInspectionServiceType(serviceTypeCode));
                    vendorServiceType.setStatusId(CommonConstant.ACTIVE);
                    vendorServiceType.setCreatedByUserId(claimVendor.getCreatedByUserId());
                    vendorServiceType.setModifiedByUserId(claimVendor.getModifiedByUserId());
                    return vendorServiceType;
                }).collect(Collectors.toList());
    }

    private List<VendorCertificate> getVendorCertificates(ClaimVendor claimVendor, List<AddClaimVendorCertificateDTO> addClaimVendorCertificateDTOS) {
        return addClaimVendorCertificateDTOS.stream()
                .map(certificate -> {
                    try {
                        VendorCertificate vendorCertificate = new VendorCertificate();
                        BeanUtils.copyProperties(certificate, vendorCertificate);
                        vendorCertificate.setClaimVendor(claimVendor);
                        vendorCertificate.setCertifyingAgency(certifyingAgencyRepository.findByCertifyingAgencyCode(certificate.getCertifyingAgencyCode()));
                        vendorCertificate.setInspectionServiceType(getInspectionServiceType(certificate.getServiceTypeCode()));
                        vendorCertificate.setExpiryDate(certificate.getExpiryDate().isEmpty() ? null : dateUtil.getDate(certificate.getExpiryDate()));
                        vendorCertificate.setStatusId(CommonConstant.ACTIVE);
                        vendorCertificate.setCreatedByUserId(claimVendor.getCreatedByUserId());
                        vendorCertificate.setModifiedByUserId(claimVendor.getModifiedByUserId());
                        return vendorCertificate;
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }

    private List<VendorAddress> getVendorAddress(ClaimVendor claimVendor, List<AddClaimVendorAddressDTO> addClaimVendorAddressDTOS) {
        return addClaimVendorAddressDTOS.stream()
                .map(address -> {
                    VendorAddress vendorAddress = new VendorAddress();
                    BeanUtils.copyProperties(address, vendorAddress);
                    vendorAddress.setClaimVendor(claimVendor);
                    vendorAddress.setAddressTypeId(addressTypeRepository.findAddressTypeIdByAddressTypeCode(address.getAddressTypeCode()));
                    vendorAddress.setCountryId(countryRepository.findCountryIdByIsoCountryCode(address.getCountryCode()));
                    vendorAddress.setStateId(stateRepository.findStateIdByIsoStateCodeAndCountryId(address.getStateCode(), vendorAddress.getCountryId()));
                    vendorAddress.setStatusId(CommonConstant.ACTIVE);
                    vendorAddress.setCreatedByUserId(claimVendor.getCreatedByUserId());
                    vendorAddress.setModifiedByUserId(claimVendor.getModifiedByUserId());
                    if(vendorAddress.getVendorAddressId() != 0)
                        vendorAddress.setCreatedDateTime(vendorAddressRepository.findById(vendorAddress.getVendorAddressId()).get().getCreatedDateTime());
                    return vendorAddress;
                }).collect(Collectors.toList());
    }

    private InspectionServiceType getInspectionServiceType(String serviceTypeCode) {
        return inspectionServiceTypeRepository.findByInspectionServiceTypeCode(serviceTypeCode);
    }
}