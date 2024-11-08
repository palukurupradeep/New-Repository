import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClaimCategoryDetails } from 'src/app/interfaces/customer.model';
import { userGroups, userMappingFindAllusersDetails } from 'src/app/interfaces/user-mapping.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserMappingService {
  
  private modifiedApiUrl = environment.apiUrl_3
  
   constructor(private http: HttpClient) { }

  getAllUsers(page: number, size: number): Observable<userMappingFindAllusersDetails> {
    return this.http.get<userMappingFindAllusersDetails>(
      this.modifiedApiUrl + 'security/users/v1/users?page='+page+'&size='+size
    );
  }
  findByUser(searchType: String, value: String): Observable<ClaimCategoryDetails> {
    return this.http.get<ClaimCategoryDetails>(
      this.modifiedApiUrl + 'security/users/v1/findUser?searchType='+searchType+'&value='+value
    );
  }

  addUser(userData: any): Observable<HttpResponse<any>>{
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl}security/users/v1/addUser`, userData, { observe: 'response' });

  }

  updateUser(userData: any): Observable<HttpResponse<any>>{
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl}security/users/v1/updateUser`, userData, { observe: 'response' });

  }

  changeUserStatus(userId: number, statusId: number){
    return this.http.put<any>(
      this.modifiedApiUrl + 'security/users/v1/userStatusToggle?userId='+userId+'&statusId='+statusId, {}
    )
  }

  deleteUserById(userId: number){
    return this.http.delete<any>(this.modifiedApiUrl + 'security/users/v1/deleteUserById?userId=' + userId);
  }


  // getAllUserGroups(action?:string){
  //   return this.http.get<userGroups[]>(
  //     this.modifiedApiUrl + 'users/v1/getAllUserGroups?action=' + action
  //   );
  // }
  getAllUserGroups(action?: string) {
    return this.http.get<userGroups[]>(
      this.modifiedApiUrl + 'security/users/v1/getAllUserGroups' + (action ? '?action=' + action : '')
    );
  }
  
  getManagerById(id: number){
    return this.http.get<any>(
      this.modifiedApiUrl + 'security/users/v1/getUserByGroupId?userGroupId=' + id
    );
  }
 
  fetchUserFromAD(searchValues:string) {
    return this.http.get<any>(
      this.modifiedApiUrl + 'security/ad/v1/fetchUserFromAD?' + searchValues
    );
  }

  getApprovalTypes(){
    return this.http.get<any>(
      this.modifiedApiUrl + 'security/users/v1/approvalLimitTypes'
    );
  }

  fetchRolesAndPermissionByUserId(userId :number){
    return this.http.get<any>(
      this.modifiedApiUrl + 'security/users/v1/fetchRolesAndPermissionByUserId?userId=' + userId
    );
  }

  addUserGroupMapping(UserGroupMap:any){
    return this.http.post<any>(`${this.modifiedApiUrl}security/users/v1/addUserGroupMapping`, UserGroupMap);
  }

  refreshUser(userId: number){
    return this.http.put<any>(this.modifiedApiUrl + 'security/ad/v1/syncUsersFromAD?userId=' + userId, {observe: 'response'});
  }

  findByUserId(userId: number){
    return this.http.get<any>(this.modifiedApiUrl + 'security/users/v1/findUserById?userId=' + userId);
  }
 deleteUserToUserGroupMapping(userId: number, userGroupId: number, managerId: number): Observable<HttpResponse<any>> {
  const url = `${this.modifiedApiUrl}security/users/v1/deleteUserToUserGroupMapping?userId=${userId}&userGroupId=${userGroupId}&managerId=${managerId}`;

  return this.http.delete<HttpResponse<any>>(url, { observe: 'response' });
}
  
  
  getManager(userId: number, userGroupId: number): Observable<any> {
    const url = `${this.modifiedApiUrl}security/users/v1/getManager?userId=${userId}&userGroupId=${userGroupId}`;
    return this.http.get(url);
  }
  getUserGroupAndManager(userId: number): Observable<any>  {
    const url = `${this.modifiedApiUrl}security/users/v1/getUserGroupAndManager?userId=${userId}`;
    return this.http.get(url);
  }
  validateUser(objectId: string): Observable<HttpResponse<any>> {
    return this.http.get<HttpResponse<any>>(
        `${this.modifiedApiUrl}security/users/v1/validateUser?objectId=${objectId}`,
        { observe: 'response' }
    );
}




}

 