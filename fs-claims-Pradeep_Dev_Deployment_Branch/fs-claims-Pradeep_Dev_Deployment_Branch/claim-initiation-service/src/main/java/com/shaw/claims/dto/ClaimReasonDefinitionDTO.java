package com.shaw.claims.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ClaimReasonDefinitionDTO extends BaseEntityDTO{

    private String claimReasonCode;
    private Integer claimReasonId;
    private String claimReasonDescription;
}
