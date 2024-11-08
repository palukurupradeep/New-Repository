import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { HttpResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, Input } from '@angular/core';
import { Sort } from '@angular/material/sort';
import { ActivatedRoute, Router } from '@angular/router';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { userGroups } from 'src/app/interfaces/user-mapping.model';
import { AuthService } from 'src/app/ngrx/store/services/auth.service';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { DashboardService } from 'src/app/ngrx/store/services/dashboard.service';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';
import { WorkqueueService } from 'src/app/ngrx/store/services/workqueue.service';

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}
// let tempWorkQueue: never[] = []

@Component({
  selector: 'app-work-queue',
  templateUrl: './work-queue.component.html',
  styleUrls: ['./work-queue.component.scss'],
})
export class WorkQueueComponent {
  @Input() screenWidth = 0;
  isSideNavCollapsed: boolean = true;
  public userGroupList: any = [];
  public managerGroupList: any = [];
  public selectedGroup!: number;
  public selectedManager: any = {};
  claimWorkQueuesList: any = {};
  orginalClaimWorkQueuesList: any = {}
  toRestoreData: any[] = [];
  fetchUserWorkQueueFields: any[] = [];
  defaultfetchWorkQueueFields: any[] = [];
  filterFetchUserWorkQueueFields: any[] = [];
  selectedWorkQueueFields: any;
  selectedWorkQueueField: any; // You can initialize this as needed 
  searchFilters: { [key: string]: string } = {};
  sortColumn: string | null = null;
  sortDirection: string | null = null;

  userId!: number;
  loadingData: boolean = false;
  public claimWorkQueuesListCurrentPage: number = 1;
  perPageOptions = [50, 100, 250, 500];
  itemsPerPage: number = 50;

  deletedColumns: any[] = [];
  showAddColumnDialog = false;
  selectedColumn: string | undefined;
  public activeType: string = '';
  public activeValue: string = '';
  unmatchedFields: any[] = [];
  public activeStatus: string = '';
  public groupList: userGroups[] = [];
  public selectedGroupName: number = 1;
  public selectedManagerName: string = '';
  public managerList: any[] = [];
  isManager: any = false;
  removedFields: any[] = [];
  enableOk: boolean = true;
  hoveredColumn: string | null = null;
  tempWorkQueue: any
  public sortTemp:any;
  public tempSearchValue:any;
 public tempPropertyValue:any;

  constructor(
    private bodyClassService: BodyClassService,
    private workQueueService: WorkqueueService,
    private router: Router,
    private userMapping: UserMappingService,
    private cdr: ChangeDetectorRef,
    private activeRoute: ActivatedRoute,
    private dashBoardservice: DashboardService,
    private authService: AuthService
  ) { }

  onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  getBodyClass(): string {
    return this.bodyClassService.getBodyClass(
      this.isSideNavCollapsed,
      this.screenWidth
    );
  }
  ngOnInit(): void {
    this.userMapping.fetchRolesAndPermissionByUserId(Number(localStorage.getItem('userId'))).subscribe(data => {
      const userPerData =data;
      
        const roles = userPerData.map((roleData:any) => roleData.roleCode);
    const permissions = userPerData.flatMap((roleData:any) => roleData.permissions);
    console.log(roles,'roles');
    console.log(permissions,'permissions');
   this.authService.setRolesAndPermissions(roles, permissions);
   const rolesfetch = this.authService.getRoles();
const permissionsfetch = this.authService.getPermissions();

console.log('Roles:', rolesfetch);
console.log('Permissions:', permissionsfetch);
  });
    this.getGroupsList();
   this.selectedGroupName = Number(localStorage.getItem('selectedGroupName'))
   this.selectedManagerName = String(localStorage.getItem('selectedManagerId'))
    // this.fetchUserWorkQueueFieldsByUserId();
   // // this.getClaimWorkQueues();



  }
  getOpenClaims() {
    this.dashBoardservice.previousDayClaims(this.userId).subscribe(data => {
      console.log(data);
    })
  }
  getLastActivityAge() {
    this.dashBoardservice.lastActivityClaims(this.userId, Number(this.activeValue)).
      subscribe((data: any) => {
       // console.log('lastActivityAge', data.body);
        const records = data.body.workQueueDetailsDTO.filter((record: any) => record.workStatus !== "CLAIM PENDED" && record.workStatus !== "ASSIGNED TO ISC");
        this.claimWorkQueuesList.workQueueDetailsDTO = records;
        // const records = data.body.workQueueDetailsDTO.filter((record: any) => record.lastActivityAge < Number(this.activeValue));

    //    this.claimWorkQueuesList.workQueueDetailsDTO = data.body.workQueueDetailsDTO;
    //   this.tempWorkQueue = data.body.workQueueDetailsDTO;
     this.tempWorkQueue = records;
      this.toRestoreData = records;
      })
  }
  getPriorityClaims() {
    this.dashBoardservice.getPriorityClaims(this.userId).
      subscribe((data: any) => {
        // console.log(data,'...getPriorityClaims');
        const records = data.body.workQueueDetailsDTO.filter((record: any) => record.priorityClaim == true && record.workStatus !== "CLAIM PENDED" && record.workStatus !== "ASSIGNED TO ISC");
        this.claimWorkQueuesList.workQueueDetailsDTO = records;
        this.tempWorkQueue = records;
        this.toRestoreData = records;
      })
  }

