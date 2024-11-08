package com.shaw.claims.repo;

import com.shaw.claims.model.ApplicationActions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationActionsRepository extends JpaRepository<ApplicationActions, Integer> {
    @Query("SELECT aa FROM ApplicationActions aa WHERE aa.actionId > 0 AND aa.statusId != 3")
    List<ApplicationActions> findAllActiveApplicationActions();
}
