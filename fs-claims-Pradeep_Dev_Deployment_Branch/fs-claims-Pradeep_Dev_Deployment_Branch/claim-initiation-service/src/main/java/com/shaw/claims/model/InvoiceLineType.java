package com.shaw.claims.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "invoicelinetype", schema = "clm")
@Data
public class InvoiceLineType extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoicelinetypeid")
    private int invoiceLineTypeId;

    @Column(name = "invoicelinetypecode")
    private String invoiceLineTypeCode;

    @Column(name = "invoicelinetypedescription")
    private String invoiceLineTypeDescription;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;

}