  getAgedClaims() {
    this.dashBoardservice.getAgedClaims(this.userId, Number(this.activeValue)).
      subscribe(data => {

       // this.claimWorkQueuesList = data.body;
       // this.tempWorkQueue = data.body.workQueueDetailsDTO;
        // data.body.workQueueDetailsDTO.forEach((data:any) => {console.log(data.claimAge);})


           const records = data.body.workQueueDetailsDTO.filter((record: any) => record.workStatus !== "CLAIM PENDED" && record.workStatus !== "ASSIGNED TO ISC");
               this.claimWorkQueuesList.workQueueDetailsDTO = records;
        this.tempWorkQueue = records;
        this.toRestoreData = records;

      })
  }
  getGroupsList() {
    this.userMapping.getAllUserGroups().subscribe((data) => {
      // console.log("Group List =>"+JSON.stringify(data));
      this.groupList = data;
      // let userId = Number(localStorage.getItem('selectedManagerId')) || Number(localStorage.getItem('userId'));

      this.userId = Number(localStorage.getItem('selectedManagerId'));
      console.log(this.userId, 'Retrieved userId from localStorage');

      this.getManagersList();
    });



  }
  getUserGroups() {
    this.userId = Number(localStorage.getItem('userId'));
    this.userMapping.findByUserId(this.userId).subscribe(data => {
      this.userGroupList = data.userGroups;
      if (this.userGroupList.length > 0) {
        this.selectedGroup = this.userGroupList[0].userGroupId;
      }
      // console.log(this.selectedGroup, 'selectedGroup');
      // console.log(this.userGroupList, 'myusergrp');

      // Call getManagerList here to ensure it is executed after getUserGroups
      this.getManagersList();
    });
  }
  getManagersList() {
    console.log(this.selectedGroupName, 'selectedGroupName');
    if (this.selectedGroupName) {
      this.userMapping.getManagerById(Number(this.selectedGroupName)).subscribe((data) => {
        this.managerList = data;
        // this.userId = Number(localStorage.getItem('userId'));
        // Check if managerList contains manager of logged in user
        const loggedInUserManager = this.managerList.find(manager => manager.userId === this.userId);
        localStorage.setItem('selectedGroupName', this.selectedGroupName.toString());
        if (loggedInUserManager) {

          this.selectedManagerName = loggedInUserManager.userId; // Set the selected manager to logged in user's manager
          this.fetchUserWorkQueueFieldsByUserId();
          //  this.onManagerSelected();
        } else {
          this.selectedManagerName = ''; // Clear selection if logged in user's manager is not found
          console.log(this.selectedManagerName, 'this.selectedManagerName ')
        }
      });
    } else {
      this.managerList = []; // If no group is selected, clear the manager list and selection
      this.selectedManagerName = '';
    }
  }

  // getManagerList() {
  //   // console.log('got called');
  //   this.userMapping.getManagerById(this.selectedGroup).subscribe(
  //     (data: any) => {
  //       this.managerGroupList = data;
  //       // console.log(this.managerGroupList, 'managerlist');

  //       // Set the userId property of selectedManager when managerGroupList is updated
  //       if (this.managerGroupList.length > 0) {
  //         this.selectedManager = { userId:  this.userId }; // Set to the first manager

  //       }

  //       // console.log(this.selectedManager, 'selectedManager');

