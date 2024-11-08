package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "instoreclaim", schema = "clm")
@Data
public class InstoreClaim extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instoreclaimid")
    private int instoreClaimId;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "claimid")
    private Claim claim;

    @Column(name = "claimnumber")
    private String claimNumber;

    @Column(name = "claimdate")
    private Date claimDate;

    @Column(name = "claimtype")
    private String claimType;

    @Column(name = "customernumber")
    private String customerNumber;

    @Column(name = "sellingcompany")
    private String sellingCompany;

    @Column(name = "territorymanager")
    private String territoryManager;

    @Column(name = "region")
    private String region;

    @Column(name = "division")
    private String division;

    @Column(name = "currentterritory")
    private String currentTerritory;

    @Column(name = "salesreviewstatuscode")
    private String salesReviewStatusCode;

    @Column(name = "territorystatuscode")
    private String territoryStatusCode;

    @Column(name = "soldbyterritory")
    private String soldByTerritory;

    @Column(name = "divisionvicepresident")
    private String divisionVicePresident;

    @Column(name = "divisionstatuscode")
    private String divisionStatusCode;

    @Column(name = "divisionreasoncode")
    private String divisionReasonCode;

    @Column(name = "regionvicepresident")
    private String regionVicePresident;

    @Column(name = "regionstatuscode")
    private String regionStatusCode;

    @Column(name = "regionreasoncode")
    private String regionReasonCode;

    @Column(name = "extentionflag")
    private boolean extensionFlag;

    @Column(name = "duedate")
    private Date dueDate;

    @Column(name = "multipleterritoryflag")
    private boolean multipleTerritoryFlag;

}

