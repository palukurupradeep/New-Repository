package com.shaw.claims.repo;

import com.shaw.claims.model.SalesReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesReviewStatusRepository extends JpaRepository<SalesReviewStatus,Integer> {
    SalesReviewStatus findBySalesReviewStatusCode(@Param("salesReviewStatusCode") String salesReviewStatusCode);
}
