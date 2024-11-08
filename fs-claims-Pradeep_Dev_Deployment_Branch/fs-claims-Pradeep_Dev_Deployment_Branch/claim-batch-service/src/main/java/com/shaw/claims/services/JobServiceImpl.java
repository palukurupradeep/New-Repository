package com.shaw.claims.services;


import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.shaw.claims.dto.ClaimJobDTO;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.ClaimJob;
import com.shaw.claims.model.JobConfiguration;
import com.shaw.claims.repo.JobRepo;

@Service
public class JobServiceImpl implements JobService {
	
@Autowired
JobRepo jobRepo;

private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);

   
@Override
public List<ClaimJob> findAllJobs() {	
   return jobRepo.findAllJobs();
}
@Override
public ClaimJob addJob(ClaimJobDTO claimjobDTO) {
	ClaimJob claimjobData = null;
	JobConfiguration jobConfiguration = new JobConfiguration();
	ClaimJob claimJob = new ClaimJob();
    String jobNumber = String.format("%04d", jobRepo.getNextJobNumber());
    StringBuilder jobCodeBuilder = new StringBuilder(7);
    StringBuilder configurationKeyBuilder = new StringBuilder(17);
    jobCodeBuilder.append("clm");
    jobCodeBuilder.append(jobNumber);
    String jobCode = jobCodeBuilder.toString();
    configurationKeyBuilder.append("batch.job.");
    
    configurationKeyBuilder.append(jobCode);
    String configurationKey = configurationKeyBuilder.toString();
	jobConfiguration.setConfigurationKey(configurationKey);
	jobConfiguration.setConfigurationValue(claimjobDTO.getConfigurationValue());
	claimJob.setJobName(claimjobDTO.getJobName());
	claimJob.setJobDescription(claimjobDTO.getJobDescription());
	claimJob.setJobFrequency(claimjobDTO.getJobFrequency());
	claimJob.setAlertEmail(claimjobDTO.getAlertEmail());
	claimJob.setStatusId(claimjobDTO.getStatusId());
	claimJob.setJobConfiguration(jobConfiguration);
	claimJob.setJobCode(jobCode);
	claimjobData = jobRepo.save(claimJob);
	return claimjobData;
}

public void updateJobResult(String jobCode,String executionResult,String jobError)
{
	ClaimJob claimJob = null;
	claimJob = jobRepo.findByJobCode(jobCode);
	if(claimJob != null) {
		claimJob.setExecutionResult(executionResult);
		claimJob.setErrorMessage(jobError);
		claimJob = jobRepo.save(claimJob);
	}
	else {
		throw new CommonException(
				"Record Not Found with jobCode :: " + jobCode);	
	}
}
@Override
public ClaimJob findJobById(int jobId) {
	Optional<ClaimJob> jobInfo = jobRepo.findById(jobId);
	if (jobInfo.isPresent()) {
        return jobInfo.get();
	} else {
		throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
				"Record Not Found with jobId :: " + jobId);	
	}
}
@Override
public ClaimJob updateJob(ClaimJobDTO claimjobDTO){
    ClaimJob claimJob = null;
    if (claimjobDTO != null){
    	claimJob = findJobById(Integer.valueOf(claimjobDTO.getJobId()));  
    	claimJob.setJobName(claimjobDTO.getJobName());
    	claimJob.setJobDescription(claimjobDTO.getJobDescription());
    	claimJob.setJobFrequency(claimjobDTO.getJobFrequency());
    	claimJob.setAlertEmail(claimjobDTO.getAlertEmail());
    	claimJob.setStatusId(claimjobDTO.getStatusId());
    	claimJob.getJobConfiguration().setConfigurationValue(claimjobDTO.getConfigurationValue());
        claimJob = jobRepo.save(claimJob);
    }
    return claimJob;
}
}