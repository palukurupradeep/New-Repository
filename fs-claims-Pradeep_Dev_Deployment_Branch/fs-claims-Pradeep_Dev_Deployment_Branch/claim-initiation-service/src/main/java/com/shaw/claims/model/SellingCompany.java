package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "sellingcompany", schema = "mas")
@Data
public class SellingCompany extends BaseEntity {
	@Id
	@Column(name = "sellingcompanyid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sellingCompanyId;
	@Column(name = "sellingcompanycode")
	private String sellingCompanyCode;
	@Column(name = "sellingcompanyname")
	private String sellingCompanyName;
	@Column(name = "companytype")
    private char companyType;
	@Column(name = "companydescription")
	private String companyDescription;
	@Column(name = "yarn")
	private Boolean yarn;
	@Column(name = "resilient")
	private Boolean resilient;
	@Column(name = "hardsurface")
	private Boolean hardSurface;
	@Column(name = "carpet")
	private Boolean carpet;
	@Column(name = "tuftexrug")
	private Boolean tuftexRug;
	@Column(name = "promotionalgoods")
	private Boolean promotionalGoods;
	@Column(name = "shawgrass")
	private Boolean shawGrass;
	@Column(name = "ecommercerugs")
	private Boolean ecommerceRugs;
	@Column(name = "laminate")
	private Boolean laminate;
	@Column(name = "ceramic")
	private Boolean ceramic;
	@Column(name = "bamboo")
	private Boolean bamboo;
	@Column(name = "cork")
	private Boolean cork;
	@Column(name = "floorigami")
	private Boolean floorigami;
	@Column(name = "carpettile")
	private Boolean carpetTile;
	@Column(name = "statusid")
	private Integer statusId;
}
