package com.shaw.claims.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shaw.claims.dto.ClaimAssignmentDataResponseDTO;
import com.shaw.claims.dto.SearchClaimHistoryRequestDTO;
import com.shaw.claims.dto.SearchClaimHistoryResponseDTO;
import com.shaw.claims.dto.SearchHistoryEndUserDTO;
import com.shaw.claims.dto.SecondarySearchDTO;
import com.shaw.claims.dto.UserGroupMappingResponseDTO;
import com.shaw.claims.model.Claim;
import com.shaw.claims.model.ClaimAddress;
import com.shaw.claims.model.ClaimCrmHeader;
import com.shaw.claims.model.ClaimDispositionHeader;
import com.shaw.claims.model.ClaimDocument;
import com.shaw.claims.model.ClaimLineDetail;
import com.shaw.claims.model.ClaimReasonDefinition;
import com.shaw.claims.model.ClaimRgaHeader;
import com.shaw.claims.model.ClaimStatus;
import com.shaw.claims.model.DocumentType;
import com.shaw.claims.model.EndUserInformation;
import com.shaw.claims.model.Lookup;
import com.shaw.claims.repo.ClaimRepository;
import com.shaw.claims.repo.ClaimStatusRepository;
import com.shaw.claims.repo.DocumentTypeRepository;
import com.shaw.claims.repo.LookupRepository;
import com.shaw.claims.repo.SellingCompanyRepository;
import com.shaw.claims.repo.StateRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class SearchClaimHistoryServiceImpl implements SearchClaimHistoryService {
	Logger log = LogManager.getLogger(SearchClaimHistoryService.class);
	
	
	@Autowired
	SellingCompanyRepository sellingCompanyRepository;
	@Value("${claim-integration.rest.url}")
	private String restUrl;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	StateRepository stateRepository;
	
	@Autowired
	LookupRepository lookupRepository;

	@Autowired
	ClaimRepository claimrepository;

	@Autowired
	ClaimStatusRepository claimStatusRepository;
	
	 @Autowired
	   EntityManager entityManager;
	
	@Autowired
	DocumentTypeRepository documentTypeRepository;
	
	public List<SearchClaimHistoryResponseDTO> searchClaimHistory(SearchClaimHistoryRequestDTO searchClaimHistoryRequest) {
		log.info("Enter searchClaimHistory()");  
		
		List<SearchClaimHistoryResponseDTO> searchHistoryList = new ArrayList<>();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Claim> query =cb.createQuery(Claim.class);
		Root<Claim> claimRoot = query.from(Claim.class);
		List<Predicate> predicates = new ArrayList<>();
	
		
		
		if (searchClaimHistoryRequest.getPrimarySearchType().equalsIgnoreCase("USR")) {
			UserGroupMappingResponseDTO userInfo=searchClaimHistoryRequest.getClaimUser();
			predicates.add(cb.equal(claimRoot.get("claimUserId"), userInfo.getUserId()));
		}
	if (searchClaimHistoryRequest.getPrimarySearchType().equalsIgnoreCase("CUS") && !searchClaimHistoryRequest.getPrimaryValue().isEmpty()) {
		if(searchClaimHistoryRequest.isPrimarySearchToggle==true) {
			log.info("Enter inside primarycustomernumber");
			predicates.add(cb.equal(claimRoot.get("primaryCustNumber"), searchClaimHistoryRequest.getPrimaryValue()));
		}else {
			predicates.add(cb.equal(claimRoot.get("customerNumber"), searchClaimHistoryRequest.getPrimaryValue()));
		}
			
			
		}
	if (searchClaimHistoryRequest.getPrimarySearchType().equalsIgnoreCase("STO") && !searchClaimHistoryRequest.getPrimaryValue().isEmpty()) {
		
			predicates.add(cb.equal(claimRoot.get("storeNumber"), searchClaimHistoryRequest.getPrimaryValue()));
		}
		if (searchClaimHistoryRequest.getPrimarySearchType().equalsIgnoreCase("INV") && !searchClaimHistoryRequest.getPrimaryValue().isEmpty()) {
			
	         Join<Claim, ClaimDocument> claimDocumentJoin = claimRoot.join("claimDocuments");
	         
	         // Join ClaimDocument with DocumentType entity
	         Join<ClaimDocument, DocumentType> documentTypeJoin = claimDocumentJoin.join("documentType");
	          
	         // Add the condition to filter by DocumentTypeCode 'INV'
	         predicates.add(cb.equal(documentTypeJoin.get("documentTypeCode"), "INV"));
	          
	         // Add the existing condition to filter by documentNumber
	         predicates.add(cb.equal(claimDocumentJoin.get("documentNumber"),searchClaimHistoryRequest.getPrimaryValue()));
	     }
		
		if (searchClaimHistoryRequest.getPrimarySearchType().equalsIgnoreCase("ORD") && !searchClaimHistoryRequest.getPrimaryValue().isEmpty()) {
			
	         Join<Claim, ClaimDocument> claimDocumentJoin = claimRoot.join("claimDocuments");
	         
	         	// Join ClaimDocument with DocumentType entity
				Join<ClaimDocument, DocumentType> documentTypeJoin = claimDocumentJoin.join("documentType");

				// Add the condition to filter by DocumentTypeCode 'ORD'
				predicates.add(cb.equal(documentTypeJoin.get("documentTypeCode"), "ORD"));
	 
	         predicates.add(cb.equal(claimDocumentJoin.get("orderNumber"),searchClaimHistoryRequest.getPrimaryValue()));
	     }
		
		if (searchClaimHistoryRequest.getPrimarySearchType().equalsIgnoreCase("RET") && !searchClaimHistoryRequest.getPrimaryValue().isEmpty()) {
			
			 Join<Claim,ClaimRgaHeader> rgaNumberJoin = claimRoot.join("claimRgaHeader");
	         predicates.add(cb.equal(rgaNumberJoin.get("rgaNumber"),searchClaimHistoryRequest.getPrimaryValue()));
		}
		if (searchClaimHistoryRequest.getPrimarySearchType().equalsIgnoreCase("CRM") && !searchClaimHistoryRequest.getPrimaryValue().isEmpty()) {

			 Join<Claim,ClaimCrmHeader> crmNumberJoin = claimRoot.join("claimCrmHeader");
	         predicates.add(cb.equal(crmNumberJoin.get("crmNumber"),searchClaimHistoryRequest.getPrimaryValue()));
		}
		if (searchClaimHistoryRequest.getPrimarySearchType().equalsIgnoreCase("TM") && !searchClaimHistoryRequest.getPrimaryValue().isEmpty()) {
			
			predicates.add(cb.equal(claimRoot.get("territory"), searchClaimHistoryRequest.getPrimaryValue()));
		}
		
		if (searchClaimHistoryRequest.getPrimarySearchType().equalsIgnoreCase("RM") && !searchClaimHistoryRequest.getPrimaryValue().isEmpty()) {
			
			predicates.add(cb.equal(claimRoot.get("region"), searchClaimHistoryRequest.getPrimaryValue()));
		}
		if (searchClaimHistoryRequest.getPrimarySearchType().equalsIgnoreCase("DVP") && !searchClaimHistoryRequest.getPrimaryValue().isEmpty()) {
			
			predicates.add(cb.equal(claimRoot.get("division"), searchClaimHistoryRequest.getPrimaryValue()));
		}
		
		if (searchClaimHistoryRequest.getPrimarySearchType().equalsIgnoreCase("RSN") && !searchClaimHistoryRequest.getPrimaryValue().isEmpty()) {
			Join<Claim,ClaimReasonDefinition> claimReasonCodeJoin = claimRoot.join("claimReasonDefinition");
	         predicates.add(cb.equal(claimReasonCodeJoin.get("claimReasonCode"),searchClaimHistoryRequest.getPrimaryValue()));
		}
		if (searchClaimHistoryRequest.getPrimarySearchType().equalsIgnoreCase("PON") && !searchClaimHistoryRequest.getPrimaryValue().isEmpty()) {
			 Join<Claim,ClaimDocument> purchaseOrderJoin = claimRoot.join("claimDocuments");
			    // Join ClaimDocument with DocumentType entity
				Join<ClaimDocument, DocumentType> documentTypeJoin = purchaseOrderJoin.join("documentType");
				// Add the condition to filter by DocumentTypeCode 'PON'
				predicates.add(cb.equal(documentTypeJoin.get("documentTypeCode"), "PON"));
	         predicates.add(cb.equal(purchaseOrderJoin.get("purchaseOrderNumber"),searchClaimHistoryRequest.getPrimaryValue()));
		}
		if (searchClaimHistoryRequest.getPrimarySearchType().equalsIgnoreCase("STY") && !searchClaimHistoryRequest.getPrimaryValue().isEmpty()) {
			 Join<Claim,ClaimLineDetail> styleNumberJoin = claimRoot.join("claimLineDetail");
	         predicates.add(cb.equal(styleNumberJoin.get("styleNumber"),searchClaimHistoryRequest.getPrimaryValue()));
		}
		if (searchClaimHistoryRequest.getPrimarySearchType().equalsIgnoreCase("IVS") && !searchClaimHistoryRequest.getPrimaryValue().isEmpty()) {
			 Join<Claim,ClaimLineDetail> inventoryStyleJoin = claimRoot.join("claimLineDetail");
	         predicates.add(cb.equal(inventoryStyleJoin.get("inventoryStyle"),searchClaimHistoryRequest.getPrimaryValue()));
		}
		if (searchClaimHistoryRequest.getPrimarySearchType().equalsIgnoreCase("ROL") && !searchClaimHistoryRequest.getPrimaryValue().isEmpty()) {
			 Join<Claim,ClaimLineDetail> rollNumberJoin = claimRoot.join("claimLineDetail");
	         predicates.add(cb.equal(rollNumberJoin.get("rollNumber"),searchClaimHistoryRequest.getPrimaryValue()));
		}
		
		if (searchClaimHistoryRequest.getPrimarySearchType().equalsIgnoreCase("DYE") && !searchClaimHistoryRequest.getPrimaryValue().isEmpty()) {
			 Join<Claim,ClaimLineDetail> dyeLotJoin = claimRoot.join("claimLineDetail");
	         predicates.add(cb.equal(dyeLotJoin.get("dyeLot"),searchClaimHistoryRequest.getPrimaryValue()));
		}
		if (searchClaimHistoryRequest.getPrimarySearchType().equalsIgnoreCase("CON")) {
			SearchHistoryEndUserDTO endUserInfo = searchClaimHistoryRequest.getEndUserInformation();
			 if (endUserInfo != null) {
				    Join<Claim, EndUserInformation> endUserJoin = claimRoot.join("endUserInformation");
 
				    if (endUserInfo.getFirstName() != null && !endUserInfo.getFirstName().isEmpty()) {
				    	predicates.add(cb.like(endUserJoin.get("firstName"), "%" + endUserInfo.getFirstName() + "%"));
//				        predicates.add(cb.equal(endUserJoin.get("firstName"), endUserInfo.getFirstName()));
				    }
				    if (endUserInfo.getLastName() != null && !endUserInfo.getLastName().isEmpty()) {
				    	predicates.add(cb.like(endUserJoin.get("lastName"), "%" + endUserInfo.getLastName() + "%"));
//				        predicates.add(cb.equal(endUserJoin.get("lastName"), endUserInfo.getLastName()));
				    }
				    if (endUserInfo.getCompanyName() != null && !endUserInfo.getCompanyName().isEmpty()) {
				        predicates.add(cb.equal(endUserJoin.get("companyName"), endUserInfo.getCompanyName()));
				    }
				    if (endUserInfo.getAddressLine1() != null && !endUserInfo.getAddressLine1().isEmpty()) {
				        predicates.add(cb.equal(endUserJoin.get("addressLine1"), endUserInfo.getAddressLine1()));
				    }
				    if (endUserInfo.getAddressLine2() != null && !endUserInfo.getAddressLine2().isEmpty()) {
				        predicates.add(cb.equal(endUserJoin.get("addressLine2"), endUserInfo.getAddressLine2()));
				    }
				    if (endUserInfo.getPhoneNumber() != null && !endUserInfo.getPhoneNumber().isEmpty()) {
				        predicates.add(cb.equal(endUserJoin.get("businessPhoneNumber"), endUserInfo.getPhoneNumber()));
				    }
				}

		}
		if (searchClaimHistoryRequest.getPrimarySearchType().equalsIgnoreCase("PLT")
				&& !searchClaimHistoryRequest.getPrimaryValue().isEmpty()) {
							Join<Claim, ClaimLineDetail> plantNumberJoin = claimRoot.join("claimLineDetail");
							predicates.add(
									cb.equal(plantNumberJoin.get("manufacturingPlant"), searchClaimHistoryRequest.getPrimaryValue()));
						}
		if (searchClaimHistoryRequest.getPrimarySearchType().equalsIgnoreCase("DSP")
				&& !searchClaimHistoryRequest.getPrimaryValue().isEmpty()) {
				 
							Join<Claim, ClaimDocument> claimDocumentJoin = claimRoot.join("claimDocuments");
				 
							// Join ClaimDocument with DocumentType entity
							Join<ClaimDocument, DocumentType> documentTypeJoin = claimDocumentJoin.join("documentType");
				 
							// Add the condition to filter by documentTypeCode 'DSP'
							predicates.add(cb.equal(documentTypeJoin.get("documentTypeCode"), "DSP"));
				 
							// Add the existing condition to filter by documentNumber
							predicates.add(
									cb.equal(claimDocumentJoin.get("documentNumber"), searchClaimHistoryRequest.getPrimaryValue()));
						}
		
		secondarySearchCheck(searchClaimHistoryRequest, predicates, cb, claimRoot);   
		optionalSearchCheck(searchClaimHistoryRequest, predicates, cb, claimRoot);
//	}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		query.where(predicates.toArray(new Predicate[0]));
		log.info("query :: " + query);
		List<Claim> claimList = (List<Claim>) entityManager.createQuery(query).getResultList();
		searchHistoryList = getClaimsData(claimList);
		 log.info(" searchHistoryList()");
		//comparator for datesort->converting string into LocalDate
		 Comparator<SearchClaimHistoryResponseDTO> dateComparator = (dto1, dto2) -> {
	            LocalDate date1 = LocalDate.parse(dto1.getClaimDate(), formatter);
	            LocalDate date2 = LocalDate.parse(dto2.getClaimDate(), formatter);
	            return date2.compareTo(date1); // Reversed for descending order
		 };
		 
		// Sort the list using the custom comparator
	        searchHistoryList.sort(dateComparator);

		return searchHistoryList;
    }
	
	 private  List<Predicate> secondarySearchCheck(SearchClaimHistoryRequestDTO searchClaimHistoryRequest,
	    		List<Predicate> predicates,CriteriaBuilder cb,Root<Claim> claimRoot)
	    {
		 if (!searchClaimHistoryRequest.getSecondarySearchType().isEmpty()
					&& !searchClaimHistoryRequest.getSecondaryValue().isEmpty()) {
			 
			 	if(searchClaimHistoryRequest.getSecondarySearchType().equalsIgnoreCase("INV")) {
			 		Join<Claim, ClaimDocument> claimDocumentJoin = claimRoot.join("claimDocuments");
			 		predicates.add(cb.equal(claimDocumentJoin.get("documentNumber"),searchClaimHistoryRequest.getSecondaryValue()));
			 	}
			 	if (searchClaimHistoryRequest.getSecondarySearchType().equalsIgnoreCase("RET") && !searchClaimHistoryRequest.getSecondaryValue().isEmpty()) {
					
					 Join<Claim,ClaimRgaHeader> rgaNumberJoin = claimRoot.join("claimRgaHeader");
			         predicates.add(cb.equal(rgaNumberJoin.get("rgaNumber"),searchClaimHistoryRequest.getSecondaryValue()));
				}
				if (searchClaimHistoryRequest.getSecondarySearchType().equalsIgnoreCase("CRM") && !searchClaimHistoryRequest.getSecondaryValue().isEmpty()) {
					 Join<Claim,ClaimCrmHeader> crmNumberJoin = claimRoot.join("claimCrmHeader");
			         predicates.add(cb.equal(crmNumberJoin.get("crmNumber"),searchClaimHistoryRequest.getSecondaryValue()));
				}
				if (searchClaimHistoryRequest.getSecondarySearchType().equalsIgnoreCase("PON") && !searchClaimHistoryRequest.getSecondaryValue().isEmpty()) {
					 Join<Claim,ClaimDocument> claimRgaHeaderJoin = claimRoot.join("claimDocuments");
			         predicates.add(cb.equal(claimRgaHeaderJoin.get("purchaseOrderNumber"),searchClaimHistoryRequest.getSecondaryValue()));
				}
//				if (searchClaimHistoryRequest.getSecondarySearchType().equalsIgnoreCase("Serial Number") && !searchClaimHistoryRequest.getSecondaryValue().isEmpty()) {
//					 Join<Claim,ClaimDocument> claimRgaHeaderJoin = claimRoot.join("claimDocuments");
//			         predicates.add(cb.equal(claimRgaHeaderJoin.get("crmNumber"),searchClaimHistoryRequest.getSecondaryValue()));
//				}
//				if (searchClaimHistoryRequest.getSecondarySearchType().equalsIgnoreCase("STO") && !searchClaimHistoryRequest.getSecondaryValue().isEmpty()) {
//					 Join<Claim,ClaimDocument> claimRgaHeaderJoin = claimRoot.join("claimDocuments");
//			         predicates.add(cb.equal(claimRgaHeaderJoin.get("crmNumber"),searchClaimHistoryRequest.getSecondaryValue()));
//				}
				if (searchClaimHistoryRequest.getSecondarySearchType().equalsIgnoreCase("CUS") && !searchClaimHistoryRequest.getSecondaryValue().isEmpty()) {
					
					predicates.add(cb.equal(claimRoot.get("customerNumber"), searchClaimHistoryRequest.getSecondaryValue()));
				}
				if (searchClaimHistoryRequest.getSecondarySearchType().equalsIgnoreCase("RSN") && !searchClaimHistoryRequest.getSecondaryValue().isEmpty()) {
					Join<Claim,ClaimReasonDefinition> claimReasonCodeJoin = claimRoot.join("claimReasonDefinition");
			         predicates.add(cb.equal(claimReasonCodeJoin.get("claimReasonCode"),searchClaimHistoryRequest.getSecondaryValue()));
				
				}
				if (searchClaimHistoryRequest.getSecondarySearchType().equalsIgnoreCase("STY") && !searchClaimHistoryRequest.getSecondaryValue().isEmpty()) {
					 Join<Claim,ClaimLineDetail> styleNumberJoin = claimRoot.join("claimLineDetail");
			         predicates.add(cb.equal(styleNumberJoin.get("styleNumber"),searchClaimHistoryRequest.getSecondaryValue()));
				}
				if (searchClaimHistoryRequest.getSecondarySearchType().equalsIgnoreCase("CLR") && !searchClaimHistoryRequest.getSecondaryValue().isEmpty()) {
					 Join<Claim,ClaimLineDetail> styleNumberJoin = claimRoot.join("claimLineDetail");
			         predicates.add(cb.equal(styleNumberJoin.get("colorNumber"),searchClaimHistoryRequest.getSecondaryValue()));
				}
			 	
	    }
		 return predicates;
	    }
    
    
    private  List<Predicate> optionalSearchCheck(SearchClaimHistoryRequestDTO searchClaimHistoryRequest,
    		List<Predicate> predicates,CriteriaBuilder cb,Root<Claim> claimRoot)
    {
    	log.info("Enter optionalSearchCheck()");
    	
		if (!searchClaimHistoryRequest.getClaimInitiationFromDate().isEmpty()
		&& !searchClaimHistoryRequest.getClaimInitiationToDate().isEmpty()) {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			LocalDate fromDate = LocalDate.parse(searchClaimHistoryRequest.getClaimInitiationFromDate(), dtf);
			LocalDate toDate = LocalDate.parse(searchClaimHistoryRequest.getClaimInitiationToDate(), dtf);
			Predicate date = cb.between(claimRoot.get("createdDateTime"), fromDate, toDate);
			predicates.add(date);
		}
		if (!searchClaimHistoryRequest.getClaimAmountFrom().isEmpty()
			&& !searchClaimHistoryRequest.getClaimAmountTo().isEmpty()) {
			Predicate amount = cb.between(claimRoot.get("claimAmountUsd"),
					new BigDecimal(searchClaimHistoryRequest.getClaimAmountFrom()),
					new BigDecimal(searchClaimHistoryRequest.getClaimAmountTo()));
			predicates.add(amount);
		}

		if (searchClaimHistoryRequest.getClaimStatus() != null
			&& !searchClaimHistoryRequest.getClaimStatus().isEmpty()) {
			ClaimStatus claimStatus = claimStatusRepository.findClaimStatusByClaimStatusCode(searchClaimHistoryRequest.getClaimStatus());
			if(null!=claimStatus)
			{
				predicates.add(cb.equal(claimRoot.get("claimStatusId"), claimStatus.getClaimStatusId()));
			}
						
		}

		return predicates;
    }


	@Override
	public List<ClaimStatus> getClaimStatus() {
		log.info("Enter getClaimStatus()");
		return claimStatusRepository.findAll();

	}

	private List<SearchClaimHistoryResponseDTO> getClaimsData(List<Claim> claimList) {
		log.info("Enter getClaimsData()");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		List<SearchClaimHistoryResponseDTO> searchHistoryList = new ArrayList<>();
		for (Claim claimData : claimList) {
			SearchClaimHistoryResponseDTO searchHistoryDto = new SearchClaimHistoryResponseDTO();
			log.info("Enter start");
			searchHistoryDto.setClaimNbr(claimData.getClaimNumber());
			String status=claimStatusRepository.findById(claimData.getClaimStatusId()).get().getClaimStatusDescription();
			if(null!=status) {
			searchHistoryDto.setStatus(status);
			}
			searchHistoryDto.setClaimCategory(claimData.getClaimCategory().getClaimCategoryName());
			log.info("Enter firsthalf()");
			BigDecimal amount = claimData.getClaimAmountUsd() != null
				    ? claimData.getClaimAmountUsd().setScale(2, RoundingMode.HALF_UP): BigDecimal.ZERO;
				searchHistoryDto.setAmount(amount);
			searchHistoryDto.setTerritoryManager(claimData.getTerritoryManagerName());
			searchHistoryDto.setReasonCodeDescription(claimData.getClaimReasonDefinition().getClaimReasonDescription());
			searchHistoryDto.setCustomerNbr(claimData.getCustomerNumber()); 
			searchHistoryDto.setSellingCompany(claimData.getSellingCompany());
			
			
			log.info("Enter second half()");
			searchHistoryDto.setDivision(claimData.getDivision());
			
			searchHistoryDto.setRegion(claimData.getRegion());
			
			searchHistoryDto.setTerritory(claimData.getTerritory());
		
			if (claimData.getClaimReasonDefinition() != null) {
			    String claimReasonCode = "ALL".equalsIgnoreCase(claimData.getClaimReasonDefinition().getClaimReasonCode()) 
			                             ? " " 
			                             : claimData.getClaimReasonDefinition().getClaimReasonCode();
			    searchHistoryDto.setClaimReason(claimReasonCode);
			} else {
			    searchHistoryDto.setClaimReason("");
			}	
			
			
			String customerName = getCustomerName(claimData.getCustomerNumber());
			if (null!=customerName) {
				
			    searchHistoryDto.setCustomerName(customerName);
			} 
			
			LocalDateTime claimDate = claimData.getCreatedDateTime();
			if (null!=claimDate) {
				log.info("Enter  claimdate()");
				String formattedclaimDate = claimDate.format(formatter);
				searchHistoryDto.setClaimDate(formattedclaimDate);
			}

			LocalDateTime lastActivityDate = claimData.getModifiedDateTime();
			if (null!= lastActivityDate ) {
				log.info("Enter  lastdate()");
				String formattedlastActivityDate = lastActivityDate.format(formatter);
				searchHistoryDto.setLastActivityDate(formattedlastActivityDate);
			}
			
		  
			
			if (null != claimData.getClaimCrmHeader() && !claimData.getClaimCrmHeader().isEmpty()) {
			    log.info("Enter CRM Header");
 
			    // Set to store unique formatted CRM details
			    LinkedHashSet<String> crmDetails = new LinkedHashSet<>();
 
			    claimData.getClaimCrmHeader().forEach(crm -> {
			        // Convert int to string
			    	String crmNbr= crm.getCrmNumber()+"";
			        String crmNumber = crmNbr != null ? crmNbr.toString() : "";
			        String crmDate = crm.getIssuedDate() != null ? crm.getIssuedDate().format(formatter) : " ";
			        String crmAmount = crm.getAmountUsd() != null ? String.format("$%.2f", crm.getAmountUsd()) : "0.00";
			        String crmReasonCode = (crm.getClaimReasonDefinition() != null && 
			                                !crm.getClaimReasonDefinition().getClaimReasonCode().equalsIgnoreCase("ALL")) 
			                                ? crm.getClaimReasonDefinition().getClaimReasonCode() : " ";
 
			        // Combine the details into a single string separated by a comma
			        String crmDetail = String.join(",", crmNumber, crmDate, crmAmount, crmReasonCode,"\n");
			        crmDetails.add(crmDetail); // Adding to set ensures uniqueness
			    });
 
			    // Set the combined details in searchHistoryDto
			    searchHistoryDto.setCrmDetail(crmDetails);
			}		
			
			if(null!=claimData.getEndUserInformation()) {
				
				searchHistoryDto.setConsumerName(claimData.getEndUserInformation().getFirstName()+"  "+claimData.getEndUserInformation().getLastName());
				}
			
		
			if (null != claimData.getClaimRgaHeader() && !claimData.getClaimRgaHeader().isEmpty()) {
			    log.info("Enter RGA Header");
 
			    // Set to store unique formatted RGA details
			    LinkedHashSet<String> rgaDetails = new LinkedHashSet<>();
 
			    claimData.getClaimRgaHeader().forEach(rgaHeader -> {
			        // Convert int to string
			    	String rgaNbr=rgaHeader.getRgaNumber()+"";
			        String rgaNumber = rgaNbr != null ? rgaNbr.toString() : "";
			        String rgaDate = rgaHeader.getIssuedDate() != null ? rgaHeader.getIssuedDate().format(formatter) : "";
			        String rgaAmount = rgaHeader.getAmountUsd() != null ? String.format("$%.2f", rgaHeader.getAmountUsd()) : "0.00";
 
			        // Combine the details into a single string separated by a comma
			        String rgaDetail = String.join(",", rgaNumber, rgaDate, rgaAmount,"\n");
			        rgaDetails.add(rgaDetail); // Adding to set ensures uniqueness
			    });
			    // Set the combined details in searchHistoryDto
			    searchHistoryDto.setRgaDetail(rgaDetails);
			}			
			
			if(null!=claimData.getClaimAddresses()) {
				log.info("Enter Adress()");
				Optional<ClaimAddress> optionalAddress = claimData.getClaimAddresses().stream()
			            .filter(p -> p.getAddressTypeId() == 6)
			            .findAny();
				if(optionalAddress.isPresent()) {
					String stateCode=stateRepository.findStatesByIsoStateId(optionalAddress.get().getStateId()).getIsoStateCode();
					
					if(null!=stateCode) {
						searchHistoryDto.setState(stateCode);
					}
					
				}
	
				
			}

			if (null != claimData.getClaimDocuments() && !claimData.getClaimDocuments().isEmpty()) {
			    log.info("Enter Invoice()");
 
			    // Set to store unique formatted invoice details
			    LinkedHashSet<String> invoiceDetails = new LinkedHashSet<>();
 
			    claimData.getClaimDocuments().forEach(document -> {
			    	
			    	// Check if the document type is 'INV' before adding it to the invoiceDetails
			        if ("INV".equalsIgnoreCase(document.getDocumentType().getDocumentTypeCode())) {

			        String invoiceNumber = document.getDocumentNumber() != null ? document.getDocumentNumber() : "";
			        String invoiceDate = (document.getDocumentDate() != null) ? document.getDocumentDate().format(formatter) : "";
			        String invoiceAmount = document.getAmountUsd() != null ? String.format("$%.2f", document.getAmountUsd()) : "0.00";
 
			        // Combine the details into a single string
			        String invoiceDetail = String.join(",", invoiceNumber, invoiceDate, invoiceAmount,"\n"); // Using , as a delimiter
			        invoiceDetails.add(invoiceDetail); // Adding to set ensures uniqueness
			        }
			    });
 
			    // Set the combined details in searchHistoryDto
			    searchHistoryDto.setInvoiceDetail(invoiceDetails);
			}
			
		  			searchHistoryList.add(searchHistoryDto);

		}
		
		System.out.println(searchHistoryList);
		return searchHistoryList;

	}
	private String getCustomerName(String customerNumber) {
        String cusomerName = "";
        if (customerNumber != null && !customerNumber.isEmpty()) {
            ClaimAssignmentDataResponseDTO[] claimAssignmentDataResponse;   // = getClaimAssignmentDataResponse(customerNumber)
            try {
                claimAssignmentDataResponse = getClaimAssignmentDataResponse(customerNumber);
            }catch (Exception e) {
                log.error("An unexpected error occurred: {}", e.getMessage());
                claimAssignmentDataResponse = new ClaimAssignmentDataResponseDTO[0]; // Returning an empty array as a fallback
            }
            assert claimAssignmentDataResponse.length == 1;
//            cusomerName = claimAssignmentDataResponse[0].getCustomerName();
            if (claimAssignmentDataResponse != null && claimAssignmentDataResponse.length > 0) {
            	cusomerName = claimAssignmentDataResponse[0].getCustomerName();
            }
        } 
        return cusomerName;
    }
	private ClaimAssignmentDataResponseDTO[] getClaimAssignmentDataResponse(String customerNumber) {
        String url = "/customer/v1/getClaimAssignmentData?customerNumber=" + customerNumber;
        String finalUrl = restUrl + url;
        log.info("Final Url :: " + finalUrl);
        ClaimAssignmentDataResponseDTO[] claimAssignmentDataResponse = restTemplate.getForObject(finalUrl, ClaimAssignmentDataResponseDTO[].class);
        
        return claimAssignmentDataResponse;
    }


	@Override
	public List<Lookup> getPrimarySearch() {
		log.info("Enter getPrimarySearch()");
		
		return lookupRepository.primarySearchDropDown();
	}
	
	
	
	public List<SecondarySearchDTO> getSecondarySearch(String primarySearchType) {
	    log.info("Enter getSecondarySearch() with primarySearchType: {}", primarySearchType);
	    List<Lookup> secondarySearch = lookupRepository.secondarySearchDropDown();
	    List<SecondarySearchDTO> customizedSecondarySearch = new ArrayList<>();

	    // Define the mapping between lookup codes and primarySearchId
	    Map<String, List<Integer>> primarySearchIdMap = new HashMap<>();
        primarySearchIdMap.put("INV",  Arrays.asList(43,45)); // Invoice
        primarySearchIdMap.put("CCB", Arrays.asList(44)); // Debit
        primarySearchIdMap.put("DEB", Arrays.asList(43)); // Debit
        primarySearchIdMap.put("CRM",  Arrays.asList(43)); // Credit
        primarySearchIdMap.put("RET",  Arrays.asList(43)); // RGA
        primarySearchIdMap.put("PON", Arrays.asList(43)); // Purchase Order
        primarySearchIdMap.put("SER",  Arrays.asList(43)); // Serial No.
        primarySearchIdMap.put("STO",  Arrays.asList(43)); // Store No.
        primarySearchIdMap.put("CUS", Arrays.asList(69, 70, 71)); // Customer Number for REASON CODE and STYLE NO / INVENTORY STYLE
        primarySearchIdMap.put("RSN", Arrays.asList(70,71,74)); // Reason Code and Plant No.
        primarySearchIdMap.put("STY", Arrays.asList(74)); // Style No. (Inventory Style) and Plant No.
        primarySearchIdMap.put("CLR", Arrays.asList(70,71)); // Color No.

        for (Lookup lookup : secondarySearch) {
            List<Integer> lookupCodes = primarySearchIdMap.get(lookup.getLookupCode());
            //lookupcode will get all lookupcodes
           //here lookupCodes will get primarySearchId from above for each lookupCode
          
            if (lookupCodes != null) {
                for (Integer primarySearchId : lookupCodes) {
//                	System.out.println(lookupCodes+"--------------------123");
                    if (isPrimarySearchTypeMatch(primarySearchType, primarySearchId)) {
                        SecondarySearchDTO dto = createSecondarySearchDTO(lookup, primarySearchId);
                        customizedSecondarySearch.add(dto);
                    }
                }
            }
        }
        return customizedSecondarySearch;
        }
        
	
	private boolean isPrimarySearchTypeMatch(String primarySearchType, Integer primarySearchId) {

	            switch (primarySearchType.toUpperCase()) {

	                case "CUS":

	                    return primarySearchId == 43;

	                case "CRM":

	                    return primarySearchId == 44;

	                case "RET":

	                    return primarySearchId == 45;

	                case "RSN":

	                    return primarySearchId == 69;

	                case "STY":

	                    return primarySearchId == 70 ;

	                case "IVS": 

	                    return primarySearchId == 71;

	                case "PLT":

	                    return primarySearchId == 74;

	                default:

	                    return false;

	            }

		}
	 	private SecondarySearchDTO createSecondarySearchDTO(Lookup lookup, Integer primarySearchId) {
	    SecondarySearchDTO searchDto = new SecondarySearchDTO();
	    searchDto.setLookupId(lookup.getLookupId());
	    searchDto.setLookupCode(lookup.getLookupCode());
	    searchDto.setLookupDescription(lookup.getLookupDescription());
	    searchDto.setDisplaySequence(lookup.getDisplaySequence());
	    searchDto.setStatusId(lookup.getStatusId());
	    searchDto.setPrimarySearchId(primarySearchId);
	    return searchDto;
	}

	 	@Override
		public byte[] exportToExcel(SearchClaimHistoryRequestDTO searchClaimHistoryRequest) throws IOException {
			 List<SearchClaimHistoryResponseDTO> searchHistoryList = searchClaimHistory(searchClaimHistoryRequest);
	 
		        if (searchHistoryList == null || searchHistoryList.isEmpty()) {
		            return null;  // Handle empty data
		        }
	 
		        // Create the Excel workbook and sheet
		        ByteArrayOutputStream out = new ByteArrayOutputStream();
		        try (Workbook workbook = new XSSFWorkbook()) {
		            Sheet sheet = workbook.createSheet("Claim History");
	 
		            // Create header row
		            Row headerRow = sheet.createRow(0);
		            String[] headers = {
		                "Claim Number", "Status", "Amount", "Territory Manager", "Reason Code Description", "Customer Number",
		                "Selling Company", "Division", "Region", "Territory", "Claim Reason", "Customer Name",
		                "Claim Date", "Last Activity Date", "CRM Details", "Consumer Name", "RGA Details", "State", "Invoice Details"
		            };
	 
		            for (int i = 0; i < headers.length; i++) {
		                headerRow.createCell(i).setCellValue(headers[i]);
		            }
	 
		            // Populate the data rows
		            int rowIdx = 1;
		            for (SearchClaimHistoryResponseDTO dto : searchHistoryList) {
		                Row row = sheet.createRow(rowIdx++);
	 
		                row.createCell(0).setCellValue(dto.getClaimNbr() != null ? dto.getClaimNbr() : "");
		                row.createCell(1).setCellValue(dto.getStatus() != null ? dto.getStatus() : "");
		                row.createCell(2).setCellValue(dto.getAmount() != null ? dto.getAmount().toString() : BigDecimal.ZERO.toString());
		                row.createCell(3).setCellValue(dto.getTerritoryManager() != null ? dto.getTerritoryManager() : "");
		                row.createCell(4).setCellValue(dto.getReasonCodeDescription() != null ? dto.getReasonCodeDescription() : "");
		                row.createCell(5).setCellValue(dto.getCustomerNbr() != null ? dto.getCustomerNbr() : "");
		                row.createCell(6).setCellValue(dto.getSellingCompany() != null ? dto.getSellingCompany() : "");
		                row.createCell(7).setCellValue(dto.getDivision() != null ? dto.getDivision() : "");
		                row.createCell(8).setCellValue(dto.getRegion() != null ? dto.getRegion() : "");
		                row.createCell(9).setCellValue(dto.getTerritory() != null ? dto.getTerritory() : "");
		                row.createCell(10).setCellValue(dto.getClaimReason() != null ? dto.getClaimReason() : "");
		                row.createCell(11).setCellValue(dto.getCustomerName() != null ? dto.getCustomerName() : "");
		                row.createCell(12).setCellValue(dto.getClaimDate() != null ? dto.getClaimDate() : "");
		                row.createCell(13).setCellValue(dto.getLastActivityDate() != null ? dto.getLastActivityDate() : "");
		                row.createCell(14).setCellValue(dto.getCrmDetail() != null ? String.join("\n", dto.getCrmDetail()) : "");
		                row.createCell(15).setCellValue(dto.getConsumerName() != null ? dto.getConsumerName() : "");
		                row.createCell(16).setCellValue(dto.getRgaDetail() != null ? String.join("\n", dto.getRgaDetail()) : "");
		                row.createCell(17).setCellValue(dto.getState() != null ? dto.getState() : "");
		                row.createCell(18).setCellValue(dto.getInvoiceDetail() != null ? String.join("\n", dto.getInvoiceDetail()) : "");
		            }
	 
		            // Auto-size columns
		            for (int i = 0; i < headers.length; i++) {
		                sheet.autoSizeColumn(i);
		            }
	 
		            // Write the workbook to the output stream
		            workbook.write(out);
		        }
	 
		        // Return the Excel file as a byte array
		        return out.toByteArray();
		    }


}
