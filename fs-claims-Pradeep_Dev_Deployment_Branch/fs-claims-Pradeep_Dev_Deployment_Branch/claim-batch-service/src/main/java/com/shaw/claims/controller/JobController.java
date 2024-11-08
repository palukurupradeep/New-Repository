package com.shaw.claims.controller;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.shaw.claims.services.JobService;
import com.shaw.claims.dto.ClaimJobDTO;
import com.shaw.claims.model.ClaimJob;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/Job/v1")
public class JobController {
	
	@Autowired
	JobService jobService;
	   
	Logger log = LogManager.getLogger(JobController.class);
	@GetMapping("/jobs")
	public ResponseEntity<List<ClaimJob>> findAllJobs(){
      log.info("Inside JobController.findAllJobs");
	  return new ResponseEntity<List<ClaimJob>>(jobService.findAllJobs(), HttpStatus.OK);
	}
	@PostMapping("/addJob")
	public ResponseEntity<ClaimJob> addJob(@RequestBody ClaimJobDTO claimjobDTO) {
	   log.info("Inside JobController.addJob");
	   return new ResponseEntity<ClaimJob>(jobService.addJob(claimjobDTO),HttpStatus.OK);
	}

	@PostMapping("/updateJob")
	public ResponseEntity<ClaimJob> updateJob(@RequestBody ClaimJobDTO claimjobDTO) {
	        log.info("Inside JonController.updateJob");
	        return new ResponseEntity<ClaimJob>(jobService.updateJob(claimjobDTO),HttpStatus.OK);
	}

}

