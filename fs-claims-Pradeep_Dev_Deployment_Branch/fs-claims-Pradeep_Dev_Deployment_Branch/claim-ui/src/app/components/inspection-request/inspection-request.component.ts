import { Component,Input } from '@angular/core';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-inspection-request',
  templateUrl: './inspection-request.component.html',
  styleUrls: ['./inspection-request.component.scss']
})
export class InspectionRequestComponent {
  constructor(
    private bodyClassService: BodyClassService){}
    @Input() screenWidth = 0;
    public isSideNavCollapsed: boolean = true;
    public getBodyClass(): string {
      return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
    }

    public onToggleSideNav(data: SideNavToggle): void {
      this.screenWidth = data.screenWidth;
      this.isSideNavCollapsed = data.collapsed;
    }

    public getPostponeModal(){
      const modelDiv = document.getElementById('PostponeModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
    }
    public closeModal(id:string){
      const modelDiv = document.getElementById(id);
      if (modelDiv != null) {
        modelDiv.style.display = 'none';
      }}
}
