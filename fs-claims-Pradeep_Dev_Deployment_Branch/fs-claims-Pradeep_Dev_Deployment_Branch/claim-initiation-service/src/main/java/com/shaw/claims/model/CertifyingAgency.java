package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "certifyingagency", schema = "clm")
@Data
public class CertifyingAgency extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "certifyingagencyid")
	private int certifyingAgencyId;

	@Column(name = "certifyingagencycode")
	private String certifyingAgencyCode;

	@Column(name = "certifyingagencyname")
	private String certifyingAgencyName;

	@Column(name = "displaysequence")
	private int displaySequence;

	@Column(name = "statusid")
	private int statusId;

}
