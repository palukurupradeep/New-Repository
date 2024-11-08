package com.shaw.claims.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.dto.ClaimAssignmentDataResponseDTO;
import com.shaw.claims.dto.ClaimLevelRulesDTO;
import com.shaw.claims.dto.SmartCodeMessageDTO;
import com.shaw.claims.enums.ReasonCodes;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.Claim;
import com.shaw.claims.model.ClaimDocument;
import com.shaw.claims.model.ClaimLineDetail;
import com.shaw.claims.model.CodeRules;
import com.shaw.claims.model.EndUserInformation;
import com.shaw.claims.model.SellingCompany;
import com.shaw.claims.repo.ClaimRepository;
import com.shaw.claims.repo.CodeRulesRepository;
import com.shaw.claims.repo.EndUserInformationRepository;
import com.shaw.claims.repo.SellingCompanyRepository;
import com.shaw.claims.util.ObjectMapperUtil;

@Service
public class ClaimLevelValidationServiceImpl  {
	Logger log = LogManager.getLogger(ClaimLevelValidationServiceImpl.class);
	@Autowired
	CodeRulesRepository codeRulesRepository;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	ObjectMapperUtil mapper;
	@Value("${claim-integration.rest.url}")
	private String restUrl;
	@Autowired
	EndUserInformationRepository endUserInformationRepository;
	@Autowired
    private ClaimRepository claimRepository;
	@Autowired
	SellingCompanyRepository sellingCompanyRepository;


	public List<SmartCodeMessageDTO> claimLevelValidations(ClaimLevelRulesDTO claimLevelRulesDTO) {
		List<SmartCodeMessageDTO> smartCodeMessageList=new ArrayList<>();
		//List<CodeRules> codeRules = codeRulesRepository.fetchCodeRuleByReasonId(claimLevelRulesDTO.getReasonCode());
		List<CodeRules> codeRules = null;
		for (CodeRules codeRule : codeRules) {
			SmartCodeMessageDTO smartCodeMessageDTO = switch (codeRule.getCodeRuleCategory().getCodeRuleCategoryCode()) {
			case CommonConstant.CUSTOMER_REF_NO: {
				yield validateCustRefRules(claimLevelRulesDTO,codeRule);
			}
			case CommonConstant.END_USER_INFO: {
				yield validateEndUserInfoRules(claimLevelRulesDTO,codeRule);
			}
			case CommonConstant.SELLING_CMPNY: {
				yield validateSellingCompanyRules(claimLevelRulesDTO,codeRule);
			}
			
			case CommonConstant.VENDOR_NAME: {
				yield validateVendorNameRules(claimLevelRulesDTO,codeRule);
			}
			case CommonConstant.PRODUCT_CODE: {
				yield validateProductCodeRules(claimLevelRulesDTO,codeRule);
			}
			
			default:
				throw new CommonException(String.valueOf(HttpStatus.OK));

			};
			smartCodeMessageList.add(smartCodeMessageDTO);
		}
		
		return smartCodeMessageList;
	}
	
