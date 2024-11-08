package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.Locations;

@Repository
public interface LocationRepository extends JpaRepository<Locations,Integer> {

    @Query("SELECT l FROM Locations l WHERE l.statusId = 1")
    List<Locations> findAllByStatus();

    @Query("SELECT l.locationId FROM Locations l WHERE l.locationCode =:locationCode")
    int findLocationIdByLocationCode(@Param("locationCode") String locationCode);

    Locations findByLocationCode(@Param("locationCode") String locationCode);

    List<Locations> findByCityAndStateCode(@Param("city") String city, @Param("stateCode") String stateCode);
//    Locations findByCityAndStateCodeAndLocationType(@Param("city") String city,
//                                                          @Param("stateCode") String stateCode,
//                                                          @Param("locationType") String locationType);
    
    List<Locations> findByCityAndStateCodeAndLocationType(@Param("city") String city,
            @Param("stateCode") String stateCode,
            @Param("locationType") String locationType);
}
