package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.dto.AnalyzeLineDTO;
import com.shaw.claims.dto.CodePhotosDTO;
import com.shaw.claims.dto.CodePhotosResponseDTO;
import com.shaw.claims.model.ClaimPhotos;
import com.shaw.claims.model.CodeRuleReturnType;


public interface ClaimCodeManagementService {

	public List<ClaimPhotos> getClaimPhotos();

	public CodePhotosResponseDTO saveCodePhotos(CodePhotosDTO codePhotos);

	public void deleteCodePhotos(Integer codePhotoId);

	public void deleteClaimPhotos(Integer photoId);

	public ClaimPhotos saveorUpdateClaimPhotos(ClaimPhotos claimPhotos);

	public List<CodeRuleReturnType> getCodeRuleReturnType();

	public List<CodePhotosResponseDTO> getClaimCodePhotos(Integer reasonId);

	public AnalyzeLineDTO fetchResolutionsAndPhotosByReasonId(Integer reasonId);

}
