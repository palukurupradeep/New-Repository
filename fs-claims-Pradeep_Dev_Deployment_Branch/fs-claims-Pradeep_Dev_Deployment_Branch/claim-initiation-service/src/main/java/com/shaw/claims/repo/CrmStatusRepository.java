package com.shaw.claims.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.CrmStatus;

@Repository
public interface CrmStatusRepository extends JpaRepository<CrmStatus, Integer> {

	CrmStatus findByCrmStatusCode(String crmStatusCode);

	CrmStatus findByCrmStatusId(int crmStatusId);
}