  //       // Emit the selected manager when managerGroupList is updated
  //       this.onManagerSelected();
  //     }
  //   );
  // }
  onManagerSelected() {
    localStorage.setItem('selectedManagerId', this.selectedManagerName.toString());
    this.userId = Number(this.selectedManagerName);
    this.checkIsManager(this.userId);
    console.log(this.removedFields, 'removedFields')

    console.log(this.fetchUserWorkQueueFields, 'mangefecth')
    this.getClaimWorkQueues();
    // this.userId = Number(this.selectedManager.userId);
    // console.log(this.selectedManager, 'selectedManager userId');
    // this.fetchUserWorkQueueFieldsByUserId();
    // this.getClaimWorkQueues();
  }

  getClaimWorkQueues() {
    this.loadingData = true;
    this.workQueueService
      .getClaimWorkQueues(this.userId)
      .subscribe(
        (data) => {
          // Success case
          this.claimWorkQueuesList = data.body;
          this.orginalClaimWorkQueuesList = data.body;
          //  this.tempWorkQueue = data.body.workQueueDetailsDTO;
  //        this.tempWorkQueue = JSON.parse(JSON.stringify(data.body.workQueueDetailsDTO));
       //   this.toRestoreData = data.body.workQueueDetailsDTO;
          console.log(this.toRestoreData, 'assigned');
          console.log(this.isManager, 'ismanagereeee')

          if (this.isManager === true) {
            console.log(this.removedFields, 'this.removedFields')
            this.fetchUserWorkQueueFields.push(...this.removedFields);
            //  this.removedFields.forEach(field => {
            //   if (!this.fetchUserWorkQueueFields.some(existingField => existingField.workQueueFieldName === field.workQueueFieldName)) {
            //     this.fetchUserWorkQueueFields.push(field);
            //   }
            // });
            this.fetchUserWorkQueueFields.sort((a, b) => a.displaySequence - b.displaySequence);
          } else {
            console.log('should come', this.filterFetchUserWorkQueueFields);
            this.fetchUserWorkQueueFields = this.fetchUserWorkQueueFields.filter(
              (field) => field.workQueueFieldName.toLowerCase() !== 'user name'
            );
            this.filterFetchUserWorkQueueFields = this.filterFetchUserWorkQueueFields.filter(
              (field) => field.workQueueFieldName.toLowerCase() !== 'user name'
            );
            console.log('should come', this.filterFetchUserWorkQueueFields);
          }

          console.log(this.fetchUserWorkQueueFields, 'this.fetchUserWorkQueueFieldsafetrap')
          this.loadingData = false;

          if (this.activeType === 'workStatus') {
            const value = data.body.workQueueDetailsDTO.filter((element: any) => element.workStatus === this.activeStatus);
            this.claimWorkQueuesList.workQueueDetailsDTO = value;
            this.tempWorkQueue = value;
          this.toRestoreData = value;
          } else {
            const value = data.body.workQueueDetailsDTO.filter(
              (element: any) => element.workStatus !== "CLAIM PENDED" && element.workStatus !== "ASSIGNED TO ISC"
            );
            this.claimWorkQueuesList.workQueueDetailsDTO = value;
            this.tempWorkQueue = value;
            this.toRestoreData = value;
          }
        },
        (error) => {
          // Error case
          this.loadingData = false;
          this.claimWorkQueuesList = []
          console.error('Error fetching claim work queues:', error);
          if (this.isManager === true) {
            this.fetchUserWorkQueueFields.push(...this.removedFields);
            this.fetchUserWorkQueueFields.sort((a, b) => a.displaySequence - b.displaySequence);
          } else {
            console.log('should else come');
            this.fetchUserWorkQueueFields = this.fetchUserWorkQueueFields.filter(
              (field) => field.workQueueFieldName.toLowerCase() !== 'user name'
            );
            this.filterFetchUserWorkQueueFields = this.filterFetchUserWorkQueueFields.filter(
              (field) => field.workQueueFieldName.toLowerCase() !== 'user name'
            );
            console.log('should else come', this.filterFetchUserWorkQueueFields);
          }

        }
      );
  }


