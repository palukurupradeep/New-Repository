import { Component, ComponentFactoryResolver, EventEmitter, Input, Output, ViewChild, ViewContainerRef } from '@angular/core';
import { MsalService } from '@azure/msal-angular';
import { ApexAxisChartSeries, ApexChart, ApexDataLabels, ApexPlotOptions, ApexXAxis, ChartComponent } from 'ng-apexcharts';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { DashboardService } from 'src/app/ngrx/store/services/dashboard.service';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';
import { WorkqueueService } from 'src/app/ngrx/store/services/workqueue.service';
import { TableComponent } from 'src/app/shared/table/table.component';
import { DashboardCardComponent } from './dashboard-card/dashboard-card.component';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { userGroups } from 'src/app/interfaces/user-mapping.model';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
import { AuthService } from 'src/app/ngrx/store/services/auth.service';
import { Sort } from '@angular/material/sort';
import { ClaimService } from 'src/app/ngrx/store/services/claim.service';
import { switchMap } from 'rxjs';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}
// Define an interface for the sorting state
interface ColumnSortState {
  [key: string]: 'asc' | 'desc';
}

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart;
  dataLabels: ApexDataLabels;
  plotOptions: ApexPlotOptions;
  xaxis: ApexXAxis;
};
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {
  @ViewChild('tableContainer', { read: ViewContainerRef })
  tableContainer!: ViewContainerRef;
  @Input() collapsed = false;
  @Input() screenWidth = 0;
  @Output() userIdEmitter = new EventEmitter<number>();
  isSideNavCollapsed: boolean = true;
  name: any;
  username: any;
  userData: any = [];
  watchListClaimsTitle: string = 'Watchlist by Claims';
  watchListCustomerTitle: string = 'Watchlist by Customer';
  OverdueTitle: string = 'overdue';
  claimsDueTitle: string = 'Claims Due';
  public userGroupList: any = [];
  public managerGroupList: any = [];
  public selectedGroup!: number;
  public selectedManager: any = {};
  userId!: number;
  @ViewChild("chart") chart!: ChartComponent;
  public chartOptions!: Partial<ChartOptions> | any;
  public chartData: any = [];
  public dashboardchartsData: any = []
  public progressCountdata: any = []
  public workStatusCounts: { [key: string]: number } = {};
  public watchListArray: any = [];
  public sortwatchListArray: any = [];
  public customerListArray: any = [];
  public sortcustomerListArray: any = [];
  public claimsDueListArray: any = [];
  public sortclaimsDueListArray: any = [];
  public overdueClaimListArray: any = [];
  public sortoverdueClaimListArray: any = [];
  @ViewChild(DashboardCardComponent) child!: DashboardCardComponent;
  public tempClaimId: number = 0;
  public tempCustomer: string = '';
  public columnSortStates: { [key: string]: ColumnSortState } = {
    watchList: {},
    customerList: {},
    claimsDueList: {},
    overdueList: {},
  };
  public  inputRespData = {
    claimDuesArray: this.claimsDueListArray,
    overdueList: this.overdueClaimListArray,
    recentClaimsList: this.watchListArray,
    upList: this.customerListArray

  }
  public groupList: userGroups[] = [];
  public selectedGroupName: number = 1;
  public selectedManagerName: string = '';
  public managerList: any[] = [];
  userIdSet: boolean = false;
  public claimsSpinner: boolean = true;
  public customerSpinner: boolean = true;
  public overDueSpinner: boolean = true;
  public dueSpinner: boolean = true;
  public selectedManagerId: any;
public claimCount:number=0;
 


  constructor(private componentFactoryResolver: ComponentFactoryResolver, private bodyClassService: BodyClassService, dashBoard: DashboardService, private msalService: MsalService,
    private userMapping: UserMappingService,
    private dashBoardService: DashboardService,
    private workQueueService: WorkqueueService,
    private router: Router,
    private toasterService: ToasterService,
    private userMappingService: UserMappingService,
    private authService:AuthService,
    private claimService:ClaimService
  ) {
    // this.hostName = dashBoard.hostURL
    //  this.dashboardChart();

  }


  ngOnInit(): void {
    const activeAccount = this.msalService.instance.getActiveAccount();
    if (activeAccount) {
      this.name = activeAccount.name;
      this.username = activeAccount.username;
      localStorage.setItem('userName', this.name);
      console.log('Active account:', activeAccount);
      console.log('activeAccount.idTokenClaims.oid', activeAccount?.idTokenClaims?.oid)
      if(activeAccount?.idTokenClaims?.oid){
      this.userMapping.findByUser('ObjectId',activeAccount.idTokenClaims.oid).subscribe(
        (data) => {
                  this.userData = data;
                   localStorage.setItem('userId', this.userData[0].userId);
          localStorage.setItem('selectedManagerId', this.userData[0].userId);
          console.log(localStorage.getItem('userId'), 'localstorageuserid');
          this.claimService.getClaimCount(this.userData[0].activeDirectoryId).subscribe(data=>{
             this.claimCount = data.body;
           
          })
          this.userMappingService.fetchRolesAndPermissionByUserId(this.userData[0].userId).subscribe(data => {
          const userPerData = data;
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
          this.userIdSet = true; 
          console.log(this.userIdSet,'this.userIdSet')
        this.getGroupsList();
      }   );
    }
    } 
      // console.log('No active account');
    //   else {
    //     localStorage.setItem('userId', '3104');
    //     console.log('No active account');
    //     this.userIdSet = true; 
    //     // console.log(this.userIdSet,'this.userIdSet')
    //     this.getGroupsList();
      
    // }
    //this.getUserGroups();
    //this.getGroupsList();

    // this.getWatchListData();
    // this.getcustomerWatchList();
    // this.claimsDue();
    // this.overdueClaim();

  }

  getGroupsList() {
    this.userMapping.getAllUserGroups().subscribe((data) => {
      // console.log("Group List =>"+JSON.stringify(data));
      this.groupList = data;
      this.userId = Number(localStorage.getItem('userId'));
    console.log(this.userId, 'Retrieved userId from localStorage');
    setTimeout(() => {
      this.userIdEmitter.emit(this.userId);
    });
    this.getManagersList();
    });
      this.userId = Number(localStorage.getItem('userId'));
    console.log(this.userId, 'Retrieved userId from localStorage');
    setTimeout(() => {
      this.userIdEmitter.emit(this.userId);
    });
 
  }
 
  getManagersList(id:number = 0) {
    console.log(this.selectedGroupName,'selectedGroupName');
    if (this.selectedGroupName) {
        this.userMapping.getManagerById(Number(this.selectedGroupName)).subscribe((data) => {
            this.managerList = data;
           // this.userId = Number(localStorage.getItem('userId'));
            // Check if managerList contains manager of logged in user
            const loggedInUserManager = this.managerList.find(manager => manager.userId === this.userId);
            localStorage.setItem('selectedGroupName', this.selectedGroupName.toString());
            if (loggedInUserManager) {
            
                this.selectedManagerName = loggedInUserManager.userId; // Set the selected manager to logged in user's manager
                localStorage.setItem('selectedManagerId', this.userId.toString());
                           this.getWatchListData();
                this.getcustomerWatchList();
                this.dashboardChart();
                this.claimsDue();
                this.overdueClaim();
              //  this.onManagerSelected();
              if(id != 0){
                setTimeout(() => {
                  this.child.getUserDashboardWidgetsByUserId();
                  this.child.getAgedClaims();
                  this.child.getClaimWorkQueues();
                  this.child.getPriorityClaims();
                }, 1000);
              }
              
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
 
  // onManagerSelected() {
  //   this.userId = Number(this.selectedManagerName);
  //   //this.userId = Number(this.selectedManager.userId);
  //   localStorage.setItem('userId', (this.userId).toString())
  //   // console.log(this.selectedManager, 'selectedManager userId');
  //   this.getWatchListData();
  //   this.getcustomerWatchList();
  //   this.dashboardChart();
  //   this.claimsDue();
  //   this.overdueClaim();
  //   setTimeout(() => {
  //     this.child.getUserDashboardWidgetsByUserId();
  //     this.child.getAgedClaims();
  //     this.child.getClaimWorkQueues();
  //     this.child.getPriorityClaims();
  //   }, 1000);


  // }

  onManagerSelected() {
    this.selectedManagerId = Number(this.selectedManagerName);
    localStorage.setItem('selectedManagerId', this.selectedManagerId.toString());
    console.log(this.selectedManagerId, 'Selected Manager User ID');
    console.log('Stored Selected Manager ID:', localStorage.getItem('selectedManagerId'));
    // Use selectedManagerId for further logic instead of userId
    this.userMappingService.findByUserId(Number(this.selectedManagerName)).pipe(
      switchMap(userDetails => {
        // Assign activeDirectoryId and proceed with fetching claim count
        const activeDirectoryId = userDetails.activeDirectoryId;
        return this.claimService.getClaimCount(activeDirectoryId);
      })
    ).subscribe({
      next: (data) => {
        this.claimCount = data.body;
      },
      error: (err) => {
        console.error("Error fetching claim count:", err);
      }
    });
    this.getWatchListData();
    this.getcustomerWatchList();
    this.dashboardChart();
    this.claimsDue();
    this.overdueClaim();
    setTimeout(() => {
      this.child.getUserDashboardWidgetsByUserId();
      this.child.getAgedClaims();
      this.child.getClaimWorkQueues();
      this.child.getPriorityClaims();
    }, 1000);
  }

 


  getWatchListData() {
    // let userId = Number(localStorage.getItem("userId"));
    let managerId = Number(localStorage.getItem('selectedManagerId')) || Number(localStorage.getItem('userId'));
    this.claimsSpinner = true;
    this.dashBoardService.getClaimWatchList(managerId).subscribe(data => {
      this.watchListArray = [];
      this.sortwatchListArray = [];
      data.forEach((element: any) => {
        let object = { days: "", claimNumber: "", customerNumber: "", customerName: "", workStatus: "", claimId: "" };
        object.days = element.days;
        object.claimId = element.claimId;
        object.claimNumber = element.claimNumber;
        object.customerNumber = element.customerNumber;
        object.customerName = element.customerName;
        // object.count = element.count;
        object.workStatus = element.workStatus;

        this.watchListArray.push(object);
        this.sortwatchListArray.push(object);
      });
      this.claimsSpinner = false;
    });

  }



  getcustomerWatchList() {
    let managerId = Number(localStorage.getItem('selectedManagerId')) || Number(localStorage.getItem('userId'));
    this.customerSpinner = true;
    this.dashBoardService.getCustomerWatchListCountDetails(managerId).subscribe(data => {
      this.customerListArray = [];
      this.sortcustomerListArray = [];
      data.forEach((element: any) => {
        let object = { customerNumber: "", customerName: "", claimcount: "" };
        object.customerNumber = element.customerNumber;
        object.customerName = element.customerName;
        object.claimcount = element.count;
        this.customerListArray.push(object);
        this.sortcustomerListArray.push(object);
      });
      this.customerSpinner = false;
    });
  }

  claimsDue() {
    let managerId = Number(localStorage.getItem('selectedManagerId')) || Number(localStorage.getItem('userId'));
    this.dueSpinner = true;
    this.dashBoardService.claimsDue(managerId).subscribe(data => {
      this.claimsDueListArray = [];
      this.sortclaimsDueListArray = [];
      data.forEach((element: any) => {
        let object = { days: "", claimNumber: "", customerNumber: "", customerName: "", workStatus: "" };
        object.days = element.days;
        object.claimNumber = element.claimNumber;
        object.customerNumber = element.customerNumber;
        object.customerName = element.customerName;
        object.workStatus = element.workStatus;
        this.claimsDueListArray.push(object);
        this.sortclaimsDueListArray.push(object);
      });
      this.dueSpinner = false;
    });
  }

  overdueClaim() {
    let managerId = Number(localStorage.getItem('selectedManagerId')) || Number(localStorage.getItem('userId'));
    this.overDueSpinner = true;
    this.dashBoardService.overdueClaim(managerId).subscribe(data => {
      this.overdueClaimListArray = [];
      this.sortoverdueClaimListArray = [];
      data.forEach((element: any) => {
        let object = { days: "", claimNumber: "", customerNumber: "", customerName: "", workStatus: "" };
        object.days = element.days;
        object.claimNumber = element.claimNumber;
        object.customerNumber = element.customerNumber;
        object.customerName = element.customerName;
        object.workStatus = element.workStatus;
        this.overdueClaimListArray.push(object);
        this.sortoverdueClaimListArray.push(object);
      });
      this.overDueSpinner = false;
      console.log(this.overdueClaimListArray,'overdueClaimListArray')
    });
   
    
  }

  deleteCustomerWatchlist(customerNumber: string){
    this.tempCustomer = customerNumber;
  }
  
  deleteCustomer() {
    // const userId = Number(localStorage.getItem('userId'));
    let userId = Number(localStorage.getItem('selectedManagerId')) || Number(localStorage.getItem('userId'));
    this.dashBoardService.deleteCustomerWatchList(userId, this.tempCustomer).subscribe(() => {

      this.customerListArray = this.customerListArray.filter((item: any) =>
        item.customerNumber !== this.tempCustomer);
      this.toasterService.showToast('success', 'Customer Removed from Watchlist Successfully');
      const modelDiv = document.getElementById('deleteCustomerModal');
      if (modelDiv != null) {
        modelDiv.style.display = 'none';
      }
    }, error => {
      // console.error('Error deleting customer:', error);
      
    });
  }

  
  deleteClaimWatchList(claimId: number){
    this.tempClaimId = claimId;
  }

  deleteClaim() {
    // const userId = Number(localStorage.getItem('userId'));
    let userId =  Number(localStorage.getItem('userId'));
    let managerId = Number(localStorage.getItem('selectedManagerId'));

    if(userId === managerId) {
      this.dashBoardService.deleteClaimWatchList(userId, this.tempClaimId).subscribe(() => {

        this.watchListArray = this.watchListArray.filter((item: any) => item.claimId !== this.tempClaimId);
        this.toasterService.showToast('success', 'Claim Deleted from Watchlist Successfully');
        const modelDiv = document.getElementById('deleteModal');
        if (modelDiv != null) {
          modelDiv.style.display = 'none';
        }
      }, error => {
        // console.error('Error deleting claim:', error);
  
      });
    }
    else {
      this.toasterService.showToast('error', 'You can not delete others watchclaim details');
      const modelDiv = document.getElementById('deleteModal');
      if (modelDiv != null) {
        modelDiv.style.display = 'none';
      }
    }
  
  }
  //   removeClaimRecord(index: number){
  // this.watchListArray.splice(index, 1);
  //   }
  //   removeCust(i:number){
  // this.customerListArray.splice(i, 1);
  //   }




  onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }
  getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
  }

 
  dashboardChart() {
    let managerId = Number(localStorage.getItem('selectedManagerId')) || Number(localStorage.getItem('userId'));
    // console.log(localStorage.getItem('userId'), 'localstorageuserincompoennt');
    // let userId = Number(localStorage.getItem('userId'));
    this.workQueueService.getClaimWorkQueues(managerId).subscribe((data: any) => {
      this.chartData = data.body.workQueueDetailsDTO;
      // Reset workStatusCounts before counting
      this.workStatusCounts = {};
      // Count occurrences of each work status
      this.chartData.forEach((element: any) => {
        const workStatus = element.workStatus;
        this.workStatusCounts[workStatus] = (this.workStatusCounts[workStatus] || 0) + 1;
      });
      // Prepare x-axis categories and series data
      const xCategories: any[] = [];
      const seriesData: any[] = [];
      for (const workStatus in this.workStatusCounts) {
        if (this.workStatusCounts.hasOwnProperty(workStatus)) {
          xCategories.push(workStatus);
          seriesData.push(this.workStatusCounts[workStatus]);
        }
      }
      // Set chart options
      const chartOptions = {
        series: [
          {
            name: 'Count',
            data: seriesData
          }
        ],
        chart: {
          type: 'bar',
          height: 350,
          toolbar: {
            show: false // Hide the toolbar
          },
          events: {
            dataPointSelection: (event: any, chartContext: any, config: any) => {
              this.router.navigate(['/workQueue', 'workStatus', xCategories[config.dataPointIndex]]);
            }
          }
        },
        plotOptions: {
          bar: {
            horizontal: true
          }
        },
        dataLabels: {
          enabled: false
        },
        xaxis: {
          categories: xCategories
        },
        yaxis: {
          min: 0, // Starting value on the Y-axis
        },
        tooltip: {
          enabled: true,
          y: {
            formatter: function (val: any) {
              return val; // Keep Y-axis tooltip as it is
            }
          },
          x: {
            formatter: function (val: any) {
              return val; // Display only the work status in the X-axis tooltip
            }
          }
        }
      };
      this.chartOptions = chartOptions;
    });
  }

  onProgressBarClick(event: any) {
    const clickedWorkStatus = event.name.split(' ')[0];
    console.log("clickedWorkStatus -- ", clickedWorkStatus);
    // this.router.navigate(['/workqueue'], { queryParams: { workStatus: clickedWorkStatus } });
    this.router.navigate(['/workQueue', clickedWorkStatus]);
  }

 
  chartDataAvailable(): boolean {
    if (this.chartOptions && this.chartOptions.series && this.chartOptions.series.length > 0) {
        return true;
    } else {
        return false;
    }

    
}


sortData(sort: Sort) {
  const data = this.watchListArray.slice(); // Use productCatalog for sorting

  if (!sort.active || sort.direction === '') {
    this.sortwatchListArray = data;
    return;
  }

  this.sortwatchListArray = data.sort((a:any, b:any) => {
    const isAsc = sort.direction === 'asc';
    switch (sort.active) {
      case 'days':
        return this.compare(a.days, b.days, isAsc);
      case 'claim':
        return this.compare(a.claimNumber, b.claimNumber, isAsc);
      case 'customer':
        return this.compare(a.customerNumber, b.customerNumber, isAsc);
        case 'customerName':
          return this.compare(a.customerName, b.customerName, isAsc);  
          case 'workStatus':
            return this.compare(a.workStatus, b.workStatus, isAsc);  
      default:
        return 0;
    }
  });
}
sortCustomerData(sort: Sort) {
  const data = this.customerListArray.slice(); // Use productCatalog for sorting

  if (!sort.active || sort.direction === '') {
    this.sortcustomerListArray = data;
    return;
  }

  this.sortcustomerListArray = data.sort((a:any, b:any) => {
    const isAsc = sort.direction === 'asc';
    switch (sort.active) {
      case 'customerNumber':
        return this.compare(a.customerNumber, b.customerNumber, isAsc);
        case 'customerName':
          return this.compare(a.customerName, b.customerName, isAsc);  
          case 'claimCount':
            return this.compare(a.claimcount, b.claimcount, isAsc);  
      default:
        return 0;
    }
  });
}

sortClaimsData(sort: Sort) {
  const data = this.claimsDueListArray.slice(); // Use productCatalog for sorting

  if (!sort.active || sort.direction === '') {
    this.sortclaimsDueListArray = data;
    return;
  }

  this.sortclaimsDueListArray = data.sort((a:any, b:any) => {
    const isAsc = sort.direction === 'asc';
    switch (sort.active) {
      case 'days':
        return this.compare(a.days, b.days, isAsc);
      case 'claim':
        return this.compare(a.claimNumber, b.claimNumber, isAsc);
      case 'customer':
        return this.compare(a.customerNumber, b.customerNumber, isAsc);
        case 'customerName':
          return this.compare(a.customerName, b.customerName, isAsc);  
          case 'workStatus':
            return this.compare(a.workStatus, b.workStatus, isAsc);  
      default:
        return 0;
    }
  });
}

sortOverDueData(sort: Sort) {
  const data = this.overdueClaimListArray.slice(); // Use productCatalog for sorting

  if (!sort.active || sort.direction === '') {
    this.sortoverdueClaimListArray = data;
    return;
  }

  this.sortoverdueClaimListArray = data.sort((a:any, b:any) => {
    const isAsc = sort.direction === 'asc';
    switch (sort.active) {
      case 'days':
        return this.compare(a.days, b.days, isAsc);
      case 'claim':
        return this.compare(a.claimNumber, b.claimNumber, isAsc);
      case 'customer':
        return this.compare(a.customerNumber, b.customerNumber, isAsc);
        case 'customerName':
          return this.compare(a.customerName, b.customerName, isAsc);  
          case 'workStatus':
            return this.compare(a.workStatus, b.workStatus, isAsc);  
      default:
        return 0;
    }
  });
}


// Comparison function for sorting
 compare(a: string | number, b: string | number, isAsc: boolean) {
return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}

 public navigateToClaimDetail(claimNumber: string) {
  this.router.navigate(['/claim-detail', claimNumber]).then(() => {
      window.scrollTo(0, 0);
  });
}



}