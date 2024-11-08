package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.NoteTemplateSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "audithistory", schema = "clm")
@Data
public class AuditHistory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "audithistoryid")
	private int auditHistoryId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JsonSerialize(using = NoteTemplateSerializer.class)
	@JoinColumn(name = "notetemplateid")
	private NoteTemplate noteTemplate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "claimid")
	@JsonIgnore
	private Claim claim;

	@Column(name = "audithistorytext")
	private String auditHistoryText;

	@Column(name = "statusid")
	private int statusId;
}
