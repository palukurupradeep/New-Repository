import { Component } from '@angular/core';
import {
  PermissionResponse,
  RoleApiResponse,
  RolePermissionPayload,
  RoleResponse,
  RoleUsersPayload,
} from 'src/app/interfaces/roles-permission.model';
import { RolesService } from 'src/app/ngrx/store/services/roles.service';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
import { Sort } from '@angular/material/sort';


interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-roles-matrix-table',
  templateUrl: './roles-matrix-table.component.html',
  styleUrls: ['./roles-matrix-table.component.scss'],
})
export class RolesMatrixTableComponent {
  roles = []; // Replace with your actual roles data
  users: any = []; // Replace with your actual permissions data
  sortusers: any = [];
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
  filteredUsers: any[]=[];
selectedPermissions: any[] = [];
  searchRole: string = '';
  searchValue = '';
  searchPermission: string = '';
  public searchTextError: boolean = false;
  public backBtn: boolean = false;
// In your component
showEditSection: boolean = false;
editingPermission: any[] = [];
selectedRole: any;
filteredRolesList: any[]=[];
roleCodeFilter: string = ''; 
permissionCodeFilter: string = '';

columnUserFilterSort: { [key: string]: 'asc' | 'desc' } = {};
loading = true; 
//from here

public userItemsPerPage: number = 50; // Default items per page
public userCurrentPage: number = 1;
// public searchText: any = {};
public originalUsersData: any[] = [];
  modalusers: any[]=[];
  sortModalUsers : any[]=[]
 public roleName: string= '';
 public perMange:boolean = false;
 
 columnSortState: { [key: string]: 'asc' | 'desc' } = {};
 public roleId:number = 1;
 selectedPermissionId: number = -1;
 selectedCheckboxPermissionId: number = -1; // Initialize to an initial value
 selectedCheckboxRoleId: number = -1;
  public editModal: boolean = false;
  public viewModal: boolean = false;
  public addModal: boolean = false;
  public childNavValue: boolean = true;
  public allUsersCurrentPage: number = 1;
  public allUsersItemsPerPage: number = 50;
  perPageOptions = [50, 100, 250, 500];
  public UsersRecordsData: any = [];
  public allUsertotalPages: any;
  public allUsersPerPageitems: number = 50;
  searchText = {
    roleCode:'',
    roleDescription: '',
    userDescription: '',
    activeDirectoryId:'',
  firstName:'',
  lastName:'',
  emailAddress:''
    
  };
    filteredModalUsers: any[]=[];
  public roleListItemsPerPage: number = 50; // Default items per page
  public roleListCurrentPage: number = 1;
  deleteUser: any;
  OginaldUsers: any[]=[];
  rowRoleData: any;
  orginalTotalPageCount: any;
  oldAllUsersCurrentPage: number = 1;
  deSelctedRoleId!: number;
  deSelectedUserId!: number;
  
  constructor(
    private rolesService: RolesService,
    private bodyClassService: BodyClassService,
    private userMappingService: UserMappingService,
    private toasterservice: ToasterService
    
  ) {
    // Initialize the matrix based on your data
  }

  ngOnInit() {
    this.getRoles();
    //this.getApplicationObjects();
    this.geAllUsersData(1,50);
   // this.filteredRoles = this.rolesList;
   // this.filteredUsers = this.users;
    this.filteredRolesList = [...this.rolesList];
  }

  toggleDropdown() {
    this.permissionDropdownOpen = !this.permissionDropdownOpen;
  }

