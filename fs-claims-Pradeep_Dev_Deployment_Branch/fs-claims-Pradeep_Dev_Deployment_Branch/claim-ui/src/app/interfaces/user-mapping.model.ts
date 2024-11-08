export interface userMappingFindAllusersDetails{
    
    createdByUserId : string,
    modifiedByUserId : string,
    createdDateTime : string,
    modifiedDateTime : string,
    userId : string,
    userKey : string,
    userName : string,
    activeDirectoryId : string,
    firstName : string,
    middleInitial : string,
    lastName : string,
    racfId : string,
    racfId2 : string,
    statusId : string,
    emailAddress : string,
    lid2 : string,
    lid : string,
    departmentName : string,
    faxNumber :string,
    locations :[],
    phoneNumber:string,
    title :string,
    userGroups :[],
     
}

export interface UserRequestObject{
createdByUserId: string,
modifiedByUserId: string,
createdDateTime: string,
modifiedDateTime: string,
userId: string,
userKey: string,
userName: string,
activeDirectoryId: string,
firstName: string,
middleInitial: string,
lastName: string,
racfId: string,
racfId2: string,
statusId: string,
emailAddress: string,
departmentName: string,
title: string,
phoneNumber: string,
faxNumber: string,
userGroups: userGroups[],
locations: [
  {
    createdByUserId: string,
    modifiedByUserId: string,
    createdDateTime: string,
    modifiedDateTime: string,
    locationId: string,
    locationCode: string,
    locationDescription: string,
    statusId: string
  }
],
userApprovalLimit: userApprovalLimit[],
lid: string,
lid2: string
}

export interface userGroups {
  createdByUserId: string,
  modifiedByUserId: string,
  createdDateTime: string,
  modifiedDateTime: string,
  userGroupId: string,
  userGroupCode: string,
  userGroupDescription: string,
  statusId: string,
  manager: manager
}

export interface manager{
 
    createdByUserId: number,
    modifiedByUserId: number,
    createdDateTime: string,
    modifiedDateTime: string,
    userId: number,
    userKey: number,
    userName: string,
    activeDirectoryId: string,
    firstName: string,
    middleInitial: string ,
    lastName: string,
    racfId: string,
    racfId2: string,
    statusId: 1,
    emailAddress:string,
    departmentName: string,
    title:string ,
    phoneNumber: string,
    faxNumber:number ,
    objectId: string,
    userGroups: string,
    locations: string,
    userApprovalLimit: string,
    lid2: string,
    lid: string
}


export interface userApprovalLimit {
  createdByUserId: string,
  modifiedByUserId: string,
  createdDateTime: string,
  modifiedDateTime: string,
  userApprovalLimitId: string,
  userId: string,
  approvalLimitType: approvalLimitType,
  approvalLimit: string,
  statusId: string
}

export interface approvalLimitType {
  createdByUserId: number,
  modifiedByUserId: number,
  createdDateTime: string,
  modifiedDateTime: string,
  approvalLimitTypeId: number,
  approvalLimitTypeCode: string,
  approvalLimitTypeDescription: string,
  statusId: number
}

export interface selectedGroupObject { 
  userGroup: string,
  managerGroup: string
}

export interface UserGroupMap{
  userId: number,
  userGroupDTO: UserGroupDTO[]
}
 export interface UserGroupDTO{
    userGroupId: number,
    managerId: number
 }   


