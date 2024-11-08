package com.shaw.claims.services;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.dto.CRMRequestLineDetailsDTO;
import com.shaw.claims.dto.CreditMemoRequestDTO;
import com.shaw.claims.dto.RestockFeeRequestDTO;
import com.shaw.claims.dto.RestockFeeResponseDTO;
import com.shaw.claims.model.ClaimLineDetail;
import com.shaw.claims.model.ClaimReasonDefinition;
import com.shaw.claims.model.SellingCompany;
import com.shaw.claims.repo.ClaimLineDetailsRepository;
import com.shaw.claims.repo.ClaimReasonDefinitionRepository;
import com.shaw.claims.repo.ClaimRestockFeeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditMemoServiceImpl implements CreditMemoService{

    Logger log = LogManager.getLogger(CreditMemoServiceImpl.class);

    @Autowired
    private ClaimDetailsServiceImpl claimDetailsService;
    @Autowired
    private ClaimRestockFeeRepository claimRestockFeeRepository;
    @Autowired
    private ClaimLineDetailsRepository claimLineDetailsRepository;
    @Autowired
	ClaimReasonDefinitionRepository claimReasonDefinitionRepository;

    @Override
    public void prepareCreditMemo(CreditMemoRequestDTO creditMemoRequestDTO) {
        BigDecimal creditMemoAmount = BigDecimal.ZERO, rate = BigDecimal.ZERO;
        List<ClaimLineDetail> claimLineDetailList = null;
        log.info("CreditMemoServiceImpl.prepareCreditMemo");
        SellingCompany sellingCompany = claimDetailsService.getSellingCompany(creditMemoRequestDTO.getSellingCompanyCode());
        if (sellingCompany.getCompanyType() == 'R' || sellingCompany.getCompanyType() == 'C') {
            if (sellingCompany.getCarpet()) {
                rate = getRestockFeeRate(getRestockFeeConstant(sellingCompany.getCompanyType(), CommonConstant.CARPET));
                claimLineDetailList = getClaimLineDetailListForCarpet(creditMemoRequestDTO.getClaimLineId());
            } else if (sellingCompany.getHardSurface()) {
                rate = getRestockFeeRate(getRestockFeeConstant(sellingCompany.getCompanyType(), CommonConstant.HARD_SURFACE));
                claimLineDetailList = getClaimLineDetailListForHardSurface(creditMemoRequestDTO.getClaimLineId());
            }
        }
        creditMemoAmount = calculateCreditMemoAmount(rate, claimLineDetailList);
    }

    @Override
    public RestockFeeResponseDTO restockFee(RestockFeeRequestDTO restockFeeRequestDTO) {
        BigDecimal restockFeePercentage = BigDecimal.ZERO;
        List<ClaimLineDetail> claimLineDetailList = null;
        log.info("CreditMemoServiceImpl.restockFee");
        SellingCompany sellingCompany = claimDetailsService.getSellingCompany(restockFeeRequestDTO.getSellingCompanyCode());
        if (sellingCompany.getCompanyType() == 'R' || sellingCompany.getCompanyType() == 'C') {
            if (sellingCompany.getCarpet()) {
                restockFeePercentage = getRestockFeeRate(getRestockFeeConstant(sellingCompany.getCompanyType(), CommonConstant.CARPET));
                claimLineDetailList = getClaimLineDetailListForCarpetRestockFee(restockFeeRequestDTO.getRequestLineDetailsDTOS());
            } else if (sellingCompany.getHardSurface()) {
                restockFeePercentage = getRestockFeeRate(getRestockFeeConstant(sellingCompany.getCompanyType(), CommonConstant.HARD_SURFACE));
                claimLineDetailList = getClaimLineDetailListForHardSurfaceRestockFee(restockFeeRequestDTO.getRequestLineDetailsDTOS());
            }
        }
        BigDecimal restockFee = calculationRestockFee(restockFeePercentage,claimLineDetailList);
    	ClaimReasonDefinition reasonDefinition = getClaimReasonDefinition(restockFeeRequestDTO.getRequestLineDetailsDTOS().get(0).getReasonCode());
        return setRestockFeeResponse(restockFeePercentage,restockFee,sellingCompany.getSellingCompanyCode(), reasonDefinition);
    }

    private RestockFeeResponseDTO setRestockFeeResponse(BigDecimal restockFeePercentage, BigDecimal restockFee, String sellingCompanyCode,ClaimReasonDefinition reasonDefinition) {
        RestockFeeResponseDTO restockFeeResponseDTO = new RestockFeeResponseDTO();
        restockFeeResponseDTO.setRestockFee(restockFee);
        restockFeeResponseDTO.setRestockFeePercentage(restockFeePercentage);
        restockFeeResponseDTO.setSellingCompanyCode(sellingCompanyCode);
        if(null!=reasonDefinition)
        {
        	restockFeeResponseDTO.setCodeRuleReturnType(reasonDefinition.getCodeRuleReturnType().getCodeRuleReturnTypeDescription());
        }
        return restockFeeResponseDTO;
    }

    private BigDecimal calculationRestockFee(BigDecimal restockFeePercentage, List<ClaimLineDetail> claimLineDetailList) {
        BigDecimal invoiceAmount = getInvoiceAmount(claimLineDetailList);
        return invoiceAmount.divide(BigDecimal.valueOf(100)).multiply(restockFeePercentage);

    }

    private String getRestockFeeConstant(char companyType, String companyTypeDesc) {
        if (companyType == 'R') {
            if (companyTypeDesc.equals(CommonConstant.CARPET)) {
                return CommonConstant.RESIDENTIAL_CARPET;
            } else if (companyTypeDesc.equals(CommonConstant.HARD_SURFACE)) {
                return CommonConstant.RESIDENTIAL_HARD_SURFACE;
            }
        } else if (companyType == 'C') {
            if (companyTypeDesc.equals(CommonConstant.CARPET)) {
                return CommonConstant.COMMERCIAL_CARPET;
            } else if (companyTypeDesc.equals(CommonConstant.HARD_SURFACE)) {
                return CommonConstant.COMMERCIAL_HARD_SURFACE;
            }
        }
        throw new IllegalArgumentException("Invalid company type");
    }

    private BigDecimal calculateCreditMemoAmount(BigDecimal rate, List<ClaimLineDetail> claimLineDetailList) {
        BigDecimal invoiceAmount = getInvoiceAmount(claimLineDetailList);
        BigDecimal invoiceAmountTax = invoiceAmount.multiply(BigDecimal.valueOf(0.0825));
        BigDecimal restockFee = invoiceAmount.divide(BigDecimal.valueOf(100)).multiply(rate);
        BigDecimal balanceFromInvoice = invoiceAmount.subtract(restockFee);
        return balanceFromInvoice.add(invoiceAmountTax);
    }

    private BigDecimal getInvoiceAmount(List<ClaimLineDetail> claimLineDetailList) {
        return claimLineDetailList.stream()
                .map(ClaimLineDetail::getLineAmountUsd)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    private List<ClaimLineDetail> getClaimLineDetailListForHardSurface(List<Integer> claimLineId) {
        return claimLineId.stream().map(id -> claimLineDetailsRepository.findByLineIdIgnoreFreightAndMiceCharges(id))
                .filter(claimLineDetail -> claimLineDetail != null)
                .collect(Collectors.toList());
    }

    private List<ClaimLineDetail> getClaimLineDetailListForCarpet(List<Integer> claimLineId) {
        return claimLineId.stream().map(id -> claimLineDetailsRepository.findByLineIdIgnoreReturnFreight(id))
                .filter(claimLineDetail -> claimLineDetail != null)
                .collect(Collectors.toList());
    }

    private BigDecimal getRestockFeeRate(String claimRestockFeeDescription) {
        return claimRestockFeeRepository.findRateByClaimRestockFeeDescription(claimRestockFeeDescription);
    }
    
    private List<ClaimLineDetail> getClaimLineDetailListForHardSurfaceRestockFee(List<CRMRequestLineDetailsDTO> crmRequestLineDetailsDto) {
        return crmRequestLineDetailsDto.stream().map(id -> claimLineDetailsRepository.findByLineIdIgnoreFreightAndMiceCharges(id.getClaimLineId()))
                .filter(claimLineDetail -> claimLineDetail != null)
                .collect(Collectors.toList());
    }
    

    private List<ClaimLineDetail> getClaimLineDetailListForCarpetRestockFee(List<CRMRequestLineDetailsDTO> crmRequestLineDetailsDto) {
        return crmRequestLineDetailsDto.stream().map(id -> claimLineDetailsRepository.findByLineIdIgnoreReturnFreight(id.getClaimLineId()))
                .filter(claimLineDetail -> claimLineDetail != null)
                .collect(Collectors.toList());
    }
    
    private ClaimReasonDefinition getClaimReasonDefinition(String reasonCode) {
		return claimReasonDefinitionRepository.findByClaimReasonCode(reasonCode);

	}
}
