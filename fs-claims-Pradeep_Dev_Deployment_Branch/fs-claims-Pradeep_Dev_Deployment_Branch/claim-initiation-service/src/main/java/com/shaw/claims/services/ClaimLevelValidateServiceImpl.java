package com.shaw.claims.services;

import java.util.ArrayList;
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
import com.shaw.claims.dto.ClaimDTO;
import com.shaw.claims.dto.ClaimLevelRulesDTO;
import com.shaw.claims.dto.ClaimLineDetailDTO;
import com.shaw.claims.dto.LineDetailsRequest;
import com.shaw.claims.dto.SmartCodeMessageDTO;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.Claim;
import com.shaw.claims.model.ClaimAddress;
import com.shaw.claims.model.ClaimLineDetail;
import com.shaw.claims.model.ClaimReasonDefinition;
import com.shaw.claims.model.ClaimRgaHeader;
import com.shaw.claims.model.CodeRuleType;
import com.shaw.claims.model.CodeRules;
import com.shaw.claims.model.EndUserInformation;
import com.shaw.claims.model.SellingCompany;
import com.shaw.claims.model.State;
import com.shaw.claims.repo.ClaimLineDetailsRepository;
import com.shaw.claims.repo.ClaimReasonDefinitionRepository;
import com.shaw.claims.repo.ClaimRepository;
import com.shaw.claims.repo.ClaimRgaHeaderRepository;
import com.shaw.claims.repo.CodeRuleCategoryRepository;
import com.shaw.claims.repo.CodeRuleTypeRepository;
import com.shaw.claims.repo.CodeRulesRepository;
import com.shaw.claims.repo.EndUserInformationRepository;
import com.shaw.claims.repo.SellingCompanyRepository;
import com.shaw.claims.repo.StateRepository;
import com.shaw.claims.util.ObjectMapperUtil;

@Service
public class ClaimLevelValidateServiceImpl {
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
	@Autowired
	CodeRuleCategoryRepository codeRuleCategoryRepository;
	@Autowired
	CodeRuleTypeRepository codeRuleTypeRepository;
	@Autowired
	ClaimLineDetailsRepository claimLineDetailsRepository;
	@Autowired
	ClaimRgaHeaderRepository claimRgaHeaderRepository;
	@Autowired
	StateRepository stateRepository;
	@Autowired
	ClaimReasonDefinitionRepository claimReasonDefinitionRepository;

	public List<SmartCodeMessageDTO> claimLevelValidations(ClaimLevelRulesDTO smartCodeRulesDTO) throws Exception {
		List<SmartCodeMessageDTO> smartCodeMessageList = new ArrayList<>();
		SmartCodeMessageDTO smartCodeMessageDTO = new SmartCodeMessageDTO();
		List<CodeRules> codeRules = codeRulesRepository
				.findByClaimReasonDefinition_ClaimReasonCode(smartCodeRulesDTO.getReasonCode());
		if (codeRules != null && codeRules.size() > 0) {
			for (CodeRules codeRule : codeRules) {
				String ruleImpactArea = "";
				if (smartCodeRulesDTO.getRuleOfImpactArea() != null
						&& !smartCodeRulesDTO.getRuleOfImpactArea().isEmpty()) {
					ruleImpactArea = smartCodeRulesDTO.getRuleOfImpactArea();
				} else {
					ruleImpactArea = codeRule.getCodeRuleImpactArea().getCodeRuleImpactAreaCode();
				}

				if (smartCodeRulesDTO.getUpdateReasonCodeLineItem().equals(true)) {
					smartCodeMessageDTO = updateLineItemByClaim(smartCodeRulesDTO, ruleImpactArea, codeRule);
				} else {
					smartCodeMessageDTO = validateCodeByRuleImpactArea(smartCodeRulesDTO, ruleImpactArea, codeRule);
				}
				smartCodeMessageList.add(smartCodeMessageDTO);
			}
		} else {
			Claim claim = claimRepository.findByClaimId(smartCodeRulesDTO.getClaimId());
			if (claim != null) {
				claim.setClaimReasonDefinition(validateClaimReasonDefinition(smartCodeRulesDTO.getReasonCode()));
				claim = claimRepository.save(claim);
				smartCodeMessageDTO.setUpdatedReasonCode(claim.getClaimReasonDefinition().getClaimReasonCode());
				smartCodeMessageDTO.setClaimId(claim.getClaimId());
				smartCodeMessageList.add(smartCodeMessageDTO);
			}

		}

		return smartCodeMessageList;
	}

