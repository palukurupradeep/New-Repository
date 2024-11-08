package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class FINServicesGetContactDetailsResponse {
    @JsonProperty("FinancialServices.getContactDetailsResult")
    public ArrayList<FINServicesGetContactDetailsResult> contactDetailsResult;
}
