package com.shaw.claims.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
@Component
@Data
public class ClaimBuilderDTO extends BaseEntityDTO{
    private int claimId;
    private String claimCategoryCode;
    private String customerNumber;
    private String primaryCustNumber;
    private String claimNumber;
    private String storeNumber;
    private String claimReasonCode;
    private BigDecimal claimAmount;
    private String submitterId;
    private String submitterName;
    private String submitterEmail;
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
    private List<ClaimBuilderDocumentDTO> claimDocumentDTOS;
    private ClaimBuilderEndUserInformationDTO endUserInformationDTO;
    private ClaimNoteDTO claimNoteDTO;
    private List<ClaimBuilderAddressDTO> claimAddressDTOS;
    private ClaimBuilderInstalledFlooringDataDTO installedFlooringDataDTO;
    private ClaimSubmittalPriorClaimCommentDTO priorClaimCommentDTO;
}
