package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FINServicesGetCustomerCreditClaimsDataResponse {
    @JsonProperty("FinancialServices.CustomerNumber")
    public String CustomerNumber;
    @JsonProperty("FinancialServices.CustomerName")
    public String CustomerName;
    @JsonProperty("FinancialServices.SubCreditArea")
    public String SubCreditArea;
    @JsonProperty("FinancialServices.SubCreditAreaCreditAreaNbr")
    public String SubCreditAreaCreditAreaNbr;
    @JsonProperty("FinancialServices.SubCreditAreaDesc")
    public String SubCreditAreaDesc;
    @JsonProperty("FinancialServices.ClaimsArea")
    public String ClaimsArea;
    @JsonProperty("FinancialServices.ClaimsAreaDesc")
    public String ClaimsAreaDesc;
    @JsonProperty("FinancialServices.BusinessSegment")
    public String BusinessSegment;
    @JsonProperty("FinancialServices.BusinessSegmentDesc")
    public String BusinessSegmentDesc;
    @JsonProperty("FinancialServices.LegalHold")
    public String LegalHold;
    @JsonProperty("FinancialServices.LegalHoldInitialActionDate")
    public String LegalHoldInitialActionDate;
    @JsonProperty("FinancialServices.LegalHoldReason")
    public String LegalHoldReason;
    @JsonProperty("FinancialServices.LegalHoldReasonDesc")
    public String LegalHoldReasonDesc;
    @JsonProperty("FinancialServices.DefaultToAutoApplyCreditMemo")
    public String DefaultToAutoApplyCreditMemo;
    @JsonProperty("FinancialServices.HasSpecialClaimsHandling")
    public String HasSpecialClaimsHandling;
    @JsonProperty("FinancialServices.SpecialClaimsHandlingComments")
    public String SpecialClaimsHandlingComments;
}