	private SmartCodeMessageDTO validateEndUserInfoRules(ClaimLevelRulesDTO claimLevelRulesDTO,CodeRules codeRules)
	{
		List<EndUserInformation> endUserinfoList = endUserInformationRepository
				.findByClaimId(claimLevelRulesDTO.getClaimId());
		
		if (endUserinfoList == null || endUserinfoList.isEmpty())
			 return getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),CommonConstant.END_USER_INFO);
		else
			return getSmartCodeMessageDTO(CommonConstant.END_USER_INFO,codeRules.getSuccessMessage());

	}
	
	private SmartCodeMessageDTO validateVendorNameRules(ClaimLevelRulesDTO claimLevelRulesDTO,CodeRules codeRules)
	{
		String vendorName="";
		Optional<Claim> claimInfo = claimRepository.findById(claimLevelRulesDTO.getClaimId());
		outerScope:for (ClaimDocument doc : claimInfo.get().getClaimDocuments()) {
			for (ClaimLineDetail lineDetails : doc.getClaimLineDetails()) {
				if(!lineDetails.getVendorId().isEmpty())
				 vendorName=lineDetails.getVendorId();
				 break outerScope;
				   
			}
		}
		
		SmartCodeMessageDTO smartCodeMessageDTO= switch (ReasonCodes.valueOf(claimLevelRulesDTO.getReasonCode())) {
		case D01,D02,D03,D04,D05,D06,D07,D08,D09,D11,D12,D15,D16,D17,
		D18,D19,D20,D21,D22,D24,D26,D27,D29,D32,D35,D36,D37,D41,D42,
		D43,D45,D46,D47,D48,D49,D53,D54,D55,D57,D59,D60,D61,D62,D66,
		D68,D71,D73,D74,D77,D78,D79,D81,D87,D89,D90,D91,D92,D93,D94,
		D96,D97,D98,D99,S00:{
			if(vendorName==null || vendorName.isEmpty() || !vendorName.equalsIgnoreCase(CommonConstant.VENDOR_NAME_SHAW))
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),CommonConstant.VENDOR_NAME);
			else
			 yield getSmartCodeMessageDTO(CommonConstant.VENDOR_NAME_SHAW,codeRules.getSuccessMessage());
			
		}
		case T01,T02,T03,T04,T05,T06,T10,T11,T12,T15,T16,T17,T18,T19,
		T27,T28,T30,T31,T32,T33,T34,T35,T36,T37,T38,T39,T51,T53,T54,
		T55,T56,T60,T61,T62,T65,T66,T67,T68,T69,T77,T78,T80,T81,T82,
		T83,T84,T85,T86,T87,T88,T89
		:{
			if(vendorName!=null && vendorName.equalsIgnoreCase(CommonConstant.VENDOR_NAME_SHAW))
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),CommonConstant.VENDOR_NAME);
			else
			 yield getSmartCodeMessageDTO(CommonConstant.VENDOR_NAME_SHAW,codeRules.getSuccessMessage());
			
		}
		
		default : throw new CommonException(String.valueOf(HttpStatus.OK));
		};
		
		return smartCodeMessageDTO;
	}
	
	private SmartCodeMessageDTO validateProductCodeRules(ClaimLevelRulesDTO claimLevelRulesDTO,CodeRules codeRules)
	{
		String productCode="";
		Optional<Claim> claimInfo = claimRepository.findById(claimLevelRulesDTO.getClaimId());
		outerScope:for (ClaimDocument doc : claimInfo.get().getClaimDocuments()) {
			for (ClaimLineDetail lineDetails : doc.getClaimLineDetails()) {
				if(!lineDetails.getProductCode().isEmpty())
				 productCode=lineDetails.getProductCode();
				 break outerScope;
				   
			}
		}
		String[] arrOfProductCodes=codeRules.getRuleDescription().split(",",-2);
		List<String> productCodeList=Arrays.asList(arrOfProductCodes);
	
		SmartCodeMessageDTO smartCodeMessageDTO= switch (ReasonCodes.valueOf(claimLevelRulesDTO.getReasonCode())) {
		case D06,D15:{
			if(productCodeList.contains(productCode))
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),CommonConstant.PRODUCT_CODE);
			else
				
			 yield getSmartCodeMessageDTO(CommonConstant.PRODUCT_CODE,codeRules.getSuccessMessage());
			
		}
		default : throw new CommonException(String.valueOf(HttpStatus.OK));
		};
		
		return smartCodeMessageDTO;
	}
	
	private SmartCodeMessageDTO validateSellingCompanyRules(ClaimLevelRulesDTO claimLevelRulesDTO,CodeRules codeRules)
	{
		Optional<Claim> claimInfo = claimRepository.findById(claimLevelRulesDTO.getClaimId());
		
		if(claimInfo.get().getSellingCompany().isEmpty())
			throw new CommonException(String.valueOf(HttpStatus.OK));
		Optional<SellingCompany> sellingCompany=sellingCompanyRepository.findSellingCompnayBySellingCompanyCode(claimInfo.get().getSellingCompany());
		SmartCodeMessageDTO smartCodeMessageDTO= switch (ReasonCodes.valueOf(claimLevelRulesDTO.getReasonCode())) {
		case A98,A99:{
			if(sellingCompany.get().getHardSurface()==false)
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),CommonConstant.SELLING_CMPNY);
			else
			 yield getSmartCodeMessageDTO(CommonConstant.SELLING_CMPNY,codeRules.getSuccessMessage());
			
		}
		case D56:{
			if(sellingCompany.get().getResilient()==false)
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),CommonConstant.SELLING_CMPNY);
			else
			 yield getSmartCodeMessageDTO(CommonConstant.SELLING_CMPNY,codeRules.getSuccessMessage());
			
		}
		case Y13,Y14,Y39,Y40,Y42,Y43,Y56:{
			if(sellingCompany.get().getYarn()==false)
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),CommonConstant.SELLING_CMPNY);
			else
			 yield getSmartCodeMessageDTO(CommonConstant.SELLING_CMPNY,codeRules.getSuccessMessage());
			
		}
		default : throw new CommonException(String.valueOf(HttpStatus.OK));
		};
		
		return smartCodeMessageDTO;
		}
	private SmartCodeMessageDTO validateCustRefRules(ClaimLevelRulesDTO claimLevelRulesDTO,CodeRules codeRules)
	{
		String cusRef="";
		List<String>  codeRuleDataList = codeRules.getCodeRuleDataList().stream().map(x -> x.getDataListValue())
					.collect(Collectors.toList());
			
		 ClaimAssignmentDataResponseDTO[] claimAssignmentDataResponse = getClaimAssignmentDataResponse(
						claimLevelRulesDTO.getCustomerNumber());
				assert claimAssignmentDataResponse.length == 1;
				cusRef = claimAssignmentDataResponse[0].getCustomerReferenceNumber();
		SmartCodeMessageDTO smartCodeMessageDTO= switch (ReasonCodes.valueOf(claimLevelRulesDTO.getReasonCode())) {
		case A09:{
			
			 if (codeRuleDataList!=null && codeRuleDataList.contains(cusRef))
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),CommonConstant.CUSTOMER_REF_NO);
			else
				yield getSmartCodeMessageDTO(CommonConstant.CUSTOMER_REF_NO,codeRules.getSuccessMessage());
			}
		
		case A25,A26,A27,W18 :{
			if (codeRuleDataList!=null && !codeRuleDataList.contains(cusRef))
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),CommonConstant.CUSTOMER_REF_NO);
			else
				yield getSmartCodeMessageDTO(CommonConstant.CUSTOMER_REF_NO,codeRules.getSuccessMessage());
		}
		
		default : throw new CommonException(String.valueOf(HttpStatus.OK));

		};
		return smartCodeMessageDTO;
	}
	
	private SmartCodeMessageDTO getSmartCodeMessageDTO(String errorMessage,boolean override,String ruleCategory)
	{

		SmartCodeMessageDTO smartCodeMessageDTO = new SmartCodeMessageDTO();
		smartCodeMessageDTO.setErrorMessage(errorMessage);
		smartCodeMessageDTO.setRuleCategory(ruleCategory);
		smartCodeMessageDTO.setOverride(override);

		return smartCodeMessageDTO;
    	    }
	private SmartCodeMessageDTO getSmartCodeMessageDTO(String ruleCategory,String successMessage)
	{

		SmartCodeMessageDTO smartCodeMessageDTO = new SmartCodeMessageDTO();
		smartCodeMessageDTO.setSuccessMessage(successMessage);
		smartCodeMessageDTO.setRuleCategory(ruleCategory);
		return smartCodeMessageDTO;
    	    }
	
	private ClaimAssignmentDataResponseDTO[] getClaimAssignmentDataResponse(String customerNumber) {
        String url = "/customer/v1/getClaimAssignmentData?customerNumber=" + customerNumber;
        String finalUrl = restUrl + url;
        log.info("Final Url :: " + finalUrl);
        ClaimAssignmentDataResponseDTO[] claimAssignmentDataResponse = restTemplate.getForObject(finalUrl, ClaimAssignmentDataResponseDTO[].class);
        return claimAssignmentDataResponse;
    }

}
