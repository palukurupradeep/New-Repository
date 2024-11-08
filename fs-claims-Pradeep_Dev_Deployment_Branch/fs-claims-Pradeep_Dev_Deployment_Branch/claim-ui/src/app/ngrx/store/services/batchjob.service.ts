import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BatchjobService {

  private modifiedApiUrl = environment.apiUrl_4
  
   constructor(private http: HttpClient) { }


  getJobs(){
    return this.http.get<any>(this.modifiedApiUrl + 'batch/Job/v1/jobs'); 
  }
  
  updateJob(payload: any): Observable<any> {
    return this.http.post<any>(this.modifiedApiUrl + 'batch/Job/v1/updateJob', payload);
  }
  addJob(payload: any): Observable<any> {
    return this.http.post<any>(this.modifiedApiUrl + 'batch/Job/v1/addJob', payload);
  }
}
