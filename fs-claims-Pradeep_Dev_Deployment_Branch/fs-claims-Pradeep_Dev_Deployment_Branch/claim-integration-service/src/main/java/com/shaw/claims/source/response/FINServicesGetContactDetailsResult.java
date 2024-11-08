package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FINServicesGetContactDetailsResult {
    @JsonProperty("FinancialServices.CustomerNumber")
    public String CustomerNumber;
    @JsonProperty("FinancialServices.ContactDisposition")
    public String ContactDisposition;
    @JsonProperty("FinancialServices.ContactType")
    public String ContactType;
    @JsonProperty("FinancialServices.ContactTitleCode")
    public String ContactTitleCode;
    @JsonProperty("FinancialServices.ContactTitle")
    public String ContactTitle;
    @JsonProperty("FinancialServices.ContactCompanyName")
    public String ContactCompanyName;
    @JsonProperty("FinancialServices.ContactPrimaryInd")
    public String ContactPrimaryInd;
    @JsonProperty("FinancialServices.FirstName")
    public String FirstName;
    @JsonProperty("FinancialServices.MiddleName")
    public String MiddleName;
    @JsonProperty("FinancialServices.LastName")
    public String LastName;
    @JsonProperty("FinancialServices.Suffix")
    public String Suffix;
    @JsonProperty("FinancialServices.NickName")
    public String NickName;
    @JsonProperty("FinancialServices.ContactComments")
    public String ContactComments;
    @JsonProperty("FinancialServices.Category")
    public String Category;
    @JsonProperty("FinancialServices.PhonePrimaryInd")
    public String PhonePrimaryInd;
    @JsonProperty("FinancialServices.PhoneType")
    public String PhoneType;
    @JsonProperty("FinancialServices.CountryCode")
    public String CountryCode;
    @JsonProperty("FinancialServices.AreaCode")
    public String AreaCode;
    @JsonProperty("FinancialServices.PhoneNumber")
    public String PhoneNumber;
    @JsonProperty("FinancialServices.Extension")
    public String Extension;
    @JsonProperty("FinancialServices.EmailPrimaryInd")
    public String EmailPrimaryInd;
    @JsonProperty("FinancialServices.Email")
    public String Email;
    @JsonProperty("FinancialServices.AddressPrimaryInd")
    public String AddressPrimaryInd;
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
    @JsonProperty("FinancialServices.StateProvince")
    public String StateProvince;
    @JsonProperty("FinancialServices.ZipCode")
    public String ZipCode;
    @JsonProperty("FinancialServices.Country")
    public String Country;
    @JsonProperty("FinancialServices.County")
    public String County;
    @JsonProperty("FinancialServices.Latitude")
    public String Latitude;
    @JsonProperty("FinancialServices.Longitude")
    public String Longitude;
}