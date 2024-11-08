package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.UnitOfMeasure;

@Repository
public interface UnitOfMeasureRepository extends JpaRepository<UnitOfMeasure,Integer> {
    UnitOfMeasure findByUnitOfMeasureCode(@Param("unitOfMeasureCode") String unitOfMeasureCode);

	List<UnitOfMeasure> findByStatusId(int active);
	UnitOfMeasure findByUnitOfMeasureId(Integer unitOfMeasureId);
}
