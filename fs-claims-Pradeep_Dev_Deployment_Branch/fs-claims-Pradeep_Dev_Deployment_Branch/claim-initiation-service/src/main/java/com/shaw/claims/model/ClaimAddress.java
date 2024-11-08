package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "claimaddress", schema = "clm")
@Data
public class ClaimAddress extends  BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claimaddressid")
    private int claimAddressId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "claimid")
    private Claim claim;

    @Column(name = "addresstypeid")
    private int addressTypeId;

    @Column(name = "firstname")
    private String firstName = "";

    @Column(name = "middleinitial")
    private String middleInitial = "";

    @Column(name = "lastname")
    private String lastName = "";

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "addressline1")
    private String addressLine1;

    @Column(name = "addressline2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "stateid")
    private int stateId;

    @Column(name = "countryid")
    private int countryId;

    @Column(name = "postalcode")
    private String postalCode;

    @Column(name = "phonedialcodeid")
    private int phoneDialCodeId = 0;

    @Column(name = "phonenumber")
    private String phoneNumber = "";

    @Column(name = "phoneextension")
    private String phoneExtension = "";

    @Column(name = "faxdialcodeid")
    private int faxDialCodeId = 0;

    @Column(name = "faxnumber")
    private String faxNumber = "";

    @Column(name = "faxextension")
    private String faxExtension = "";

    @Column(name = "cellphonedialcodeid")
    private int cellPhoneDialCodeId = 0;

    @Column(name = "cellphonenumber")
    private String cellPhoneNumber = "";

    @Column(name = "cellphoneextension")
    private String cellPhoneExtension = "";

    @Column(name = "emailaddress")
    private String emailAddress = "";

    @Column(name = "claimreference")
    private String claimReference = "";

    @Column(name = "statusid")
    private int statusId = 1;
}

