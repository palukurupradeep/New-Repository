package com.shaw.claims.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "invoicestatus", schema = "clm")
@Data
public class InvoiceStatus extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoicestatusid")
    private int invoiceStatusId;

    @Column(name = "invoicestatuscode")
    private String invoiceStatusCode;

    @Column(name = "invoicestatusdescription")
    private String invoiceStatusDescription;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;

}