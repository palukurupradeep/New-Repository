package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class SellingStyleInfo {

	public String sellStyleNumber;
	public String name;
	public String productCode;
	public String productCodeDescription;
	public String abbreviatedName;
	public String productTypeCode;
	public String primaryInventoryStyleNbr;
	public String sellingCompany;
	public String customerReferenceNbr;
	public String statusCode;
	public String sellColorNbr;
	public String colorName;
}
