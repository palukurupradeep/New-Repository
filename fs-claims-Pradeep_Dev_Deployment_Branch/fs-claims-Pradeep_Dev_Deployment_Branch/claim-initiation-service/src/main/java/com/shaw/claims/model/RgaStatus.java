package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "rgastatus", schema = "clm")
@Data
public class RgaStatus  extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rgastatusid")
	private int rgaStatusId;
	
	@Column(name = "rgastatuscode")
	private String rgaStatusCode;
	
	@Column(name = "rgastatusdescription")
	private String rgaStatusDescription;
	
	@Column(name = "displaysequence")
	private int DisplaySequence;
	
	@Column(name = "statusid")
	private int statusId;
}
