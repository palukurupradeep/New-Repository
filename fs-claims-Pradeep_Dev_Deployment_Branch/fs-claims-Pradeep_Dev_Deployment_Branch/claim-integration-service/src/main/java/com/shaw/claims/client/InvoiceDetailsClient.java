
package com.shaw.claims.client;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.shaw.claims.model.*;
import com.shaw.claims.source.response.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.shaw.claims.exception.CommonException;
import com.shaw.claims.source.request.SearchClaimInvoiceDetailsRequest;

@Component
public class InvoiceDetailsClient {

	Logger log = LogManager.getLogger(InvoiceDetailsClient.class);
	@Value("${rest.url}")
	private String restUrl;
	@Autowired
	RestTemplateClient restTemplateClient;

	public List<SearchClaimInvoiceDetails> getSearchInvoice(SearchClaimInvoiceDetailsRequest request) {
		log.info("Inside InvoiceDetailsClient.getClaimInvoiceDetails()");
		RestTemplate restTemplate = restTemplateClient.buildRestTemplate();
		String formattedUrl = "/searchClaimInvoice?";
		if (request != null) {
			formattedUrl += request.getCustomerNumber() != null ? String.format("custNum=%s", request.getCustomerNumber()) : String.format("&custNum=%s", "");
			formattedUrl += request.getInvoiceNumber() != null ? String.format("&invNum=%s", request.getInvoiceNumber()) : String.format("&invNum=%s", "");
			formattedUrl += request.getBolNumber() != null ? String.format("&bolNum=%s", request.getBolNumber()) : String.format("&bolNum=%s", "");
			formattedUrl += request.getPoNumber() != null ? String.format("&poNum=%s", request.getPoNumber()) : String.format("&poNum=%s", "");
			formattedUrl += request.getOrderNumber() != null ? String.format("&orderNum=%s", request.getOrderNumber()) : String.format("&orderNum=%s", "");
			formattedUrl += request.getFromDate() != null ? String.format("&fromDt=%s", request.getFromDate()) : String.format("&fromDt=%s", "");
			formattedUrl += request.getRollNumber() != null ? String.format("&rollNum=%s", request.getRollNumber()) : String.format("&rollNum=%s", "");
			formattedUrl += request.getToDate() != null ? String.format("&toDt=%s", request.getToDate()) : String.format("&toDt=%s", "");
			formattedUrl += request.getCommonCustomerNumber() != null ? String.format("&commCustNum=%s", request.getCommonCustomerNumber()) : String.format("&commCustNum=%s", "");
			formattedUrl += request.getStyleNum() != null ? String.format("&styleNum=%s", request.getStyleNum()) : String.format("&styleNum=%s", "");
			formattedUrl += request.getColorNum() != null ? String.format("&colorNum=%s", request.getColorNum()) : String.format("&colorNum=%s", "");
			formattedUrl += request.getProNum() != 0 ? String.format("&proNum=%s", request.getProNum()) : String.format("&proNum=%s", 0);
			formattedUrl += request.getMastBolNum() != null ? String.format("&mastbolNum=%s", request.getMastBolNum()) : String.format("&mastbolNum=%s", "");
			log.info("formattedUrl :: " + formattedUrl);
		}
		String url = restUrl + formattedUrl;
		log.info("Final Url :: " + url);
		InvoiceDetailsRoot root = restTemplate.getForObject(url, InvoiceDetailsRoot.class);
		log.info("After api call\n" + root);
		List<FINServicesSearchClaimInvoiceResult> rows = root.getSearchClaimInvoiceResponse().getSearchClaimInvoiceResult();

		if (rows == null || rows.isEmpty()) {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND), "Record Not Found");
		}
		String docType = request.getInvoiceNumber() != null ? "INV" : (request.getBolNumber() != null ? "BOL" : (request.getPoNumber() != null ? "PON" : (request.getOrderNumber() != null ? "ORD" : null)));
		return mergeSearchInvoiceClaimDetails(rows, docType);
	}

	private List<SearchClaimInvoiceDetails> mergeSearchInvoiceClaimDetails(List<FINServicesSearchClaimInvoiceResult> rows, String docType) {
		log.info("Inside InvoiceDetailsClient.mergeSearchInvoiceClaimDetails()");
		List<SearchClaimInvoiceDetails> invoiceDetailsList = new ArrayList<>();
		if (rows != null && rows.size() > 0) {
			rows.forEach(row -> {
				SearchClaimInvoiceDetails invoiceDetails = new SearchClaimInvoiceDetails();
				BeanUtils.copyProperties(row, invoiceDetails);
				invoiceDetailsList.add(invoiceDetails);
			});
		}
		return invoiceDetailsList;
	}
	public List<GetInvoiceHeaderResponse> getInvoiceHeader(String customerNumber, String invoiceNumber, String invoiceDate) {
		log.info("Inside InvoiceDetailsClient.getInvoiceHeader()");
		String formattedUrl = "";
		if (customerNumber != null)
			formattedUrl += String.format("/getInvoiceHeader?custNum=%s", customerNumber);

		formattedUrl += invoiceNumber != null ? String.format("&invNum=%s", invoiceNumber) : String.format("&invNum=%s", "");
		formattedUrl += invoiceDate != null ? String.format("&invDt=%s", invoiceDate) : String.format("&invDt=%s", "");
		log.info("formattedUrl :: " + formattedUrl);
		RestTemplate restTemplate = restTemplateClient.buildRestTemplate();
		String url = restUrl + formattedUrl;
		log.info("Final Url :: " + url);
		CustomerDetailsRoot root = restTemplate.getForObject(url, CustomerDetailsRoot.class);
		log.info("After api call\n" + root);
		List<FINServicesGetInvoiceHeaderResult> rows = root.getInvoiceHeaderResponse().getInvoiceHeaderResult();
		if (rows == null || rows.isEmpty()) {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND),
					"Record Not Found for customer number :: " + customerNumber);
		}
		return mergeinvoiceHeaderResponse(rows);
	}

	private List<GetInvoiceHeaderResponse> mergeinvoiceHeaderResponse(List<FINServicesGetInvoiceHeaderResult> rows) {
		log.info("Inside InvoiceDetailsClient.mergeinvoiceHeaderResponse()");
		List<GetInvoiceHeaderResponse> invoiceHeaderResponseList = rows.stream().map(row -> {
			GetInvoiceHeaderResponse getInvoiceHeaderResponse = new GetInvoiceHeaderResponse();
			BeanUtils.copyProperties(row, getInvoiceHeaderResponse);
			return getInvoiceHeaderResponse;
		}).collect(Collectors.toList());
		return invoiceHeaderResponseList;
	}

	public List<GetInvoiceChargeResponse> getInvoiceCharges(String invoiceNumber,
															String invoiceDate) {
		log.info("Inside InvoiceDetailsClient.getInvoiceCharges()");
		String formattedUrl = "";
		if (invoiceNumber != null)
			formattedUrl += String.format("/getInvoiceCharges?invNum=%s", invoiceNumber);

		//formattedUrl += invoiceNumber != null ? String.format("invNum=%s", invoiceNumber) : String.format("invNum=%s", "");
		formattedUrl += invoiceDate != null ? String.format("&invDt=%s", invoiceDate) : String.format("&invDt=%s", "");
		log.info("formattedUrl :: " + formattedUrl);
		RestTemplate restTemplate = restTemplateClient.buildRestTemplate();
		String url = restUrl + formattedUrl;
		log.info("Final Url :: " + url);
		CustomerDetailsRoot root = restTemplate.getForObject(url, CustomerDetailsRoot.class);
		log.info("After api call\n" + root);
		List<FINServicesGetInvoiceChargesResult> rows = root.getInvoiceChargesResponse().getInvoiceChargesResult();
		if (rows == null || rows.isEmpty()) {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND),
					"Record Not Found for invoice number :: " + invoiceNumber);
		}
		return mergeinvoiceChargesResponse(rows);
	}

	private List<GetInvoiceChargeResponse> mergeinvoiceChargesResponse(List<FINServicesGetInvoiceChargesResult> rows) {
		log.info("Inside InvoiceDetailsClient.mergeinvoiceChargesResponse()");
		List<GetInvoiceChargeResponse> invoiceChargeResponseList = rows.stream().map(row -> {
			GetInvoiceChargeResponse getInvoiceChargesResponse = new GetInvoiceChargeResponse();
			BeanUtils.copyProperties(row, getInvoiceChargesResponse);
			return getInvoiceChargesResponse;
		}).collect(Collectors.toList());
		return invoiceChargeResponseList;
	}

	public List<GetInvoiceDetailsResponse> getInvoiceDetail(String invoiceNumber, String invoiceDate) {
		log.info("Inside InvoiceDetailsClient.getInvoiceDetail()");
		String formattedUrl = "";
		if (invoiceNumber != null)
			formattedUrl += String.format("/getInvoiceDetail?invNum=%s", invoiceNumber);

		formattedUrl += invoiceDate != null ? String.format("&invDt=%s", invoiceDate) : String.format("&invDt=%s", "");
		log.info("formattedUrl :: " + formattedUrl);
		RestTemplate restTemplate = restTemplateClient.buildRestTemplate();
		String url = restUrl + formattedUrl;
		log.info("Final Url :: " + url);
		CustomerDetailsRoot root = restTemplate.getForObject(url, CustomerDetailsRoot.class);
		log.info("After api call\n" + root);
		List<FINServicesGetInvoiceDetailResult> rows = root.getInvoiceDetailsResponse().getInvoiceDetailsResult();
		if (rows == null || rows.isEmpty()) {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND),
					"Record Not Found for invoice number :: " + invoiceNumber);
		}
		return mergeInvoiceDetailResponse(rows);
	}

	private List<GetInvoiceDetailsResponse> mergeInvoiceDetailResponse(List<FINServicesGetInvoiceDetailResult> rows) {
		log.info("Inside InvoiceDetailsClient.mergeInvoiceDetailResponse()");
		return rows.stream().map(details -> {
			GetInvoiceDetailsResponse getInvoiceDetailsResponse = new GetInvoiceDetailsResponse();
			BeanUtils.copyProperties(details,getInvoiceDetailsResponse);
			return getInvoiceDetailsResponse;
		}).collect(Collectors.toList());
	}
}
