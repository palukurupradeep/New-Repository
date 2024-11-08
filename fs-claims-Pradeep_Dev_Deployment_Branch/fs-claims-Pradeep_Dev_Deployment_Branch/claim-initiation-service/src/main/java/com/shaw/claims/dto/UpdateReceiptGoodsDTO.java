package com.shaw.claims.dto;

import java.util.List;

import lombok.Data;

@Data
public class UpdateReceiptGoodsDTO {

	private Integer claimRgaHeaderId;

	private List<ClaimRgaLineDTO> claimRgaLineDTO;

}
