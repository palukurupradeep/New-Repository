package com.shaw.claims.repo;

import com.shaw.claims.model.ClaimCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimCategoryRepository extends JpaRepository<ClaimCategory, Integer> {
    @Query("SELECT c FROM ClaimCategory c JOIN c.status s WHERE s.statusId = 1 ORDER BY c.displaySequence")
    List<ClaimCategory> getClaimCategories();

    ClaimCategory findByClaimCategoryCode(@Param("categoryCode") String categoryCode);
@Query("SELECT c.claimCategoryId FROM ClaimCategory c WHERE c.claimCategoryCode =:claimCategoryCode")
    int findByClaimCategoryIdUsingClaimCategoryCode(String claimCategoryCode);
}
