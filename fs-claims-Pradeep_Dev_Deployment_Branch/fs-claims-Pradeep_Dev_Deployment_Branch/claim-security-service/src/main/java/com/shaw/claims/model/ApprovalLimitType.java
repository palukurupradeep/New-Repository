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
@Table(name = "approvallimittype", schema = "clm")
public class ApprovalLimitType extends BaseEntity{
	@Id
	@Column(name = "approvallimittypeid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int approvalLimitTypeId;
	@Column(name = "approvallimittypecode")
	private String approvalLimitTypeCode;
	@Column(name = "approvallimittypedescription")
	private String approvalLimitTypeDescription;
	@Column(name = "statusid")
	private int statusId = 1;

}
