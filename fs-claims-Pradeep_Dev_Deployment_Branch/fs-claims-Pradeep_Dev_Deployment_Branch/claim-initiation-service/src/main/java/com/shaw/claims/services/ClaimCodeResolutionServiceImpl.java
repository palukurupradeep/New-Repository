package com.shaw.claims.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.dto.ClaimCodeResolutionDTO;
import com.shaw.claims.dto.ClaimResolutionDTO;
import com.shaw.claims.dto.CodeResolutionCreateDTO;
import com.shaw.claims.enums.StatusTypes;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.ClaimCodeResolution;
import com.shaw.claims.model.ClaimReasonDefinition;
import com.shaw.claims.model.ClaimResolution;
import com.shaw.claims.repo.ClaimCodeResolutionRepository;
import com.shaw.claims.repo.ClaimReasonDefinitionRepository;
import com.shaw.claims.repo.ClaimResolutionRepository;
import com.shaw.claims.repo.StatusRepository;

@Service
public class ClaimCodeResolutionServiceImpl implements ClaimCodeResolutionService {
	Logger log = LogManager.getLogger(ClaimCodeResolutionServiceImpl.class);
	@Autowired
	ClaimCodeResolutionRepository repo;

	@Autowired
	ClaimResolutionRepository resolutionRepo;

	@Autowired
	ClaimReasonDefinitionRepository reasonDefinitionRepo;

	@Autowired
	StatusRepository statusRepo;

	@Override
	public List<ClaimResolution> getAllClaimResolutions() {
		return resolutionRepo.findAllClaimResolution();
	}

	@Override
	public ClaimCodeResolution createCodeResolution(CodeResolutionCreateDTO request) {

		 ClaimCodeResolution claimCodeResolution = new ClaimCodeResolution();
		if (request.getResolutionId() != null) {
			ClaimResolution resolution = resolutionRepo.findByResolutionId(request.getResolutionId());
			claimCodeResolution.setClaimResolution(resolution);
		}

		ClaimReasonDefinition reasonDefinition = reasonDefinitionRepo
				.findByClaimReasonId(request.getClaimReasonId());
		if (reasonDefinition != null) {
			claimCodeResolution.setClaimReasonDefinition(reasonDefinition);
		}

		int statusId = statusRepo.findStatusIdByStatusCode("Active");
		claimCodeResolution.setStatus(statusId);
		claimCodeResolution.setCreatedByUserId(request.getCreatedByUserId());
		claimCodeResolution.setModifiedByUserId(0);
		claimCodeResolution.setCreatedDateTime(LocalDateTime.now());
		claimCodeResolution = repo.save(claimCodeResolution);
		
		return claimCodeResolution;
	}

	@Override
	public void deleteCodeResolution(int codeResolutionId) {

		Optional<ClaimCodeResolution> codeResolution = repo.findByCodeResolutionId(codeResolutionId);
		if (codeResolution.isPresent()) {
			repo.deleteById(codeResolutionId);
		} else {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found with CodeResolutionId :: " + codeResolutionId);
		}

	}

	@Override
	public ClaimResolution addOrUpdateClaimResolution(ClaimResolutionDTO request) {
		ClaimResolution resolutionResponse = new ClaimResolution();
		BeanUtils.copyProperties(request, resolutionResponse);
		if(request.getResolutionId() != null) {
			ClaimResolution claimResolution = resolutionRepo.findByResolutionId(request.getResolutionId());
			if(claimResolution == null) {
				throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
						"Record not found for resolution name :: " + request.getResolutionName());
			}
			resolutionResponse.setCreatedByUserId(claimResolution.getCreatedByUserId());
			resolutionResponse.setModifiedByUserId(request.getModifiedByUserId());
			resolutionResponse.setModifiedDateTime(LocalDateTime.now());
			resolutionResponse.setStatus(claimResolution.getStatus());
			
		}else {
			Optional<ClaimResolution> claimResolution = resolutionRepo.findByResolutionName(request.getResolutionName());
			if(claimResolution.isPresent()) {
				throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
						"Duplicate record with resolution name :: " + request.getResolutionName());
			}	
			resolutionResponse.setCreatedByUserId(request.getCreatedByUserId());
			resolutionResponse.setCreatedDateTime(LocalDateTime.now());
			int statusId = statusRepo.findStatusIdByStatusCode("Active");
			resolutionResponse.setStatus(statusId);
		}
		
		
		resolutionResponse = resolutionRepo.save(resolutionResponse);
		return resolutionResponse;
	}

	@Override
	public void deleteClaimResolution(int claimResolutionId) {
		List<ClaimCodeResolution> claimCodeList= repo.findClaimCodeResolutionWithresolutionId(claimResolutionId);
		if(claimCodeList.size()>0)
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"This resolution is associated with reason code.To delete, remove the association from reason code.");
		
		ClaimResolution codeResolution = resolutionRepo.findByResolutionId(claimResolutionId);
		if (codeResolution != null) {
			codeResolution.setStatus(StatusTypes.DELETE.getStatusId());
			resolutionRepo.save(codeResolution);
		} else {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found with ResolutionId :: " + claimResolutionId);
		}
	}

	@Override
	public List<ClaimCodeResolutionDTO> getClaimCodeResolution(Integer reasonId) {
		List<ClaimCodeResolution> codeResolutionList =  repo.findActiveClaimCodeResolutionByReasonId(CommonConstant.ACTIVE, reasonId);
		List<ClaimCodeResolutionDTO> claimCodeResolutionList = null;
		if(codeResolutionList != null) {
			claimCodeResolutionList = codeResolutionList.stream().map(data -> {
				ClaimCodeResolutionDTO codeResolutionDTO = new ClaimCodeResolutionDTO();
				codeResolutionDTO.setCodeResolutionId(data.getCodeResolutionId());
				codeResolutionDTO.setReasonCode(data.getClaimReasonDefinition().getClaimReasonCode());
				codeResolutionDTO.setReasonDefinitonId(data.getClaimReasonDefinition().getClaimReasonId());
				codeResolutionDTO.setReasonDescription(data.getClaimReasonDefinition().getClaimReasonDescription());
				codeResolutionDTO.setCreatedByUserId(data.getCreatedByUserId());
				codeResolutionDTO.setModifiedByUserId(data.getModifiedByUserId());
				codeResolutionDTO.setStatus(data.getStatus());
				
				ClaimResolutionDTO claimResolutionDTO = new ClaimResolutionDTO();
				claimResolutionDTO.setResolutionId(data.getClaimResolution().getResolutionId());
				claimResolutionDTO.setResolutionName(data.getClaimResolution().getResolutionName());
				claimResolutionDTO.setProcedureManual(data.getClaimResolution().getProcedureManual());
				claimResolutionDTO.setDescription(data.getClaimResolution().getDescription());
				claimResolutionDTO.setCreatedByUserId(data.getCreatedByUserId());
				claimResolutionDTO.setModifiedByUserId(data.getClaimResolution().getModifiedByUserId());
				codeResolutionDTO.setClaimResolution(claimResolutionDTO);
				return codeResolutionDTO;
			}).collect(Collectors.toList());
		}
		return claimCodeResolutionList;
	}
}
