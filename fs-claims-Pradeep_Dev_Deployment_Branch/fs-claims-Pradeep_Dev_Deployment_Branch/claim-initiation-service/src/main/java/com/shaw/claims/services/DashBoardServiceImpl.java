package com.shaw.claims.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shaw.claims.dto.DashboardWidgetsResponseDTO;
import com.shaw.claims.dto.GetClaimWatchlistDTO;
import com.shaw.claims.dto.UserDashboardWidgetDTO;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.Claim;
import com.shaw.claims.model.ClaimAddress;
import com.shaw.claims.model.ClaimWatchlist;
import com.shaw.claims.model.CustomerWatchlist;
import com.shaw.claims.model.DashboardWidget;
import com.shaw.claims.model.UserDashboardWidget;
import com.shaw.claims.model.WorkStatus;
import com.shaw.claims.repo.ClaimRepository;
import com.shaw.claims.repo.ClaimWatchlistRepository;
import com.shaw.claims.repo.CustomerWatchlistRepository;
import com.shaw.claims.repo.DashboardWidgetRepository;
import com.shaw.claims.repo.UserDashboardWidgetIdRepository;
import com.shaw.claims.repo.WorkStatusRepository;

@Service
public class DashBoardServiceImpl implements DashBoardService{
	Logger log = LogManager.getLogger(DashBoardServiceImpl.class);
	@Autowired
	CustomerWatchlistRepository customerWatchlistRepository;
	@Autowired
	DashboardWidgetRepository dashboardWidgetRepository;
	@Autowired
	UserDashboardWidgetIdRepository userDashboardWidgetIdRepository;
	@Autowired
	ClaimWatchlistRepository claimWatchlistRepository;
	@Autowired
	ClaimRepository claimRepository;
	@Autowired
	WorkStatusRepository workStatusRepository;
	@Autowired
	ClaimDetailsService claimDetailsService;
	@Autowired
	private RestTemplate restTemplate;
	@Value(("${claim-security.rest.url}"))
	private String restSecurityUrl;
	@Autowired
	WorkHistoryServiceImpl workHistory;
	@Override
	public CustomerWatchlist addorUpdateCustomerWatchlist(CustomerWatchlist customerWatchlist) {
		CustomerWatchlist customerWatchListInfo = customerWatchlistRepository.save(customerWatchlist);
		List<Claim> claimList = claimDetailsService.getClaims(null, customerWatchListInfo.getCustomerNumber());
		if(claimList != null && !claimList.isEmpty()) {
			claimList.stream().forEach(data -> {
				workHistory.updateWorkHistory(data,  data.getModifiedByUserId());
			});
		}
		
		return customerWatchListInfo;
	}
	private List<DashboardWidgetsResponseDTO> fetchDashboardWidgets() {
		List<DashboardWidgetsResponseDTO> dashboardWidgetsListData = new ArrayList<>();
		List<DashboardWidget> result = dashboardWidgetRepository.fetchDashboardWidgets();
		for (DashboardWidget dashboardWidget : result) {
			DashboardWidgetsResponseDTO dashboardWidgetsResponseDTO = new DashboardWidgetsResponseDTO();
			dashboardWidgetsResponseDTO.setDashboardWidgetId(dashboardWidget.getDashboardWidgetId());
			dashboardWidgetsResponseDTO.setDashboardWidgetName(dashboardWidget.getDashboardWidgetName());
			dashboardWidgetsResponseDTO.setDisplaySequence(dashboardWidget.getDisplaySequence());
			dashboardWidgetsListData.add(dashboardWidgetsResponseDTO);
		}
		return dashboardWidgetsListData;
	}
	private void deleteUserDashboardWidgets(Integer createdByUserId) {
		userDashboardWidgetIdRepository.deleteByUserDashboardWidgets(createdByUserId);
	}
	@Override
	public List<DashboardWidgetsResponseDTO> fetchUserDashboardWidgetsByUserId(Integer createdByUserId) {
		List<UserDashboardWidget> result = userDashboardWidgetIdRepository
				.findUserDashboardWidgetByUserId(createdByUserId);
		List<DashboardWidgetsResponseDTO> dashboardWidgetsListData = new ArrayList<>();
		if (!result.isEmpty()) {
			for (UserDashboardWidget userDashboardWidget : result) {
				DashboardWidgetsResponseDTO dashboardWidgetsResponseDTO = new DashboardWidgetsResponseDTO();
				dashboardWidgetsResponseDTO
						.setDashboardWidgetId(userDashboardWidget.getDashboardWidget().getDashboardWidgetId());
				dashboardWidgetsResponseDTO
						.setDashboardWidgetName(userDashboardWidget.getDashboardWidget().getDashboardWidgetName());
				dashboardWidgetsResponseDTO.setDisplaySequence(userDashboardWidget.getDisplaySequence());
				dashboardWidgetsListData.add(dashboardWidgetsResponseDTO);
			}

			return dashboardWidgetsListData;
		}
		return fetchDashboardWidgets();
	}
	@Override
	public void addUserDashboardWidgets(UserDashboardWidgetDTO userDashboardWidgetDTO) {
		List<UserDashboardWidget> userWorkQueueFieldListData = userDashboardWidgetIdRepository
				.findUserDashboardWidgetByUserId(userDashboardWidgetDTO.getCreatedByUserId());
		List<UserDashboardWidget> userDashboardWidgetList = new ArrayList<>();
		if (!userWorkQueueFieldListData.isEmpty()) {
			deleteUserDashboardWidgets(userDashboardWidgetDTO.getCreatedByUserId());
		}
		for (DashboardWidget dashboardWidget : userDashboardWidgetDTO.getDashboardWidget()) {
			UserDashboardWidget userDashboardWidget = new UserDashboardWidget();
			userDashboardWidget.setCreatedByUserId(userDashboardWidgetDTO.getCreatedByUserId());
			userDashboardWidget.setModifiedByUserId(userDashboardWidgetDTO.getModifiedByUserId());
			userDashboardWidget.setDashboardWidget(dashboardWidget);
			userDashboardWidget.setStatusId(1);
			userDashboardWidget.setDisplaySequence(dashboardWidget.getDisplaySequence());
			userDashboardWidgetList.add(userDashboardWidget);
		}
		userDashboardWidgetIdRepository.saveAll(userDashboardWidgetList);
	}
	@Override
	public List<GetClaimWatchlistDTO> getCustomerWatchList(Integer createdByUserId) {
		 List<GetClaimWatchlistDTO>  customerWatchList=new ArrayList<>();
		List<Object[]> data=claimRepository.getCustomerNumberCount(createdByUserId);
		for(int i=0;i<data.size();i++){
			GetClaimWatchlistDTO customerWatchListDto=new GetClaimWatchlistDTO();
			String customerNumber=(String) data.get(i)[0];
			String customerName=(String) data.get(i)[1];
			Long count=(Long) data.get(i)[2];
			customerWatchListDto.setCustomerNumber(customerNumber);
			customerWatchListDto.setCount(count);
			//customerWatchListDto.setCustomerName(getLoggedInUserName((Integer) data.get(i)[3]));
			customerWatchListDto.setCustomerName(customerName);
			customerWatchList.add(customerWatchListDto);
		}
		return customerWatchList;
	}
	@Override
	public Integer getCustomerWatchListStatus(Integer userId, String customerNumber) {
		List<CustomerWatchlist> customerWatchlist = customerWatchlistRepository.findByCustomerNumber(userId, customerNumber);
		return !customerWatchlist.isEmpty() ? 1 : 0;
	}

