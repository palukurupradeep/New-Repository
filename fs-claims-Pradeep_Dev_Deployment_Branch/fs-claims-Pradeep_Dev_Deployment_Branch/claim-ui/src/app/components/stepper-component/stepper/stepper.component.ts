import {
  Component,
  ElementRef,
  OnInit,
  ViewChild,
  AfterViewInit,
  VERSION,
  ChangeDetectorRef,
} from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { ThemePalette } from '@angular/material/core';
import { Observable, debounceTime, fromEvent } from 'rxjs';
import { ClaimCategoryDetails } from 'src/app/interfaces/customer.model';
import {
  PermissionResponse,
  RoleResponse,
} from 'src/app/interfaces/roles-permission.model';
import { userMappingFindAllusersDetails } from 'src/app/interfaces/user-mapping.model';
import { RolesService } from 'src/app/ngrx/store/services/roles.service';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';
import { GenericValidator } from 'src/app/utils/generic-validator';
import { MatStepper } from '@angular/material/stepper';
import { RolesMatrixTableComponent } from '../../roles-matrix-table/roles-matrix-table.component';
import { MatrixTableComponent } from '../../matrix-table/matrix-table.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';

@Component({
  selector: 'app-stepper',
  templateUrl: './stepper.component.html',
  styleUrls: ['./stepper.component.scss'],
})
export class StepperComponent implements OnInit {
  public roleId: number = 0;
  public rolesForm: FormGroup;
  public displayMessage: { [key: string]: string } = {};
  public formInputElements!: ElementRef[];
  //private genericValidator!: GenericValidator;
  public permissionsList: PermissionResponse[] = [];
  public usersList: any = [];
  public selectedPermissions: { [key: string]: boolean } = {};
  public expandedPermissions: { [key: number]: boolean } = {};
  public selectedUser: { [key: string]: boolean } = {};
  public expandedUsers: { [key: number]: boolean } = {};
  public selPermList: any = [];
  public selUserList: any = [];
  public roleCode: string = '';
  public roleDescription: string = '';

  public perBtn: boolean = true;
  public userBtn: boolean = true;
  public loading = true;
  public saveForm!: FormGroup;

  public searchText: any = {};
  originalPermissions: any[] = [];
  public allUsersData: any = [];
  public recordsData: any = [];
  public allUsertotalPages: any = [];
  public UsersRecordsData: any = [];
  public allUsersCurrentPage: number = 1;
  public allUsersItemsPerPage: number = 50;
  public allPermissionCurrentPage: number = 1;
  public allPermissionItemsPerPage: number = 50;
  perPageOptions = [50, 100, 250, 500];
  public originalUsersData: any[] = [];
  public columnSortState: { [key: string]: 'asc' | 'desc' } = {};
  public columnUserFilterSort: { [key: string]: 'asc' | 'desc' } = {};
  public selectedUsers: { [key: string]: boolean } = {};
  permissionFlag: boolean = false;
    usersFlag: boolean = false;
      constructor(
    private cdRef: ChangeDetectorRef,
    private form: FormBuilder,
    private rolesService: RolesService,
    private userService: UserMappingService,
    private matrixTableComponent: MatrixTableComponent,
    private toasterservice: ToasterService,
    private userMappingService: UserMappingService
  ) {
    this.rolesForm = this.form.group({
      roleCode: ['', Validators.required],
      roleDescription: ['', Validators.required],
    });
  }

  @ViewChild('stepper') stepper!: MatStepper;

  isLinear = true;
  field1: string = '';
  userData: any[] = [
    {
      name: 'John Doe',
      age: 25,
      dateOfBirth: new Date('1998-05-15'),
      selected: false,
    },
    {
      name: 'Jane Smith',
      age: 30,
      dateOfBirth: new Date('1992-08-22'),
      selected: false,
    },
    // Add more user data as needed
  ];

