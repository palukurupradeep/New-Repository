package com.shaw.claims.builder;

import com.shaw.claims.dto.ClaimBuilderDTO;
import com.shaw.claims.enums.ClaimType;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.Claim;
import com.shaw.claims.services.ClaimInitiationService;
import com.shaw.claims.services.ClaimSubmittalService;
import com.shaw.claims.util.TDVServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClaimBuilder {
     @Autowired
     private TDVServiceUtil tdvServiceUtil;
     @Autowired
     private ClaimSubmittalService claimSubmittalService;
     @Autowired
     private ClaimInitiationService claimInitiationService;

    public Claim getClaim(ClaimBuilderDTO claimBuilderDTO, String type){
        ClaimType claimType = ClaimType.fromString(type);
        switch (claimType) {
            case SUBMITTAL:
               return claimSubmittalService.getClaim(claimBuilderDTO);
            case INITIATION:
                return claimInitiationService.getClaim(claimBuilderDTO);
            default:
                throw new CommonException("Invalid claim type:" + type);
        }
    }
}
