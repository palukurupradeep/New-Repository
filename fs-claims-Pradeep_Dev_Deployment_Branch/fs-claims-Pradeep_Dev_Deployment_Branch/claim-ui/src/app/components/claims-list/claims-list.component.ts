import { Component } from '@angular/core';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}
@Component({
  selector: 'app-claims-list',
  templateUrl: './claims-list.component.html',
  styleUrls: ['./claims-list.component.scss']
})
export class ClaimsListComponent {
  screenWidth: number = 0;
  isSideNavCollapsed: boolean = false;
  claims: any =[];

  constructor(private bodyClassService: BodyClassService,private claimService: ClaimDetailService) {
    
  
  }

  ngOnInit() {
    this.getClaims();
  }

  getClaims() {
    this.claimService.getClaims().subscribe(data => {
    console.log(data);
    this.claims = data;
    })

  }
  onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }
  getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed,this.screenWidth);
  }


}
