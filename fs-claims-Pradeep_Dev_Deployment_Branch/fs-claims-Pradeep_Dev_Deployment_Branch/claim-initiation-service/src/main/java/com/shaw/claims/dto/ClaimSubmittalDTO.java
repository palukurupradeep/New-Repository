package com.shaw.claims.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimSubmittalDTO extends BaseEntityDTO{
    private String claimNumber;
    private String claimCategoryCode;
    private String customerNumber;
    private String primaryCustNumber;
    private String storeNumber;
    private String submitterId;
    private String submitterName;
    private String submitterEmail;
    private String submitterCode;
    private String claimReasonCode;
    private BigDecimal claimAmount;
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
    private String claimReason;
    private String claimResolution;
    private String claimRsnDescription;
    private Boolean sampleAvailable;
    private Boolean flooringInstalled;
    private Boolean labelMatch;
    private Boolean attachments;
    private Boolean visibleDamage;
    private Boolean holesDamage;
    private Boolean tearsDamage;
    private Boolean poleDamage;
    private Boolean wrinkleDamage;
    private Boolean creaseDamage;
    private Boolean dirtyDamage;
    private Boolean telescopedDamage;
    private Boolean wetDamage;
    private Boolean trailerDamage;
    private Boolean nailsDamage;
    private Boolean ribsDamage;
    private Boolean wallDamage;
    private Boolean otherDamage;
    private String requestedCarrier;
    private String shippingCarrier;
    private String nationalAcctType;
    private String nationalAcctName;
    private List<ClaimSubmittalDocumentDTO> claimDocumentDTOS;
    private List<ClaimSubmittalAddressDTO> claimAddressDTOS;
    private ClaimSubmittalEndUserInformationDTO endUserInformationDTO;
    private ClaimSubmittalClaimNoteDTO claimNoteDTO;
    private ClaimBuilderInstalledFlooringDataDTO installedFlooringDataDTO;
    private ClaimSubmittalPriorClaimCommentDTO priorClaimCommentDTO;
}
