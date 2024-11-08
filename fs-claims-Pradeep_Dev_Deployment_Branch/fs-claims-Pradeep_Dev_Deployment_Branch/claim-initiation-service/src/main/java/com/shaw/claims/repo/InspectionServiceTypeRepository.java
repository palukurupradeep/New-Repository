package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.InspectionServiceType;

@Repository
public interface InspectionServiceTypeRepository  extends JpaRepository<InspectionServiceType, Integer> {

	List<InspectionServiceType> findByStatusIdOrderByDisplaySequenceAsc(int statusId);


    @Query("SELECT ist.inspectionServiceTypeId FROM InspectionServiceType ist WHERE ist.inspectionServiceTypeCode =:serviceTypeCode")
    int findInspectionServiceTypeIdByInspectionServiceTypeCode(@Param("serviceTypeCode") String serviceTypeCode);
    @Query("SELECT ist.inspectionServiceTypeCode FROM InspectionServiceType ist WHERE ist.inspectionServiceTypeId =:inspectionServiceTypeId")
    String findInspectionServiceTypeCodeByInspectionServiceTypeId(@Param("inspectionServiceTypeId") int inspectionServiceTypeId);
    @Query("SELECT ist.inspectionServiceTypeName FROM InspectionServiceType ist WHERE ist.inspectionServiceTypeId =:inspectionServiceTypeId")
    String findInspectionServiceTypeNameByInspectionServiceTypeId(@Param("inspectionServiceTypeId") int inspectionServiceTypeId);
    InspectionServiceType findByInspectionServiceTypeCode(@Param("inspectionServiceTypeCode") String inspectionServiceTypeCode);
}

