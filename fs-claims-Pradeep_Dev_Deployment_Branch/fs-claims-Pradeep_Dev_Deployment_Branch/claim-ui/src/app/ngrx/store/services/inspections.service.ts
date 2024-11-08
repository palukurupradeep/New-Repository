import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class InspectionsService {
  private modifiedApiUrl_1 = environment.apiUrl_1
  private modifiedApiUrl_2 = environment.apiUrl_2
  constructor(private http: HttpClient) { }
 
  
  getClaimVendorDetail(claimVendorNumber :string ):Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/claimvendor/v1/getClaimVendorDetail?claimVendorNumber=${claimVendorNumber}`, { observe: 'response' });
  }

  getAllInspectionServiceType():Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/inspectiondetails/v1/getAllInspectionServiceType`, { observe: 'response' });
  }
 
  getAllCertifyingAgency():Observable<HttpResponse<any>>{
  return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/inspectiondetails/v1/getAllCertifyingAgency`, { observe: 'response' });
  }


  getAllActiveInspectionServices():Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/inspectiondetails/v1/getAllActiveInspectionServices`, { observe: 'response' });
    }
   
  getAllInspectionStatus():Observable<HttpResponse<any>>{
      return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/inspectiondetails/v1/getAllInspectionStatus`, { observe: 'response' });
      }
   
  getAllActiveInspectionRequestTypes():Observable<HttpResponse<any>>{
     return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/inspectiondetails/v1/getAllActiveInspectionRequestTypes`, { observe: 'response' });
        }

   
  addClaimVendor(payLoad:any):Observable<HttpResponse<any>>{
     return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/claimvendor/v1/addClaimVendor`, payLoad, { observe: 'response' });
         }
}
