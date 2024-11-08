package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TerritoryDivisionResponse {
	public String salesResult;
	public String sfdTerritory;
	public String sfdDivTerritory;
	public String sfdRgnTerritory;
	public String sfdFirstName;
	public String sfdMiddleInitial;
	public String sfdLastName;
	public String sfdFormattedName;
	public String sfdOfficePhone;
	public String sfdTerritoryEmail;
	public String sfdDivisionOldregion;
	public String sfdRegionOlddistrict;
	public String sfdType;
	public String sfdTypeDesc;
	public String sfdTypeMapsTo;
	public String sfdTerritoryStatus;
	public String sfdTerritoryStatusDesc;
}
