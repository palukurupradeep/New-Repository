import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service';


@Injectable()
export class AuthGaurd implements  CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const expectedRole = route.data['role'];
    console.log(expectedRole,'CLAIMINIT');
    if (this.authService.hasRole(expectedRole)) {
      return true;
    } else {
     this.router.navigate(['/login']); // redirect if role doesn't match
      return false;
    }
  }
}