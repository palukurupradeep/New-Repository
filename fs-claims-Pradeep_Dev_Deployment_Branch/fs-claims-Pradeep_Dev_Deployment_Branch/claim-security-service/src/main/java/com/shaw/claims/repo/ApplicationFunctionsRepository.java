package com.shaw.claims.repo;

import com.shaw.claims.model.ApplicationObjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.ApplicationFunctions;

import java.util.List;

@Repository
public interface ApplicationFunctionsRepository extends JpaRepository<ApplicationFunctions, Integer> {
    @Query("SELECT af FROM ApplicationFunctions af WHERE af.functionId > 0 AND af.statusId != 3")
    List<ApplicationFunctions> findAllActiveApplicationFunctions();
}
