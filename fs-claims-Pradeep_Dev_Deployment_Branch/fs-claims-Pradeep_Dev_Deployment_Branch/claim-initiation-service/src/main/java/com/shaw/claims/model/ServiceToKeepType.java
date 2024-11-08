package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="servicetokeeptype", schema = "clm")
public class ServiceToKeepType extends BaseEntity {

	@Id
	@Column(name = "servicetokeeptypeid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer serviceToKeepTypeId;

	@Column(name = "servicetokeeptypecode")
	private String serviceToKeepTypeCode;

	@Column(name = "servicetokeeptypedescription")
	private String serviceToKeepTypeDescription;

	@Column(name = "displaysequence")
	private int displaySequence;

	@Column(name = "statusid")
	private Integer statusId;
	

}
