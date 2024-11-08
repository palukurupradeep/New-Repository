package com.shaw.claims.repo;

import com.shaw.claims.model.ClaimRoute;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimRouteRepository extends JpaRepository<ClaimRoute,Integer> {
    @Query("SELECT cr FROM ClaimRoute cr WHERE cr.claim.claimId = :claimId ")
    ClaimRoute findByClaimId(@Param("claimId") int claimId);
    
    @Query("SELECT cr FROM ClaimRoute cr WHERE cr.routedToUserId=:userId ")
   List<ClaimRoute> findByRoutedToUserId( int userId);
    
    @Query("SELECT cr FROM ClaimRoute cr WHERE cr.routedToUserId=:userId and cr.claim.claimId = :claimId and  cr.routedToUserGroupId=:routedToUserGroupId")
    List<ClaimRoute> findRoutedData( int userId,int claimId,int routedToUserGroupId);
}
