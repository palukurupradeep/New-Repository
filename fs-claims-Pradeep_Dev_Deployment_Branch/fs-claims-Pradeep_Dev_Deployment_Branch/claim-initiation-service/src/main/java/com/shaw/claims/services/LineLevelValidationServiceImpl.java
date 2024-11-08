package com.shaw.claims.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.dto.ClaimLevelRulesDTO;
import com.shaw.claims.dto.SmartCodeMessageDTO;
import com.shaw.claims.enums.ReasonCodes;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.ClaimAddress;
import com.shaw.claims.model.ClaimDocument;
import com.shaw.claims.model.ClaimLineDetail;
import com.shaw.claims.model.CodeRules;
import com.shaw.claims.model.RcsCodes;
import com.shaw.claims.model.SellingCompany;
import com.shaw.claims.model.State;
import com.shaw.claims.repo.ClaimAddressRepository;
import com.shaw.claims.repo.ClaimLineDetailsRepository;
import com.shaw.claims.repo.CodeRulesRepository;
import com.shaw.claims.repo.CountryRepository;
import com.shaw.claims.repo.RcsCodesRepository;
import com.shaw.claims.repo.SellingCompanyRepository;
import com.shaw.claims.repo.StateRepository;

@Service
public class LineLevelValidationServiceImpl {

	@Autowired
	ClaimLineDetailsRepository claimLineDetailsRepo;

	@Autowired
	SellingCompanyRepository sellingCompanyRepo;

	@Autowired
	CodeRulesRepository codeRulesRepo;

	@Autowired
	ClaimAddressRepository claimAddressRepo;

	@Autowired
	CountryRepository countryRepo;

	@Autowired
	StateRepository stateRepo;
	
	@Autowired
	RcsCodesRepository rcsCodeRepo;

	public SmartCodeMessageDTO lineLevelValidations1(ClaimLevelRulesDTO claimLevelRulesDTO) {
		SmartCodeMessageDTO smartCodeMessageDTO = new SmartCodeMessageDTO();
		switch (claimLevelRulesDTO.getReasonCode()) {
		case "W93", "W94", "W96", "W98": {
			// smartCodeMessageDTO = validateRuleCategory(claimLevelRulesDTO, true, "Country
			// Code", "CA");
			break;
		}
		case "W97": {
			// smartCodeMessageDTO = validateRuleCategory(claimLevelRulesDTO, false,
			// "Country Code", "GB");
			break;
		}
		case "UAL", "UAR", "UAZ", "UCA", "UCO", "UCT", "UDC", "UFL", "UGA", "UHI", "UIA", "UID", "UIL", "UIN", "UKS",
		"UKY", "ULA", "UMA", "UMD", "UME", "UMI", "UMN", "UMO", "UMS", "UNC", "UND", "UNE", "UNJ", "UNM", "UNV",
		"UNY", "UOH", "UOK", "UPA", "URI", "USC", "USD", "UTN", "UTX", "UUT", "UVA", "UVT", "UWA", "UWI", "UWV",
		"UWY": {
			// smartCodeMessageDTO = validateRuleCategory(claimLevelRulesDTO, false,
			// "State", "GB");
			break;
		}
		default:
			//smartCodeMessageDTO = new SmartCodeMessageDTO(null, "Invalid Reason Code", null, true);
		}
		return smartCodeMessageDTO;
	}

