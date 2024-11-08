package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.CertifyingAgency;

@Repository
public interface CertifyingAgencyRepository extends JpaRepository<CertifyingAgency, Integer> {

	List<CertifyingAgency> findByStatusIdOrderByDisplaySequenceAsc(int active);
	
    @Query("SELECT ca.certifyingAgencyId FROM CertifyingAgency ca WHERE ca.certifyingAgencyCode =:certifyingAgencyCode")
    int findCertifyingAgencyIdByCertifyingAgencyCode(@Param("certifyingAgencyCode") String certifyingAgencyCode);

    CertifyingAgency findByCertifyingAgencyCode(@Param("certifyingAgencyCode") String certifyingAgencyCode);
}