  contacts: any[] = [
    {
      name: 'Contact 1',
      address: 'Address 1',
      phone: '123-456-7890',
      selected: false,
    },
    {
      name: 'Contact 2',
      address: 'Address 2',
      phone: '987-654-3210',
      selected: false,
    },
    // Add more contact data as needed
  ];
 roleFlag: boolean = false;
  userFlag: boolean = false;

  isStep1Completed(): boolean {
    return this.rolesForm.valid;
  }

  isStep2Completed(): boolean {
    // Add relevant logic if needed
    return this.perBtn;
  }

  isStep3Completed(): boolean {
    // Add relevant logic if needed
    return this.contacts.some((contact) => contact.selected);
  }

  saveContact() {
    // Example: this.contactService.saveContact(this.contactName, this.contactAddress, this.contactPhone).subscribe(() => console.log('Contact saved successfully'));
  }

  ngOnInit() {
    this.getPermissionsList();
    this.geAllUsersData(1, 50);
      }
  public geAllUsersData(page: number, pageSize: number) {
    this.loading = true;
    this.userMappingService.getAllUsers(page, pageSize).subscribe(
      (data: any) => {
        this.allUsersData = data.content;
        this.recordsData = data.totalElements;
        this.allUsertotalPages = data.totalPages;
        this.UsersRecordsData = this.allUsersData.length;
        this.originalUsersData = [...this.allUsersData];
        // this.loading = false;

        console.log('get all users response------>', this.allUsersData);
        console.log('this.UsersRecordsData------>', this.UsersRecordsData);
        console.log(
          'this. this.allUsertotalPages------>',
          this.allUsertotalPages
        );
      },
      (error) => {
        console.log('fetch user details failed: ', error);
      },
      () => {
        this.loading = false; // Set loading to false when the API request completes (regardless of success or failure)
      }
    );
  }
  getPermissionsList() {
    this.rolesService.getApplicationPermissions().subscribe((data) => {
      this.permissionsList = data;
      this.originalPermissions = this.permissionsList;
    });
    console.log(JSON.stringify(this.permissionsList));
  }
  addRoleAndNext() {
    this.addRole();
   
  }
  
 

 
  
  // addRole() {
  //   console.log('role is called');

  
  //   this.rolesService.addRole(this.rolesForm.value).subscribe(
  //     (response: RoleResponse) => {
  //       this.roleId = response.roleId;
  //       if (response.roleId) {
  //         this.snackBar.open('Role created Successfully', 'Close', {
  //           duration: 3000,
  //           verticalPosition: 'top',
  //           panelClass: ['custom-snackbar'], // Apply the CSS class here
  //         });
  //         this.stepper.next();

  //       }
  //       this.roleFlag = true;
   
  //           },
  //     (error) => {
  //       if (error.status == 400) {
  //         // Show toaster message for Role exists //
  //         this.snackBar.open('Role already exists', 'Close', {
  //           duration: 3000,
  //           verticalPosition: 'top',
  //           panelClass: ['custom-snackbar'],
  //         });
  //       }
  //       this.roleFlag = false;
  //     }
  //   );
  // }
  
  addRole() {
    console.log('role is called');
  
    this.rolesService.addRole(this.rolesForm.value).subscribe(
      (response: RoleResponse) => {
        this.roleId = response.roleId;
        if (response.roleId) {
          this.toasterservice.showToast('success', 'Role created Successfully');
          this.roleFlag = true;
          console.log(this.roleFlag ,'this.roleFlag ')
          this.cdRef.detectChanges();
          this.stepper.next();
        }
      
      },
      (error) => {
        if (error.status == 400) {
          this.toasterservice.showToast('error', 'Role already exists');
         
        }
        this.roleFlag = false;
      }
    );
   
  }
  
  addUser() {
    this.userFlag = true;
    this.cdRef.detectChanges();
  }


