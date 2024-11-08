package com.shaw.claims.source.response;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FINServicesSearchClaimInvoiceResponse {
	@JsonProperty("FinancialServices.searchClaimInvoiceResult")
    public ArrayList<FINServicesSearchClaimInvoiceResult> searchClaimInvoiceResult;
}
