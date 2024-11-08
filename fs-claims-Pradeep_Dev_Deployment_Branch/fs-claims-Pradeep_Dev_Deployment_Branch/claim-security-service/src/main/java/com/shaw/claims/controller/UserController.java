package com.shaw.claims.controller;

import java.util.List;

import com.shaw.claims.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import com.shaw.claims.services.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/users/v1")
public class UserController {
    Logger log = LogManager.getLogger(UserController.class);
    @Autowired
    UserService userService;
    @GetMapping("/findUser")
    public ResponseEntity<List<Users>> findUser(@RequestParam(name = "searchType") String searchType,
                                                @RequestParam(name = "value") String value){
        log.info("Inside UserController.findUser");
        return new ResponseEntity<>(userService.findUser(searchType, value), HttpStatus.OK);
    }
    @GetMapping("/applicationRoles")
    public ResponseEntity<List<ApplicationRoles>> fetchApplicationRoles(){
    	log.info("Inside UserController.fetchApplicationRoles");
    	return new ResponseEntity<>(userService.fetchApplicationRoles(),HttpStatus.OK);
    }
    @GetMapping("/applicationPermissions")
    public ResponseEntity<List<ApplicationPermissions>> fetchApplicationPermissions(){
    	log.info("Inside UserController.fetchApplicationPermissions");
    	return new ResponseEntity<>(userService.fetchApplicationPermissions(),HttpStatus.OK);
    }
    @GetMapping("/findUserById")
    public ResponseEntity<Users> findUser(@RequestParam(name = "userId") int userId){
        log.info("Inside UserController.findUserById");
        return new ResponseEntity<>(userService.findUserById(userId), HttpStatus.OK);
    }
    @GetMapping("/fetchRolesAndPermissionByUserId")
    public ResponseEntity<List<ApplicationRoles>> fetchRolesAndPermissionByUserId(@RequestParam(name = "userId") int userId){
        log.info("Inside UserController.fetchRolesAndPermissionByUserId");
        return new ResponseEntity<>(userService.getRolesAndPermissionByUserId(userId), HttpStatus.OK);
    }
    @GetMapping("/findRoleById")
    public ResponseEntity<ApplicationRoles> findRole(@RequestParam(name = "roleId") int roleId){
        log.info("Inside UserController.findRoleById");
        return new ResponseEntity<>(userService.findRoleById(roleId), HttpStatus.OK);
    }
    @GetMapping("/findUsersByRoleId")
    public ResponseEntity<List<Users>> findUsersByRoleId(@RequestParam(name = "roleId") int roleId){
        log.info("Inside UserController.findUsersByRoleId");
        return new ResponseEntity<>(userService.findUsersByRoleId(roleId), HttpStatus.OK);
    }
    @GetMapping("/findUserNames")
    public ResponseEntity<List<String>> findUserNames(@RequestParam(name = "userIds") String userIds){
        log.info("Inside UserController.findUserNames");
        return new ResponseEntity<>(userService.findUserNames(userIds), HttpStatus.OK);
    }
    @GetMapping("/applicationObjects")
    public ResponseEntity<List<ApplicationObjects>> fetchApplicationObjects(){
        log.info("Inside UserController.fetchApplicationObjects");
        return new ResponseEntity<>(userService.fetchApplicationObjects(), HttpStatus.OK);
    }
    @GetMapping("/applicationFunctions")
    public ResponseEntity<List<ApplicationFunctions>> fetchApplicationFunctions(){
        log.info("Inside UserController.findByObjectId");
        return new ResponseEntity<>(userService.fetchApplicationFunctions(), HttpStatus.OK);
    }
    @GetMapping("/applicationActions")
    public ResponseEntity<List<ApplicationActions>> fetchApplicationActions(){
        log.info("Inside UserController.fetchApplicationActions");
        return new ResponseEntity<>(userService.fetchApplicationActions(), HttpStatus.OK);
    }
    @GetMapping("/fetchActionsAndFunctionsByPermissionId")
    public ResponseEntity<ApplicationPermissions> fetchObjectActionsFunctionsByPermissionId(@RequestParam(name = "permissionId") int permissionId){
        log.info("Inside UserController.fetchObjectActionsFunctionsByPermissionId");
        return new ResponseEntity<>(userService.fetchObjectActionsFunctionsByPermissionId(permissionId), HttpStatus.OK);
    }
    @PostMapping("/addRole")
    public ResponseEntity<ApplicationRoles> addRoles(@RequestBody ApplicationRoles applicationRoles)
    {
        log.info("Inside UserController.addRoles");
        return new ResponseEntity<ApplicationRoles>(userService.addRoles(applicationRoles),HttpStatus.OK);
    }
    @PostMapping("/addPermission")
    public ResponseEntity<ApplicationPermissions> addPermission(@RequestBody ApplicationPermissions applicationPermissions)
    {
        log.info("Inside UserController.addPermission");
        return new ResponseEntity<ApplicationPermissions>(userService.addPermission(applicationPermissions),HttpStatus.OK);
    }
    @DeleteMapping("/deleteRoleById")
    public ResponseEntity<ApplicationRoles> deleteRoleById(@RequestParam(name = "roleId") int roleId){
        log.info("Inside UserController.deleteRoleById");
        return new ResponseEntity<>(userService.deleteRoleById(roleId), HttpStatus.OK);
    }
    @DeleteMapping("/deletePermissionById")
    public ResponseEntity<ApplicationPermissions> deletePermissionById(@RequestParam(name = "permissionId") int permissionId){
        log.info("Inside UserController.deletePermissionById");
        return new ResponseEntity<>(userService.deletePermissionById(permissionId), HttpStatus.OK);
    }
    @PostMapping("/addUsersToRoles")
    public ResponseEntity<Void> addUsersToRoles(@RequestBody UserRoleDTO addUserRoleDTO){
        log.info("Inside UserController.addUsersToRoles");
        userService.addUsersToRoles(addUserRoleDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/deleteUsersToRoles")
    public ResponseEntity<Void> deleteUsersToRoles(@RequestParam(name = "roleId") int roleId, @RequestParam(name = "userId") int userIds){
        log.info("Inside UserController.deleteUsersToRoles");
        userService.deleteUsersToRoles(roleId, userIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteUserToUserGroupMapping")
    public ResponseEntity<Void> deleteUserToUserGroupMapping(@RequestParam(name = "userId") int userId,
                                                             @RequestParam(name = "userGroupId") int userGroupId,
                                                             @RequestParam(name = "managerId") int managerId){
        userService.deleteUserToUserGroupMapping(userId, userGroupId, managerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/users")
    public ResponseEntity<Page<Users>> findAllUsers(Pageable pageable){
        log.info("Inside UserController.findAllUsers");
        return new ResponseEntity<>(userService.findAllUsers(pageable), HttpStatus.OK);
    }
    @PostMapping("/createPermissionSet")
    public ResponseEntity<Void> createPermissionSet(@RequestBody PermissionSetDTO permissionSetDTO){
        log.info("Inside UserController.createPermissionSet");
        userService.createPermissionSet(permissionSetDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/mapRolePermission")
    public ResponseEntity<Void> mapRolePermission(@RequestBody RolePermissionDTO mapRolePermissionDTO) {
        log.info("Inside UserController.mapRolePermission");
        userService.mapRolePermission(mapRolePermissionDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/validateUser")
    public ResponseEntity<Void> validateUser(@RequestParam(name = "objectId") String objectId) {
        log.info("Inside UserController.validateUser");
        userService.validateUser(objectId);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    @PostMapping("/addUser")
    public ResponseEntity<Users> addUser(@RequestBody Users users) {
        log.info("Inside UserController.addUser");
        return new ResponseEntity<Users>(userService.addUser(users),HttpStatus.OK);
    }

    @PostMapping("/updateUser")
    public ResponseEntity<Users> updateUser(@RequestBody UserDTO userDTO) {
        log.info("Inside UserController.updateUser");
        return new ResponseEntity<Users>(userService.updateUser(userDTO),HttpStatus.OK);
    }

    @DeleteMapping("/deleteUserById")
    public ResponseEntity<Users> deleteUserById(@RequestParam(name = "userId") int userId){
        log.info("Inside UserController.deleteUserById");
        return new ResponseEntity<>(userService.deleteUserById(userId), HttpStatus.OK);
    }
    @PutMapping("/userStatusToggle")
    public ResponseEntity<Users> userStatusToggle(@RequestParam(name = "userId") int userId, @RequestParam(name = "statusId") int statusId){
        log.info("Inside UserController.userStatusToggle");
        return new ResponseEntity<>(userService.userStatusToggle(userId, statusId), HttpStatus.OK);
    }
    @GetMapping("/getAllUserGroups")
    @Operation(description = "<h2><b>Possible actions are : assign, route.</h2></b>")
    public ResponseEntity<List<UserGroups>> getAllUserGroups(@RequestParam(name = "action", required = false) String  action){
    	log.info("Inside UserController.getAllUserGroups");
    	return new ResponseEntity<>(userService.getAllUserGroups(action),HttpStatus.OK);
    }
    @GetMapping("/getUserByGroupId")
    public ResponseEntity<List<Users>> getUserByGroupId(@RequestParam(name = "userGroupId") int userGroupId){
        log.info("Inside UserController.getAllUserGroups");
        return new ResponseEntity<>(userService.getUserByGroupId(userGroupId),HttpStatus.OK);
    }

    @GetMapping("/getUsersIds")
    public ResponseEntity<List<Integer>> getUsersIds(@RequestParam(name = "userId") int userId){
        log.info("Inside UserController.getUsersIds");
        return new ResponseEntity<>(userService.getUsersIds(userId),HttpStatus.OK);
    }

    @GetMapping("/isManager")
    public ResponseEntity<Boolean> isManager(@RequestParam(name = "userId") int userId){
        log.info("Inside UserController.isManager");
        return new ResponseEntity<>(userService.isManager(userId),HttpStatus.OK);
    }

    @GetMapping("/getUserIdByGroupCode")
    public ResponseEntity<Integer> getUserIdByGroupCode(@RequestParam(name = "userGroupCode") String  userGroupCode){
        log.info("Inside UserController.getUserIdByGroupCode");
        Integer userId = userService.getUserIdByGroupCode(userGroupCode);
        return new ResponseEntity<>(userId,HttpStatus.OK);
    }
    @GetMapping("/getUserGroupById")
    public ResponseEntity<List<UserGroupMapping>> getUserGroupById(@RequestParam(name = "userId") int  userId){
        log.info("Inside UserController.getUserGroupById");
        return new ResponseEntity<>(userService.getUserGroupById(userId),HttpStatus.OK);
    }
    @GetMapping("/getUserIdByActiveDirectoryId")
    public ResponseEntity<Integer> getUserIdByActiveDirectoryId(@RequestParam(name = "activeDirectoryId") String  activeDirectoryId){
        log.info("Inside UserController.getUserIdByActiveDirectoryId");
        Integer userId = userService.getUserIdByActiveDirectoryId(activeDirectoryId);
        return new ResponseEntity<>(userId,HttpStatus.OK);
    }
    @GetMapping("/getManager")
    public ResponseEntity<Users> getManager(@RequestParam(name = "userId") int userId, @RequestParam(name = "userGroupId") int userGroupId){
        log.info("Inside UserController.getManager");
        return new ResponseEntity<>(userService.getManager(userId, userGroupId),HttpStatus.OK);
    }

    @GetMapping("/getUserGroupAndManager")
    public ResponseEntity<UserGroupMappingDTO> getUserGroupAndManager(@RequestParam(name = "userId") int userId){
        log.info("Inside UserController.getManager");
        return new ResponseEntity<>(userService.getUserGroupAndManager(userId),HttpStatus.OK);
    }

    @GetMapping("/approvalLimitTypes")
    public ResponseEntity<List<ApprovalLimitType>> getApprovalLimitTypes(){
        log.info("Inside UserController.getApprovalLimitTypes");
        return new ResponseEntity<>(userService.getApprovalLimitTypes(),HttpStatus.OK);
    }
    
    @PostMapping("/addUserGroupMapping")
    public ResponseEntity<List<UserGroupMapping>> addUserGroupMapping(@RequestBody UserGroupMappingDTO userGroupMappingDTO)
    {
        log.info("Inside UserController.addUserGroupMapping");
        return new ResponseEntity<List<UserGroupMapping>>(userService.addUserGroupMapping(userGroupMappingDTO),HttpStatus.OK);
    }
    @DeleteMapping("/deletePermissionsToRoles")
    public ResponseEntity<Void> deletePermissionsToRoles(@RequestBody RolePermissionDTO rolePermissionDTO){
        log.info("Inside UserController.deletePermissionsToRoles");
        userService.deletePermissionsToRoles(rolePermissionDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/getUserGroupByUserGroupId")
    public ResponseEntity<UserGroups> getUserGroupByUserGroupId(@RequestParam(name = "userGroupId") int userGroupId){
        return new ResponseEntity<UserGroups>(userService.getUserGroupByUserGroupId(userGroupId),HttpStatus.OK);
    }
    @GetMapping("/getApprovalLimitByUserId")
    public ResponseEntity<UserApprovalLimitDTO> getApprovalLimitByUserId(@RequestParam(name = "userId") int userId, @RequestParam(name = "approvalLimitType") String approvalLimitType){
        log.info("Inside UserController.getApprovalLimitByUserId");
        return new ResponseEntity<>(userService.getApprovalLimitByUserId(userId, approvalLimitType),HttpStatus.OK);
    }
    @GetMapping("/findUserName")
    public ResponseEntity<String> findUserName(@RequestParam(name = "userId") int userId){
        log.info("Inside UserController.findUserName");
        return new ResponseEntity<>(userService.findUserName(userId), HttpStatus.OK);
    }
    @PutMapping("/rolesAndPermissionStatusToggle")
    public ResponseEntity<RolesAndPermissionStatusDTO> rolesAndPermissionStatusToggle(@RequestParam(name = "roleId", required = false) Integer roleId,@RequestParam(name = "permissionId", required = false) Integer permissionId, @RequestParam(name = "statusId") int statusId){
        log.info("Inside UserController.rolesAndPermissionStatusToggle");
        return new ResponseEntity<>(userService.rolesAndPermissionStatusToggle(roleId, permissionId, statusId), HttpStatus.OK);
    }

    @GetMapping("/userGroupByUserAndUserGroupID")
    public ResponseEntity<List<UserGroupMapping>> userGroupByUserAndUserGroupID(@RequestParam(name = "userId") int userId,
                                                                          @RequestParam(name = "userGroupId") int userGroupId){
        log.info("Inside UserController.userGroupByUserAndUserGroupID");
        return new ResponseEntity<>(userService.userGroupByUserAndUserGroupID(userId,userGroupId),HttpStatus.OK);
    }
 }
