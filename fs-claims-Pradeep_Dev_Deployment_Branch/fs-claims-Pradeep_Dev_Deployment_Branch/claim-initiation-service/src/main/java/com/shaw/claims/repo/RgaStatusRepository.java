package com.shaw.claims.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.RgaStatus;

@Repository
public interface RgaStatusRepository  extends JpaRepository<RgaStatus, Integer>{

	RgaStatus findByRgaStatusCode(String rgaStatusCode);

	RgaStatus findByRgaStatusId(int rgaStatusId);
}
