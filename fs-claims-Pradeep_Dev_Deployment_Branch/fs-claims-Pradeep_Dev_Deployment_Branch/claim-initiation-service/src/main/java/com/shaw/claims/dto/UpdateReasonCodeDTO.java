package com.shaw.claims.dto;

import lombok.Data;

@Data
public class UpdateReasonCodeDTO {
private int claimId;
private String reasonCode;
private String sellingcompanyCode;
private int modifiedByUserId;

}
