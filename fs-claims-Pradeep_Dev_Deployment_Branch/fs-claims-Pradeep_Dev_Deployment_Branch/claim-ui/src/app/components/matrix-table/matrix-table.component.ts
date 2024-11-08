import { Component } from '@angular/core';
import {
  PermissionResponse,
  RoleApiResponse,
  RolePermissionPayload,
  RoleResponse,
} from 'src/app/interfaces/roles-permission.model';
import { RolesService } from 'src/app/ngrx/store/services/roles.service';
import { rolesListData } from './mock-roles.data';
import { permissionListData } from './mock-columns.data';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';


interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-matrix-table',
  templateUrl: './matrix-table.component.html',
  styleUrls: ['./matrix-table.component.scss'],
})
export class MatrixTableComponent {
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
  filteredRoles: any[]=[];
  filteredPermissions: any[]=[];
  searchRole: string = '';
  searchValue = '';
  searchPermission: string = '';
// In your component
showEditSection: boolean = false;
selectedPermissions: any[] = [];
editingPermission: any[] = [];
selectedRole: any;
filteredRolesList: any[]=[];
roleCodeFilter: string = ''; 
permissionCodeFilter: string = '';
public childNavValue: boolean = true;
//from here
public allUsersItemsPerPage: number = 50; // Default items per page
public userItemsPerPage: number = 50; // Default items per page
public allUsersCurrentPage: number = 1;
public userCurrentPage: number = 1;
public originalUsersData: any[] = [];
public roleId:number = 1;
selectedPermissionId: number = -1;
selectedCheckboxPermissionId: number = -1; // Initialize to an initial value
selectedCheckboxRoleId: number = -1; // Initialize to an initial value
selectedCheckboxPermission: boolean = false;
searchText = {
  roleDescription: '',
  permissionDescription: '',
  permissionCode:''
};
itemsPerPage: number = 50;
  totalItems: number = 0;
  modalPermissions: any[]=[];
  loading = true; 
  public roleName: string = '';
  public perMange:boolean = false;
  public permissionSet: boolean = false;
  currentView: string = 'table';
  backBtn: boolean = false;
  pageSize: number = 5;
  currentPage: number = 0;
  permissionCodeSearch: string = '';
  permissionDescriptionSearch: string = '';
  originalPermissions: any[]=[];
  editAndView: boolean = false;
  perPageOptions = [50, 100, 250, 500];
  deleteUser: any;
  orginalRoles: any;
  uncheCkedRoleId: number;
  unCheckedPermissionId: any;
  constructor(
    private rolesService: RolesService,
    private bodyClassService: BodyClassService,
    private userMappingService: UserMappingService,
    private toasterService: ToasterService
  ) {
    // Initialize the matrix based on your data
  }

  ngOnInit() {
    this.getRoles();
    this.getApplicationObjects();
    this.geAllUsersData();
    this.filteredRoles = this.rolesList;
    this.filteredPermissions = this.permissions;
    this.filteredRolesList = [...this.rolesList];
  }

  toggleDropdown() {
    this.permissionDropdownOpen = !this.permissionDropdownOpen;
  }

