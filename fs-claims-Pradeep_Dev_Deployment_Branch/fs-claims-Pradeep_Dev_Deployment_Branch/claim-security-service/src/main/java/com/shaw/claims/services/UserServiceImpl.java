package com.shaw.claims.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.shaw.claims.dto.ObjectDTO;
import com.shaw.claims.dto.PermissionSetDTO;
import com.shaw.claims.dto.RolePermissionDTO;
import com.shaw.claims.dto.RolesAndPermissionStatusDTO;
import com.shaw.claims.dto.UserApprovalLimitDTO;
import com.shaw.claims.dto.UserDTO;
import com.shaw.claims.dto.UserGroupDTO;
import com.shaw.claims.dto.UserGroupMappingDTO;
import com.shaw.claims.dto.UserRoleDTO;
import com.shaw.claims.enums.Action;
import com.shaw.claims.enums.StatusTypes;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.ApplicationActions;
import com.shaw.claims.model.ApplicationFunctions;
import com.shaw.claims.model.ApplicationObjects;
import com.shaw.claims.model.ApplicationPermissions;
import com.shaw.claims.model.ApplicationRoles;
import com.shaw.claims.model.ApprovalLimitType;
import com.shaw.claims.model.PermissionSet;
import com.shaw.claims.model.UserApprovalLimit;
import com.shaw.claims.model.UserGroupMapping;
import com.shaw.claims.model.UserGroups;
import com.shaw.claims.model.Users;
import com.shaw.claims.repo.ApplicationActionsRepository;
import com.shaw.claims.repo.ApplicationFunctionsRepository;
import com.shaw.claims.repo.ApplicationObjectsRepository;
import com.shaw.claims.repo.ApplicationPermissionsRepository;
import com.shaw.claims.repo.ApplicationRolesRepository;
import com.shaw.claims.repo.ApprovalLimitTypeRepository;
import com.shaw.claims.repo.PermissionSetRepository;
import com.shaw.claims.repo.UserGroupMappingRepository;
import com.shaw.claims.repo.UserGroupsRepository;
import com.shaw.claims.repo.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class UserServiceImpl implements UserService {
    Logger log = LogManager.getLogger(UserServiceImpl.class);
    @Autowired
    EntityManager entityManager;
    @Autowired
    ApplicationRolesRepository applicationRolesRepository;
    @Autowired
    ApplicationPermissionsRepository applicationPermissionsRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ApplicationObjectsRepository applicationObjectsRepository;
    @Autowired
    ApplicationFunctionsRepository applicationFunctionsRepository;
    @Autowired
    ApplicationActionsRepository applicationActionsRepository;
    @Autowired
    PermissionSetRepository permissionSetRepository;
    @Autowired
    private UserGroupsRepository userGroupsRepository;
    @Autowired
    private ApprovalLimitTypeRepository approvalLimitTypeRepository;
    @Autowired
    private UserGroupMappingRepository userGroupMappingRepository;

    @Override
    public List<Users> findUser(String searchType, String value) {
        log.info("Inside UserServiceImpl.findUser");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Users> query = cb.createQuery(Users.class);
        Root<Users> userRoot = query.from(Users.class);
        List<Predicate> predicates = new ArrayList<>();
        if (searchType.equalsIgnoreCase("ActiveDirectoryId") && value != null && !value.isEmpty()) {
            predicates.add(cb.like(userRoot.get("activeDirectoryId"), value+"%"));
        }
        if (searchType.equalsIgnoreCase("FullName") && value != null && !value.isEmpty()) {
            String[] fName = value.split(" ");
            predicates.add(cb.equal(userRoot.get("firstName"), fName[0]));
            predicates.add(cb.equal(userRoot.get("middleInitial"), fName[1]));
            predicates.add(cb.equal(userRoot.get("lastName"), fName[2]));
        }
        if (searchType.equalsIgnoreCase("FirstName") && value != null && !value.isEmpty()) {
            predicates.add(cb.like(userRoot.get("firstName"), value+"%"));
        }
        if (searchType.equalsIgnoreCase("LastName") && value != null && !value.isEmpty()) {
            predicates.add(cb.like(userRoot.get("lastName"), value+"%"));
        }
        if (searchType.equalsIgnoreCase("EmailAddress") && value != null && !value.isEmpty()) {
            predicates.add(cb.like(userRoot.get("emailAddress"), value+"%"));
        }
        if (searchType.equalsIgnoreCase("ObjectId") && value != null && !value.isEmpty()) {
            predicates.add(cb.equal(userRoot.get("objectId"), value));
        }
        if(predicates.size()<1) {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Please enter correct search criteria !!");
        }
        query.where(predicates.toArray(new Predicate[0]));
        List<Users>  usersList = entityManager.createQuery(query).getResultList();
        if(usersList==null || usersList.isEmpty())
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Record Not Found with " + searchType + " :: " + value);
        return usersList;
    }
	@Override
	public List<ApplicationRoles> fetchApplicationRoles() {
		log.info("Inside UserServiceImpl.fetchApplicationRoles");
        return applicationRolesRepository.findAllActiveApplicationRoles();
	}
	@Override
	public List<ApplicationPermissions> fetchApplicationPermissions() {
		log.info("Inside UserServiceImpl.fetchApplicationPermissions");
        return applicationPermissionsRepository.findAllActiveApplicationPermissions();
	}

	@Override
	public Users findUserById(int userId) {
        log.info("Inside UserServiceImpl.findUserById");
		Optional<Users> userInfo = userRepository.findById(userId);
		if (userInfo.isPresent()) {
            return userInfo.get();
		} else {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found with userId :: " + userId);
		}
	}

    @Override
    public List<ApplicationRoles> getRolesAndPermissionByUserId(int userId) {
        log.info("Inside UserServiceImpl.getRolesAndPermissionByUserId");
        Users user = findUserById(userId);
        List<ApplicationRoles> roles = user.getRoles();
        for (ApplicationRoles r:roles) {
            r.setUsers(null);
        }
        return roles;
    }
    @Override
    public ApplicationRoles findRoleById(int roleId) {
        log.info("Inside UserServiceImpl.findRoleById");
        Optional<ApplicationRoles> roleOptional = applicationRolesRepository.findById(roleId);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        } else {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Record Not Found with roleId :: " + roleId);
        }
    }
    @Override
    public List<Users> findUsersByRoleId(int roleId) {
        log.info("Inside UserServiceImpl.findUsersByRoleId");
        ApplicationRoles roles = findRoleById(roleId);
        return roles.getUsers();
    }
    @Override
    public List<String> findUserNames(String userIds) {
        log.info("Inside UserServiceImpl.findUserNames");
        List<Integer> userIdList = Arrays.stream(userIds.split(",")).map(Integer::parseInt).toList();
        List<String> usersList = userRepository.findUserNames(userIdList);
        return usersList;
    }

    @Override
    public ApplicationPermissions findPermissionsById(int permissionId) {
        log.info("Inside UserServiceImpl.findPermissionsById");
        Optional<ApplicationPermissions> permissionsOptional =applicationPermissionsRepository.findById(permissionId);
        ApplicationPermissions perm = null;
        if(permissionsOptional.isPresent()) {
            perm = permissionsOptional.get();
            return perm;
        } else {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Record Not Found with permissionId :: " + permissionId);
        }
    }
    @Override
    public List<ApplicationObjects> fetchApplicationObjects() {
        log.info("Inside UserServiceImpl.fetchApplicationObjects");
        List<ApplicationObjects> applicationObjects = applicationObjectsRepository.findAllActiveApplicationObjects();
        applicationObjects.stream().filter(obj -> !obj.getViewActions().isEmpty() && !obj.getViewFunctions().isEmpty())
                .forEach(obj -> {
            obj.setViewActions(null);
            obj.setViewFunctions(null);
        });
        return applicationObjects;
    }
    @Override
    public ApplicationObjects findApplicationObjectsById(int objectId) {
        log.info("Inside UserServiceImpl.findApplicationObjectsById");
        Optional<ApplicationObjects> objectOptional = applicationObjectsRepository.findById(objectId);
        if (objectOptional.isPresent()) {
            ApplicationObjects applicationObjects = objectOptional.get();
            applicationObjects.setViewFunctions(null);
            applicationObjects.setViewActions(null);
            return applicationObjects;
        } else {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Record Not Found with objectId :: " + objectId);
        }
    }
    @Override
    public List<ApplicationFunctions> fetchApplicationFunctions() {
        log.info("Inside UserServiceImpl.fetchApplicationFunctionsByObjectId");
        return applicationFunctionsRepository.findAllActiveApplicationFunctions();
    }
    @Override
    public ApplicationFunctions findApplicationFunctionsById(int functionId) {
        log.info("Inside UserServiceImpl.findApplicationFunctionsById");
        Optional<ApplicationFunctions> functionOptional = applicationFunctionsRepository.findById(functionId);
        if (functionOptional.isPresent()) {
            return functionOptional.get();
        } else {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Record Not Found with functionId :: " + functionId);
        }
    }
    @Override
	public List<ApplicationActions> fetchApplicationActions() {
		log.info("Inside UserServiceImpl.fetchApplicationActionsByObjectId");
        return applicationActionsRepository.findAllActiveApplicationActions();
	}
    @Override
    public ApplicationActions findApplicationActionsById(int actionId) {
        log.info("Inside UserServiceImpl.findApplicationActionsById");
        Optional<ApplicationActions> actionOptional = applicationActionsRepository.findById(actionId);
        if (actionOptional.isPresent()) {
            return actionOptional.get();
        } else {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Record Not Found with actionId :: " + actionId);
        }
    }
    @Override
	public ApplicationPermissions fetchObjectActionsFunctionsByPermissionId(int permissionId) {
		log.info("Inside UserServiceImpl.fetchObjectActionsFunctionsByPermissionId");
        ApplicationPermissions applicationPermissions = findPermissionsById(permissionId);
        applicationPermissions.getApplicationObjects().stream().filter(obj -> !obj.getApplicationActions().isEmpty() && !obj.getApplicationFunctions().isEmpty())
                .forEach(obj -> {
                    obj.setApplicationActions(null);
                    obj.setApplicationFunctions(null);
                });
		return applicationPermissions;
	}
    @Override
    public ApplicationRoles addRoles(ApplicationRoles applicationRoles) {
        log.info("Inside UserServiceImpl.addRoles");
        ApplicationRoles applicationRolesData=null;
        ApplicationRoles appRoles = findRoleByCode(applicationRoles.getRoleCode());
        if(appRoles!=null){
            if(appRoles.getStatusId()==2 || appRoles.getStatusId()==3) {
                appRoles.setStatusId(1);
                appRoles.setRoleDescription(applicationRoles.getRoleDescription());
                applicationRolesData= appRoles;
            }else
                throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Roles already exists");
        }else
            applicationRolesData= applicationRoles;
        applicationRolesData.setModifiedDateTime(LocalDateTime.now());
        return applicationRolesRepository.save(applicationRolesData);
    }
    @Override
    public ApplicationPermissions addPermission(ApplicationPermissions applicationPermissions) {
        log.info("Inside UserServiceImpl.addPermission");
        ApplicationPermissions applicationPermissionsData=null;
        ApplicationPermissions appPermissions = findPermissionsByCode(applicationPermissions.getPermissionCode());
        if(appPermissions!=null && (appPermissions.getStatusId()==2 || appPermissions.getStatusId()==3)){
            appPermissions.setStatusId(1);
            appPermissions.setPermissionDescription(applicationPermissions.getPermissionDescription());
            applicationPermissionsData= appPermissions;
        }else
            applicationPermissionsData= applicationPermissions;
        applicationPermissionsData.setModifiedDateTime(LocalDateTime.now());
        return applicationPermissionsRepository.save(applicationPermissionsData);
    }
    @Override
    public ApplicationRoles deleteRoleById(int roleId) {
        log.info("Inside UserServiceImpl.deleteRoleById");
        ApplicationRoles applicationRoles = findRoleById(roleId);
        validateRoleAssociatedWithUserAndPermission(applicationRoles);
        applicationRoles.setStatusId(3);
        return applicationRolesRepository.save(applicationRoles);
    }
    @Override
    public ApplicationPermissions deletePermissionById(int permissionId) {
        log.info("Inside UserServiceImpl.deletePermissionById");
        ApplicationPermissions applicationPermissions = findPermissionsById(permissionId);
        validatePermissionIdAssociatedWithRole(applicationPermissions);
        applicationPermissions.setStatusId(3);
        return applicationPermissionsRepository.save(applicationPermissions);
    }
    @Override
    public void addUsersToRoles(UserRoleDTO addUserRoleDTOs) {
        log.info("Inside UserServiceImpl.addUsersToRoles");
        ApplicationRoles role = findRoleById(addUserRoleDTOs.getRoleId());
        for (int userId : addUserRoleDTOs.getUserIds()) {
        	Users user = findUserById(userId);
        	  boolean userExists = role.getUsers().stream().anyMatch(usr -> usr.getUserId() == userId);
        	  if(!userExists)
                  role.getUsers().add(user);
              else{
                  throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Mapping already exists for RoleId - " + addUserRoleDTOs.getRoleId() + " and UserId - "+addUserRoleDTOs.getUserIds());
              }
		}
        applicationRolesRepository.save(role);
    }
    @Override
    public void deleteUsersToRoles(int roleId, int userIds) {
        log.info("Inside UserServiceImpl.deleteUsersToRoles");
        ApplicationRoles role = findRoleById(roleId);
        Users user = findUserById(userIds);
        role.getUsers().remove(user);
        applicationRolesRepository.save(role);
    }

    @Override
    public void deleteUserToUserGroupMapping(int userId, int userGroupId, int managerId) {
        log.info("Inside UserServiceImpl.deleteUserToUserGroupMapping");
        Users user = findUserById(userId);
        UserGroups userGroups = getUserGroupByUserGroupId(userGroupId);
        Users manager = findUserById(managerId);
        //added for non-existing mapping check
        fetchUserGroupMappingByIds(user.getUserId(), userGroups.getUserGroupId(), manager.getUserId(), false);
        userGroupMappingRepository.deleteUserToUserGroupMapping(user.getUserId(), userGroups.getUserGroupId(), manager.getUserId());
    }

    @Override
    public Page<Users> findAllUsers(Pageable pageable) {
        log.info("Inside UserServiceImpl.findAllUsers");
        Page<Users> users =  userRepository.findAllActiveUsers(pageable);
        users.forEach(user -> {
            if (user.getFaxNumber() != null) {
                user.setFaxNumber(user.getFaxNumber().trim());
            }
        });
        return users;
    }

    @Override
    public List<Users> findAllUsers() {
        return userRepository.findAllActiveUsers();
    }

    @Override
	public void validateRoleAssociatedWithUserAndPermission(ApplicationRoles roles) {
		log.info("Inside UserServiceImpl.validateRoleAssociatedWithUserAndPermission");
		if(roles.getUsers().size() > 0 || roles.getPermissions().size() > 0) {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "This role is associated with user / permission. Remove the role-association from users first");
        }
	}
	@Override
	public void validatePermissionIdAssociatedWithRole(ApplicationPermissions permissions) {
		log.info("Inside UserServiceImpl.validatePermissionIdAssociatedWithRole");
		if (permissions.getRoles().size() > 0) {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "This permission is associated with role. Remove the permission-association from roles first");
        }
	}
    @Override
    public void createPermissionSet(PermissionSetDTO permissionSetDTO) {
        log.info("Inside UserServiceImpl.createPermissionSet");
        List<ObjectDTO> objectDTOList = permissionSetDTO.getObjects();
        for (ObjectDTO object : objectDTOList) {
            if(object.getActionIds().size() > object.getFunctionIds().size()){
                for (Integer actionId : object.getActionIds()) {
                    for (Integer functionId : object.getFunctionIds()) {
                        permissionSet(permissionSetDTO.getPermissionId(), object.getObjectId(), actionId, functionId);
                    }
                }
            } else{
                for (Integer functionId : object.getFunctionIds()) {
                    for (Integer actionId : object.getActionIds()) {
                        permissionSet(permissionSetDTO.getPermissionId(), object.getObjectId(), actionId, functionId);
                    }
                }
            }
        }
    }
    private void permissionSet(int permissionId, int objectId, int actionId, int functionId){
        PermissionSet permissionSet = new PermissionSet();
        List<PermissionSet> psList = permissionSetRepository.findByIds(permissionId, objectId, actionId, functionId);
        if(psList.size()==0) {
            ApplicationPermissions applicationPermissions = new ApplicationPermissions();
            applicationPermissions.setPermissionId(permissionId);
            permissionSet.setPermissions(applicationPermissions);
            ApplicationObjects applicationObjects = new ApplicationObjects();
            applicationObjects.setObjectId(objectId);
            permissionSet.setObject(applicationObjects);
            ApplicationFunctions applicationFunctions = new ApplicationFunctions();
            applicationFunctions.setFunctionId(functionId);
            permissionSet.setFunctions(applicationFunctions);
            ApplicationActions applicationActions = new ApplicationActions();
            applicationActions.setActionId(actionId);
            permissionSet.setActions(applicationActions);
            permissionSetRepository.save(permissionSet);
        }else{
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Mapping already exists");
        }
    }
    @Override
	public void mapRolePermission(RolePermissionDTO mapRolePermissionDTO) {
		log.info("Inside UserServiceImpl.mapRolePermission");
		ApplicationRoles role = findRoleById(mapRolePermissionDTO.getRoleId());
		for (int permissionId : mapRolePermissionDTO.getPermissionIdData()) {
			ApplicationPermissions permissions = findPermissionsById(permissionId);
			boolean permissionExists = role.getPermissions().stream()
					.anyMatch(permission -> permission.getPermissionId() == permissionId);
			if (!permissionExists) {
				if (mapRolePermissionDTO.isActive()) {
					role.getPermissions().add(permissions);
				} else {
					role.getPermissions().remove(permissions);
				}
			} else {
				throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
						"Mapping already exists for RoleId - " + mapRolePermissionDTO.getRoleId()
								+ " and PermissionId - " + permissionId);
			}

		}
		applicationRolesRepository.save(role);
	}
    private ApplicationRoles findRoleByCode(String roleCode) {
        ApplicationRoles appRole = null;
        Optional<ApplicationRoles> applicationRoles = applicationRolesRepository.findByRoleCode(roleCode);
        if(applicationRoles.isPresent())
            appRole = applicationRoles.get();
        return appRole;
    }
    private ApplicationPermissions findPermissionsByCode(String permissionCode) {
        ApplicationPermissions appPermission = null;
        Optional<ApplicationPermissions> applicationPermissions = applicationPermissionsRepository.findByPermissionCode(permissionCode);
        if (applicationPermissions.isPresent())
            appPermission = applicationPermissions.get();
        return appPermission;
    }
    @Override
    public void validateUser(String objectId) {
		Optional<Users> userInfo = userRepository.findByObjectId(objectId);
		if (userInfo.isPresent()) {
			if (userInfo.get().getStatusId() == StatusTypes.ACTIVE.getStatusId())
				throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "User already exists");
			if (userInfo.get().getStatusId() == StatusTypes.INACTIVE.getStatusId())
				throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "User is InActive");
			if (userInfo.get().getStatusId() == StatusTypes.DELETE.getStatusId())
				throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "User is Deleted");
		}
    }
	@Override
	public Users addUser(Users user) {
		log.info("Inside UserServiceImpl.addUser");
		Users userData = null;
        List <UserApprovalLimit> userApprovalLimit = user.getUserApprovalLimit();
        for (UserApprovalLimit approvalLimit:userApprovalLimit) {
            approvalLimit.setApprovalLimitType(approvalLimitTypeRepository.findByApprovalLimitTypeCode(approvalLimit.getApprovalLimitType().getApprovalLimitTypeCode()).get());
            approvalLimit.setUsers(user);
        }
        Users userInfo = findUserById(Integer.valueOf(user.getUserId()));
        if(userInfo != null) {
        	user.setStatusId(2);
        }
        userData = userRepository.save(user);
		return userData;
	}

	public Users updateUser(UserDTO userDTO){
        log.info("Inside UserServiceImpl.updateUser");
        Users user = null;
        if (userDTO != null){
            user = findUserById(Integer.valueOf(userDTO.getUserId()));
            user.setPhoneNumber(userDTO.getPhoneNumber());
            user.setFaxNumber(userDTO.getFaxNumber());
            user.setEmailAddress(userDTO.getEmailAddress());
            List<UserApprovalLimit> userApprovalLimitList = null;
            if(user.getUserApprovalLimit()!=null && !user.getUserApprovalLimit().isEmpty()){
                userApprovalLimitList = user.getUserApprovalLimit();
                for (UserApprovalLimit userApprovalLimit:userApprovalLimitList) {
                    if(userApprovalLimit.getApprovalLimitType().getApprovalLimitTypeCode().equalsIgnoreCase("RETAUTH"))
                        userApprovalLimit.setApprovalLimit(userDTO.getReturnAuthorizationLimit());
                    if(userApprovalLimit.getApprovalLimitType().getApprovalLimitTypeCode().equalsIgnoreCase("CREDMEMO"))
                        userApprovalLimit.setApprovalLimit(userDTO.getCreditMemoLimit());
                    if(userApprovalLimit.getApprovalLimitType().getApprovalLimitTypeCode().equalsIgnoreCase("INSPECTION"))
                        userApprovalLimit.setApprovalLimit(userDTO.getInspectionServiceLimit());
                    if(userApprovalLimit.getApprovalLimitType().getApprovalLimitTypeCode().equalsIgnoreCase("INSTALLATION"))
                        userApprovalLimit.setApprovalLimit(userDTO.getInstallationServiceLimit());
                    if(userApprovalLimit.getApprovalLimitType().getApprovalLimitTypeCode().equalsIgnoreCase("CHARGES"))
                        userApprovalLimit.setApprovalLimit(userDTO.getOtherChargesLimit());
                }
            } else{
                userApprovalLimitList = new ArrayList<>();
                UserApprovalLimit userApprovalLimit = null;
                if(userDTO.getReturnAuthorizationLimit()!=null){
                    userApprovalLimit = new UserApprovalLimit();
                    userApprovalLimit.setApprovalLimit(userDTO.getReturnAuthorizationLimit());
                    userApprovalLimit.setApprovalLimitType(approvalLimitTypeRepository.findByApprovalLimitTypeCode("RETAUTH").get());
                    userApprovalLimit.setUsers(user);
                    userApprovalLimitList.add(userApprovalLimit);
                }
                if(userDTO.getCreditMemoLimit()!=null){
                    userApprovalLimit = new UserApprovalLimit();
                    userApprovalLimit.setApprovalLimit(userDTO.getCreditMemoLimit());
                    userApprovalLimit.setApprovalLimitType(approvalLimitTypeRepository.findByApprovalLimitTypeCode("CREDMEMO").get());
                    userApprovalLimit.setUsers(user);
                    userApprovalLimitList.add(userApprovalLimit);
                }
                if(userDTO.getInspectionServiceLimit()!=null){
                    userApprovalLimit = new UserApprovalLimit();
                    userApprovalLimit.setApprovalLimit(userDTO.getInspectionServiceLimit());
                    userApprovalLimit.setApprovalLimitType(approvalLimitTypeRepository.findByApprovalLimitTypeCode("INSPECTION").get());
                    userApprovalLimit.setUsers(user);
                    userApprovalLimitList.add(userApprovalLimit);
                }
                if(userDTO.getInstallationServiceLimit()!=null){
                    userApprovalLimit = new UserApprovalLimit();
                    userApprovalLimit.setApprovalLimit(userDTO.getInstallationServiceLimit());
                    userApprovalLimit.setApprovalLimitType(approvalLimitTypeRepository.findByApprovalLimitTypeCode("INSTALLATION").get());
                    userApprovalLimit.setUsers(user);
                    userApprovalLimitList.add(userApprovalLimit);
                }
                if(userDTO.getOtherChargesLimit()!=null){
                    userApprovalLimit = new UserApprovalLimit();
                    userApprovalLimit.setApprovalLimit(userDTO.getOtherChargesLimit());
                    userApprovalLimit.setApprovalLimitType(approvalLimitTypeRepository.findByApprovalLimitTypeCode("CHARGES").get());
                    userApprovalLimit.setUsers(user);
                    userApprovalLimitList.add(userApprovalLimit);
                }
                user.setUserApprovalLimit(userApprovalLimitList);
            }
            user = userRepository.save(user);
        }
        return user;
    }
	
	@Override
	public Users deleteUserById(int userId) {
		log.info("Inside UserServiceImpl.deleteUserById");
        Users users = findUserById(userId);
        users.setStatusId(3);
        return userRepository.save(users);
	}
	@Override
	public Users userStatusToggle(int userId, int statusId) {
		log.info("Inside UserServiceImpl.userStatusToggle");
        Users users = findUserById(userId);
        users.setStatusId(statusId);
        users.setModifiedDateTime(LocalDateTime.now());
        return userRepository.save(users);
	}
	@Override
	public List<UserGroups> getAllUserGroups(String action) {
		log.info("Inside UserServiceImpl.getAllUserGroups");
        List<UserGroups> userGroupsList = null;
        if(action == null || action.isEmpty()) {
            userGroupsList = userGroupsRepository.findAllActiveUserGroups();
        }else {
            if (action.equalsIgnoreCase(Action.ASSIGN.getAction()))
                userGroupsList = userGroupsRepository.findAllActiveClaimAssignmentUserGroups();
            else if (action.equalsIgnoreCase(Action.ROUTE.getAction()))
                userGroupsList = userGroupsRepository.findAllActiveClaimRoutingUserGroups();
        }
        return userGroupsList;
	}
    @Override
    public UserGroups getUserGroupByUserGroupId(int userGroupId){
        UserGroups userGroups = null;
        Optional<UserGroups> userGroupsOptional = userGroupsRepository.findById(userGroupId);
        if(userGroupsOptional.isPresent()) {
            userGroups = userGroupsOptional.get();
        } else {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Record Not Found with userGroupId :: " + userGroupId);
        }
        return userGroups;
    }
    @Override
    public List<Users> getUserByGroupId(int userGroupId) {
        UserGroups userGroups = getUserGroupByUserGroupId(userGroupId);
        List<Users> userList = userGroups.getUsers();
        return userList.stream().filter(usr->usr.getStatusId() == 1).distinct().toList();
    }
    @Override
    public List<Integer> getUsersIds(int userId) {
        List<Integer> userIdList = new ArrayList<>();
        userIdList.add(userId);
        try {
            List<UserGroupMapping> userGroupMapping = getUserGroupMappingByManagerId(userId);
            boolean flag = !userGroupMapping.isEmpty() ? userGroupMapping.stream().anyMatch(ugm -> ugm.getManagerId() == userId) : false;
            if (flag) {
                userIdList.addAll(userGroupMapping.stream().map(ugm -> ugm.getUserId()).distinct().collect(Collectors.toList()));
            }
        }catch (Exception ex){
            return userIdList;
        }
        return userIdList;
    }
    public Boolean isManager(int userId) {
        List<UserGroupMapping> userGroupMapping = getUserGroupMappingByManagerId(userId);
        return !userGroupMapping.isEmpty();
    }
    @Override
    public Users getManager(int userId, int userGroupId) {
        Integer managerId = userGroupMappingRepository.getManager(userId, userGroupId);
        if (managerId != null) {
            Optional<Users> managerOpt = userRepository.findById(managerId);
            if (managerOpt.isPresent()) {
                Users manager = managerOpt.get();
                manager.setUserGroups(null);
                manager.setUserApprovalLimit(null);
                manager.setLocations(null);
                return manager;
            } else {
                throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                        "Record Not Found with userId :: " + managerId);
            }
        }else {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Record Not Found with userId :: " + userId + " and userGroupId :: " + userGroupId);
        }
    }
    @Override
    public UserGroupMappingDTO getUserGroupAndManager(int userId) {
        UserGroupMappingDTO userGroupMappingDTO = new UserGroupMappingDTO();
        List<UserGroupDTO> userGroupDTOList = new ArrayList<>();
        UserGroupDTO userGroupDTO = null;
        List<UserGroupMapping> ugmList = userGroupMappingRepository.findByUserId(userId);
        userGroupMappingDTO.setUserId(userId);
        for (UserGroupMapping ugm : ugmList) {
            userGroupDTO = new UserGroupDTO();
            Users manager = findUserById(ugm.getManagerId());
            Optional<UserGroups> userGroups = userGroupsRepository.findById(ugm.getUserGroupId());
            UserGroups ug = userGroups.get();
            userGroupDTO.setUserGroupId(ug.getUserGroupId());
            userGroupDTO.setUserGroupName(ug.getUserGroupDescription());
            userGroupDTO.setManagerId(manager.getUserId());
            userGroupDTO.setManagerName(manager.getFirstName() + " " + manager.getLastName());
            userGroupDTOList.add(userGroupDTO);
        }
        userGroupMappingDTO.setUserGroupDTO(userGroupDTOList);
        return  userGroupMappingDTO;
    }
    @Override
    public Integer getUserIdByGroupCode(String userGroupCode) {
        int userId = 0;
        Optional<UserGroups> userGroupsOptional = userGroupsRepository.findByUserGroupCode(userGroupCode);
        if(userGroupsOptional.isPresent()) {
            userId = userGroupsOptional.get().getUserGroupId();
        } else {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Record Not Found with userGroupCode :: " + userGroupCode);
        }
        return userId;
    }
    @Override
    public List<UserGroupMapping> getUserGroupById(int userId) {
        List<UserGroupMapping> userGroupMapping = userGroupMappingRepository.findByUserId(userId);
        if(userGroupMapping.isEmpty()){
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Record Not Found with userIdId :: " + userId);
        }
        return userGroupMapping;
    }

    private List<UserGroupMapping> getUserGroupMappingByManagerId(int managerId) {
        List<UserGroupMapping> userGroupMapping = userGroupMappingRepository.findByManagerId(managerId);
        if(userGroupMapping.isEmpty()){
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Record Not Found with managerId :: " + managerId);
        }
        return userGroupMapping;
    }

    @Override
    public Integer getUserIdByActiveDirectoryId(String activeDirectoryId) {
        int userId = 0;
        Optional<Users> usersOptional = userRepository.findByActiveDirectoryId(activeDirectoryId);
        if(usersOptional.isPresent()) {
            userId = usersOptional.get().getUserId();
        } else {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Record Not Found with activeDirectoryId :: " + activeDirectoryId);
        }
        return userId;
    }

    @Override
    public List<ApprovalLimitType> getApprovalLimitTypes() {
        return approvalLimitTypeRepository.findAllActiveApprovalLimitType();
    }
    public void fetchUserGroupMappingByIds(int userId, int userGroupId, int managerId, boolean flag){
        UserGroupMapping userGroupMappingInfo = userGroupMappingRepository.findByUserIdAndUserGroupIdAndManagerId(userId, userGroupId, managerId);
        List<UserGroupMapping> userGroupMapping= userGroupMappingRepository.findByUserIdAndUserGroupId(userId, userGroupId);
       
       /* if(flag && userGroupMappingInfo != null) {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "User Group Mapping Already Exist for " +
                    "userId ::" + userId +
                    " groupId ::" + userGroupId +
                    " managerId ::" + managerId);*/
            if(flag && userGroupMapping.size()>=1) {
           	 throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "User Group Mapping Already Exist for " +
                        "userId ::" + userId +
                        " groupId ::" + userGroupId);
           }
        else if(!flag && userGroupMappingInfo == null){
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "User Group Mapping does not Exist for " +
                    "userId ::" + userId +
                    " groupId ::" + userGroupId +
                    " managerId ::" + managerId);
        }
    }
	@Override
	public List<UserGroupMapping> addUserGroupMapping(UserGroupMappingDTO userGroupMappingDTO) {
		log.info("Inside UserServiceImpl.addUserGroupMapping");
		List<UserGroupMapping> listUserGroupMapping = new ArrayList<>();
		userGroupMappingDTO.getUserGroupDTO().forEach(u -> {
            if (userGroupMappingDTO.getUserId() == u.getManagerId()){
                throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "User and its Manager can not be same");
            }
			//added duplication check
			fetchUserGroupMappingByIds(userGroupMappingDTO.getUserId(), u.getUserGroupId(), u.getManagerId(), true);
			UserGroupMapping userGroupMapping = new UserGroupMapping();
			userGroupMapping.setUserGroupId(u.getUserGroupId());
			userGroupMapping.setUserId(userGroupMappingDTO.getUserId());
			userGroupMapping.setManagerId(u.getManagerId());
			listUserGroupMapping.add(userGroupMapping);
		});
		List<UserGroupMapping> userGropMappingData = userGroupMappingRepository.saveAll(listUserGroupMapping);
		return userGropMappingData;
	}

	@Override
	public void deletePermissionsToRoles(RolePermissionDTO rolePermissionDTO) {
		log.info("Inside UserServiceImpl.deletePermissionsToRoles");
		ApplicationRoles role = findRoleById(rolePermissionDTO.getRoleId());
		for (int permissionId : rolePermissionDTO.getPermissionIdData()) {
			ApplicationPermissions permissions = findPermissionsById(permissionId);
			boolean permissionExists = role.getPermissions().stream()
					.anyMatch(permission -> permission.getPermissionId() == permissionId);
			if (permissionExists) {
				role.getPermissions().remove(permissions);
			} else {
				throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "permission doesn't exists for RoleId - "
						+ rolePermissionDTO.getRoleId() + " and PermissionId - " + permissionId);
			}

		}
		applicationRolesRepository.save(role);
	}
	
	@Override
	public UserApprovalLimitDTO getApprovalLimitByUserId(int userId, String approvalLimitType) {
		Users user = findUserById(Integer.valueOf(userId));
		UserApprovalLimitDTO userApprovalLimit = new UserApprovalLimitDTO();
		if (user != null) {
			for (UserApprovalLimit userInfo : user.getUserApprovalLimit()) {
				if (userInfo.getApprovalLimitType().getApprovalLimitTypeCode()
						.equalsIgnoreCase(approvalLimitType)) {
					BeanUtils.copyProperties(userInfo, userApprovalLimit);
				}
			}
		}
		return userApprovalLimit;
	}

    @Override
    public String findUserName(int userId) {
        log.info("Inside UserServiceImpl.findUserName");
        return userRepository.findUserName(userId);
    }

    @Override
    public RolesAndPermissionStatusDTO rolesAndPermissionStatusToggle(Integer roleId, Integer permissionId, int statusId) {
    	log.info("Inside UserServiceImpl.rolesAndPermissionStatusToggle");

    	RolesAndPermissionStatusDTO rolesAndPermissionStatusDTO = new RolesAndPermissionStatusDTO();

    	if(roleId != null) {
    		Optional<ApplicationRoles> applicationRoles = applicationRolesRepository.findById(roleId);
        	if (applicationRoles.isPresent()) {
        		applicationRoles.get().setStatusId(statusId);
        		applicationRoles.get().setModifiedDateTime(LocalDateTime.now());
        		ApplicationRoles savedRoleInfo = applicationRolesRepository.save(applicationRoles.get());
        		rolesAndPermissionStatusDTO.setRoleId(savedRoleInfo.getRoleId());
        		rolesAndPermissionStatusDTO.setStatusId(savedRoleInfo.getStatusId());

        	}
    	}
    	
    	if(permissionId != null) {
    		Optional<ApplicationPermissions> applicationPermission = applicationPermissionsRepository
        			.findById(permissionId);
        	if (applicationPermission.isPresent()) {
        		applicationPermission.get().setStatusId(statusId);
        		applicationPermission.get().setModifiedDateTime(LocalDateTime.now());
        		ApplicationPermissions savedPermissionInfo = applicationPermissionsRepository
        				.save(applicationPermission.get());
        		rolesAndPermissionStatusDTO.setPermissionId(savedPermissionInfo.getPermissionId());
        		rolesAndPermissionStatusDTO.setStatusId(savedPermissionInfo.getStatusId());
        	}
    	}
    	return rolesAndPermissionStatusDTO;
    }

    @Override
    public List<UserGroupMapping> userGroupByUserAndUserGroupID(int userId, int userGroupId) {
        List<UserGroupMapping> userGroupMappingList = userGroupMappingRepository.findByUserIdAndUserGroupId(userId,userGroupId);
        if(userGroupMappingList == null || userGroupMappingList.isEmpty())
            throw new CommonException("No UserGroupMapping found for userId " + userId + " and userGroupId " + userGroupId);
        return userGroupMappingList;
    }
}