  fetchUserWorkQueueFieldsByUserId() {
    this.workQueueService
      .fetchUserWorkQueueFieldsByUserId(Number(localStorage.getItem('userId')))
      .subscribe((data) => {

        this.toFetchDefaultWorkQueueFields();

        this.fetchUserWorkQueueFields = data.body;
        this.checkIsManager(Number(this.selectedManagerName))
        this.fetchUserWorkQueueFields.unshift({
          workQueueFieldId: '',
          workQueueFieldName: '',
          displaySequence: 0,
        });
        console.log(this.fetchUserWorkQueueFields, 'this.fetchUserWorkQueueFields');
        console.log(this.isManager, 'isManagerrasss')

        // Create a property to store the removed fields


        // Check if isManager is false and filter out the "User Name" field
        // if (!this.isManager) {
        //   const removedField = this.fetchUserWorkQueueFields.find(
        //     (field) => field.workQueueFieldName.toLowerCase() === 'user name'
        //   );

        //   if (removedField) {
        //     // Save the removed field into the 'removedFields' array
        //     this.removedFields.push(removedField);

        //     // Remove the "User Name" field from fetchUserWorkQueueFields
        //     this.fetchUserWorkQueueFields = this.fetchUserWorkQueueFields.filter(
        //       (field) => field.workQueueFieldName.toLowerCase() !== 'user name'
        //     );
        //   }
        // }
        if (!this.isManager) {
          const removedField = this.fetchUserWorkQueueFields.find(
            (field) => field.workQueueFieldName.toLowerCase() === 'user name'
          );

          // if (removedField) {
          //   // Save the removed field into the 'removedFields' array
          //   this.removedFields.push(removedField);

          //   // Remove the "User Name" field from fetchUserWorkQueueFields
          //   this.fetchUserWorkQueueFields = this.fetchUserWorkQueueFields.filter(
          //     (field) => field.workQueueFieldName.toLowerCase() !== 'user name'
          //   );
          // }

          // Filter out fields with empty 'workQueueFieldId', 'workQueueFieldName', or 'displaySequence' equal to 0
          this.filterFetchUserWorkQueueFields = this.fetchUserWorkQueueFields.filter(
            (field) => field.workQueueFieldId !== '' && field.workQueueFieldName !== '' && field.displaySequence !== 0
          );
          console.log(this.filterFetchUserWorkQueueFields, 'filterFetchUserWorkQueueFieldsif')
        } else {
          // If isManager is true, do not filter out the "User Name" field and only filter for non-empty fields
          this.filterFetchUserWorkQueueFields = this.fetchUserWorkQueueFields.filter(
            (field) => field.workQueueFieldId !== '' && field.workQueueFieldName !== '' && field.displaySequence !== 0
          );
        }




        console.log(this.fetchUserWorkQueueFields, 'this.fetchUserWorkQueueFieldsafter');
        //  this.getClaimWorkQueues();


        // console.log(this.fetchUserWorkQueueFields,'fetchUserWorkQueueFields')

        // this.filterFetchUserWorkQueueFields = this.fetchUserWorkQueueFields.filter(
        //   (field) => field.workQueueFieldId !== '' && field.workQueueFieldName !== '' && field.displaySequence !== 0
        // );
        // console.log(this.filterFetchUserWorkQueueFields,'coulns')
        this.activeRoute.params.subscribe(params => {
          this.activeType = params['type'];
          if (this.activeType == 'Aged Claims') {
            this.activeValue = params['value'];
            this.getAgedClaims();

          }
          else if (this.activeType == 'Priority Claims') {
            this.getPriorityClaims();

          } else if (this.activeType == 'Last Activity Age') {
            this.activeValue = params['value'];
            this.getLastActivityAge();

          } else if (this.activeType == 'Open Claims') {

            this.getClaimWorkQueues();
          } else if (this.activeType == 'workStatus') {
            this.activeStatus = params['value'];
            this.getClaimWorkQueues();
          }
          else {
            this.getClaimWorkQueues();
          }



        });

      });
  }


