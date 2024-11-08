package com.shaw.claims.repo;

import com.shaw.claims.model.DetailType;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailTypeRepository extends JpaRepository<DetailType,Integer> {
    @Query("SELECT dt FROM DetailType dt WHERE dt.detailTypeCode =:detailTypeCode")
    DetailType findByDetailTypeCode(@Param("detailTypeCode") String detailTypeCode);
    @Query("SELECT s.detailTypeCode FROM DetailType s WHERE s.detailTypeId =:detailTypeId")
    String findDetailTypeCodeByDetailTypeId(String detailTypeId);
	List<DetailType> findByStatusId(int active);
}
