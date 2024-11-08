package com.shaw.claims.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.ApplicationPermissions;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationPermissionsRepository extends JpaRepository<ApplicationPermissions, Integer> {
    @Query("SELECT ap FROM ApplicationPermissions ap WHERE ap.permissionId > 0 AND ap.statusId != 3 ORDER BY ap.createdDateTime DESC")
    List<ApplicationPermissions> findAllActiveApplicationPermissions();
    Optional<ApplicationPermissions> findByPermissionCode(String permissionCode);

}
