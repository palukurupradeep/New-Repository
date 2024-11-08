package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "claimrouting", schema = "clm")
public class ClaimRoute extends BaseEntity{

    @Id
    @Column(name = "claimroutingid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer claimRoutingId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "claimid")
    private Claim claim;

    @Column(name = "routedtouserid")
    private int routedToUserId;

    @Column(name = "routedtousergroupid")
    private int routedToUserGroupId;

    @Column(name = "statusid")
    private int statusId;
}
