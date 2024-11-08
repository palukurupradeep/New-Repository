package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "claims", schema = "clm")
@Data
public class Claim extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claimid")
    private int claimId;

    @Column(name = "claimnumber")
    private String claimNumber;

    @Column(name = "customernumber")
    private String customerNumber;

    @Column(name = "primarycustomernumber")
    private String primaryCustNumber;

    @Column(name = "storenumber")
    private String storeNumber;

    @OneToOne
    @JoinColumn(name = "claimcategoryid", referencedColumnName = "claimcategoryid")
    private ClaimCategory claimCategory;

    @Column(name = "claimuserid")
    private int claimUserId;

    @Column(name = "isenduserclaim")
    private Boolean endUserClaim = false;

    @Column(name = "isjobstopped")
    private Boolean jobStopped = true;

    @Column(name = "ispriorityclaim")
    private Boolean priorityClaim = true;

    @Column(name = "claimstatusid")
    private int claimStatusId = 1;

    @Column(name = "workstatusid")
    private int workStatusId = 1;

    @Column(name = "claimamountusd")
    private BigDecimal claimAmountUsd = BigDecimal.ZERO;

    @Column(name = "claimamountforeign")
    private BigDecimal claimAmountForeign = BigDecimal.ZERO;


    @Column(name = "exchangerate")
    public BigDecimal exchangeRate;

    @Column(name = "sellingcompany")
    private String sellingCompany = "";

    @Column(name = "territory")
    private String territory = "";

    @Column(name = "division")
    private String division = "";

    @Column(name = "region")
    private String region = "";

    @Column(name = "territorymanagername")
    private String territoryManagerName = "";

    @Column(name = "claimareatypeid")
    private int claimAreaTypeId;

    @Column(name = "claimareaid")
    private int claimAreaId;

    @Column(name = "iscreditmemoapproved")
    private Boolean creditMemoApproved = false;

    @Column(name = "claimreopenuserid")
    private int claimReopenUserId = 0;

    @Column(name = "claimreopendate")
    private LocalDateTime claimReopenDate;

    @Column(name = "claimclosedate")
    private LocalDateTime claimCloseDate;

    @Column(name = "declinereasonid")
    private int declineReasonId = 1;

    @Column(name = "claimdeclinedate")
    private LocalDateTime claimDeclineDate;

    @Column(name = "returnlocation")
    private String returnLocation = "";



    @Column(name = "submittercode")
    private String submitterCode = "";

   // @Column(name = "isclaimfromdebit")
    //private Boolean isClaimFromDebit = false;



    @OneToOne(mappedBy = "claim", cascade = CascadeType.ALL)
    private EndUserInformation endUserInformation;

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL)
    private List<ClaimDocument> claimDocuments;
    
    @OneToMany(mappedBy = "claim")
    private List<ClaimRgaHeader> claimRgaHeader;
    
    @OneToMany(mappedBy = "claim")
    private List<ClaimCrmHeader> claimCrmHeader;
 
   @OneToOne
    @JoinColumn(name = "claimreasonid", referencedColumnName = "claimreasonid")
    private ClaimReasonDefinition claimReasonDefinition;

    @OneToOne
    @JoinColumn(name = "businessmarketindicator", referencedColumnName = "lookupid")
    private  Lookup lookup;

    @OneToMany(mappedBy = "claim",cascade = CascadeType.ALL)
    private List<ClaimAddress> claimAddresses;

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL)
    private List<ClaimWatchlist> claimWatchlist;

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL)
    private List<WorkHistory> workHistory;

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL)
    private List<ClaimNote> claimNotes;

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL)
    private List<TraceTask> traceTasks;

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL)
    private List<AuditHistory> auditHistory;

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL)
    private List<ClaimRoute> claimRoutes;

    @Column(name = "samplerequested")
    private int sampleRequested = 0;
    
    @OneToMany(mappedBy = "claim")
    private List<ClaimLineDetail> claimLineDetail;

    @OneToOne(mappedBy = "claim", cascade = CascadeType.ALL)
    private InstoreClaim instoreClaim;
}



