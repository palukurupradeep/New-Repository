import { Component, OnInit } from '@angular/core';
import { userGroups } from 'src/app/interfaces/user-mapping.model';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';

@Component({
  selector: 'app-user-manager-grp',
  templateUrl: './user-manager-grp.component.html',
  styleUrls: ['./user-manager-grp.component.scss']
})
export class UserManagerGrpComponent implements OnInit {
  public groupList: userGroups[] = [];
  public selectedManagerName: number | null = null; 
  public selectedGroupName: number = 1;
  public managerList: any[] = [];
  userId!: number;
  drop: any;
  constructor(private userMappingService: UserMappingService,
    private bodyClassService:BodyClassService){}
  ngOnInit(): void {
    
    // this.bodyClassService.selectedGroupName$.subscribe((groupName) => {
    //   this.selectedGroupName = groupName;
    // });

    this.bodyClassService.selectedManagerName$.subscribe((managerName) => {
      this.selectedManagerName = managerName;
    });
    console.log(this.selectedManagerName,'this.selectedManagerName......')
    this.getGroupsList();
  }


  
  getGroupsList() {
    this.userMappingService.getAllUserGroups('assign').subscribe((data) => {
      // console.log("Group List =>"+JSON.stringify(data));
      this.groupList = data;
      this.userId = Number(localStorage.getItem('userId'));
    console.log(this.userId, 'Retrieved userId from localStorage');
  
    this.getManagersList();
    });
 
 
  }
  getManagersList() {
    console.log(this.selectedManagerName,'selectedManagerNamefrst');
    if (this.selectedGroupName) {
        this.userMappingService.getManagerById(Number(this.selectedGroupName)).subscribe((data) => {
            this.managerList = data;
          
            const loggedInUserManager = this.managerList.find(manager => manager.userId === this.userId);
            console.log(loggedInUserManager,'loggedInUserManager')
         
            if (loggedInUserManager) {
            
                this.selectedManagerName = loggedInUserManager.userId; // Set the selected manager to logged in user's manager
                console.log(this.selectedManagerName,'selectedManagerNamsecond');
              } else {
                this.selectedManagerName = null;// Clear selection if logged in user's manager is not found
            console.log(this.selectedManagerName ,'this.selectedManagerName ')
              }
        });
    } else {
        this.managerList = []; // If no group is selected, clear the manager list and selection
        this.selectedManagerName = null;
    }
}
onManagerSelected() {
  console.log(this.selectedManagerName,'selectedManagerName')
  this.selectedManagerName = this.selectedManagerName;
  this.bodyClassService.setSelectedGroupAndManager(
    this.selectedGroupName,
    this.selectedManagerName
  );
  this.bodyClassService.selectedManagerName$.subscribe((managerName) => {
    this.selectedManagerName = managerName;
  });
  console.log( this.selectedManagerName,' this.selectedManagerName')
}

}
