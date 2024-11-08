import { Observable, throwError } from 'rxjs';
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, map } from 'rxjs/operators';

@Injectable()
export class HttpServiceHelper {

  constructor(private http: HttpClient) {
  }

  public httpGetRequest(url: string): Observable<any> {
    return this.http.get(url)
      .pipe(
        map(response => {
          return response;
        }),
        catchError(error => throwError(error))
      );
  }
}
