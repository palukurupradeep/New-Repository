import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApplicationObject, PermissionResponse, Role, RoleData, RolePermissionPayload, RoleResponse, RoleUsersPayload, mapRolePermission, mapRoleUser, mapRoleUser1, permissionData, permissionSetObj } from 'src/app/interfaces/roles-permission.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RolesService {

  private modifiedApiUrl = environment.apiUrl_3
  constructor(private http: HttpClient) {}

  public getApplicationPermissions(): Observable<any> {
    return this.http.get<any>(`${this.modifiedApiUrl}security/users/v1/applicationPermissions`);
  }

  public getUsers(): Observable<any> {
    return this.http.get<any>(`${this.modifiedApiUrl}security/users/v1/users`);
  }

  getRoles(): Observable<Role[]> { // Use the interface
    return this.http.get<Role[]>(`${this.modifiedApiUrl}security/users/v1/applicationRoles`);
    
  }
  addRole(roleData: RoleData): Observable<RoleResponse> {
    return this.http.post<RoleResponse>(`${this.modifiedApiUrl}security/users/v1/addRole`,roleData);
  }

  
  deleteRoleById(roleId: number): Observable<HttpResponse<any>> {
    return this.http.delete<HttpResponse<any>>(`${this.modifiedApiUrl}security/users/v1/deleteRoleById?roleId=${roleId}`, { observe: 'response' });
  }
  
  addPermission(permissionDataData: permissionData): Observable<PermissionResponse> {
    return this.http.post<PermissionResponse>(`${this.modifiedApiUrl}security/users/v1/addPermission`,permissionDataData);
  }

  deletePermissionById(permissionId: number): Observable<HttpResponse<any>> {
    return this.http.delete<HttpResponse<any>>(`${this.modifiedApiUrl}security/users/v1/deletePermissionById?permissionId=${permissionId}`, { observe: 'response' });
  }

  
  getApplicationObjects(): Observable<ApplicationObject[]> { // Use the interface
    return this.http.get<ApplicationObject[]>(`${this.modifiedApiUrl}security/users/v1/applicationObjects`);
    
  }
mapRolePermission(map: mapRolePermission): Observable<any> {
    return this.http.post<any>(this.modifiedApiUrl + 'security/users/v1/mapRolePermission', map);
}

  addUserstoRoles(map:mapRoleUser): Observable<any> {
  return this.http.post<any>(this.modifiedApiUrl+'security/users/v1/addUsersToRoles', map);
}
mapRoleUser(map:RoleUsersPayload): Observable<any> {
  return this.http.post<any>(this.modifiedApiUrl + 'security/users/v1/addUsersToRoles',map);
}

maptoRolePermission(requestBody:RolePermissionPayload): Observable<any> {
  return this.http.post<any>(this.modifiedApiUrl+'security/users/v1/mapRolePermission', requestBody);
 }

createPermissionSet(map:permissionSetObj):Observable<any> {
  return this.http.post<any>(this.modifiedApiUrl + 'security/users/v1/createPermissionSet',map)
}

getApplicationActions(): Observable<any[]> { // Use the interface
  return this.http.get<any[]>(`${this.modifiedApiUrl}security/users/v1/applicationActions`);
  
}

getApplicationFunctions(): Observable<any[]> { // Use the interface
  return this.http.get<any[]>(`${this.modifiedApiUrl}security/users/v1/applicationFunctions`);
  
}


deleteUserToRole(roleId: number, userId: number): Observable<HttpResponse<any>>{
  return this.http.delete<HttpResponse<any>>(`${this.modifiedApiUrl}security/users/v1/deleteUsersToRoles?roleId=${roleId}&userId=${userId}`, { observe: 'response' });
}

deletePermissionsToRoles(requestBody: RolePermissionPayload): Observable<HttpResponse<any>> {
  return this.http.delete<HttpResponse<any>>(this.modifiedApiUrl + 'security/users/v1/deletePermissionsToRoles', { 
    body: requestBody,
    observe: 'response'
  });
}


}
