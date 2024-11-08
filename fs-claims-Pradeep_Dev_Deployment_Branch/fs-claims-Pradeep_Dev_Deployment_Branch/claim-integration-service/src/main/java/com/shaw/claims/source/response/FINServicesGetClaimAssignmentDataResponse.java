package com.shaw.claims.source.response;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FINServicesGetClaimAssignmentDataResponse {
	@JsonProperty("FinancialServices.getClaimAssignmentDataResult")
    public ArrayList<FINServicesGetClaimAssignmentDataResult> claimAssignmentDataResult;
}
