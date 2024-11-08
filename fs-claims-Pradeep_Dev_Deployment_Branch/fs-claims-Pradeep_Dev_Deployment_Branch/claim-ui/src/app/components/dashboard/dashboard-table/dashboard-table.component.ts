import { Component, Input } from '@angular/core';
import { DashboardService } from 'src/app/ngrx/store/services/dashboard.service';

@Component({
  selector: 'app-dashboard-table',
  templateUrl: './dashboard-table.component.html',
  styleUrls: ['./dashboard-table.component.scss']
})
export class DashboardTableComponent {
  // @Input('title')
  // tableTitle!: string;
  // @Input('tableData')
  // tableData!: Array<any>;
  @Input('title') tableTitle!: string;
  @Input('tableData') tableData!: Array<any>;
    constructor(private dashBoardservice:DashboardService) {
  }

  ngOnInit() {
    // this.fetchData();
    console.log('tableData Data => ', this.tableData);
    // this.userId =  Number(localStorage.getItem("userId"));
  }
  // fetchData() {
  //   if (this.apiType === 'customer') {
  //     this.dashBoardservice.getCustomerWatchListCountDetails().subscribe(data => {
  //       // Process data here
  //       console.log('Customer Watchlist Data:', data);
  //     });
  //   } else if (this.apiType === 'claims') {
  //    let userId =  Number(localStorage.getItem("userId"));
  //     this.dashBoardservice.getClaimWatchList(userId).subscribe(data => {
  //       // Process data here
  //       console.log('Claims Watchlist Data:', data);
  //     });
  //   }
  //   // Add more conditions for other API types if needed
  // }
}
