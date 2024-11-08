package com.shaw.claims.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.UserGroupMapping;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGroupMappingRepository extends JpaRepository<UserGroupMapping, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM UserGroupMapping ugm WHERE ugm.userId =:userId AND ugm.userGroupId =:userGroupId AND ugm.managerId =:managerId")
    void deleteUserToUserGroupMapping(int userId, int userGroupId, int managerId);
    @Query("SELECT managerId FROM UserGroupMapping ugm WHERE ugm.userId =:userId AND ugm.userGroupId =:userGroupId")
    Integer getManager(int userId, int userGroupId);
    List<UserGroupMapping> findByUserId(int userId);
    List<UserGroupMapping> findByManagerId(int managerId);
    @Query("SELECT ugm FROM UserGroupMapping ugm WHERE ugm.userId =:userId AND ugm.userGroupId =:userGroupId AND ugm.managerId =:managerId")
    UserGroupMapping findByUserIdAndUserGroupIdAndManagerId(int userId, int userGroupId, int managerId);
    @Query("SELECT ugm FROM UserGroupMapping ugm WHERE ugm.userId =:userId AND ugm.userGroupId =:userGroupId")
    List<UserGroupMapping> findByUserIdAndUserGroupId(@Param("userId") int userId, @Param("userGroupId") int userGroupId);
}
