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
@Table(name = "customerwatchlist", schema = "clm")
public class CustomerWatchlist extends BaseEntity{

	 @Id
    @Column(name = "customerwatchlistid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int customerWatchlistId;
	@Column(name = "customernumber")
	private String customerNumber;
	@Column(name = "statusid")
	 private int statusId;
}
