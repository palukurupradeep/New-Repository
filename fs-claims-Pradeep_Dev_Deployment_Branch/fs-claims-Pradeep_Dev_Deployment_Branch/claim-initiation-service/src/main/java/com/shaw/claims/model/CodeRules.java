package com.shaw.claims.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "coderules", schema = "clm")
@Data
public class CodeRules extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coderuleid")
	private int codeRuleId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "claimreasonid", referencedColumnName = "claimreasonid")
	private ClaimReasonDefinition claimReasonDefinition;

	@Column(name = "ruledescription")
	private String ruleDescription;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coderulecategoryid", referencedColumnName = "coderulecategoryid")
	private CodeRuleCategory codeRuleCategory;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coderuletypeid", referencedColumnName = "coderuletypeid")
	private CodeRuleType codeRuleType;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coderulelevelid", referencedColumnName = "coderulelevelid")
	private CodeRuleLevel codeRuleLevel;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coderuleimpactareaid", referencedColumnName = "coderuleimpactareaid")
	private CodeRuleImpactArea codeRuleImpactArea;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coderuleactiontypeid", referencedColumnName = "coderuleactiontypeid")
	private CodeRuleActionType codeRuleActionType;

	@Column(name = "actiondescription")
	private String actionDescription;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coderuledatatypeid", referencedColumnName = "coderuledatatypeid")
	private CodeRuleDataType codeRuleDataType;

	@Column(name = "successmessage")
	private String successMessage;
	@Column(name = "failuremessage")
	private String failureMessage;
	@Column(name = "allowoverride")
	private boolean allowOverride;
	@Column(name = "approvalrequired")
	private boolean approvalRequired;
	@Column(name = "updateworkstatus")
	private boolean updateWorkStatus;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workstatusid", referencedColumnName = "workstatusid")
	private WorkStatus workStatus;

	@Column(name = "workstatusremarks")
	private String workStatusRemarks;

	@Column(name = "statusid")
	private int statusId;

	@OneToMany(mappedBy = "codeRules", fetch = FetchType.LAZY)
	private List<CodeRuleDataList> codeRuleDataList;

}
