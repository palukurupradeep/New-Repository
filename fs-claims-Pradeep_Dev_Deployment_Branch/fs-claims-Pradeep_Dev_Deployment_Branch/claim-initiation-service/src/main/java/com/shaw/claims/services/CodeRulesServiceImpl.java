package com.shaw.claims.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.shaw.claims.model.CodeRuleDataList;
import com.shaw.claims.model.CodeRuleDataType;
import com.shaw.claims.repo.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.shaw.claims.dto.CodeRulesDTO;
import com.shaw.claims.enums.StatusTypes;
import com.shaw.claims.model.CodeRules;

@Service
public class CodeRulesServiceImpl implements CodeRulesService {
	
	@Autowired
	CodeRulesRepository codeRulesRepository;
	@Autowired
	ClaimReasonDefinitionRepository claimReasonDefinitionRepository;
	@Autowired
	CodeRuleCategoryRepository codeRuleCategoryRepository;
	@Autowired
	CodeRuleTypeRepository codeRuleTypeRepository;
	@Autowired
	CodeRuleLevelRepository codeRuleLevelRepository;
	@Autowired
	CodeRuleImpactAreaRepository codeRuleImpactAreaRepository;
	@Autowired
	CodeRuleActionTypeRepository codeRuleActionTypeRepository;
	@Autowired
	CodeRuleDataTypeRepository codeRuleDataTypeRepository;
	@Autowired
	WorkStatusRepository workStatusRepository;
	@Autowired
	CodeRuleDataListRepository codeRuleDataListRepository;

	@Override
	public void saveCodeRules(CodeRulesDTO codeRulesDTO) {
		CodeRules rules = new CodeRules();
		
		if (Objects.nonNull(codeRulesDTO.getCodeRuleId()) && codeRulesDTO.getCodeRuleId() != 0) {
			rules.setCodeRuleId(codeRulesDTO.getCodeRuleId());
			codeRuleDataListRepository.deleteCodeRuleDataListBycodeRuleId(codeRulesDTO.getCodeRuleId());
		}
		
		CodeRuleDataType codeRuleDataType = codeRuleDataTypeRepository.findByCodeRuleDataTypeCode(codeRulesDTO.getCodeRuleDataTypeDTO().getCodeRuleDataTypeCode());
		rules.setCodeRuleDataType(codeRuleDataType);
		
		rules.setClaimReasonDefinition(claimReasonDefinitionRepository.findByClaimReasonCode(codeRulesDTO.getClaimReasonDefinitionDTO().getClaimReasonCode()));
		rules.setRuleDescription(codeRulesDTO.getRuleDescription());
		rules.setCodeRuleCategory(codeRuleCategoryRepository.findByCodeRuleCategoryCode(codeRulesDTO.getCodeRuleCategoryDTO().getCodeRuleCategoryCode()));
		rules.setCodeRuleType(codeRuleTypeRepository.findByCodeRuleTypeCode(codeRulesDTO.getCodeRuleTypeDTO().getCodeRuleTypeCode()));
		rules.setCodeRuleLevel(codeRuleLevelRepository.findByCodeRuleLevelCode(codeRulesDTO.getCodeRuleLevelDTO().getCodeRuleLevelCode()));
		rules.setCodeRuleImpactArea(codeRuleImpactAreaRepository.findByCodeRuleImpactAreaCode(codeRulesDTO.getCodeRuleImpactAreaDTO().getCodeRuleImpactAreaCode()));
		rules.setCodeRuleActionType(codeRuleActionTypeRepository.findByCodeRuleActionTypeCode(codeRulesDTO.getCodeRuleActionTypeDTO().getCodeRuleActionTypeCode()));
		rules.setActionDescription(codeRulesDTO.getActionDescription());
		
		rules.setSuccessMessage(codeRulesDTO.getSuccessMessage());
		rules.setFailureMessage(codeRulesDTO.getFailureMessage());
		rules.setAllowOverride(codeRulesDTO.isAllowOverride());
		rules.setApprovalRequired(codeRulesDTO.isApprovalRequired());
		rules.setUpdateWorkStatus(codeRulesDTO.isUpdateWorkStatus());
		if(codeRulesDTO.getWorkStatusDTO().getWorkStatusCode().isEmpty())
		{
			rules.setWorkStatus(workStatusRepository.findByWorkStatusCode("000"));
		}else {
			rules.setWorkStatus(workStatusRepository.findByWorkStatusCode(codeRulesDTO.getWorkStatusDTO().getWorkStatusCode()));
		}
		
		rules.setWorkStatusRemarks(codeRulesDTO.getWorkStatusRemarks());
		rules.setStatusId(StatusTypes.ACTIVE.getStatusId());
		rules.setCreatedByUserId(codeRulesDTO.getCreatedByUserId());
		rules.setModifiedByUserId(codeRulesDTO.getModifiedByUserId());
		codeRulesRepository.save(rules);
		String[] arrOfdataList=codeRulesDTO.getCodeRuleDataListDTO().getDataListValue().split(",",-2);
		List<CodeRuleDataList> codeRuleDataResultList=new ArrayList<>();
		for (String dataListResult : arrOfdataList) {
			CodeRuleDataList dataList=new CodeRuleDataList();
			
			dataList.setDataListValue(dataListResult);
			dataList.setCodeRuleDataType(codeRuleDataType);
			dataList.setCreatedByUserId(codeRulesDTO.getCreatedByUserId());
			dataList.setModifiedByUserId(codeRulesDTO.getModifiedByUserId());
			dataList.setCodeRules(rules);
			codeRuleDataResultList.add(dataList);
			
		}
		codeRuleDataListRepository.saveAll(codeRuleDataResultList);
		
	
	}

	
}
