import { Component,Input } from '@angular/core';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-inspection-screen',
  templateUrl: './inspection-screen.component.html',
  styleUrls: ['./inspection-screen.component.scss']
})
export class InspectionScreenComponent {
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
}
