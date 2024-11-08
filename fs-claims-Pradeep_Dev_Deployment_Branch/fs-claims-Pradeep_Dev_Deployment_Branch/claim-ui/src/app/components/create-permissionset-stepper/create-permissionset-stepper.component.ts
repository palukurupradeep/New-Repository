//import { Component, OnInit } from '@angular/core';
import { RolesService } from 'src/app/ngrx/store/services/roles.service';
import {
  Component,
  ElementRef,
  OnInit,
  ViewChild,
  AfterViewInit,
  VERSION,
} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ThemePalette } from '@angular/material/core';
import { Observable, debounceTime, fromEvent } from 'rxjs';
import { ClaimCategoryDetails } from 'src/app/interfaces/customer.model';
import {
  PermissionResponse,
  RoleApiResponse,
  RolePermissionPayload,
  RoleResponse,
  permissionSetObj,
} from 'src/app/interfaces/roles-permission.model';
// import { userMappingFindAllusersDetails } from 'src/app/interfaces/user-mapping.model';

import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';
import { GenericValidator } from 'src/app/utils/generic-validator';
import { MatStepper } from '@angular/material/stepper';
// import { RolesMatrixTableComponent } from '../../roles-matrix-table/roles-matrix-table.component';
// import { MatrixTableComponent } from '../../matrix-table/matrix-table.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatrixTableComponent } from '../matrix-table/matrix-table.component';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-create-permissionset-stepper',
  templateUrl: './create-permissionset-stepper.component.html',
  styleUrls: ['./create-permissionset-stepper.component.scss'],
})
export class CreatePermissionsetStepperComponent implements OnInit {
  roles = []; // Replace with your actual roles data
  permissions: any = []; // Replace with your actual permissions data
  rolePermissionMatrix: any[] = [];
  rolesList: any = [];
  roleCode: any;
  roleDescription: any;
  permissionCode: any;
  permissionDescription: any;
  expandedPermissions: { [key: number]: boolean } = {};
  permissionDropdownOpen: boolean = false;
  activePermissionIndex!: number;
  //selectedPermissions: { [key: string]: boolean } = {};
  public isSideNavCollapsed: boolean = true;
  public collapsed = false;
  public screenWidth = 0;
  applicationObjects: any = [];
  public allUsersData: any = [];
  
  filteredPermissions: any[] = [];
 
  searchValue = '';
  searchPermission: string = '';
  // In your component
  showEditSection: boolean = false;
  selectedPermissions: any[] = [];
  editingPermission: any[] = [];
  selectedRole: any;
  filteredRolesList: any[] = [];
  roleCodeFilter: string = '';
  permissionCodeFilter: string = '';
  //from here
  public allUsersItemsPerPage: number = 50; // Default items per page
  public userItemsPerPage: number = 50; // Default items per page
  public allPermissionsCurrentPage: number = 1;
  public userCurrentPage: number = 1;
  public originalUsersData: any[] = [];
  public roleId: number = 1;
  selectedPermissionId: number = -1;
  selectedCheckboxPermissionId: number = -1; // Initialize to an initial value
  selectedCheckboxRoleId: number = -1; // Initialize to an initial value
  selectedCheckboxPermission: boolean = false;
  searchText = {
    
    permissionDescription: '',
    objectDescription: '',
  };
  itemsPerPage: number = 50;
  totalItems: number = 0;
  modalPermissions: any[] = [];
  loading = true;
  public permissionDescriptionName: string = '';
  public perMange: boolean = false;
  public permissionSet: boolean = false;
  currentView: string = 'table';
  backBtn: boolean = false;
  pageSize: number = 5;
  currentPage: number = 0;
  permissionCodeSearch: string = '';
  permissionDescriptionSearch: string = '';
  originalPermissions: any[] = [];
  editAndView: boolean = false;
  // In your component.ts file
  functionLabel: string = 'Function';
  actionLabel: string = 'Action';
  selectedObjects: any[] = [];
  selectedFunctions: any[] = [];
  selectedActions: any[] = [];
  perPageOptions = [50, 100, 250, 500];
  deletepermission: any;
  constructor(
    private rolesService: RolesService,
    private bodyClassService: BodyClassService,
    private toasterService: ToasterService
  ) {
    // Initialize the matrix based on your data
  }

  ngOnInit() {
    this.getRoles();
    this.getApplicationObjects();
    
    
    this.filteredPermissions = this.permissions;
   
  }

  toggleDropdown() {
    this.permissionDropdownOpen = !this.permissionDropdownOpen;
  }

  

