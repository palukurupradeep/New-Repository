package com.shaw.claims.services;

import java.math.BigDecimal;
import java.util.List;

import com.shaw.claims.dto.LaborRateDTO;
import com.shaw.claims.dto.TicCalculationResponse;

public interface TicCalculationService {

	TicCalculationResponse ticCalculations(Integer claimId, Integer claimLineId, BigDecimal estimateAmount);

	List<LaborRateDTO> findAllLaborRates();
	

}