  // addRoleAndNext() {
  //   this.addRole();
  //   this.stepper.next(); // Move to the next step
  // }
  SavePermissionAndNext() {
    this.savePermission();
    this.stepper.next();
  }

  // adding selected permissions into array
  selectPermission(permissionId: number, index: number) {
    this.selectedPermissions[permissionId] =
      !this.selectedPermissions[permissionId];

    if (this.selectedPermissions[permissionId]) {
      this.selPermList.push(permissionId);
      this.perBtn = false;
    } else {
      this.selPermList.splice(this.selPermList.indexOf(permissionId), 1);
      if (this.selPermList.length > 0) {
        this.perBtn = false;
      } else {
        this.perBtn = true;
      }
    }
  }

  // saving selected permissions
  savePermission() {
    const mapData = {
      roleId: this.roleId,
      permissionIdData: this.selPermList,
      active: true,
    };
    this.rolesService.mapRolePermission(mapData).subscribe(
      (data) => {
        this.toasterservice.showToast('success','Permission added to Role Successfully');
         
      },
      (error) => {
        if (error.status == 400) {
          this.toasterservice.showToast('error','Permissions already exists');
        
        }
      }
    );
    this.permissionFlag = true;
    this.cdRef.detectChanges();
    console.log(this.permissionFlag, ' this.permissionFlag');
  }

  ColumnSearch(): void {
    const searchTextPermission =
      this.searchText.permissionDescription.toLowerCase();
    const searchTextPermissionCode =
      this.searchText.permissionCode.toLowerCase();

    console.log('searchTextPermissionCode:', searchTextPermissionCode);
    console.log('searchTextPermission:', searchTextPermission);

    if (searchTextPermissionCode === '' && searchTextPermission === '') {
      // If both search inputs are empty, reset the data to its original state
      console.log('Resetting data...');
      this.resetData();
    } else {
      console.log('inside loop');
      // Filter permissions based on search terms
      this.permissionsList = this.permissionsList.filter((permission: any) => {
        const codeMatch = permission.permissionCode
          .toLowerCase()
          .includes(searchTextPermissionCode);
        const descriptionMatch = permission.permissionDescription
          .toLowerCase()
          .includes(searchTextPermission);
        return codeMatch && descriptionMatch;
      });

      console.log('Filtered permissions:', this.permissionsList);
    }
  }

  resetData() {
    this.permissionsList = this.originalPermissions;
  }
  onNextPageClick() {
    const maxPage = Math.ceil(
      this.permissionsList.length / this.allPermissionItemsPerPage
    );
    if (this.allPermissionCurrentPage < maxPage) {
      this.allPermissionCurrentPage++;
    }
  }

  onPageNumberClick(page: number) {
    this.allPermissionCurrentPage = page;
  }

  onItemsPerPageChange() {
    this.allPermissionCurrentPage = 1;
  }

  getPageNumbers(): number[] {
    const maxPage = Math.ceil(
      this.permissionsList.length / this.allPermissionItemsPerPage
    );
    return Array.from({ length: maxPage }, (_, i) => i + 1);
  }
  onPreviousPageClick() {
    if (this.allPermissionCurrentPage > 1) {
      this.allPermissionCurrentPage--;
    }
  }

