package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "coderulelevel", schema = "clm")
@Data
public class CodeRuleLevel extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coderulelevelid")
	private int codeRuleLevelId;
	@Column(name = "coderulelevelcode")
	private String codeRuleLevelCode;
	@Column(name = "coderuleleveldescription")
	private String codeRuleLevelDescription;
	@Column(name = "displaysequence")
	private int displaySequence;
	@Column(name = "statusid")
	private int statusId;
	
	
}
