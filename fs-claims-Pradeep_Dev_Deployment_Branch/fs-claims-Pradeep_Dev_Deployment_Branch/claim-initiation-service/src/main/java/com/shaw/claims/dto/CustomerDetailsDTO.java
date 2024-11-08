package com.shaw.claims.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CustomerDetailsDTO {
	private String customerNumber;
	private String customerName;
	@JsonProperty("dbaName")
	private String DBAName;
	private String primaryCustNumber;
	private String primaryCustName;
	private String billsToPrimaryInd;
	private String customerStatus;
	private String inactiveDate;
	private String inactiveReason;
	private String inactiveReasonDesc;
	private String yearBusinessStarted;
	private String establishDate;
	private String reinstateDate;
	private String reinstateReason;
	private String customerType;
	private String businessType;
	private String businessTypeDesc;
	private String storeNumber;
	private String customerEmail;
	private String phoneCountryCode;
	private String phoneAreaCode;
	private String phoneNumber;
	private String phoneExtension;
	private String faxCountryCode;
	private String faxAreaCode;
	private String faxNumber;
	private String faxExtension;
	private String webPage;
	@JsonIgnore
	private String address;
	private String storeType;
	private String groupAccount;
}
