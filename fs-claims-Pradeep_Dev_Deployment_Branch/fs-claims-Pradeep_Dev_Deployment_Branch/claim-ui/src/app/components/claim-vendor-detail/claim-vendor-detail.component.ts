import { Component,Input } from '@angular/core';
import { Router } from '@angular/router';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { InspectionsService } from 'src/app/ngrx/store/services/inspections.service';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-claim-vendor-detail',
  templateUrl: './claim-vendor-detail.component.html',
  styleUrls: ['./claim-vendor-detail.component.scss']
})
export class ClaimVendorDetailComponent {
  @Input() screenWidth = 0;
  public isSideNavCollapsed: boolean = true;
  public vendorDetails:any = [];
  public inspectionSeviceTypes:any[] = [];
  public inspectionSevices:any[] = [];
  public allCertifyingAgency:any[] = [];
  public vpaVendorDetail: any;
  public remitToAddress:any;

  constructor(
    private bodyClassService: BodyClassService,
    private inspectionService:InspectionsService,
    private router:Router){}

    ngOnInit(){
      this.getClaimVendorDetail();
    }
   


    public getBodyClass(): string {
      return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
    }

    public onToggleSideNav(data: SideNavToggle): void {
      this.screenWidth = data.screenWidth;
      this.isSideNavCollapsed = data.collapsed;
    }

    public getClaimVendorDetail(){
      this.inspectionService.getClaimVendorDetail('0000005').subscribe((data:any)=>{
        this.vendorDetails = data.body
        this.vpaVendorDetail = this.vendorDetails.claimVendorAddressResponseDTOS.find((address: any) => address.addressTypeCode === 'VPA');
        this.remitToAddress = this.vendorDetails.claimVendorAddressResponseDTOS.find((address: any) => address.addressTypeCode === 'VRT');
  this.getAllActiveInspectionServices();
  this.getAllInspectionServiceType();
      })
    }

    public getAllInspectionServiceType(){
      this.inspectionService.getAllInspectionServiceType().subscribe((data:any)=>{
        this.inspectionSeviceTypes = data.body
      })
    }

    public getAllActiveInspectionServices(){
      this.inspectionService.getAllActiveInspectionServices().subscribe((data:any)=>{
        this.inspectionSevices = data.body
      })
    }

    public getAllCertifyingAgency(){
      this.inspectionService.getAllCertifyingAgency().subscribe((data:any)=>{
        this.allCertifyingAgency = data.body
      })
    }

    public backToMapPage(){
      this.router.navigate(['/claim-vendor-map']);
    }

    public openVendorLists(){
      this.router.navigate(['/claim-vendor-list']);
    }

    public openCancelModal(){
       const modelDiv = document.getElementById('cancel-request-Inspection');
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
    public navToClaimDetail(){
      this.router.navigate(['/claim-detail/:id']);
    }

    public checkTypeValues(values:any):boolean{
      let value = this.vendorDetails.claimVendorServiceTypeResponseDTOS.some((item:any)=>item.inspectionServiceTypeCode == values.inspectionServiceTypeCode)
      return value
     
    }

    public checkServiceValues(item:any):boolean{
      let value = this.vendorDetails.claimVendorServiceResponseDTOS.some((ele:any)=>ele.inspectionServiceCode == item.inspectionServiceCode)
      return value
     
    }
    

}
