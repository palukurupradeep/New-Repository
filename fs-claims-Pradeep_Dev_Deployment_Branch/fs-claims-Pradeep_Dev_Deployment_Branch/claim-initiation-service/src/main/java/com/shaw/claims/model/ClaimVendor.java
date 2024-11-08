package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "claimvendor", schema = "clm")
@Data
public class ClaimVendor extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claimvendorid")
    private int claimVendorId;

    @Column(name = "claimvendornumber")
    private String claimVendorNumber;

    @Column(name = "federalid")
    private String federalId;

    @Column(name = "psvendorid")
    private String psVendorId;

    @Column(name = "psvendorlocation")
    private String psVendorLocation;

    @Column(name = "legalname")
    private String legalName;

    @Column(name = "dbaname")
    private String dbaName;

    @Column(name = "primarycontact")
    private String primaryContact;

    @Column(name = "secondarycontact")
    private String secondaryContact;

    @Column(name = "w9receiveddate")
    private Date w9ReceivedDate;

    @Column(name = "psvendorformdate")
    private Date psVendorFormDate;

    @Column(name = "claimsprofileformdate")
    private Date claimsProfileFormDate;

    @Column(name = "profilereviewdate")
    private Date profileReviewDate;

    @Column(name = "backgroundcheckdate")
    private Date backgroundCheckDate;

    @Column(name = "latitude")
    private BigDecimal latitude;

    @Column(name = "longitude")
    private BigDecimal longitude;

    @Column(name = "phonedialcodeid")
    private int phoneDialCodeId;

    @Column(name = "phonenumber")
    private String phoneNumber;

    @Column(name = "phoneextension")
    private String phoneExtension;

    @Column(name = "celldialcodeid")
    private int cellDialCodeId;

    @Column(name = "cellnumber")
    private String cellNumber = "";

    @Column(name = "emailaddress")
    private String emailAddress;

    @Column(name = "statusid")
    private int statusId;

    @OneToMany(mappedBy = "claimVendor", cascade = CascadeType.ALL)
    private List<VendorAddress> vendorAddresses;

    @OneToMany(mappedBy = "claimVendor", cascade = CascadeType.ALL)
    private List<VendorCertificate> vendorCertificates;

    @OneToMany(mappedBy = "claimVendor", cascade = CascadeType.ALL)
    private List<VendorService> vendorServices;

    @OneToMany(mappedBy = "claimVendor", cascade = CascadeType.ALL)
    private List<VendorServiceType> vendorServiceTypes;
}