import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { saveNoteTemplatePayload } from 'src/app/interfaces/note-tasks.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class NotesTasksService {
  
  private modifiedApiUrl = environment.apiUrl_2
  constructor(private http: HttpClient) { }


  fetchAllNoteTemplates(): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/noteTemplate/v1/fetchAllNoteTemplates`, { observe: 'response' });
  }

 
  fetchNoteType(): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/notesandtasks/v1/fetchNoteType`, { observe: 'response' });
  }

  fetchNoteTemplateByNoteTypeAndNoteGroup(noteTypeId: string, noteGroupId: string): Observable<HttpResponse<any>> {
    const url = `${this.modifiedApiUrl}initiation/noteTemplate/v1/fetchNoteTemplateByNoteTypeAndNoteGroup?noteTypeId=${noteTypeId}&noteGroupId=${noteGroupId}`;
    return this.http.get<HttpResponse<any>>(url, { observe: 'response' });
  }
  saveNoteTemplate(saveRequestPayload:any):Observable<HttpResponse<any>> {
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/noteTemplate/v1/saveNoteTemplate`, saveRequestPayload ,{ observe: 'response' });
  }

  deleteNoteTemplate(noteTemplateId: number): Observable<HttpResponse<any>>{
    return this.http.delete<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/noteTemplate/v1/deleteNoteTemplate?noteTemplateId=${noteTemplateId}` ,{ observe: 'response' });
  }

 
  saveNoteTaskTemplate(RequestPayload:any):Observable<HttpResponse<any>> {
    return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/notesandtasks/v1`, RequestPayload ,{ observe: 'response' });
  }

  fetchTraceType(): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/notesandtasks/v1/fetchTraceType`, { observe: 'response' });
  }


  fetchClaimNotes(claimId:string): Observable<HttpResponse<any>>{
    return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/notesandtasks/v1/fetchClaimNotes?claimId=${claimId}`, { observe: 'response' });
  
}

savePendNoteTaskTemplate(RequestPayload:any):Observable<HttpResponse<any>> {
  return this.http.post<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/notesandtasks/v1/pend`, RequestPayload ,{ observe: 'response' });
}

noteGroup(): Observable<HttpResponse<any>>{
  return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/notesandtasks/v1/noteGroup`, { observe: 'response' });
}

userNamesForClaimNotes(claimId:string): Observable<HttpResponse<any>>{
  return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/notesandtasks/v1/userNamesForClaimNotes?claimId=${claimId}`, { observe: 'response' });
}


fetchNoteTemplateBynoteTypeCode(noteTypeCode: string): Observable<HttpResponse<any>>{
  return this.http.get<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/noteTemplate/v1/fetchNoteTemplateBynoteTypeCode?noteTypeCode=${noteTypeCode}` ,{ observe: 'response' });
}


deleteNotesAndTask(claimNoteId: string): Observable<HttpResponse<any>>{
  return this.http.delete<HttpResponse<any>>(`${this.modifiedApiUrl}initiation/notesandtasks/v1/deleteNotesAndTask?claimNoteId=${claimNoteId}` ,{ observe: 'response' });
}
}
