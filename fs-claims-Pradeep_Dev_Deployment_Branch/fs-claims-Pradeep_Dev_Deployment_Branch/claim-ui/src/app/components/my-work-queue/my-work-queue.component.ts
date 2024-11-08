import { Component,Input } from '@angular/core';
import { Sort } from '@angular/material/sort';
import { userGroups } from 'src/app/interfaces/user-mapping.model';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';
import { WorkHistoryService } from 'src/app/ngrx/store/services/work-history.service';

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-my-work-queue',
  templateUrl: './my-work-queue.component.html',
  styleUrls: ['./my-work-queue.component.scss']
})
export class MyWorkQueueComponent {
  @Input() screenWidth = 0;
  public isSideNavCollapsed: boolean = true;
  public workHistoryDetails: Array<any> = [];
  public sortworkHistoryDetails: Array<any> = [];
  public workHistoryDetailsData:Array<any> = [];

  public selectedUserName : string = "";
  public userGroupList: any = [];
  public managerGroupList: any = [];
  public selectedGroup!: number;
  public selectedManager: any ={};
  public userId!:number;
  public groupList: userGroups[] = [];
  public selectedGroupName: number = 1;
  public selectedManagerName: string = '';
  public managerList: any[] = [];
  public currentDate: string | undefined;
  public tabSearchText: any = {};
  public workhistoryListItemsPerPage: number = 50; // Default items per page
  public workhistoryListCurrentPage: number = 1;
  public perPageOptions = [50, 100, 250, 500];
  public columnSortState: { [key: string]: 'asc' | 'desc' } = {};


  constructor(
    private bodyClassService: BodyClassService,
    private workHistoryService: WorkHistoryService,
    private userMapping: UserMappingService,
  ) { }

  ngOnInit() {
    this.getGroupsList();
    this.getCurrentDate();
     let userId = Number(localStorage.getItem("userId"));
     this.selectedGroupName = Number(localStorage.getItem('selectedGroupName'))
   this.selectedManagerName = String(localStorage.getItem('selectedManagerId'))
   
  }

  public getCurrentDate(): void {
    const today = new Date();
    const dd = String(today.getDate()).padStart(2, '0');
    const mm = String(today.getMonth() + 1).padStart(2, '0'); // January is 0!
    const yyyy = today.getFullYear();
    this.currentDate = mm + '/' + dd + '/' + yyyy;
  }

  public transformToCamelCase(name: string): string {
    // Trim leading and trailing whitespace
    name = name.trim();

    const words = name.split(' ');

    // Handle special cases for specific headers
    switch (name.toLowerCase()) {
      case 'reason code':
        return 'claimReasonCode';
      case 'sell company':
        return 'sellingCompanyId';
      case 'customer #':
        return 'customerNumber';
      case 'state':
        return 'stateCode';
      case 'end user':
        return 'endUserName';
      case 'inspection report date':
        return 'inspectionReportDate';
      case 'global/national':
        return 'globalorNational';
      default:
        // Make the first word lowercase
        const camelCaseWords = [words[0].toLowerCase()];

        // Capitalize the first letter of each subsequent word
        for (let i = 1; i < words.length; i++) {
          camelCaseWords.push(
            words[i].charAt(0).toUpperCase() + words[i].slice(1).toLowerCase()
          );
        }

        // Join the words to form camelCase
        const camelCaseName = camelCaseWords.join('');
        return camelCaseName;
    }
  }

  public onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  public getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed,this.screenWidth);
  }

  public getGroupsList() {
    this.userMapping.getAllUserGroups().subscribe((data) => {
      this.groupList = data;
      this.userId = Number(localStorage.getItem('selectedManagerId'));
 
    this.getManagersList();
    this.getWorkHistory();
    });
    }

    public getManagersList() {


    if (this.selectedGroupName) {
        this.userMapping.getManagerById(Number(this.selectedGroupName)).subscribe((data) => {
            this.managerList = data;
            const loggedInUserManager = this.managerList.find(manager => manager.userId === this.userId);
         
            if (loggedInUserManager) {
            
                this.selectedManagerName = loggedInUserManager.userId; // Set the selected manager to logged in user's manager
            
              } else {
                this.selectedManagerName = ''; // Clear selection if logged in user's manager is not found

              }
        });
    } else {
        this.managerList = []; // If no group is selected, clear the manager list and selection
        this.selectedManagerName = '';
    }
}

public onManagerSelected() {
    this.userId = Number(this.selectedManagerName);

   // this.userId = Number(this.selectedManager.userId);

    this.getWorkHistory();
    // this.fetchUserWorkQueueFieldsByUserId();
   // this.getClaimWorkQueues();
  }

  public getWorkHistory(){
    this.workHistoryService.getWorkHistory(this.userId).
    subscribe(
      (data:any) => {
        this.workHistoryDetails = data;
        this.workHistoryDetailsData = data.slice();
        this.sortworkHistoryDetails = data;
      }
    )
  }




