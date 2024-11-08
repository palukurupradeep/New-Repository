package com.shaw.claims.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.dto.ClaimReasonDefinitionDTO;
import com.shaw.claims.dto.CodeRuleActionTypeDTO;
import com.shaw.claims.dto.CodeRuleCategoryDTO;
import com.shaw.claims.dto.CodeRuleDataListDTO;
import com.shaw.claims.dto.CodeRuleDataTypeDTO;
import com.shaw.claims.dto.CodeRuleImpactAreaDTO;
import com.shaw.claims.dto.CodeRuleLevelDTO;
import com.shaw.claims.dto.CodeRuleTypeDTO;
import com.shaw.claims.dto.CodeRulesDTO;
import com.shaw.claims.dto.ViewAndExportDTO;
import com.shaw.claims.dto.WorkStatusDTO;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.CodeRuleActionType;
import com.shaw.claims.model.CodeRuleCategory;
import com.shaw.claims.model.CodeRuleDataType;
import com.shaw.claims.model.CodeRuleImpactArea;
import com.shaw.claims.model.CodeRuleLevel;
import com.shaw.claims.model.CodeRuleType;
import com.shaw.claims.model.CodeRules;
import com.shaw.claims.repo.ClaimReasonDefinitionRepository;
import com.shaw.claims.repo.CodeRuleActionTypeRepository;
import com.shaw.claims.repo.CodeRuleCategoryRepository;
import com.shaw.claims.repo.CodeRuleDataListRepository;
import com.shaw.claims.repo.CodeRuleDataTypeRepository;
import com.shaw.claims.repo.CodeRuleImpactAreaRepository;
import com.shaw.claims.repo.CodeRuleLevelRepository;
import com.shaw.claims.repo.CodeRuleTypeRepository;
import com.shaw.claims.repo.CodeRulesRepository;

@Service
public class RuleDefinitionServiceImpl implements RuleDefinitionService {

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
	CodeRulesRepository codeRulesRepository;
	@Autowired
	ClaimReasonDefinitionRepository claimReasonDefinitionRepository;
	@Autowired
	CodeRuleDataListRepository codeRuleDataListRepository;
	
	@Override
	public List<CodeRuleCategory> getCodeRuleCategory() {
		
		return codeRuleCategoryRepository.findByStatusId(CommonConstant.ACTIVE);
	}

	@Override
	public List<CodeRuleType> getCodeRuleType() {
		
		return codeRuleTypeRepository.findByStatusId(CommonConstant.ACTIVE);
	}

	@Override
	public List<CodeRuleLevel> getCodeRuleLevel() {
		
		return codeRuleLevelRepository.findByStatusId(CommonConstant.ACTIVE);
	}

	@Override
	public List<CodeRuleImpactArea> getCodeRuleImpactArea() {
		return codeRuleImpactAreaRepository.findByStatusId(CommonConstant.ACTIVE);
	}

	@Override
	public List<CodeRuleActionType> getCodeRuleActionType() {
		return codeRuleActionTypeRepository.findByStatusId(CommonConstant.ACTIVE);
	}

	@Override
	public List<CodeRuleDataType> getCodeRuleDataType() {
		return codeRuleDataTypeRepository.findByStatusId(CommonConstant.ACTIVE);
	}


