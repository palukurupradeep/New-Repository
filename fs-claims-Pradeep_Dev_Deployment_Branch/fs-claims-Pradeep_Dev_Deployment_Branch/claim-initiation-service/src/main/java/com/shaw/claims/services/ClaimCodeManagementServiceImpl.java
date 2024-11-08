package com.shaw.claims.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.dto.AnalyzeLineDTO;
import com.shaw.claims.dto.ClaimPhotosDTO;
import com.shaw.claims.dto.ClaimResolutionDTO;
import com.shaw.claims.dto.CodePhotosDTO;
import com.shaw.claims.dto.CodePhotosResponseDTO;
import com.shaw.claims.enums.StatusTypes;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.ClaimPhotos;
import com.shaw.claims.model.ClaimReasonDefinition;
import com.shaw.claims.model.ClaimResolution;
import com.shaw.claims.model.CodePhotos;
import com.shaw.claims.model.CodeRuleReturnType;
import com.shaw.claims.repo.ClaimCodeResolutionRepository;
import com.shaw.claims.repo.ClaimPhotosRepository;
import com.shaw.claims.repo.ClaimReasonDefinitionRepository;
import com.shaw.claims.repo.ClaimResolutionRepository;
import com.shaw.claims.repo.CodePhotosRepository;
import com.shaw.claims.repo.CodeRuleReturnTypeRepository;

@Service
public class ClaimCodeManagementServiceImpl implements ClaimCodeManagementService {
	@Autowired
	ClaimPhotosRepository claimPhotosRepository;
	@Autowired
	CodePhotosRepository codePhotosRepository;
	@Autowired
	ClaimReasonDefinitionRepository claimReasonDefinitionRepository;
	@Autowired
	CodeRuleReturnTypeRepository codeRuleReturnTypeRepository;
	@Autowired
	ClaimResolutionRepository claimResolutionRepository;
	@Autowired
	ClaimCodeResolutionRepository claimCodeResolutionRepository;

	@Override
	public List<ClaimPhotos> getClaimPhotos() {

		return claimPhotosRepository.findActiveClaimPhotos();
	}

	@Override
	public ClaimPhotos saveorUpdateClaimPhotos(ClaimPhotos claimPhotos) {
		claimPhotos.setStatusId(StatusTypes.ACTIVE.getStatusId());
		return claimPhotosRepository.save(claimPhotos);
	}

	@Override
	public void deleteClaimPhotos(Integer photoId) {
	List<CodePhotos>codePhotos=codePhotosRepository.findCodePhotosWithphotoId(photoId);
	if(codePhotos.size()>0)
		throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
				"This photo is associated with reason code.To delete,remove the association from reason code.");
	
