package com.shaw.claims.jobs;


import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.math.BigDecimal;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.shaw.claims.model.ClaimCrm;
import com.shaw.claims.services.JobService;
import com.shaw.claims.mapper.ClaimCrmRowMapper;

@Component
@Configuration

public class ClaimTestJob {
	
	private static final Logger log = LoggerFactory.getLogger(ClaimTestJob.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	private List <ClaimCrm> claimcrmlist;
	
	private String reportHeader1 = new String("                        S H A W  I N D U S T R I E S,  I N C.            \n");
    private String reportHeader2 = new String("                              CLAIMS and Document Details            \n"); 
    private String reportHeader3 = new String(" Claim Number 	Selling Co	Reg	Terr Document Number Amount USD Document Date Order No Purchase Order No");
	private BufferedWriter reportOutput = null;
	
	private String jobErrorMessage =null;
	private String executionResult;
	private String jobCode ="clm0002";
	
@Autowired
	
	JdbcTemplate  jdbcTemplateForJob;
@Autowired
	JobService jobService;


//@Scheduled(cron = "${batch.job.clm003}")

public int executeJob () {
	
	String SQL = "select Top 10 A.ClaimNumber,A.SellingCompany,A.Region,A.Territory,B.DocumentNumber,B.AmountUsd, B.DocumentDate, B.OrderNumber, B.PurchaseOrderNumber from CLM.claims A Join CLM.ClaimDocuments B on A.ClaimId = B.ClaimId";
    claimcrmlist = jdbcTemplateForJob.query(SQL, new ClaimCrmRowMapper());
    
    try {
        reportOutput = new BufferedWriter(new FileWriter ("C:\\CMS-Dev\\TestJobOutputtxt"));
     
        if (reportOutput !=null) {
        	
        	reportOutput.write(reportHeader1);
        	reportOutput.write(reportHeader2);
        	reportOutput.write(reportHeader3);
        	for(int i=0;i < claimcrmlist.size();i++) {
    	
        		writeRecord(claimcrmlist.get(i));
  
        	}
        	log.info("The time is now {}", dateFormat.format(new Date()));
  
        	reportOutput.flush();
        	reportOutput.close();
        }
    }
    catch(IOException e) {
   	 
   	 log.error("Exception while opening file for wrirting {}",e);
   	 jobErrorMessage = e.getMessage();
    	
    } 
    
    if(jobErrorMessage != null) {
      executionResult = "Failed";
      jobService.updateJobResult(jobCode, executionResult, jobErrorMessage);
	   return -1;
	}
    else {
      executionResult = "Completed";
      jobErrorMessage =" ";
      jobService.updateJobResult(jobCode, executionResult, jobErrorMessage);
  	  return 0;
    }
}

private void writeRecord(ClaimCrm crmObject) throws IOException {
	
		 reportOutput.write(crmObject.getClaimNumber() + "   " + crmObject.getSellingCompany() + "    "+
				 crmObject.getClaimRegion() + "    " + crmObject.getClaimTerritory() +
	        "       " + crmObject.getDocumentNumber() + "    " + crmObject.getDocumentDate() +
	        "    " + crmObject.getOrderNumber() + "    " +
	        "    " + crmObject.getPurchaseOrderNumber() +  "     " +
	        crmObject.getAmountUsd() + " \n");  
        
}

}