	@Override
	public List<CodeRulesDTO> getCodeRuleByReasonCode(String reasonCode) {
		List<CodeRules> codeRuleList = codeRulesRepository.findByClaimReasonDefinition_ClaimReasonCode(reasonCode);
		List<CodeRulesDTO> codeRulesDTOList = null;
		if(codeRuleList != null) {
			codeRulesDTOList = codeRuleList.stream().map(data -> {
				CodeRulesDTO codeRuleDTO = new CodeRulesDTO();
				BeanUtils.copyProperties(data, codeRuleDTO);
				
				if(data.getClaimReasonDefinition() != null) {
					ClaimReasonDefinitionDTO reasondefinitonDTO = new ClaimReasonDefinitionDTO();
					reasondefinitonDTO.setClaimReasonCode(data.getClaimReasonDefinition().getClaimReasonCode());
					reasondefinitonDTO.setClaimReasonDescription(data.getClaimReasonDefinition().getClaimReasonDescription());
					reasondefinitonDTO.setClaimReasonId(data.getClaimReasonDefinition().getClaimReasonId());
					reasondefinitonDTO.setCreatedByUserId(data.getClaimReasonDefinition().getCreatedByUserId());
					codeRuleDTO.setClaimReasonDefinitionDTO(reasondefinitonDTO);
				}
				
				if(data.getCodeRuleCategory() != null) {
					CodeRuleCategoryDTO codeRuleCategoryDTO = new CodeRuleCategoryDTO();
					codeRuleCategoryDTO.setCodeRuleCategoryCode(data.getCodeRuleCategory().getCodeRuleCategoryCode());
					codeRuleCategoryDTO.setCodeRuleCategoryDescription(data.getCodeRuleCategory().getCodeRuleCategoryDescription());
					codeRuleCategoryDTO.setCodeRuleCategoryId(data.getCodeRuleCategory().getCodeRuleCategoryId());
					codeRuleDTO.setCodeRuleCategoryDTO(codeRuleCategoryDTO);
				}
				
				if(data.getCodeRuleActionType() != null) {
					CodeRuleActionTypeDTO codeRuleActionTypeDTO = new CodeRuleActionTypeDTO();
					codeRuleActionTypeDTO.setCodeRuleActionTypeCode(data.getCodeRuleActionType().getCodeRuleActionTypeCode());
					codeRuleActionTypeDTO.setCodeRuleActionTypeDescription(data.getCodeRuleActionType().getCodeRuleActionTypeDescription());
					codeRuleActionTypeDTO.setCodeRuleActionTypeid(data.getCodeRuleActionType().getCodeRuleActionTypeid());
					codeRuleDTO.setCodeRuleActionTypeDTO(codeRuleActionTypeDTO);
				}
				
				if(data.getCodeRuleDataType() != null) {
					CodeRuleDataTypeDTO codeRuleDataTypeDTO = new CodeRuleDataTypeDTO();
					codeRuleDataTypeDTO.setCodeRuleDataTypeCode(data.getCodeRuleDataType().getCodeRuleDataTypeCode());
					codeRuleDataTypeDTO.setCodeRuleDataTypeDescription(data.getCodeRuleDataType().getCodeRuleDataTypeDescription());
					codeRuleDataTypeDTO.setCodeRuleDataTypeid(data.getCodeRuleDataType().getCodeRuleDataTypeid());
					codeRuleDTO.setCodeRuleDataTypeDTO(codeRuleDataTypeDTO);
				}
				if(data.getCodeRuleImpactArea() != null) {
					CodeRuleImpactAreaDTO codeRuleImpactAreaDTO = new CodeRuleImpactAreaDTO();
					codeRuleImpactAreaDTO.setCodeRuleImpactAreaCode(data.getCodeRuleImpactArea().getCodeRuleImpactAreaCode());
					codeRuleImpactAreaDTO.setCodeRuleImpactAreaDescription(data.getCodeRuleImpactArea().getCodeRuleImpactAreaDescription());
					codeRuleImpactAreaDTO.setCodeRuleImpactAreaId(data.getCodeRuleImpactArea().getCodeRuleImpactAreaId());
					codeRuleDTO.setCodeRuleImpactAreaDTO(codeRuleImpactAreaDTO);
				}
				
				if(data.getCodeRuleLevel() != null) {
					CodeRuleLevelDTO codeRuleLevelDTO = new CodeRuleLevelDTO();
					codeRuleLevelDTO.setCodeRuleLevelCode(data.getCodeRuleLevel().getCodeRuleLevelCode());
					codeRuleLevelDTO.setCodeRuleLevelDescription(data.getCodeRuleLevel().getCodeRuleLevelDescription());
					codeRuleLevelDTO.setCodeRuleLevelId(data.getCodeRuleLevel().getCodeRuleLevelId());
					codeRuleDTO.setCodeRuleLevelDTO(codeRuleLevelDTO);
				}
				if(data.getCodeRuleType() != null) {
					CodeRuleTypeDTO codeRuleTypeDTO = new CodeRuleTypeDTO();
					codeRuleTypeDTO.setCodeRuleTypeId(data.getCodeRuleType().getCodeRuleTypeId());
					codeRuleTypeDTO.setCodeRuleTypeCode(data.getCodeRuleType().getCodeRuleTypeCode());
					codeRuleTypeDTO.setCodeRuleTypeDescription(data.getCodeRuleType().getCodeRuleTypeDescription());
					codeRuleDTO.setCodeRuleTypeDTO(codeRuleTypeDTO);
				}
				
				if(data.getWorkStatus() != null) {
					WorkStatusDTO workStatusDTO = new WorkStatusDTO();
					workStatusDTO.setWorkStatusId(data.getWorkStatus().getWorkStatusId());
					workStatusDTO.setWorkStatusCode(data.getWorkStatus().getWorkStatusCode());
					workStatusDTO.setWorkStatusDescription(data.getWorkStatus().getWorkStatusDescription());
					codeRuleDTO.setWorkStatusDTO(workStatusDTO);
				}
				
				if(data.getCodeRuleDataList() != null) {
					CodeRuleDataListDTO dataListDTO = new CodeRuleDataListDTO();
					
					String dataListValue=data.getCodeRuleDataList().stream().map(x->x.getDataListValue()).collect(Collectors.joining(","));
					dataListDTO.setDataListValue(dataListValue);
					codeRuleDTO.setCodeRuleDataListDTO(dataListDTO);
				}
				
				return codeRuleDTO;
			}).collect(Collectors.toList());
		}else {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record not found with reasoncode:: " + reasonCode);
		}
		return codeRulesDTOList;
	}

