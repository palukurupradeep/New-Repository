package com.shaw.claims.repo;

import com.shaw.claims.model.DetailRecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailRecordTypeRepository extends JpaRepository<DetailRecordType,Integer> {
    @Query("SELECT d.detailRecordTypeId FROM DetailRecordType d WHERE d.detailRecordTypeCode =:detailRecordTypeCode")
    int findDetailRecordTypeIdByDetailRecordTypeCode(@Param("detailRecordTypeCode") String detailRecordTypeCode);

    DetailRecordType findByDetailRecordTypeCode(@Param("detailRecordTypeCode") String detailRecordTypeCode);
}
