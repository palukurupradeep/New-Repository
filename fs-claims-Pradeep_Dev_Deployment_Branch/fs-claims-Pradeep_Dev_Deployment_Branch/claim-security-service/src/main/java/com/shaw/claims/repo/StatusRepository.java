package com.shaw.claims.repo;

import com.shaw.claims.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status,Integer> {
    @Query("SELECT s.statusId FROM Status s WHERE s.statusCode =:statusCode")
    int findStatusIdByStatusCode(String statusCode);
}
