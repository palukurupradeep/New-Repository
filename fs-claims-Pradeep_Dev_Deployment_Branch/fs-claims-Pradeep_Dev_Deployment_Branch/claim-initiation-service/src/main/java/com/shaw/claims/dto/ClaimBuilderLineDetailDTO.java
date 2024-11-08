package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Data
public class ClaimBuilderLineDetailDTO extends BaseEntityDTO {
    private int claimLineId;
    private String unitOfMeasureCode;
    private String rcsCode;
    private int lineNumber;
    private int invoiceLineNumber;
    private String rollNumber;
    private String styleNumber;
    private String colorNumber;
    private String claimReasonCode;
    private String grade;
    private BigDecimal lineAmountUsd;
    private BigDecimal lineAmountForeign;
    private BigDecimal unitPriceUsd;
    private BigDecimal unitPriceForeign;
    private String pricingCurrencyCode;
    private String currencyCodeDesignation;
    private String currencyCodeName;
    private BigDecimal exchangeRate;
    private String dyeLot;
    private BigDecimal quantity;
    private String sellingCompany;
    private String productCode;
    private BigDecimal squareFeet;
    private BigDecimal salesDiscount;
    private String inventoryStyle;
    private String inventoryColor;
    private String manufacturingPlant;
    private String detailTypeCode;
    private String detailStatusTypeCode;
    private String detailStatusDescription;
    private String lineSourceCode;
    private String lineSourceReference;
    private String lineAddReasonCode;
    private String addReasonDescription;
    private String vendorId;
    private String comments;
    private Boolean payToKeep;
    private Boolean repairFlag;
    private Boolean installFlag;
    private Date installDate;
    private Boolean atkFlag;
    private Boolean tryAtkFlag;
    private Boolean stkFlag;
    private Boolean tryStkFlag;
    private String inspectionNbr;
    private String styleStatus;
    private String invType;
    private BigDecimal taxPercent;
    private BigDecimal packages;
    private BigDecimal units;
    private BigDecimal hsUnitPrice;
    private String styleOrdered;
    private String colorOrdered;
    private String styleReceived;
    private String colorReceived;
    private BigDecimal numberOfCuts;
    private BigDecimal amount;
    private String styleName;
    private Date orderDate;
    private String sampleType;
    private String sampleLabelType;
    private String sampleInvoiceSpecType;
    private String shipPoint;
    private String department;
    private String purchaseMfg;
    private Date poDate;
    private BigDecimal physicalQuantity;
    private String inventoryUom;
    private String inventoryConversionFactor;
    private String retailConversionFactor;
    private String orderOrigin;
    private String proNumber;
    private String poLineNumber;
    private int poCustomerProdId;
    private String rgaNumber;
    private Date rgaDate;
    private String rgaStatus;
    private String creditMemoNumber;
    private Date creditMemoDate;
    private ClaimSubmittalChargesDTO chargesDTO;
    private ClaimSubmittalProductSepcDTO productSepcDTO;
    private List<ClaimBuilderDetailRecordDTO> claimDetailRecordDTOS;
}