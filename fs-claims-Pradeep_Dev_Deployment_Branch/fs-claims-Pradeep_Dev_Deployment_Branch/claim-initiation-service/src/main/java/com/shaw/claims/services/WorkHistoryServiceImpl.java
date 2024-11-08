package com.shaw.claims.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.shaw.claims.enums.StatusTypes;
import com.shaw.claims.repo.*;
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

import com.shaw.claims.dto.UsersDTO;
import com.shaw.claims.dto.WorkHistoryDTO;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.Claim;
import com.shaw.claims.model.State;
import com.shaw.claims.model.WorkHistory;

@Service
public class WorkHistoryServiceImpl implements WorkHistoryService {
	Logger log = LogManager.getLogger(WorkHistoryServiceImpl.class);
	@Autowired
	WorkHistoryRepository workHistoryRepo;
	@Autowired
	ClaimRepository claimRepo;
	@Autowired
	WorkStatusRepository workStatusRepo;
	//	ClaimServiceImpl claimServiceImpl;
	@Autowired
	ClaimAddressRepository claimAddressRepo;
	@Autowired
	EndUserInformationRepository endUserInfoRepo;
	@Autowired
	StateRepository stateRepo;
	@Value(("${claim-security.rest.url}"))
	private String restSecurityUrl;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private StatusRepository statusRepository;

	public void updateWorkHistory(Claim claim, Integer modifiedUserId) {
		log.info("claim id :: {}", claim.getClaimId());
		Optional<WorkHistory> workHistInfo = workHistoryRepo.findByClaimAndModifiedByUserId(claim, modifiedUserId);
		WorkHistory workHistory = null;
		if (workHistInfo.isEmpty()) { 
			workHistory = new WorkHistory();
			workHistory.setClaim(claim);
			workHistory.setCreatedDateTime(LocalDateTime.now());
			workHistory.setCreatedByUserId(modifiedUserId);
		} else {
			workHistory = workHistInfo.get();
		}
		workHistory.setStatusId(1);
		workHistory.setModifiedDateTime(LocalDateTime.now());
		workHistory.setModifiedByUserId(modifiedUserId);
		workHistoryRepo.save(workHistory);
	}

	public List<WorkHistoryDTO> getWorkHistory(Integer userId) {

		List<WorkHistory> workHistoryList = workHistoryRepo.findByCreatedByUserId(userId);
	
		List<WorkHistory> workHistoryCurrentDateList = workHistoryList.stream()
				.sorted(Comparator.comparing(WorkHistory::getModifiedDateTime).reversed())
				.filter(data -> Duration.between(data.getModifiedDateTime().toLocalDate().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays() == 0)
				.collect(Collectors.toList());

		List<WorkHistoryDTO> workHistoryDtoList = workHistoryCurrentDateList.stream()
				
				.map(data -> {
			WorkHistoryDTO workHistoryDto = new WorkHistoryDTO();

			if (data.getClaim() != null) {
				String workStatusDescription = workStatusRepo.findworkStatusDescriptionByWorkStatusId(data.getClaim().getWorkStatusId());
				
				workHistoryDto.setWorkStatus(workStatusDescription);
				
				UsersDTO userInfo = getUsersInfo(data.getClaim().getClaimUserId());

				if (userInfo != null) {
					workHistoryDto.setUsername(userInfo.getFirstName() + " " + userInfo.getLastName());
				}

				workHistoryDto.setClaimNumber(data.getClaim().getClaimNumber());
				workHistoryDto.setClaimAmount(data.getClaim().getClaimAmountUsd());
				workHistoryDto.setReasonCode(data.getClaim().getClaimReasonDefinition().getClaimReasonCode());
				workHistoryDto.setCustomerNumber(data.getClaim().getCustomerNumber());

				if (data.getClaim().getClaimAddresses() != null) {
					data.getClaim().getClaimAddresses().stream().forEach(claimAddress -> {
						if (claimAddress.getAddressTypeId() == 14) {
							workHistoryDto.setCustomerName(claimAddress.getFullName());
							workHistoryDto.setCity(claimAddress.getCity());
							State state = stateRepo.findStatesByIsoStateId(claimAddress.getStateId());
							if (state != null) {
								workHistoryDto.setState(state.getIsoStateCode());
							}
							workHistoryDto.setZip(claimAddress.getPostalCode());
						}
					});

				}

				if (data.getClaim().getEndUserInformation() != null) {
					workHistoryDto.setEndUserName(data.getClaim().getEndUserInformation().getFirstName() + " "
							+ data.getClaim().getEndUserInformation().getLastName());
				}
				workHistoryDto.setWorkTime(data.getModifiedDateTime());
			}
			return workHistoryDto;
		}).collect(Collectors.toList());

		return workHistoryDtoList;

	}

	@Override
	public void setWorkHistory(Claim claim) {
		log.info("claim id :: {}", claim.getClaimId());
		if(claim != null){
			Optional<WorkHistory> workHistInfo = workHistoryRepo.findByClaim(claim);
			WorkHistory workHistory = null;
			if(workHistInfo.isPresent()){
				workHistory = workHistInfo.get();
				workHistory.setModifiedDateTime(LocalDateTime.now());
				claim.getWorkHistory().add(workHistory);
			}else{
				workHistory = new WorkHistory();
				workHistory.setClaim(claim);
				workHistory.setCreatedByUserId(claim.getModifiedByUserId());
				claim.getWorkHistory().add(workHistory);
			}
			workHistory.setStatusId(StatusTypes.ACTIVE.getStatusId());
			workHistory.setModifiedByUserId(claim.getModifiedByUserId());
		}
	}

	private UsersDTO getUsersInfo(int userId) {
		String url = "/users/v1/findUserById?userId=" + userId;
		String finalUrl = restSecurityUrl + url;
		log.info("Final Security Url :: " + finalUrl);
		ResponseEntity<UsersDTO> responseEntity = restTemplate.exchange(finalUrl, HttpMethod.GET, null,
				new ParameterizedTypeReference<UsersDTO>() {
		});
		UsersDTO response = responseEntity.getBody();
		if (response == null) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found with userId :: " + userId);
		}
		return response;
	}
}
