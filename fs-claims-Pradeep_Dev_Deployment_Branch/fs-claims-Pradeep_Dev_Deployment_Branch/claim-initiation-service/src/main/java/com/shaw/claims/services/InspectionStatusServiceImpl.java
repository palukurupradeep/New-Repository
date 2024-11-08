package com.shaw.claims.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.dto.InspectionStatusDTO;
import com.shaw.claims.repo.InspectionStatusRepository;

@Service
public class InspectionStatusServiceImpl implements InspectionStatusService {
	
	@Autowired
	InspectionStatusRepository inspectionStatusRepository;

	@Override
	public List<InspectionStatusDTO> getAllInspectionStatus() {
		return inspectionStatusRepository.findByStatusIdOrderByDisplaySequenceAsc(CommonConstant.ACTIVE).stream().map(data -> {
			InspectionStatusDTO inspectionStatusDTO = new InspectionStatusDTO();
			BeanUtils.copyProperties(data,  inspectionStatusDTO);
			return inspectionStatusDTO;
		}).collect(Collectors.toList());
	}
}
