package com.shaw.claims.dto;

import lombok.Data;

@Data
public class CodeRulesDTO extends BaseEntityDTO {
	
	private Integer codeRuleId;
	
	private ClaimReasonDefinitionDTO claimReasonDefinitionDTO;
	private String ruleDescription;
	private CodeRuleCategoryDTO codeRuleCategoryDTO;
	private CodeRuleTypeDTO codeRuleTypeDTO;
	private CodeRuleLevelDTO codeRuleLevelDTO;
	private CodeRuleImpactAreaDTO codeRuleImpactAreaDTO;
	private CodeRuleActionTypeDTO codeRuleActionTypeDTO;
	private CodeRuleDataTypeDTO codeRuleDataTypeDTO;
	private String actionDescription;
	private String successMessage;
	private String failureMessage;
	private boolean allowOverride;
	private boolean approvalRequired;
	private boolean updateWorkStatus;
	private WorkStatusDTO workStatusDTO;
	private String workStatusRemarks;
	private CodeRuleDataListDTO codeRuleDataListDTO;
}
