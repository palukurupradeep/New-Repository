package com.shaw.claims.source.response;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FINServicesGetCustomerSalesCustomerServiceDataResponse {
	@JsonProperty("FinancialServices.getCustomerSalesCustomerServiceDataResult")
    public ArrayList<FINServicesGetCustomerSalesCustomerServiceDataResult> getCustomerServiceDataResult;

}