	@Override
	public void deleteCodeRuleById(Integer codeRuleId) {
		Optional<CodeRules> codeRule = codeRulesRepository.findById(codeRuleId);
		if(codeRule.isPresent()) {
			codeRuleDataListRepository.deleteCodeRuleDataListBycodeRuleId(codeRule.get().getCodeRuleId());
			codeRulesRepository.delete(codeRule.get());
		}else {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record not found with codeRuleId:: " + codeRuleId);
		}
	}

	@Override
	public List<ViewAndExportDTO> viewAndExportRule(String reasonCode) {
		List<CodeRules> codeRulesList = codeRulesRepository
				.findByClaimReasonDefinition_ClaimReasonCode(reasonCode);
		List<ViewAndExportDTO> response = null;
		if (codeRulesList != null && codeRulesList.size() > 0) {
			response = codeRulesList.stream().map(data -> {
				ViewAndExportDTO viewAndExportDTO = new ViewAndExportDTO();
				viewAndExportDTO.setCodeRuleId(data.getCodeRuleId());
				viewAndExportDTO.setCodeDescription(data.getClaimReasonDefinition().getClaimReasonDescription());
				viewAndExportDTO.setRuleDescription(data.getRuleDescription());
				viewAndExportDTO.setAllowOverride(data.isAllowOverride());
				viewAndExportDTO.setApprovalRequired(data.isApprovalRequired());
				viewAndExportDTO.setRuleCategory(data.getCodeRuleCategory().getCodeRuleCategoryDescription());
				viewAndExportDTO.setRuleLevel(data.getCodeRuleLevel().getCodeRuleLevelDescription());
				viewAndExportDTO.setAllowanceToKeep(data.getClaimReasonDefinition().isAllowanceToKeep());
				viewAndExportDTO.setServiceToKeep(data.getClaimReasonDefinition().isServiceToKeep());
				viewAndExportDTO.setAreaOfImpact(data.getCodeRuleImpactArea().getCodeRuleImpactAreaDescription());
				viewAndExportDTO.setCreatedByUserId(data.getCreatedByUserId());
				viewAndExportDTO.setModifiedByUserId(data.getModifiedByUserId());
				
				viewAndExportDTO.setDisplayMessage(data.getSuccessMessage() != null && !data.getSuccessMessage().isEmpty() ? data.getSuccessMessage() : "");
				viewAndExportDTO.setDisplayMessage(data.getFailureMessage() != null && !data.getFailureMessage().isEmpty() && viewAndExportDTO.getDisplayMessage() != "" ? data.getFailureMessage() : "");
				
				return viewAndExportDTO;
			}).collect(Collectors.toList());
		} else {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record not found with reasonCode:: " + reasonCode);
		}
		return response;
	}

}
