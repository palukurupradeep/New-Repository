package com.shaw.claims.controller;

import java.util.List;

import com.shaw.claims.dto.InspectionRequestTypeResponseDTO;
import com.shaw.claims.dto.InspectionServicesResponseDTO;
import com.shaw.claims.services.InspectionServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shaw.claims.dto.CertifyingAgencyDTO;
import com.shaw.claims.dto.InspectionServiceTypeDTO;
import com.shaw.claims.dto.InspectionStatusDTO;
import com.shaw.claims.services.CertifyingAgencyService;
import com.shaw.claims.services.InspectionStatusService;
import com.shaw.claims.services.InspectionTypeService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/inspectiondetails/v1")
public class InspectionController {

	Logger log = LogManager.getLogger(ClaimDetailsController.class);

	@Autowired
	InspectionStatusService inspectionStatusService;
	@Autowired
	InspectionTypeService inspectionTypeService;
	@Autowired
	CertifyingAgencyService certifyingAgencyService;
	@Autowired
	private InspectionServices inspectionServices;

	@GetMapping("/getAllInspectionStatus")
	public ResponseEntity<List<InspectionStatusDTO>> getAllInspectionStatus() {
		return new ResponseEntity<>(inspectionStatusService.getAllInspectionStatus(), HttpStatus.OK);
	}

	@GetMapping("/getAllInspectionServiceType")
	public ResponseEntity<List<InspectionServiceTypeDTO>> getAllInspectionServiceType() {
		return new ResponseEntity<>(inspectionTypeService.getAllInspectionServiceType(), HttpStatus.OK);
	}

	@GetMapping("/getAllCertifyingAgency")
	public ResponseEntity<List<CertifyingAgencyDTO>> getAllCertifyingAgency() {
		return new ResponseEntity<>(certifyingAgencyService.getAllCertifyingAgency(), HttpStatus.OK);
	}

	@GetMapping("/getAllActiveInspectionServices")
	public ResponseEntity<List<InspectionServicesResponseDTO>> getAllInspectionServices() {
		log.info("InspectionController.getAllInspectionServicesOffered");
		return new ResponseEntity<>(inspectionServices.getAllInspectionServices(), HttpStatus.OK);
	}

	@GetMapping("/getAllActiveInspectionRequestTypes")
	public ResponseEntity<List<InspectionRequestTypeResponseDTO>> getAllActiveInspectionRequestTypes() {
		log.info("InspectionController.getAllInspectionServicesOffered");
		return new ResponseEntity<>(inspectionServices.getAllActiveInspectionRequestTypes(), HttpStatus.OK);
	}
}
