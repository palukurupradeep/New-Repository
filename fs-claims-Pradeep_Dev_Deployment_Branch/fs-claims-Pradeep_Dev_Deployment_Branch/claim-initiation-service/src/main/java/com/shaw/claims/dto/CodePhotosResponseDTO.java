package com.shaw.claims.dto;

import lombok.Data;

@Data
public class CodePhotosResponseDTO {

	private int codePhotoId;
	private String claimReasonCode;
	private Integer claimReasonId;
	private String claimReasonDescription;
	private Integer photoId;
	private String photoName;
	private String photoDescription;

}
