import { Component,Input } from '@angular/core';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';


interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-view-export-rules',
  templateUrl: './view-export-rules.component.html',
  styleUrls: ['./view-export-rules.component.scss']
})
export class ViewExportRulesComponent {

  @Input() collapsed = false;
  @Input() screenWidth = 0;
  isSideNavCollapsed: boolean = true; 

  constructor(private bodyClassService: BodyClassService) {}

  onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed,this.screenWidth);
  }
}