	private ClaimReasonDefinition validateClaimReasonDefinition(String reasonCode) {
		ClaimReasonDefinition claimReasonDefinition = claimReasonDefinitionRepository.findByClaimReasonCode(reasonCode);
		if (claimReasonDefinition != null) {
			return claimReasonDefinition;
		}
		return null;
	}

	private SmartCodeMessageDTO validateCodeByRuleImpactArea(ClaimLevelRulesDTO smartCodeRulesDTO,
			String ruleImpactArea, CodeRules codeRule) throws Exception {
		SmartCodeMessageDTO smartCodeMessageDTO = switch (ruleImpactArea) {
		case "Add/Edit Claim Reason Code": {
			log.info("enter Add/Edit Claim Reason Code..");
			yield validateRuleCategory(smartCodeRulesDTO, codeRule);

		}
		case "Add/Edit Line Item": {
			log.info("Add/Edit Line Item..");
			yield validateRuleCategory(smartCodeRulesDTO, codeRule);
		}

		case "Issue Credit Memo": {
			log.info("Enter issue credit memo..");
			yield validateRuleCategory(smartCodeRulesDTO, codeRule);
		}

		case "View Claim": {
			log.info("Enter View Claim..");
			yield validateRuleCategory(smartCodeRulesDTO, codeRule);
		}

		case "Claim Initiation": {
			log.info("Enter Claim Initiation..");
			yield validateRuleCategory(smartCodeRulesDTO, codeRule);
		}

		case "Claim Assignment": {
			log.info("Enter Claim Assignment..");
			yield validateRuleCategory(smartCodeRulesDTO, codeRule);
		}

		case "Claim Details": {
			log.info("Enter Claim Details..");
			yield validateRuleCategory(smartCodeRulesDTO, codeRule);
		}

		default:
			throw new CommonException(String.valueOf(HttpStatus.OK));

		};
		return smartCodeMessageDTO;
	}

