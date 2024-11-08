package com.shaw.claims.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AddOrUpdateReasonDefinitionDTO extends ClaimReasonDefinitionDTO{
		private Integer claimReasonId;
	    private String claimCategoryCode;

	    private Integer visibilityScope;

	    private Integer reasonTypeGroup;

	    private Boolean isDistributionCompliance;

	    private String sampleSizeRequirement;

	    private String definition;

	    private String cause;

	    private String testing;

	    private Integer statusId;
	    
	    private String claimReasonDescription;

	    private boolean allowanceToKeep;

	    private boolean serviceToKeep;

	    private boolean returnEligible;

	    private String codeRuleReturnTypeCode;
}
