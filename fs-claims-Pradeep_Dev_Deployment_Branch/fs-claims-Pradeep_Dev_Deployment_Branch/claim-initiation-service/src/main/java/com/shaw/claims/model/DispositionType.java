package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "dispositiontype", schema = "clm")
public class DispositionType extends BaseEntity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dispositiontypeid")
	private int dispositionTypeId;

    @Column(name = "dispositiontypecode")
	private String dispositionTypeCode;

    @Column(name = "dispositiontypedescription")
	private String dispositionTypeDescription;

    @Column(name = "pattern")
	private String pattern;

    @Column(name = "displaysequence")
	private int displaySequence;

    @Column(name = "statusid")
	private int statusId;
}
