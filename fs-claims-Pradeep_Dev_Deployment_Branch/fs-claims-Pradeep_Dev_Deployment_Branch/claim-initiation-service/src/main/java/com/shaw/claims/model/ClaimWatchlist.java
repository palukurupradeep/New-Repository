package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "claimwatchlist", schema = "clm")
@Data
public class ClaimWatchlist extends BaseEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claimwatchlistid")
    private int claimWatchlistId;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "claimid", referencedColumnName = "claimid")
    private Claim claim;
	@Column(name = "statusid")
    private int statusId = 1;
}
