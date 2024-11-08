package com.shaw.claims.source.response;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FINServicesGetSellingStyleInfoResponse {

	@JsonProperty("FinancialServices.getSellingStyleInfoResult")
    public ArrayList<FINServicesGetSellingStyleInfoResult> sellingStyleInfoResult;
}
