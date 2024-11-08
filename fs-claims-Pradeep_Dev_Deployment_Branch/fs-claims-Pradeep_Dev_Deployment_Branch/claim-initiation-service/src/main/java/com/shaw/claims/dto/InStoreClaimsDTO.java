package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InStoreClaimsDTO {
    private String claimNumber;
    private String customerNumber;
    private String claimStatus;
    private String claimCategory;
    private BigDecimal claimAmountUsd;
    private String reasonCode;
    private String sellingCompany;
    private String territoryManagerName;
    private LocalDateTime createdDateTime;
    private InStoreClaimEndUserInformationDTO endUserInformationDTO;
    private List<ClaimAddressDTO> claimAddressDTOS;
    private List<InStoreClaimDocumentDTO> claimDocumentDTOS;
    private List<InStoreClaimNoteDTO> claimNoteDTOS;
}
