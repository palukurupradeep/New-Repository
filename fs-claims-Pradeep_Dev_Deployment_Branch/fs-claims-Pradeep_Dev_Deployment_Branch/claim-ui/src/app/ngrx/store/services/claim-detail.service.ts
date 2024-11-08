import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, catchError } from 'rxjs';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { CustomerTypeRequestPayload, CustomerTypeResponseModel } from 'src/app/interfaces/dashboard.model';
import { ClaimDuesResponseModel } from 'src/app/interfaces/claim-dues.model';
import { ClimDetailsResponce } from 'src/app/interfaces/claim-details.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ClaimDetailService {

  private modifiedApiUrl_2 = environment.apiUrl_2
  private modifiedApiUrl_3 = environment.apiUrl_3
  constructor(private http: HttpClient) { }
  
   public jobStopToggle(claimId:any, jobStatus:any,userId:number): Observable<any> {
        const urlWithParams = `${this.modifiedApiUrl_2}initiation/claimdetails/v1/jobStopToggle?claimId=${claimId}&jobStopped=${jobStatus}&userId=${userId}`;
        return this.http.put<any>(urlWithParams, {});
 }

public watchListToggle(payload: any): Observable<any> {
  const urlWithParams = `${this.modifiedApiUrl_2}initiation/claimdetails/v1/claimWatchlistToggle`;
  
  return this.http.put<any>(urlWithParams, payload);
}

public priorityClaimToggle(claimId:any, priorityclaim:any,noteText:string,userId:number): Observable<any> {
    const urlWithParams = `${this.modifiedApiUrl_2}initiation/claimdetails/v1/priorityClaimToggle?claimId=${claimId}&priorityClaim=${priorityclaim}&noteText=${noteText}&userId=${userId}`;
    return this.http.put<any>(urlWithParams, {});
}

