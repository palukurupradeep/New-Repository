package com.shaw.claims.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class SalesCustomerDetailsDTO {

	private String customerNumber;

	private String customerName;

	private String customerType;

	private String customerReferenceNumber;

	private String customerReferenceNumberDesc;

	private String storeType;

	private String storeTypeDesc;

	private String storeTypeGroupCode;

	private String groupAccount;

	private String ccaAccount;

	private String alignedAccount;

	private String insideSalesGroup;

	private String homeCenter;

	private String sfnActivateDate;

	private String sfnDeactivateDate;

	private String primarySellingCompany;

	private String primaryTerritory;

	private String comments;

	private String shipCode;

	private String shipCodeName;

	private String shipAuthorizationRequired;

	private String customerBroker;

	private String customerBrokerName;

	private String eSPCustomer;

}
