import { AfterViewInit, Component, EventEmitter, Input, OnChanges, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { DashboardService } from 'src/app/ngrx/store/services/dashboard.service';
@Component({
  selector: 'app-dashboard-card',
  templateUrl: './dashboard-card.component.html',
  styleUrls: ['./dashboard-card.component.scss']
})
export class DashboardCardComponent implements OnInit{
  public dashBoardWidgetsList: any[] = [];
  @Input() userIdEmitter: number =1;
userId : number = 1;
  totalOpenClaims: number = 0;
  // days: number =30;
  // activityClaimsDays:number=7
  agedDaysLenght: number = 0;
  agedDaysPercentage:string= ''
  priorityLenght:number =0 ;
  debtLength:number= 0;
  lastActivityLenght:number = 0;
  lastActivityPercentage:string=''
  recievedLenghtPercentage: string = ''
  recievedLenght:number= 0;
  receivedPercentage:string ='';
  receivedPrevDayAllClaims:string = '';
  receivedPrevDayStatusClaims:string = '';
  previousDayClaimsData:any={};
  public claimAge : number = 30;
  public lastActivity : number = 7;
  @Output() dashboardWidgetsData: EventEmitter<any> = new EventEmitter<any>();
  public agedClaimsSpinner:boolean = false;
  public lastActivitySpinner:boolean = false;
  public priorityClaimsSpinner:boolean = false;
  public previousDaysSpinner:boolean = false;
  public dashboardWidgetsSpinner:boolean = false;
  public debitSpinner: boolean = false
  constructor( 
    private dashBoardService:DashboardService,
    private router:Router)
  {
  }
  ngOnInit() {
  // console.log('dashboard card inncard');
   this.getUserDashboardWidgetsByUserId();
    this.userId =  Number(localStorage.getItem("userId"));
   this.getClaimWorkQueues();
} 

public getUserDashboardWidgetsByUserId(){
  this.agedClaimsSpinner = true;
  // console.log(localStorage.getItem('userId'), 'localstorageuseridincard');
  // let createdByUserId = Number(localStorage.getItem('userId'));
  let createdByUserId =  Number(localStorage.getItem('userId'));
 // let createdByUserId = this.userId ;
  this.dashBoardService.fetchUserDashboardWidgetsByUserId(createdByUserId).subscribe(
    (data:any) =>{
      this.dashBoardWidgetsList = data;

 
      // console.log(this.dashBoardWidgetsList,' this.dashBoardWidgetsList')
this.agedClaimsSpinner = false;
    }
  )
}
getClaimWorkQueues() {
  this.dashboardWidgetsSpinner = true;
  // this.userId =  Number(localStorage.getItem("userId"));
  let userId = Number(localStorage.getItem('selectedManagerId')) || Number(localStorage.getItem('userId'));
this.dashBoardService.getClaimWorkQueues(userId).subscribe(data => {
  this.totalOpenClaims = data.body.workQueueDetailsDTO.length;
  this.dashboardWidgetsSpinner = false;
  this.getAgedClaims();
  this.getPriorityClaims();
  this.lastActivityClaims();
  this.previousDayClaims();
  // console.log(this.totalOpenClaims,'this.totalOpenClaims')
})
}
getAgedClaims(){
  this.agedClaimsSpinner = true;
  // this.userId =  Number(localStorage.getItem("userId"));
  let userId = Number(localStorage.getItem('selectedManagerId')) || Number(localStorage.getItem('userId'));
  // console.log(this.days,'updatedays')
// this.agedDaysLenght = 7;
  //this.agedDaysPercentage =   this.calculatePercentage(this.agedDaysLenght,this.totalOpenClaims)
this.dashBoardService.getAgedClaims(userId,this.claimAge).subscribe(data => {
   this.agedDaysLenght = data.body.workQueueDetailsDTO.length;
   this.agedClaimsSpinner = false;
  // this.agedDaysLenght = 7;
this.agedDaysPercentage =   this.calculatePercentage(this.agedDaysLenght,this.totalOpenClaims)
 
})
}
getPriorityClaims(){
  this.priorityClaimsSpinner = true;
  // this.userId =  Number(localStorage.getItem("userId"));
  let userId = Number(localStorage.getItem('selectedManagerId')) || Number(localStorage.getItem('userId'));
  //this.priorityLenght = 7;
  this.dashBoardService.getPriorityClaims(userId).subscribe(data => {
    this.priorityLenght = data.body.workQueueDetailsDTO.length;
    this.priorityClaimsSpinner = false;

  })
}
lastActivityClaims(){
  this.lastActivitySpinner = true;
  // this.userId =  Number(localStorage.getItem("userId"));
  let userId = Number(localStorage.getItem('selectedManagerId')) || Number(localStorage.getItem('userId'));
  // console.log(this.lastActivity,'lastActivity')
  this.dashBoardService.lastActivityClaims(userId,this.lastActivity).subscribe(data => {
    this.lastActivityLenght = data.body.workQueueDetailsDTO.length;
    this.lastActivitySpinner = false;
    this.lastActivityPercentage =   this.calculatePercentage(this.lastActivityLenght,this.totalOpenClaims)
  })
}
previousDayClaims(){
  this.previousDaysSpinner = true;
  // this.userId =  Number(localStorage.getItem("userId"));
  let userId = Number(localStorage.getItem('selectedManagerId')) || Number(localStorage.getItem('userId'));
  this.dashBoardService.previousDayClaims(userId).subscribe(data => {
    // console.log(data,'daysclaimsdata')
    this.previousDayClaimsData = data.body;
    this.receivedPercentage =this.previousDayClaimsData.percentage;
    this.receivedPercentage =this.previousDayClaimsData.prevDayAllClaims
    this.receivedPercentage =this.previousDayClaimsData.prevDayStatusClaims
    if(data.body.workQueueDetailsDTO != undefined){
      this.recievedLenght = data.body.workQueueDetailsDTO.length;
    } else {
      this.recievedLenght = 0;
    }
    this.previousDaysSpinner = false;
    this.recievedLenghtPercentage =   this.calculatePercentage(this.recievedLenght,this.totalOpenClaims)
  })
 
}
calculatePercentage(days:number,totalOpenClaims:number): string {
  // Ensure totalOpenClaims is not zero to avoid division by zero
  if (this.totalOpenClaims !== 0) {
   //pasthe count of widgent ie lenght
    const percentage = (days / totalOpenClaims) * 100;
    return percentage.toFixed(2);
  } else {
    // Return 0 or handle it based on your specific requirements
    return '0.00';
  }
}
getWidgetValue(widget: any): number {
  switch (widget.dashboardWidgetId) {
    case 1:
      return this.totalOpenClaims;
    case 2:
      return this.agedDaysLenght;
    case 3:
      return this.priorityLenght;
    case 4:
      return this.debtLength;
    case 5:
      return this.lastActivityLenght;
    case 6:
      return this.recievedLenght;
    default:
      return 0;
  }
}
 
navigateToWorkqueues(type:string){
  if(type == 'Aged Claims' ){
    this.router.navigate(['/workQueue', type, this.claimAge]);
  }
  else if( type == 'Last Activity Age' ){
    this.router.navigate(['/workQueue', type, this.lastActivity]);
  }
  else if(type == 'Completed vs Received') {

  }
  else {
    this.router.navigate(['/workQueue', type]);
 
  }
}
 
}