  public CloseRoleModal() {
    const modelDiv = document.getElementById('rolemodal');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }
  public openRoleModal() {
    console.log('role called');
    const modelDiv = document.getElementById('rolemodal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }

  

  getApplicationPermissions() {
    this.rolesService.getApplicationPermissions().subscribe((data) => {
      // this.permissions = permissionListData;
      this.permissions = data;
      this.filteredPermissions = this.permissions;
      this.originalPermissions = this.permissions;
      this.loading = false;
      console.log(data, 'permission');
    });
  }

  getApplicationObjects() {
    this.rolesService.getApplicationObjects().subscribe((data) => {
      // this.permissions = permissionListData;
      //this.permissions = data;
      this.applicationObjects = data;
      console.log(data, 'application objects');
    });
  }

  getRoles() {
    this.rolesService.getRoles().subscribe(
      (roles: any) => {
        this.rolesList = roles;
        // this.rolesList = rolesListData; // Store the response in rolesList
        this.getApplicationPermissions();
        this.loading = false;
        console.log('Roles:', this.rolesList);
      },
      (error: any) => {
        console.error('Error fetching roles:', error);
      }
    );
  }

  public getBodyClass(): string {
    return this.bodyClassService.getBodyClass(
      this.isSideNavCollapsed,
      this.screenWidth
    );
  }

  public onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  public openPermissionModal() {
    this.perMange = true;
  
  }
  public openPermissionSetModal() {
    this.permissionSet = true;
   
  }

  public ClosePermissionModal() {
    const modelDiv = document.getElementById('permissionmodal');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }
  // onSearch() {

  //   const searchTextPermission = this.searchText.permissionDescription.toLowerCase();
  //   const searchTextObjectDescription = this.searchText.objectDescription.toLowerCase();
  
  //   if (searchTextPermission === '' && searchTextObjectDescription === '') {
  //     // If both search inputs are empty, reset the data to its original state
  //     this.resetData();
  //   } else {
  //     const matchedPermissions = this.permissions.filter((p: any) => {
  //       // Check if permissions is defined and not null
  //       const permissionDescriptionMatch = p.permissionDescription.toLowerCase().includes(searchTextPermission);
  
  //       // Check if applicationObjects is defined and not null
  //       const objectDescriptionMatch = p.applicationObjects.some((object: any) => {
  //         return object.objectDescription.toLowerCase().includes(searchTextObjectDescription);
  //       });
  
  //       return permissionDescriptionMatch && objectDescriptionMatch;
  //     });
  
  //     if (matchedPermissions.length === 0) {
  //       // If no data matches the search, display a "No data available" message
  //       this.permissions = [];
  //     } else {
  //       // Log the matched items to the console
  //       console.log('Matched Items:', matchedPermissions);
  
  //       // Update the permissions to contain the matchedPermissions
  //       this.permissions = matchedPermissions;
  //     }
  //   }
  // }
  // onSearch() {
  //   const searchTextPermission: string = this.searchText.permissionDescription.toLowerCase();
  //   const searchTextObjectDescription: string = this.searchText.objectDescription.toLowerCase();
  
  //   if (searchTextPermission === '' && searchTextObjectDescription === '') {
  //     this.resetData();
  //   } else {
  //     const matchedPermissions = this.permissions.filter((p: any) => {
  //       const permissionDescriptionMatch = p.permissionDescription
  //         ?.toLowerCase()
  //         .includes(searchTextPermission);
  
  //         const objectDescriptionMatch = searchTextObjectDescription ?  p.applicationObjects?.some((object: any) => {
  //           const objectMatch = object.objectDescription
  //             ?.toLowerCase()
  //             .includes(searchTextObjectDescription);
  //           return objectMatch ;
  //         }) || false : false;
    
  //         return permissionDescriptionMatch || objectDescriptionMatch;
  //     });
  
  //     if (matchedPermissions.length === 0) {
  //       this.permissions = [];
  //     } else {
  //       this.permissions = matchedPermissions;
  //     }
  //   }
  // }


  onSearch(): void {
    const searchTextPermission: string = this.searchText.permissionDescription?.toLowerCase() || '';
    const searchTextObjectDescription: string = this.searchText.objectDescription?.toLowerCase() || '';
  
    // If both search fields are empty, reset the permissions list
    if (!searchTextPermission && !searchTextObjectDescription) {
      this.permissions = [...this.originalPermissions]; // Reset to the original list
      return;
    }
  
    // Filter the permissions based on the search inputs
    const matchedPermissions = this.originalPermissions.filter((p: any) => {
      const permissionDescriptionMatch = searchTextPermission
        ? p.permissionDescription?.toLowerCase().includes(searchTextPermission)
        : false;
  
      const objectDescriptionMatch = searchTextObjectDescription
        ? p.applicationObjects?.some((object: any) => object.objectDescription
            ?.toLowerCase()
            .includes(searchTextObjectDescription)) || false
        : false;
  
      return permissionDescriptionMatch || objectDescriptionMatch;
    });
  
    this.permissions = matchedPermissions.length > 0 ? matchedPermissions : [];
  }
  
  
  
  
  
  
  
  

  

  
  
  
  
  
 
  
  


  
  
 
  
  
  

  
  
  


  

  
 
  
  
  
  // Create a function to reset the data to its original state
  resetData() {
    // Fetch the actual data (replace this with your actual data retrieval method)
    this.getApplicationPermissions();
    this.permissions = this.filteredPermissions;
    
  }

  toggleCheckbox(index: number) {
    // Toggle the selected state of the permission
    this.permissions[index].selected = !this.permissions[index].selected;
  }
  
  editPermissions(permissionSets: any[], permission: any): void {
    this.selectedPermissionId = permission.permissionId
    console.log(this.selectedPermissionId, 'this.selectedPermissionId');
    this.permissionDescriptionName = permission.permissionDescription;
    console.log(permissionSets, 'permissionSets');
    console.log(this.applicationObjects, 'this.applicationObjects');
    this.editAndView = true;

    this.modalPermissions = this.applicationObjects.map((obj: any) => {
      const matchingSet = permissionSets.find(
        (set) => set.objectId === obj.objectId
      );

      // Log matching objectId
      if (matchingSet) {
        console.log('Matching objectId:', obj.objectId);
      }

      if (matchingSet) {
        obj.objectSelected = true;

        // Check for matching functionId in applicationFunctions
        if (matchingSet.applicationFunctions) {
          console.log('inside matchingSet:');
          obj.applicationFunctions.forEach((func: any) => {
            const matchingFunc = matchingSet.applicationFunctions.find(
              (setFunc: any) => setFunc.functionId === func.functionId
            );

            // Log matching functionId
            if (matchingFunc) {
              console.log('Matching functionId:', func.functionId);
            }

            func.selected = !!matchingFunc;
          });
        }

        // Check for matching actionId in applicationActions
        if (matchingSet.applicationActions) {
          console.log('inside matchingSet:');
          obj.applicationActions.forEach((action: any) => {
            const matchingAction = matchingSet.applicationActions.find(
              (setAction: any) => setAction.actionId === action.actionId
            );

            // Log matching actionId
            if (matchingAction) {
              console.log('Matching actionId:', action.actionId);
            }

            action.selected = !!matchingAction;
          });
        }
      }

      return obj;
    });

    this.currentView = 'editModel';

    const modelDiv = document.getElementById('editModel');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
    this.backBtn = true;
  }

  onDeleteRole(role: any): void {
    this.rolesService.deleteRoleById(role.roleId).subscribe((data) => {
      this.getRoles();
    });
  }
  public CloseEditModal() {
    const modelDiv = document.getElementById('editModel');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }
 
  confirmAction() {
      
    
    const payload: permissionSetObj = {
      permissionId: this.selectedPermissionId,
      objects: this.selectedObjects.map(objectId => ({
        objectId: objectId,
        functionIds: Array.isArray(this.selectedFunctions) ? this.selectedFunctions : [],
        actionIds: Array.isArray(this.selectedActions) ? this.selectedActions : []
      }))
    };
    
    
    
    console.log(payload,'payload')
    
    
    this.rolesService.createPermissionSet(payload).subscribe(
      data => {
        this.toasterService.showToast('success', 'PermissionSet added to Permission Successfully');
     
    
        // Call additional methods here
       
        this.goBack();
        this.getApplicationPermissions();
        this.applicationObjects();
      },
      error => {
        // Handle error if necessary
        console.error('Error creating permission set:', error);
        // Optionally, you can show an error message to the user.
      }
    );
    
    
    
    
  
   
  }
  

  onPreviousPageClick() {
    if (this.allPermissionsCurrentPage > 1) {
      this.allPermissionsCurrentPage--;
    }
  }

  onNextPageClick() {
    const maxPage = Math.ceil(this.permissions.length / this.itemsPerPage);
    if (this.allPermissionsCurrentPage < maxPage) {
      this.allPermissionsCurrentPage++;
    }
  }

  onPageNumberClick(page: number) {
    this.allPermissionsCurrentPage = page;
  }

  onItemsPerPageChange() {
    this.allPermissionsCurrentPage = 1;
  }

  getPageNumbers(): number[] {
    const maxPage = Math.ceil(this.permissions.length / this.itemsPerPage);
    return Array.from({ length: maxPage }, (_, i) => i + 1);
  }
  

  viewMore(permissionSets: any, permission: any): void {
    this.editAndView = true;
    this.permissionDescriptionName = permission.permissionDescription;
    this.modalPermissions = permissionSets;
    // Implement the logic to handle the "View More" click
    // For example, you can display a modal or expand the row to show all permissionSet.object.objectDescription
    console.log('View More clicked!', this.modalPermissions);

    this.currentView = 'viewModel';
    const modelDiv = document.getElementById('viewModel');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
    this.backBtn = true;
  }

  public CloseviewRoleModal() {
    const modelDiv = document.getElementById('viewModel');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  switchToPermissions(): void {
    this.backBtn = true;
    this.currentView = 'permissions';
  }

  switchToPermissionSet(): void {
    this.backBtn = true;
    this.currentView = 'permissionSet';
  }

  switchToTable(): void {
    console.log('called function');
    this.backBtn = false;
    this.currentView = 'table';
  }
  goBack() {
    this.currentView = 'table';
    this.backBtn = false;
    this.editAndView = false;
  }
  get totalPages(): number {
    return Math.ceil(this.permissions.length / this.pageSize);
  }

  // get paginatedPermissions(): any[] {
  //   const startIndex = this.currentPage * this.pageSize;
  //   const endIndex = startIndex + this.pageSize;
  //   return this.permissions.slice(startIndex, endIndex);
  // }

  prevPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
    }
  }

  nextPage() {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
    }
  }

  
  getObjectDescription(permissionId: number): string | undefined {
    const permission = this.permissions.find(
      (p: { permissionId: number }) => p.permissionId === permissionId
    );

    if (
      permission &&
      permission.permissionSets &&
      permission.permissionSets.length > 0
    ) {
      const firstPermissionSet = permission.permissionSets[0];
      return firstPermissionSet.object.objectDescription;
    }

    return undefined;
  }
  togglePermission(permission: any): void {
    permission.expanded = !permission.expanded;
  }

