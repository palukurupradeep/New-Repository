package com.shaw.claims.services;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.dto.AddOrUpdateReasonDefinitionDTO;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.ClaimReasonDefinition;
import com.shaw.claims.model.CodeRuleReturnType;
import com.shaw.claims.model.Status;
import com.shaw.claims.repo.ClaimReasonDefinitionRepository;
import com.shaw.claims.repo.CodeRuleReturnTypeRepository;
import com.shaw.claims.repo.StatusRepository;
import com.shaw.claims.util.ObjectMapperUtil;

@Service
public class ClaimReasonDefinitionServiceImpl implements ClaimReasonDefinitionService {
    Logger log = LogManager.getLogger(ClaimReasonDefinitionServiceImpl.class);
    @Autowired
    ClaimReasonDefinitionRepository repo;
    @Autowired
    ClaimServiceImpl claimService;
    @Autowired
    CodeRuleReturnTypeRepository ruleDataTypeRepo;
    @Autowired
    StatusRepository statusRepo;

    @Override
    public List<ClaimReasonDefinition> getReasonCodeByCategory(Integer categoryId) {
        log.info("ClaimCategoryServiceImpl.getReasonCodeByCategory");
        return repo.getReasonCodeByCategory(categoryId);
    }

	@Override
	public AddOrUpdateReasonDefinitionDTO addReasonDefinition(AddOrUpdateReasonDefinitionDTO request) {
		ClaimReasonDefinition reasonDefinition = repo.findByClaimReasonCode(request.getClaimReasonCode());
		AddOrUpdateReasonDefinitionDTO reasonDefinitionDTO = new AddOrUpdateReasonDefinitionDTO();
		if(reasonDefinition != null) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Duplicate record with reasoncode :: " + request.getClaimReasonCode());
		}else {
			ClaimReasonDefinition createClaimReasonDefinition = new ClaimReasonDefinition();
			BeanUtils.copyProperties(request, createClaimReasonDefinition, ObjectMapperUtil.getNullPropertyNames(request));
			createClaimReasonDefinition.setClaimCategory(claimService.getClaimCategory(request.getClaimCategoryCode()));
			createClaimReasonDefinition.setCodeRuleReturnType(getCodeRuleReturnType(request.getCodeRuleReturnTypeCode()));
			createClaimReasonDefinition.setStatus(getStatus(CommonConstant.ACTIVE));
			createClaimReasonDefinition.setIsDistributionCompliance(request.getIsDistributionCompliance());
			createClaimReasonDefinition.setCreatedByUserId(request.getCreatedByUserId());
			createClaimReasonDefinition.setModifiedByUserId(request.getCreatedByUserId());
			createClaimReasonDefinition.setCreatedDateTime(LocalDateTime.now());
			reasonDefinition = repo.save(createClaimReasonDefinition);
			BeanUtils.copyProperties(reasonDefinition, reasonDefinitionDTO);
			reasonDefinitionDTO.setClaimCategoryCode(createClaimReasonDefinition.getClaimCategory().getClaimCategoryCode());
			reasonDefinitionDTO.setCodeRuleReturnTypeCode(createClaimReasonDefinition.getCodeRuleReturnType().getCodeRuleReturnTypeCode());
			reasonDefinitionDTO.setStatusId(createClaimReasonDefinition.getStatus().getStatusId());
		}
		return reasonDefinitionDTO;
	}

	private Status getStatus(int statusId) {
		Status status = statusRepo.findByStatusId(statusId);
		if(status != null) {
			return status;
		}
		return null;
	}

	private CodeRuleReturnType getCodeRuleReturnType(String codeRuleReturnTypeCode) {
		CodeRuleReturnType codeRuleReturnType = ruleDataTypeRepo.findByCodeRuleReturnTypeCode(codeRuleReturnTypeCode);
		if(codeRuleReturnType != null) {
			return codeRuleReturnType;
		}
		return null;
	}

	@Override
	public AddOrUpdateReasonDefinitionDTO updateReasonDefinition(AddOrUpdateReasonDefinitionDTO request) {
		ClaimReasonDefinition reasonDefinition = repo.findByClaimReasonId(request.getClaimReasonId());
		AddOrUpdateReasonDefinitionDTO reasonDefinitionDTO = new AddOrUpdateReasonDefinitionDTO();
		if(reasonDefinition == null) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record not found with reasoncode :: " + request.getClaimReasonCode());
		}else {
			BeanUtils.copyProperties(request, reasonDefinition, ObjectMapperUtil.getNullPropertyNames(request));
			if(request.getClaimCategoryCode() != null) {
				reasonDefinition.setClaimCategory(claimService.getClaimCategory(request.getClaimCategoryCode()));
			}
			if(request.getCodeRuleReturnTypeCode() != null) {
				reasonDefinition.setCodeRuleReturnType(getCodeRuleReturnType(request.getCodeRuleReturnTypeCode()));
			}
			reasonDefinition.setModifiedByUserId(request.getModifiedByUserId());
			reasonDefinition.setModifiedDateTime(LocalDateTime.now());
			reasonDefinition = repo.save(reasonDefinition);
			BeanUtils.copyProperties(reasonDefinition, reasonDefinitionDTO);
		}
		return reasonDefinitionDTO;
	}
}
