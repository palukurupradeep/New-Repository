package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.model.*;
import com.shaw.claims.source.request.SearchClaimInvoiceDetailsRequest;

public interface InvoiceDetailsService {

	List<SearchClaimInvoiceDetails> getSearchInvoice(SearchClaimInvoiceDetailsRequest request);

	public List<GetInvoiceHeaderResponse> getInvoiceHeader(String customerNumber, String invoiceNumber, String invoiceDate);

	public List<GetInvoiceChargeResponse> getInvoiceCharges(String invoiceNumber, String invoiceDate);

	List<GetInvoiceDetailsResponse> getInvoiceDetail(String invoiceNumber, String invoiceDate);
}
