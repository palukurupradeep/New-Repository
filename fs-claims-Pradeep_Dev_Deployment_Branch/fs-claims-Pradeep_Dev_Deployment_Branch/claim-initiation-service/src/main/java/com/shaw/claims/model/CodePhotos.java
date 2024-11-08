package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "codephotos", schema = "clm")
public class CodePhotos extends BaseEntity {

	@Id
	@Column(name = "codephotoid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer codePhotoId;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "claimreasonid")
	private ClaimReasonDefinition claimReasonDefinition;
	@OneToOne
	@JoinColumn(name = "photoid", referencedColumnName = "photoid")
	private ClaimPhotos claimPhotos;
	@Column(name = "statusid")
	private Integer statusId;
	
}
