package com.shaw.claims.services;

import com.shaw.claims.dto.CreditMemoRequestDTO;
import com.shaw.claims.dto.RestockFeeRequestDTO;
import com.shaw.claims.dto.RestockFeeResponseDTO;

import java.util.List;

public interface CreditMemoService {
    void prepareCreditMemo(CreditMemoRequestDTO creditMemoRequestDTO);


    RestockFeeResponseDTO restockFee(RestockFeeRequestDTO restockFeeRequestDTO);
}
