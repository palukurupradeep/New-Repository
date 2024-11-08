package com.shaw.claims.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class AnalyzeLineDTO {

	private List<ClaimPhotosDTO> claimPhotosDTOList;
	private List<ClaimResolutionDTO> claimResolutionDTOList;
}
