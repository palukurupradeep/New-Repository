package com.shaw.claims.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.UserGroups;

@Repository
public interface UserGroupsRepository extends JpaRepository<UserGroups, Integer>{
    @Query("SELECT ug FROM UserGroups ug WHERE ug.statusId = 1")
    List<UserGroups> findAllActiveUserGroups();
    @Query("SELECT ug FROM UserGroups ug WHERE ug.claimAssignment = 1 AND ug.statusId = 1")
    List<UserGroups> findAllActiveClaimAssignmentUserGroups();
    @Query("SELECT ug FROM UserGroups ug WHERE ug.claimRouting = 1 AND ug.statusId = 1")
    List<UserGroups> findAllActiveClaimRoutingUserGroups();
    Optional<UserGroups> findByUserGroupCode(String userGroupCode);
}
