package com.shaw.claims.services;

import com.shaw.claims.dto.ClaimAddressDTO;
import com.shaw.claims.dto.ClaimBatchDetailsDTO;
import com.shaw.claims.dto.ClaimDTO;
import com.shaw.claims.dto.ClaimDetailRecordDTO;
import com.shaw.claims.dto.ClaimDocumentDTO;
import com.shaw.claims.dto.ClaimInitiationResponseDTO;
import com.shaw.claims.dto.ClaimLineDetailDTO;
import com.shaw.claims.dto.ClaimNoteDTO;
import com.shaw.claims.dto.CustomerAddressDetailsDTO;
import com.shaw.claims.dto.InvoiceDetailsDTO;
import com.shaw.claims.dto.InvoiceHeaderResponseDTO;
import com.shaw.claims.dto.InvoiceRequestDTO;
import com.shaw.claims.dto.SearchClaimInvoiceDetailsResponseDTO;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.ClaimBatchDetail;
import com.shaw.claims.model.ClaimBatchHeader;
import com.shaw.claims.repo.ClaimReasonDefinitionRepository;
import com.shaw.claims.repo.UnitOfMeasureRepository;
import com.shaw.claims.util.DateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClaimBatchDetailServiceImpl implements ClaimBatchDetailService {
	Logger log = LogManager.getLogger(ClaimBatchDetailServiceImpl.class);
    @Autowired
    private ClaimReasonDefinitionRepository reasonDefinitionRepository;
    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;
    @Autowired
    DateUtil dateUtil;
    @Value("${claim-integration.rest.url}")
    private String restUrl;
    @Value(("${claim-security.rest.url}"))
    private String restSecurityUrl;
    @Autowired
    private RestTemplate restTemplate;

    public List<ClaimBatchDetail> getClaimBatchDetail(List<ClaimBatchDetailsDTO> batchDetailsDTOS, ClaimBatchHeader claimBatchHeader) {
    	ClaimServiceImpl claimsService=new ClaimServiceImpl();
        return batchDetailsDTOS.stream().map(batchDetailsDTO -> {
        ClaimBatchDetail claimBatchDetail = new ClaimBatchDetail();
        BeanUtils.copyProperties(batchDetailsDTO,claimBatchDetail);
       if(!batchDetailsDTO.getAssociateInvoiceDate().isEmpty())
       {
         claimBatchDetail.setAssociateInvoiceDate(dateUtil.getLocalDateTime(batchDetailsDTO.getAssociateInvoiceDate()));
         }
       else
       {
    	   claimBatchDetail.setAssociateInvoiceDate(LocalDateTime.now());
       }
       claimBatchDetail.setInvoiceDate(dateUtil.getLocalDateTime(batchDetailsDTO.getInvoiceDate()));
        claimBatchDetail.setClaimReasonDefinition(reasonDefinitionRepository.findByClaimReasonCode(batchDetailsDTO.getClaimReasonCode()));
        claimBatchDetail.setUnitOfMeasure(unitOfMeasureRepository.findByUnitOfMeasureCode(batchDetailsDTO.getUnitOfMeasureCode()));
        claimBatchDetail.setClaimBatchHeader(claimBatchHeader);
      
        return claimBatchDetail;
        }).collect(Collectors.toList());
    }
    
    
    public ClaimDTO createDynamicsClaim(String invoiceNumber,String invoiceDate,String customerNumber,int userId,String reasonCode,String claimNote) {
		ClaimDTO claimDTO=new ClaimDTO();
		
		List<InvoiceDetailsDTO> invoiceDetailsDTOS = getInvoiceDetails(invoiceNumber, invoiceDate.split("T00")[0]);
		List<SearchClaimInvoiceDetailsResponseDTO> searchInvoice=getSearchClaimInvoiceDetails(customerNumber,invoiceNumber);
		List<CustomerAddressDetailsDTO> customerDetailsList=getCustomerDetails(customerNumber);
		List<InvoiceHeaderResponseDTO> invoiceHeaderResponseDTOS = getInvoiceHeaders(customerNumber,invoiceNumber, invoiceDate.split("T00")[0]);
		 
		   List<ClaimDocumentDTO> claimDocumentDTOSList=new ArrayList<>();
		   List<ClaimLineDetailDTO> claimLineDetailDTOS=new ArrayList<>();
		   ClaimDocumentDTO claimDocumentDTO=new ClaimDocumentDTO();
		   ClaimNoteDTO noteDto=new ClaimNoteDTO();
		   noteDto.setClaimNoteText(claimNote);
		   noteDto.setNoteTypeCode("INITNOTE");
		   claimDTO.setCustomerNumber(customerNumber);
		   claimDTO.setClaimCategoryCode("DEF");
		   claimDTO.setCreatedByUserId(userId);
		   claimDTO.setModifiedByUserId(userId);
		   claimDTO.setJobStopped(false);
		   claimDTO.setPriorityClaim(false);
		   claimDTO.setClaimNoteDTO(noteDto);
		   claimDTO.setAddToWatchList(false);
		   claimDTO.setClaimReasonCode(reasonCode);
		   claimDTO.setEndUserClaim(false);
		   claimDTO.setBusinessMarketIndicatorCode("");
		  
			   claimDTO.setDivision("");
			   claimDTO.setRegion(invoiceDetailsDTOS==null || invoiceDetailsDTOS.isEmpty()?"": invoiceDetailsDTOS.get(0).getRegion());
			   claimDTO.setTerritory(invoiceDetailsDTOS==null || invoiceDetailsDTOS.isEmpty()?"":invoiceDetailsDTOS.get(0).getTerritory());
			   claimDTO.setSellingCompany(invoiceDetailsDTOS==null || invoiceDetailsDTOS.isEmpty()?"":invoiceDetailsDTOS.get(0).getSelco());
			   claimDocumentDTO.setMasterBillofLading(invoiceDetailsDTOS==null || invoiceDetailsDTOS.isEmpty()?"":invoiceDetailsDTOS.get(0).getMasterBolNbr());
		   
		  
		  
		   claimDTO.setPrimaryCustNumber(customerDetailsList==null || customerDetailsList.isEmpty()?"": customerDetailsList.get(0).getPrimaryCustNumber());
			   claimDTO.setStoreNumber(customerDetailsList==null || customerDetailsList.isEmpty()?"":customerDetailsList.get(0).getStoreNumber()); 
		    claimDTO.setClaimAddressDTO(getClaimAddress(customerDetailsList));
		
		 invoiceDetailsDTOS.stream().forEach(invData->{
			 ClaimLineDetailDTO claimLineDto=new ClaimLineDetailDTO();
			 setClaimLineDetails(claimLineDto,invData,invoiceHeaderResponseDTOS);
			claimLineDetailDTOS.add(claimLineDto);
		 });
		
		/* if(null!=invoiceHeaderResponseDTOS && !invoiceHeaderResponseDTOS.isEmpty()
		&& invoiceHeaderResponseDTOS.get(0).getTotalFreightAmt()!=0.0)
		 {
			 int lineNumber=invoiceDetailsDTOS.size()+1;
			 setFreightAndTaxDetails("FRGHT",lineNumber,BigDecimal.valueOf(invoiceHeaderResponseDTOS.get(0).getTotalFreightAmt()));
		 }
		 
		 if(null!=invoiceHeaderResponseDTOS && !invoiceHeaderResponseDTOS.isEmpty()
					&& invoiceHeaderResponseDTOS.get(0).getTotalTaxAmt()!=0.0)
					 {
						 int lineNumber=invoiceDetailsDTOS.size()+2;
						 setFreightAndTaxDetails("TAX",lineNumber,BigDecimal.valueOf(invoiceHeaderResponseDTOS.get(0).getTotalTaxAmt()));
					 }*/
		
		 claimDocumentDTO.setDocumentNumber(invoiceNumber);
		 claimDocumentDTO.setInvoiceDate(invoiceDate.split("T00")[0]);
		 claimDocumentDTO.setClaimLineDetailDTOS(claimLineDetailDTOS);
		
			 claimDocumentDTO.setBillofLading(searchInvoice==null || searchInvoice.isEmpty()?"":searchInvoice.get(0).getBolNumber());
			 claimDocumentDTO.setOrderNumber(searchInvoice==null || searchInvoice.isEmpty()?"":searchInvoice.get(0).getOrderNumber());
			 claimDocumentDTO.setPurchaseOrderNumber(searchInvoice==null || searchInvoice.isEmpty()?"":searchInvoice.get(0).getPONbr());
		
		
			 claimDocumentDTO.setExchangeRate(invoiceHeaderResponseDTOS==null ||
			invoiceHeaderResponseDTOS.isEmpty()?BigDecimal.valueOf(1):BigDecimal.valueOf(invoiceHeaderResponseDTOS.get(0).getExchangeRate()));
		
		
		 claimDocumentDTOSList.add(claimDocumentDTO);
		 
		 claimDTO.setClaimDocumentDTOS(claimDocumentDTOSList);
		 
		 System.out.println("data="+claimDTO);
		
	
	 return claimDTO;
}

public  List<ClaimAddressDTO> getClaimAddress( List<CustomerAddressDetailsDTO> customerDetailsList)
{
	 List<ClaimAddressDTO> addressList=null;
	 addressList=customerDetailsList.get(0).getAddress().stream().map(addressData->{
		ClaimAddressDTO dto=new ClaimAddressDTO();
		BeanUtils.copyProperties(addressData, dto);
		dto.setFirstName("");
		dto.setLastName("");
		dto.setFullName("");
		 switch (addressData.getAddressType()) {
		case "PHYSICAL": 
			 dto.setAddressTypeCode("CBP");
			 break;
		case "MAILING":
			 dto.setAddressTypeCode("CBM");
			 break;
		case "BILLING":
			 dto.setAddressTypeCode("CBB");
			 break;
		 }
			
		return dto;
	}).collect(Collectors.toList());
	 return  addressList;
}

public void setClaimLineDetails( ClaimLineDetailDTO claimLineDto,InvoiceDetailsDTO invoiceDetailsDto, List<InvoiceHeaderResponseDTO> invoiceHeaderResponseDTOS )
{
	
	claimLineDto.setUnitOfMeasureCode(invoiceDetailsDto.getUnitofMeasure());
	claimLineDto.setRcsCode(invoiceDetailsDto.getRcsCode());
	claimLineDto.setLineNumber(Integer.parseInt(invoiceDetailsDto.getLineNbr()));
	claimLineDto.setStyleNumber(invoiceDetailsDto.getInventoryStyleNbr());
	claimLineDto.setColorNumber(invoiceDetailsDto.getColorNbr());
	claimLineDto.setGrade(invoiceDetailsDto.getGrade());
	claimLineDto.setLineAmountUsd(BigDecimal.valueOf(invoiceDetailsDto.getNetAmount()));
	claimLineDto.setQuantity(BigDecimal.valueOf(invoiceDetailsDto.getQuantity()));
	claimLineDto.setUnitPriceUsd(BigDecimal.valueOf(invoiceDetailsDto.getUnitPrice()));
	claimLineDto.setSellingCompany(invoiceDetailsDto.getSelco());
	claimLineDto.setDetailTypeCode("MERCH");
	claimLineDto.setDyeLot(invoiceDetailsDto.getDyeLot());
	claimLineDto.setLineAmountForeign(BigDecimal.valueOf(0.0));

	claimLineDto.setCurrencyCodeDesignation(
			invoiceHeaderResponseDTOS == null || invoiceHeaderResponseDTOS.isEmpty() ? ""
					: invoiceHeaderResponseDTOS.get(0).getCurrencyCodeDesignation());

	claimLineDto.setCurrencyCodeName(invoiceHeaderResponseDTOS == null || invoiceHeaderResponseDTOS.isEmpty() ? ""
			: invoiceHeaderResponseDTOS.get(0).getCurrencyCodeName());
	claimLineDto.setExchangeRate(
			invoiceHeaderResponseDTOS == null || invoiceHeaderResponseDTOS.isEmpty() ? BigDecimal.valueOf(1)
					: BigDecimal.valueOf(invoiceHeaderResponseDTOS.get(0).getExchangeRate()));
	claimLineDto
			.setPricingCurrencyCode(invoiceHeaderResponseDTOS == null || invoiceHeaderResponseDTOS.isEmpty() ? ""
					: invoiceHeaderResponseDTOS.get(0).getPricingCurrencyCode());

	claimLineDto.setInventoryColor(invoiceDetailsDto.getInventoryColorNbr());
	claimLineDto.setInventoryStyle(invoiceDetailsDto.getInventoryStyleNbr());
	claimLineDto.setLineSourceReference(invoiceDetailsDto.getInvoiceNbr());
	claimLineDto.setLineSourceCode("");
	claimLineDto.setManufacturingPlant("");
	claimLineDto.setProductCode(invoiceDetailsDto.getProductCode());
	claimLineDto.setRollNumber(invoiceDetailsDto.getRollNbr());
	claimLineDto.setUnitPriceForeign(BigDecimal.valueOf(0.0));
	claimLineDto.setVendorId(invoiceDetailsDto.getVendorId());
	claimLineDto.setClaimDetailRecordDTOS(setDetailRecords(invoiceDetailsDto,invoiceHeaderResponseDTOS));
}



public List<ClaimDetailRecordDTO> setDetailRecords(InvoiceDetailsDTO invoiceDetailsDto, List<InvoiceHeaderResponseDTO> invoiceHeaderResponseDTOS)
{
	ClaimDetailRecordDTO claimDetailRecordInvDTO=new ClaimDetailRecordDTO();
	ClaimDetailRecordDTO claimDetailRecordClmDTO=new ClaimDetailRecordDTO();
	List<ClaimDetailRecordDTO> detailsList=new ArrayList<>();
	
	claimDetailRecordInvDTO.setDetailRecordTypeCode("INV");
	claimDetailRecordInvDTO.setWidthInFeet(0);
	claimDetailRecordInvDTO.setWidthInInches(0);
	claimDetailRecordInvDTO.setLengthInFeet(0);
	claimDetailRecordInvDTO.setLengthInInches(0);
	claimDetailRecordInvDTO.setQuantity(BigDecimal.valueOf(invoiceDetailsDto.getQuantity()));
	claimDetailRecordInvDTO.setUnitOfMeasure(invoiceDetailsDto.getUnitofMeasure());
	claimDetailRecordInvDTO.setAmountUsd(BigDecimal.valueOf(invoiceDetailsDto.getNetAmount()));
	claimDetailRecordInvDTO.setAmountForeign(BigDecimal.valueOf(0.0));
	claimDetailRecordInvDTO.setUnitPriceUsd(BigDecimal.valueOf(invoiceDetailsDto.getUnitPrice()));
	claimDetailRecordInvDTO.setUnitPriceForeign(BigDecimal.valueOf(0.0));
	
	
	claimDetailRecordInvDTO.setExchangeRate(invoiceHeaderResponseDTOS==null || invoiceHeaderResponseDTOS.isEmpty()?BigDecimal.valueOf(0.0) :
		BigDecimal.valueOf(invoiceHeaderResponseDTOS.get(0).getExchangeRate()));
	
	
	claimDetailRecordClmDTO.setDetailRecordTypeCode("CLM");
	claimDetailRecordClmDTO.setWidthInFeet(0);
	claimDetailRecordClmDTO.setWidthInInches(0);
	claimDetailRecordClmDTO.setLengthInFeet(0);
	claimDetailRecordClmDTO.setLengthInInches(0);
	claimDetailRecordClmDTO.setQuantity(BigDecimal.valueOf(invoiceDetailsDto.getQuantity()));
	claimDetailRecordClmDTO.setUnitOfMeasure(invoiceDetailsDto.getUnitofMeasure());
	claimDetailRecordClmDTO.setAmountUsd(BigDecimal.valueOf(invoiceDetailsDto.getNetAmount()));
	claimDetailRecordClmDTO.setAmountForeign(BigDecimal.valueOf(0.0));
	claimDetailRecordClmDTO.setUnitPriceUsd(BigDecimal.valueOf(invoiceDetailsDto.getUnitPrice()));
	claimDetailRecordClmDTO.setUnitPriceForeign(BigDecimal.valueOf(0.0));
	claimDetailRecordClmDTO.setExchangeRate(invoiceHeaderResponseDTOS==null || invoiceHeaderResponseDTOS.isEmpty()?BigDecimal.valueOf(0.0) :
		BigDecimal.valueOf(invoiceHeaderResponseDTOS.get(0).getExchangeRate()));
	
	detailsList.add(claimDetailRecordInvDTO);
	detailsList.add(claimDetailRecordClmDTO);
	
	
	return detailsList;
	
	
}



public void setFreightAndTaxDetails( String detailTypeCode,int lineNumber,  BigDecimal lineAmountUsd)
{
	 ClaimLineDetailDTO claimLineDto=new ClaimLineDetailDTO();
	claimLineDto.setUnitOfMeasureCode("");
	claimLineDto.setRcsCode("");
	claimLineDto.setLineNumber(lineNumber);
	claimLineDto.setStyleNumber("");
	claimLineDto.setColorNumber("");
	claimLineDto.setGrade("");
	claimLineDto.setLineAmountUsd(lineAmountUsd);
	claimLineDto.setQuantity(BigDecimal.valueOf(0.0));
	claimLineDto.setUnitPriceUsd(BigDecimal.valueOf(0.0));
	claimLineDto.setSellingCompany("");
	claimLineDto.setDetailTypeCode(detailTypeCode);
	claimLineDto.setDyeLot("");
	claimLineDto.setLineAmountForeign(BigDecimal.valueOf(0.0));

	claimLineDto.setCurrencyCodeDesignation("");
	claimLineDto.setCurrencyCodeName("");
	claimLineDto.setExchangeRate(BigDecimal.valueOf(1));
	claimLineDto.setPricingCurrencyCode("");

	claimLineDto.setInventoryColor("");
	claimLineDto.setInventoryStyle("");
	claimLineDto.setLineSourceReference("");
	claimLineDto.setLineSourceCode("");
	claimLineDto.setManufacturingPlant("");
	claimLineDto.setProductCode("");
	claimLineDto.setRollNumber("");
	claimLineDto.setUnitPriceForeign(BigDecimal.valueOf(0.0));
	claimLineDto.setVendorId("");
	
}

 public List<InvoiceDetailsDTO> getInvoiceDetails(String invNumber, String invDate) {
        List<InvoiceDetailsDTO> invoiceDetailsDTOS = null;
        try {
            String url = "";
            if(invNumber != null)
                url = String.format("/customer/v1/getInvoiceDetail?invNum=%s",invNumber);
            url += invDate != null ? String.format("&invDt=%s", invDate) : String.format("&invDt=%s", "");
            String finalUrl = restUrl + url;
            log.info("Final Url :: " + finalUrl);
            ResponseEntity<List<InvoiceDetailsDTO>> responseEntity = restTemplate.exchange(
                    finalUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<InvoiceDetailsDTO>>() {
                    }
            );
            if (responseEntity.getStatusCode().is2xxSuccessful())
                invoiceDetailsDTOS = responseEntity.getBody();
        } catch (Exception e) {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Invoice details not found in this documentNumber # " + invNumber);
        }
        return invoiceDetailsDTOS;
    }
 
 
 private  List<CustomerAddressDetailsDTO> getCustomerDetails(String customerNumber) {
        List<CustomerAddressDetailsDTO> customerDetailsDTOS = null;
        String url = "/customer/v1/getCustomerDetails?customerNumber=" + customerNumber;
        String finalUrl = restUrl + url;
        log.info("Final URL :: " + finalUrl);
        ResponseEntity<List<CustomerAddressDetailsDTO>> response = restTemplate.exchange(finalUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CustomerAddressDetailsDTO>>() {
                });
        if(response.getStatusCode().is2xxSuccessful()){
            customerDetailsDTOS = response.getBody();
        }else {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Customer Details Not Found For This Customer Number:: " + customerNumber);
        }
        return customerDetailsDTOS;
    }

 private List<SearchClaimInvoiceDetailsResponseDTO> getSearchClaimInvoiceDetails(String customerNumber, String documentNumber) {
        List<SearchClaimInvoiceDetailsResponseDTO> searchClaimInvoiceDetailsResponseDTOS = null;
        try {
            String url = "/customer/v1/getSearchInvoice";
            String finalUrl = restUrl + url;
            log.info("Final Url :: " + finalUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            InvoiceRequestDTO invoiceRequestDTO = new InvoiceRequestDTO(customerNumber, documentNumber);
            HttpEntity<InvoiceRequestDTO> requestEntity = new HttpEntity<>(invoiceRequestDTO, headers);

            ResponseEntity<List<SearchClaimInvoiceDetailsResponseDTO>> responseEntity = restTemplate.exchange(
                    finalUrl,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<List<SearchClaimInvoiceDetailsResponseDTO>>() {}
            );

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                searchClaimInvoiceDetailsResponseDTOS = responseEntity.getBody();
            }
        } catch (Exception e) {
            log.error("Error fetching search claim invoice details", e);
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Search Claim Invoice details not found for documentNumber # " + documentNumber + " and customerNumber # " + customerNumber);
        }
        return searchClaimInvoiceDetailsResponseDTOS;
    }

 
 private List<InvoiceHeaderResponseDTO> getInvoiceHeaders(String customerNumber, String documentNumber, String documentDate) {
        List<InvoiceHeaderResponseDTO> invoiceHeaderResponseDTOS = null;
        try {
            String url = "";
            if(customerNumber != null)
                url = String.format("/customer/v1/getInvoiceHeader?customerNumber=%s",customerNumber);
            url += documentNumber != null ? String.format("&invoiceNumber=%s", documentNumber) : String.format("&invoiceNumber=%s", "");
            url += documentDate != null ? String.format("&invoiceDate=%s", documentDate) : String.format("&invoiceDate=%s", "");
            String finalUrl = restUrl + url;
            log.info("Final Url :: " + finalUrl);
            ResponseEntity<List<InvoiceHeaderResponseDTO>> responseEntity = restTemplate.exchange(
                    finalUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<InvoiceHeaderResponseDTO>>() {
                    }
            );
            if (responseEntity.getStatusCode().is2xxSuccessful())
                invoiceHeaderResponseDTOS = responseEntity.getBody();
        } catch (Exception e) {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Invoice Header details not found in this documentNumber # " + documentNumber + " and CustomerNumber # " + customerNumber);
        }
        return invoiceHeaderResponseDTOS;
    }


}
