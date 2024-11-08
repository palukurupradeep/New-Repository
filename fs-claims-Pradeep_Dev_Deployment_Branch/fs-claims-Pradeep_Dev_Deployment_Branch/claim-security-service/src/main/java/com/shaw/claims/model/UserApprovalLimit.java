package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.ApprovalLimitTypeSerializer;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "userapprovallimit", schema = "clm")
public class UserApprovalLimit  extends BaseEntity{

	@Id
	@Column(name = "userapprovallimitid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userApprovalLimitId;
	@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userid")
	private Users users;
	@JsonSerialize(using = ApprovalLimitTypeSerializer.class)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approvallimittypeid")
	private ApprovalLimitType approvalLimitType;
	@Column(name = "approvallimit")
	private double approvalLimit;
	@Column(name = "statusid")
	private int statusId = 1;
}
