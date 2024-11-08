package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "userworkhistory", schema = "clm")
public class WorkHistory extends BaseEntity{
    @Id
    @Column(name = "userworkhistoryid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userWorkHistoryId;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "claimid")
    private Claim claim;
    @Column(name = "statusid")
    private int statusId;
}