	private SmartCodeMessageDTO updateLineItemByClaim(ClaimLevelRulesDTO smartCodeRulesDTO, String ruleImpactArea,
			CodeRules codeRule) throws Exception {
		Claim claim = claimRepository.findByClaimId(smartCodeRulesDTO.getClaimId());
		String reasonCode = smartCodeRulesDTO.getReasonCode();
		SmartCodeMessageDTO updatedLineItemResponse = null;
		if (claim != null) {
			if (claim.getPriorityClaim().equals(false)) {
				List<ClaimLineDetail> claimLineDetailsList = claimLineDetailsRepository
						.findByClaim_ClaimId(smartCodeRulesDTO.getClaimId());
				SmartCodeMessageDTO smartCodeMessageDTO = null;
				for (ClaimLineDetail claimLineDetail : claimLineDetailsList) {
					List<CodeRules> codeRulesList = codeRulesRepository.findByClaimReasonDefinition_ClaimReasonCode(
							claimLineDetail.getClaimReasonDefinition().getClaimReasonCode());
					try {
						for (CodeRules codeRules : codeRulesList) {
							smartCodeRulesDTO
									.setReasonCode(claimLineDetail.getClaimReasonDefinition().getClaimReasonCode());
							smartCodeMessageDTO = validateCodeByRuleImpactArea(smartCodeRulesDTO, ruleImpactArea,
									codeRules);
							if (smartCodeMessageDTO != null && smartCodeMessageDTO.getErrorMessage() != null
									&& !smartCodeMessageDTO.getErrorMessage().isEmpty()) {
								return smartCodeMessageDTO;
							}
						}
					} catch (Exception e) {
						log.info("smart code validation failed :: ", e.getMessage());
					}

					claimLineDetail.setClaimReasonDefinition(validateClaimReasonDefinition(reasonCode));
					claimLineDetail = claimLineDetailsRepository.save(claimLineDetail);

					if (claimLineDetail == null) {
						smartCodeMessageDTO = getSmartCodeMessageDTO(codeRule.getFailureMessage(),
								codeRule.isAllowOverride(), CommonConstant.CLAIM_LEVEL, null, null);
					} else {
						smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRule.isAllowOverride(),
								CommonConstant.CLAIM_LEVEL, reasonCode, codeRule.getSuccessMessage());
					}
				}
			}
		}
		return updatedLineItemResponse;
	}

	private SmartCodeMessageDTO validateRuleCategory(ClaimLevelRulesDTO smartCodeRulesDTO, CodeRules codeRule)
			throws Exception {

		SmartCodeMessageDTO smartCodeMessageDTO = switch (codeRule.getCodeRuleCategory().getCodeRuleCategoryCode()) {
		case "Country Code": {
			log.info("enter country code");
			yield validateCountryCodeRules(smartCodeRulesDTO, codeRule);

		}

		case "Customer Ref No": {
			log.info("enter cust ref");
			yield validateCustRefRules(smartCodeRulesDTO, codeRule);

		}

		case "End User Information": {
			log.info("enter end user info");
			yield validateEndUserInfoRules(smartCodeRulesDTO, codeRule);

		}

		case "Vendor Name/ID": {
			yield validateVendorNameRules(smartCodeRulesDTO, codeRule);
		}

		case "Pricing Code": {
			yield null;

		}

		case "Product Code": {
			yield null;

		}
		case "Freight": {
			yield validateFreightRules(smartCodeRulesDTO, codeRule);

		}

		case "Province": {
			yield validateCountryCodeRules(smartCodeRulesDTO, codeRule);

		}

		case "RCS Code": {
			log.info("RCS Code");
			yield validateRCSCodeRules(smartCodeRulesDTO, codeRule);

		}

		case "Reason Code": {
			yield validateReasonCode(smartCodeRulesDTO, codeRule);

		}

		case "RGA": {
			log.info("RGA");
			yield validateRGARules(smartCodeRulesDTO, codeRule);

		}

		case "Selling Company": {
			log.info("Selling Company");
			yield validateSellingCompanyRules(smartCodeRulesDTO, codeRule);

		}

		case "State": {
			yield validateStateCodeRules(smartCodeRulesDTO, codeRule);
		}

		case "Vendor Name": {
			log.info("Vendor Name");
			yield validateVendorNameRules(smartCodeRulesDTO, codeRule);

		}
		default:
			throw new CommonException(String.valueOf(HttpStatus.OK));

		};
		return smartCodeMessageDTO;
	}

	private SmartCodeMessageDTO validateStateCodeRules(ClaimLevelRulesDTO smartCodeRulesDTO, CodeRules codeRule) {
		int claimId = getClaimIdByLineDetails(smartCodeRulesDTO);

		Claim claim = claimRepository.findById(claimId).get();
		SmartCodeMessageDTO smartCodeMessageDTO;

		State state = stateRepository
				.findByIsoStateCode(codeRule.getClaimReasonDefinition().getClaimReasonCode().substring(1));
		ClaimAddress claimAddressStateValidation = claim.getClaimAddresses().stream()
				.filter(data -> (data.getAddressTypeId() == 2 && data.getStateId() == state.getIsoStateId())).findAny()
				.orElseThrow(() -> new CommonException(
						"Statecode not found for claim ID: " + smartCodeRulesDTO.getClaimId()));
		if (claimAddressStateValidation != null) {
			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRule.getFailureMessage(), codeRule.isAllowOverride(),
					CommonConstant.STATE, null, null);
		} else {
			smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRule.isAllowOverride(), CommonConstant.STATE, null,
					codeRule.getSuccessMessage());
		}

		return smartCodeMessageDTO;

	}

	private SmartCodeMessageDTO validateReasonCode(ClaimLevelRulesDTO smartCodeRulesDTO, CodeRules codeRules) {
		SmartCodeMessageDTO smartCodeMessageDTO = null;
		int claimId = getClaimIdByLineDetails(smartCodeRulesDTO);
		List<ClaimRgaHeader> claimRgaHeaders = claimRgaHeaderRepository.findByClaimIdAndRgaStatusId(claimId);
		if ((claimRgaHeaders == null) || (claimRgaHeaders.isEmpty())
				&& (!codeRules.getClaimReasonDefinition().getClaimReasonCode().startsWith("A")
						|| !codeRules.getClaimReasonDefinition().getClaimReasonCode().startsWith("T")
						|| !codeRules.getClaimReasonDefinition().getClaimReasonCode().startsWith("B04"))) {

//				smartCodeMessageDTO =  getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),
//				
//						CommonConstant.REASON_CODE, null, null);
			// remove else if hardcode
			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRules.getFailureMessage(), true,
					CommonConstant.REASON_CODE, null, null);

		} else {
			ClaimRgaHeader claimHeader = null;

			for (ClaimRgaHeader claimRgaHeader : claimRgaHeaders) {
				claimRgaHeader.getClaimReasonDefinition().setClaimReasonCode("F23");
				claimHeader = claimRgaHeaderRepository.save(claimRgaHeader);
			}

			smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRules.isAllowOverride(), CommonConstant.REASON_CODE,
					claimHeader.getClaimReasonDefinition().getClaimReasonCode(), codeRules.getSuccessMessage());

		}
		if (!codeRules.getClaimReasonDefinition().getClaimReasonCode().startsWith("D")) {

			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRules.getFailureMessage(), true,
					CommonConstant.REASON_CODE, null, null);
		} else {

			ClaimRgaHeader claimHeader = null;
			for (ClaimRgaHeader claimRgaHeader : claimRgaHeaders) {
				claimRgaHeader.getClaimReasonDefinition().setClaimReasonCode("D40");
				claimHeader = claimRgaHeaderRepository.save(claimRgaHeader);
			}

			smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRules.isAllowOverride(), CommonConstant.REASON_CODE,
					claimHeader.getClaimReasonDefinition().getClaimReasonCode(), codeRules.getSuccessMessage());
		}

		return smartCodeMessageDTO;
	}

	private SmartCodeMessageDTO validateVendorNameRules(ClaimLevelRulesDTO smartCodeRulesDTO, CodeRules codeRule) {
		int claimId = getClaimIdByLineDetails(smartCodeRulesDTO);
		List<ClaimLineDetail> claimLineDetails = claimLineDetailsRepository.findByClaim_ClaimId(claimId);
		if (claimLineDetails == null)
			throw new CommonException(
					"There is no claim line details not found for this claim #" + smartCodeRulesDTO.getClaimId());
		SmartCodeMessageDTO smartCodeMessageDTO;

		boolean nonShawVendorPresent = claimLineDetails.stream()
				.anyMatch(data -> !data.getVendorId().equalsIgnoreCase(CommonConstant.VENDOR_NAME_SHAW));

		if (nonShawVendorPresent) {
			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRule.getFailureMessage(), codeRule.isAllowOverride(),
					CommonConstant.VENDOR_NAME, null, null);
		} else {
			smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRule.isAllowOverride(), CommonConstant.VENDOR_NAME,
					null, codeRule.getSuccessMessage());
		}

		boolean shawVendorPresent = claimLineDetails.stream()
				.anyMatch(data -> data.getVendorId().equalsIgnoreCase(CommonConstant.VENDOR_NAME_SHAW));

		if (shawVendorPresent) {
			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRule.getFailureMessage(), codeRule.isAllowOverride(),
					CommonConstant.VENDOR_NAME, null, null);
		} else {
			smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRule.isAllowOverride(), CommonConstant.VENDOR_NAME,
					null, codeRule.getSuccessMessage());
		}

		return smartCodeMessageDTO;
	}

	private SmartCodeMessageDTO validateSellingCompanyRules(ClaimLevelRulesDTO smartCodeRulesDTO, CodeRules codeRule)
			throws Exception {
		int claimId = getClaimIdByLineDetails(smartCodeRulesDTO);
		Optional<Claim> claimDetails = claimRepository.findById(claimId);
		Claim claim = null;
		if (claimDetails.isPresent()) {
			claim = claimDetails.get();
		}
		SmartCodeMessageDTO smartCodeMessageDTO;
		SellingCompany sellingCompany = sellingCompanyRepository.findBySellingCompanyCode(claim.getSellingCompany());
		if (sellingCompany == null)
			throw new CommonException("Selling company not found for this claim #" + smartCodeRulesDTO.getClaimId());

		if (!sellingCompany.getResilient()) {
			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRule.getFailureMessage(), codeRule.isAllowOverride(),
					CommonConstant.SELLING_CMPNY, null, null);
		} else {
			smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRule.isAllowOverride(), CommonConstant.SELLING_CMPNY,
					null, codeRule.getSuccessMessage());
		}

		if (!sellingCompany.getHardSurface()) {
			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRule.getFailureMessage(), codeRule.isAllowOverride(),
					CommonConstant.SELLING_CMPNY, null, null);
		} else {
			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRule.getFailureMessage(), codeRule.isAllowOverride(),
					CommonConstant.SELLING_CMPNY, null, codeRule.getSuccessMessage());
		}

		if (!sellingCompany.getSellingCompanyCode().equalsIgnoreCase("68")
				|| !sellingCompany.getSellingCompanyCode().equalsIgnoreCase("70")) {
			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRule.getFailureMessage(), codeRule.isAllowOverride(),
					CommonConstant.SELLING_CMPNY, null, null);
		} else {
			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRule.getFailureMessage(), codeRule.isAllowOverride(),
					CommonConstant.SELLING_CMPNY, null, codeRule.getSuccessMessage());
		}

		if (!sellingCompany.getSellingCompanyCode().equalsIgnoreCase("06")
				|| !sellingCompany.getSellingCompanyCode().equalsIgnoreCase("75")) {
			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRule.getFailureMessage(), codeRule.isAllowOverride(),
					CommonConstant.SELLING_CMPNY, null, null);
		} else {
			smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRule.isAllowOverride(), CommonConstant.SELLING_CMPNY,
					null, codeRule.getSuccessMessage());
		}

		if (!sellingCompany.getSellingCompanyCode().equalsIgnoreCase("50")
				|| !sellingCompany.getSellingCompanyCode().equalsIgnoreCase("04")
				|| !sellingCompany.getSellingCompanyCode().equalsIgnoreCase("05")) {
			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRule.getFailureMessage(), codeRule.isAllowOverride(),
					CommonConstant.SELLING_CMPNY, null, null);
		} else {
			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRule.getFailureMessage(), codeRule.isAllowOverride(),
					CommonConstant.SELLING_CMPNY, null, codeRule.getSuccessMessage());
		}

		return smartCodeMessageDTO;
	}

	private SmartCodeMessageDTO validateRGARules(ClaimLevelRulesDTO smartCodeRulesDTO, CodeRules codeRule) {
		int claimId = getClaimIdByLineDetails(smartCodeRulesDTO);

		List<ClaimRgaHeader> claimRgaHeaders = claimRgaHeaderRepository.findByClaimIdAndRgaStatusId(claimId);
		SmartCodeMessageDTO smartCodeMessageDTO;
		if (claimRgaHeaders == null) {
			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRule.getFailureMessage(), codeRule.isAllowOverride(),
					CommonConstant.RGA, null, null);
		} else {
			smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRule.isAllowOverride(), CommonConstant.RGA, null,
					codeRule.getSuccessMessage());
		}

		if (claimRgaHeaders != null) {
			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRule.getFailureMessage(), codeRule.isAllowOverride(),
					CommonConstant.RGA, null, null);

		} else {
			smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRule.isAllowOverride(), CommonConstant.RGA, null,
					codeRule.getSuccessMessage());
		}

		return smartCodeMessageDTO;
	}

	private int getClaimIdByLineDetails(ClaimLevelRulesDTO smartCodeRulesDTO) {
		int claimId = 0;

		for (LineDetailsRequest data : smartCodeRulesDTO.getLineDetails()) {
			if (data.getLineId() != 0) {
				ClaimLineDetail claimLineDetails = claimLineDetailsRepository.findByClaimLineId(claimId);
				if (claimLineDetails != null) {
					claimId = claimLineDetails.getClaimLineId();
				} else {
					claimId = smartCodeRulesDTO.getClaimId();
				}
			}
		}
		return claimId;

	}

	private SmartCodeMessageDTO validateRCSCodeRules(ClaimLevelRulesDTO smartCodeRulesDTO, CodeRules codeRule) {
		int claimId = getClaimIdByLineDetails(smartCodeRulesDTO);
		List<ClaimLineDetail> claimLineDetails = claimLineDetailsRepository.findByClaim_ClaimId(claimId);

		boolean nonRCSCodePresent = claimLineDetails.stream()
				.anyMatch(data -> !data.getRcsCodes().getRcsCode().equalsIgnoreCase("S"));
		SmartCodeMessageDTO smartCodeMessageDTO;
		if (nonRCSCodePresent) {
			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRule.getFailureMessage(), codeRule.isAllowOverride(),
					CommonConstant.RCS_CODE, null, null);
		} else {
			smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRule.isAllowOverride(), CommonConstant.RCS_CODE,
					null, codeRule.getSuccessMessage());
		}

		boolean RCSCodePresent = claimLineDetails.stream()
				.anyMatch(data -> data.getRcsCodes().getRcsCode().equalsIgnoreCase("S"));

		if (RCSCodePresent) {
			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRule.getFailureMessage(), codeRule.isAllowOverride(),
					CommonConstant.RCS_CODE, null, null);
		} else {

			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRule.getFailureMessage(), codeRule.isAllowOverride(),
					CommonConstant.RCS_CODE, null, codeRule.getSuccessMessage());
		}

		return smartCodeMessageDTO;
	}

	private SmartCodeMessageDTO validateCountryCodeRules(ClaimLevelRulesDTO smartCodeRulesDTO, CodeRules codeRule)
			throws Exception {

		int claimId = getClaimIdByLineDetails(smartCodeRulesDTO);

		Claim claim = claimRepository.findById(claimId).get();
		SmartCodeMessageDTO smartCodeMessageDTO;

		Optional<ClaimAddress> claimAddress = claim.getClaimAddresses().stream()
				.filter(data -> (data.getAddressTypeId() == 8 && (data.getCountryId() == CommonConstant.CANADA))
						|| data.getCountryId() == CommonConstant.UNITED_KINGDOM)
				.findAny();
		if (claimAddress.isEmpty()) {
			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRule.getFailureMessage(), codeRule.isAllowOverride(),
					CommonConstant.COUNTRY_CODE, null, null);
		} else {
			smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRule.isAllowOverride(), CommonConstant.COUNTRY_CODE,
					null, codeRule.getSuccessMessage());
		}

		return smartCodeMessageDTO;
	}

	private SmartCodeMessageDTO validateCustRefRules(ClaimLevelRulesDTO smartCodeRulesDTO, CodeRules codeRules) {
		String cusRef = "";
		String alignedAccount = "";
		SmartCodeMessageDTO smartCodeMessageDTO = new SmartCodeMessageDTO();
		List<String> codeRuleDataList = codeRules.getCodeRuleDataList().stream().map(x -> x.getDataListValue())
				.collect(Collectors.toList());

		ClaimAssignmentDataResponseDTO[] claimAssignmentDataResponse = getClaimAssignmentDataResponse(
				smartCodeRulesDTO.getCustomerNumber());
		assert claimAssignmentDataResponse.length == 1;
		cusRef = claimAssignmentDataResponse[0].getCustomerReferenceNumber();
		alignedAccount = claimAssignmentDataResponse[0].getAlignedAccount();
		log.info("cusRef=" + cusRef);
		Optional<CodeRuleType> codeRuleType = codeRuleTypeRepository
				.findById(codeRules.getCodeRuleType().getCodeRuleTypeId());
		log.info("codeRuleType==" + codeRuleType.get().getCodeRuleTypeCode());
		if (codeRuleType.get().getCodeRuleTypeCode().equalsIgnoreCase("Inclusive") && codeRuleDataList != null
				&& codeRuleDataList.contains(cusRef)) {// success message
			smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRules.isAllowOverride(),
					CommonConstant.CUSTOMER_REF_NO, null, codeRules.getSuccessMessage());

		}

		else {
			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),
					CommonConstant.CUSTOMER_REF_NO, null, null);
		}

		if (codeRuleType.get().getCodeRuleTypeCode().equalsIgnoreCase("Exclusive") && codeRuleDataList != null
				&& !codeRuleDataList.contains(cusRef) && alignedAccount.equalsIgnoreCase("No")) {
			log.info("enter inside--");

			smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRules.isAllowOverride(),
					CommonConstant.CUSTOMER_REF_NO, null, codeRules.getSuccessMessage());

		}

		else {
			smartCodeMessageDTO = getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),
					CommonConstant.CUSTOMER_REF_NO, null, null);
		}

		return smartCodeMessageDTO;
	}

	private SmartCodeMessageDTO validateEndUserInfoRules(ClaimLevelRulesDTO smartCodeRulesDTO, CodeRules codeRules) {
		List<EndUserInformation> endUserinfoList = endUserInformationRepository
				.findByClaimId(smartCodeRulesDTO.getClaimId());

		if (endUserinfoList == null || endUserinfoList.isEmpty())
			return getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),
					CommonConstant.END_USER_INFO, null, null);
		else
			return getSmartCodeMessageDTO(null, codeRules.isAllowOverride(), CommonConstant.END_USER_INFO, null,
					codeRules.getSuccessMessage());

	}

	private SmartCodeMessageDTO getSmartCodeMessageDTO(String errorMessage, boolean override, String ruleCategory,
			String autoCode, String successMessage) {

		SmartCodeMessageDTO smartCodeMessageDTO = new SmartCodeMessageDTO();
		smartCodeMessageDTO.setErrorMessage(errorMessage);
		smartCodeMessageDTO.setSuccessMessage(successMessage);
		smartCodeMessageDTO.setRuleCategory(ruleCategory);
		smartCodeMessageDTO.setOverride(override);
		smartCodeMessageDTO.setAutoCode(autoCode);
		smartCodeMessageDTO.setUpdatedReasonCode(autoCode);
		return smartCodeMessageDTO;
	}

	private ClaimAssignmentDataResponseDTO[] getClaimAssignmentDataResponse(String customerNumber) {
		String url = "/customer/v1/getClaimAssignmentData?customerNumber=" + customerNumber;
		String finalUrl = restUrl + url;
		log.info("Final Url :: " + finalUrl);
		ClaimAssignmentDataResponseDTO[] claimAssignmentDataResponse = restTemplate.getForObject(finalUrl,
				ClaimAssignmentDataResponseDTO[].class);
		return claimAssignmentDataResponse;
	}

	private SmartCodeMessageDTO validateFreightRules(ClaimLevelRulesDTO smartCodeRulesDTO, CodeRules codeRule) {
		int claimId = getClaimIdByLineDetails(smartCodeRulesDTO);

		SmartCodeMessageDTO smartCodeMessageDTO = new SmartCodeMessageDTO();

		if (smartCodeRulesDTO.getLineDetails() != null) {
			for (LineDetailsRequest claimLineDetail : smartCodeRulesDTO.getLineDetails()) {
				ClaimLineDetail claimLineDetailInfo = claimLineDetailsRepository
						.findByClaimLineId(claimLineDetail.getLineId());

				if (claimLineDetailInfo == null) {
					throw new CommonException(
							"There is no claim line details found for this claim #" + smartCodeRulesDTO.getClaimId());
				}
				String updatedReasonCode = null;

				if (claimLineDetailInfo.getDetailType().getDetailTypeCode().equalsIgnoreCase("FRGHT")) {
					claimLineDetailInfo.getClaimReasonDefinition().setClaimReasonCode("F31");
					claimLineDetailInfo = claimLineDetailsRepository.save(claimLineDetailInfo);

					updatedReasonCode = claimLineDetailInfo.getClaimReasonDefinition().getClaimReasonCode();
					smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRule.isAllowOverride(),
							CommonConstant.REASON_CODE, updatedReasonCode, codeRule.getSuccessMessage());

				}

				if (claimLineDetailInfo.getDetailType().getDetailTypeCode().equalsIgnoreCase("FRGHT")
						&& smartCodeRulesDTO.getReasonCode().startsWith("D")) {
					claimLineDetailInfo.getClaimReasonDefinition().setClaimReasonCode("D40");
					claimLineDetailInfo = claimLineDetailsRepository.save(claimLineDetailInfo);
					updatedReasonCode = claimLineDetailInfo.getClaimReasonDefinition().getClaimReasonCode();
					smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRule.isAllowOverride(),
							CommonConstant.REASON_CODE, updatedReasonCode, codeRule.getSuccessMessage());
				}

				if (claimLineDetailInfo.getDetailType().getDetailTypeCode().equalsIgnoreCase("FRGHT")
						&& smartCodeRulesDTO.getReasonCode().startsWith("G")) {
					claimLineDetailInfo.getClaimReasonDefinition().setClaimReasonCode("F03");
					claimLineDetailInfo = claimLineDetailsRepository.save(claimLineDetailInfo);
					updatedReasonCode = claimLineDetailInfo.getClaimReasonDefinition().getClaimReasonCode();
					smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRule.isAllowOverride(),
							CommonConstant.REASON_CODE, updatedReasonCode, codeRule.getSuccessMessage());
				}

				if (claimLineDetailInfo.getDetailType().getDetailTypeCode().equalsIgnoreCase("FRGHT")
						&& (smartCodeRulesDTO.getReasonCode().startsWith("C")
								|| smartCodeRulesDTO.getReasonCode().startsWith("H")
								|| smartCodeRulesDTO.getReasonCode().startsWith("S"))) {
					claimLineDetailInfo.getClaimReasonDefinition().setClaimReasonCode("F04");
					claimLineDetailInfo = claimLineDetailsRepository.save(claimLineDetailInfo);
					updatedReasonCode = claimLineDetailInfo.getClaimReasonDefinition().getClaimReasonCode();
					smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRule.isAllowOverride(),
							CommonConstant.REASON_CODE, updatedReasonCode, codeRule.getSuccessMessage());
				}

				List<ClaimRgaHeader> claimRgaHeaders = claimRgaHeaderRepository.findByClaimIdAndRgaStatusId(claimId);
				if (claimLineDetailInfo.getDetailType().getDetailTypeCode().equalsIgnoreCase("FRGHT")
						&& (smartCodeRulesDTO.getReasonCode().startsWith("A")
								|| smartCodeRulesDTO.getReasonCode().startsWith("T")
								|| smartCodeRulesDTO.getReasonCode().startsWith("B04"))
						&& claimRgaHeaders != null && !claimRgaHeaders.isEmpty()) {
					claimLineDetailInfo.getClaimReasonDefinition().setClaimReasonCode("F23");
					claimLineDetailInfo = claimLineDetailsRepository.save(claimLineDetailInfo);
					updatedReasonCode = claimLineDetailInfo.getClaimReasonDefinition().getClaimReasonCode();
					smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRule.isAllowOverride(),
							CommonConstant.REASON_CODE, updatedReasonCode, codeRule.getSuccessMessage());
				}

				if (claimLineDetailInfo.getDetailType().getDetailTypeCode().equalsIgnoreCase("FRGHT")
						&& claimLineDetailInfo.getClaim().getClaimReasonDefinition().getClaimReasonCode()
								.startsWith("A03")
						|| claimLineDetailInfo.getClaim().getClaimReasonDefinition().getClaimReasonCode()
								.startsWith("F31")) {
					claimLineDetailInfo.getClaimReasonDefinition().setClaimReasonCode("F31");
					claimLineDetailInfo = claimLineDetailsRepository.save(claimLineDetailInfo);
					updatedReasonCode = claimLineDetailInfo.getClaimReasonDefinition().getClaimReasonCode();
					smartCodeMessageDTO = getSmartCodeMessageDTO(null, codeRule.isAllowOverride(),
							CommonConstant.REASON_CODE, updatedReasonCode, codeRule.getSuccessMessage());

				}

				if (smartCodeMessageDTO == null) {
					smartCodeMessageDTO = getSmartCodeMessageDTO(codeRule.getFailureMessage(),
							codeRule.isAllowOverride(), CommonConstant.LINE_LEVEL, null, null);
				}
			}
		}

		return smartCodeMessageDTO;
	}

	public SmartCodeMessageDTO validateFrghtaddInVoice(ClaimDTO claimDTO, SmartCodeMessageDTO smartCodeMessageDTO)
			throws Exception {
		Claim claim = findByClaimId(claimDTO.getClaimId());

		for (ClaimLineDetail claimLine : claim.getClaimLineDetail()) {

			ClaimLineDetailDTO claimLineDetailDto = new ClaimLineDetailDTO();
			String detailsTypeCode = claimLineDetailDto.getDetailTypeCode();

			for (ClaimLineDetail claimLineDetail : claim.getClaimLineDetail()) {
				if ("MERCH".equals(detailsTypeCode)) {
					claimLineDetail.getClaimReasonDefinition()
							.setClaimReasonCode(claim.getClaimReasonDefinition().getClaimReasonCode());
				} else if ("FRGHT".equals(detailsTypeCode)) {

					ClaimLevelRulesDTO smartCodeRulesDTO = new ClaimLevelRulesDTO();
					List<SmartCodeMessageDTO> smartCodeMessageList = claimLevelValidations(smartCodeRulesDTO);
					if (!smartCodeMessageList.isEmpty()) {
						SmartCodeMessageDTO updatedMessage = smartCodeMessageList.get(0);
						if (updatedMessage.getUpdatedReasonCode() != null
								&& !updatedMessage.getUpdatedReasonCode().isEmpty()) {
							claimLineDetail.getClaimReasonDefinition()
									.setClaimReasonCode(updatedMessage.getUpdatedReasonCode());
						} else {
							claimLineDetail.getClaimReasonDefinition()
									.setClaimReasonCode(claim.getClaimReasonDefinition().getClaimReasonCode());
						}
					}
				}
			}
		}
		claim = claimRepository.save(claim);

		return smartCodeMessageDTO;

	}

	private Claim findByClaimId(Integer claimId) {
		Optional<Claim> claim = claimRepository.findById(claimId);
		if (!claim.isPresent()) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found In This ClaimId :: " + claimId);
		}
		return claim.get();
	}

}
