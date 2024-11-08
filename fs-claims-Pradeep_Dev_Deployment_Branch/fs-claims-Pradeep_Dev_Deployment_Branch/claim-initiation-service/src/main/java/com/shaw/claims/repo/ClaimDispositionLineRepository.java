package com.shaw.claims.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.ClaimDispositionLine;

@Repository
public interface ClaimDispositionLineRepository extends JpaRepository<ClaimDispositionLine, Integer> {

}