  // Adding selected Users to array
  selectAllUsers(userId: number, index: number) {
    this.selectedUser[userId] = !this.selectedUser[userId];

    if (this.selectedUser[userId]) {
      this.selUserList.push(userId);
      this.userBtn = false;
    } else {
      this.selUserList.splice(this.selUserList.indexOf(userId), 1);
      if (this.selPermList.length > 0) {
        this.userBtn = false;
      } else {
        this.userBtn = true;
      }
    }
  }
  // Saving selected users
  saveUsers() {
    const mapData = {
      roleId: this.roleId,
      userIds: this.selUserList,
    };
    this.rolesService.addUserstoRoles(mapData).subscribe((response) => {
      console.log('User saved roleId:' + this.roleId);
    });
    this.goBack();
    this.toasterservice.showToast('success','User added to Role Successfully');
        
   // this.step3Editable = false;
    this.usersFlag = false;
  }
  goBack() {
    this.permissionsList.forEach((element: any) => {
      this.selectedPermissions[element.permissionId] = false;
    });
    this.usersList.forEach((element: any) => {
      this.selectedUser[element.userId] = false;
    });

    this.rolesForm.patchValue({
      roleCode: [''],
      roleDescription: [''],
    });
 this.roleFlag = false;
 this.permissionFlag = false;
 this.usersFlag = false;
    this.stepper.selectedIndex = 0;
  }
  sortColumn(columnName: string, isSort: boolean): void {
    if (isSort) {
      // Toggle sorting direction (asc, desc) for the clicked column
      this.columnSortState[columnName] =
        this.columnSortState[columnName] === 'asc' ? 'desc' : 'asc';

      // Perform the sorting logic here using the updated sorting state
      // You will need to sort your data array based on the column and direction
      this.allUsersData.sort(
        (a: { [x: string]: number }, b: { [x: string]: number }) => {
          const columnA = this.getColumnValue(a, columnName);
          const columnB = this.getColumnValue(b, columnName);

          if (this.columnSortState[columnName] === 'asc') {
            return columnA.localeCompare(columnB);
          } else {
            return columnB.localeCompare(columnA);
          }
        }
      );
    } else {
      // Toggle sorting direction (asc, desc) for the clicked column
      this.columnUserFilterSort[columnName] =
        this.columnUserFilterSort[columnName] === 'asc' ? 'desc' : 'asc';

      // Perform the sorting logic here using the updated sorting state
      // You will need to sort your data array based on the column and direction
      this.usersList.sort(
        (a: { [x: string]: number }, b: { [x: string]: number }) => {
          const columnA = this.getUserFilterColumnValue(a, columnName);
          const columnB = this.getUserFilterColumnValue(b, columnName);

          if (this.columnUserFilterSort[columnName] === 'asc') {
            return columnA.localeCompare(columnB);
          } else {
            return columnB.localeCompare(columnA);
          }
        }
      );
    }
  }

  getColumnValue(item: any, columnName: string): string {
    switch (columnName) {
      case 'activeDirectoryId':
        return item.activeDirectoryId.toString();
      case 'firstName':
        return item.firstName.toString();
      case 'middleInitial':
        return item.middleInitial.toString();
      case 'lastName':
        return item.lastName.toString();
      case 'emailAddress':
        return item.emailAddress.toString();
      case 'createdDateTime':
        return item.createdDateTime.toString();
      case 'statusId':
        return item.statusId.toString();
      default:
        return ''; // Handle other columns as needed
    }
  }
  getUserFilterColumnValue(item: any, columnName: string): string {
    if (
      columnName === 'activeDirectoryId' &&
      item.onPremisesSamAccountName != null
    ) {
      return item.onPremisesSamAccountName.toString();
    } else {
      switch (columnName) {
        case 'firstName':
          return item.givenName.toString();
        case 'lastName':
          return item.surname.toString();
        case 'userName':
          return item.userName.toString();
        case 'middleInitial':
          return item.middleInitial.toString();
        case 'emailAddress':
          return item.userPrincipalName.toString();
        default:
          return ''; // Handle other columns as needed
      }
    }
  }
  isColumnSorted(columnName: string, sortOrder: string): boolean {
    return this.columnSortState[columnName] === sortOrder;
  }
  isUserFilterColumnSorted(columnName: string, sortOrder: string): boolean {
    return this.columnUserFilterSort[columnName] === sortOrder;
  }
  onSearch(searchType: string, searchValue: string) {
    // Check if all search inputs are empty
    if (
      !this.searchText.activeDirectoryId &&
      !this.searchText.firstName &&
      !this.searchText.lastName
    ) {
      // Reset the storeListData to the original data
      // this.allUsersData = [...this.originalUsersData];
      this.allUsersData = this.geAllUsersData(1, 50);
    } else {
      // Check if the search input has a minimum of 3 characters
      if (searchValue.length >= 3) {
        this.userMappingService.findByUser(searchType, searchValue).subscribe(
          (data: any) => {
            this.allUsersData = data;
            this.recordsData = this.allUsersData.length;
            // this.allUsersItemsPerPage = this.recordsData;

            console.log(this.recordsData, 'his.recordsDatalength');
          },
          (error: any) => {
            console.error('Error fetching data:', error);
          }
        );
      } else {
        // If the input has less than 3 characters, you might want to handle this case (e.g., display a message).
        console.log('Search input should have a minimum of 3 characters.');
      }
    }
  }
  onUsersPreviousPageClick() {
    if (this.allUsersCurrentPage > 1) {
      this.allUsersCurrentPage--;
    }

    this.geAllUsersData(this.allUsersCurrentPage, this.allUsersItemsPerPage);
  }

