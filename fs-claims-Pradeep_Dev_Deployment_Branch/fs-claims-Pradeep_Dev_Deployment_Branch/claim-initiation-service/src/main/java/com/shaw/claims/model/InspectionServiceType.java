package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "inspectionservicetype", schema = "clm")
@Data
public class InspectionServiceType extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inspectionservicetypeid")
	private int inspectionServiceTypeId;

	@Column(name = "inspectionservicetypecode")
	private String inspectionServiceTypeCode;

	@Column(name = "inspectionservicetypename")
	private String inspectionServiceTypeName;

	@Column(name = "displaysequence")
	private int displaySequence;

	@Column(name = "statusid")
	private int statusId;

}
