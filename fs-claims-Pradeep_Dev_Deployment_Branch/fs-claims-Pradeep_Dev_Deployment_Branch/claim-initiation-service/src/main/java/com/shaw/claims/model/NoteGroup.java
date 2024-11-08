package com.shaw.claims.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.LookupSerializer;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "notegroup", schema = "clm")
@Data
public class NoteGroup extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notegroupid")
	private int noteGroupId;

	@Column(name = "notegroupcode")
	private String noteGroupCode;

	@Column(name = "notegroupdescription")
	private String noteGroupDescription;

	@OneToOne(fetch = FetchType.LAZY)
	@JsonSerialize(using = LookupSerializer.class)
	@JoinColumn(name = "privacyid")
	private Lookup lookup;
	
	@Column(name = "statusid")
	private int statusId;

}
