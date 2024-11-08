package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.dto.ClaimLevelRulesDTO;
import com.shaw.claims.dto.SmartCodeMessageDTO;

public interface SmartCodeRulesService {
	List<SmartCodeMessageDTO> validateSmartCodeRules(ClaimLevelRulesDTO claimLevelRulesDTO) throws Exception;
}
