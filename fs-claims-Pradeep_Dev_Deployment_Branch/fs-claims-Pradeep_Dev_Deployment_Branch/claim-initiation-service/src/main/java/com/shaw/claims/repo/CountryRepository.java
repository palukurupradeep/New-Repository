package com.shaw.claims.repo;

import com.shaw.claims.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    @Query("SELECT c FROM Country c WHERE c.statusId = 1")
    List<Country> getCountries();
    @Query("SELECT s.isoCountryId FROM Country s WHERE s.isoCountryCode =:countryCode")
    int findCountryIdByIsoCountryCode(@Param("countryCode") String countryCode);
    @Query("SELECT s.isoCountryName FROM Country s WHERE s.isoCountryId =:countryId")
    String findIsoCountryNameByIsoCountryId(@Param("countryId") int countryId);
    @Query("SELECT s.isoCountryCode FROM Country s WHERE s.isoCountryCode =:countryCode")
    String findCountryCodeByIsoCountryCode(String countryCode);
}
