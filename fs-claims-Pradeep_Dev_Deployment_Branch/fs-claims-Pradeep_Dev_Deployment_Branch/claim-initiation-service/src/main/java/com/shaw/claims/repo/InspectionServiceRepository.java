package com.shaw.claims.repo;

import com.shaw.claims.dto.InspectionServicesResponseDTO;
import com.shaw.claims.model.InspectionService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface InspectionServiceRepository extends JpaRepository<InspectionService,Integer> {
    @Query("SELECT ise.inspectionServiceId FROM InspectionService ise WHERE ise.inspectionServiceCode =:inspectionServiceCode")
    int findInspectionServiceIdByInspectionServiceCode(@Param("inspectionServiceCode") String inspectionServiceCode);

    List<InspectionService> findByStatusIdOrderByDisplaySequenceAsc(int active);

    InspectionService findByInspectionServiceCode(@Param("inspectionServiceCode") String inspectionServiceCode);
}