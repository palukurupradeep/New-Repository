package com.shaw.claims.repo;

import com.shaw.claims.model.DetailStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailStatusTypeRepository extends JpaRepository<DetailStatusType,Integer> {
    @Query("SELECT dst FROM DetailStatusType dst WHERE dst.detailStatusTypeCode =:detailStatusTypeCode")
    DetailStatusType findByDetailStatusTypeCode(@Param("detailStatusTypeCode") String detailStatusTypeCode);
    @Query("SELECT s.detailStatusTypeCode FROM DetailStatusType s WHERE s.detailStatusTypeId =:detailStatusTypeId")
    String findDetailStatusTypeCodeByDetailStatusTypeId(int detailStatusTypeId);
}
