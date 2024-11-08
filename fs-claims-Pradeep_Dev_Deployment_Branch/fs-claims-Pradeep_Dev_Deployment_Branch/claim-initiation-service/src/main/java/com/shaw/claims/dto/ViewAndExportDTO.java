package com.shaw.claims.dto;

import lombok.Data;

@Data
public class ViewAndExportDTO extends BaseEntityDTO {
	private Integer codeRuleId;
	private String codeDescription;
	private String ruleDescription;
	private boolean allowOverride;
	private boolean approvalRequired;
	private String ruleCategory;
	private String ruleLevel;
	private boolean allowanceToKeep;
	private boolean serviceToKeep;
	public String areaOfImpact;
	public String displayMessage;
}
