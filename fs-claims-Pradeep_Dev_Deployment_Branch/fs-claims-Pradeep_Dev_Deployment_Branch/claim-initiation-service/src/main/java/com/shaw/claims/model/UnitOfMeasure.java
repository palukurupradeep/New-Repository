package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "unitofmeasure", schema = "clm")
@Data
public class UnitOfMeasure extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unitofmeasureid")
    private int unitOfMeasureId;

    @Column(name = "unitofmeasurecode")
    private String unitOfMeasureCode;

    @Column(name = "unitofmeasuredescription")
    private String unitOfMeasureDescription;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;

}