  transformToCamelCase(name: string): string {
    // Trim leading and trailing whitespace
    name = name.trim();

    const words = name.split(' ');

    // Handle special cases for specific headers
    switch (name.toLowerCase()) {
      case 'work status':
        return 'workStatus';
      case 'claim amount':
        return 'claimAmount';
      case 'reason code':
        return 'claimReasonCode';
      case 'claim number':
        return 'claimNumber';
      case 'trace date':
        return 'traceDate';
      case 'claim age':
        return 'claimAge';
      case 'last activity age':
        return 'lastActivityAge';
      case 'sell company':
        return 'sellingCompanyId';
      case 'customer #':
        return 'customerNumber';
      case 'customer name':
        return 'customerName';
      case 'state':
        return 'stateName';
      case 'end user':
        return 'endUserName';
      case 'inspection':
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

        //console.log(camelCaseName, 'convertedHeader');
        return camelCaseName;
    }
  }

public propertyNameMappings: Record<string, string> = {
  'User Name': 'userName',
  'Claim Amount': 'claimAmount',
  'Work Status': 'workStatus',
  'Claim Number': 'claimNumber',
  'Reason Code': 'claimReasonCode',
  'Claim Age': 'claimAge',
  'Last Activity Age': 'lastActivityAge',
  'Sell Company': 'sellingCompanyId',
  'Customer # ': 'customerNumber',
  'State': 'stateName',
  'End User': 'endUserName',
  'Inspection report': 'inspectionReportDate',
  'Global/National': 'globalorNational',
  'Trace Date': 'traceDate',
  'Customer Name': 'customerName'
};

  onSearch(propertyName: string, searchValue: string) {
    if (propertyName in this.propertyNameMappings) {
      propertyName = this.propertyNameMappings[propertyName];
    }
    this.tempSearchValue = searchValue
    this.tempPropertyValue = propertyName
    if (searchValue == '' ) {
      // Restore the original data
      // console.log(this.toRestoreData, 'this.toRestoreDatainitail')
      this.claimWorkQueuesList.workQueueDetailsDTO = this.toRestoreData;

    } else {
      if(this.sortTemp && this.sortTemp?.direction == ''){
        const filteredData = this.tempWorkQueue.filter((item: any) => {
          const itemValue = String(item[propertyName]);
          console.log(itemValue, 'itemValue');
          console.log(item[propertyName], 'item[propertyName]'); // Add this line for debugging
          return itemValue.toLowerCase().includes(searchValue.toLowerCase());
        });
        this.claimWorkQueuesList.workQueueDetailsDTO = filteredData;
      }else{
        const filteredData = this.toRestoreData.filter((item: any) => {
          const itemValue = String(item[propertyName]);
          console.log(itemValue, 'itemValue');
          console.log(item[propertyName], 'item[propertyName]'); // Add this line for debugging
          return itemValue.toLowerCase().includes(searchValue.toLowerCase());
        });
        this.claimWorkQueuesList.workQueueDetailsDTO = filteredData;
      }
      // Convert "Claim Amount" to "claimAmount"

      // Apply the search filter to your original data
      //  propertyName = 'claimAmount';
      console.log(propertyName, 'converted')
      // Apply the search filter to your original data
     
      // Log the final output after applying the search
      // console.log('Filtered Data:', filteredData);
     

      // console.log(this.toRestoreData, 'assinged')
    }
    if(this.sortTemp != undefined){
      this.claimWorkQueuesList.workQueueDetailsDTO = this.claimWorkQueuesList.workQueueDetailsDTO.sort((a: any, b: any) => {
        const isAsc = this.sortTemp.direction === 'asc';
        switch (this.sortTemp.active) {
          case 'User Name':
            return this.compare(a.userName, b.userName, isAsc);
          case 'Work Status':
            return this.compare(a.workStatus, b.workStatus, isAsc);
          case 'Claim Number':
            return this.compare(a.claimNumber, b.claimNumber, isAsc);
          case 'Claim Amount':
            return this.compare(a.claimAmount, b.claimAmount, isAsc);
  
          case 'Reason Code':
            return this.compare(a.claimReasonCode, b.claimReasonCode, isAsc);
          case 'Trace Date':
            return this.compare(a.traceDate, b.traceDate, isAsc);
  
          case 'Claim Age':
            return this.compare(a.claimAge, b.claimAge, isAsc);
          case 'Last Activity Age':
            return this.compare(a.lastActivityAge, b.lastActivityAge, isAsc);
          case 'Sell Company':
            return this.compare(a.sellingCompanyId, b.sellingCompanyId, isAsc);
          case 'Customer # ':
            return this.compare(a.customerNumber, b.customerNumber, isAsc);
          case 'Customer Name':
            return this.compare(a.customerName, b.customerName, isAsc);
          case 'State':
            return this.compare(a.stateName, b.stateName, isAsc);
          case 'End User':
            return this.compare(a.endUserName, b.endUserName, isAsc);
          case 'Inspection':
            return this.compare(a.inspectionReportDate, b.inspectionReportDate, isAsc);
          case 'Global/National':
            return this.compare(a.globalorNational, b.globalorNational, isAsc);
  
          default:
            return 0;
        }
      });
    }
   
  }






