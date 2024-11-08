package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "inspectionstatus", schema = "clm")
@Data
public class InspectionStatus  extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inspectionstatusid")
	private int inspectionStatusId;
	
	@Column(name = "inspectionstatuscode")
	private String inspectionStatusCode;
	
	@Column(name = "inspectionstatusdescription")
	private String inspectionStatusDescription;
	
	@Column(name = "displaysequence")
	private int displaySequence;
	
	@Column(name = "statusid")
	private int statusId;
	

}
