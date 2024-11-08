package com.shaw.claims.repo;

import com.shaw.claims.model.PermissionSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionSetRepository extends JpaRepository<PermissionSet, Integer> {
    @Query("SELECT ps FROM PermissionSet ps WHERE ps.permissions.permissionId =:permissionId AND ps.object.objectId =:objectId AND ps.actions.actionId =:actionId AND ps.functions.functionId =:functionId")
    List<PermissionSet> findByIds(int permissionId, int objectId, int actionId, int functionId);

}
