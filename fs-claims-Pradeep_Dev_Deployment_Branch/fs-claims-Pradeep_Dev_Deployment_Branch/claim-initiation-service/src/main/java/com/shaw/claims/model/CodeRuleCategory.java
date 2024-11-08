package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "coderulecategory", schema = "clm")
@Data
public class CodeRuleCategory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coderulecategoryid")
	private int codeRuleCategoryId;
	@Column(name = "coderulecategorycode")
	private String codeRuleCategoryCode;
	@Column(name = "coderulecategorydescription")
	private String codeRuleCategoryDescription;
	@Column(name = "requiresdatalist")
	private boolean requiresDataList;
	@Column(name = "displaysequence")
	private int displaySequence;
	@Column(name = "statusid")
	private int statusId;
}
