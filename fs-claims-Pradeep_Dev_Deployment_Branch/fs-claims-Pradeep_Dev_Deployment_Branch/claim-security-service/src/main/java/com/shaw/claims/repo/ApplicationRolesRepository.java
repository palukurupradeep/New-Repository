package com.shaw.claims.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.ApplicationRoles;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRolesRepository extends JpaRepository<ApplicationRoles, Integer> {
    @Query("SELECT ar FROM ApplicationRoles ar WHERE ar.roleId > 0 AND ar.statusId != 3 ORDER BY ar.createdDateTime DESC")
    List<ApplicationRoles> findAllActiveApplicationRoles();
    Optional<ApplicationRoles> findByRoleCode(String roleCode);
}
