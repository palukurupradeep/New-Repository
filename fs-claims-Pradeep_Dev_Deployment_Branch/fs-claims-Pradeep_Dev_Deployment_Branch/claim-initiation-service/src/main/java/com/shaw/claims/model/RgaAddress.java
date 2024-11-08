package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "rgaaddress", schema = "clm")
@Data
public class RgaAddress extends  BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rgaaddressid")
	private int rgaAddressId;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "claimid")
	private Claim claim;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "claimrgaheaderid", referencedColumnName = "claimrgaheaderid")
	private ClaimRgaHeader claimRgaHeader;

	@Column(name = "addresstypeid")
	private int addressTypeId=0;

	@Column(name = "fullname")
	private String fullName ="";

	@Column(name = "addressline1")
	private String addressLine1 ="";

	@Column(name = "addressline2")
	private String addressLine2 ="";

	@Column(name = "city")
	private String city ="";

	@Column(name = "stateid")
	private int stateId =0;

	@Column(name = "countryid")
	private int countryId =0;

	@Column(name = "postalcode")
	private String postalCode ="";

	@Column(name = "statusid")
	private int statusId=1;
}