  toReSet() {
    this.workQueueService.deleteUserWorkQueueFields(Number(localStorage.getItem('userId'))).subscribe(data => {
      console.log(data)
      if (data.status === 200) {
        this.fetchUserWorkQueueFieldsByUserId();
        this.closeFieldChooserModal()

      }
    })
  }





  onPreviousPageClick() {
    if (this.claimWorkQueuesListCurrentPage > 1) {
      this.claimWorkQueuesListCurrentPage--;
    }
  }

  onNextPageClick() {
    const maxPage = Math.ceil(this.claimWorkQueuesList.workQueueDetailsDTO.length / this.itemsPerPage);
    if (this.claimWorkQueuesListCurrentPage < maxPage) {
      this.claimWorkQueuesListCurrentPage++;
    }
  }

  onPageNumberClick(page: number) {
    this.claimWorkQueuesListCurrentPage = page;
  }

  onItemsPerPageChange() {
    this.claimWorkQueuesListCurrentPage = 1;
  }

  getPageNumbers(): number[] {
    const maxPage = Math.ceil(this.claimWorkQueuesList.workQueueDetailsDTO?.length / this.itemsPerPage);
    return Array.from({ length: maxPage }, (_, i) => i + 1);
  }

  sortDataValue(sort: Sort) {
    const data = [...this.tempWorkQueue].slice();
    this.sortTemp = sort
    if (!sort.active || sort.direction === '') {
      this.claimWorkQueuesList.workQueueDetailsDTO = data;
      return;
    }

    if(this.tempSearchValue != undefined && this.tempPropertyValue != undefined){
      this.onSearch(this.tempPropertyValue,this.tempSearchValue)
    }else{
      this.claimWorkQueuesList.workQueueDetailsDTO = data.sort((a: any, b: any) => {
        const isAsc = sort.direction === 'asc';
        switch (sort.active) {
          case 'User Name':
            return this.compare(a.userName, b.userName, isAsc);
          case 'Work Status':
            return this.compare(a.workStatus, b.workStatus, isAsc);
          case 'Claim Number':
            return this.compare(a.claimNumber, b.claimNumber, isAsc);
          case 'Claim Amount':
            return this.compare(a.claimAmount, b.claimAmount, isAsc);
  
          case 'Reason Code':
            return this.compare(a.claimReasonCode, b.claimReasonCode, isAsc);
          case 'Trace Date':
            return this.compare(a.traceDate, b.traceDate, isAsc);
  
          case 'Claim Age':
            return this.compare(a.claimAge, b.claimAge, isAsc);
          case 'Last Activity Age':
            return this.compare(a.lastActivityAge, b.lastActivityAge, isAsc);
          case 'Sell Company':
            return this.compare(a.sellingCompanyId, b.sellingCompanyId, isAsc);
          case 'Customer # ':
            return this.compare(a.customerNumber, b.customerNumber, isAsc);
          case 'Customer Name':
            return this.compare(a.customerName, b.customerName, isAsc);
          case 'State':
            return this.compare(a.stateName, b.stateName, isAsc);
          case 'End User':
            return this.compare(a.endUserName, b.endUserName, isAsc);
          case 'Inspection':
            return this.compare(a.inspectionReportDate, b.inspectionReportDate, isAsc);
          case 'Global/National':
            return this.compare(a.globalorNational, b.globalorNational, isAsc);
  
          default:
            return 0;
        }
      });
    }
   
    }


