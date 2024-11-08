package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RGARequestDTO {
    private String continueFrom;
    private Integer claimId;
    private BigDecimal defectOfPercentage;
    private List<RGARequestLineDetailsDTO> requestLineDetailsDTOS;
    private String loggedInUser;
}