	@Override
	public List<GetClaimWatchlistDTO> ClaimWatchList(int userId) {
		List<GetClaimWatchlistDTO> claimwatchListResponse = new ArrayList<>();
		List<ClaimWatchlist> claimWatchList = claimWatchlistRepository.findByCreatedByUserId(userId);

		if (claimWatchList != null && !claimWatchList.isEmpty()) {
			claimwatchListResponse = claimWatchList.stream().map(data -> {
				GetClaimWatchlistDTO response = new GetClaimWatchlistDTO();
				long days = Duration.between(data.getCreatedDateTime(), LocalDateTime.now()).toDays();
				response.setClaimId(data.getClaim().getClaimId());
				response.setClaimNumber(data.getClaim().getClaimNumber());
				response.setCustomerNumber(data.getClaim().getCustomerNumber());
				 String custometName = data.getClaim().getClaimAddresses().stream()
			                .filter(x -> x.getAddressTypeId()==14)
			                .map(ClaimAddress::getFullName)                    
			                .findAny()
			                .orElse("");
				 response.setCustomerName(custometName);
				WorkStatus workStatus = getWorkStatusById(data.getClaim().getWorkStatusId());
				if (workStatus != null) {
					response.setWorkStatus(workStatus.getWorkStatusDescription());
				}
				response.setDays((int) days);
				return response;
			}).toList();
		}

		return claimwatchListResponse.stream()
				.sorted(Comparator.comparing(GetClaimWatchlistDTO::getDays)).collect(Collectors.toList());
	}
	private WorkStatus getWorkStatusById(int workStatusId) {
		Optional<WorkStatus> workStatus = workStatusRepository.findById(workStatusId);
		if(workStatus.isPresent()) {
			return workStatus.get();
		} else {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND), "Work status not found for Id :: {}" + workStatusId);
		}
	}
	
	@Override
	public void deleteCustomerWatchList(int userId, String customerNumber) {
		customerWatchlistRepository.deleteCustomerWatchList(userId, customerNumber);
	}
	
	@Override
	public void deleteClaimWatchList(Integer userId, Integer claimId) {
		claimWatchlistRepository.deleteClaimWatchList(userId, claimId);
	}
	@Override
	public List<GetClaimWatchlistDTO> overDueClaim(int userId) {
		List<Claim> claimList = getClaims(userId);
		log.info("Claim list size before ::"+ claimList.size());
		List<Claim> agedClaimList =  claimList.stream().filter(data-> Duration.between(data.getCreatedDateTime(), LocalDateTime.now()).toDays() <= 30)
				.sorted(Comparator.comparing(Claim::getCreatedDateTime)).limit(8).toList();
		log.info("Aged Claim list size ::"+ agedClaimList.size());
		List<GetClaimWatchlistDTO> workQueueDetailsList = new ArrayList<>();
		for (Claim claimData : agedClaimList) {
			GetClaimWatchlistDTO dto=new GetClaimWatchlistDTO();
			dto.setDays((int) Duration.between(claimData.getCreatedDateTime(), LocalDateTime.now()).plusDays(1).toDays());
			//dto.setCustomerName(getLoggedInUserName(userId));
			 String custometName = claimData.getClaimAddresses().stream()
		                .filter(x -> x.getAddressTypeId()==14)
		                .map(ClaimAddress::getFullName)                    
		                .findAny()
		                .orElse("");
			 dto.setCustomerName(custometName);
			dto.setCustomerNumber(claimData.getCustomerNumber());
			dto.setWorkStatus(
					workStatusRepository.findworkStatusDescriptionByWorkStatusId(claimData.getWorkStatusId()));
			dto.setClaimNumber(claimData.getClaimNumber());
			dto.setClaimId(claimData.getClaimId());
			workQueueDetailsList.add(dto);
		}
		return workQueueDetailsList;
	}

	private String getLoggedInUserName(int userId) {
			String userName = "";
			String url = "/users/v1/findUserName?userId=" + userId;
			String finalUrl = restSecurityUrl + url;
			log.info("Final Security Url :: " + finalUrl);
			String response = restTemplate.getForObject(finalUrl, String.class);
			if (response != null) {
				userName = response;
			}
			return userName;
	}

	@Override
	public List<GetClaimWatchlistDTO> claimsDue(int userId) {
		List<Claim> claimList = getClaims(userId);
		log.info("Claim list size before ::"+ claimList.size());
		List<Claim> lastActivityClaimList =  claimList.stream().filter(data-> Duration.between(data.getModifiedDateTime(), LocalDateTime.now()).toDays() <= 7)
				.sorted(Comparator.comparing(Claim::getModifiedDateTime)).limit(4).toList();
		log.info("Last Activity Claim list size ::"+ lastActivityClaimList.size());
		List<GetClaimWatchlistDTO> workQueueDetailsList = new ArrayList<>();
		for (Claim claimData : lastActivityClaimList) {
			GetClaimWatchlistDTO dto=new GetClaimWatchlistDTO();
			dto.setDays((int) Duration.between(claimData.getCreatedDateTime(), LocalDateTime.now()).plusDays(1).toDays());
			//dto.setCustomerName(getLoggedInUserName(userId));
			 String custometName = claimData.getClaimAddresses().stream()
		                .filter(x -> x.getAddressTypeId()==14)
		                .map(ClaimAddress::getFullName)                    
		                .findAny()
		                .orElse("");
			dto.setCustomerName(custometName);
			dto.setCustomerNumber(claimData.getCustomerNumber());
			dto.setWorkStatus(
					workStatusRepository.findworkStatusDescriptionByWorkStatusId(claimData.getWorkStatusId()));
			dto.setClaimNumber(claimData.getClaimNumber());
			dto.setClaimId(claimData.getClaimId());
			workQueueDetailsList.add(dto);
		}
		return workQueueDetailsList;
	}
	
	private List<Claim> getClaims(Integer userId){
		return claimDetailsService.getClaims(getUsersIds(userId));
	}
	
	private List<Integer> getUsersIds(int userId) {
		String url = "/users/v1/getUsersIds?userId=" + userId;
		String finalUrl = restSecurityUrl + url;
		log.info("Final Security Url :: " + finalUrl);
		ResponseEntity<List<Integer>> responseEntity = restTemplate.exchange(
				finalUrl,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Integer>>() {});
		List<Integer> response = responseEntity.getBody();
		if (response == null || response.isEmpty()) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found with userId :: " + userId);
		}
		return response;
	}
}
