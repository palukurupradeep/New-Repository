package com.shaw.claims.repo;

import com.shaw.claims.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<State, Integer> {
    @Query("SELECT s FROM State s WHERE s.isoCountryId =:countryId AND s.statusId = 1")
    List<State> getStatesByCountryId(Integer countryId);
    @Query("SELECT s.isoStateId FROM State s WHERE s.isoStateCode =:state")
    int findStateIdByIsoStateCode(@Param("state") String state);
    @Query("SELECT s.isoStateId FROM State s WHERE s.isoStateCode =:state AND s.isoCountryId =:countryId")
    int findStateIdByIsoStateCodeAndCountryId(@Param("state") String state, @Param("countryId") int countryId);
    @Query("SELECT s FROM State s WHERE s.isoStateId =:stateId AND s.statusId = 1")
    State findStatesByIsoStateId(Integer stateId);
    State findByIsoStateCode(String stateCode);
    @Query("SELECT s.isoStateName FROM State s WHERE s.isoStateId =:stateId AND s.isoCountryId =:countryId")
    String findIsoStateNameByIsoStateIdAndIsoCountryId(@Param("stateId") int stateId, @Param("countryId") int countryId);
    @Query("SELECT s.isoStateCode FROM State s WHERE s.isoStateCode =:stateCode AND s.isoCountryId =:countryId")
    String findStateCodeByIsoStateCodeAndCountryId(String stateCode, int countryId);
}