  onUsersNextPageClick() {
    // Assume you have the total number of pages available in a variable totalPages
    if (this.allUsersCurrentPage < this.allUsertotalPages) {
      this.allUsersCurrentPage++;
    }
    // Fetch data for the new page
    this.geAllUsersData(this.allUsersCurrentPage, this.allUsersItemsPerPage);
  }

  onUsersPageNumberClick(page: number | string) {
    if (typeof page === 'number' && page !== this.allUsersCurrentPage) {
      this.allUsersCurrentPage = page;
      this.geAllUsersData(this.allUsersCurrentPage, this.allUsersItemsPerPage);
    } else {
      // Handle the case when '...' is clicked (optional)
      console.log('Ellipsis clicked or other handling for non-numeric pages.');
    }
  }

  onUsersPageSizeChange() {
    // Reset current page to 1 when changing the page size
    this.allUsersCurrentPage = 1;

    // Fetch data for the new page size
    this.geAllUsersData(this.allUsersCurrentPage, this.allUsersItemsPerPage);
  }

  getUsersPageNumbers(): number[] {
    return Array.from(
      { length: this.allUsertotalPages },
      (_, index) => index + 1
    );
  }

  // displayedPageNumbers(): (number | string)[] {
  //   const totalPages = this.allUsertotalPages;
  //   const visiblePages = 5; // Adjust the number of visible pages as needed
  //   const startPage = Math.max(
  //     1,
  //     this.allUsersCurrentPage - Math.floor(visiblePages / 2)
  //   );
  //   const endPage = Math.min(totalPages, startPage + visiblePages - 1);

  //   let pages: (number | string)[] = Array.from(
  //     { length: endPage - startPage + 1 },
  //     (_, index) => startPage + index
  //   );

  //   // Add ellipsis and last page number if necessary
  //   if (endPage < totalPages) {
  //     pages = [...pages, '...', totalPages];
  //   }

  //   // Add a link to the first page if the last page is reached
  //   if (this.allUsersCurrentPage === totalPages && totalPages > visiblePages) {
  //     pages = [1, '...', ...pages];
  //   }

  //   return pages;
  // }
  displayedPageNumbers(): (number | string)[] {
    if (this.allUsersData.length === 0) {
      return [1];
    }
  
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
  onKeyPress(event: Event): void {
    // Cast the event to KeyboardEvent
    const keyboardEvent = event as KeyboardEvent;
  
    // Prevent the input of space character (key code 32)
    if (keyboardEvent.keyCode === 32) {
      keyboardEvent.preventDefault();
    }
  }
  goToStep(stepIndex: number): void {
    console.log("called...back",stepIndex)
    // Reset the form values
    this.rolesForm.patchValue({
      roleCode: '',
      roleDescription: ''
    });
  
    // Set the selected index of the stepper to the specified step
    this.stepper.selectedIndex = stepIndex;
  }
  
  
}
