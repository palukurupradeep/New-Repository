import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BulkUploadService {

  private modifiedApiUrl = environment.apiUrl_2
  constructor(private http: HttpClient) { 

 }

  createBatch(payload : any):Observable<HttpResponse<any>> {
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/claimbatch/v1/create`, payload ,{ observe: 'response' });
  }
  getBatchStatus():Observable<HttpResponse<any>> {
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/claimbatch/v1/getBatchStatus`, { observe: 'response' });
  }
  // fetchClaimBatchHeader(payload:any):Observable<HttpResponse<any>> {


  fetchClaimBatchHeader(payload: any): Observable<HttpResponse<any>>{
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/claimbatch/v1/fetchClaimBatchHeader`, payload ,{ observe: 'response' });
  }
}
