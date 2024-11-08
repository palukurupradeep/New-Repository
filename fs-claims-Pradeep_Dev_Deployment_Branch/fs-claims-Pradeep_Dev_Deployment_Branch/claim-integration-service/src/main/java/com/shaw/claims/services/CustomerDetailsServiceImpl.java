package com.shaw.claims.services;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shaw.claims.client.CustomerDetailsClient;
import com.shaw.claims.client.ProductDetailsClient;
import com.shaw.claims.model.ClaimAssignmentData;
import com.shaw.claims.model.CrmReasons;
import com.shaw.claims.model.CustomerAddress;
import com.shaw.claims.model.CustomerCreditClaimsDataResponse;
import com.shaw.claims.model.CustomerDetails;
import com.shaw.claims.model.CustomerServiceData;
import com.shaw.claims.model.GetInvoiceChargeResponse;
import com.shaw.claims.model.GetInvoiceHeaderResponse;
import com.shaw.claims.model.SellingStyleInfo;
import com.shaw.claims.model.TerritoryDivisionResponse;
import com.shaw.claims.source.response.RDCPlantIDResponse;
import com.shaw.claims.source.response.SkuResponse;

@Service
public class CustomerDetailsServiceImpl implements CustomerDetailsService{
    Logger log = LogManager.getLogger(ContactDetailsServiceImpl.class);
    @Autowired
    private CustomerDetailsClient customerDetailsClient;

	@Autowired
	private ProductDetailsClient productDetailsClient;
    public List<CustomerDetails> getCustomerDetails(String customerNumber){
        log.info("Inside CustomerDetailsServiceImpl");
        return customerDetailsClient.getCustomerDetails(customerNumber);
    }
    
	@Override
	public List<CustomerDetails> getCustomerDetailsByStoreNumber(String storeNumber) {
		log.info("Inside CustomerDetailsServiceImpl");
        return customerDetailsClient.getCustomerDetailsByStoreNumber(storeNumber);
	}

	@Override
	public CustomerAddress getCustomerAddress(String customerNumber, String addressType) {
		log.info("Inside CustomerDetailsServiceImpl");
        return customerDetailsClient.getCustomerAddress(customerNumber, addressType);
	}

	@Override
	public List<ClaimAssignmentData> getClaimAssignmentData(String customerNumber) {
		log.info("Inside CustomerDetailsServiceImpl");
        return customerDetailsClient.getClaimAssignmentData(customerNumber);
	}

	@Override
	public List<CustomerServiceData> getCustomerSalesCustomerServiceData(String customerNumber) {
		log.info("Inside CustomerDetailsServiceImpl");
        return customerDetailsClient.getCustomerSalesCustomerServiceData(customerNumber);
	}

	@Override
	public List<CrmReasons> getCrmReasons() {
		log.info("Inside CustomerDetailsServiceImpl");
        return customerDetailsClient.getCrmReasons();
	}

	@Override
	public List<GetInvoiceHeaderResponse> getInvoiceHeader(String customerNumber, String invoiceNumber,
			String invoiceDate) {
		log.info("Inside CustomerDetailsServiceImpl");
        return customerDetailsClient.getInvoiceHeader(customerNumber, invoiceNumber, invoiceDate);
	}

	@Override
	public List<GetInvoiceChargeResponse> getInvoiceCharges(String invoiceNumber,
			String invoiceDate) {
		log.info("Inside CustomerDetailsServiceImpl");
        return customerDetailsClient.getInvoiceCharges(invoiceNumber, invoiceDate);
	}

	@Override
	public List<CustomerCreditClaimsDataResponse> getCustomerCreditClaimsData(String custNbr) {
		log.info("Inside CustomerDetailsServiceImpl");
		return customerDetailsClient.getCustomerCreditClaimsData(custNbr);
	}

	@Override
	public List<SellingStyleInfo> getSellingStyleInfo(String sellStyleNumber) {
		log.info("Inside CustomerDetailsServiceImpl");
        return customerDetailsClient.getSellingStyleInfo(sellStyleNumber);
	}
	

	@Override
	public List<TerritoryDivisionResponse> getTerritoryDivisionRegion(String territoryNbr) {
		log.info("Inside CustomerDetailsServiceImpl");
        return customerDetailsClient.getTerritoryDivisionRegion(territoryNbr);
	}

	@Override
	public List getProductDetailsByStyleNumber(String styleNumber) {
		return productDetailsClient.getProductDetailsByStyleNumber(styleNumber);
	}

	@Override
	public List<RDCPlantIDResponse> getRDCPlantID(String rgaZipCode) {
		log.info("Inside CustomerDetailsServiceImpl");
		return customerDetailsClient.getRDCPlantID(rgaZipCode);
	}
	
	@Override
	public List<TerritoryDivisionResponse> getTerritoryDivisionRegionCustSelco(String custNbr, String selco){
		log.info("Inside CustomerDetailsServiceImpl");
		return customerDetailsClient.getTerritoryDivisionRegionCustSelco(custNbr, selco);
	}
	
	@Override
	public List<SkuResponse> returnsDecision(String plant, String sku, String keepQty) {
		log.info("Inside CustomerDetailsServiceImpl");
		return customerDetailsClient.returnsDecision(plant, sku, keepQty);
	}
}
