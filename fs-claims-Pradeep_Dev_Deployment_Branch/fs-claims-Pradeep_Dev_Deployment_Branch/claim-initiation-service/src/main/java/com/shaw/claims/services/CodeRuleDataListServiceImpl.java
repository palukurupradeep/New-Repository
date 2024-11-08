package com.shaw.claims.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.SellingCompany;
import com.shaw.claims.repo.SellingCompanyRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class CodeRuleDataListServiceImpl implements CodeRuleDataListService {
	@Autowired
	SellingCompanyRepository repo;
	@Autowired
	EntityManager entityManager;

	@Override
	public List<String> fetchDataListByDataType(String DataType) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SellingCompany> query = cb.createQuery(SellingCompany.class);
		Root<SellingCompany> sellingCompanyRoot = query.from(SellingCompany.class);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(sellingCompanyRoot.get(DataType), 1));
		query.where(predicates.toArray(new Predicate[0]));
		List<SellingCompany> sellingList = entityManager.createQuery(query).getResultList();
		if (sellingList == null || sellingList.isEmpty())
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Record Not Found with " + DataType);
		List<String> sellingcomplanyCodesData = sellingList.stream().map(x -> x.getSellingCompanyCode())
				.collect(Collectors.toList());

		return sellingcomplanyCodesData;

	}

}
