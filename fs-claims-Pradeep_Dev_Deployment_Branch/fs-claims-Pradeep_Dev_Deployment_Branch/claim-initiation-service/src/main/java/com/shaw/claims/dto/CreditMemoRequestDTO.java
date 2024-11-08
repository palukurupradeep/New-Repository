package com.shaw.claims.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreditMemoRequestDTO {

    private String sellingCompanyCode;
    private Boolean isReturnedFullAmount;
    private List<Integer> claimLineId;

}
