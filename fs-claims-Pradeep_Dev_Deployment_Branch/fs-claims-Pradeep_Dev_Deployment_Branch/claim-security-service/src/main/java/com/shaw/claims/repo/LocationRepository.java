package com.shaw.claims.repo;

import java.util.List;
import java.util.Optional;

import com.shaw.claims.dto.GeoLocationDTO;
import com.shaw.claims.model.ApprovalLimitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.Locations;

@Repository
public interface LocationRepository extends JpaRepository<Locations, Integer> {

	Optional<Locations> findByLocationCode(String locationCode);

	List<Locations> findByLocationType(String locationType);

	@Query("SELECT new com.shaw.claims.dto.GeoLocationDTO(loc.latitude, loc.longitude) FROM Locations loc WHERE loc.locationCode NOT IN ('DEF','QC','QT','QV','R3')")
	List<GeoLocationDTO> getAllLocations();

}
