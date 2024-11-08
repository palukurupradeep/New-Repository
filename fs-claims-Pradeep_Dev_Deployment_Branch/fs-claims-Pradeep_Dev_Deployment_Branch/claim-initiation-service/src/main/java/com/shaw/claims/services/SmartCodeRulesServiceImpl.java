package com.shaw.claims.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shaw.claims.dto.ClaimLevelRulesDTO;
import com.shaw.claims.dto.SmartCodeMessageDTO;

@Service
public class SmartCodeRulesServiceImpl implements SmartCodeRulesService {
		
	@Autowired
	ClaimLevelValidateServiceImpl claimLevelValidationServiceImpl;
	
//	@Autowired
//	LineLevelValidationServiceImpl lineLevelValidationServiceImpl;

	@Override
	public List<SmartCodeMessageDTO> validateSmartCodeRules(ClaimLevelRulesDTO claimLevelRulesDTO) throws Exception {
		return claimLevelValidationServiceImpl.claimLevelValidations(claimLevelRulesDTO);
	}

}
