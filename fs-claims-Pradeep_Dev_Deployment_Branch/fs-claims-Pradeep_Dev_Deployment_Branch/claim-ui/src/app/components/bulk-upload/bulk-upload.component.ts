import { Component,Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { BulkUploadService } from 'src/app/ngrx/store/services/bulk-upload.service';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';
import { ToasterComponent } from '../toaster/toaster.component';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
import { Sort } from '@angular/material/sort';

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}


@Component({
  selector: 'app-bulk-upload',
  templateUrl: './bulk-upload.component.html',
  styleUrls: ['./bulk-upload.component.scss']
})
export class BulkUploadComponent  implements OnInit {

  @Input() collapsed = false;
  @Input() screenWidth = 0;
  isSideNavCollapsed: boolean = true; 
  batchStatus: any[]=[];
  selectedBatchStatusCode: string = "U";
  batchForm: FormGroup;
  batchData: any[]=[];
  sortbatchData: any[]=[];
  public batchStoreData: any[] = [];
  userId!: number;
  public groupList: any[] = [];
  public selectedGroupName: number = 1;
  public selectedManagerName: string = '';
  public managerList: any[] = [];
  public columnSortState: { [key: string]: 'asc' | 'desc' } = {};
  public tabSearchText: any = {};
 
  initialSelectedManagerName: string = '';
  public loadingData: boolean = false;
  constructor(private bodyClassService: BodyClassService,
    private router: Router,
    private bulkUploadService:  BulkUploadService,
    private fb: FormBuilder,
    private toasterService: ToasterService,
    private userMapping: UserMappingService,) {
      this.batchForm = this.fb.group({
        createdDate: [''],
        processedDate: ['']
      });
    }

    ngOnInit(): void {
      this.getBatchStatus()
      this.userId =Number(localStorage.getItem("userId"));
      this.getGroupsList();
    }

  onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed,this.screenWidth);
  }
  navigateToCreateBulkUpload(){
    this.router.navigate(['/create-bulk-upload']);
  }
  getBatchStatus(){
  this.bulkUploadService.getBatchStatus().subscribe(data =>{
    if(data.status === 200){
      this.batchStatus = data.body
      console.log( this.batchStatus,' this.batchStatus')
      this.onRetrieve()
    }
  
  })
  }

  
  onBatchStatusChange() {
    console.log("Selected Batch Status Code: ", this.selectedBatchStatusCode);
  }
  updateCreatedDate(event: any) {
    this.batchForm.get('createdDate')?.setValue(event);
    this.batchForm.get('processedDate')?.setValue(event);
  }
  
  onRetrieve() {
    // Retrieve values and log to console
    console.log('Form controls:', this.batchForm.value);
    const createdDate = this.batchForm.value.createdDate;
    const processedDate = this.batchForm.value.processedDate;
    console.log('Retrieved - Created Date:', createdDate, 'Processed Date:', processedDate);
    const retrivePayload = {
       "userGroupId": this.selectedGroupName,
      "batchStatusCode": this.selectedBatchStatusCode,
       "createdByUserId": this.userId,
       "processedByUserId": this.userId,
       "createdDateTime": this.formatDate(createdDate),
       "processedDateTime": this.formatDate(processedDate),
    }
    console.log(retrivePayload,'retrivePayload')
    this.bulkUploadService.fetchClaimBatchHeader(retrivePayload).subscribe(data =>{
      this.batchData = data.body
      this.sortbatchData = data.body.slice()
      this.batchStoreData = data.body
      this.loadingData = false;
      // console.log(data,'data')

    })
  }

  

  // Method to handle reset action
  onReset() {
  
    this.batchForm.reset();
    this.selectedBatchStatusCode = 'U';
    this.selectedGroupName = 1;
    this.selectedManagerName = this.initialSelectedManagerName;
    
  }

  // private formatDate(rawDate: string): string {
  //   // Assuming item.INVOICE_DATE is in 'DD/MM/YYYY' format
  //   const [day, month, year] = rawDate.split('/');
  //   const formattedDate = `${year}-${month}-${day}`;
  //   return formattedDate;
  // }
  private formatDate(rawDate: string | undefined): string {
    if (!rawDate) {
      return ''; // Return empty string if the date is empty
    }
  
    const isoDate = new Date(rawDate);
    const formattedDate = isoDate.toISOString().split('T')[0];
    return formattedDate;
  }

  getGroupsList() {
    this.userMapping.getAllUserGroups().subscribe((data) => {
      // console.log("Group List =>"+JSON.stringify(data));
      this.groupList = data;
      this.userId = Number(localStorage.getItem('userId'));
    console.log(this.userId, 'Retrieved userId from localStorage');
  
    this.getManagersList();
    });
      this.userId = Number(localStorage.getItem('userId'));
    console.log(this.userId, 'Retrieved userId from localStorage');
  
  }
 


  getManagersList() {
    console.log(this.selectedGroupName,'selectedGroupName');
    if (this.selectedGroupName) {
        this.userMapping.getManagerById(Number(this.selectedGroupName)).subscribe((data) => {
            this.managerList = data;
           // this.userId = Number(localStorage.getItem('userId'));
            // Check if managerList contains manager of logged in user
            const loggedInUserManager = this.managerList.find(manager => manager.userId === this.userId);
         
            if (loggedInUserManager) {
              console.log(loggedInUserManager,'loggedInUserManageronLoad')
                this.selectedManagerName = loggedInUserManager.userId; // Set the selected manager to logged in user's manager
                this.initialSelectedManagerName = loggedInUserManager.userId; 
               console.log(this.selectedManagerName,'this.selectedManagerNameonload')
               // this.iniitalSelectedManagerName = `${loggedInUserManager.firstName} ${loggedInUserManager.middleInitial} ${loggedInUserManager.lastName}`;
           
              //  this.onManagerSelected();
              } else {
                this.selectedManagerName = ''; // Clear selection if logged in user's manager is not found
            console.log(this.selectedManagerName ,'this.selectedManagerName ')
              }
        });
    } else {
        this.managerList = []; // If no group is selected, clear the manager list and selection
        this.selectedManagerName = '';
    }
}
 
  onManagerSelected() {
    this.userId = Number(this.selectedManagerName);
    //this.userId = Number(this.selectedManager.userId);
    localStorage.setItem('userId', (this.userId).toString())
    // console.log(this.selectedManager, 'selectedManager userId');
  
  


  }
  getSelectedGroupDescription(): string {
    const selectedGroup = this.groupList.find(group => group.userGroupId === this.selectedGroupName);
    return selectedGroup ? selectedGroup.userGroupDescription : '';
  }
  getSelectedManagerFullName(): string {
    const selectedManager = this.managerList.find(manager => manager.userId === this.selectedManagerName);
    if (selectedManager) {
      return `${selectedManager.firstName} ${selectedManager.middleInitial} ${selectedManager.lastName}`;
    }
    return '';
  }

  sortData(sort: Sort) {
    const data = this.batchData.slice(); // Use productCatalog for sorting
  
    if (!sort.active || sort.direction === '') {
      this.sortbatchData = data;
      return;
    }
  
    this.sortbatchData = data.sort((a:any, b:any) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'batch':
          return this.compare(a.claimBatchHeaderId, b.claimBatchHeaderId, isAsc);
        case 'batchDescription':
          return this.compare(a.batchDescription, b.batchDescription, isAsc);
        case 'department':
          return this.compare(a.userGroupName, b.userGroupName, isAsc);
          case 'customerRollUp':
            return this.compare(a.customerRollUp, b.customerRollUp, isAsc);  
            case 'deliverCreditMemo':
              return this.compare(a.deliverCreditMemo, b.deliverCreditMemo, isAsc);  
              case 'loadedBy':
                return this.compare(a.createdUserName, b.createdUserName, isAsc);  
                case 'dateTimeLoaded':
                  return this.compare(a.createdDateTime, b.createdDateTime, isAsc);  
                  case 'processedBy':
                    return this.compare(a.processedUserName, b.processedUserName, isAsc);  
                    case 'dateTimeProcessed':
                      return this.compare(a.processedDateTime, b.processedDateTime, isAsc);  
                      case 'batchStatus':
                        return this.compare(a.batchStatusCode, b.batchStatusCode, isAsc);  
        default:
          return 0;
      }
    });
  }

  compare(a: string | number, b: string | number, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
    }

  public onTabSearch() {
    if (!this.tabSearchText.batch && !this.tabSearchText.batchDescription && !this.tabSearchText.department
      && !this.tabSearchText.customerRollUp && !this.tabSearchText.deliverCreditMemo && !this.tabSearchText.loadedBy
      && !this.tabSearchText.dateTimeLoaded && !this.tabSearchText.processedBy && !this.tabSearchText.dateTimeProcessed
    ) {
      this.sortbatchData = this.batchStoreData;
    } else {
      this.sortbatchData = this.batchStoreData.filter((user: any) => {
        let matches = true;
  
        if (this.tabSearchText.batch) {
          matches = matches && user.claimBatchHeaderId != null && 
          user.claimBatchHeaderId.toString().includes(this.tabSearchText.batch.toString());
        }
  
        if (this.tabSearchText.batchDescription) {
          matches = matches && user.batchDescription != null && 
          user.batchDescription.toLowerCase().includes(this.tabSearchText.batchDescription.toLowerCase());
        }
  
        if (this.tabSearchText.department) {
          matches = matches && user.userGroupName != null &&
           user.userGroupName.toLowerCase().includes(this.tabSearchText.department.toLowerCase());
        }
  
        if (this.tabSearchText.customerRollUp) {
          matches = matches && user.customerRollUp != null &&
           user.customerRollUp.includes(this.tabSearchText.customerRollUp);
        }
  
        if (this.tabSearchText.deliverCreditMemo) {
          matches = matches && user.deliverCreditMemo != null 
          && user.deliverCreditMemo.includes(this.tabSearchText.deliverCreditMemo);
        }
  
        if (this.tabSearchText.loadedBy) {
          matches = matches && user.createdByUserId != null &&
           user.createdByUserId.toString().includes(this.tabSearchText.loadedBy);
        }
  
        if (this.tabSearchText.dateTimeLoaded) {
          matches = matches && user.createdDateTime != null && 
          user.createdDateTime.includes(this.tabSearchText.dateTimeLoaded);
        }
  
        if (this.tabSearchText.processedBy) {
          matches = matches && user.createdByUserId != null && 
          user.createdByUserId.toString().includes(this.tabSearchText.processedBy);
        }
  
        if (this.tabSearchText.dateTimeProcessed) {
          matches = matches && user.processedDateTime != null && 
          user.processedDateTime.includes(this.tabSearchText.dateTimeProcessed);
        }
  
        return matches;
      });
    }
  }

}

