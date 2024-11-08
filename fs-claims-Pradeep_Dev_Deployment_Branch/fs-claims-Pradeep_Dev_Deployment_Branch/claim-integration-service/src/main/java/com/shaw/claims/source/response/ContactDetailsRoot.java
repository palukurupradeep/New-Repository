package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ContactDetailsRoot {
    @JsonProperty("FinancialServices.getContactDetailsResponse")
    public FINServicesGetContactDetailsResponse contactDetailsResponse;
}
