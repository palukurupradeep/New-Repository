import { Component,Input } from '@angular/core';
import { Router } from '@angular/router';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-claim-vendor-map',
  templateUrl: './claim-vendor-map.component.html',
  styleUrls: ['./claim-vendor-map.component.scss']
})
export class ClaimVendorMapComponent {
  @Input() screenWidth = 0;
  public isSideNavCollapsed: boolean = true;
  constructor(
    private bodyClassService: BodyClassService,
    private router:Router){}
   
    public getBodyClass(): string {
      return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
    }

    public onToggleSideNav(data: SideNavToggle): void {
      this.screenWidth = data.screenWidth;
      this.isSideNavCollapsed = data.collapsed;
    }

    public openVendorScreenView(){
      this.router.navigate(['/claim-vendor-detail']);
    }

    public openCancelModal(){
      const modelDiv = document.getElementById('cancel-vendor-map');
     if (modelDiv != null) {
     modelDiv.style.display = 'block';
     }
   }
   public closeModal(id:string){
     const modelDiv = document.getElementById(id);
     if (modelDiv != null) {
       modelDiv.style.display = 'none';
     }
   }
   public navToClaimDeatail(){
     this.router.navigate(['/claim-detail/:id']);
   }
}
