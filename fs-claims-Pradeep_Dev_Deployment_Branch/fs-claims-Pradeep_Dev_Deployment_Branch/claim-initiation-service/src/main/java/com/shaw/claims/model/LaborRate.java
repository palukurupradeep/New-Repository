package com.shaw.claims.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "claimlaborrate", schema = "clm")
@Data
public class LaborRate extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "claimlaborrateid")
	private int claimLaborRateId;

	@Column(name = "claimlaborratecode")
	private String claimLaborRateCode;

	@Column(name = "claimlaborratedescription")
	private String claimLaborRateDescription;

	@Column(name = "rate")
	private BigDecimal rate;

	@Column(name = "statusid")
	private int statusId = 1;

}
