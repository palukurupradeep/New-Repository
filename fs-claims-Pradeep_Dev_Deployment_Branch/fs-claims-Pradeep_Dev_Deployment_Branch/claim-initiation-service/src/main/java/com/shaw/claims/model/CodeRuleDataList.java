package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "coderuledatalist", schema = "clm")
@Data
public class CodeRuleDataList extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "datalistid")
	private int dataListId;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "coderuleid")
	private CodeRules codeRules;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "datatypeid", referencedColumnName = "coderuledatatypeid")
	private CodeRuleDataType codeRuleDataType;

	@Column(name = "datalistvalue")
    private String dataListValue;
	@Column(name = "statusid")
    private int statusid=1;
}
