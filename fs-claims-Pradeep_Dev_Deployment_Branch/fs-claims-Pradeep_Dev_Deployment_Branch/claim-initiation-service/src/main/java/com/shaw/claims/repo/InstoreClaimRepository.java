package com.shaw.claims.repo;

import com.shaw.claims.model.Claim;
import com.shaw.claims.model.InstoreClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstoreClaimRepository extends JpaRepository<InstoreClaim,Integer> {

}
