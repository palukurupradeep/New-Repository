import { Component, Input } from '@angular/core';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { InspectionsService } from 'src/app/ngrx/store/services/inspections.service';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}


@Component({
  selector: 'app-claim-vendor-profile',
  templateUrl: './claim-vendor-profile.component.html',
  styleUrls: ['./claim-vendor-profile.component.scss']
})
export class ClaimVendorProfileComponent {
  @Input() screenWidth = 0;
  public isSideNavCollapsed: boolean = true;
  public inspectionSeviceTypes:any[] = [];
  public inspectionSevices:any[] = [];
  public inspectonTableData: any = [];
  public allCertifyingAgency:any[] = [];



  constructor(
    private bodyClassService: BodyClassService,
    private inspectionService:InspectionsService,
    private toasterService: ToasterService) { }

    ngOnInit(){
      this.getAllInspectionServiceType();
      this.getAllActiveInspectionServices();

    }
 
  public getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
  }

  public onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  public closeModal(id: string) {
    const modelDiv = document.getElementById(id);
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }
  public opencertificateModal() {
    const modelDiv = document.getElementById('certificateModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
    this.vendorListTableData();
    this.getAllCertifyingAgency();
  }

  public openModal(){
    const modelDiv = document.getElementById('vendorModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
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
  public vendorListTableData(){
    this.inspectionService.getAllActiveInspectionRequestTypes().subscribe((data:any)=>{
      this.inspectonTableData = data.body
    }
  )}

  public getAllCertifyingAgency(){
    this.inspectionService.getAllCertifyingAgency().subscribe((data:any)=>{
      this.allCertifyingAgency = data.body
    })
  }

  public addVendorDetails(){
    const addPayload = {
      "createdByUserId": Number(localStorage.getItem('userId')),
      "modifiedByUserId": Number(localStorage.getItem('userId')),
      "createdDate": "2024-09-12T05:30:56.480Z",
      "claimVendorId": 0,
      "federalId": "string",
      "psVendorId": "string",
      "psVendorLocation": "string",
      "legalName": "string",
      "dbaName": "string",
      "primaryContact": "string",
      "secondaryContact": "string",
      "w9ReceivedDate": "string",
      "psVendorFormDate": "string",
      "claimsProfileFormDate": "string",
      "profileReviewDate": "string",
      "backgroundCheckDate": "string",
      "latitude": 0,
      "longitude": 0,
      "phoneDialCodeId": 0,
      "phoneNumber": "string",
      "phoneExtension": "string",
      "cellDialCodeId": 0,
      "cellNumber": "string",
      "emailAddress": "string",
      "isStatus": true,
      "addClaimVendorAddressDTOS": [
        {
          "vendorAddressId": 0,
          "addressTypeCode": "string",
          "fullName": "string",
          "addressLine1": "string",
          "addressLine2": "string",
          "addressLine3": "string",
          "city": "string",
          "stateCode": "string",
          "countryCode": "string",
          "county": "string",
          "postalCode": "string"
        }
      ],
      "addClaimVendorCertificateDTOS": [
        {
          "vendorCertificateId": 0,
          "certifyingAgencyCode": "string",
          "serviceTypeCode": "string",
          "certificateNumber": "string",
          "expiryDate": "string"
        }
      ],
      "addClaimVendorServiceDTOS": [
        {
          "vendorServiceId": 0,
          "inspectionServiceCode": "string"
        }
      ],
      "addClaimVendorServiceTypeDTOS": [
        {
          "vendorServiceTypeId": 0,
          "inspectionServiceTypeCodes": "string"
        }
      ]
    }
    this.inspectionService.addClaimVendor(addPayload).subscribe(data=>{
      this.toasterService.showToast('success', 'Vendor Claim Added Sucessfully');
    },
  error=>{
    this.toasterService.showToast('error', 'Vendor Claim Added Failed');
  })
  }

}
