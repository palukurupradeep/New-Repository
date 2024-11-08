import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Observable } from "rxjs";
export class AuthInterceptorService implements HttpInterceptor{
    // intercept(req: HttpRequest<any>, next: HttpHandler) {
    //     let modifiedRequest = req.clone({
            
    //         headers:req.headers.append('auth', 'abc'),
    //       //  params: req.params.append('hi','hello'),
    //     });
    //     //need to set timer for token refresh
    //     console.log('sending request')
    //     return next.handle(modifiedRequest);
    //     //return next.handle(req);
    // } 
    private subscriptionKey = 'c1decd06786045309409e35ffb5dba07';

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
      const clonedRequest = req.clone({
        setHeaders: {
          'Ocp-Apim-Subscription-Key': this.subscriptionKey,
        }
      });
      return next.handle(clonedRequest);
    }
}