import { Component } from '@angular/core';

@Component({
  selector: 'app-work-queue-table',
  templateUrl: './work-queue-table.component.html',
  styleUrls: ['./work-queue-table.component.scss']
})
export class WorkQueueTableComponent {
  public columnSortState: { [key: string]: 'asc' | 'desc' } = {};
  public columnUserFilterSort: { [key: string]: 'asc' | 'desc' } = {};
  public allUsersData: any = [];
  public userData: any = [];
  public allUsersItemsPerPage: number = 50; // Default items per page
  public userItemsPerPage: number = 50; // Default items per page
  public allUsersCurrentPage: number = 1;
  public userCurrentPage: number = 1;
  public tabSearchText: any = {};
  displayedWorkQueueInfo: any = [];
  public emptyUserData: boolean = false;
  perPageOptions = [50, 100, 250, 500];
  constructor() {

  }

  ngOnInit() {

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
  onItemsPerPageChange(itemPerPage: boolean) {
    if (itemPerPage) {
      this.allUsersCurrentPage = 1; // Reset to the first page when changing items per page
    } else {
      this.userCurrentPage = 1; // Reset to the first page when changing items per page
    }
  }
  onTabSearch() {
    
  }
  onPageSizeChange() {
  }

  onUsersPreviousPageClick() {
  }

  onUsersNextPageClick() {
  }

  onUsersPageNumberClick(page: number | string) {
  }

  onUsersPageSizeChange() {
  }

  getUsersPageNumbers() {
  }

  onKeyPress(event: Event): void {
    // Cast the event to KeyboardEvent
    const keyboardEvent = event as KeyboardEvent;
  
    // Prevent the input of space character (key code 32)
    if (keyboardEvent.keyCode === 32) {
      keyboardEvent.preventDefault();
    }
  }
  toggleRowSelection(selectedWorkQueue: any) {
  }

  onUserPageNumberClick(page: number): void {
    this.userCurrentPage = page;
  }
  
  onPreviousUserPageClick(): void {
  }
  
  onNextUserPageClick(): void {
  }

  getUserPageNumbers(): number[] {
    const totalItems = this.userData.length;
  
    // Handle no data scenario
    if (totalItems === 0) {
      this.userCurrentPage = 1; // Set current page to 1 when there's no data
      return [1]; // Return an array with page 1
    }
  
    const totalPages = Math.ceil(totalItems / this.userItemsPerPage);
    return Array.from({ length: totalPages }, (_, index) => index + 1);
  }

  onPageUserSizeChange(): void {
    this.userCurrentPage = 1;
    }
}
