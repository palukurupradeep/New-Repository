package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "claimphotos", schema = "clm")
public class ClaimPhotos extends BaseEntity {

	@Id
	@Column(name = "photoid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer photoId;
	@Column(name = "photoname")
	private String photoName;
	@Column(name = "description")
	private String photoDescription;
	@Column(name = "photoimage")
	// private Blob photoImage;
	private byte[] photo;
	@Column(name = "statusid")
	private Integer statusId;
}