public getClaim(claimNumber: string, userId: number) {
  return this.http.get<ClimDetailsResponce>(
    `${this.modifiedApiUrl_2}initiation/claimdetails/v1/getClaim?claimNumber=${claimNumber}&userId=${userId}`
  );
}



  public getClaims(){
    return this.http.get<[ClimDetailsResponce[]]
    >(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/getClaims'
    );
  }
  
  public getAllReasonCodes(): Observable<any> {
    return this.http.get<any>(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/getAllReasonCodes' 
    );
  }

  public getAllSellingCompanyData(){
    return this.http.get<any>(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/getAllSellingCompanyData' 
    );
  }

  public updateCustomerNumber(claimId: number, custNo: string){
    return this.http.put<any>(this.modifiedApiUrl_2 + 
      'initiation/claimdetails/v1/updateCustomerDetails?claimId=' + claimId + '&customerNumber=' + custNo, {})
  }

  public getClaimStatusById(id: number){
    return this.http.get<any>(
      this.modifiedApiUrl_2 + 'initiation/claim/v1/getClaimStatusById?claimStatusId=' + id 
    );
  }

  public getWorkStatusById(id: number,userId:number){
    return this.http.get<any>(
      this.modifiedApiUrl_2 + 'initiation/claim/v1/getWorkStatusById?workStatusId=' + id + '&userId=' + userId);
  }

  public getManager(userId: number, userGroupId: number){
    return this.http.get<any>(
      this.modifiedApiUrl_3 + 'security/users/v1/getManager?userId=' + userId + '&userGroupId=' + userGroupId
    );
  }

  public getAllDeclinedReasons(): Observable<any>{
    return this.http.get<any>(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/getAllDeclinedReasons'
    );
  }

  public updateDeclinedClaimStatus(updateUrl:any): Observable<any>{
    return this.http.put<any>(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/updateDeclinedClaimStatus?'+updateUrl, {}
    );
  }

  public updateAllClaimStatus(updateUrl:any): Observable<any>{
    return this.http.put<any>(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/updateClaimStatusByAction?'+updateUrl, {}
    );
  }

  // duplicate api
  public duplicateLineItem(item: any){
    return this.http.post<any>(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/duplicateLineItem', item
    );
  }

  public addInvoice(item: any){
    return this.http.post<any>(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/addInvoice', item
    );
  }
  

  // delete api
  public deleteLineItem(claimLineIds: number[]): Observable<any> {
    // Convert the array of IDs into a comma-separated string
    const ids = claimLineIds.join(',');
    return this.http.delete<any>(
      `${this.modifiedApiUrl_2}initiation/claimdetails/v1/deleteLineItem?claimLineIds=${ids}`
    );
  }

  // edit update api
  public updateLineItem(item: any){
    return this.http.put<any>(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/updateLineItem', item
    );
  }

  public laborLineItem(item: any){
    return this.http.post<any>(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/laborLineItem', item
    );
  }

  reAssign(claimId: any, userId: any, managerId: any){
    return this.http.put<any>(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/reAssign?claimId='+ claimId +'&userId='+ userId +'&adminId='+ managerId, ''
    )
  }

  public getClaimRateDetails(customerNumber:string){
    return this.http.get<any>(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/getClaimRateDetails?customerNumber=' + customerNumber
    );
  }
  
  public findDetailTypeCodeByDetailTypeId(detailTypeId:string){
    return this.http.get<any>(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/findDetailTypeCodeByDetailTypeId?detailTypeId=' + detailTypeId
    );
  }

  public addMoreLineItems(data: any){
    return this.http.post<any>(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/addMoreLineItems',  data
    );
  }
  public addGenericDocument(data: any){
    return this.http.post<any>(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/addGenericDocument',  data
    );
  }

 
  public mergedClaims(parentClaimId:number){
    return this.http.get<any>(
      this.modifiedApiUrl_2 + 'initiation/claim/v1/mergedClaims?parentClaimId=' + parentClaimId
    );
  }
  
  public unMergedClaims(parentClaimId:number, customerNumber:string){
    return this.http.get<any>(
      this.modifiedApiUrl_2 + 'initiation/claim/v1/unMergedClaims?parentClaimId=' + parentClaimId + '&customerNumber=' +customerNumber 
    );
  }

  public merged(requestPayload:any){
    return this.http.post<any>(
      this.modifiedApiUrl_2 + 'initiation/claim/v1/merge',requestPayload, {} 
    );
  }
  
  public unMerged(requestPayload:any){
    return this.http.post<any>(
      this.modifiedApiUrl_2 + 'initiation/claim/v1/unMerge',requestPayload, {}
    );
  }

  // Create Serive api
  public createService(item: any){
    return this.http.post<any>(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/serviceLineItem', item
    );
  }
  
  public getAllClaimLineAddReason(){
    return this.http.get<any>(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/getAllClaimLineAddReason'
    );
  }

  public getClaimUnitOfMeasure(){
    return this.http.get<any>(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/getClaimUnitOfMeasure'
    );
  }
  
  public getPriorClaimForInvoice(documentNumber:number, documentTypeId: number, claimId:number): Observable<any>{
    return this.http.get<any>(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/getPriorClaimForInvoice?documentNumber='+ documentNumber + '&documentTypeId=' + documentTypeId + '&claimId=' + claimId, {} 
    );
  } 
  
  public getAllLineCategory(){
    return this.http.get<any>(
      this.modifiedApiUrl_2 + 'initiation/claimdetails/v1/getAllLineCategory'
    );
  }

  
  findByClaimReasonCode(reasonCode: string): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/claimdetails/v1/findByClaimReasonCode?reasonCode=${reasonCode}` ,{ observe: 'response' });
  }
  public updateClaimAddress(payload: any){
    return this.http.put<any>(this.modifiedApiUrl_2 + 
      'initiation/claim/v1/updateClaimAddress',payload
      )
  }
  private claimDetailsSubject = new BehaviorSubject<any>(null);
  claimDetails$ = this.claimDetailsSubject.asObservable();

  setClaimDetails(claimDetails: any) {
    this.claimDetailsSubject.next(claimDetails);
  }

  fetchAuditHistoryByClaimId(claimId:number):Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/claimdetails/v1/fetchAuditHistoryByClaimId?claimId=${claimId}` ,{ observe: 'response' });
  }
  validateSmartCodeRules(payload:any):Observable<HttpResponse<any>>{
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/claimdetails/v1/validateSmartCodeRules`, payload, { observe: 'response' });
  }

  routed(payload:any):Observable<HttpResponse<any>>{
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/claimdetails/v1/routed`, payload, { observe: 'response' });
  }
  
  updatePauseClaimNote(payload:any):Observable<HttpResponse<any>>{
    return this.http.put<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/claimdetails/v1/updatePauseClaimNote`, payload, { observe: 'response' });
  }
  
  prepareRGA(payload:any):Observable<HttpResponse<any>>{
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/claimdetails/v1/prepareRGA`, payload, { observe: 'response' });
  }

  fetchRecipients(claimId:number):Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/smartcode/v1/fetchRecipients?claimId=${claimId}`, { observe: 'response' });
  }
  getReturnLocations(locationCode:string):Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/smartcode/v1/getReturnLocations?locationCode=${locationCode}`, { observe: 'response' });
  }
  fetchRgaDetails(claimId:number ):Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/rga/v1/fetchRgaDetails?claimId=${claimId}`, { observe: 'response' });
  }
  rgaDraftORSave(rgaPayload:any):Observable<HttpResponse<any>>{
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/rga/v1/rgaDraftORSave`, rgaPayload, { observe: 'response' });
  }
  updateRGAStatusByAction(claimRgaHeaderId:number,loggedInUserId:number,action:string,noteText:string):Observable<HttpResponse<any>>{
    return this.http.put<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/rga/v1/updateRGAStatusByAction?claimRgaHeaderId=${claimRgaHeaderId}&loggedInUserId=${loggedInUserId}&action=${action}&noteText=${noteText}`, { observe: 'response' });
  }

  cancelRga(claimId: number, rgaNumber: number): Observable<HttpResponse<any>> {
    return this.http.put<HttpResponse<any>>(
      `${this.modifiedApiUrl_2}initiation/rga/v1/cancelRga?claimId=${claimId}&rgaNumber=${rgaNumber}`, 
      {}, 
      { observe: 'response' }
    );
  }
  

  issueRGAValidation(claimRgaHeaderId:number):Observable<HttpResponse<any>>{
    return this.http.put<HttpResponse<any>>(`${this.modifiedApiUrl_2}rga/v1/issueRGAValidation?claimRgaHeaderId=${claimRgaHeaderId}`, { observe: 'response' });
  }

  ticCalculation(claimId:number,claimLineId:number,estimateAmount:number):Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/ticCalculation/v1/ticCalculation?claimId=${claimId}&claimLineId=${claimLineId}&estimateAmount=${estimateAmount}`, { observe: 'response' });
  }


  findAllLaborRates(): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/ticCalculation/v1/findAllLaborRates`, { observe: 'response' });
  }

  fetchSampleSizeRequirementAndTesting(reasonCode:string):Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/samplereview/v1/fetchSampleSizeRequirementAndTesting?reasonCode=${reasonCode}`, { observe: 'response' });
  }
 
  updateSampleReviewByAction(updateSamplePayLoad:any):Observable<HttpResponse<any>>{
    return this.http.put<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/samplereview/v1/updateSampleReviewByAction`, updateSamplePayLoad, { observe: 'response' });
  }

 
  sampleRequest(samplePayLoad:any):Observable<HttpResponse<any>>{
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/samplereview/v1/sampleRequest`, samplePayLoad, { observe: 'response' });
  }
  // fetchServiceToKeepType(): Observable<HttpResponse<any>> {
  //   return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/rga/v1/serviceToKeepType`, { observe: 'response' });
  // }

  // fetchDocumentsByClaimId(claimId:number):Observable<HttpResponse<any>>{
  //   return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/crm/v1/fetchDocumentsByClaimId?claimId=${claimId}`, { observe: 'response' });
  // }
  // fetchCrmDetailsByClaimIdAndCrmNumber(claimId:number, crmNumber :number ):Observable<HttpResponse<any>>{
  //   return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/crm/v1/fetchCrmDetailsByClaimIdAndCrmNumber?claimId=${claimId}&crmNumber=${crmNumber}`, { observe: 'response' });
  // }

  // restockFee(restockFeePayload:any):Observable<HttpResponse<any>>{
  //   return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/crm/v1/restockFee`, restockFeePayload, { observe: 'response' });
  // }
  // crmHoldOrIssue(scrmHoldOrIssuePayload:any):Observable<HttpResponse<any>>{
  //   return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/crm/v1/crmHoldOrIssue`, scrmHoldOrIssuePayload, { observe: 'response' });
  // }
  // updateCrmDetailsByAction(claimCrmHeaderId:number,action:string):Observable<HttpResponse<any>>{
  //   return this.http.put<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/crm/v1/updateCrmDetailsByAction?claimCrmHeaderId=${claimCrmHeaderId}&Action=${action}`, { observe: 'response' });
  // }

  // claimCrmHeaderId=25&userId=3107&customerNumber=0112082
  creditMemoValidation(claimCrmHeaderId:number,userId:number,customerNumber:string,isTraceTaskOpen:boolean):Observable<HttpResponse<any>>{
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/crm/v1/creditMemoValidation?claimCrmHeaderId=${claimCrmHeaderId}&userId=${userId}&customerNumber=${customerNumber}&isTraceTaskOpen=${isTraceTaskOpen}`, { observe: 'response' });
  }

 
  // getAllDispositionTypes():Observable<HttpResponse<any>>{
  //   return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/crm/v1/getAllDispositionTypes`, { observe: 'response' });
  // }



  dispositionHoldOrIssue(payload:any):Observable<HttpResponse<any>>{
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/disposition/v1/dispositionHoldOrIssue`, payload,{ observe: 'response' });
  }

  // fetchRgaDetailsByClaimIdAndRgaNumber(claimId:number, rgaNumber :number ):Observable<HttpResponse<any>>{
  //   return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/rga/v1/fetchRgaDetailsByClaimIdAndRgaNumber?claimId=${claimId}&rgaNumber=${rgaNumber}`, { observe: 'response' });
  // }

 
  dispositonVoid(claimDispositionHeaderId:number):Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/disposition/v1/dispositonVoid?claimDispositionHeaderId=${claimDispositionHeaderId}`, { observe: 'response' });
  }
  fetchServiceToKeepType(): Observable<HttpResponse<any>> {
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/rga/v1/serviceToKeepType`, { observe: 'response' });
  }

  fetchDocumentsByClaimId(claimId:number):Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/crm/v1/fetchDocumentsByClaimId?claimId=${claimId}`, { observe: 'response' });
  }
  fetchCrmDetailsByClaimIdAndCrmNumber(claimId:number, crmNumber :number ):Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/crm/v1/fetchCrmDetailsByClaimIdAndCrmNumber?claimId=${claimId}&crmNumber=${crmNumber}`, { observe: 'response' });
  }

  restockFee(restockFeePayload:any):Observable<HttpResponse<any>>{
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/crm/v1/restockFee`, restockFeePayload, { observe: 'response' });
  }
  crmHoldOrIssue(scrmHoldOrIssuePayload:any):Observable<HttpResponse<any>>{
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/crm/v1/crmHoldOrIssue`, scrmHoldOrIssuePayload, { observe: 'response' });
  }

  updateCrmDetailsByAction(claimCrmHeaderId:number,action:string,userId:number,noteText:string):Observable<HttpResponse<any>>{
    return this.http.put<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/crm/v1/updateCrmDetailsByAction?claimCrmHeaderId=${claimCrmHeaderId}&Action=${action}&userId=${userId}&noteText=${noteText}`, { observe: 'response' });
  }

  
  // creditMemoValidation(claimCrmHeaderId:number):Observable<HttpResponse<any>>{
  //   return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/crm/v1/creditMemoValidation?claimCrmHeaderId=${claimCrmHeaderId}`, { observe: 'response' });
  // }

 
  getAllDispositionTypes():Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/crm/v1/getAllDispositionTypes`, { observe: 'response' });
  }
  fetchRgaDetailsByClaimIdAndRgaNumber(claimId:number, rgaNumber :number ):Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/rga/v1/fetchRgaDetailsByClaimIdAndRgaNumber?claimId=${claimId}&rgaNumber=${rgaNumber}`, { observe: 'response' });
  }

 
  fetchDispositonDetailsByClaimIdAndDispositonNumber(claimId:number,dispositionNumber:string):Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/disposition/v1/fetchDispositonDetailsByClaimIdAndDispositonNumber?claimId=${claimId}&dispositionNumber=${dispositionNumber}`, { observe: 'response' });
  }
  fetchCountryAndState(countryId:number,stateId:number):Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/rga/v1/fetchCountryAndState?countryId=${countryId}&stateId=${stateId}`, { observe: 'response' });
  }


  updateSellingCompanyAndReasoCode(payLoad:any):Observable<HttpResponse<any>>{
    return this.http.put<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/claimdetails/v1/updateSellingCompanyAndReasoCode`,payLoad , { observe: 'response' });
  }



  getClaimStatus(): Observable<HttpResponse<any>> {
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/searchclaimhistory/v1/getClaimStatus`, { observe: 'response' });
  }

  getSearchClaimHistory(searchPayload: any): Observable<HttpResponse<any>>{
    
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/searchclaimhistory/v1/searchClaimHistory`, searchPayload ,{ observe: 'response' });
  }

  getPrimarySearch(): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/searchclaimhistory/v1/getPrimarySearch`, { observe: 'response' });
  }

getSecondarySearch(primaryCode:any):Observable<HttpResponse<any>>{
  return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl_2}/initiation/searchclaimhistory/v1/getSecondarySearch?primarySearchType=${primaryCode}`, { observe: 'response' });

}

exportToExcel(searchClaimHistoryRequest: any): Observable<HttpResponse<Blob>> {
  return this.http.post(`${this.modifiedApiUrl_2}initiation/searchclaimhistory/v1/exportToExcel`, searchClaimHistoryRequest, {
    observe: 'response',
    responseType: 'blob' as 'json'  // Important: this tells TypeScript that the response is of type Blob
  }) as Observable<HttpResponse<Blob>>; // Explicitly cast the result to Observable<HttpResponse<Blob>>
}
declineCreditMemo(claimRgaHeaderId:number,userId:number):Observable<HttpResponse<any>>{
    return this.http.put<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/crm/v1/declineCreditMemo?claimCrmHeaderId=${claimRgaHeaderId}&userId=${userId}`, { observe: 'response' });
}

getAllowanceToKeepData(payload:any):Observable<HttpResponse<any>>{
  return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl_2}initiation/crm/v1/getAllowanceToKeepData`, payload, { observe: 'response' });
}
}