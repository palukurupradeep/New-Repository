package com.shaw.claims.dto;

import lombok.Data;
	@Data
	public class ClaimJobDTO {
	  
		private Integer jobId= 0;
	    private String jobName="";
	    private String jobDescription="";
	    private String jobFrequency="";
	    private String alertEmail="";
	    private String  configurationKey="";
	    private String  configurationValue="";
	    private Integer statusId =0;
	}



