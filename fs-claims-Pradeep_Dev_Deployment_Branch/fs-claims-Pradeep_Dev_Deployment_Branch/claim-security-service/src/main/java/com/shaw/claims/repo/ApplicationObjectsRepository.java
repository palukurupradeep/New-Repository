package com.shaw.claims.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.ApplicationObjects;

import java.util.List;

@Repository
public interface ApplicationObjectsRepository extends JpaRepository<ApplicationObjects, Integer> {
    @Query("SELECT ao FROM ApplicationObjects ao WHERE ao.objectId > 0 AND ao.statusId != 3")
    List<ApplicationObjects> findAllActiveApplicationObjects();

}
