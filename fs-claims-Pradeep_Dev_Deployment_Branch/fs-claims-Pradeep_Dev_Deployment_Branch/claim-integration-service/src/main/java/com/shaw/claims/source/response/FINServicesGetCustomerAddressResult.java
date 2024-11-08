package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FINServicesGetCustomerAddressResult {

    @JsonProperty("FinancialServices.CustomerNumber")
    public String CustomerNumber;

    @JsonProperty("FinancialServices.CustomerName")
    public String CustomerName;

    @JsonProperty("FinancialServices.CareOf_ContactName")
    public String CareOfContactName;

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

    @JsonProperty("FinancialServices.Latitude")
    public String Latitude;

    @JsonProperty("FinancialServices.Longitude")
    public String Longitude;

}
