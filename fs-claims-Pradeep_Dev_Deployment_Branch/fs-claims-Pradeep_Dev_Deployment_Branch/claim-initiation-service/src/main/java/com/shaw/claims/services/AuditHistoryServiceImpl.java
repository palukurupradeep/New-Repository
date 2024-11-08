package com.shaw.claims.services;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shaw.claims.dto.AuditHistoryDTO;
import com.shaw.claims.model.AuditHistory;
import com.shaw.claims.repo.AuditHistoryRepository;

@Service
public class AuditHistoryServiceImpl implements AuditHistoryService{
    @Autowired
    AuditHistoryRepository auditHistoryRepository;
    @Override
    public List<AuditHistoryDTO> fetchAuditHistoryByClaimId(Integer claimId) {
        List<AuditHistory> auditHistoryList = auditHistoryRepository.fetchAuditHistoryByClaimId(claimId);
        List<AuditHistory> auditHistorySortedList;
        if(auditHistoryList != null) {
        	auditHistorySortedList = auditHistoryList.stream()
			.sorted(Comparator.comparing(AuditHistory::getModifiedDateTime).reversed())
			.collect(Collectors.toList());
        	 return auditHistorySortedList.stream().map(auditHistory -> {
                 AuditHistoryDTO auditHistoryDTO = new AuditHistoryDTO();
                 BeanUtils.copyProperties(auditHistory,auditHistoryDTO);
                 auditHistoryDTO.setCreatedDate(auditHistory.getCreatedDateTime());
                 return auditHistoryDTO;
             }).toList();
        }
       return null; 
    }
}
