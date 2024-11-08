package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.StatusSerializer;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "claimcategory", schema = "clm")
public class ClaimCategory extends BaseEntity{

    @Id
    @Column(name = "claimcategoryid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer claimCategoryId;

    @Column(name = "claimcategorycode")
    private String claimCategoryCode;

    @Column(name = "claimcategoryname")
    private String claimCategoryName;

    @Column(name = "displaysequence")
    private int displaySequence;

    @JsonIgnore
    @JsonSerialize(using = StatusSerializer.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusid", referencedColumnName = "statusid")
    private Status status;

}