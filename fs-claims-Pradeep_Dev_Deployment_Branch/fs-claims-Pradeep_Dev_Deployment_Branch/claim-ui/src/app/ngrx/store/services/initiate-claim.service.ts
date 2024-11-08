import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import {
  ClaimCategoryDetails,
  CountryCodeData,
  CustomerDetails,
  CustomerStoreNumbersResult,
  ReasonCodeByCategory,
  addInvoice,
} from 'src/app/interfaces/customer.model';

@Injectable({
  providedIn: 'root',
})
export class InitiateClaimService {

  private modifiedApiUrl_1 = environment.apiUrl_1
  private modifiedApiUrl_2 = environment.apiUrl_2
  constructor(private http: HttpClient) {}

  getCustomerDetails(customerNumber: string): Observable<CustomerDetails> {
    return this.http.get<CustomerDetails>(
      this.modifiedApiUrl_1 +
        'integration/customer/v1/getCustomerDetails?customerNumber=' +
        customerNumber
    );
  }
  getStoreDetails(storeNumber: number): Observable<CustomerStoreNumbersResult> {
    return this.http.get<CustomerStoreNumbersResult>(
      this.modifiedApiUrl_1 +
        'integration/customer/v1/getCustomerDetailsByStoreNumber?storeNumber=' +
        storeNumber
    );
  }
  getClaimCategories(): Observable<ClaimCategoryDetails> {
    return this.http.get<ClaimCategoryDetails>(
      this.modifiedApiUrl_2 + 'initiation/claim/v1/getClaimCategories'
    );
  }

  getAssociateGlobalAccount(id: string): Observable<any> {
    return this.http.get<any>(
      this.modifiedApiUrl_2 + 'initiation/lookup/v1/getLookupTypeByCode?lookupTypeCode=' + id
    );
  }
  getReasonCodeByCategory(id: number): Observable<ReasonCodeByCategory> {
    return this.http.get<ReasonCodeByCategory>(
      this.modifiedApiUrl_2 + 'initiation/claim/v1/getReasonCodeByCategory/' + id
    );
  }

 
  getSearchInvoice(requestBody: any): Observable<HttpResponse<any>> {
    return this.http.post(
      `${this.modifiedApiUrl_1}integration/customer/v1/getSearchInvoice`,
      requestBody,
      { observe: 'response' }
    );
  }
 
  public getAddInvioce(requestBody: any): Observable<HttpResponse<any>> {
    return this.http.get(
      `${this.modifiedApiUrl_1}integration/customer/v1/getInvoiceDetail?invNum=${requestBody.invoiceNumber}&invDt=${requestBody.invoiceDate}`,
      { observe: 'response' }
    );
  }
  
  getCountriesList(): Observable<any> {
    return this.http.get<any>(this.modifiedApiUrl_2 + 'initiation/claim/v1/getCountries');
  }
  public getCountryCodeByStates(id: number): Observable<CountryCodeData> {
    return this.http.get<CountryCodeData>(
      this.modifiedApiUrl_2 + 'initiation/claim/v1/getStatesByCountryId/' + id
    );
  }
  public getDialCodesByCountryId(id: number): Observable<any> {
    return this.http.get<any>(
      this.modifiedApiUrl_2 + 'initiation/claim/v1/getDialCodes' 
    );
  } 
  public viewPriorClaim(requestBody: any): Observable<any> {
    return this.http.post<any>(`${this.modifiedApiUrl_2}initiation/claim/v1/getPriorClaimSummary`, requestBody);
  }
  public finalInitiateClaim(requestBody: any): Observable<any> {
    return this.http.post<any>(
      `${this.modifiedApiUrl_2}initiation/claim/v1/initiate`,
      requestBody
    );
  }

  public endUserInformation(requestBody: any): Observable<any> {
    return this.http.post<any>(
      `${this.modifiedApiUrl_2}initiation/claimdetails/v1/addorUpdateEndUserInfo`,
      requestBody
    );
  }
 
  getClaimAssignment(claimId: number, claimUserId: number): Observable<any> {
    const urlWithParams = `${this.modifiedApiUrl_2}initiation/claim/v1/claimAssignment?claimId=${claimId}&claimUserId=${claimUserId}`;
    return this.http.post<any>(urlWithParams, {});
  }

  public automaticAssignment(requestBody: any): Observable<HttpResponse<any>> {
    return this.http.post(
      `${this.modifiedApiUrl_2}initiation/claim/v1/automaticAssignment`,
      requestBody,
      { observe: 'response' }
    );
    }
  getCrmReasons(): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_1}integration/customer/v1/getCrmReasons`, { observe: 'response' });
  }
  getClaimAssignmentData(customerNumber: string): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_1}integration/customer/v1/getClaimAssignmentData?customerNumber=${customerNumber}` ,{ observe: 'response' });
  }
  getInvoiceHeader(customerNumber: string,invoiceNumber:string,invoiceDate:string): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_1}integration/customer/v1/getInvoiceHeader?customerNumber=${customerNumber}&invoiceNumber=${invoiceNumber}&invoiceDate=${invoiceDate}` ,{ observe: 'response' });
  }

  getInvoiceCharges(customerNumber: string,invoiceNumber:string,invoiceDate:string): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_1}integration/customer/v1/getInvoiceCharges?customerNumber=${customerNumber}&invoiceNumber=${invoiceNumber}&invoiceDate=${invoiceDate}` ,{ observe: 'response' });
  }

  getCustomerCreditClaimsData(custNbr:string): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_1}integration/customer/v1/getCustomerCreditClaimsData?custNbr=${custNbr}` ,{ observe: 'response' });

  }
  getDocIdByDocumentTypeCode(documentTypeCode:string): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/claim/v1/getDocIdByDocumentTypeCode?documentTypeCode=${documentTypeCode}` ,{ observe: 'response' });
   }

  
   getTerritoryDivisionRegion(territoryNbr:string): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_1}integration/customer/v1/getTerritoryDivisionRegion?territoryNbr=${territoryNbr}` ,{ observe: 'response' });
   }
}