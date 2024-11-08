package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.dto.DashboardWidgetsResponseDTO;
import com.shaw.claims.dto.GetClaimWatchlistDTO;
import com.shaw.claims.dto.UserDashboardWidgetDTO;
import com.shaw.claims.model.CustomerWatchlist;

public interface DashBoardService {
	CustomerWatchlist addorUpdateCustomerWatchlist(CustomerWatchlist customerWatchlist);
	List<DashboardWidgetsResponseDTO> fetchUserDashboardWidgetsByUserId(Integer createdByUserId);
	void addUserDashboardWidgets(UserDashboardWidgetDTO userDashboardWidgetDTO);
	List<GetClaimWatchlistDTO> getCustomerWatchList(Integer createdByUserId);
	Integer getCustomerWatchListStatus(Integer userId, String customerNumber);
	List<GetClaimWatchlistDTO> ClaimWatchList(int userId);
	void deleteCustomerWatchList(int userId,String customerNumber);
	void deleteClaimWatchList(Integer userId, Integer claimId);
	List<GetClaimWatchlistDTO> overDueClaim(int userId);
	List<GetClaimWatchlistDTO> claimsDue(int userId);
}
