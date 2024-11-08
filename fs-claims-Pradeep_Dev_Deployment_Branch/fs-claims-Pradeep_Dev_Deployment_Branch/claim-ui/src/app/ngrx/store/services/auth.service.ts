import { Injectable } from '@angular/core';
import { PublicClientApplication, AuthError } from '@azure/msal-browser';
import { msalConfig } from './auth-config';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private app: PublicClientApplication;
  private isMsalInitialized: boolean = false;
  private roles: any[] = [];
  private permissions: any[] = [];
  constructor() {
    this.app = new PublicClientApplication(msalConfig);
    this.initializeMsal();
    this.loadRolesAndPermissions();
  }

  private async initializeMsal(): Promise<void> {
    try {
      await this.app.initialize();
      await this.app.handleRedirectPromise();
      this.isMsalInitialized = true; 
    } catch (error) {
      console.error('MSAL initialization error:', error);
    }
  }

  async login(): Promise<void> {
    if (!this.isMsalInitialized) {
      await this.initializeMsal();
    }

    try {
      const loginResponse = await this.app.loginPopup({
        scopes: ['openid', 'profile', 'User.Read'],
      });
      console.log('Login successful', loginResponse);
    } catch (error) {
      if (error instanceof AuthError) {
        console.error('Authentication error:', error.errorMessage);
      } else {
        console.error('Unexpected error during login:', error);
      }
    }
  }

  logout(): void {
    this.app.logout();
  }


  setRolesAndPermissions(roles: any[], permissions: any[]) {
    this.roles = roles;
    this.permissions = permissions;

    // Save to localStorage to persist across reloads
    localStorage.setItem('roles', JSON.stringify(roles));
    localStorage.setItem('permissions', JSON.stringify(permissions));
  }

  private loadRolesAndPermissions() {
    const storedRoles = localStorage.getItem('roles');
    const storedPermissions = localStorage.getItem('permissions');

    if (storedRoles && storedPermissions) {
      this.roles = JSON.parse(storedRoles);
      this.permissions = JSON.parse(storedPermissions);
    }
  }

  getRoles() {
    return this.roles;
  }

  getPermissions() {
    return this.permissions;
  }

  hasPermission(permissionCode: string): boolean {
    return this.permissions.some(permission => permission.permissionCode.trim(' ') === permissionCode);
  }
 
  hasRole(roleCode: string): boolean {
    return this.roles.some(role => role.roleCode === roleCode);
  }
  

  
}