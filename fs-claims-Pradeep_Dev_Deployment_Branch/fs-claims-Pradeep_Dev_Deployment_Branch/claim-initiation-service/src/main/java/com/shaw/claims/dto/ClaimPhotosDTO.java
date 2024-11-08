package com.shaw.claims.dto;

import lombok.Data;

@Data
public class ClaimPhotosDTO {

	private Integer photoId;
	private String photoName;
	private String description;
	private byte[] photo;
}
