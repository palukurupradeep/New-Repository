import { Component, Input } from '@angular/core';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-search-invoice-history',
  templateUrl: './search-invoice-history.component.html',
  styleUrls: ['./search-invoice-history.component.scss']
})
export class SearchInvoiceHistoryComponent {
  @Input() screenWidth = 0;
  public isSideNavCollapsed: boolean = true;

  constructor(
    private bodyClassService: BodyClassService) { }

    public getBodyClass(): string {
      return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
    }
  
    public onToggleSideNav(data: SideNavToggle): void {
      this.screenWidth = data.screenWidth;
      this.isSideNavCollapsed = data.collapsed;
    }
}
