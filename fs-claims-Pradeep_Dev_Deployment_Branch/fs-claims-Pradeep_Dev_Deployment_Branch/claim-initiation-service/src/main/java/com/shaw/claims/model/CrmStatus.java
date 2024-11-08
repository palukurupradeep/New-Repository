package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "crmstatus", schema = "clm")
@Data
public class CrmStatus  extends BaseEntity{

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "crmstatusid")
	private int crmStatusId;
	
	@Column(name = "crmstatuscode")
	private String crmStatusCode;
	
	@Column(name = "crmstatusdescription")
	private String crmStatusDescription;
	
	@Column(name = "displaysequence")
	private int DisplaySequence;
	
	@Column(name = "statusid")
	private int statusId;
	
}
