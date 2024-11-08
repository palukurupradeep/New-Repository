package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FINServicesGetCustomerStoreNumberResult {

    @JsonProperty("FinancialServices.StoreNumber")
    public String StoreNumber;

    @JsonProperty("FinancialServices.CustomerNumber")
    public String CustomerNumber;

    @JsonProperty("FinancialServices.PrimaryCustNumber")
    public String PrimaryCustNumber;

    @JsonProperty("FinancialServices.CustomerStatus")
    public String CustomerStatus;

    @JsonProperty("FinancialServices.CustomerName")
    public String CustomerName;

    @JsonProperty("FinancialServices.AddressType")
    public String AddressType;

    @JsonProperty("FinancialServices.AddressLine1")
    public String AddressLine1;

    @JsonProperty("FinancialServices.AddressLine2")
    public String AddressLine2;

    @JsonProperty("FinancialServices.AddressLine3")
    public String AddressLine3;

    @JsonProperty("FinancialServices.City")
    public String City;

    @JsonProperty("FinancialServices.StateCode")
    public String StateCode;

    @JsonProperty("FinancialServices.ZipCode")
    public String ZipCode;

    @JsonProperty("FinancialServices.County")
    public String Country;

    @JsonProperty("FinancialServices.Country")
    public String CountryCode;

}
