package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.*;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "claimlinedetails", schema = "clm")
@Data
public class ClaimLineDetail extends BaseEntity {

    @Id
    @Column(name = "claimlineid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int claimLineId;
    private transient int claimDocId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "claimid")
    private Claim claim;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "claimdocumentid", referencedColumnName = "claimdocumentid")
    private ClaimDocument claimDocument;

    @Column(name = "linenumber")
    private int lineNumber;

    @Column(name = "rollnumber")
    private String rollNumber = "";

    @Column(name = "stylenumber")
    private String styleNumber = "";

    @Column(name = "colornumber")
    private String colorNumber = "";

    @JsonSerialize(using = RcsCodesSerializer.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rcscodeid", referencedColumnName = "rcscodeid")
    private RcsCodes rcsCodes;

    @OneToOne
    @JoinColumn(name = "claimreasonid", referencedColumnName = "claimreasonid")
    private ClaimReasonDefinition claimReasonDefinition;

    @Column(name = "grade")
    private String grade = "";

    @Column(name = "lineamountusd")
    private BigDecimal lineAmountUsd = BigDecimal.ZERO;

    @Column(name = "lineamountforeign")
    private BigDecimal lineAmountForeign = BigDecimal.ZERO;

    @Column(name = "unitpriceusd")
    private BigDecimal unitPriceUsd = BigDecimal.ZERO;

    @Column(name = "unitpriceforeign")
    private BigDecimal unitPriceForeign = BigDecimal.ZERO;

    @Column(name = "pricingcurrencycode")
    private String pricingCurrencyCode = "";

    @Column(name = "currencycodedesignation")
    private String currencyCodeDesignation = "";

    @Column(name = "currencycodename")
    private String currencyCodeName = "";

    @Column(name = "exchangerate")
    public BigDecimal exchangeRate = BigDecimal.ZERO;

    @Column(name = "dyelot")
    private String dyeLot = "";

    @Column(name = "quantity")
    private BigDecimal quantity = BigDecimal.ZERO;

    @Column(name = "sellingcompany")
    private String sellingCompany ="";

    @JsonSerialize(using = UnitOfMeasureSerializer.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unitofmeasureid", referencedColumnName = "unitofmeasureid")
    private UnitOfMeasure unitOfMeasure;

    @Column(name = "squarefeet")
    private BigDecimal squareFeet = BigDecimal.ZERO;

    @Column(name = "productcode")
    private String productCode="";

    @Column(name = "assignedtosalesid")
    private String assignedToSalesId = "";

    @Column(name = "approvedbysalesid")
    private String approvedBySalesId = "";

    @JsonSerialize(using = SalesReviewStatusSerializer.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salesreviewstatusid", referencedColumnName = "salesreviewstatusid")
    private SalesReviewStatus salesReviewStatus;

    @Column(name = "salesreviewdate")
    private LocalDateTime salesReviewDate;

    @Column(name = "approvedunitprice")
    private BigDecimal approvedUnitPrice = BigDecimal.ZERO;

    @Column(name = "salesdiscount")
    private BigDecimal salesDiscount = BigDecimal.ZERO;

    @Column(name = "salesoriginid")
    private String salesOriginId = "";

    @Column(name = "inventorystyle")
    private String inventoryStyle = "";

    @Column(name = "inventorycolor")
    private String inventoryColor = "";

    @Column(name = "manufacturingplant")
    private String manufacturingPlant = "";

    @JsonSerialize(using = DetailTypeSerializer.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detailtypeid", referencedColumnName = "detailtypeid")
    private DetailType detailType;

    @JsonSerialize(using = LineSourceSerializer.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linesourceid", referencedColumnName = "linesourceid")
    private LineSource lineSource;

    @Column(name = "linesourcereference")
    private String lineSourceReference="";

    @JsonSerialize(using = DetailStatusTypeSerializer.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detailstatustypeid", referencedColumnName = "detailstatustypeid")
    private DetailStatusType detailStatusType;

    @Column(name = "lineaddreasonid")
    private int lineAddReasonId = 1;

    @Column(name = "vendorid")
    private String vendorId="";

    @Column(name = "statusid")
    private int statusId = 1;

    @Column(name = "invoicelinenumber")
    private int invoiceLineNumber;

    @OneToMany(mappedBy = "claimLineDetail", cascade = CascadeType.ALL)
    private List<ClaimDetailRecord> claimDetailRecords;

}

