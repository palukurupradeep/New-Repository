package com.shaw.claims.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shaw.claims.dto.AddOrUpdateReasonDefinitionDTO;
import com.shaw.claims.dto.AnalyzeLineDTO;
import com.shaw.claims.dto.ClaimCodeResolutionDTO;
import com.shaw.claims.dto.ClaimResolutionDTO;
import com.shaw.claims.dto.CodePhotosDTO;
import com.shaw.claims.dto.CodePhotosResponseDTO;
import com.shaw.claims.dto.CodeResolutionCreateDTO;
import com.shaw.claims.dto.CodeRulesDTO;
import com.shaw.claims.dto.LocationDTO;
import com.shaw.claims.dto.ViewAndExportDTO;
import com.shaw.claims.model.ClaimCodeResolution;
import com.shaw.claims.model.ClaimPhotos;
import com.shaw.claims.model.ClaimResolution;
import com.shaw.claims.model.CodeRuleActionType;
import com.shaw.claims.model.CodeRuleCategory;
import com.shaw.claims.model.CodeRuleDataType;
import com.shaw.claims.model.CodeRuleImpactArea;
import com.shaw.claims.model.CodeRuleLevel;
import com.shaw.claims.model.CodeRuleReturnType;
import com.shaw.claims.model.CodeRuleType;
import com.shaw.claims.services.ClaimCodeManagementService;
import com.shaw.claims.services.ClaimCodeResolutionService;
import com.shaw.claims.services.ClaimReasonDefinitionService;
import com.shaw.claims.services.CodeRuleDataListService;
import com.shaw.claims.services.CodeRulesService;
import com.shaw.claims.services.RGaServices;
import com.shaw.claims.services.RuleDefinitionService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/smartcode/v1")
public class SmartCodeAdminController {

	Logger log = LogManager.getLogger(SmartCodeAdminController.class);
	@Autowired
	ClaimCodeManagementService claimCodeManagementService;
	
	@Autowired
	ClaimCodeResolutionService service;
	
	@Autowired
	RuleDefinitionService ruleDefinationService;
	
	@Autowired
	ClaimReasonDefinitionService reasonDefinitionService;

	@Autowired
	CodeRulesService codeRulesService;
	@Autowired
	CodeRuleDataListService codeRuleDataListService;
	@Autowired
	RGaServices rgaService;

	@GetMapping("/getClaimPhotos")
	public ResponseEntity<List<ClaimPhotos>> getClaimPhotos() {
		log.info("Inside SmartCodeAdminController.getClaimPhotos");

		return new ResponseEntity<>(claimCodeManagementService.getClaimPhotos(), HttpStatus.OK);
	}
	
