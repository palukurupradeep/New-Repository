package com.shaw.claims.repo;

import com.shaw.claims.model.TraceType;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TraceTypeRepository extends JpaRepository<TraceType,Integer> {
	TraceType findByTraceTypeCode(String traceTypeCode);

	@Query("select t from TraceType t where t.statusId=1")
	List<TraceType> findAllTraceTypes();
	@Query("SELECT tt.traceTypeId FROM TraceType tt WHERE tt.traceTypeCode = :traceTypeCode")
    int findTraceTypeIdByTraceTypeCode(String traceTypeCode);
}
