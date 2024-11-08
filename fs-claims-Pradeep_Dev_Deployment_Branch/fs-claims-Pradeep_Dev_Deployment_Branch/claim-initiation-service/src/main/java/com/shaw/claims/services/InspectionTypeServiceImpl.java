package com.shaw.claims.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.dto.InspectionServiceTypeDTO;
import com.shaw.claims.repo.InspectionServiceTypeRepository;

@Service
public class InspectionTypeServiceImpl implements InspectionTypeService {

	@Autowired
	InspectionServiceTypeRepository inspectionServiceTypeRepository;

	@Override
	public List<InspectionServiceTypeDTO> getAllInspectionServiceType() {
		return inspectionServiceTypeRepository.findByStatusIdOrderByDisplaySequenceAsc(CommonConstant.ACTIVE).stream().map(data -> {
			InspectionServiceTypeDTO inspectionServiceTypeDTO = new InspectionServiceTypeDTO();
			BeanUtils.copyProperties(data, inspectionServiceTypeDTO);
			return inspectionServiceTypeDTO;
		}).collect(Collectors.toList());
	}

}
