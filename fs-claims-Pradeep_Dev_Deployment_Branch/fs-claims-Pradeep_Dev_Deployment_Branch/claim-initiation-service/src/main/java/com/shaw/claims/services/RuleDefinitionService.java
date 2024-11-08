package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.dto.CodeRulesDTO;
import com.shaw.claims.dto.ViewAndExportDTO;
import com.shaw.claims.model.CodeRuleActionType;
import com.shaw.claims.model.CodeRuleCategory;
import com.shaw.claims.model.CodeRuleDataType;
import com.shaw.claims.model.CodeRuleImpactArea;
import com.shaw.claims.model.CodeRuleLevel;
import com.shaw.claims.model.CodeRuleType;

public interface RuleDefinitionService {

	public List<CodeRuleCategory> getCodeRuleCategory();
	public List<CodeRuleType> getCodeRuleType();
	public List<CodeRuleLevel> getCodeRuleLevel();
	public List<CodeRuleImpactArea> getCodeRuleImpactArea();
	public List<CodeRuleActionType> getCodeRuleActionType();
	public List<CodeRuleDataType> getCodeRuleDataType();
	public List<CodeRulesDTO> getCodeRuleByReasonCode(String reasonCode);
	public void deleteCodeRuleById(Integer codeRuleId);
	public List<ViewAndExportDTO> viewAndExportRule(String reasonCode);
}
