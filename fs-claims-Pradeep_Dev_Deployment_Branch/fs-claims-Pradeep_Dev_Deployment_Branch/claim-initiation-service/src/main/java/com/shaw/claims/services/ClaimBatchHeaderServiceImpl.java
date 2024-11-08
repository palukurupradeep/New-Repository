package com.shaw.claims.services;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shaw.claims.dto.ClaimBatchDetailsDTO;
import com.shaw.claims.dto.ClaimBatchHeaderDTO;
import com.shaw.claims.dto.ClaimReasonDefinitionDTO;
import com.shaw.claims.dto.UserGroupDTO;
import com.shaw.claims.dto.UsersDTO;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.BatchStatus;
import com.shaw.claims.model.ClaimBatchDetail;
import com.shaw.claims.model.ClaimBatchHeader;
import com.shaw.claims.model.ClaimReasonDefinition;
import com.shaw.claims.model.UnitOfMeasure;
import com.shaw.claims.repo.BatchStatusRepository;
import com.shaw.claims.repo.ClaimBatchDetailRepository;
import com.shaw.claims.repo.ClaimBatchHeaderRepository;
import com.shaw.claims.repo.ClaimReasonDefinitionRepository;
import com.shaw.claims.repo.UnitOfMeasureRepository;
import com.shaw.claims.util.DateUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class ClaimBatchHeaderServiceImpl implements ClaimBatchHeaderService{
    Logger log = LogManager.getLogger(ClaimBatchHeaderServiceImpl.class);
    @Autowired
    private BatchStatusRepository batchStatusRepository;
    @Autowired
    private ClaimBatchHeaderRepository batchHeaderRepository;
    @Autowired
    private ClaimBatchDetailServiceImpl detailService;
    @Autowired
    private ClaimBatchDetailRepository claimBatchDetailRepository;
    @Autowired
	ClaimReasonDefinitionRepository claimReasonDefinitionRepository;
    @Autowired
    UnitOfMeasureRepository unitOfMeasureRepository;
    @Autowired
    EntityManager entityManager;
    @Value(("${claim-security.rest.url}"))
    private String restSecurityUrl;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    DateUtil dateUtil;
   
    


    @Override
    public void create(List<ClaimBatchHeaderDTO> claimBatchHeaderDTOS) {
        List<ClaimBatchHeader> claimBatchHeaderList =  claimBatchHeaderDTOS.stream()
                .map(batchHeaderDTO -> {
                    ClaimBatchHeader claimBatchHeader = new ClaimBatchHeader();
                    BeanUtils.copyProperties(batchHeaderDTO,claimBatchHeader);
                    claimBatchHeader.setBatchStatus(batchStatusRepository.findByBatchStatusCode(batchHeaderDTO.getBatchStatusCode()));
                    List<ClaimBatchDetail> claimBatchDetails = detailService.getClaimBatchDetail(batchHeaderDTO.getClaimBatchDetailsDTOS(), claimBatchHeader);
                    claimBatchHeader.setClaimBatchDetails(claimBatchDetails);
                    return claimBatchHeader;
                }).collect(Collectors.toList());
        batchHeaderRepository.saveAll(claimBatchHeaderList);
    }


	@Override
	public List<BatchStatus> getBatchStatus() {
		
		return batchStatusRepository.findAll();
	}

	@Override
	public List<ClaimBatchDetailsDTO> getClaimBatchDetail(Integer claimBatchHeaderId) {
	List<ClaimBatchDetail> claimBatchList=	claimBatchDetailRepository.
			findClaimBatchDetailByclaimBatchHeaderId(claimBatchHeaderId);
	List<ClaimBatchDetailsDTO> claimBatchDtoList=new ArrayList<>();
	
	for (ClaimBatchDetail claimBatchDetail : claimBatchList) {
		ClaimBatchDetailsDTO claimBatchDto = new ClaimBatchDetailsDTO();
		
		ClaimReasonDefinitionDTO claimReasonDto = new ClaimReasonDefinitionDTO();
		ClaimReasonDefinition claimReasonDef = claimReasonDefinitionRepository
				.findByClaimReasonId(claimBatchDetail.getClaimReasonDefinition().getClaimReasonId());
		UnitOfMeasure unitOfMeasure = unitOfMeasureRepository
				.findByUnitOfMeasureId(claimBatchDetail.getUnitOfMeasure().getUnitOfMeasureId());
		claimBatchDto.setCustomerNumber(claimBatchDetail.getCustomerNumber());
		claimBatchDto.setClaimReasonCode(claimReasonDef.getClaimReasonCode());
		claimBatchDto.setUnitOfMeasureCode(unitOfMeasure.getUnitOfMeasureCode());
		claimBatchDto.setInvoiceNumber(claimBatchDetail.getInvoiceNumber());
		claimBatchDto.setInvoiceDate(String.valueOf(claimBatchDetail.getInvoiceDate()));
		claimBatchDto.setStyleNumber(claimBatchDetail.getStyleNumber());
		claimBatchDto.setColorNumber(claimBatchDetail.getColorNumber());
		claimBatchDto.setClaimAmountUsd(claimBatchDetail.getClaimAmountUsd());
		claimBatchDto.setClaimNote(claimBatchDetail.getClaimNote());
		claimBatchDto.setClaimId(claimBatchDetail.getClaimId());
		claimBatchDto.setCreditMemoId(claimBatchDetail.getCreditMemoId());
		claimBatchDto.setIncentiveId(claimBatchDetail.getIncentiveId());
		claimBatchDto.setAssociateInvoiceNumber(claimBatchDetail.getAssociateInvoiceNumber());
		claimBatchDto.setAssociateInvoiceDate(String.valueOf(claimBatchDetail.getAssociateInvoiceDate()));
		claimBatchDto.setQuantity(claimBatchDetail.getQuantity());
		claimBatchDto.setUnitPriceUsd(claimBatchDetail.getUnitPriceUsd());
		claimBatchDto.setCreatedByUserId(claimBatchDetail.getCreatedByUserId());
		claimBatchDto.setProcessedByUserId(claimBatchDetail.getProcessedByUserId());
		claimBatchDto.setCreatedDateTime(String.valueOf(claimBatchDetail.getCreatedDateTime()));
		claimBatchDto.setProcessedDateTime(String.valueOf(claimBatchDetail.getProcessedDateTime()));
		claimBatchDtoList.add(claimBatchDto);
	}
		return claimBatchDtoList;
	}



    @Override
    public List<ClaimBatchHeaderDTO> fetchClaimBatchHeader(ClaimBatchHeaderDTO claimBatchHeaderDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ClaimBatchHeader> query = cb.createQuery(ClaimBatchHeader.class);
        Root<ClaimBatchHeader> claimBatchHeaderRoot = query.from(ClaimBatchHeader.class);
        List<Predicate> predicates = new ArrayList<>();
        if(claimBatchHeaderDTO.getUserGroupId() > 0)
            predicates.add(cb.equal(claimBatchHeaderRoot.get("userGroupId"), claimBatchHeaderDTO.getUserGroupId()));
        if(claimBatchHeaderDTO.getCreatedByUserId() > 0)
            predicates.add(cb.equal(claimBatchHeaderRoot.get("createdByUserId"), claimBatchHeaderDTO.getCreatedByUserId()));
        if (claimBatchHeaderDTO.getCreatedDateTime()!=null)
            predicates.add(cb.equal(claimBatchHeaderRoot.<LocalDate>get("createdDateTime").as(LocalDate.class), claimBatchHeaderDTO.getCreatedDateTime()));
        if (claimBatchHeaderDTO.getProcessedDateTime()!=null)
            predicates.add(cb.equal(claimBatchHeaderRoot.<LocalDate>get("processedDateTime").as(LocalDate.class), claimBatchHeaderDTO.getProcessedDateTime()));
        Join<ClaimBatchHeader, BatchStatus> batchStatusJoin = claimBatchHeaderRoot.join("batchStatus");
        if (claimBatchHeaderDTO.getBatchStatusCode()!=null && !claimBatchHeaderDTO.getBatchStatusCode().isEmpty())
            predicates.add(cb.equal(batchStatusJoin.get("batchStatusCode"), claimBatchHeaderDTO.getBatchStatusCode()));

        query.where(predicates.toArray(new Predicate[0]));
        List<ClaimBatchHeader> claimBatchHeaderList = entityManager.createQuery(query).getResultList();
        return claimBatchHeaderList.stream().map(this::setClaimBatchHeaderDTO).toList();
    }
    private ClaimBatchHeaderDTO setClaimBatchHeaderDTO(ClaimBatchHeader cbh) {
        ClaimBatchHeaderDTO claimBatchHeaderDTO = new ClaimBatchHeaderDTO();
        BeanUtils.copyProperties(cbh,claimBatchHeaderDTO);
        claimBatchHeaderDTO.setBatchStatusCode(cbh.getBatchStatus().getBatchStatusCode());
        claimBatchHeaderDTO.setCreatedDateTime(cbh.getCreatedDateTime().toLocalDate());
        claimBatchHeaderDTO.setProcessedDateTime(cbh.getProcessedDateTime().toLocalDate());
        //below needs to be pulled from the security service api call and then set it.
        claimBatchHeaderDTO.setUserGroupName(getUserGroups(cbh.getUserGroupId()).getUserGroupDescription());
        UsersDTO processeduserDTO= getUsersInfo(cbh.getProcessedByUserId());
        claimBatchHeaderDTO.setProcessedUserName(processeduserDTO.getFirstName()+" "+processeduserDTO.getLastName());
        UsersDTO createduserDTO= getUsersInfo(cbh.getCreatedByUserId());
        claimBatchHeaderDTO.setCreatedUserName(createduserDTO.getFirstName()+" "+createduserDTO.getLastName());
        return claimBatchHeaderDTO;
    }

    public UserGroupDTO getUserGroups(int userGroupId) {
        String url = "/users/v1/getUserGroupByUserGroupId?userGroupId=" + userGroupId;
        String finalUrl = restSecurityUrl + url;
        log.info("Final Security Url :: " + finalUrl);
        ResponseEntity<UserGroupDTO> responseEntity = restTemplate.exchange(
                finalUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<UserGroupDTO>() {
                });
        UserGroupDTO response = responseEntity.getBody();
        if (response == null) {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Record Not Found with userGroupId :: " + userGroupId);
        }
        return response;
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

