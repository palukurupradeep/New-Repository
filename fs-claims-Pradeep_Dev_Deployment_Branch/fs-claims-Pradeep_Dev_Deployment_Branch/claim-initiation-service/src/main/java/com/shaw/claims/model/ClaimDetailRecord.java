package com.shaw.claims.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.DetailRecordTypeSerializer;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "claimdetailrecords", schema = "clm")
@Data
public class ClaimDetailRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claimdetailrecordid")
    private int claimDetailRecordId;

    @JsonSerialize(using = DetailRecordTypeSerializer.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detailrecordtypeid", referencedColumnName = "detailrecordtypeid")
    private DetailRecordType detailRecordType;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "claimlineid")
    private ClaimLineDetail claimLineDetail;

    @Column(name = "widthinfeet")
    private int widthInFeet;

    @Column(name = "widthininches")
    private int widthInInches;

    @Column(name = "lengthinfeet")
    private int lengthInFeet;

    @Column(name = "lengthininches")
    private int lengthInInches;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "unitofmeasure")
    private String unitOfMeasure;

    @Column(name = "amountusd")
    private BigDecimal amountUsd;

    @Column(name = "amountforeign")
    private BigDecimal amountForeign = BigDecimal.ZERO;

    @Column(name = "unitpriceusd")
    private BigDecimal unitPriceUsd = BigDecimal.ZERO;

    @Column(name = "unitpriceforeign")
    private BigDecimal unitPriceForeign = BigDecimal.ZERO;

    @Column(name = "exchangerate")
    public BigDecimal exchangeRate;

    @Column(name = "statusid")
    private int statusId = 1;


}

