package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.LaborRate;

@Repository
public interface LaborRateRepository extends JpaRepository<LaborRate,Integer> {

	List<LaborRate> findByStatusId(int i);

}
