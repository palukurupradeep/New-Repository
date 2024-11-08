package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shaw.claims.model.ApplicationActions;
import com.shaw.claims.model.ApplicationFunctions;
import com.shaw.claims.model.ApplicationObjects;
import com.shaw.claims.model.ApplicationPermissions;
import com.shaw.claims.model.ApplicationRoles;
import com.shaw.claims.model.ApprovalLimitType;
import com.shaw.claims.model.UserApprovalLimit;
import com.shaw.claims.model.UserGroupMapping;
import com.shaw.claims.model.UserGroups;
import com.shaw.claims.model.Users;

public interface UserService {
    List<Users> findUser(String searchType, String value);
    List<ApplicationRoles> fetchApplicationRoles();
    List<ApplicationPermissions> fetchApplicationPermissions();
    Users findUserById(int userId);
    List<ApplicationRoles> getRolesAndPermissionByUserId(int userId);
    ApplicationRoles findRoleById(int roleId);
    List<Users> findUsersByRoleId(int roleId);
    List<String> findUserNames(String userIds);
    ApplicationPermissions findPermissionsById(int permissionId);
    List<ApplicationObjects> fetchApplicationObjects();
    ApplicationObjects findApplicationObjectsById(int objectId);
    List<ApplicationFunctions> fetchApplicationFunctions();
    ApplicationFunctions findApplicationFunctionsById(int functionId);
    List<ApplicationActions> fetchApplicationActions();
    ApplicationActions findApplicationActionsById(int actionId);
    ApplicationPermissions fetchObjectActionsFunctionsByPermissionId(int permissionId);
    ApplicationRoles addRoles(ApplicationRoles applicationRoles);
    ApplicationPermissions addPermission(ApplicationPermissions applicationPermissions);
    ApplicationRoles deleteRoleById(int roleId);
    ApplicationPermissions deletePermissionById(int permissionId);
    void addUsersToRoles(UserRoleDTO addUserRoleDTO);
    void deleteUsersToRoles(int roleId, int userIds);
    void deleteUserToUserGroupMapping(int userId, int userGroupId, int managerId);
    void validateRoleAssociatedWithUserAndPermission(ApplicationRoles roles);
    void validatePermissionIdAssociatedWithRole(ApplicationPermissions permissions);
    Page<Users> findAllUsers(Pageable pageable);
    List<Users> findAllUsers();
    void createPermissionSet(PermissionSetDTO permissionSetDTO);
    void mapRolePermission(RolePermissionDTO mapRolePermissionDTO);
    void validateUser(String objectId);
    Users addUser(Users user);
    Users updateUser(UserDTO userDTO);
    Users deleteUserById(int userId);
    Users userStatusToggle(int userId, int statusId);
    List<UserGroups> getAllUserGroups(String action);
    List<Users> getUserByGroupId(int userGroupId);
    List<Integer> getUsersIds(int userId);
    Boolean isManager(int userId);
    List<ApprovalLimitType> getApprovalLimitTypes();
    List<UserGroupMapping> addUserGroupMapping(UserGroupMappingDTO userGroupMappingDTO);
    Users getManager(int userId, int userGroupId);
    UserGroupMappingDTO getUserGroupAndManager(int userId);
    Integer getUserIdByGroupCode(String userGroupCode);
    List<UserGroupMapping> getUserGroupById(int userId);
    Integer getUserIdByActiveDirectoryId(String activeDirectoryId);
	void deletePermissionsToRoles(RolePermissionDTO rolePermissionDTO);
    UserGroups getUserGroupByUserGroupId(int userGroupId);
	UserApprovalLimitDTO getApprovalLimitByUserId(int userId, String approvalLimitType);

    String findUserName(int userId);
    RolesAndPermissionStatusDTO rolesAndPermissionStatusToggle(Integer roleId, Integer permissionId, int statusId);

    List<UserGroupMapping> userGroupByUserAndUserGroupID(int userId, int userGroupId);
}
