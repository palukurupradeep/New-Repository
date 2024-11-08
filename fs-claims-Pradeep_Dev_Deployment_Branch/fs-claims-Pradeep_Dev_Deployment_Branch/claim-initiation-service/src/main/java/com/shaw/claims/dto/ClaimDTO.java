package com.shaw.claims.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
@Data
@JsonInclude(Include.NON_NULL)
public class ClaimDTO extends BaseEntityDTO{
    private int claimId;
    private String claimCategoryCode;
    private String customerNumber;
    private String primaryCustNumber;
    private String storeNumber;
    private String claimReasonCode;
    private String submitterCode = "";
    private Boolean endUserClaim;
    private Boolean jobStopped;
    private Boolean priorityClaim;
    private BigDecimal exchangeRate;
    private String sellingCompany;
    private String territory;
    private String division;
    private String region;
    private String businessMarketIndicatorCode;
    private Boolean addToWatchList;
    private List<ClaimDocumentDTO> claimDocumentDTOS;
    private EndUserInformationDTO endUserInformationDTO;
    private ClaimNoteDTO claimNoteDTO;
    private List<ClaimAddressDTO> claimAddressDTO;
}
