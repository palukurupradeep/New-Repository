package com.shaw.claims.controller;

import java.util.List;

import com.shaw.claims.model.*;
import com.shaw.claims.source.response.RDCPlantIDResponse;
import com.shaw.claims.source.response.SkuResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shaw.claims.client.CustomerDetailsClient;
import com.shaw.claims.services.CustomerDetailsService;
import com.shaw.claims.services.InvoiceDetailsService;
import com.shaw.claims.source.request.SearchClaimInvoiceDetailsRequest;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/customer/v1")
public class CustomerDetailsController {
	
	Logger log = LogManager.getLogger(CustomerDetailsClient.class);
	 
    @Autowired
    private CustomerDetailsService service;
    
    @Autowired
	  private InvoiceDetailsService invoiceService;

    @GetMapping("/getCustomerDetails")
    public ResponseEntity<List<CustomerDetails>> getCustomerDetails(@RequestParam String customerNumber){
        return new ResponseEntity<>(service.getCustomerDetails(customerNumber), HttpStatus.OK);
    }
    
    @GetMapping("/getCustomerDetailsByStoreNumber")
    public ResponseEntity<List<CustomerDetails>> getCustomerDetailsByStoreNumber(@RequestParam String storeNumber){
    	log.info("Get customerDetails by store number : " + storeNumber);
        return new ResponseEntity<>(service.getCustomerDetailsByStoreNumber(storeNumber), HttpStatus.OK);
    }
	
	@GetMapping("/getCustomerAddress")
	public ResponseEntity<CustomerAddress> getCustomerAddress(@RequestParam(required = true) String customerNumber, @RequestParam(required = false) String addressType) {
		log.info("Get customer address by customer number : " + customerNumber);
		return new ResponseEntity<>(service.getCustomerAddress(customerNumber, addressType), HttpStatus.OK);
	}
	
	@GetMapping("/getClaimAssignmentData")
	public ResponseEntity<List<ClaimAssignmentData>> getClaimAssignmentData(@RequestParam(required = true) String customerNumber) {
		log.info("Get claim assignment data by customer number : " + customerNumber);
		return new ResponseEntity<>(service.getClaimAssignmentData(customerNumber), HttpStatus.OK);
	}
	@GetMapping("/getCustomerSalesCustomerServiceData")
	public ResponseEntity<List<CustomerServiceData>> getCustomerSalesCustomerServiceData(@RequestParam(required = true) String customerNumber) {
		log.info("Get CustomerSales by customer number : " + customerNumber);
		return new ResponseEntity<>(service.getCustomerSalesCustomerServiceData(customerNumber), HttpStatus.OK);
	}
	@GetMapping("/getCrmReasons")
	public ResponseEntity<List<CrmReasons>> getCrmReasons() {
		log.info("Get Crm Reasons");
		return new ResponseEntity<>(service.getCrmReasons(), HttpStatus.OK);
	}
	@PostMapping(value="/getSearchInvoice")
	public ResponseEntity<List<SearchClaimInvoiceDetails>> getSearchInvoice(@RequestBody SearchClaimInvoiceDetailsRequest request) throws Exception {
		log.info("Get ClaimInvoice Details");
		return new ResponseEntity<>(invoiceService.getSearchInvoice(request), HttpStatus.OK);
	}

	@GetMapping("/getInvoiceHeader")
	public ResponseEntity<List<GetInvoiceHeaderResponse>> getInvoiceHeader(@RequestParam String customerNumber, @RequestParam String invoiceNumber, @RequestParam String invoiceDate) {
		return new ResponseEntity<>(invoiceService.getInvoiceHeader(customerNumber, invoiceNumber, invoiceDate), HttpStatus.OK);
	}

	@GetMapping("/getInvoiceCharges")
	public ResponseEntity<List<GetInvoiceChargeResponse>> getInvoiceCharges(@RequestParam String invoiceNumber, @RequestParam String invoiceDate) {
		return new ResponseEntity<>(invoiceService.getInvoiceCharges(invoiceNumber, invoiceDate), HttpStatus.OK);
	}

	@GetMapping("/getInvoiceDetail")
	public ResponseEntity<List<GetInvoiceDetailsResponse>> getInvoiceDetail(@RequestParam("invNum") String invoiceNumber, @RequestParam("invDt") String invoiceDate) {
		return new ResponseEntity<>(invoiceService.getInvoiceDetail(invoiceNumber, invoiceDate),
				HttpStatus.OK);
	}

	@GetMapping("/getCustomerCreditClaimsData")
	public ResponseEntity<List<CustomerCreditClaimsDataResponse>> getCustomerCreditClaimsData(@RequestParam String custNbr) {
		return new ResponseEntity<>(service.getCustomerCreditClaimsData(custNbr),
				HttpStatus.OK);
	}
	
	@GetMapping("/getSellingStyleInfo")
	public ResponseEntity<List<SellingStyleInfo>> getSellingStyleInfo(
			@RequestParam(required = true) String sellStyleNumber) {
		log.info("Get SellingStyleInfo data by sellStyle number : " + sellStyleNumber);
		return new ResponseEntity<>(service.getSellingStyleInfo(sellStyleNumber), HttpStatus.OK);
	}
	
	@GetMapping("/getTerritoryDivisionRegion")
	public ResponseEntity<List<TerritoryDivisionResponse>> getTerritoryDivisionRegion(
			@RequestParam(required = true) String territoryNbr) {
		log.info("Get SellingStyleInfo data by sellStyle number : " + territoryNbr);
		return new ResponseEntity<>(service.getTerritoryDivisionRegion(territoryNbr), HttpStatus.OK);
	}
	
	@GetMapping("/productDetails/{styleNumber}")
	public ResponseEntity<?> getProductDetailsByStyleNumber(@PathVariable String styleNumber) {
		log.info("Get product details by style number : %s", styleNumber);
		return new ResponseEntity<>(service.getProductDetailsByStyleNumber(styleNumber), HttpStatus.OK);
	}

	@GetMapping("/getRDCPlantID")
	public ResponseEntity<List<RDCPlantIDResponse>> getRDCPlantID(@RequestParam("RGAZipCode") String RGAZipCode) {
		log.info("Get RDC PlantId details by RGA Zip Code : %s", RGAZipCode);
		return new ResponseEntity<>(service.getRDCPlantID(RGAZipCode), HttpStatus.OK);
	}
	
	@GetMapping("/getTerritoryDivisionRegionCustSelco")
	public ResponseEntity<List<TerritoryDivisionResponse>> getTerritoryDivisionRegionCustSelco(
			@RequestParam(required = true) String custNbr, 
			@RequestParam(required = true) String selco) {
		log.info("Get SellingStyleInfo data by sellStyle number:" +  custNbr, selco);	
		return new ResponseEntity<>(service.getTerritoryDivisionRegionCustSelco(custNbr,selco), HttpStatus.OK);
	}
	
	@GetMapping("/returnsDecision")
	public ResponseEntity<List<SkuResponse>> returnsDecision(
			@RequestParam(required = true) String plant, 
			@RequestParam(required = true) String sku,
	        @RequestParam(required = true) String keepQty)
	{
		log.info("Get returnsDecision by plant, sku, keepQty: {},{},{}" +  plant, sku,keepQty);	
		return new ResponseEntity<>(service.returnsDecision(plant,sku,keepQty), HttpStatus.OK);
	}
  
}

