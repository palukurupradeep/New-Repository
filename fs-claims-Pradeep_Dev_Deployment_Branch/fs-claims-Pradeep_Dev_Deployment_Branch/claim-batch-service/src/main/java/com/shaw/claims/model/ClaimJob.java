package com.shaw.claims.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.JobConfigurationSerializer;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
@Table(name = "claimjob", schema = "clm")
public class ClaimJob extends BaseEntity {
    @Id
    @Column(name = "jobid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int jobId;
    @Column(name = "jobname")
    private String jobName;
	@Column(name = "jobdescription")
    private String jobDescription;
    @Column(name = "jobfrequency")
    private String jobFrequency;
    @Column(name = "executionresult")
    private String executionResult="";
    @Column(name = "alertemail")
    private String alertEmail;
    @Column(name = "errormessage")
    private String errorMessage="";
    @Column(name = "statusid")
    private int statusId=0;
    @Column(name = "jobcode")
    private String jobCode;
	@JsonSerialize(using = JobConfigurationSerializer.class)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "configurationid",referencedColumnName = "configurationid")
	private JobConfiguration jobConfiguration;
	
}