  deletePermission() {
    // Add logic to delete permission for the selected permissionIndex
    console.log('Deleting permission for permissionIndex:');
  }
openUserModal() {
  this.addModal= true;
  this.editModal = false;
  this.viewModal = false;
  this.backBtn = true;
}
sortData(sort: Sort) {
  const data = this.sortusers.slice(); 

  if (!sort.active || sort.direction === '') {
    this.users = data;
    return;
  }

  this.users = data.sort((a:any, b:any) => {
    const isAsc = sort.direction === 'asc';
    switch (sort.active) {
      case 'activeDirectoryId':
        return this.compare(a.activeDirectoryId, b.activeDirectoryId, isAsc);
      case 'firstName':
        return this.compare(a.firstName, b.firstName, isAsc);
      case 'middleInitial':
        return this.compare(a.middleInitial, b.middleInitial, isAsc);
        case 'lastName':
          return this.compare(a.lastName, b.lastName, isAsc);
      case 'emailAddress':
            return this.compare(a.emailAddress, b.emailAddress, isAsc);
         
      default:
        return 0;
    }
  });
}

sortUserData(sort: Sort) {
  const data = this.modalusers.slice(); // Use productCatalog for sorting

  if (!sort.active || sort.direction === '') {
    this.sortModalUsers = data;
    return;
  }

  this.sortModalUsers = data.sort((a:any, b:any) => {
    const isAsc = sort.direction === 'asc';
    switch (sort.active) {
      case 'activeDirectoryId':
        return this.compare(a.activeDirectoryId, b.activeDirectoryId, isAsc);
      case 'firstName':
        return this.compare(a.firstName, b.firstName, isAsc);
      case 'middleInitial':
        return this.compare(a.middleInitial, b.middleInitial, isAsc);
        case 'lastNmae':
          return this.compare(a.lastName, b.lastName, isAsc);
      case 'emailAddress':
            return this.compare(a.emailAddress, b.emailAddress, isAsc);
         
      default:
        return 0;
    }
  });
}


// Comparison function for sorting
 compare(a: string | number, b: string | number, isAsc: boolean) {
return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}

confirmAction() {
 
  const permissionIdData = this.selectedPermissions.map(p => p.userId);

  const payload: RoleUsersPayload = {
    roleId: this.selectedCheckboxRoleId,
    userIds: permissionIdData
  };
console.log(payload,'payload');

  this.rolesService.mapRoleUser(payload).subscribe((data) => {
    console.log(data, 'permission');
    this.getRoles();
    this.geAllUsersData(1,50);
    if(data.status == 200){

  this.toasterservice.showToast('success', 'Mappig created Successfully');
    
  this.cancelAction();
}
   
  },
  (error) => {
if(error.status == 400){
  this.toasterservice.showToast('error', 'Mapping already exists for  this RoleId');
  
}
}
  );
  console.log( this.selectedPermissions,' this.selectedPermissionsbefore');
  this.selectedPermissions = [];
  console.log( this.selectedPermissions,' this.selectedPermissions');
  this.allUsersCurrentPage = 1;
  this.cancelAction();
}



backToTable() {
  this.searchText.activeDirectoryId = ''
  this.searchText.firstName = ''
  this.searchText.lastName = ''
  this.searchText.emailAddress = ''
  this.searchText.roleCode = ''
  this.searchText.roleDescription = ''
  this.searchText.userDescription = ''
  this.getRoles();
this.geAllUsersData(1,50);
  this.backBtn = false;
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

  // getApplicationPermissions() {
  //   console.log("function got called");
  //   this.rolesService.getallUser().subscribe((data) => {
  //     // this.permissions = permissionListData;
  //     this.users = data.content;
  //     this.filteredUsers = this.users;
  //     console.log(data, 'users');
  //   });
  // }

  // getApplicationObjects() {
  //   this.rolesService.getApplicationObjects().subscribe((data) => {
  //     // this.permissions = permissionListData;
  //     //this.permissions = data;
  //     this.applicationObjects = data;
  //     console.log(data, 'application objects');
  //   });
  // }

  getRoles() {
    this.rolesService.getRoles().subscribe(
      (roles: any) => {
        this.rolesList = roles;
        this.filteredRoles=  this.rolesList;
        // this.rolesList = rolesListData; // Store the response in rolesList
        this.geAllUsersData(1,50);

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
    console.log('open');
    const modelDiv = document.getElementById('permissionmodal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
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
        this.geAllUsersData(1,50);
        this.permissionCode = ''; // Reset permissionCode
        this.permissionDescription = '';
        this.ClosePermissionModal();
      },
      (error: any) => {
        console.error('Error adding role:', error);
      }
    );
  }

 

 

  public geAllUsersData(page: number, pageSize: number) {
    this.loading = true;
    this.userMappingService.getAllUsers(page, pageSize).subscribe(
      (data: any) => {
        this.users = data.content.slice();
        this.sortusers = data.content;
        this.OginaldUsers = this.users;
        this.allUsertotalPages = data.totalPages;
                console.log('get all users response------>', this.allUsersData);
      },
      (error) => {
        console.log('fetch user details failed: ', error);
      } , () => {
        this.loading = false; // Set loading to false when the API request completes (regardless of success or failure)
      }
    );
  }
  filterRoles() {
    this.filteredRoles = this.rolesList.filter((role:any) =>
      role.roleCode.toLowerCase().includes(this.searchRole.toLowerCase())
    );
  }

  filterPermissions() {
    this.filteredPermissions = this.users.filter((user:any) =>
      user.firstName.toLowerCase().includes(this.searchPermission.toLowerCase())
    );
  }
  roleHasUsers(role: any): boolean {
    return this.users.some((user:any) => this.roleHasUser(role, user.firstName));
  }

  roleHasUser(role: any, username: string): boolean {
    return role.users.includes(username);
  }

  
  // toggleEditMode(role:any) {
  //   role.editMode = !role.editMode;
  //   if (role.editMode) {
  //     role.showPermissions = true;
  //   }
  // }
  

 

  toggleShowPermissions(role: any) {
    role.showPermissions = !role.showPermissions;
  }
  toggleEditMode(role:any) {
    role.editMode = !role.editMode;
    if (role.editMode) {
      role.showPermissions = true;
    }
  }

  // isPermissionAssociated(role:any, permission:any) {
  //   // Implement logic to check if the role is associated with the permission
  //   return role.permissions.some((p:any) => p.permissionCode === permission.permissionCode);
  // }
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






onSearchEdit(searchType: string, searchValue: string) {
  // Check if all search inputs are empty
  const searchTextactiveDirectoryId = this.searchText.activeDirectoryId.toLowerCase();
  const searchTextfirstName = this.searchText.firstName.toLowerCase();
  const searchTextlastName = this.searchText.lastName.toLowerCase();
  const searchTextemailAddress = this.searchText.emailAddress.toLowerCase();
  if (
    !this.searchText.activeDirectoryId &&
    !this.searchText.firstName &&
    !this.searchText.lastName &&
    !this.searchText.emailAddress
  ) {
    // Reset the data to the original data
    this.users = this.OginaldUsers;
  }  else {
    const filteredUsers = this.users.filter((user: any) => {
      const activeDirectoryId = user.activeDirectoryId.toLowerCase();
      const firstName = user.firstName.toLowerCase();
      const lastName = user.lastName.toLowerCase();
      const emailAddress = user.emailAddress.toLowerCase();
      
      return (
        activeDirectoryId.includes(searchTextactiveDirectoryId)&&
        firstName.includes(searchTextfirstName)&&
        lastName.includes(searchTextlastName)&&
        emailAddress.includes(searchTextemailAddress)
      );
    });

    if (filteredUsers.length === 0) {
      // If no data matches the search, display a "No data available" message
      this.users = []; // Clear the current data
    } else {
      // Update the rolesList to contain the filtered roles
      this.users = filteredUsers;
    }
  }
  
}
onViewSearch(searchType: string, searchValue: string) {
  console.log('searched log')
  // Check if all search inputs are empty
  const searchTextactiveDirectoryId = this.searchText.activeDirectoryId.toLowerCase();
  const searchTextfirstName = this.searchText.firstName.toLowerCase();
  const searchTextlastName = this.searchText.lastName.toLowerCase();
  const searchTextemailAddress = this.searchText.emailAddress.toLowerCase();
  if (
    !this.searchText.activeDirectoryId &&
    !this.searchText.firstName &&
    !this.searchText.lastName &&
    !this.searchText.emailAddress
  ) {
    // Reset the data to the original data
    this.sortModalUsers = this.filteredModalUsers;
  }  else {
    console.log('searched loginside')
    const filteredModalUsers = this.filteredModalUsers.filter((user: any) => {
      const activeDirectoryId = user.activeDirectoryId.toLowerCase();
      const firstName = user.firstName.toLowerCase();
      const lastName = user.lastName.toLowerCase();
      const emailAddress = user.emailAddress.toLowerCase();
      
      return (
        activeDirectoryId.includes(searchTextactiveDirectoryId)&&
        firstName.includes(searchTextfirstName)&&
        lastName.includes(searchTextlastName)&&
        emailAddress.includes(searchTextemailAddress)
      );
    });

    if (filteredModalUsers.length === 0) {
      // If no data matches the search, display a "No data available" message
      this.sortModalUsers = []; // Clear the current data
      // console.log(this.modalusers,'this.modalusers')
    } else {
      // Update the rolesList to contain the filtered roles
      this.sortModalUsers = filteredModalUsers;
    }
  }
  
}




  onPreviousPageClick(preClick: boolean) {
    if (preClick) {
      if (this.roleListCurrentPage > 1) {
        this.roleListCurrentPage--;
      }
    } else {
      if (this.roleListCurrentPage > 1) {
        this.roleListCurrentPage--;
      }
    }
  }
  onItemsPerPageChange() {
  
      this.roleListCurrentPage = 1; // Reset to the first page when changing items per page
  
  }
  // Function to navigate to the next page
  onNextPageClick(nextClick: boolean) {
    if (nextClick) {
      const maxPage = Math.ceil(
        this.rolesList.length / this.roleListItemsPerPage
      );
      if (this.roleListCurrentPage < maxPage) {
        this.roleListCurrentPage++;
      } else {
        const maxPage = Math.ceil(this.rolesList.length / this.roleListItemsPerPage);
        if (this.roleListCurrentPage < maxPage) {
          this.roleListCurrentPage++;
        }
      }
    }
  }
  getPageNumbers(pageNo: boolean): number[] {
    if (pageNo) {
      const pageCount = Math.ceil(
        this.rolesList.length /  this.roleListItemsPerPage
      );
      return Array.from({ length: pageCount }, (_, index) => index + 1);
    } else {
      const pageCount = Math.ceil(this.rolesList.length / this.roleListItemsPerPage); 
      return Array.from({ length: pageCount }, (_, index) => index + 1);
    }
  }

  // Function to navigate to a specific page
  onPageNumberClick(page: number, pageClick: boolean) {
    if (pageClick) {
      if (page >= 1 && page <= this.getPageNumbers(pageClick).length) {
        this.roleListCurrentPage = page;
      }
    } else {
      if (page >= 1 && page <= this.getPageNumbers(pageClick).length) {
        this.roleListCurrentPage = page;
      }
    }
  }
  // toggleCheckbox(index: number) {
  //   for (let i = 0; i < this.permissions.length; i++) {
  //     if (i === index) {
  //       this.permissions[i].selected = !this.permissions[i].selected;
  //     } else {
  //       this.permissions[i].selected = false;
  //     }
  //   }
  // }
  toggleCheckbox(index: number) {
    // Toggle the selected state of the permission
    this.users[index].selected = !this.users[index].selected;
  }
  editUsers(role:any) {
    console.log(role,'myroles');
    this.rowRoleData= role;
    this.backBtn = true;
    this.editModal = true;
    this.addModal= false;
    this.viewModal = false;
    this.roleId = role.roleId
    this.roleName = role.roleDescription;
    console.log(this.roleId,'roleData');
    // Reset the selected property for all permissions
    this.users.forEach((permission :any) => (permission.selected = false));

    // Loop through the role's permissions and mark them as selected
    role.users.forEach((rolePermission: any) => {
      // Find a user in the 'users' array whose 'activeDirectoryId' matches the 'activeDirectoryId' of the current rolePermission
      const permission = this.users.find(
        (user: any) => user.activeDirectoryId === rolePermission.activeDirectoryId
      );
    
      // If a matching user is found, set its 'selected' property to true
      if (permission) {
        permission.selected = true;
      }
    });
    
  }

  mapRoleToUserBasedObPage(page: number, pageSize: number){
    this.rowRoleData
    
      this.loading = true;
      this.userMappingService.getAllUsers(page, pageSize).subscribe(
        (data: any) => {
          this.users = data.content;
          this.users.forEach((user: any) => {
            const rolePermission = this.rowRoleData.users.find(
              (rp: any) => rp.activeDirectoryId === user.activeDirectoryId
            );
            if (rolePermission) {
              user.selected = true;
            }
          });
          this.allUsertotalPages = data.totalPages;
         
        },
        (error) => {
          console.log('fetch user details failed: ', error);
        } , () => {
          this.loading = false; // Set loading to false when the API request completes (regardless of success or failure)
        }
      );
    

  }

  onCheckboxChange(userId: number, roleId: number, isChecked: boolean): void {
    const selectedPermission = this.users.find((p: any) => p.userId === userId);
  
    if (selectedPermission) {
      if (isChecked) {
        // console.log('checked');
        selectedPermission.selected = true;
        // Perform actions when a permission is selected
        this.selectedCheckboxRoleId = roleId;
        // Store the selected permission
        this.selectedPermissions.push({ userId, roleId });
      } else {
        // console.log('unchecked');
        // console.log('Unchecking roleId:', roleId, 'userId:', userId);
        const modelDiv = document.getElementById('deleteuserRoleModal');
        if (modelDiv != null) {
          modelDiv.style.display = 'block';
        }
        selectedPermission.selected = false;
        // Perform actions when a permission is deselected
  
        // Log roleId and userId when checkbox is unchecked
        
        this.deSelctedRoleId = roleId;
        this.deSelectedUserId = userId;
  
        // Remove the deselected permission from the stored array
        this.selectedPermissions = this.selectedPermissions.filter(
          (p: any) => !(p.userId === userId && p.roleId === roleId)
        );
      }
  
      // Construct the payload with all selected permissions
  
      // Call the rolesService.mapRolePermission with the constructed payload
      // this.rolesService.mapRolePermission(payload).subscribe((data) => {
      //   console.log(data, 'permission');
      //   this.getRoles();
      // });
    }
  }
  


  public viewMore(users: any[],role:any) {
    this.roleName = role.roleDescription;
    this.backBtn = true;
    this.viewModal = true;
    this.editModal= false;
    this.addModal = false;
    console.log('users Data:', users);
    this.modalusers = users;
    this.sortModalUsers = users.slice()
    this.filteredModalUsers = this.modalusers
    console.log(this.modalusers,'this.modalPermissions')
    
    const modelDiv = document.getElementById('viewModel');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }
  public CloseviewRoleModal() {
    this.backToTable();
    // const modelDiv = document.getElementById('viewModel');
    // if (modelDiv != null) {
    //   modelDiv.style.display = 'none';
    // }
  }
  onDeleteRole(role: any): void {
    console.log(role,'roles...')
    this.deleteUser = role;

    const modelDiv = document.getElementById('deleteModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
    

  }
  
  cancelDeleteRoleModel(){
    const modelDiv = document.getElementById('deleteModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }
  cancelDeleteUsersToRolesModel(){
    const modelDiv = document.getElementById('deleteuserRoleModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }
    confirmDeleteRoleModel(){
    console.log('role delte confirmation');
     this.rolesService.deleteRoleById(this.deleteUser.roleId).subscribe((data) => {
    
     
     
      if(data.status == 200){
       
        this.toasterservice.showToast('success', 'Role Deleted Successfully');
  
        this.getRoles();
      }
     
        },
          (error) => {
      if(error.status == 400){
        this.toasterservice.showToast('error', 'This role is associated with user / permission. Remove the role-association from users first');
  
      }
    });
this.cancelDeleteRoleModel();
  }
  deleteUsersToRoles(){
    
    
  
     this.rolesService.deleteUserToRole(this.deSelctedRoleId,this.deSelectedUserId).subscribe((data) => {
    
     
     
      if(data.status == 200){
       
        this.toasterservice.showToast('success', 'Role disassociated From  User Successfully');
  
        this.getRoles();
        this.geAllUsersData(1,50);
        this.cancelAction();
      }
     
        },
          (error) => {
      if(error.status == 400){
        this.toasterservice.showToast('error', 'This role is associated with user / permission. Remove the role-association from users first');
  
      }
    });
this.cancelDeleteUsersToRolesModel();
  }
     

  onUsersPreviousPageClick() {
    if (this.allUsersCurrentPage > 1) {
      this.allUsersCurrentPage--;
    }
    //this.getApplicationPermissions()

    this.mapRoleToUserBasedObPage(this.allUsersCurrentPage, this.allUsersItemsPerPage);
  }

  onUsersNextPageClick() {
    // Assume you have the total number of pages available in a variable totalPages
    if (this.allUsersCurrentPage < this.allUsertotalPages) {
      this.allUsersCurrentPage++;
    }
    // Fetch data for the new page
   this.mapRoleToUserBasedObPage(this.allUsersCurrentPage, this.allUsersItemsPerPage);
  }

  onUsersPageNumberClick(page: number | string) {
    if (typeof page === 'number' && page !== this.allUsersCurrentPage) {
      this.allUsersCurrentPage = page;
      this.mapRoleToUserBasedObPage(this.allUsersCurrentPage, this.allUsersItemsPerPage);
    } else {
      // Handle the case when '...' is clicked (optional)
      console.log('Ellipsis clicked or other handling for non-numeric pages.');
    }
  }

  onUsersPageSizeChange() {
    // Reset current page to 1 when changing the page size
    this.allUsersCurrentPage = 1;

    // Fetch data for the new page size
   this.mapRoleToUserBasedObPage(this.allUsersCurrentPage, this.allUsersItemsPerPage);
  }

  getUsersPageNumbers(): number[] {
    return Array.from(
      { length: this.allUsertotalPages },
      (_, index) => index + 1
    );
  }
  
  displayedPageNumbers(): (number | string)[] {
        const totalPages = this.allUsertotalPages;
    const visiblePages = 5; // Adjust the number of visible pages as needed
    const startPage = Math.max(
      1,
      this.allUsersCurrentPage - Math.floor(visiblePages / 2)
    );
    const endPage = Math.min(totalPages, startPage + visiblePages - 1);
  
    let pages: (number | string)[] = Array.from(
      { length: endPage - startPage + 1 },
      (_, index) => startPage + index
    );
  
    // Add ellipsis and last page number if necessary
    if (endPage < totalPages) {
      pages = [...pages, '...', totalPages];
    }
  
    // Add a link to the first page if the last page is reached
    if (this.allUsersCurrentPage === totalPages && totalPages > visiblePages) {
      pages = [1, '...', ...pages];
    }
  
    return pages;
  }

  
  onRolesAndUsersSearch() {
    const searchTextRoleCode = this.searchText.roleCode.toLowerCase();
    const searchTextRole = this.searchText.roleDescription.toLowerCase();
    const searchTextUser = this.searchText.userDescription.toLowerCase();
  
    if (searchTextRoleCode === '' && searchTextRole === '' && searchTextUser === '') {
      // If all search inputs are empty, reset the data to its original state
      // this.resetData();
      this.rolesList = this.filteredRoles;
      console.log(this.filteredRoles, 'this.rolesList called');
    } else {
      const filteredRoles = this.filteredRoles.filter((role: any) => {
        const roleCode = role.roleCode.toLowerCase();
        const roleDescription = role.roleDescription.toLowerCase();
  
        // Check if the role code and/or role description matches
        const roleMatches = roleCode.includes(searchTextRoleCode) && roleDescription.includes(searchTextRole);
  
        console.log('Role:', role);
        console.log('Role Code:', roleCode);
        console.log('Role Description:', roleDescription);
        console.log('Role Matches:', roleMatches);
  
        return roleMatches;
      });
  
      console.log('Filtered Roles:', filteredRoles);
  
      if (filteredRoles.length === 0) {
        // If no data matches the role search, display a "No data available" message
        this.rolesList = [];
      } else {
        // Update the rolesList to contain the filtered roles
        this.rolesList = filteredRoles;
      }
  
      // Now, let's filter the users within the filtered roles
      if (searchTextUser !== '') {
        this.rolesList = this.rolesList.filter((role: any) => {
          const userMatches = role.users?.some((user: any) =>
            this.isUserMatch(user, searchTextUser)
          );
  
          console.log('User Matches:', userMatches);
  
          return userMatches;
        });
      }
    }
  }
  

  
  
  // Function to check if any user properties match the search criteria
  private isUserMatch(user: any, searchText: string): boolean {
    if (!user) {
      return false;
    }
  
    // Concatenate user properties for search
    const userDescription = `${user.firstName} ${user.middleInitial} ${user.lastName}`.toLowerCase();
  
    // Check if the user description includes the search text
    return userDescription.includes(searchText);
  }
  
  cancelAction() {
    console.log('Cancel button clicked!');
    this.allUsersCurrentPage = 1;
    this.backBtn = false;
   
   
  }
  onSearch(searchType: string, searchValue: string) {
    // Check if all search inputs are empty
    if (
      !this.searchText.activeDirectoryId &&
      !this.searchText.firstName &&
      !this.searchText.lastName &&
      !this.searchText.emailAddress
    ) {
      //console.log('inside log');
     // this.users = this.OginaldUsers;
     // this.allUsertotalPages = this.orginalTotalPageCount;
      this.searchTextError = false;
      this.allUsersCurrentPage = this.oldAllUsersCurrentPage;
      this.mapRoleToUserBasedObPage(this.allUsersCurrentPage, this.allUsersItemsPerPage)
      
    } else {
      // Check if the search input has a minimum of 3 characters
      if (searchValue.length >= 3) {
       
        this.userMappingService.findByUser(searchType, searchValue).subscribe(
          (data: any) => {
            this.users = data;
            console.log(this.users.length, 'this.users.length');
            this.allUsertotalPages = this.users.length;
            this.oldAllUsersCurrentPage =this.allUsersCurrentPage
            this.allUsertotalPages = Math.ceil(this.allUsertotalPages / this.allUsersItemsPerPage);
            this.allUsersCurrentPage = 1;
            this.users.forEach((user: any) => {
              const rolePermission = this.rowRoleData.users.find(
                (rp: any) => rp.activeDirectoryId === user.activeDirectoryId
              );
              if (rolePermission) {
                user.selected = true;
              }
            });
  
            this.searchTextError = false;
            
          },
          (error: any) => {
            console.error('Error fetching data:', this.searchTextError);
            this.searchTextError = true;
            this.users = [];
           
          }
        );
      } else {
        // Search input has less than 3 characters
        console.log('Search input should have a minimum of 3 characters.');
        //this.users = this.OginaldUsers; // Reset to original data

      }
    }
  }
  
  resetSearchedValues(){
    this.searchText = {
      roleCode: '',
      roleDescription: '',
      userDescription: '',
      activeDirectoryId: '',
      firstName: '',
      lastName: '',
      emailAddress: ''
    };
  }
  onKeyPress(event: Event): void {
    // Cast the event to KeyboardEvent
    const keyboardEvent = event as KeyboardEvent;
  
    // Prevent the input of space character (key code 32)
    if (keyboardEvent.keyCode === 32) {
      keyboardEvent.preventDefault();
    }
  }
}
