package com.shaw.claims.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.LookupSerializer;
import com.shaw.claims.serialization.NoteGroupSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "notetype", schema = "clm")
@Data
public class NoteType extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notetypeid")
	private int noteTypeId;
	
	@Column(name = "notetypecode")
	private String noteTypeCode;
	
	@Column(name = "notetypedescription")
	private String noteTypeDescription;
	
	@Column(name = "displaysequence")
	private int displaySequence;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JsonSerialize(using = NoteGroupSerializer.class)
	@JoinColumn(name = "notegroupid")
	private NoteGroup noteGroup;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JsonSerialize(using = LookupSerializer.class)
	@JoinColumn(name = "privacyid")
	private Lookup lookup;
	
	@Column(name = "statusid")
	private int statusId;
	
	@Column(name = "ismanual")
	private Boolean isManual;
}
