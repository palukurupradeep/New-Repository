import { Component,Input } from '@angular/core';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { InspectionsService } from 'src/app/ngrx/store/services/inspections.service';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-claim-vendor-list',
  templateUrl: './claim-vendor-list.component.html',
  styleUrls: ['./claim-vendor-list.component.scss']
})
export class ClaimVendorListComponent {
    @Input() screenWidth = 0;
    public isSideNavCollapsed: boolean = true;
    public inspectionStatus:any = [];
    public inspectonTableData: any = [];

    constructor(
      private bodyClassService: BodyClassService,
    private inspectionService:InspectionsService){}

    ngOnInit(){
      this.vendorListTableData();
    }
    public getBodyClass(): string {
      return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
    }

    public onToggleSideNav(data: SideNavToggle): void {
      this.screenWidth = data.screenWidth;
      this.isSideNavCollapsed = data.collapsed;
    }

    public getAllInspectionStatus(){
      this.inspectionService.getAllInspectionStatus().subscribe((data:any)=>{
        this.inspectionStatus = data.body
      })
    }

    public vendorListTableData(){
      this.inspectionService.getAllActiveInspectionRequestTypes().subscribe((data:any)=>{
        this.inspectonTableData = data.body
      }
    )}
}
