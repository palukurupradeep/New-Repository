package com.shaw.claims.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "invoicetype", schema = "clm")
@Data
public class InvoiceType extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoicetypeid")
    private int invoiceTypeId;

    @Column(name = "invoicetypecode")
    private String invoiceTypeCode;

    @Column(name = "invoicetypedescription")
    private String invoiceTypeDescription;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;

}