package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;	
@Entity
@Data
@Table(name = "jobconfiguration", schema = "clm")

public class JobConfiguration extends BaseEntity {
	    @Id
	    @Column(name = "configurationid")
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int configurationId;
	    @Column(name = "configurationkey")
	    private String configurationKey;
	    @Column(name = "configurationvalue")
	    private String configurationValue;
	    @Column(name = "statusid")
	    private int statusId; 
	    @OneToOne(mappedBy = "jobConfiguration")
	    private ClaimJob claimJob;
	
}



