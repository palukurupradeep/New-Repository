package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.NoteTemplateSerializer;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "vendorcertificate", schema = "clm")
@Data
public class VendorCertificate extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendorcertificateid")
    private int vendorCertificateId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "claimvendorid")
    private ClaimVendor claimVendor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certifyingagencyid")
    private CertifyingAgency certifyingAgency;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspectionservicetypeid")
    private InspectionServiceType inspectionServiceType;

    @Column(name = "certificatenumber")
    private String certificateNumber;

    @Column(name = "expirydate")
    private Date expiryDate;

    @Column(name = "statusid")
    private int statusId;

}