	private SmartCodeMessageDTO validateCountryCodeOrState(ClaimLevelRulesDTO claimLevelRulesDTO, CodeRules codeRules,
			ClaimLineDetail claimLineDetails) {

		SmartCodeMessageDTO smartCodeMessageDTO = switch (ReasonCodes.valueOf(claimLevelRulesDTO.getReasonCode())) {
		case W93, W94, W96, W97, W98: {
			ClaimAddress claimAddress = getClaimAddress(claimLineDetails, CommonConstant.COUNTRY_CODE, null);
			if (claimAddress != null) {
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),
						CommonConstant.COUNTRY_CODE);
			} else
				yield getSmartCodeMessageDTO(CommonConstant.COUNTRY_CODE, codeRules.getSuccessMessage());
		}
		case UAL, UAR, UAZ, UCA, UCO, UCT, UDC, UFL, UGA, UHI, UIA, UID, UIL, UIN, UKS, UKY, ULA, UMA, UMD, UME, UMI,
		UMN, UMO, UMS, UNC, UND, UNE, UNJ, UNM, UNV, UNY, UOH, UOK, UPA, URI, USC, USD, UTN, UTX, UUT, UVA, UVT,
		UWA, UWI, UWV, UWY: {
			ClaimAddress claimAddress = getClaimAddress(claimLineDetails, CommonConstant.STATE,
					codeRules.getClaimReasonDefinition().getClaimReasonCode().substring(1));
			if (claimAddress != null) {
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),
						CommonConstant.STATE);
			} else
				yield getSmartCodeMessageDTO(CommonConstant.STATE, codeRules.getSuccessMessage());
		}
		case UBC, UMB, UON, UQC, USK: {
			ClaimAddress claimAddress = getClaimAddress(claimLineDetails, CommonConstant.STATE,
					codeRules.getClaimReasonDefinition().getClaimReasonCode().substring(1));
			if (claimAddress != null) {
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),
						CommonConstant.PROVINCE);
			} else
				yield getSmartCodeMessageDTO(CommonConstant.PROVINCE, codeRules.getSuccessMessage());
		}
		default:
			throw new CommonException(String.valueOf(HttpStatus.OK));
		};
		return smartCodeMessageDTO;
	}

	private ClaimAddress getClaimAddress(ClaimLineDetail claimLineDetails, String ruleCategory, String stateCode) {
		if (claimLineDetails.getClaim().getClaimAddresses() != null) {
			claimLineDetails.getClaim().getClaimAddresses().forEach(claimAddress -> {
				if (ruleCategory.equalsIgnoreCase(CommonConstant.COUNTRY_CODE) && claimAddress.getAddressTypeId() == 8
						&& claimAddress.getCountryId() != 41) {
					return;
				}

				if (ruleCategory.equalsIgnoreCase(CommonConstant.STATE) && claimAddress.getAddressTypeId() == 8) {
					State state = stateRepo.findByIsoStateCode(stateCode);
					if (claimAddress.getStateId() == state.getIsoStateId()) {
						// validationpending for issue credit memu
						return;
					}
				}
			});
		}
		return null;
	}

	public List<SmartCodeMessageDTO> lineLevelValidations(ClaimLevelRulesDTO claimLevelRulesDTO) {
		List<SmartCodeMessageDTO> smartCodeMessageList = new ArrayList<>();
		//List<CodeRules> codeRules = codeRulesRepo.fetchCodeRuleByReasonId(claimLevelRulesDTO.getReasonId());
		List<CodeRules> codeRules = null;
		Optional<ClaimLineDetail> claimLineDetails = claimLineDetailsRepo
				.findByClaim_ClaimIdAndClaimReasonDefinition_claimReasonCode(claimLevelRulesDTO.getClaimId(),
						claimLevelRulesDTO.getReasonCode());
		for (CodeRules codeRule : codeRules) {
			SmartCodeMessageDTO smartCodeMessageDTO = switch (codeRule.getCodeRuleCategory()
					.getCodeRuleCategoryCode()) {
					case CommonConstant.SELLING_CMPNY: {
						yield validateSellingCompanyRules(claimLevelRulesDTO, codeRule, claimLineDetails.get());
					}
					case CommonConstant.VENDOR_NAME: {
						yield validateVendorNameRules(claimLevelRulesDTO, codeRule, claimLineDetails.get());
					}
					case CommonConstant.COUNTRY_CODE: {
						yield validateCountryCodeOrState(claimLevelRulesDTO, codeRule, claimLineDetails.get());
					}
					case CommonConstant.STATE: {
						yield validateCountryCodeOrState(claimLevelRulesDTO, codeRule, claimLineDetails.get());
					}
					case CommonConstant.PROVINCE: {
						yield validateCountryCodeOrState(claimLevelRulesDTO, codeRule, claimLineDetails.get());
					}
					case CommonConstant.RGA: {
						yield validateRGA(claimLevelRulesDTO, codeRule, claimLineDetails.get());
					}
					case CommonConstant.RCS_CODE: {
						yield validateRCSCode(claimLevelRulesDTO, codeRule, claimLineDetails.get());
					}
					case CommonConstant.REASON_CODE: {
						yield validateReasonCode(claimLevelRulesDTO, codeRule, claimLineDetails.get());
					}
					case CommonConstant.FREIGHT: {
						yield validateFreight(claimLevelRulesDTO, codeRule, claimLineDetails.get());
					}
					default:
						throw new CommonException(String.valueOf(HttpStatus.OK));

			};
			smartCodeMessageList.add(smartCodeMessageDTO);
		}

		return smartCodeMessageList;
	}

	private SmartCodeMessageDTO validateFreight(ClaimLevelRulesDTO claimLevelRulesDTO, CodeRules codeRules,
			ClaimLineDetail claimLineDetail) {
		SmartCodeMessageDTO smartCodeMessageDTO = switch (ReasonCodes.valueOf(claimLevelRulesDTO.getReasonCode())) {
		case A03,F31: {
			
			if(codeRules.getCodeRuleActionType().getCodeRuleActionTypeid() == 4) {
				
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),
						CommonConstant.FREIGHT);
			} else
				yield getSmartCodeMessageDTO(CommonConstant.FREIGHT, codeRules.getSuccessMessage());
		}
		case D40: {
			
			if(!codeRules.getClaimReasonDefinition().getClaimReasonCode().startsWith("D")) {
				
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),
						CommonConstant.FREIGHT);
			} else if(codeRules.getCodeRuleActionType().getCodeRuleActionTypeid() == 4) 
				yield getSmartCodeMessageDTO(CommonConstant.FREIGHT, codeRules.getSuccessMessage());
		}
		case F03: {
			
			if(!codeRules.getClaimReasonDefinition().getClaimReasonCode().startsWith("G")) {
				
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),
						CommonConstant.FREIGHT);
			} else if(codeRules.getCodeRuleActionType().getCodeRuleActionTypeid() == 4) 
				yield getSmartCodeMessageDTO(CommonConstant.FREIGHT, codeRules.getSuccessMessage());
		}
		case F23: {
			
			/**
			 * validation pending for issue credit memo development
			 **/
			
			boolean rga = false;
			
			if((!codeRules.getClaimReasonDefinition().getClaimReasonCode().startsWith("A") || !codeRules.getClaimReasonDefinition().getClaimReasonCode().startsWith("T")
					|| !codeRules.getClaimReasonDefinition().getClaimReasonCode().startsWith("B04")) && rga) {
				
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),
						CommonConstant.FREIGHT);
			} else if(codeRules.getCodeRuleActionType().getCodeRuleActionTypeid() == 4) 
				yield getSmartCodeMessageDTO(CommonConstant.FREIGHT, codeRules.getSuccessMessage());
		}
		default:
			throw new CommonException(String.valueOf(HttpStatus.OK));
		};
		return smartCodeMessageDTO;
	}

	private SmartCodeMessageDTO validateReasonCode(ClaimLevelRulesDTO claimLevelRulesDTO, CodeRules codeRules,
			ClaimLineDetail claimLineDetail) {
		SmartCodeMessageDTO smartCodeMessageDTO = switch (ReasonCodes.valueOf(claimLevelRulesDTO.getReasonCode())) {
		case L00: {
			yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),
					CommonConstant.REASON_CODE);
		}
		case W02: {
			boolean reasonCode = false;
			/**
			 * validation pending for issue credit memo development.*
			 **/
			if (reasonCode) {
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),
						CommonConstant.REASON_CODE);
			} else
				yield getSmartCodeMessageDTO(CommonConstant.REASON_CODE, codeRules.getSuccessMessage());
		}

		default:
			throw new CommonException(String.valueOf(HttpStatus.OK));
		};
		return smartCodeMessageDTO;
	}

	private SmartCodeMessageDTO validateRCSCode(ClaimLevelRulesDTO claimLevelRulesDTO, CodeRules codeRules,
			ClaimLineDetail claimLineDetail) {
		SmartCodeMessageDTO smartCodeMessageDTO = switch (ReasonCodes.valueOf(claimLevelRulesDTO.getReasonCode())) {
		case I02,I03,I04,I07,I08,I09,I10,I11,I12,I13,I14,I15,I17: {
			Optional<RcsCodes> rcsCodes = rcsCodeRepo.findById(claimLineDetail.getRcsCodes().getRcsCodeId());
			if ((rcsCodes.isPresent() && !rcsCodes.get().getRcsCode().equalsIgnoreCase(CommonConstant.RCSCODE_SAMPLE))) {
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),
						CommonConstant.RCS_CODE);
			} else
				yield getSmartCodeMessageDTO(CommonConstant.RCS_CODE, codeRules.getSuccessMessage());
		}

		default:
			throw new CommonException(String.valueOf(HttpStatus.OK));
		};
		return smartCodeMessageDTO;
	}

	private SmartCodeMessageDTO validateRGA(ClaimLevelRulesDTO claimLevelRulesDTO, CodeRules codeRules,
			ClaimLineDetail claimLineDetail) {

		SmartCodeMessageDTO smartCodeMessageDTO = switch (ReasonCodes.valueOf(claimLevelRulesDTO.getReasonCode())) {
		case A02, A03, A05: {
			boolean isRGA = false;
			/**
			 * RGA validation need to performed after implemeting issue credit memo
			 **/
			if (isRGA) {
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),
						CommonConstant.RGA);
			} else
				yield getSmartCodeMessageDTO(CommonConstant.RGA, codeRules.getSuccessMessage());
		}

		default:
			throw new CommonException(String.valueOf(HttpStatus.OK));
		};
		return smartCodeMessageDTO;
	}

	private SmartCodeMessageDTO getSmartCodeMessageDTO(String errorMessage, boolean override, String ruleCategory) {

		SmartCodeMessageDTO smartCodeMessageDTO = new SmartCodeMessageDTO();
		smartCodeMessageDTO.setErrorMessage(errorMessage);
		smartCodeMessageDTO.setRuleCategory(ruleCategory);
		smartCodeMessageDTO.setOverride(override);

		return smartCodeMessageDTO;
	}

	private SmartCodeMessageDTO getSmartCodeMessageDTO(String ruleCategory, String successMessage) {

		SmartCodeMessageDTO smartCodeMessageDTO = new SmartCodeMessageDTO();
		smartCodeMessageDTO.setSuccessMessage(successMessage);
		smartCodeMessageDTO.setRuleCategory(ruleCategory);
		return smartCodeMessageDTO;
	}

	private SmartCodeMessageDTO validateSellingCompanyRules(ClaimLevelRulesDTO claimLevelRulesDTO, CodeRules codeRules,
			ClaimLineDetail claimLineDetails) {
		if (claimLineDetails.getSellingCompany().isEmpty())
			throw new CommonException(String.valueOf(HttpStatus.OK));
		Optional<SellingCompany> sellingCompany = sellingCompanyRepo
				.findSellingCompnayBySellingCompanyCode(claimLineDetails.getSellingCompany());
		SmartCodeMessageDTO smartCodeMessageDTO = switch (ReasonCodes.valueOf(claimLevelRulesDTO.getReasonCode())) {
		case A30, A31: {
			if (sellingCompany.isPresent() && sellingCompany.get().getResilient() == false)
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),
						CommonConstant.SELLING_CMPNY);
			else
				yield getSmartCodeMessageDTO(CommonConstant.SELLING_CMPNY, codeRules.getSuccessMessage());
		}
		default:
			throw new CommonException(String.valueOf(HttpStatus.OK));
		};
		return smartCodeMessageDTO;
	}

	private SmartCodeMessageDTO validateVendorNameRules(ClaimLevelRulesDTO claimLevelRulesDTO, CodeRules codeRules,
			ClaimLineDetail claimLineDetails) {
		String vendorName = "";
		outerScope: for (ClaimDocument doc : claimLineDetails.getClaim().getClaimDocuments()) {
			for (ClaimLineDetail lineDetails : doc.getClaimLineDetails()) {
				if (!lineDetails.getVendorId().isEmpty())
					vendorName = lineDetails.getVendorId();
				break outerScope;
			}
		}
		SmartCodeMessageDTO smartCodeMessageDTO = switch (ReasonCodes.valueOf(claimLevelRulesDTO.getReasonCode())) {
		case Z01, Z02, Z03, Z04, Z05, Z06, Z07, Z08, Z09, Z11, Z12, Z15, Z16, Z17, Z18, Z19, Z20, Z21, Z22, Z24, Z26,
		Z27, Z29, Z32, Z35, Z36, Z37, Z39, Z41, Z42, Z43, Z45, Z46, Z47, Z48, Z49, Z53, Z54, Z55, Z57, Z59, Z60,
		Z61, Z62, Z63, Z65, Z66, Z68, Z72, Z73, Z74, Z77, Z78, Z79, Z81, Z87, Z88, Z89, Z90, Z92, Z94, Z96, Z97,
		Z98, Z99: {
			if (vendorName != null && !vendorName.equalsIgnoreCase(CommonConstant.VENDOR_NAME_SHAW))
				yield getSmartCodeMessageDTO(codeRules.getFailureMessage(), codeRules.isAllowOverride(),
						CommonConstant.VENDOR_NAME);
			else
				yield getSmartCodeMessageDTO(CommonConstant.VENDOR_NAME_SHAW, codeRules.getSuccessMessage());
		}
		default:
			throw new CommonException(String.valueOf(HttpStatus.OK));
		};
		return smartCodeMessageDTO;
	}

}
