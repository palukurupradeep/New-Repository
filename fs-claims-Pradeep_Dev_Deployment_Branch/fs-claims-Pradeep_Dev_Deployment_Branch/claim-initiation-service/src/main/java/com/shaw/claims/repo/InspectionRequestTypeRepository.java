package com.shaw.claims.repo;

import com.shaw.claims.dto.InspectionRequestTypeResponseDTO;
import com.shaw.claims.model.InspectionRequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectionRequestTypeRepository extends JpaRepository<InspectionRequestType,Integer> {
    List<InspectionRequestType> findByStatusIdOrderByDisplaySequenceAsc(int active);
}