	@PostMapping("/saveorUpdateClaimPhotos")
	public ResponseEntity<ClaimPhotos> saveorUpdateClaimPhotos(@RequestBody ClaimPhotos claimPhotos) {
		log.info("Inside SmartCodeAdminController.saveorUpdateClaimPhotos");
		return new ResponseEntity<>(claimCodeManagementService.saveorUpdateClaimPhotos(claimPhotos), HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteClaimPhotos")
	public ResponseEntity<Void> deleteClaimPhotos(@RequestParam(name = "photoId") Integer photoId) {
		log.info("Inside SmartCodeAdminController.deleteClaimPhotos");
		claimCodeManagementService.deleteClaimPhotos(photoId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/saveCodePhotos")
	public ResponseEntity<CodePhotosResponseDTO> saveCodePhotos(@RequestBody CodePhotosDTO codePhotos) {
		log.info("Inside SmartCodeAdminController.saveCodePhotos");
		return new ResponseEntity<>(claimCodeManagementService.saveCodePhotos(codePhotos), HttpStatus.OK);
	}

	@DeleteMapping("/deleteCodePhotos")
	public ResponseEntity<Void> deleteCodePhotos(@RequestParam(name = "codePhotoId") Integer codePhotoId) {
		log.info("Inside SmartCodeAdminController.deleteCodePhotos");
		claimCodeManagementService.deleteCodePhotos(codePhotoId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/getAllClaimResolutions")
	public ResponseEntity<List<ClaimResolution>> getAllClaimResolutions() {
		log.info("Inside SmartCodeAdminController.getAllClaimResolutions");
		return new ResponseEntity<>(service.getAllClaimResolutions(), HttpStatus.OK);
	}

	@PostMapping("/createCodeResolution")
	public ResponseEntity<ClaimCodeResolution> createCodeResolution(@RequestBody CodeResolutionCreateDTO request) {
		 log.info("Inside SmartCodeAdminController.createCodeResolution");
		return new ResponseEntity<ClaimCodeResolution>(service.createCodeResolution(request), HttpStatus.OK);
	}

	@DeleteMapping("/deleteCodeResolution")
	public ResponseEntity<Void> deleteCodeResolution(@RequestParam(name = "codeResolutionId") int codeResolutionId) {
		log.info("Inside SmartCodeAdminController.deleteCodeResolution");
		service.deleteCodeResolution(codeResolutionId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/addOrUpdateClaimResolution")
	public ResponseEntity<ClaimResolution> addOrUpdateClaimResolution(@RequestBody ClaimResolutionDTO request) {
		 log.info("Inside SmartCodeAdminController.addOrUpdateClaimResolution");
		return new ResponseEntity<ClaimResolution>(service.addOrUpdateClaimResolution(request), HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteClaimResolution")
	public ResponseEntity<Void> deleteClaimResolution(@RequestParam(name = "claimResolutionId") int claimResolutionId) {
		 log.info("Inside SmartCodeAdminController.claimResolutionId");
		service.deleteClaimResolution(claimResolutionId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

	@GetMapping("/getCodeRuleReturnType")
	public ResponseEntity<List<CodeRuleReturnType>> getCodeRuleReturnType() {
		log.info("Inside SmartCodeAdminController.CodeRuleReturnType");
		return new ResponseEntity<>(claimCodeManagementService.getCodeRuleReturnType(), HttpStatus.OK);
	}
	
	
	@GetMapping("/getCodeRuleCategory")
	public ResponseEntity<List<CodeRuleCategory>> getCodeRuleCategory() {
		log.info("Inside SmartCodeAdminController.getCodeRuleCategory");
		return new ResponseEntity<>(ruleDefinationService.getCodeRuleCategory(), HttpStatus.OK);
	}
	
	@GetMapping("/getClaimCodeResolution")
	public ResponseEntity<List<ClaimCodeResolutionDTO>> getClaimCodeResolution(@RequestParam(name = "reasonId") Integer reasonId) {
		log.info("Inside SmartCodeAdminController.getClaimCodeResolution");
		return new ResponseEntity<>(service.getClaimCodeResolution(reasonId), HttpStatus.OK);
	}
	
	@GetMapping("/getClaimCodePhotos")
	public ResponseEntity<List<CodePhotosResponseDTO>> getClaimCodePhotos(@RequestParam(name = "reasonId") Integer reasonId) {
		log.info("Inside SmartCodeAdminController.getClaimCodePhotos");
		return new ResponseEntity<>(claimCodeManagementService.getClaimCodePhotos(reasonId), HttpStatus.OK);
	}
	

	@GetMapping("/getCodeRuleType")
	public ResponseEntity<List<CodeRuleType>> getCodeRuleType() {
		 log.info("Inside SmartCodeAdminController.getCodeRuleType");
		return new ResponseEntity<>(ruleDefinationService.getCodeRuleType(), HttpStatus.OK);
	}
	
	@GetMapping("/getCodeRuleLevel")
	public ResponseEntity<List<CodeRuleLevel>> getCodeRuleLevel() {
		 log.info("Inside SmartCodeAdminController.getCodeRuleLevel");
		return new ResponseEntity<>(ruleDefinationService.getCodeRuleLevel(), HttpStatus.OK);
	}
	
	@GetMapping("/getCodeRuleImpactArea")
	public ResponseEntity<List<CodeRuleImpactArea>> getCodeRuleImpactArea() {
		 log.info("Inside SmartCodeAdminController.getCodeRuleImpactArea");
		return new ResponseEntity<>(ruleDefinationService.getCodeRuleImpactArea(), HttpStatus.OK);
	}
	
	@GetMapping("/getCodeRuleActionType")
	public ResponseEntity<List<CodeRuleActionType>> getCodeRuleActionType() {
		 log.info("Inside SmartCodeAdminController.getCodeRuleActionType");
		return new ResponseEntity<>(ruleDefinationService.getCodeRuleActionType(), HttpStatus.OK);
	}
	
	@GetMapping("/getCodeRuleDataType")
	public ResponseEntity<List<CodeRuleDataType>> getCodeRuleDataType() {
		 log.info("Inside SmartCodeAdminController.getCodeRuleDataType");
		return new ResponseEntity<>(ruleDefinationService.getCodeRuleDataType(), HttpStatus.OK);
	}
	
	@PostMapping("/addReasonDefinition")
	public ResponseEntity<AddOrUpdateReasonDefinitionDTO> addReasonDefinition(@RequestBody AddOrUpdateReasonDefinitionDTO request) {
		 log.info("Inside SmartCodeAdminController.addReasonDefinition");
		return new ResponseEntity<>(reasonDefinitionService.addReasonDefinition(request), HttpStatus.OK);
	}
	
	@PutMapping("/updateReasonDefinition")
	public ResponseEntity<AddOrUpdateReasonDefinitionDTO> updateReasonDefinition(@RequestBody AddOrUpdateReasonDefinitionDTO request) {
		 log.info("Inside SmartCodeAdminController.updateReasonDefinition");
		return new ResponseEntity<>(reasonDefinitionService.updateReasonDefinition(request), HttpStatus.OK);
	}

	@PostMapping("/saveCodeRules")
	public ResponseEntity<Void> saveCodeRules(@RequestBody CodeRulesDTO codeRulesDTO) {
		log.info("Inside SmartCodeAdminController.saveCodeRules");
		codeRulesService.saveCodeRules(codeRulesDTO);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	@GetMapping("/fetchDataListByDataType")
	public ResponseEntity<List<String>> fetchDataListByDataType(@RequestParam(name = "dataTypeCode") String dataTypeCode)
	{
		return new ResponseEntity<>(codeRuleDataListService.fetchDataListByDataType(dataTypeCode),HttpStatus.OK);
	}
	
	@GetMapping("/getCodeRuleByReasonCode")
	public ResponseEntity<List<CodeRulesDTO>> getCodeRuleByReasonCode(@RequestParam String reasonCode) {
		 log.info("Inside SmartCodeAdminController.getCodeRuleByReasonCode");
		return new ResponseEntity<>(ruleDefinationService.getCodeRuleByReasonCode(reasonCode), HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteCodeRuleById")
	public void deleteCodeRuleById(@RequestParam Integer codeRuleId) {
		 log.info("Inside SmartCodeAdminController.deleteCodeRuleById");
		 ruleDefinationService.deleteCodeRuleById(codeRuleId);
	}
	
	@GetMapping("/viewAndExportRule")
	public ResponseEntity<List<ViewAndExportDTO>> viewAndExportRule(@RequestParam String reasonCode) {
		 log.info("Inside SmartCodeAdminController.viewAndExportRule");
		return new ResponseEntity<>(ruleDefinationService.viewAndExportRule(reasonCode), HttpStatus.OK);
	}
	
	
	@GetMapping("/fetchResolutionsAndPhotos")
	public ResponseEntity<AnalyzeLineDTO> fetchResolutionsAndPhotosByReasonId(@RequestParam Integer reasonId) {
		 log.info("Inside SmartCodeAdminController.fetchResolutionsAndPhotosByReasonId");
		
		 return new ResponseEntity<>( claimCodeManagementService.fetchResolutionsAndPhotosByReasonId(reasonId),HttpStatus.OK);
	}
	
	@GetMapping("/fetchRecipients")
	public ResponseEntity<List<String>> fetchRecipients(@RequestParam(name = "claimId") String claimId) {
		 log.info("Inside SmartCodeAdminController.fetchRecipients");
		return new ResponseEntity<>(rgaService.fetchRecipients(claimId), HttpStatus.OK);
	}
	
	@GetMapping("/getReturnLocations")
	public ResponseEntity<List<LocationDTO>> getReturnLocations(@RequestParam(name = "locationCode") String locationCode) {
		 log.info("Inside SmartCodeAdminController.getReturnLocations");
		return new ResponseEntity<>(rgaService.getReturnLocations(locationCode), HttpStatus.OK);
	}
	
}
