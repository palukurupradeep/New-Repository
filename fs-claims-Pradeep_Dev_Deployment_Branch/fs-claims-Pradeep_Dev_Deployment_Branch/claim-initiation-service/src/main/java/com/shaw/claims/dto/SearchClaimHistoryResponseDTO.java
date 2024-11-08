package com.shaw.claims.dto;
 
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
 
import lombok.Data;
 
@Data
public class SearchClaimHistoryResponseDTO {
 
	
	public String status="";
	public String claimNbr="";
	public String claimDate="";
	public String lastActivityDate="";
	public String customerNbr="";
	public String customerName="";
	public String claimCategory = "";
	public String sellingCompany="";
	public String division="";
	public String region="";
	public String territory="";
	public LinkedHashSet<String> invoiceDetail;
	public LinkedHashSet<String> rgaDetail;
	public LinkedHashSet<String> crmDetail;
	public String debitMemo="";
	public BigDecimal amount;
	public String claimReason;
	public String consumerName="";
	public String state="";
	public String territoryManager="";
	public String reasonCodeDescription="";
	public SearchHistoryEndUserDTO endUserInformation;
}