  toggleFunctions(permission: any): void {
    permission.showFunctions = !permission.showFunctions;
  }

  toggleActions(permission: any): void {
    permission.showActions = !permission.showActions;
  }

  getPermissionApplicationObject(permissionId: number): any[] {
    const permission = this.permissions.find(
      (p: any) => p.permissionId === permissionId
    );
    return permission ? permission.applicationObjects : [];
  }
  

onObjectSelected(permission: any): void {
  console.log('Object Selected - objectId:', permission.objectId);
  if (permission.objectSelected) {
    this.selectedObjects.push(permission.objectId);
  } else {
    this.selectedObjects = this.selectedObjects.filter(id => id !== permission.objectId);
  }
}

onFunctionSelected(permission: any, func: any): void {
  console.log('Function Selected - objectId:', permission.objectId, 'functionId:', func.functionId);
  if (func.selected) {
    // Add functionId to the selectedFunctions array
    this.selectedFunctions.push(func.functionId);
  } else {
    // Remove item with matching objectId and functionId from the selectedFunctions array
    this.selectedFunctions = this.selectedFunctions.filter(item =>
      !(item.objectId === permission.objectId && item.functionId === func.functionId)
    );
  }
  
}

onActionSelected(permission: any, action: any): void {
  console.log('called');
  console.log('Action Selected - objectId:', permission.objectId, 'actionId:', action.actionId);
  if (action.selected) {
    this.selectedActions.push(action.actionId );
  } else {
    this.selectedActions = this.selectedActions.filter(item =>
      !(item.objectId === permission.objectId && item.actionId === action.actionId)
    );
  }
}
onDeletePermission(permission: any): void {

  this.deletepermission = permission;

  const modelDiv = document.getElementById('deleteModal');
  if (modelDiv != null) {
    modelDiv.style.display = 'block';
  }
    
 
}
cancelDeletePermissionModel(){
  const modelDiv = document.getElementById('deleteModal');
  if (modelDiv != null) {
    modelDiv.style.display = 'none';
  }
}

  confirmDeletePermissionModel(){
  console.log('role delte confirmation');
   this.rolesService.deletePermissionById(this.deletepermission.permissionId).subscribe((data) => {
  
   
   
    if(data.status == 200){
      this.toasterService.showToast('success', 'Permission Deleted Successfully');
      this.getApplicationPermissions();
    }
   
      },
        (error) => {
    if(error.status == 400){
      this.toasterService.showToast('error', 'This permission is associated with role. Remove the permission-association from roles first');
    }
  });
this.cancelDeletePermissionModel();
}
}
