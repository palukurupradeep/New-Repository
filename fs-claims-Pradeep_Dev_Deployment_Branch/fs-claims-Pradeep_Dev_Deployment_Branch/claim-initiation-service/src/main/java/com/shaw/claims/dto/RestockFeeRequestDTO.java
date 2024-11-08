package com.shaw.claims.dto;

import java.util.List;

import lombok.Data;

@Data
public class RestockFeeRequestDTO {

    private String sellingCompanyCode;
   // private List<Integer> claimLineId;
    private List<CRMRequestLineDetailsDTO> requestLineDetailsDTOS;
}