  // Comparison function for sorting
  compare(a: string | number, b: string | number, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  resetTable(){
    const data = [...this.tempWorkQueue].slice();
    this.claimWorkQueuesList.workQueueDetailsDTO = data;
  }




  drop(event: CdkDragDrop<any[]>): void {
    moveItemInArray(this.filterFetchUserWorkQueueFields, event.previousIndex, event.currentIndex);
    this.updateDisplaySequence();
  }

  // deleteColumn(index: number): void {
  //   const deletedColumn = this.filterFetchUserWorkQueueFields.splice(index, 1)[0];
  //   this.deletedColumns.push(deletedColumn);
  //   this.updateDisplaySequence();
  // }
  deleteColumn(index: number): void {
    const deletedColumn = this.filterFetchUserWorkQueueFields.splice(index, 1)[0];
    this.deletedColumns.push(deletedColumn);
    this.unmatchedFields.push(deletedColumn); // Store in unmatchedFields
    this.updateDisplaySequence();
  }

  // openAddColumnDialog(): void {

  //   console.log(this.unmatchedFields,'unmatchedFields')

  //   if (this.deletedColumns.length > 0) {
  //     this.showAddColumnDialog = !this.showAddColumnDialog;
  //   }
  // }
  openAddColumnDialog(): void {

    this.enableOk = false;
    console.log(this.unmatchedFields, 'unmatchedFields');

    // Check if there are unmatchedFields to restore
    if (this.unmatchedFields.length > 0) {
      this.showAddColumnDialog = true;
      //this.showAddColumnDialog = !this.showAddColumnDialog;
    }
  }


  addColumn(): void {
    if (this.selectedColumn) {
      this.enableOk = true;
      const restoredColumn = this.unmatchedFields.find(column => column.workQueueFieldName === this.selectedColumn);
      if (restoredColumn) {
        this.filterFetchUserWorkQueueFields.push(restoredColumn);
        this.deletedColumns = this.deletedColumns.filter(column => column !== restoredColumn);
        this.unmatchedFields = this.unmatchedFields.filter(column => column !== restoredColumn);
        this.selectedColumn = undefined;
        this.showAddColumnDialog = false;
        this.updateDisplaySequence();
      }
    }
  }

  private updateDisplaySequence(): void {
    // Update display sequence based on the new order
    this.filterFetchUserWorkQueueFields.forEach((field, index) => {
      field.displaySequence = index + 1;
    });

    console.log(this.filterFetchUserWorkQueueFields, 'update sequence')
  }
  addUserWorkQueueFields() {

    const payload = {
      createdByUserId: Number(localStorage.getItem('userId')),
      modifiedByUserId: Number(localStorage.getItem('userId')),
      workQueueField: this.filterFetchUserWorkQueueFields.map(field => ({
        createdByUserId: Number(localStorage.getItem('userId')),
        modifiedByUserId: Number(localStorage.getItem('userId')),
        createdDateTime: "",
        modifiedDateTime: "",
        workQueueFieldId: field.workQueueFieldId,
        workQueueFieldName: field.workQueueFieldName,
        workQueueFieldDescription: "",
        displaySequence: field.displaySequence,
        displayByDefault: true,
        statusId: 0
      }))
    };
    console.log(payload, 'payload')
    this.workQueueService.addUserWorkQueueFields(payload).subscribe(
      (data) => {
        if (data && data.status === 200) {

          this.closeFieldChooserModal();
          this.fetchUserWorkQueueFieldsByUserId();
          //  this.getClaimWorkQueues();

        }
      },
      (error) => {
        // Handle error here if necessary
        console.error('Error:', error);
      }
    );

  }

  toFetchDefaultWorkQueueFields() {
    this.workQueueService.fetchWorkQueueFields().subscribe((data: HttpResponse<any>) => {
      this.defaultfetchWorkQueueFields = data.body; // Assuming you want to access the response body
      this.compareFields();
      console.log(this.defaultfetchWorkQueueFields, 'defaultfetchWorkQueueFields');
    });

  }
  compareFields(): void {
    // Find unmatched fields
    console.log('campare called')
    this.unmatchedFields = this.defaultfetchWorkQueueFields.filter(defaultField =>
      !this.fetchUserWorkQueueFields.some(userField => userField.workQueueFieldId === defaultField.workQueueFieldId)
    );
    console.log('campare called', this.unmatchedFields)
  }
  openFieldChooserModal() {
    const modelDiv = document.getElementById('FieldChooser');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }
  closeFieldChooserModal() {
    const modelDiv = document.getElementById('FieldChooser');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }


  checkIsManager(userId: number) {
    this.workQueueService.isManager(this.userId).subscribe(
      (data) => {
        console.log(data, 'mymanagerdata');

        if (data && data.status === 200) {
          this.isManager = data.body;

          console.log('isManager 300', this.isManager);
        } else if (data && data.status === 400) {
          this.isManager = false;
          console.log('isManager 400 error', this.isManager);
          console.warn('Response status is 400. Setting isManager to false.');
        }
      },
      (error) => {
        // Error case
        this.isManager = false;
        console.error('Error checking manager status:', this.isManager);
      }
    );


  }





}