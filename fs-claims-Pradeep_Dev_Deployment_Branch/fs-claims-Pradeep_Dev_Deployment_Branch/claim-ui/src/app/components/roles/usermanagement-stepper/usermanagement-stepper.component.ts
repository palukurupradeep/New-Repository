import { Component, OnInit } from '@angular/core';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';

@Component({
  selector: 'app-usermanagement-stepper',
  templateUrl: './usermanagement-stepper.component.html',
  styleUrls: ['./usermanagement-stepper.component.scss']
})
export class UsermanagementStepperComponent implements OnInit{
  public emptyUserData: boolean = false;
  public userData: any = [];
  public columnSortState: { [key: string]: 'asc' | 'desc' } = {};
  public allUsersData: any = [];
  public originalUsersData: any[] = [];
  public managerUserList: any[] = [];
  public columnUserFilterSort: { [key: string]: 'asc' | 'desc' } = {};
  public userCurrentPage: number = 1;
  public userItemsPerPage: number = 5;
  public setUserData: any;
  public userRowselected: boolean = false;
  public allUsersCurrentPage: number = 1;
  public allUsersItemsPerPage: number = 5;
  public showStepper: boolean = false;

constructor(
  private userMappingService: UserMappingService
){
  
}

ngOnInit(): void {
  this.geAllUsersData();
}

// on click search button 
public getUserData() {
  this.userMappingService
    .findByUser("FirstName", "ANDREW")
    .subscribe(
      (data: any) => {
        this.emptyUserData = false;
        this.userData = data;
        console.log('find users response------>', this.userData);
        this.showStepper = true;
      },
      (error) => {
        this.userData = [];
        this.emptyUserData = true;
        console.log(
          'fetch find user details failed: ' + JSON.stringify(this.userData)
        );
      }
    );
}

// sort table data
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
    this.userData.sort(
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
  switch (columnName) {
    case 'firstName':
      return item.firstName.toString();
    case 'lastName':
      return item.lastName.toString();
    case 'userName':
      return item.userName.toString();
    case 'middleInitial':
      return item.middleInitial.toString();
    case 'emailAddress':
      return item.emailAddress.toString();
    case 'statusId':
      return item.statusId.toString();
    default:
      return ''; // Handle other columns as needed
  }
}

isUserFilterColumnSorted(columnName: string, sortOrder: string): boolean {
  return this.columnUserFilterSort[columnName] === sortOrder;
}

get pagedUsersListData() {
  const startIndex = (this.userCurrentPage - 1) * this.userItemsPerPage;
  const endIndex = startIndex + this.userItemsPerPage;
  return this.userData.slice(startIndex, endIndex);
}

// select only one user
toggleRowSelection(selectedUser: any) {
  selectedUser.selected = !selectedUser.selected;
  for (const user of this.userData) {
    if (user !== selectedUser) {
      user.selected = false;
    }
    if (user == selectedUser) {
      this.setUserData = selectedUser;
    }
  }
  this.userRowselected = this.userData.some(
    (user: { selected: any }) => user.selected
  );
  // console.log("Selected User => "+JSON.stringify(this.setUserData));
}

onPreviousPageClick(preClick: boolean) {
  if (preClick) {
    if (this.allUsersCurrentPage > 1) {
      this.allUsersCurrentPage--;
    }
  } else {
    if (this.userCurrentPage > 1) {
      this.userCurrentPage--;
    }
  }
}

getPageNumbers(pageNo: boolean): number[] {
  if (pageNo) {
    const pageCount = Math.ceil(
      this.allUsersData.length / this.allUsersItemsPerPage
    );
    return Array.from({ length: pageCount }, (_, index) => index + 1);
  } else {
    const pageCount = Math.ceil(this.userData.length / this.userItemsPerPage);
    return Array.from({ length: pageCount }, (_, index) => index + 1);
  }
}

onPageNumberClick(page: number, pageClick: boolean) {
  if (pageClick) {
    if (page >= 1 && page <= this.getPageNumbers(pageClick).length) {
      this.allUsersCurrentPage = page;
    }
  } else {
    if (page >= 1 && page <= this.getPageNumbers(pageClick).length) {
      this.userCurrentPage = page;
    }
  }
}

onNextPageClick(nextClick: boolean) {
  if (nextClick) {
    const maxPage = Math.ceil(
      this.allUsersData.length / this.allUsersItemsPerPage
    );
    if (this.allUsersCurrentPage < maxPage) {
      this.allUsersCurrentPage++;
    } else {
      const maxPage = Math.ceil(this.userData.length / this.userItemsPerPage);
      if (this.userCurrentPage < maxPage) {
        this.userCurrentPage++;
      }
    }
  }
}

onItemsPerPageChange(itemPerPage: boolean) {
  if (itemPerPage) {
    this.allUsersCurrentPage = 1; // Reset to the first page when changing items per page
  } else {
    this.userCurrentPage = 1; // Reset to the first page when changing items per page
  }
}

// Add user
addSelectedUser() {
  console.log('Selected User => ' + JSON.stringify(this.setUserData));
  this.setUserData.statusId = 1;
  this.userMappingService.addUser(this.setUserData).subscribe((data) => {
    console.log('resp data => ' + data);
    // this.geAllUsersData();
  });
}

// getting all users data
public geAllUsersData() {
  this.userMappingService.getAllUsers(0, 10).subscribe(
    (data: any) => {
      this.allUsersData = data.content;
      this.originalUsersData = [...this.allUsersData];
      this.managerUserList = data.content;
      console.log('get all users response------>', this.allUsersData);
    },
    (error) => {
      console.log('fetch user details failed: ', error);
    }
  );
}

}