  deletePermission() {
    // Add logic to delete permission for the selected permissionIndex
    console.log('Deleting permission for permissionIndex:');
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

  addRole() {
    console.log('role is called');
    const roleData = {
      roleCode: this.roleCode,
      roleDescription: this.roleDescription,
    };

    this.rolesService.addRole(roleData).subscribe(
      (response: RoleApiResponse) => {
        console.log('Role added:', response);
        // Call getRoles after successful addition
        this.getRoles();
                this.CloseRoleModal();
      },
      (error: any) => {
        console.error('Error adding role:', error);
      }
    );
  }

  getApplicationPermissions() {
    this.rolesService.getApplicationPermissions().subscribe((data) => {
      // this.permissions = permissionListData;
      this.permissions = data;
      this.originalPermissions =this.permissions;
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
         this.orginalRoles =this.rolesList ; // Store the response in rolesList
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
  addPermission() {
    console.log('role is called');
    const permissionData = {
      permissionCode: this.permissionCode,
      permissionDescription: this.permissionDescription,
    };

    this.rolesService.addPermission(permissionData).subscribe(
      (response: PermissionResponse) => {
        console.log('Role added:', response);
        // Call getRoles after successful addition
        this.getApplicationPermissions();
        this.permissionCode = ''; // Reset permissionCode
        this.permissionDescription = '';
        this.ClosePermissionModal();
      },
      (error: any) => {
        console.error('Error adding role:', error);
      }
    );
  }

 

 

  public geAllUsersData() {
    this.userMappingService.getAllUsers(1, 10).subscribe(
      (data: any) => {
        this.allUsersData = data.content;

        console.log('get all users response------>', this.allUsersData);
      },
      (error) => {
        console.log('fetch user details failed: ', error);
      }
    );
  }
  filterRoles() {
    this.filteredRoles = this.rolesList.filter((role:any) =>
      role.roleCode.toLowerCase().includes(this.searchRole.toLowerCase())
    );
  }

  filterPermissions() {
    this.filteredPermissions = this.permissions.filter((permission:any) =>
      permission.permissionCode.toLowerCase().includes(this.searchPermission.toLowerCase())
    );
  }
  roleHasPermissions(role: any): boolean {
    return this.permissions.some((permission:any) => this.roleHasPermission(role, permission.permissionCode));
  }

  roleHasPermission(role: any, permissionCode: string): boolean {
    return role.permissions.includes(permissionCode);
  }

  
 
  

 

  toggleShowPermissions(role: any) {
    role.showPermissions = !role.showPermissions;
  }
  toggleEditMode(role:any) {
    role.editMode = !role.editMode;
    if (role.editMode) {
      role.showPermissions = true;
    }
  }

  
  isPermissionAssociated(role:any, permission:any) {
    console.log('Role Permissions:', role.permissions);
    console.log('Checking Permission:', permission);
  
    const associated = role.permissions.some((p:any) => p.permissionCode === permission.permissionCode);
    console.log('Is Associated:', associated);
  
    return associated;
  }
  
 

  isPermissionSelected(permissionCode:any, rolePermissions:any) {
    return rolePermissions.some((permission:any) => permission.permissionCode === permissionCode);
  }
  applyFilter() {
    const filterValue = this.searchValue.trim().toLowerCase();
    // Apply the filter to the rolesList data
    // Update this filtering logic based on your data structure
    this.rolesList = this.rolesList.filter((role:any) => {
      return role.roleCode.toLowerCase().includes(filterValue) ||
        role.permissions.some((permission:any) => permission.permissionCode.toLowerCase().includes(filterValue));
    });
  }
 


  
 

onPermissionChange(permissionId: number, roleId: number, isChecked: boolean): void {
  const selectedPermission = this.permissions.find((p: any) => p.permissionId === permissionId);

  if (selectedPermission) {
    if (isChecked) {
      selectedPermission.selected = true;
      // Perform actions when a permission is selected
      console.log('Permission selected - Permission ID:', permissionId, 'Role ID:', roleId);
      this.selectedCheckboxRoleId = roleId;
      // Store the selected permission
      this.selectedPermissions.push({ permissionId, roleId });

    } else {
      selectedPermission.selected = false;
      // Perform actions when a permission is deselected
      console.log('Permission deselected - Permission ID:', permissionId, 'Role ID:', roleId);
      this.uncheCkedRoleId= roleId;
      this.unCheckedPermissionId = permissionId;
this.openDeletePermissionsToRoles();
      // Remove the deselected permission from the stored array
      this.selectedPermissions = this.selectedPermissions.filter(
        (p: any) => !(p.permissionId === permissionId && p.roleId === roleId)
      );
    }

    

   
  }
}

canceldeletePermissionsToRolesModel(){
  const modelDiv = document.getElementById('permissionsToRoles');
  if (modelDiv != null) {
    modelDiv.style.display = 'none';
  }
}
openDeletePermissionsToRoles(){
  const modelDiv = document.getElementById('permissionsToRoles');
  if (modelDiv != null) {
    modelDiv.style.display = 'block';
  }

}

deletePermissionsToRoles(){
  const payload: RolePermissionPayload = {
    roleId: this.uncheCkedRoleId,
    permissionIdData: [this.unCheckedPermissionId],
    active: true
  };
  this.rolesService.deletePermissionsToRoles(payload).subscribe((data)=>{
    if(data.status == 200){
      this.canceldeletePermissionsToRolesModel();
      this.getRoles();
      this.getApplicationPermissions();
     this.switchToTable();
      this.toasterService.showToast('success', 'Permission disassociated From  Role Successfully');
       
    }
  })
}
  

  onSearch() {
    const searchTextRole = this.searchText.roleDescription.toLowerCase();
    const searchTextPermission = this.searchText.permissionDescription.toLowerCase();
      
    if (searchTextRole === '' && searchTextPermission === '') {
      console.log('inside search to reset');
      // If both search inputs are empty, reset the data to its original state
      this.resetData();
    } else {
      const filteredRoles = this.rolesList.filter((role: any) => {
        const roleDescription = role.roleDescription.toLowerCase();
        const permissionDescription = role.permissions.map((p: any) => p.permissionDescription.toLowerCase()).join(' ');
        
        return (
          roleDescription.includes(searchTextRole) &&
          permissionDescription.includes(searchTextPermission)
        );
      });
  
      if (filteredRoles.length === 0) {
        // If no data matches the search, display a "No data available" message
        this.rolesList = []; // Clear the current data
      } else {
        // Update the rolesList to contain the filtered roles
        this.rolesList = filteredRoles;
      }
    }
  }
  
  // Create a function to reset the data to its original state
  resetData() {
    // Fetch the actual data (replace this with your actual data retrieval method)
    //this.getRoles();
    this.permissions = this.originalPermissions;
    //this.editPermissions(role)
    
    //this.getApplicationPermissions();
    this.rolesList = this.orginalRoles
    console.log(this.permissions ,'this.permissions ')
    console.log(this.rolesList ,'this.rolesList ')
  
  }
  
  
  
  

  
  toggleCheckbox(index: number) {
    // Toggle the selected state of the permission
    this.permissions[index].selected = !this.permissions[index].selected;
  }
  editPermissions(role:any) {
  
    this.editAndView=true;
    this.roleId = role.roleId
    this.roleName = role.roleDescription;
    console.log(this.roleId,'roleData');
    // Reset the selected property for all permissions
    this.permissions.forEach((permission :any) => (permission.selected = false));

    // Loop through the role's permissions and mark them as selected
    role.permissions.forEach((rolePermission:any) => {
      const permission = this.permissions.find(
        (permission:any) => permission.permissionDescription === rolePermission.permissionDescription
      );
      if (permission) {
        permission.selected = true;
      }
    });
    this.currentView = 'editModel';
    this.backBtn = true;
  }

  onDeleteRole(role: any): void {
     this.deleteUser = role;
    console.log(role,'roles')
    const modelDiv = document.getElementById('deleteModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
   
    
  }
  confirmDeleteRoleModel(){
    console.log('role delte confirmation');
     this.rolesService.deleteRoleById(this.deleteUser.roleId).subscribe((data) => {
    
     
     
      if(data.status == 200){
        this.toasterService.showToast('success', 'Role Deleted Successfully');
      
        this.getRoles();
      }
     
        },
          (error) => {
      if(error.status == 400){
        this.toasterService.showToast('error', 'This role is associated with user / permission. Remove the role-association from users first');
      
      }
    });
this.cancelDeleteRoleModel();
  }
  cancelDeleteRoleModel(){
    const modelDiv = document.getElementById('deleteModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }
  public CloseEditModal() {
    const modelDiv = document.getElementById('editModel');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }
  confirmAction() {
    console.log("functioncalled")
    const permissionIdData = this.selectedPermissions.map(p => p.permissionId);

    const payload: RolePermissionPayload = {
      roleId: this.selectedCheckboxRoleId,
      permissionIdData: permissionIdData,
      active: true
    };
console.log(payload,'payload');
  
    this.rolesService.maptoRolePermission(payload).subscribe((data) => {
      console.log(data, 'permission');
      this.getRoles();
      this.getApplicationPermissions();
     this.switchToTable();
      if(data.status == 200){
  this.toasterService.showToast('success', 'Mappig created Successfully');
      
}
   
  },
  (error) => {
if(error.status == 400){
  this.toasterService.showToast('success', 'Mapping already exists');
}
}
  );
   
  }

  onPreviousPageClick() {
    if (this.allUsersCurrentPage > 1) {
      this.allUsersCurrentPage--;
    }
  }

  onNextPageClick() {
    const maxPage = Math.ceil(this.rolesList.length / this.itemsPerPage);
    if (this.allUsersCurrentPage < maxPage) {
      this.allUsersCurrentPage++;
    }
  }

  onPageNumberClick(page: number) {
    this.allUsersCurrentPage = page;
  }

  onItemsPerPageChange() {
    this.allUsersCurrentPage = 1;
  }

  getPageNumbers(): number[] {
    const maxPage = Math.ceil(this.rolesList.length / this.itemsPerPage);
    return Array.from({ length: maxPage }, (_, i) => i + 1);
  }
  public viewMore(permission: any[],role:any) {
    this.editAndView=true;
    this.roleName = role.roleDescription;
    console.log('role code:', role);
    console.log('Permission Data:', permission);
    this.modalPermissions = permission;
    console.log(this.modalPermissions,'this.modalPermissions')
    this.backBtn = true;
    this.currentView = 'viewModel';
   
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
    console.log('called function')
    this.backBtn = false;
    this.currentView = 'table';
  }
  goBack(){
    this.currentView = 'table';
    this.backBtn = false;
    this.editAndView = false;
  }
  get totalPages(): number {
    return Math.ceil(this.permissions.length / this.pageSize);
  }

  get paginatedPermissions(): any[] {
    const startIndex = this.currentPage * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    return this.permissions.slice(startIndex, endIndex);
  }

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

  ColumnSearch(): void {
    const searchTextPermission = this.searchText.permissionDescription.toLowerCase();
    const searchTextPermissionCode = this.searchText.permissionCode.toLowerCase();
  
    if (searchTextPermissionCode === '' && searchTextPermission === '') {
      // If both search inputs are empty, reset the data to its original state
      this.resetData();
    } else {
      // Filter permissions based on search terms
      this.permissions = this.permissions.filter((permission: any) => {
        const codeMatch = permission.permissionCode.toLowerCase().includes(searchTextPermissionCode);
        const descriptionMatch = permission.permissionDescription.toLowerCase().includes(searchTextPermission);
        return codeMatch && descriptionMatch;
      });
    }
  }
  
}
