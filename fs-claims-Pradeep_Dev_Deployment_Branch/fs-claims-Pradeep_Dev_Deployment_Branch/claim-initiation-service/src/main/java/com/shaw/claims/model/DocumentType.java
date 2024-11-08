package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "documenttype", schema = "clm")
@Data
public class DocumentType extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "documenttypeid")
    private int documentTypeId;

    @Column(name = "documenttypecode")
    private String documentTypeCode;

    @Column(name = "documenttypedescription")
    private String documentTypeDescription;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;

}
