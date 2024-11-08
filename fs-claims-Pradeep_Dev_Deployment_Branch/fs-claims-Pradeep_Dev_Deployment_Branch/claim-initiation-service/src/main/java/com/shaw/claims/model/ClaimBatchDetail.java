package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.DetailTypeSerializer;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "claimbatchdetail", schema = "CLM")
@Data
public class ClaimBatchDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claimbatchdetailid")
    private int claimBatchDetailId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "claimbatchheaderid")
    private ClaimBatchHeader claimBatchHeader;

    @Column(name = "customernumber")
    private String customerNumber;

    @OneToOne
    @JoinColumn(name = "claimreasonid", referencedColumnName = "claimreasonid")
    private ClaimReasonDefinition claimReasonDefinition;

    @Column(name = "invoicenumber")
    private String invoiceNumber;

    @Column(name = "invoicedate")
    private LocalDateTime invoiceDate;

    @Column(name = "stylenumber")
    private String styleNumber;

    @Column(name = "colornumber")
    private String colorNumber;

    @Column(name = "claimamountusd")
    private BigDecimal claimAmountUsd;

    @Column(name = "claimnote")
    private String claimNote;

    @Column(name = "claimid")
    private int claimId;

    @Column(name = "creditmemoid")
    private int creditMemoId;

    @Column(name = "incentiveid")
    private String incentiveId;

    @Column(name = "associateinvoicenumber")
    private String associateInvoiceNumber;

    @Column(name = "associateinvoicedate")
    private LocalDateTime associateInvoiceDate;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @OneToOne
    @JoinColumn(name = "unitofmeasureid", referencedColumnName = "unitofmeasureid")
    private UnitOfMeasure unitOfMeasure;

    @Column(name = "unitpriceusd")
    private BigDecimal unitPriceUsd;

    @Column(name = "createdbyuserid")
    private int createdByUserId;

    @Column(name = "processedbyuserid")
    private int processedByUserId;

    @Column(name = "createddatetime")
    private LocalDateTime createdDateTime = LocalDateTime.now();

    @Column(name = "processeddatetime")
    private LocalDateTime processedDateTime = LocalDateTime.now();
}

