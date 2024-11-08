package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CrmReasons {
	public String code;
	public String description;
	public String prodRestrict;
	public String apprRestrict;
	public String slcoRestrict;
	public String xlatRestrict;
	public String overrideRestrict;
}
