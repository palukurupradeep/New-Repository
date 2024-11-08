package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shaw.claims.client.InvoiceDetailsClient;
import com.shaw.claims.source.request.SearchClaimInvoiceDetailsRequest;

@Service
public class InvoiceDetailsServiceImpl implements InvoiceDetailsService{
	Logger log = LogManager.getLogger(InvoiceDetailsServiceImpl.class);
    @Autowired
    private InvoiceDetailsClient invoiceDetailsClient;
	
	@Override
	public List<SearchClaimInvoiceDetails> getSearchInvoice(SearchClaimInvoiceDetailsRequest request) {
		log.info("Inside InvoiceDetailsServiceImpl");
        return invoiceDetailsClient.getSearchInvoice(request);
	}
	@Override
	public List<GetInvoiceHeaderResponse> getInvoiceHeader(String customerNumber, String invoiceNumber,
														   String invoiceDate) {
		log.info("Inside InvoiceDetailsServiceImpl");
		return invoiceDetailsClient.getInvoiceHeader(customerNumber, invoiceNumber, invoiceDate);
	}
	@Override
	public List<GetInvoiceChargeResponse> getInvoiceCharges(String invoiceNumber,
															String invoiceDate) {
		log.info("Inside InvoiceDetailsServiceImpl");
		return invoiceDetailsClient.getInvoiceCharges(invoiceNumber, invoiceDate);
	}

	@Override
	public List<GetInvoiceDetailsResponse> getInvoiceDetail(String invoiceNumber, String invoiceDate) {
		log.info("Inside InvoiceDetailsServiceImpl");
		return invoiceDetailsClient.getInvoiceDetail(invoiceNumber, invoiceDate);
	}


}
