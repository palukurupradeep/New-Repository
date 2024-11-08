package com.shaw.claims.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.dto.CertifyingAgencyDTO;
import com.shaw.claims.model.CertifyingAgency;
import com.shaw.claims.repo.CertifyingAgencyRepository;

@Service
public class CertifyingAgencyServiceImpl implements CertifyingAgencyService {

	@Autowired
	CertifyingAgencyRepository certifyingAgencyRepository;

	@Override
	public List<CertifyingAgencyDTO> getAllCertifyingAgency() {

		List<CertifyingAgency> certifyingAgencies = certifyingAgencyRepository
				.findByStatusIdOrderByDisplaySequenceAsc(CommonConstant.ACTIVE);
		return certifyingAgencies.stream().map(data -> {
			CertifyingAgencyDTO certifyingAgencyDTO = new CertifyingAgencyDTO();
			BeanUtils.copyProperties(data, certifyingAgencyDTO);
			return certifyingAgencyDTO;
		}).collect(Collectors.toList());
	}

}
