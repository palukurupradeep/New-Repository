package com.shaw.claims.services;

import com.shaw.claims.dto.ClaimNoteRequestDTO;
import com.shaw.claims.dto.InStoreClaimResponseDTO;
import com.shaw.claims.dto.InStoreClaimsDTO;
import com.shaw.claims.dto.InStoreClaimsResponseDTO;

import java.util.List;

public interface InStoreClaimService {
    List<InStoreClaimsResponseDTO> inStoreClaimsList(String region, String territory, String division);

    InStoreClaimResponseDTO inStoreClaimByClaimNumber(String claimNumber);
    InStoreClaimsDTO getClaim(String claimNumber);
}
