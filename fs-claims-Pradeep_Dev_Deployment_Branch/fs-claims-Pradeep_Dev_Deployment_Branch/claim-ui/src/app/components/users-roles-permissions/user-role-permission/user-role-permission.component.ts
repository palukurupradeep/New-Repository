import { Component } from '@angular/core';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';



interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-user-role-permission',
  templateUrl: './user-role-permission.component.html',
  styleUrls: ['./user-role-permission.component.scss']
})
export class UserRolePermissionComponent {

  public userModel: boolean = false;
  public isSideNavCollapsed: boolean = true;
  public collapsed = false;
  public screenWidth = 0;
  public permissionModel: boolean = false;
  public rolesModel: boolean = false;
  public userMange:boolean = false;
  public roleMange:boolean = false;
  public perMange:boolean = false;
  public emptyUserData: boolean = false;
  public userData: any = [];
  public columnSortState: { [key: string]: 'asc' | 'desc' } = {};
  public allUsersData: any = [];
  public originalUsersData: any[] = [];
  public columnUserFilterSort: { [key: string]: 'asc' | 'desc' } = {};
  public userCurrentPage: number = 1;
  public userItemsPerPage: number = 5;
  public setUserData: any;
  public userRowselected: boolean = false;
  public allUsersCurrentPage: number = 1;
  public allUsersItemsPerPage: number = 5;
  constructor(
    private bodyClassService: BodyClassService,
    private userMappingService: UserMappingService
   
  ) {}

  ngOnInit(): void {
    this.changeTab('user')
  }



   // on change tabs
  changeTab(value: string){
    if(value == 'user'){
      this.userModel = true;
      this.permissionModel = false;
      this.rolesModel = false;
      this.userMange = false;
      this.roleMange = false;
      this.perMange = false;
    } else if(value == 'role'){
      this.userModel = false;
      this.permissionModel = false;
      this.rolesModel = true;
      this.userMange = false;
      this.roleMange = false;
      this.perMange = false;
    } else if(value == 'permission'){
      this.userModel = false;
      this.permissionModel = true;
      this.rolesModel = false;
      this.userMange = false;
      this.roleMange = false;
      this.perMange = false;
    } else if(value == 'userMange'){
      this.userModel = false;
      this.permissionModel = false;
      this.rolesModel = false;
      this.userMange = true;
      this.roleMange = false;
      this.perMange = false;
    } else if(value == 'roleMange'){
      this.userModel = false;
      this.permissionModel = false;
      this.rolesModel = false;
      this.userMange = false;
      this.roleMange = true;
      this.perMange = false;
    } else if(value == 'permissionMange'){
      this.userModel = false;
      this.permissionModel = false;
      this.rolesModel = false;
      this.userMange = false;
      this.roleMange = false;
      this.perMange = true;
    }
  }

  public onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }
  public getBodyClass(): string {
    return this.bodyClassService.getBodyClass(
      this.isSideNavCollapsed,
      this.screenWidth
    );
  }
  
     
  }

 

  




