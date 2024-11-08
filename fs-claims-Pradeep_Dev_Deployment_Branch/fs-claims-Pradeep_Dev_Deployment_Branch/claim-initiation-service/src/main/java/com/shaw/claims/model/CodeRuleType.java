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
@Table(name = "coderuletype", schema = "clm")
public class CodeRuleType extends BaseEntity {

	@Id
	@Column(name = "coderuletypeid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer codeRuleTypeId;
	@Column(name = "coderuletypecode")
	private String codeRuleTypeCode;
	@Column(name = "coderuletypedescription")
	private String codeRuleTypeDescription;
	@Column(name = "displaysequence")
	private int displaySequence;
	@Column(name = "statusid")
	private int statusId;
}
