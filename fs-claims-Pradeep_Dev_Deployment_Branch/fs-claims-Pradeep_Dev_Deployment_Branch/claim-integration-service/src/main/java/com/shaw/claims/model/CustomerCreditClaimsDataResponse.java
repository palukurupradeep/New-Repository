package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerCreditClaimsDataResponse {

    private String customerNumber;
    private String customerName;
    private String subCreditArea;
    private String subCreditAreaCreditAreaNbr;
    private String subCreditAreaDesc;
    private String claimsArea;
    private String claimsAreaDesc;
    private String businessSegment;
    private String businessSegmentDesc;
    private String legalHold;
    private String legalHoldInitialActionDate;
    private String legalHoldReason;
    private String legalHoldReasonDesc;
    private String defaultToAutoApplyCreditMemo;
    private String hasSpecialClaimsHandling;
    private String specialClaimsHandlingComments;

}
