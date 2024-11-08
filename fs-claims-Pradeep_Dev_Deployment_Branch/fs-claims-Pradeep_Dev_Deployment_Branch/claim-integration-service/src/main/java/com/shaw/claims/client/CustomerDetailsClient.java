
package com.shaw.claims.client;

import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.*;
import com.shaw.claims.source.response.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerDetailsClient {

    Logger log = LogManager.getLogger(CustomerDetailsClient.class);
    @Value("${rest.url}")
    private String restUrl;
    @Autowired
    RestTemplateClient restTemplateClient;
    
    @Value("${rest.dsserviceurl}")
    private String restDsServiceUrl;
    @Autowired
    private RestTemplate restTemplate;

    public List<CustomerDetails> getCustomerDetails(String customerNumber) {
        log.info("Inside CustomerDetailsClient.getCustomerDetails()");
        String formattedUrl = "";
        if (customerNumber != null)
            formattedUrl += String.format("/getCustomerDetails?custNbr=%s", customerNumber);
        log.info("formattedUrl :: " + formattedUrl);
        RestTemplate restTemplate = restTemplateClient.buildRestTemplate();
        String url = restUrl + formattedUrl;
        log.info("Final Url :: " + url);
        CustomerDetailsRoot root = restTemplate.getForObject(url, CustomerDetailsRoot.class);
        log.info("After api call\n" + root);
        List<FINServicesGetCustomerDetailsResult> rows = root.getCustomerDetailsResponse().getCustomerDetailsResult();
        
        if(rows == null || rows.isEmpty()){
        	throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND), "Record Not Found for customer number :: " + customerNumber);
        }
        List<CustomerServiceData>customerServiceData= getCustomerSalesCustomerServiceData(customerNumber);
        return mergeCustomerDetails(rows,customerServiceData);
    }

    private List<CustomerDetails> mergeCustomerDetails(List<FINServicesGetCustomerDetailsResult> rows,  List<CustomerServiceData>customerServiceData) {
        log.info("Inside CustomerDetailsClient.mergeCustomerDetails()");
        List<CustomerDetails> customerDetailsList = new ArrayList<>();
        rows.forEach(row -> {
            CustomerDetails customerDetails = new CustomerDetails();
            BeanUtils.copyProperties(row, customerDetails);
            
            if(customerDetails.getPhoneNumber() != null && !customerDetails.getPhoneNumber().isBlank() && customerDetails.getPhoneNumber().contains("-") && 
            		customerDetails.getPhoneAreaCode() != null && !customerDetails.getPhoneAreaCode().isBlank()) {
            	customerDetails.setPhoneNumber("("+customerDetails.getPhoneAreaCode()+")"+customerDetails.getPhoneNumber());
            } else {
            	customerDetails.setPhoneNumber(String.format("(%s)%s-%s",
            			customerDetails.getPhoneNumber().substring(0, 3), 
            			customerDetails.getPhoneNumber().substring(3, 6), 
            			customerDetails.getPhoneNumber().substring(6)));
            }
            
          CustomerAddress customerAddress = getCustomerAddress(row.getCustomerNumber(), null);
          customerDetails.setAddress(customerAddress.getAddress());
          if(null!=customerServiceData) {
         customerDetails.setStoreType(customerServiceData.get(0).getStoreType());
         customerDetails.setGroupAccount(customerServiceData.get(0).getGroupAccount());
          }
         
            customerDetailsList.add(customerDetails);
        });
        return customerDetailsList;
    }

	public List<CustomerDetails> getCustomerDetailsByStoreNumber(String storeNumber) {
		log.info("Inside CustomerDetailsClient.getCustomerDetails()");
        String formattedUrl = "";
        if (storeNumber != null)
            formattedUrl += String.format("/getCustomerStoreNumbers?storeNbr=%s", storeNumber);
        log.info("formattedUrl :: " + formattedUrl);
        RestTemplate restTemplate = restTemplateClient.buildRestTemplate();
        String url = restUrl + formattedUrl;
        log.info("Final Url :: " + url);
        CustomerDetailsRoot root = restTemplate.getForObject(url, CustomerDetailsRoot.class);
        log.info("After api call\n" + root);
        List<FINServicesGetCustomerStoreNumberResult> rows = root.getCustomerDetailsByStoreNumberResponse().getCustomerDetailsByStoreResult();
        
        if(rows == null || rows.isEmpty()){
        	throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND), "Record Not Found for store number :: " + storeNumber);
        }
        return mergeCustomerDetailsByStore(rows);
	}
	
	private List<CustomerDetails> mergeCustomerDetailsByStore(List<FINServicesGetCustomerStoreNumberResult> rows) {
        log.info("Inside CustomerDetailsClient.mergeCustomerDetails()");
        List<CustomerDetails> customerDetailsList = new ArrayList<>();
        rows.forEach(row -> {
            CustomerDetails customerDetails = new CustomerDetails();
            BeanUtils.copyProperties(row, customerDetails);
            Address address = new Address();
            BeanUtils.copyProperties(row, address);
           // CustomerAddress customerAddress = getCustomerAddress(row.getCustomerNumber(), null);
            customerDetails.setAddress(List.of(address));
            customerDetailsList.add(customerDetails);
        });
        return customerDetailsList;
    }

	public CustomerAddress getCustomerAddress(String customerNumber, String addressType) {
		log.info("Inside CustomerDetailsClient.getCustomerAddress()");
		String formattedUrl = "";
		if (customerNumber != null) 
			formattedUrl += String.format("/getCustomerAddress?custNbr=%s", customerNumber);
		
		formattedUrl += addressType != null ? String.format("&addrSelectType=%s", addressType) : String.format("&addrSelectType=%s", "");
		log.info("formattedUrl :: " + formattedUrl);
		RestTemplate restTemplate = restTemplateClient.buildRestTemplate();
		String url = restUrl + formattedUrl;
		log.info("Final Url :: " + url);
		CustomerDetailsRoot root = restTemplate.getForObject(url, CustomerDetailsRoot.class);
		log.info("After api call\n" + root);
		List<FINServicesGetCustomerAddressResult> rows = root.getCustomerAddress().getCustomerAddressResult();
		if (rows == null || rows.isEmpty()) {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND),
					"Record Not Found for customer number :: " + customerNumber);
		}
		return mergeCustomerAddressInfoByCustNumber(rows);
	}

	private CustomerAddress mergeCustomerAddressInfoByCustNumber(List<FINServicesGetCustomerAddressResult> rows) {
		 log.info("Inside CustomerDetailsClient.mergeCustomerAddressInfoByCustNumber()");
	        CustomerAddress customerDetails = new CustomerAddress();
	        List<Address> addressList = new ArrayList<>();
	        rows.forEach(row -> {
	           customerDetails.setCustomerNumber(row.getCustomerNumber());
	           customerDetails.setCustomerName(row.getCustomerName());
	            Address address = new Address();
	            BeanUtils.copyProperties(row, address);
	            addressList.add(address);
	        });
	        customerDetails.setAddress(addressList);
	        return customerDetails;
	}

	public List<ClaimAssignmentData> getClaimAssignmentData(String customerNumber) {
		log.info("Inside CustomerDetailsClient.getClaimAssignmentData()");
		String formattedUrl = "";
		if (customerNumber != null) 
			formattedUrl += String.format("/getClaimAssignmentData?custNbr=%s", customerNumber);
		
		log.info("formattedUrl :: " + formattedUrl);
		RestTemplate restTemplate = restTemplateClient.buildRestTemplate();
		String url = restUrl + formattedUrl;
		log.info("Final Url :: " + url);
		CustomerDetailsRoot root = restTemplate.getForObject(url, CustomerDetailsRoot.class);
		log.info("After api call\n" + root);
		List<FINServicesGetClaimAssignmentDataResult> rows = root.getClaimAssignmentData().getClaimAssignmentDataResult();
		if (rows == null || rows.isEmpty()) {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND),
					"Record Not Found for customer number :: " + customerNumber);
		}
		return mergeClaimAssignmentDataByCustNumber(rows);
	}

	private List<ClaimAssignmentData> mergeClaimAssignmentDataByCustNumber(
			List<FINServicesGetClaimAssignmentDataResult> rows) {
		log.info("Inside CustomerDetailsClient.mergeClaimAssignmentDataByCustNumber()");
		List<ClaimAssignmentData> customerDetailsList = rows.stream().map(row -> {
			ClaimAssignmentData claimAssignmentData = new ClaimAssignmentData();
			BeanUtils.copyProperties(row, claimAssignmentData);
			return claimAssignmentData;
		}).collect(Collectors.toList());
		return customerDetailsList;
	}

	public List<CustomerServiceData> getCustomerSalesCustomerServiceData(String customerNumber) {
		log.info("Inside CustomerDetailsClient.getCustomerSalesCustomerServiceData()");
		String formattedUrl = "";
		if (customerNumber != null) 
			formattedUrl += String.format("/getCustomerSalesCustomerServiceData?custNbr=%s", customerNumber);
		
		log.info("formattedUrl :: " + formattedUrl);
		RestTemplate restTemplate = restTemplateClient.buildRestTemplate();
		String url = restUrl + formattedUrl;
		log.info("Final Url :: " + url);
		CustomerDetailsRoot root = restTemplate.getForObject(url, CustomerDetailsRoot.class);
		log.info("After api call\n" + root);
		List<FINServicesGetCustomerSalesCustomerServiceDataResult> rows = root.getCustomerServiceData().getGetCustomerServiceDataResult();
		if (rows == null || rows.isEmpty()) {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND),
					"Record Not Found for customer number :: " + customerNumber);
		}
		return mergeCustomerServicetDataByCustNumber(rows);
	}

	private List<CustomerServiceData> mergeCustomerServicetDataByCustNumber(
			List<FINServicesGetCustomerSalesCustomerServiceDataResult> rows) {
		log.info("Inside CustomerDetailsClient.mergeCustomerServicetDataByCustNumber()");
        return rows.stream().map(row -> {
			CustomerServiceData customerServiceData = new CustomerServiceData();
			BeanUtils.copyProperties(row, customerServiceData);
			return customerServiceData;
		}).collect(Collectors.toList());
	}

	public List<CrmReasons> getCrmReasons() {
		log.info("Inside CustomerDetailsClient.getCrmReasons()");
		String formattedUrl =  String.format("/getCrmReasons");
		
		log.info("formattedUrl :: " + formattedUrl);
		RestTemplate restTemplate = restTemplateClient.buildRestTemplate();
		String url = restUrl + formattedUrl;
		log.info("Final Url :: " + url);
		CustomerDetailsRoot root = restTemplate.getForObject(url, CustomerDetailsRoot.class);
		log.info("After api call\n" + root);
		List<FINServicesGetCrmReasonsResult> rows = root.getCrmReasonsResponse().getCrmReasonsResult();
		if (rows == null || rows.isEmpty()) {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND),
					"Records Not Found for crm reason");
		}
		return mergeCrmReasons(rows);
	}

	private List<CrmReasons> mergeCrmReasons(List<FINServicesGetCrmReasonsResult> rows) {
		log.info("Inside CustomerDetailsClient.mergeCrmReasons()");
		List<CrmReasons> customerSerivceDetailsList = rows.stream().map(row -> {
			CrmReasons crmReasonData = new CrmReasons();
			crmReasonData.setApprRestrict(row.getApprRestrict().trim());
			crmReasonData.setCode(row.getCode().trim());
			crmReasonData.setDescription(row.getDescription().trim());
			crmReasonData.setOverrideRestrict(row.getOverrideRestrict().trim());
			crmReasonData.setProdRestrict(row.getProdRestrict().trim());
			crmReasonData.setSlcoRestrict(row.getSlcoRestrict().trim());
			crmReasonData.setXlatRestrict(row.getXlatRestrict().trim());
			return crmReasonData;
		}).collect(Collectors.toList());
		return customerSerivceDetailsList;
	}

	public List<GetInvoiceHeaderResponse> getInvoiceHeader(String customerNumber, String invoiceNumber, String invoiceDate) {
		log.info("Inside CustomerDetailsClient.getInvoiceHeader()");
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
		log.info("Inside CustomerDetailsClient.mergeinvoiceHeaderResponse()");
		List<GetInvoiceHeaderResponse> invoiceHeaderResponseList = rows.stream().map(row -> {
			GetInvoiceHeaderResponse getInvoiceHeaderResponse = new GetInvoiceHeaderResponse();
			BeanUtils.copyProperties(row, getInvoiceHeaderResponse);
			return getInvoiceHeaderResponse;
		}).collect(Collectors.toList());
		return invoiceHeaderResponseList;
	}

	public List<GetInvoiceChargeResponse> getInvoiceCharges(String invoiceNumber,
			String invoiceDate) {
		log.info("Inside CustomerDetailsClient.getInvoiceCharges()");
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
		log.info("Inside CustomerDetailsClient.mergeinvoiceChargesResponse()");
		List<GetInvoiceChargeResponse> invoiceChargeResponseList = rows.stream().map(row -> {
			GetInvoiceChargeResponse getInvoiceChargesResponse = new GetInvoiceChargeResponse();
			BeanUtils.copyProperties(row, getInvoiceChargesResponse);
			return getInvoiceChargesResponse;
		}).collect(Collectors.toList());
		return invoiceChargeResponseList;
	}

	public List<CustomerCreditClaimsDataResponse> getCustomerCreditClaimsData(String custNbr) {
		log.info("Inside CustomerDetailsClient.getCustomerCreditClaimsData()");
		String formattedUrl = String.format("/getCustomerCreditClaimsData?custNbr=%s", custNbr);
		log.info("formattedUrl :: " + formattedUrl);
		RestTemplate restTemplate = restTemplateClient.buildRestTemplate();
		String url = restUrl + formattedUrl;
		log.info("Final Url :: " + url);
		CustomerDetailsRoot root = restTemplate.getForObject(url, CustomerDetailsRoot.class);
		log.info("After api call\n" + root);
		List<FINServicesGetCustomerCreditClaimsDataResponse> rows = root.getCustomerCreditClaimsDataResponse().getCustomerCreditClaimsDataResult();
		if (rows == null || rows.isEmpty()) {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND),
					"Record Not Found for customer number :: " + custNbr);
		}
		return mergeGetCustomerCreditClaimsData(rows);
	}

	private List<CustomerCreditClaimsDataResponse> mergeGetCustomerCreditClaimsData(List<FINServicesGetCustomerCreditClaimsDataResponse> rows) {
		log.info("Inside CustomerDetailsClient.mergeGetCustomerCreditClaimsData()");
		return rows.stream().map(data -> {
			CustomerCreditClaimsDataResponse customerCreditClaimsDataResponse = new CustomerCreditClaimsDataResponse();
			BeanUtils.copyProperties(data,customerCreditClaimsDataResponse);
			return customerCreditClaimsDataResponse;
		}).collect(Collectors.toList());
	}
	
	public List<SellingStyleInfo> getSellingStyleInfo(String sellStyleNumber) {
		log.info("Inside CustomerDetailsClient.getSellingStyleInfo()");
		String formattedUrl = "";
		if (sellStyleNumber != null) 
			formattedUrl += String.format("/getSellingStyleInfo?SellStyleNumber=%s", sellStyleNumber);
		
		log.info("formattedUrl :: " + formattedUrl);
		RestTemplate restTemplate = restTemplateClient.buildRestTemplate();
		String url = restUrl + formattedUrl;
		log.info("Final Url :: " + url);
		CustomerDetailsRoot root = restTemplate.getForObject(url, CustomerDetailsRoot.class);
		log.info("After api call\n" + root);
		List<FINServicesGetSellingStyleInfoResult> rows = root.getSellingStyleInfoResponse().getSellingStyleInfoResult();
		if (rows == null || rows.isEmpty()) {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND),
					"Record Not Found for sellStyleNumber :: " + sellStyleNumber);
		}
		return mergeSellingStyleInfoBySellStyleNumber(rows);
	}
	
	private List<SellingStyleInfo> mergeSellingStyleInfoBySellStyleNumber(
			List<FINServicesGetSellingStyleInfoResult> rows) {
		log.info("Inside CustomerDetailsClient.mergeSellingStyleInfoBySellStyleNumber()");
		List<SellingStyleInfo> customerDetailsList = rows.stream().map(row -> {
			SellingStyleInfo sellingStyleInfo = new SellingStyleInfo();
			BeanUtils.copyProperties(row, sellingStyleInfo);
			return sellingStyleInfo;
		}).collect(Collectors.toList());
		return customerDetailsList;
	}

	public List<TerritoryDivisionResponse> getTerritoryDivisionRegion(String territoryNbr) {
		log.info("Inside CustomerDetailsClient.getTerritoryDivisionRegion()");
		String formattedUrl = "";
		if (territoryNbr != null) 
			formattedUrl += String.format("/getTerritoryDivisionRegion?territoryNbr=%s", territoryNbr);
		
		log.info("formattedUrl :: " + formattedUrl);
		RestTemplate restTemplate = restTemplateClient.buildRestTemplate();
		String url = restUrl + formattedUrl;
		log.info("Final Url :: " + url);
		CustomerDetailsRoot root = restTemplate.getForObject(url, CustomerDetailsRoot.class);
		log.info("After api call\n" + root);
		List<FINServicesGetTerritoryDivisionRegionResult> rows = root.getTerritoryDivisionRegionResponse().getTerritoryDivisionRegionResult();
		if (rows == null || rows.isEmpty()) {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND),
					"Record Not Found for sellStyleNumber :: " + territoryNbr);
		}
		return mergeTerritoryDivisionByTerritoryNumber(rows);
	}
	
	private List<TerritoryDivisionResponse> mergeTerritoryDivisionByTerritoryNumber(
			List<FINServicesGetTerritoryDivisionRegionResult> rows) {
		log.info("Inside CustomerDetailsClient.mergeTerritoryDivisionByTerritoryNumber()");
		List<TerritoryDivisionResponse> territoryDivisionLlist = rows.stream().map(row -> {
			TerritoryDivisionResponse territoryDivision = new TerritoryDivisionResponse();
			BeanUtils.copyProperties(row, territoryDivision);
			return territoryDivision;
		}).collect(Collectors.toList());
		return territoryDivisionLlist;
	}

	public List<RDCPlantIDResponse> getRDCPlantID(String rgaZipCode) {
		log.info("Inside CustomerDetailsClient.getRDCPlantID()");
		String formattedUrl = String.format("/getRDCPlantID?RGAZipCode=%s", rgaZipCode);
		log.info("formattedUrl :: " + formattedUrl);
		RestTemplate restTemplate = restTemplateClient.buildRestTemplate();
		String url = restUrl + formattedUrl;
		log.info("Final Url :: " + url);
		CustomerDetailsRoot root = restTemplate.getForObject(url, CustomerDetailsRoot.class);
		log.info("After api call\n" + root);
		List<FINServicesGetRDCPlantIDResult> rows = root.getRdcPlantIDResponse().rdcPlantIDResult;
		if (rows == null || rows.isEmpty()) {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND),
					"Record Not Found for this zipcode :: " + rgaZipCode);
		}
		return mergeGetRDCPlantIdData(rows);
	}

	private List<RDCPlantIDResponse> mergeGetRDCPlantIdData(List<FINServicesGetRDCPlantIDResult> rows) {
		log.info("Inside CustomerDetailsClient.mergeGetRDCPlantIdData()");
		return rows.stream().map(data -> {
			RDCPlantIDResponse rdcPlantIDResponse = new RDCPlantIDResponse();
			BeanUtils.copyProperties(data,rdcPlantIDResponse);
			return rdcPlantIDResponse;
		}).collect(Collectors.toList());
	}

	public List<TerritoryDivisionResponse> getTerritoryDivisionRegionCustSelco(String custNbr, String selco) {
		log.info("Inside CustomerDetailsClient.getTerritoryDivisionRegionCustSelco()");
		String formattedUrl = "";
		if (custNbr != null) 
			formattedUrl += String.format("/getTerritoryDivisionRegionCustSelco?custNbr=%s", custNbr);
		
		formattedUrl += selco != null ? String.format("&selco=%s", selco) : String.format("&selco=%s", "");
		log.info("formattedUrl :: " + formattedUrl);
		RestTemplate restTemplate = restTemplateClient.buildRestTemplate();
		String url = restUrl + formattedUrl;
		log.info("Final Url :: " + url);
		CustomerDetailsRoot root = restTemplate.getForObject(url, CustomerDetailsRoot.class);
		log.info("After api call\n" + root);
		List<FINServicesGetTerritoryDivisionRegionResult> rows = root.getTerritoryDivisionRegionCustSelcoResponse().getTerritoryDivisionRegionCustSelcoResult();
		if (rows == null || rows.isEmpty()) {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND),
					"Record Not Found for customber number :: " + custNbr);
		}
		return mergeTerritoryDivisionRegionCustSelcoByCustomerNumber(rows);
		}
	
	private List<TerritoryDivisionResponse> mergeTerritoryDivisionRegionCustSelcoByCustomerNumber(
			List<FINServicesGetTerritoryDivisionRegionResult> rows) {
		log.info("Inside CustomerDetailsClient.mergeTerritoryDivisionByTerritoryNumber()");
		List<TerritoryDivisionResponse> territoryDivisionCustSelcolist = rows.stream().map(row -> {
			TerritoryDivisionResponse territoryDivisionCustSelco = new TerritoryDivisionResponse();
			BeanUtils.copyProperties(row, territoryDivisionCustSelco);
			return territoryDivisionCustSelco;
		}).collect(Collectors.toList());
		return territoryDivisionCustSelcolist;
	}

	public List<SkuResponse> returnsDecision(String plant, String sku, String keepQty) {
		String url = "/returns-decision/v1/sku/" + sku + "?plant=" + plant + "&keepQty="+keepQty;
		String finalUrl = restDsServiceUrl + url;

		log.info("Fetching data from URL: " + finalUrl);
		ResponseEntity<SkuResponse> response = restTemplate.exchange(finalUrl, HttpMethod.GET, null,
				new ParameterizedTypeReference<SkuResponse>() {
				});

		if (response != null && response.getStatusCode().is2xxSuccessful()) {
			return Arrays.asList(response.getBody());
		}
		return null;
	}
	
}
