import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { CustomerTypeRequestPayload, CustomerTypeResponseModel, CustomerWatchlistPayload } from 'src/app/interfaces/dashboard.model';
import { ClaimDuesResponseModel } from 'src/app/interfaces/claim-dues.model';
// import { CustomerTypeRequestPayload, CustomerTypeResponseModel } from '../interfaces/dashboard.model';
// import { ClaimDuesResponseModel } from '../interfaces/claim-dues.model';
import { environment } from 'src/environments/environment';
 
@Injectable({
  providedIn: 'root'
})
export class DashboardService {
 

  private modifiedApiUrl = environment.apiUrl_2
  constructor(private http: HttpClient) { }
 
 
 
  fetchUserDashboardWidgetsByUserId(createdByUserId:number){
    return this.http.get<any>(
      this.modifiedApiUrl + 'initiation/dashboard/v1/fetchUserDashboardWidgetsByUserId?createdByUserId=' + createdByUserId
    );
  }
   
  getCustomerWatchListCountDetails(createdByUserId:number){
    return this.http.get<any>(
      this.modifiedApiUrl + 'initiation/dashboard/v1/getCustomerWatchList?createdByUserId=' + createdByUserId 
    );
  }

  getClaimWorkQueues(userId: number): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/workqueue/v1/getClaimWorkQueues?userId=${userId}`, { observe: 'response' });
  }
  
  getAgedClaims(userId: number,days:number): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/workqueue/v1/agedClaims?userId=${userId}&days=${days}`, { observe: 'response' });
  }
  getPriorityClaims(userId: number): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/workqueue/v1/getPriorityClaims?userId=${userId}`, { observe: 'response' });
  }

  lastActivityClaims(userId: number,days:number): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/workqueue/v1/lastActivityClaims?userId=${userId}&days=${days}`, { observe: 'response' });
  }

  previousDayClaims(userId: number): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/workqueue/v1/previousDayClaims?userId=${userId}`, { observe: 'response' });
  }

   public getClaimWatchList(userId:number){
    return this.http.get<any>(
      this.modifiedApiUrl + 'initiation/dashboard/v1/getClaimWatchList?userId=' +userId
    );
  }
 

  addorUpdateCustomerWatchlist(addorUpdateCustomerWatchlist:CustomerWatchlistPayload): Observable<HttpResponse<any>>{
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/dashboard/v1/addorUpdateCustomerWatchlist`, addorUpdateCustomerWatchlist ,{ observe: 'response' });
  }
  
 
  public claimsDue(userId: number){
    return this.http.get<any>(
      this.modifiedApiUrl + 'initiation/dashboard/v1/claimsDue?userId=' + userId 
    );
  }
 
  public overdueClaim(userId: number){
    return this.http.get<any>(
      this.modifiedApiUrl + 'initiation/dashboard/v1/overdueClaim?userId=' + userId 
    );
  }
  deleteCustomerWatchList(userId: number,customerNumber:string){
    return this.http.delete<any>(this.modifiedApiUrl + 'initiation/dashboard/v1/deleteCustomerWatchList?userId=' + userId +'&customerNumber=' +customerNumber);
  }
 
  deleteClaimWatchList(userId: number,claimId:number){
    return this.http.delete<any>(this.modifiedApiUrl + 'initiation/dashboard/v1/deleteClaimWatchList?userId=' + userId +'&claimId=' +claimId);
  }

  addUserDashboardWidgets(addUserWidgetPayload:any): Observable<HttpResponse<any>>{
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/dashboard/v1/addUserDashboardWidgets`, addUserWidgetPayload ,{ observe: 'response' });
  } 

  getCustomerWatchListStatus(userId: number,customerNumber:string){
    return this.http.get<any>(this.modifiedApiUrl + 'initiation/dashboard/v1/getCustomerWatchListStatus?userId=' + userId +'&customerNumber=' +customerNumber);
  }
}

