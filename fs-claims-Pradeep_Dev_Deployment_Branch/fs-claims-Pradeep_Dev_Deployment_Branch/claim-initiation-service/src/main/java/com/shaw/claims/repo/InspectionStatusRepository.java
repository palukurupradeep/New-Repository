package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.InspectionStatus;

@Repository
public interface InspectionStatusRepository  extends JpaRepository<InspectionStatus, Integer>{

	List<InspectionStatus> findByStatusIdOrderByDisplaySequenceAsc(int active);

}
