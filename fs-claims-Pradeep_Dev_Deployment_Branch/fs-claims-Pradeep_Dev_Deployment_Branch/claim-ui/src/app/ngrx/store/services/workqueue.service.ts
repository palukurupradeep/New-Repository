import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { WorkQueueField, WorkQueuePayload } from 'src/app/interfaces/workQueueField.model';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class WorkqueueService {
 
  private modifiedApiUrl_2 = environment.apiUrl_2
  private modifiedApiUrl_3 = environment.apiUrl_3
  constructor(private http: HttpClient) { }

  getClaimWorkQueues(userId: number): Observable<HttpResponse<any>>{
      return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/workqueue/v1/getClaimWorkQueues?userId=${userId}`, { observe: 'response' });
   }

  fetchUserWorkQueueFieldsByUserId(userId: number): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/workqueue/v1/fetchUserWorkQueueFieldsByUserId?createdByUserId=${userId}`, { observe: 'response' });
  }

  addUserWorkQueueFields(WorkQueueFieldPayload: WorkQueuePayload): Observable<HttpResponse<any>>{
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/workqueue/v1/addUserWorkQueueFields`, WorkQueueFieldPayload ,{ observe: 'response' });
  }

  deleteUserWorkQueueFields(userId: number): Observable<HttpResponse<any>> {
    return this.http.delete<HttpResponse<any>>(
      `${this.modifiedApiUrl_2}initiation/workqueue/v1/deleteUserWorkQueueFields?createdByUserId=${userId}`,
      { observe: 'response' }
    );
  }

  fetchWorkQueueFields():Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/workqueue/v1/fetchWorkQueueFields`, { observe: 'response' });
  }

  isManager(userId:number){
    return this.http.get<HttpResponse<any>>(
      `${this.modifiedApiUrl_3}security/users/v1/isManager?userId=${userId}`,
      { observe: 'response' }
    );
  }
}