public onTabSearch() {
  if (!this.tabSearchText.userName && !this.tabSearchText.workstatus && !this.tabSearchText.claimnumber
    && !this.tabSearchText.claimamount && !this.tabSearchText.reasoncode && !this.tabSearchText.customernumber
    && !this.tabSearchText.customername && !this.tabSearchText.citystatezip && !this.tabSearchText.endusername && !this.tabSearchText.worktime
  ) {
    this.workHistoryDetails = this.workHistoryDetailsData;
  } else {
    this.workHistoryDetails = this.workHistoryDetailsData.filter((user: any) => {
      let matches = true;

      if (this.tabSearchText.userName) {
        matches = matches && user.username != null && user.username.toLowerCase().includes(this.tabSearchText.userName.toLowerCase());
      }

      if (this.tabSearchText.workstatus) {
        matches = matches && user.workStatus != null && user.workStatus.toLowerCase().includes(this.tabSearchText.workstatus.toLowerCase());
      }

      if (this.tabSearchText.claimnumber) {
        matches = matches && user.claimNumber != null && user.claimNumber.toLowerCase().includes(this.tabSearchText.claimnumber.toLowerCase());
      }

      if (this.tabSearchText.claimamount) {
        matches = matches && user.claimAmount != null && user.claimAmount.toString().includes(this.tabSearchText.claimamount);
      }

      if (this.tabSearchText.reasoncode) {
        matches = matches && user.reasonCode != null && user.reasonCode.toLowerCase().includes(this.tabSearchText.reasoncode.toLowerCase());
      }

      if (this.tabSearchText.customernumber) {
        matches = matches && user.customerNumber != null && user.customerNumber.toLowerCase().includes(this.tabSearchText.customernumber.toLowerCase());
      }

      if (this.tabSearchText.customername) {
        matches = matches && user.customerName != null && user.customerName.toLowerCase().includes(this.tabSearchText.customername.toLowerCase());
      }

      if (this.tabSearchText.citystatezip) {
        let combinedLocation = `${user.city} ${user.state} ${user.zip}`.toLowerCase();
        matches = matches && combinedLocation.includes(this.tabSearchText.citystatezip.toLowerCase());
    }

      if (this.tabSearchText.endusername) {
        matches = matches && user.endUserName != null && user.endUserName.toLowerCase().includes(this.tabSearchText.endusername.toLowerCase());
      }

      if (this.tabSearchText.worktime) {
        matches = matches && user.workTime != null && user.workTime.toString().includes(this.tabSearchText.worktime);
      }

      return matches;
    });
  }
}


  public  onPreviousPageClick(preClick: boolean) {
    if (preClick) {
      if (this.workhistoryListCurrentPage > 1) {
        this.workhistoryListCurrentPage--;
      }
    } else {
      if (this.workhistoryListCurrentPage > 1) {
        this.workhistoryListCurrentPage--;
      }
    }
  }

  public getPageNumbers(pageNo: boolean): number[] {
    if (pageNo) {
      const pageCount = Math.ceil(
        this.workHistoryDetails.length /  this.workhistoryListItemsPerPage
      );
      return Array.from({ length: pageCount }, (_, index) => index + 1);
    } else {
      const pageCount = Math.ceil(this.workHistoryDetails.length / this.workhistoryListItemsPerPage); 
      return Array.from({ length: pageCount }, (_, index) => index + 1);
    }
  }

   // Function to navigate to a specific page
   public  onPageNumberClick(page: number, pageClick: boolean) {
    if (pageClick) {
      if (page >= 1 && page <= this.getPageNumbers(pageClick).length) {
        this.workhistoryListCurrentPage = page;
      }
    } else {
      if (page >= 1 && page <= this.getPageNumbers(pageClick).length) {
        this.workhistoryListCurrentPage = page;
      }
    }
  }

   // Function to navigate to the next page
   public onNextPageClick(nextClick: boolean) {
    if (nextClick) {
      const maxPage = Math.ceil(
        this.workHistoryDetails.length / this.workhistoryListItemsPerPage
      );
      if (this.workhistoryListCurrentPage < maxPage) {
        this.workhistoryListCurrentPage++;
      } else {
        const maxPage = Math.ceil(this.workHistoryDetails.length / this.workhistoryListItemsPerPage);
        if (this.workhistoryListCurrentPage < maxPage) {
          this.workhistoryListCurrentPage++;
        }
      }
    }
  }


  public onItemsPerPageChange() {
   this.workhistoryListCurrentPage = 1; // Reset to the first page when changing items per page
 }

 sortData(sort: Sort) {
  const data = this.sortworkHistoryDetails.slice(); // Use productCatalog for sorting

  if (!sort.active || sort.direction === '') {
    this.workHistoryDetails = data;
    return;
  }

  this.workHistoryDetails = data.sort((a:any, b:any) => {
    const isAsc = sort.direction === 'asc';
    switch (sort.active) {
      case 'username':
        return this.compare(a.username, b.username, isAsc);
      case 'workstatus':
        return this.compare(a.workStatus, b.workStatus, isAsc);
      case 'claimnumber':
        return this.compare(a.claimNumber, b.claimNumber, isAsc);
        case 'claimAmount':
          return this.compare(a.claimAmount, b.claimAmount, isAsc);
     
          case 'reasonCode':
            return this.compare(a.reasonCode, b.reasonCode, isAsc);
         case 'customerNumber':
          return this.compare(a.customerNumber, b.customerNumber, isAsc);
     
          case 'customername':
          return this.compare(a.customerName, b.customerName, isAsc);
            case 'citystatezip':
const addressA = a.address?.[0] || {};
const addressB = b.address?.[0] || {};
const formattedAddressA = `${addressA.city}, ${addressA.state}, ${addressA.zip}`;
const formattedAddressB = `${addressB.city}, ${addressB.state}, ${addressB.zip}`;
return this.compare(formattedAddressA, formattedAddressB, isAsc);
case 'endUserName':
  return this.compare(a.endUserName, b.endUserName, isAsc);
  case 'workTime':
    return this.compare(a.workTime, b.workTime, isAsc);
           
      default:
        return 0;
    }
  });
}


// Comparison function for sorting
 compare(a: string | number, b: string | number, isAsc: boolean) {
return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
}


