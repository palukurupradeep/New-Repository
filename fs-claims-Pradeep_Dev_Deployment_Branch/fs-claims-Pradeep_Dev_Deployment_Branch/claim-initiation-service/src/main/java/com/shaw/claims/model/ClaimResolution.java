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
@Table(name = "claimresolutions", schema = "clm")
public class ClaimResolution extends BaseEntity {

	@Id
	@Column(name = "resolutionid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer resolutionId;

	@Column(name = "resolutionname")
	private String resolutionName;

	@Column(name = "description")
	private String description;

	@Column(name = "proceduremanual")
	private String procedureManual;

	@Column(name = "statusid")
	private Integer status;
}
