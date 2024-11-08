package com.shaw.claims.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.NoteGroupSerializer;
import com.shaw.claims.serialization.NoteTypeSerializer;
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
@Table(name = "notetemplate", schema = "clm")
@Data
public class NoteTemplate extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notetemplateid")
	private int noteTemplateId;
	
	@Column(name = "notetemplatename")
	private String noteTemplateName;
	
	@Column(name = "notetemplatetext")
	private String noteTemplateText;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notetypeid")
	@JsonSerialize(using = NoteTypeSerializer.class)
	private NoteType noteType;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notegroupid")
	@JsonSerialize(using = NoteGroupSerializer.class)
	private NoteGroup noteGroup;
	
	@Column(name = "editable")
	private boolean editable;
	
	@Column(name = "isdefault")
	private boolean isDefault;

	@Column(name = "ismanual")
	private Boolean isManual;

	@Column(name = "audithistorytemplatetext")
	private String auditHistoryTemplateText;
	
	@Column(name = "statusid")
	private int statusId;
}
