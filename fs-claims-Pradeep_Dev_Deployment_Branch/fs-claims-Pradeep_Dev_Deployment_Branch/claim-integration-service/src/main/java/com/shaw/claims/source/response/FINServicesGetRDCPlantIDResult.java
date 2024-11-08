package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FINServicesGetRDCPlantIDResult {
    @JsonProperty("FinancialServices.RGAZipCode")
    public String RGAZipCode;
    @JsonProperty("FinancialServices.Plant")
    public String Plant;
    @JsonProperty("FinancialServices.AddressLine1")
    public String AddressLine1;
    @JsonProperty("FinancialServices.AddressLine2")
    public String AddressLine2;
    @JsonProperty("FinancialServices.City")
    public String City;
    @JsonProperty("FinancialServices.StateCode")
    public String StateCode;
    @JsonProperty("FinancialServices.RDCZipCode")
    public String RDCZipCode;
}
