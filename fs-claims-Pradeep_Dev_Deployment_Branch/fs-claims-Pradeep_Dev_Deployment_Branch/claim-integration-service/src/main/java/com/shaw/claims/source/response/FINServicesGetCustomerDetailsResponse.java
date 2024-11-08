package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class FINServicesGetCustomerDetailsResponse {
    @JsonProperty("FinancialServices.getCustomerDetailsResult")
    public ArrayList<FINServicesGetCustomerDetailsResult> customerDetailsResult;

    @JsonProperty("FinancialServices.getCustomerStoreNumbersResult")
    public ArrayList<FINServicesGetCustomerStoreNumberResult> customerDetailsByStoreResult;
    
    @JsonProperty("FinancialServices.getCustomerAddressResult")
    public ArrayList<FINServicesGetCustomerAddressResult> customerAddressResult;
    
    @JsonProperty("FinancialServices.getCrmReasonsResult")
    public ArrayList<FINServicesGetCrmReasonsResult> crmReasonsResult;

    @JsonProperty("FinancialServices.getInvoiceHeaderResult")
    public ArrayList<FINServicesGetInvoiceHeaderResult> invoiceHeaderResult;

    @JsonProperty("FinancialServices.getInvoiceChargesCharge")
    public ArrayList<FINServicesGetInvoiceChargesResult> invoiceChargesResult;

    @JsonProperty("FinancialServices.getInvoiceDetailResult")
    public ArrayList<FINServicesGetInvoiceDetailResult> invoiceDetailsResult;

    @JsonProperty("FinancialServices.getCustomerCreditClaimsDataResult")
    public ArrayList<FINServicesGetCustomerCreditClaimsDataResponse> customerCreditClaimsDataResult;
    
    @JsonProperty("FinancialServices.getTerritoryDivisionRegionResult")
    public ArrayList<FINServicesGetTerritoryDivisionRegionResult> territoryDivisionRegionResult;

    @JsonProperty("FinancialServices.getRDCPlantIDResult")
    public ArrayList<FINServicesGetRDCPlantIDResult> rdcPlantIDResult;
    
    @JsonProperty("FinancialServices.getTerritoryDivisionRegionCustSelcoResult")
    public ArrayList<FINServicesGetTerritoryDivisionRegionResult> territoryDivisionRegionCustSelcoResult;


}
