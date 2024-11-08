package com.shaw.claims.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.DispositionType;

@Repository
public interface DispositionTypeRepository  extends JpaRepository<DispositionType,Integer> {
	DispositionType findByDispositionTypeDescription(String dispositionTypeDescription);
}
