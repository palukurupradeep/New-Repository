package com.shaw.claims.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.DispositionStatus;

@Repository
public interface DispositionStatusRepository extends JpaRepository<DispositionStatus, Integer> {
	DispositionStatus findByDispositionStatusCode(String dispositionStatusCode);
}
