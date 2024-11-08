package com.shaw.claims.services;

import com.shaw.claims.dto.AddClaimVendorDTO;
import com.shaw.claims.dto.ClaimVendorDetailResponseDTO;
import com.shaw.claims.dto.UpdateClaimVendorRequestDTO;

import java.text.ParseException;
import java.util.List;

public interface ClaimVendorService {
    void addClaimVendor(AddClaimVendorDTO addClaimVendorDTO) throws ParseException;

    ClaimVendorDetailResponseDTO getClaimVendorDetail(String claimVendorNumber);

    void updateClaimVendor(UpdateClaimVendorRequestDTO claimVendorRequestDTO) throws ParseException;
}