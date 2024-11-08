package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.DocumentTypeSerializer;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "claimdocuments", schema = "clm")
@Data
public class ClaimDocument extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claimdocumentid")
    private int claimDocumentId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "claimid")
    private Claim claim;

    @JsonSerialize(using = DocumentTypeSerializer.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documenttypeid", referencedColumnName = "documenttypeid")
    private DocumentType documentType;

    @Column(name = "documentnumber")
    private String documentNumber = "";

    @Column(name = "isassociate")
    private boolean isAssociate;

    @OneToOne
    @JoinColumn(name = "claimreasonid", referencedColumnName = "claimreasonid")
    private ClaimReasonDefinition claimReasonDefinition;

    @Column(name = "amountusd")
    private BigDecimal amountUsd = BigDecimal.ZERO;

    @Column(name = "amountforeign")
    private BigDecimal amountForeign = BigDecimal.ZERO;

    @Column(name = "exchangerate")
    public BigDecimal exchangeRate;

    @Column(name = "ordernumber")
    private String orderNumber = "";

    @Column(name = "purchaseordernumber")
    private String purchaseOrderNumber = "";

    @Column(name = "billoflading")
    private String billofLading = "";

    @Column(name = "masterbilloflading")
    private String masterBillofLading = "";

    @Column(name = "approvedbyuserid")
    private int approvedByUserId;

    @Column(name = "approveddatetime")
    private LocalDateTime approvedDateTime;

    @Column(name = "miscellaneousreference")
    private String miscellaneousReference = "";

    @Column(name = "statusid")
    private int statusId = 1;

    @Column(name = "documentdate")
    private LocalDateTime documentDate;

    @Column(name = "shipvia")
    private String shipVia = "";

    @Column(name = "rdccity")
    private String rdcCity = "";

    @Column(name = "rdcstatecode")
    private String rdcStateCode = "";


    @Column(name = "documentstatuscode")
    private String documentStatusCode = "";

    @Column(name = "documentstatusdescription")
    private String documentStatusDescription = "";

    @OneToMany(mappedBy = "claimDocument", cascade = CascadeType.ALL)
    private List<ClaimLineDetail> claimLineDetails;

    @Transient
    private boolean priorClaim;
}
