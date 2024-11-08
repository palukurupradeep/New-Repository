import { Component,Input, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { DashboardService } from 'src/app/ngrx/store/services/dashboard.service';

import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-admin-count-config',
  templateUrl: './admin-count-config.component.html',
  styleUrls: ['./admin-count-config.component.scss']
})
export class AdminCountConfigComponent implements OnInit {
  @Input() collapsed = false;
  @Input() screenWidth = 0;
  isSideNavCollapsed: boolean = true; 
  userId!: number;
  dashBoardWidgetsList: any[]=[];
  defaultWidgetList:any[]=[];
  originalDashBoardWidgetsList: any;

  constructor(private bodyClassService: BodyClassService,
    private dashBoardService: DashboardService,
    private snackBar: MatSnackBar,
    private router:Router, 
  private toasterService: ToasterService) {}

  onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed,this.screenWidth);
  }

  ngOnInit() {
    this.defaultWidgetList=[
      {
          "dashboardWidgetId": 1,
          "dashboardWidgetName": "Open Claims",
          "displaySequence": 1
      },
      {
          "dashboardWidgetId": 2,
          "dashboardWidgetName": "Aged Claims",
          "displaySequence": 2
      },
      {
          "dashboardWidgetId": 3,
          "dashboardWidgetName": "Priority Claims",
          "displaySequence": 3
      },
      {
          "dashboardWidgetId": 4,
          "dashboardWidgetName": "Debit Claims",
          "displaySequence": 4
      },
      {
          "dashboardWidgetId": 5,
          "dashboardWidgetName": "Last Activity Age",
          "displaySequence": 5
      },
      {
          "dashboardWidgetId": 6,
          "dashboardWidgetName": "Completed vs Received",
          "displaySequence": 6
      }
  ]
    console.log(localStorage.getItem("userId"),'userid')
    console.log('dashboard card inncard');
    this.userId =Number(localStorage.getItem("userId"));

     this.getUserDashboardWidgetsByUserId();
    
  } 
  
  getUserDashboardWidgetsByUserId() {
    let createdByUserId = 0;
    this.dashBoardService.fetchUserDashboardWidgetsByUserId(this.userId).subscribe(
      (data: any) => {
        // Initialize dashBoardWidgetsList with enabled widgets
        this.dashBoardWidgetsList = data.map((widget: any) => ({ ...widget, enabled: true, displayByDefault: true }));
  
        // Create a map for quick lookup of existing widget IDs
        const widgetIdMap = new Map<number, any>();
        this.dashBoardWidgetsList.forEach(widget => widgetIdMap.set(widget.dashboardWidgetId, widget));
  
        // Iterate through defaultWidgetList to find and push non-existing widgets
        this.defaultWidgetList.forEach(defaultWidget => {
          if (!widgetIdMap.has(defaultWidget.dashboardWidgetId)) {
            this.dashBoardWidgetsList.push({
              ...defaultWidget,
              enabled: false,
              displayByDefault: true
            });
          }
        });
  
        // Deep copy of dashBoardWidgetsList for original state
        this.originalDashBoardWidgetsList = JSON.parse(JSON.stringify(this.dashBoardWidgetsList));
  
        console.log(this.dashBoardWidgetsList, 'this.dashBoardWidgetsList');
        console.log(this.originalDashBoardWidgetsList, 'this.originalDashBoardWidgetsList');
      }
    );
  }
  resetDefaults() {
    // Deep copy of originalDashBoardWidgetsList
    this.dashBoardWidgetsList.forEach((widget, index) => {
      this.dashBoardWidgetsList[index].enabled = this.originalDashBoardWidgetsList[index].enabled;
    });
  
    console.log(this.dashBoardWidgetsList, 'reset dashBoardWidgetsList');
  }
  


  
 

  
  
  
 
    onApplyButtonClick() {
      const payload = {
        createdByUserId: this.userId,
        modifiedByUserId: this.userId,
        dashboardWidget: this.dashBoardWidgetsList
          .filter(widget => widget.enabled !== widget.initialStatus) // Filter out widgets with unchanged status
          .filter(widget => widget.enabled) // Filter out widgets with displayByDefault: false
          .map(widget => ({
            createdByUserId: this.userId,
            modifiedByUserId: this.userId,
            dashboardWidgetId: widget.dashboardWidgetId,
            dashboardWidgetName: widget.dashboardWidgetName,
            displaySequence: widget.displaySequence,
            displayByDefault: widget.enabled ? true : false,
          })),
      };
    
      console.log(payload);
    
   
    this.dashBoardService.addUserDashboardWidgets(payload).subscribe(
      (data) => {
    
       
      
        this.toasterService.showToast('success', 'User Prefernce Updated  Succesfully');
        this.router.navigate(['/dashboard'])
      },
      (error) => {
       
        this.toasterService.showToast('success', 'Failed To Update User Prefernce');
      
      }
    )
  }
  
  
}

