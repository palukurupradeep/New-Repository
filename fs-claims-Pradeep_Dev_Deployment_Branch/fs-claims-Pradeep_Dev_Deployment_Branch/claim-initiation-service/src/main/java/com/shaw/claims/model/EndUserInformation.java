package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "enduserinformation", schema = "clm")
@Data
public class EndUserInformation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enduserinformationid")
    private int endUserInformationId;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "claimid")
    private Claim claim;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "middleinitial")
    private char middleInitial;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "companyname")
    private String companyName="";

    @Column(name = "countryid")
    private int countryId;

    @Column(name = "addressline1")
    private String addressLine1="";

    @Column(name = "addressline2")
    private String addressLine2="";

    @Column(name = "city")
    private String city="";

    @Column(name = "stateid")
    private int stateId;

    @Column(name = "postalcode")
    private String postalCode="";

    @Column(name = "county")
    private String county="";

    @Column(name = "businessphonedialcodeid")
    private int businessPhoneDialCodeId;

    @Column(name = "businessphonenumber")
    private String businessPhoneNumber ="";

    @Column(name = "businessphoneextension")
    private String businessPhoneExtension ="";

    @Column(name = "homephonedialcodeid")
    private int homePhoneDialCodeId;

    @Column(name = "homephonenumber")
    private String homePhoneNumber="";

    @Column(name = "homephoneextension")
    private String homePhoneExtension="";

    @Column(name = "cellphonedialcodeid")
    private int cellPhoneDialCodeId;

    @Column(name = "cellphonenumber")
    private String cellPhoneNumber ="";

    @Column(name = "cellphoneextension")
    private String cellPhoneExtension ="";

    @Column(name = "emailaddress")
    private String emailAddress ="";

    @Column(name = "statusid")
    private int statusId = 1;
}
