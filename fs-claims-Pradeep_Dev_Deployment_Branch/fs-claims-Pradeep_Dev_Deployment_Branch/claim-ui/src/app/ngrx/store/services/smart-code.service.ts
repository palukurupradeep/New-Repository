import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Resolutionpayload, addReasonDefinitionPayload, updateReasonDefinitionPayload } from 'src/app/interfaces/smart-code.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SmartCodeService {

  private modifiedApiUrl = environment.apiUrl_2
  constructor(private http: HttpClient) { 

  }
 
  fetchResourceMenus(menuName:string): Observable<HttpResponse<any>>{
      return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/menu/v1/fetchResourceMenus?formName=${menuName}`, { observe: 'response' });
    
  }
  deleteResourceMenu(resourceMenuId: number): Observable<HttpResponse<any>>{
    return this.http.delete<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/menu/v1/deleteResourceMenu?resourceMenuId=${resourceMenuId}` ,{ observe: 'response' });
  }

 addResourceMenu(addmenuPayload:any):Observable<HttpResponse<any>> {
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/menu/v1/addResourceMenu`, addmenuPayload ,{ observe: 'response' });
  }

  updateResourceMenu(editAdminPayload:any):Observable<HttpResponse<any>> {
    return this.http.put<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/menu/v1/updateResourceMenu`, editAdminPayload ,{ observe: 'response' });
  }

  addOrupdateClaimResolutions(payload: any): Observable<HttpResponse<any>> {
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/addOrUpdateClaimResolution`, payload ,{ observe: 'response' });
 
  }

  enableordisableMenu(resourcesMenuId: number, statusId: number): Observable<HttpResponse<any>> {
    const url = `${this.modifiedApiUrl}initiation/menu/v1/enableordisableMenu?resourcesMenuId=${resourcesMenuId}&statusId=${statusId}`;
    return this.http.put<HttpResponse<any>>(url, { observe: 'response' });
  }
 
  getClaimPhotos(): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/getClaimPhotos`, { observe: 'response' });
   
  
}
deleteClaimPhotos(photoId: number): Observable<HttpResponse<any>> {
  return this.http.delete<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/deleteClaimPhotos?photoId=${photoId}`, { observe: 'response' });
}
deleteResolutionId(resolutionId: number): Observable<HttpResponse<any>> {
  return this.http.delete<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/deleteClaimResolution?claimResolutionId=${resolutionId}`, { observe: 'response' });
}
  
  getAllClaimResolutions(): Observable<HttpResponse<any>>{ 
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/getAllClaimResolutions`, { observe: 'response' });

  }
 
  createCodeResolution(codeResolutionPayload: Resolutionpayload): Observable<HttpResponse<any>>{
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/createCodeResolution`, codeResolutionPayload ,{ observe: 'response' });
  }


  deleteCodeResolution(codeResolutionId: number): Observable<HttpResponse<any>>{
    return this.http.delete<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/deleteCodeResolution?codeResolutionId=${codeResolutionId}` ,{ observe: 'response' });
  }
 
  getClaimCodeResolution(reasonId:number): Observable<HttpResponse<any>>{ 
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/getClaimCodeResolution?reasonId=${reasonId}`, { observe: 'response' });

  }
 
  getCodeRuleReturnType(): Observable<HttpResponse<any>>{ 
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/getCodeRuleReturnType`, { observe: 'response' });

  }
 
  getCodeRuleType(): Observable<HttpResponse<any>>{ 
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/getCodeRuleType`, { observe: 'response' });

  }
 
  getCodeRuleLevel(): Observable<HttpResponse<any>>{ 
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/getCodeRuleLevel`, { observe: 'response' });

  }

  getCodeRuleCategory(): Observable<HttpResponse<any>>{ 
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/getCodeRuleCategory`, { observe: 'response' });

  }

getClaimCodePhotos(reasonId:number): Observable<HttpResponse<any>>{
  return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/getClaimCodePhotos?reasonId=${reasonId}`, { observe: 'response' });
 

}
deleteCodePhotos(photoId: number): Observable<HttpResponse<any>> {
  return this.http.delete<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/deleteCodePhotos?codePhotoId=${photoId}`, { observe: 'response' });
}
saveorUpdateClaimPhotos(requestPayload:any):Observable<HttpResponse<any>> {
  return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/saveorUpdateClaimPhotos`, requestPayload ,{ observe: 'response' });
}
saveCodePhotos(requestPayload:any):Observable<HttpResponse<any>> {
  return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/saveCodePhotos`, requestPayload ,{ observe: 'response' });
}
getCodeRuleImpactArea(): Observable<HttpResponse<any>>{ 
  return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/getCodeRuleImpactArea`, { observe: 'response' });

}

getCodeRuleActionType(): Observable<HttpResponse<any>>{ 
  return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/getCodeRuleActionType`, { observe: 'response' });

}

getCodeRuleDataType(): Observable<HttpResponse<any>>{ 
  return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/getCodeRuleDataType`, { observe: 'response' });

}

addReasonDefinition(addRequestPayload:addReasonDefinitionPayload):Observable<HttpResponse<any>> {
  return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/addReasonDefinition`, addRequestPayload ,{ observe: 'response' });
}

updateReasonDefinition(updateRequestPayload:updateReasonDefinitionPayload):Observable<HttpResponse<any>> {
  return this.http.put<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/updateReasonDefinition`, updateRequestPayload ,{ observe: 'response' });
}

getCodeRuleByReasonCode(reasonCode: string): Observable<HttpResponse<any>> {
  const url = `${this.modifiedApiUrl}initiation/smartcode/v1/getCodeRuleByReasonCode?reasonCode=${reasonCode}`;
  return this.http.get<HttpResponse<any>>(url, { observe: 'response' });
}
getWorkStatus(): Observable<HttpResponse<any>>{ 
  return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/claim/v1/getWorkStatus`, { observe: 'response' });
}


deleteCodeRuleById(codeRuleId:number): Observable<HttpResponse<any>>{ 
  return this.http.delete<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/deleteCodeRuleById?codeRuleId=${codeRuleId}`, { observe: 'response' });
}


fetchDataListByDataType(dataTypeCode:string): Observable<HttpResponse<any>>{
  return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/fetchDataListByDataType?dataTypeCode=${dataTypeCode}`, { observe: 'response' });
}


saveCodeRules(editAndSavePayload:any):Observable<HttpResponse<any>> {
  return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/saveCodeRules`, editAndSavePayload ,{ observe: 'response' });
}

getLookupTypeByCode(lookupTypeCode:string): Observable<HttpResponse<any>>{ 
  return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/lookup/v1/getLookupTypeByCode?lookupTypeCode=${lookupTypeCode}`, { observe: 'response' });
}


fetchResolutionsAndPhotos(reasonId:number): Observable<HttpResponse<any>>{ 
  return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/smartcode/v1/fetchResolutionsAndPhotos?reasonId=${reasonId}`, { observe: 'response' });
}

}