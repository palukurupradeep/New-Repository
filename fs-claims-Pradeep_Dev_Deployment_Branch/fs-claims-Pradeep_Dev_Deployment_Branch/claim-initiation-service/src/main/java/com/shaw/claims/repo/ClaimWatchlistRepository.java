package com.shaw.claims.repo;

import com.shaw.claims.model.ClaimWatchlist;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClaimWatchlistRepository extends JpaRepository<ClaimWatchlist, Integer> {
	@Query("SELECT cw FROM ClaimWatchlist cw WHERE cw.claim.claimId=:claimId AND cw.createdByUserId=:userId")
    Optional<ClaimWatchlist> findClaimWatchlist(int claimId, int userId);
	@Query("SELECT cw FROM ClaimWatchlist cw WHERE cw.statusId=1 AND cw.createdByUserId=:userId")
	List<ClaimWatchlist> findByCreatedByUserId(int userId);
	
	@Modifying
	@Transactional
	@Query("update ClaimWatchlist c set c.statusId=3 where c.createdByUserId=:userId and c.claim.claimId=:claimId")
	 void deleteClaimWatchList(@Param("userId") Integer userId,@Param("claimId") Integer claimId);
}
