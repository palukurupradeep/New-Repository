package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FINServicesGetCustomerDetailsResult {
    
    @JsonProperty("FinancialServices.CustomerNumber")
    public String CustomerNumber;
    @JsonProperty("FinancialServices.CustomerName")
    public String CustomerName;
    @JsonProperty("FinancialServices.DBAName")
    public String DBAName;
    @JsonProperty("FinancialServices.PrimaryCustNumber")
    public String PrimaryCustNumber;
    @JsonProperty("FinancialServices.PrimaryCustName")
    public String PrimaryCustName;
    @JsonProperty("FinancialServices.BillsToPrimaryInd")
    public String BillsToPrimaryInd;
    @JsonProperty("FinancialServices.CustomerStatus")
    public String CustomerStatus;
    @JsonProperty("FinancialServices.InactiveDate")
    public String InactiveDate;
    @JsonProperty("FinancialServices.InactiveReason")
    public String InactiveReason;
    @JsonProperty("FinancialServices.InactiveReasonDesc")
    public String InactiveReasonDesc;
    @JsonProperty("FinancialServices.YearBusinessStarted")
    public String YearBusinessStarted;
    @JsonProperty("FinancialServices.EstablishDate")
    public String EstablishDate;
    @JsonProperty("FinancialServices.ReinstateDate")
    public String ReinstateDate;
    @JsonProperty("FinancialServices.ReinstateReason")
    public String ReinstateReason;
    @JsonProperty("FinancialServices.CustomerType")
    public String CustomerType;
    @JsonProperty("FinancialServices.BusinessType")
    public String BusinessType;
    @JsonProperty("FinancialServices.BusinessTypeDesc")
    public String BusinessTypeDesc;
    @JsonProperty("FinancialServices.StoreNumber")
    public String StoreNumber;
    @JsonProperty("FinancialServices.CustomerEmail")
    public String CustomerEmail;
    @JsonProperty("FinancialServices.PhoneCountryCode")
    public String PhoneCountryCode;
    @JsonProperty("FinancialServices.PhoneAreaCode")
    public String PhoneAreaCode;
    @JsonProperty("FinancialServices.PhoneNumber")
    public String PhoneNumber;
    @JsonProperty("FinancialServices.PhoneExtension")
    public String PhoneExtension;
    @JsonProperty("FinancialServices.FaxCountryCode")
    public String FaxCountryCode;
    @JsonProperty("FinancialServices.FaxAreaCode")
    public String FaxAreaCode;
    @JsonProperty("FinancialServices.FaxNumber")
    public String FaxNumber;
    @JsonProperty("FinancialServices.FaxExtension")
    public String FaxExtension;
    @JsonProperty("FinancialServices.WebPage")
    public String WebPage;
}
