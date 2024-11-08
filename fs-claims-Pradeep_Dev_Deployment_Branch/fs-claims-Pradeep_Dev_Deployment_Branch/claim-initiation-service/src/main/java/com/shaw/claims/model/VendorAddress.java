package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vendoraddress", schema = "clm")
@Data
public class VendorAddress extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendoraddressid")
    private int vendorAddressId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "claimvendorid")
    private ClaimVendor claimVendor;

    @Column(name = "addresstypeid")
    private int addressTypeId;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "addressline1")
    private String addressLine1;

    @Column(name = "addressline2")
    private String addressLine2;

    @Column(name = "addressline3")
    private String addressLine3;

    @Column(name = "city")
    private String city;

    @Column(name = "stateid")
    private int stateId;

    @Column(name = "countryid")
    private int countryId;

    @Column(name = "county")
    private String county;

    @Column(name = "postalcode")
    private String postalCode;

    @Column(name = "statusid")
    private int statusId;
}