		Optional<ClaimPhotos> claimPhotos = claimPhotosRepository.findById(photoId);
		if (claimPhotos.isPresent()) {
			claimPhotos.get().setStatusId(StatusTypes.DELETE.getStatusId());
			claimPhotosRepository.save(claimPhotos.get());
		} else {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found with photoId :: " + photoId);
		}
	}

	@Override
	public CodePhotosResponseDTO saveCodePhotos(CodePhotosDTO codePhotos) {

		ClaimReasonDefinition claimReasonDef = claimReasonDefinitionRepository
				.findByClaimReasonId(codePhotos.getClaimReasonDefinitionDTO().getClaimReasonId());
		ClaimPhotos claimPhoto = claimPhotosRepository.findByphotoId(codePhotos.getClaimPhotosDTO().getPhotoId());
		CodePhotosResponseDTO codePhotoResp = new CodePhotosResponseDTO();
		CodePhotos codephotos = new CodePhotos();
		codephotos.setClaimReasonDefinition(claimReasonDef);
		codephotos.setClaimPhotos(claimPhoto);
		codephotos.setCreatedByUserId(codePhotos.getCreatedByUserId());
		codephotos.setModifiedByUserId(codePhotos.getModifiedByUserId());
		codephotos.setStatusId(StatusTypes.ACTIVE.getStatusId());
		CodePhotos res = codePhotosRepository.save(codephotos);
		codePhotoResp.setCodePhotoId(res.getCodePhotoId());
		codePhotoResp.setClaimReasonId(res.getClaimReasonDefinition().getClaimReasonId());
		codePhotoResp.setClaimReasonCode(res.getClaimReasonDefinition().getClaimReasonCode());
		codePhotoResp.setClaimReasonDescription(res.getClaimReasonDefinition().getClaimReasonDescription());
		codePhotoResp.setPhotoId(res.getClaimPhotos().getPhotoId());
		codePhotoResp.setPhotoName(res.getClaimPhotos().getPhotoName());
		codePhotoResp.setPhotoDescription(res.getClaimPhotos().getPhotoDescription());
		return codePhotoResp;
	}

	@Override
	public void deleteCodePhotos(Integer codePhotoId) {
		Optional<CodePhotos> codePhoto = codePhotosRepository.findById(codePhotoId);
		if (codePhoto.isPresent()) {
			codePhotosRepository.deleteById(codePhotoId);
		} else {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found with codePhotoId :: " + codePhotoId);
		}
	}

	@Override
	public List<CodeRuleReturnType> getCodeRuleReturnType() {

		return codeRuleReturnTypeRepository.findByStatusId(CommonConstant.ACTIVE);
	}

	@Override
	public List<CodePhotosResponseDTO> getClaimCodePhotos(Integer reasonId) {
		List<CodePhotosResponseDTO> codePhotosResponse = new ArrayList<>();
		for (CodePhotos codePhoto : codePhotosRepository.findAll()) {
			CodePhotosResponseDTO codePhotoResp = new CodePhotosResponseDTO();
			ClaimReasonDefinition claimReasonDef = claimReasonDefinitionRepository
					.findByClaimReasonId(reasonId);
			ClaimPhotos claimPhoto = claimPhotosRepository.findByphotoId(codePhoto.getClaimPhotos().getPhotoId());
			if(claimReasonDef != null) {
				codePhotoResp.setCodePhotoId(codePhoto.getCodePhotoId());
				codePhotoResp.setClaimReasonId(claimReasonDef.getClaimReasonId());
				codePhotoResp.setClaimReasonCode(claimReasonDef.getClaimReasonCode());
				codePhotoResp.setClaimReasonDescription(claimReasonDef.getClaimReasonDescription());
				codePhotoResp.setPhotoId(claimPhoto.getPhotoId());
				codePhotoResp.setPhotoName(claimPhoto.getPhotoName());
				codePhotoResp.setPhotoDescription(claimPhoto.getPhotoDescription());
				codePhotosResponse.add(codePhotoResp);
			}
		}
		return codePhotosResponse;
	}

	@Override
	public AnalyzeLineDTO fetchResolutionsAndPhotosByReasonId(Integer reasonId) {
		List<Integer> codePhotosIdsList = codePhotosRepository.findCodePhotosByReasonId(reasonId);
		AnalyzeLineDTO analyzeDto = new AnalyzeLineDTO();

		List<ClaimPhotosDTO> photoDtoList = new ArrayList<>();
		for (Integer photoId : codePhotosIdsList) {

			ClaimPhotosDTO photosdto = new ClaimPhotosDTO();

			ClaimPhotos claimPhoto = claimPhotosRepository.findByphotoId(photoId);
			photosdto.setPhotoId(claimPhoto.getPhotoId());
			photosdto.setPhotoName(claimPhoto.getPhotoName());
			photosdto.setDescription(claimPhoto.getPhotoDescription());
			photosdto.setPhoto(claimPhoto.getPhoto());
			photoDtoList.add(photosdto);

		}

		analyzeDto.setClaimPhotosDTOList(photoDtoList);
		List<Integer> codeResolutionsIdsList = claimCodeResolutionRepository
				.findClaimCodeResolutionByReasonId(reasonId);
		List<ClaimResolutionDTO> resolutionDtoList = new ArrayList<>();
		for (Integer resolutionId : codeResolutionsIdsList) {
			ClaimResolution claimResolution = claimResolutionRepository.findByResolutionId(resolutionId);

			ClaimResolutionDTO resolutiondto = new ClaimResolutionDTO();
			resolutiondto.setResolutionName(claimResolution.getResolutionName());
			resolutiondto.setDescription(claimResolution.getDescription());
			resolutiondto.setProcedureManual(claimResolution.getProcedureManual());
			resolutionDtoList.add(resolutiondto);

		}
		analyzeDto.setClaimResolutionDTOList(resolutionDtoList);
		return analyzeDto;
	}

}
