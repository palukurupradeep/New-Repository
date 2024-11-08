package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.dto.ClaimJobDTO;
import com.shaw.claims.model.ClaimJob;

public interface JobService {	
	   List<ClaimJob> findAllJobs();
	   ClaimJob addJob(ClaimJobDTO claimJobDTO);
	   ClaimJob updateJob(ClaimJobDTO claimJobDTO);
	   ClaimJob findJobById(int jobId);
	   void updateJobResult(String jobCode,String executionResult,String jobError);
}
