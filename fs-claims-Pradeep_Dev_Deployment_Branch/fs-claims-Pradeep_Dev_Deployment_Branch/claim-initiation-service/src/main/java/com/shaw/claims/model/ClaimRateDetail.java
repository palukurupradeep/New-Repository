package com.shaw.claims.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "claimratedetail", schema = "clm")
@Data
public class ClaimRateDetail extends BaseEntity{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claimratedetailid")
    private Integer claimRateDetailId;
	@Column(name = "customernumber")
    private String customerNumber;
	@Column(name = "commoncustomernumber")
    private String commonCustomerNumber;
	@Column(name = "claimreasonid")
	private int claimReasonId;
	@Column(name = "stylenumber")
    private String styleNumber;
	@Column(name = "buyinggroup")
    private String buyingGroup;
	@Column(name = "storetype")
    private String storeType;
	@Column(name = "effectivedate")
    private LocalDateTime effectiveDate = LocalDateTime.now();
	@Column(name = "overallrate")
	private BigDecimal overallRate = BigDecimal.ZERO;
	@Column(name = "deffectrate")
	private BigDecimal deffectRate = BigDecimal.ZERO;
	@Column(name = "adminrate")
	private BigDecimal adminRate = BigDecimal.ZERO;
	@Column(name = "overallsalesamount")
	private BigDecimal overallSalesAmount = BigDecimal.ZERO;
	@Column(name = "overallcreditamount")
	private BigDecimal overallCreditAmount = BigDecimal.ZERO;
	@Column(name = "deffectcreditamount")
	private BigDecimal deffectCreditAmount = BigDecimal.ZERO;
	@Column(name = "admincreditamount")
	private BigDecimal adminCreditAmount = BigDecimal.ZERO;
	@Column(name = "creditcount")
    private int creditCount;
	@Column(name = "deffectcount")
    private int deffectCount;
	@Column(name = "admincount")
    private int adminCount;
	@Column(name = "statusid")
    private int statusId;
}
