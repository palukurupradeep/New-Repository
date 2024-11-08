
package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CustomerDetailsRoot {
    @JsonProperty("FinancialServices.getCustomerDetailsResponse")
    public FINServicesGetCustomerDetailsResponse customerDetailsResponse;

    @JsonProperty("FinancialServices.getCustomerStoreNumbersResponse")
    public FINServicesGetCustomerDetailsResponse customerDetailsByStoreNumberResponse;
    
    @JsonProperty("FinancialServices.getCustomerAddressResponse")
    public FINServicesGetCustomerDetailsResponse customerAddress;
    
    @JsonProperty("FinancialServices.getClaimAssignmentDataResponse")
    public FINServicesGetClaimAssignmentDataResponse claimAssignmentData;
    
    @JsonProperty("FinancialServices.getCustomerSalesCustomerServiceDataResponse")
    public FINServicesGetCustomerSalesCustomerServiceDataResponse customerServiceData;
    
    @JsonProperty("FinancialServices.getCrmReasonsResponse")
    public FINServicesGetCustomerDetailsResponse crmReasonsResponse;

    @JsonProperty("FinancialServices.getInvoiceHeaderResponse")
    public FINServicesGetCustomerDetailsResponse invoiceHeaderResponse;

    @JsonProperty("FinancialServices.getInvoiceChargesResponse")
    public FINServicesGetCustomerDetailsResponse invoiceChargesResponse;

    @JsonProperty("FinancialServices.getInvoiceDetailResponse")
    public FINServicesGetCustomerDetailsResponse invoiceDetailsResponse;

    @JsonProperty("FinancialServices.getCustomerCreditClaimsDataResponse")
    public FINServicesGetCustomerDetailsResponse customerCreditClaimsDataResponse;
    
    @JsonProperty("FinancialServices.getSellingStyleInfoResponse")
    public FINServicesGetSellingStyleInfoResponse sellingStyleInfoResponse;
    
    @JsonProperty("FinancialServices.getTerritoryDivisionRegionResponse")
    public FINServicesGetCustomerDetailsResponse territoryDivisionRegionResponse;

    @JsonProperty("FinancialServices.getRDCPlantIDResponse")
    public FINServicesGetCustomerDetailsResponse rdcPlantIDResponse;
    
    @JsonProperty("FinancialServices.getTerritoryDivisionRegionCustSelcoResponse")
    public FINServicesGetCustomerDetailsResponse territoryDivisionRegionCustSelcoResponse;

}
