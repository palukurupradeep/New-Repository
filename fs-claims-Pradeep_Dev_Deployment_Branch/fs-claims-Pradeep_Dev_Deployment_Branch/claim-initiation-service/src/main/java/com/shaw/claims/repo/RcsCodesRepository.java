package com.shaw.claims.repo;

import com.shaw.claims.model.RcsCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RcsCodesRepository extends JpaRepository<RcsCodes,Integer> {
    @Query("SELECT s.rcsCodeId FROM RcsCodes s WHERE s.rcsCode =:rcsCode")
    int findRcsCodeIdByRcsCode(String rcsCode);

    RcsCodes findByRcsCode(@Param("rcsCode") String rcsCode);
}
