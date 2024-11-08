package com.shaw.claims.model;
import java.math.BigDecimal;
import java.util.Date;

public class ClaimCrm {
		
		private String ClaimNumber;
		private String SellingCompany;
		private String ClaimRegion;
		private String ClaimTerritory;
		private String DocumentNumber;
		private BigDecimal AmountUsd;
		private Date DocumentDate;
		private String OrderNumber;
		private String PurchaseOrderNumber;
		
	public ClaimCrm() {
		
	}
		
		public String getClaimNumber() {
		return ClaimNumber;
	}


	public void setClaimNumber(String claimNumber) {
		ClaimNumber = claimNumber;
	}


	public String getSellingCompany() {
		return SellingCompany;
	}


	public void setSellingCompany(String sellingCompany) {
		SellingCompany = sellingCompany;
	}


	public String getClaimRegion() {
		return ClaimRegion;
	}


	public void setClaimRegion(String claimRegion) {
		ClaimRegion = claimRegion;
	}


	public String getClaimTerritory() {
		return ClaimTerritory;
	}


	public void setClaimTerritory(String claimTerritory) {
		ClaimTerritory = claimTerritory;
	}


	public String getDocumentNumber() {
		return DocumentNumber;
	}


	public void setDocumentNumber(String documentNumber) {
		DocumentNumber = documentNumber;
	}


	public BigDecimal getAmountUsd() {
		return AmountUsd;
	}


	public void setAmountUsd(BigDecimal amountUsd) {
		AmountUsd = amountUsd;
	}


	public Date getDocumentDate() {
		return DocumentDate;
	}


	public void setDocumentDate(Date documentDate) {
		DocumentDate = documentDate;
	}


	public String getOrderNumber() {
		return OrderNumber;
	}


	public void setOrderNumber(String orderNumber) {
		OrderNumber = orderNumber;
	}


	public String getPurchaseOrderNumber() {
		return PurchaseOrderNumber;
	}


	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		PurchaseOrderNumber = purchaseOrderNumber;
	}

}

