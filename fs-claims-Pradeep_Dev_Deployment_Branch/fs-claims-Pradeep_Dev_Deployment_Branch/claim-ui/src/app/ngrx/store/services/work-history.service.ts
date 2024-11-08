import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})

export class WorkHistoryService{

    private modifiedApiUrl = environment.apiUrl_2
    constructor(private http: HttpClient) { }
    
    public getWorkHistory(id: number){
        return this.http.get<any>(
         this.modifiedApiUrl + 'initiation/workhistory/v1/getWorkHistory?userId=' + id 
        );
      }


}

