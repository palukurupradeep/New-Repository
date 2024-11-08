package com.shaw.claims.repo;

import com.shaw.claims.model.AreaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaTypeRepository extends JpaRepository<AreaType,Integer> {
    @Query("SELECT a.areaTypeId FROM AreaType a WHERE a.areaTypeCode =:areaTypeCode")
    int findAreaTypeIdByAreaTypeCode(@Param("areaTypeCode") String areaTypeCode);
}
