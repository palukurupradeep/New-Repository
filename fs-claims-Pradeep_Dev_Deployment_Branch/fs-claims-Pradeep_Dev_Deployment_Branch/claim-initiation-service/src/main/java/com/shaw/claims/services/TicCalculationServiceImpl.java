package com.shaw.claims.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shaw.claims.dto.LaborRateDTO;
import com.shaw.claims.dto.LineDetailsEstimateInfo;
import com.shaw.claims.dto.TicCalculation;
import com.shaw.claims.dto.TicCalculationResponse;
import com.shaw.claims.model.ClaimLineDetail;
import com.shaw.claims.model.LaborRate;
import com.shaw.claims.repo.ClaimLineDetailsRepository;
import com.shaw.claims.repo.LaborRateRepository;

@Service
public class TicCalculationServiceImpl implements TicCalculationService {

	Logger log = LogManager.getLogger(LocationServiceImpl.class);

	@Autowired
	ClaimLineDetailsRepository claimLineDetailsRepository;
	
	@Autowired
	LaborRateRepository laborRateRepository;

	@Override
	public TicCalculationResponse ticCalculations(Integer claimId, Integer claimLineId, BigDecimal estimateAmount) {
		log.info("Claim Id :: " + claimId);
		TicCalculationResponse response = new TicCalculationResponse();
		 AtomicReference<BigDecimal> totalAmount = new AtomicReference<>(BigDecimal.ZERO);

		response.setMarchandiseTotal(calculateAmount(claimId,  15, totalAmount, claimLineId, estimateAmount));
		response.setLabor(calculateAmount(claimId,  14, totalAmount, claimLineId, estimateAmount));
		response.setOtherCharges(calculateAmount(claimId,  5, totalAmount, claimLineId, estimateAmount));
		response.setFreightResponse(calculateAmount(claimId,  9, totalAmount, claimLineId, estimateAmount));
		response.setSalesTax(calculateAmount(claimId,  21, totalAmount, claimLineId, estimateAmount));
		response.setDealerServiceFees(calculateAmount(claimId,  23, totalAmount, claimLineId, estimateAmount));
		response.setTotal(totalAmount.get());
		return response;
	}

	private TicCalculation calculateAmount(Integer claimId,
			Integer detailTypeId, AtomicReference<BigDecimal>  totalAmount, Integer claimLineId, BigDecimal estimateAmount) {
		List<ClaimLineDetail> claimLineDetails = claimLineDetailsRepository
				.findByClaim_ClaimIdAndDetailType_DetailTypeId(claimId, detailTypeId);
		TicCalculation finalResponse = new TicCalculation();
		List<LineDetailsEstimateInfo> response = null;
		AtomicReference<BigDecimal> total = new AtomicReference<>(BigDecimal.ZERO);
		if (claimLineDetails != null) {
			
			response = claimLineDetails.stream().map(data -> {
				LineDetailsEstimateInfo lineDetails = new LineDetailsEstimateInfo();
				lineDetails.setLineId(data.getClaimLineId());
				lineDetails.setLineNumber(data.getLineNumber());
				lineDetails.setReasonCode(data.getClaimReasonDefinition().getClaimReasonCode());
				lineDetails.setStyle(data.getStyleNumber());
				lineDetails.setColor(data.getColorNumber());
				lineDetails.setQuantity(data.getQuantity());
				lineDetails.setUnitOfMeasure(data.getUnitOfMeasure().getUnitOfMeasureCode());
				// lineDetails.setCreditMemoNo(data.get)
				// lineDetails.setResolution(data.getr)
				BigDecimal amount;
				if ((claimLineId != null && data.getClaimLineId() == claimLineId)
						&& (estimateAmount != null  && !data.getLineAmountUsd().equals(estimateAmount))) {
					data.setLineAmountUsd(estimateAmount);
				}
//				if (quantity != 0) {
//					amount = data.getLineAmountUsd().multiply(new BigDecimal(quantity));
//					data.setLineAmountUsd(amount);
//					//data.setQuantity(data.getQuantity());
//					// claimLineDetailsRepository.save(data);
//				} else {
					amount = data.getLineAmountUsd();
				//}
				lineDetails.setEstimateAmount(amount);
				total.updateAndGet(value -> value.add(amount));
				totalAmount.updateAndGet(value -> value.add(amount));
				return lineDetails;
			}).collect(Collectors.toList());
		}
		finalResponse.setLineDetails(response);
		finalResponse.setTotal(total);
		return finalResponse;
	}

	@Override
	public List<LaborRateDTO> findAllLaborRates() {
		List<LaborRate> laborRateList = laborRateRepository.findByStatusId(1);
		List<LaborRateDTO> response = new ArrayList<>();
		if (laborRateList != null && !laborRateList.isEmpty()) {
			response = laborRateList.stream().map(laborRates -> {
				LaborRateDTO laborRateDTO = new LaborRateDTO();
				BeanUtils.copyProperties(laborRates, laborRateDTO);
				return laborRateDTO;
			}).collect(Collectors.toList());
		}
		return response;
	}

}
