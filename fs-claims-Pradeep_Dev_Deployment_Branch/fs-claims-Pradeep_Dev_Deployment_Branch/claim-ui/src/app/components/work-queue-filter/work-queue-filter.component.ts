import { Component } from '@angular/core';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';

@Component({
  selector: 'app-work-queue-filter',
  templateUrl: './work-queue-filter.component.html',
  styleUrls: ['./work-queue-filter.component.scss']
})
export class WorkQueueFilterComponent {
  public userGroupList: any = [];

  public managerGroupList: any = [];

  public selectedGroup: any;

  public selectedManager: string = '';
 
  constructor(

    private userMapping: UserMappingService

  ){}
 
  ngOnInit(): void {

    this.getUserGroups();

    this.selectedManager = String(localStorage.getItem('userName'));

  }
 
  getUserGroups(){

    let userId = Number(localStorage.getItem('userId'));

    this.userMapping.findByUserId(userId).subscribe(

      (data: any) => { this.userGroupList = data.userGroups; }

    )

  }
 
  getManagerList(){

    this.userMapping.getManagerById(this.selectedGroup).subscribe(

      (data: any) => { this.managerGroupList = data; }

    )

  }

}


