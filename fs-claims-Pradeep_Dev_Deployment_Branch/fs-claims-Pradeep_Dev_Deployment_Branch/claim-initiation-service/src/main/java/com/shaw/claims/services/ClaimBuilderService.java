package com.shaw.claims.services;

import com.shaw.claims.constant.ErrorCodes;
import com.shaw.claims.enums.ClaimType;
import com.shaw.claims.exception.CustomValidationException;
import com.shaw.claims.model.Claim;
import com.shaw.claims.repo.ClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClaimBuilderService {

    @Autowired
    private InStoreClaimService inStoreClaimService;
    @Autowired
    private ClaimRepository claimRepository;
    @Autowired
    private ClaimInitiationService claimInitiationService;

    public Object getClaim(String claimNumber, String type) {

        ClaimType claimType = ClaimType.fromString(type);
        if (!claimNumber.matches("\\d+")) {
            throw new CustomValidationException(ErrorCodes.NO_RECORD_FOUND, "Claim number must contain only numeric characters.");
        }

        if(claimNumber.length() == 6) {
        switch (claimType) {
            case INSTORE -> {
                return inStoreClaimService.getClaim(claimNumber);
            }
            case INITIATION -> {
                return claimInitiationService.getClaimInitiation(claimNumber);
            }
            default -> throw new RuntimeException("Claim Type not found :: " + type);
        }
        }else {
            throw new CustomValidationException(ErrorCodes.CHARACTERS_MISMATCH, "Claim number must be 6 digits.");
        }
    }
}
