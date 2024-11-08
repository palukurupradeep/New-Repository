package com.shaw.claims.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import lombok.Data;

@Data
public class TicCalculation {

	private List<LineDetailsEstimateInfo> lineDetails;
	private AtomicReference<BigDecimal>  total;
}
