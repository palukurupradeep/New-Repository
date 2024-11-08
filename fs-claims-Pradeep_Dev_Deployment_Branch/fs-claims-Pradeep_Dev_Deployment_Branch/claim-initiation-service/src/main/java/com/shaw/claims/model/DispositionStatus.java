package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "dispositionstatus", schema = "clm")
@Data
public class DispositionStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dispositionstatusid")
	private int dispositionStatusId;
	
	@Column(name = "dispositionstatuscode")
	private String dispositionStatusCode;
	
	@Column(name = "dispositionstatusdescription")
	private String dispositionStatusDescription;
	
	@Column(name = "displaysequence")
	private int displaySequence;
	
	@Column(name = "statusid")
	private int statusId;
}
