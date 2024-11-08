import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ClaimService {

  private modifiedApiUrl = environment.apiUrl_5
  constructor(private http: HttpClient) { }

  getClaimCount(activeDirId: string): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}onbase/v1/getClaimCount?activeDirId=${activeDirId}`, { observe: 'response' });
  }
}
