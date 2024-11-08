package com.shaw.claims.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.*;
import com.shaw.claims.util.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shaw.claims.dto.UserWorkQueueFieldDTO;
import com.shaw.claims.dto.UsersDTO;
import com.shaw.claims.dto.WorkQueueDetailsDTO;
import com.shaw.claims.dto.WorkQueueFieldsResponseDTO;
import com.shaw.claims.dto.WorkQueueResponseDTO;
import com.shaw.claims.repo.AddressTypeRepository;
import com.shaw.claims.repo.ClaimRepository;
import com.shaw.claims.repo.ClaimRouteRepository;
import com.shaw.claims.repo.SellingCompanyRepository;
import com.shaw.claims.repo.StateRepository;
import com.shaw.claims.repo.UserWorkQueueFieldRepositroy;
import com.shaw.claims.repo.WorkQueueRepository;
import com.shaw.claims.repo.WorkStatusRepository;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkQueueServiceImpl implements WorkQueueService{
    Logger log = LogManager.getLogger(WorkQueueServiceImpl.class);
    @Autowired
	WorkStatusRepository workStatusRepository;
    @Autowired
    SellingCompanyRepository sellingCompanyRepository;
    @Autowired
    StateRepository stateRepository;
    @Autowired
    AddressTypeRepository addressTypeRepository;
    @Autowired
    WorkQueueRepository workQueueRepository;
    @Autowired
    UserWorkQueueFieldRepositroy userWorkQueueFieldRepositroy;
	@Autowired
	ClaimDetailsService claimDetailsService;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	DateUtil dateUtil;
	@Value(("${claim-security.rest.url}"))
	private String restSecurityUrl;
	@Autowired
	ClaimRouteRepository claimRouteRepository;
	@Autowired
	ClaimRepository claimRepository;
	@Override
	public WorkQueueResponseDTO getClaimWorkQueues(int userId) {
		return getWorkQueueDetailsDTO(userId, null);
	}
	public WorkQueueResponseDTO getWorkQueueDetailsDTO(Integer userId, List<Claim> claimList) {
		List<Claim> clmList = userId != null ? getClaims(userId) : claimList;
		List<Claim> filteredClaimsList = getFilteredClaims(clmList);
		WorkQueueResponseDTO workQueueResponseDTO = new WorkQueueResponseDTO();
		List<WorkQueueDetailsDTO> workQueueDetailsList = new ArrayList<>();
		for (Claim claimData : filteredClaimsList) {
			WorkQueueDetailsDTO dto = new WorkQueueDetailsDTO();
			UsersDTO usersDTO=getUsersDetailsByUserId(claimData.getClaimUserId());
			dto.setUserName(usersDTO.getFirstName()+" "+usersDTO.getLastName());
			dto.setClaimNumber(claimData.getClaimNumber());
			dto.setJobStop(claimData.getJobStopped());
			dto.setPriorityClaim(claimData.getPriorityClaim());
			dto.setWorkStatus(
					workStatusRepository.findworkStatusDescriptionByWorkStatusId(claimData.getWorkStatusId()));
			dto.setClaimAmount(claimData.getClaimAmountUsd());
			dto.setClaimReasonCode(Optional.ofNullable(claimData.getClaimReasonDefinition())
					.map(ClaimReasonDefinition::getClaimReasonCode).orElse(""));
			dto.setClaimReasonDescription(Optional.ofNullable(claimData.getClaimReasonDefinition())
					.map(ClaimReasonDefinition::getClaimReasonDescription).orElse(""));
			dto.setClaimAge(Duration.between(claimData.getCreatedDateTime(), LocalDateTime.now()).toDays());
			dto.setCreatedDate(claimData.getCreatedDateTime());
			dto.setLastActivityAge(Duration.between(claimData.getModifiedDateTime(), LocalDateTime.now()).toDays());
			;
			dto.setModifiedDate(claimData.getModifiedDateTime());
			dto.setSellingCompanyId(claimData.getSellingCompany());
			dto.setSellingCompanyName(
					sellingCompanyRepository.findSellingCompnayNameBySellingCompanyCode(claimData.getSellingCompany()));
			;
			dto.setCustomerNumber(claimData.getCustomerNumber());
			dto.setGlobalorNational(
					Optional.ofNullable(claimData.getLookup()).map(Lookup::getLookupDescription).orElse(""));
			dto.setEndUserName(
					Optional.ofNullable(claimData.getEndUserInformation()).map(eu -> {
						return eu.getFirstName() + " " +  eu.getLastName();
					}).orElse(""));

			dto.setCustomerName(claimData.getClaimAddresses().stream().filter(data->data.getAddressTypeId()==14).map(ClaimAddress::getFullName).findAny().orElse(""));

			State stateData = claimData.getClaimAddresses().stream().filter(x ->addressTypeRepository.
					findAddressTypeCodeByAddressTypeId(x.getAddressTypeId()).equalsIgnoreCase("CBM"))
					.map(s -> stateRepository.findStatesByIsoStateId(s.getStateId())).findAny().orElse(null);

			dto.setStateCode(
					Optional.ofNullable(stateData).stream().map(State::getIsoStateCode).findAny().orElse(""));
			dto.setStateName(
					Optional.ofNullable(stateData).stream().map(State::getIsoStateName).findAny().orElse(""));
			dto.setTraceDate(claimData.getTraceTasks().stream().map(TraceTask::getTraceDate).sorted(Comparator.reverseOrder()).findFirst().orElse(null));
			workQueueDetailsList.add(dto);
		}

		workQueueResponseDTO.setWorkQueueDetailsDTO(workQueueDetailsList);
		return workQueueResponseDTO;
	}
	public List<Claim> getFilteredClaims(List<Claim> clmList) {
		List<Claim> respClaimList = new ArrayList<>();
        List<Claim> claimList = new ArrayList<>(clmList);
		log.info("Claim list size before ::"+ claimList.size());
		//Priority with JobStop Claim list
		List<Claim> jobPClaimList =  claimList.stream().filter(c-> c.getJobStopped() && c.getPriorityClaim())
				.sorted(Comparator.comparing(Claim::getModifiedDateTime)).toList();
		log.info("Priority with JobStop Claim list size ::"+ jobPClaimList.size());
		respClaimList.addAll(jobPClaimList);
		//Priority without JobStop Claim list
		List<Claim> pWithoutJobClaimList =  claimList.stream().filter(c-> !c.getJobStopped() && c.getPriorityClaim())
				.sorted(Comparator.comparing(Claim::getModifiedDateTime)).toList();
		log.info("Priority without JobStop Claim list size ::"+ pWithoutJobClaimList.size());
		respClaimList.addAll(pWithoutJobClaimList);
		//Non Priority Claim list
		List<Claim> nonPClaimList = claimList.stream().filter(c-> !c.getJobStopped() && !c.getPriorityClaim())
				.filter(data -> Duration.between(data.getModifiedDateTime(), LocalDateTime.now()).toDays() > 7)
				.sorted(Comparator.comparing(Claim::getModifiedDateTime)).toList(); //.reversed() -- for DESC order
		log.info("Non Priority Claim list size ::"+ nonPClaimList.size());
		respClaimList.addAll(nonPClaimList);
		//Initiated not worked Claim list
		List<Claim> notWorkClaimList = claimList.stream().filter(data -> Duration.between(data.getModifiedDateTime(), LocalDateTime.now()).toDays() <= 7)
				.filter(c->c.getCreatedDateTime().equals(c.getModifiedDateTime()))
				.sorted(Comparator.comparing(Claim::getModifiedDateTime)).toList();

		respClaimList.addAll(notWorkClaimList);
		log.info("Initiated not worked Claim list size ::"+ notWorkClaimList.size());

		//Remaining Claim list
		if(claimList.size()!=respClaimList.size()) {
			if(!respClaimList.isEmpty())
				claimList.removeAll(respClaimList);
			claimList.stream().sorted(Comparator.comparing(Claim::getModifiedDateTime)).toList();
			log.info("Remaining Claim list size ::"+ claimList.size());
			//Response Claim list
			respClaimList.addAll(claimList);
			log.info("Response Claim list size ::"+ respClaimList.size());
		}

		return respClaimList;
	}
	@Override
	public WorkQueueResponseDTO getPriorityClaims(Integer userId){
		List<Claim> claimList = getClaims(userId);
		log.info("Claim list size before ::"+ claimList.size());
		List<Claim> pClaimList =  claimList.stream().filter(Claim::getPriorityClaim)
				.sorted(Comparator.comparing(Claim::getModifiedDateTime)).toList();
		log.info("Priority Claim list size ::"+ pClaimList.size());
		return getWorkQueueDetailsDTO(null, pClaimList);
	}
	@Override
	public Map<String, String> previousDayClaims(Integer userId){
		Map<String, String> map = new HashMap<>();
		List<Claim> claimList = getClaims(userId);
		log.info("Claim list size before ::"+ claimList.size());
		List<Claim> prevDayClaimList =  claimList.stream().filter(data-> dateUtil.isWithinLastDay(data.getModifiedDateTime().toLocalDate()))
				.toList();
		log.info("prevDayClaimList size ::"+ prevDayClaimList.size());
		if(!prevDayClaimList.isEmpty()) {
			List<Claim> prevDayStatusClaimList = prevDayClaimList.stream().filter(clm-> clm.getClaimStatusId() == 2 || clm.getClaimStatusId() == 4 || clm.getClaimStatusId() == 5)
					.toList();
			map.put("prevDayAllClaims", String.valueOf(prevDayClaimList.size()));
			map.put("prevDayStatusClaims", String.valueOf(prevDayStatusClaimList.size()));
			map.put("percentage", String.valueOf((prevDayStatusClaimList.size()*100) / prevDayClaimList.size()));
			log.info("Previous Day Status Claim list size ::"+ prevDayStatusClaimList.size());
		}
		return map;
	}
	@Override
	public WorkQueueResponseDTO lastActivityClaims(Integer userId, Integer days){
		List<Claim> claimList = getClaims(userId);
		log.info("Claim list size before ::"+ claimList.size());
		List<Claim> lastActivityClaimList =  claimList.stream().filter(data-> Duration.between(data.getModifiedDateTime(), LocalDateTime.now()).toDays() > days)
				.sorted(Comparator.comparing(Claim::getModifiedDateTime)).toList();
		log.info("Last Activity Claim list size ::"+ lastActivityClaimList.size());
		return getWorkQueueDetailsDTO(null, lastActivityClaimList);
	}
	@Override
	public WorkQueueResponseDTO agedClaims(Integer userId, Integer days){
		List<Claim> claimList = getClaims(userId);
		log.info("Claim list size before ::"+ claimList.size());
		List<Claim> agedClaimList =  claimList.stream().filter(data-> Duration.between(data.getCreatedDateTime(), LocalDateTime.now()).toDays() > days)
				.sorted(Comparator.comparing(Claim::getCreatedDateTime)).toList();
		log.info("Aged Claim list size ::"+ agedClaimList.size());
		return getWorkQueueDetailsDTO(null, agedClaimList);
	}
	private List<Claim> getClaims(Integer userId){
		 List<Claim> originalClaimData=claimDetailsService.getClaims(getUsersIds(userId));
		List<Integer> claimIdsList= originalClaimData.stream().map(p->p.getClaimId()).collect(Collectors.toList());
		List< ClaimRoute> routedClaimList = claimRouteRepository.findByRoutedToUserId(userId);
		List<Claim> routedClaimData=new ArrayList<>();
		for (ClaimRoute claimRoute : routedClaimList) {
			if(!claimIdsList.contains(claimRoute.getClaim().getClaimId()))
			{
				routedClaimData.add(claimRoute.getClaim());
			}
			
		}
		
		originalClaimData.addAll(routedClaimData);
		//return claimDetailsService.getClaims(getUsersIds(userId));
		return  originalClaimData;
	}
	@Override
	public List<WorkQueueFieldsResponseDTO> fetchWorkQueueFields() {
		List<WorkQueueFieldsResponseDTO> workQueueFieldslistData=new ArrayList<>();
		List<WorkQueueField> result=workQueueRepository.fetchWorkQueueFields();
		for (WorkQueueField workQueueField : result) {
			WorkQueueFieldsResponseDTO workQueueFieldsResponseDTO=new WorkQueueFieldsResponseDTO();
			workQueueFieldsResponseDTO.setWorkQueueFieldId(workQueueField.getWorkQueueFieldId());
			workQueueFieldsResponseDTO.setWorkQueueFieldName(workQueueField.getWorkQueueFieldName());
			workQueueFieldsResponseDTO.setDisplaySequence(workQueueField.getDisplaySequence());
			workQueueFieldslistData.add(workQueueFieldsResponseDTO);
		}
		return workQueueFieldslistData;
	}
	@Override
	public void addUserWorkQueueFields(UserWorkQueueFieldDTO userWorkQueueFieldDTO) {
	List<UserWorkQueueField> userWorkQueueFieldListData=userWorkQueueFieldRepositroy.
	findUserWorkQueueFieldsByUserId(userWorkQueueFieldDTO.getCreatedByUserId());
	List<UserWorkQueueField> userWorkQueueFieldList=new ArrayList<>();
	if(!userWorkQueueFieldListData.isEmpty())
	{
		deleteUserWorkQueueFields(userWorkQueueFieldDTO.getCreatedByUserId());
	}
		for(WorkQueueField workQueueField:userWorkQueueFieldDTO.getWorkQueueField())
		{
			 UserWorkQueueField userWorkQueueField=new UserWorkQueueField();
			userWorkQueueField.setCreatedByUserId(userWorkQueueFieldDTO.getCreatedByUserId());
			userWorkQueueField.setModifiedByUserId(userWorkQueueFieldDTO.getModifiedByUserId());
			userWorkQueueField.setWorkQueueField(workQueueField);
			userWorkQueueField.setStatusId(1);
			userWorkQueueField.setDisplaySequence(workQueueField.getDisplaySequence());
			userWorkQueueFieldList.add(userWorkQueueField);
		}
		userWorkQueueFieldRepositroy.saveAll(userWorkQueueFieldList);
	}
	@Override
	public void deleteUserWorkQueueFields(Integer createdByUserId) {
		 userWorkQueueFieldRepositroy.deleteByUserWorkQueueFields(createdByUserId);
	}
	@Override
	public List<WorkQueueFieldsResponseDTO> fetchUserWorkQueueFieldsByUserId(Integer createdByUserId) {
		List<WorkQueueFieldsResponseDTO> workQueueFieldslistData=new ArrayList<>();
		List<UserWorkQueueField> result=userWorkQueueFieldRepositroy.findUserWorkQueueFieldsByUserId(createdByUserId);
		if(!result.isEmpty())
		{
			for (UserWorkQueueField userWorkQueueField : result) {
				WorkQueueFieldsResponseDTO workQueueFieldsResponseDTO=new WorkQueueFieldsResponseDTO();
				workQueueFieldsResponseDTO.setWorkQueueFieldId(userWorkQueueField.getWorkQueueField().getWorkQueueFieldId());
				workQueueFieldsResponseDTO.setWorkQueueFieldName(userWorkQueueField.getWorkQueueField().getWorkQueueFieldName());
				workQueueFieldsResponseDTO.setDisplaySequence(userWorkQueueField.getDisplaySequence());
				workQueueFieldslistData.add(workQueueFieldsResponseDTO);
			}
		return workQueueFieldslistData;
		}
		return fetchWorkQueueFields();
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
	public UsersDTO getUsersDetailsByUserId(int userId) {
		String url = "/users/v1/findUserById?userId=" + userId;
		String finalUrl = restSecurityUrl + url;
		log.info("Final Security Url :: " + finalUrl);
		ResponseEntity<UsersDTO> responseEntity = restTemplate.exchange(
				finalUrl,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<UsersDTO>() {});
		UsersDTO response = responseEntity.getBody();
		if (response == null) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found with userId :: " + userId);
		}
		return response;
	}
}
