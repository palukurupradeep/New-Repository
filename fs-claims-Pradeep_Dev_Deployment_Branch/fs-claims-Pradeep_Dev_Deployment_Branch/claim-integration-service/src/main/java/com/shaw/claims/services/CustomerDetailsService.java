package com.shaw.claims.services;

import java.util.List;
import java.util.Map;

import com.shaw.claims.model.*;
import com.shaw.claims.source.response.RDCPlantIDResponse;
import com.shaw.claims.source.response.SkuResponse;

public interface CustomerDetailsService {
	public List<CustomerDetails> getCustomerDetails(String customerNumber);

	public List<CustomerDetails> getCustomerDetailsByStoreNumber(String storeNumber);

	public CustomerAddress getCustomerAddress(String customerNumber, String addressType);

	public List<ClaimAssignmentData> getClaimAssignmentData(String customerNumber);

	public List<CustomerServiceData> getCustomerSalesCustomerServiceData(String customerNumber);

	public List<CrmReasons> getCrmReasons();

	public List<GetInvoiceHeaderResponse> getInvoiceHeader(String customerNumber, String invoiceNumber, String invoiceDate);

	public List<GetInvoiceChargeResponse> getInvoiceCharges(String invoiceNumber, String invoiceDate);

	List<CustomerCreditClaimsDataResponse> getCustomerCreditClaimsData(String custNbr);

	public List<SellingStyleInfo> getSellingStyleInfo(String sellStyleNumber);

	public List<TerritoryDivisionResponse> getTerritoryDivisionRegion(String territoryNbr);

	public List getProductDetailsByStyleNumber(String styleNumber);

	List<RDCPlantIDResponse> getRDCPlantID(String rgaZipCode);

	public List<TerritoryDivisionResponse> getTerritoryDivisionRegionCustSelco(String custNbr, String selco);

	public List<SkuResponse> returnsDecision(String plant, String sku, String keepQty);
}
