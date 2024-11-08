
package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InvoiceDetailsRoot {
	
	@JsonProperty("FinancialServices.searchClaimInvoiceResponse")
	public FINServicesSearchClaimInvoiceResponse searchClaimInvoiceResponse;

}
