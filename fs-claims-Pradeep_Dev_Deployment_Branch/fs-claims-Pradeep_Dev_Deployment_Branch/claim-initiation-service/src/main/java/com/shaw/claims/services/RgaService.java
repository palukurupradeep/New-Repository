package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.dto.ClaimRgaHeaderDTO;
import com.shaw.claims.dto.ClaimRgaReturnReceiptProcessResponseDTO;
import com.shaw.claims.dto.RgaAddressDTO;
import com.shaw.claims.dto.RgaIssueResponseDTO;
import com.shaw.claims.dto.ServiceToKeepTypeDTO;
import com.shaw.claims.dto.UpdateRGAStatusResponseDTO;
import com.shaw.claims.dto.UpdateReceiptGoodsDTO;

public interface RgaService {

	public RgaIssueResponseDTO rgaDraftORSave(ClaimRgaHeaderDTO claimRgaHeaderDTO);

	public UpdateRGAStatusResponseDTO updateRGAStatusByAction(int claimRgaHeaderId, String action, int loggedInUserId,String noteText);

	public void issueRGAValidation(int claimRgaHeaderId);

	public List<ClaimRgaHeaderDTO> fetchRgaDetails(int claimId);

	public ClaimRgaHeaderDTO findRgaDetailsByRgaNumber(String rgaNumber);

	public void updateReceiptOfGoods(UpdateReceiptGoodsDTO updateReceiptGoodsDTO);

	List<ClaimRgaReturnReceiptProcessResponseDTO> returnReceiptProcess(String locationCode, String rgaNumber);

	public ClaimRgaHeaderDTO fetchRgaDetailsByClaimIdAndRgaNumber(int claimId, String rgaNumber);

	public void cancelRga(int claimId, String rgaNumber);

	public List<ServiceToKeepTypeDTO> fetchServiceToKeepType();

	public RgaAddressDTO fetchCountryAndState(int countryId, int stateId);

}
