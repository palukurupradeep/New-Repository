import { Component,Input, OnInit } from '@angular/core';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { ClaimDetailService } from '../../ngrx/store/services/claim-detail.service';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ToasterComponent } from '../toaster/toaster.component';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
import { DatePipe } from '@angular/common';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-view-rga',
  templateUrl: './view-rga.component.html',
  styleUrls: ['./view-rga.component.scss']
})
export class ViewRgaComponent  implements OnInit {
  rgaDetails :any;
  claimId!: number;
  claimNumber!:number;
  rgaNumber!: number;
  rgaHeaderId!: number;
  locationData: any[]=[];
  locationName: string='';
  claimAddresses: any[]=[];
  selectedOption: string = 'soldTo';
  claimDocuments: any[]=[];
  rdcLocationCode: string='';
  sellingCompany: number=0;
  region: number=0;
  division:number=0;
  claimAreaId: number=0;
  customerNumber: string='';
  
  constructor(
    private bodyClassService: BodyClassService,
    private claimDetailService:ClaimDetailService,
    private route: ActivatedRoute,
    private router: Router,
    private toasterService: ToasterService,
    private snackBar: MatSnackBar,
   private datePipe: DatePipe){}

    @Input() screenWidth = 0;
    public isSideNavCollapsed: boolean = true;
    public getBodyClass(): string {
      return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
    }

    public onToggleSideNav(data: SideNavToggle): void {
      this.screenWidth = data.screenWidth;
      this.isSideNavCollapsed = data.collapsed;
    }
    ngOnInit(): void {
      this.route.paramMap.subscribe(params => {
        // this.selectedLine = window.history.state.selectedLine;
       
       // this.claimAddresses = window.history.state.claimAddresses;
        this.claimId = window.history.state.claimId;
        this.rgaNumber= window.history.state.rgaNumber;
       this.rgaHeaderId = window.history.state.rgaHeaderId;
        this.claimAddresses = window.history.state.claimAddresses;
        this.claimNumber =window.history.state.claimNumber;
        this.claimDocuments= window.history.state.claimDocuments;
       this.rdcLocationCode = window.history.state.rdcLocationCode;
       this.sellingCompany =window.history.state.sellingCompany;
       this.region =window.history.state.region;
     this.division=window.history.state.division;
     this.claimAreaId =window.history.state.claimAreaId;
     this.customerNumber= window.history.state.customerNumber;
    console.log(this.rgaNumber,'this.rgaNumber');
    console.log(this.rgaHeaderId,'this.rgaHeaderId');
    console.log(this.claimDocuments,'this.claimDocumentsonload')
        this.viewRgaDetails();
        console.log(this.claimNumber,'...........claimNumber')
        
      });
   
     
     
      
    }

     viewRgaDetails(){
    //  this.claimDetailService.fetchRgaDetailsByClaimIdAndRgaNumber(claimId,rgaNumber).subscribe(data => {
     //  console.log(data,'viewrgaDayta')
     this.rgaDetails = JSON.parse(localStorage.getItem('setViewEditDAta') || '{}');

      //  this.rgaDetails= localStorage.getItem('setViewEditDAta');
        console.log(this.rgaDetails,'this.rgaDetailsforview..')
      // this.rdcLocationCode =  this.rgaDetails.claimRgaLineDTO[0].locationCode
       
     
        console.log(this.rdcLocationCode,'rdcLocationCode')
        this.getReturnLocations(this.rdcLocationCode);
        this.claimAddresses.forEach((address, index) => {
          this.claimDetailService.fetchCountryAndState(address.countryId, address.stateId).subscribe(response => {
            // Update the respective object with stateName and countryName
            this.claimAddresses[index].stateName = response.body.stateName;
            this.claimAddresses[index].countryName = response.body.countryName;
          });
        });
        console.log(this.rgaDetails.rgaAddressDTO, 'data from rgaDetails.rgaAddressDTO..');
     // });


 
    }
    saveAsDraft(status: string) {
      console.log('function for save',this.rgaDetails);
      const claimRgaLineDTO = this.rgaDetails.claimRgaLineDTO.map((detail:any) => ({
        "claimRgaLineId": detail.claimRgaLineId||0,
        "invoiceLineNumber":detail.invoiceLineNumber,
        "rgaLineNumber": detail.rgaLineNumber,
        "claimLineId": detail.claimLineId,
        "rollNumber": detail.rollNumber,
        "styleNumber": detail.styleNumber,
        "unitOfMeasureCode": detail.unitOfMeasureCode,
        "issuedQuantity": detail.issuedQuantity,
        "issuedAmount": detail.issuedAmount,
        "rdcLocationId": detail.rdcLocationId,
        "issuedWidthInFeet": detail.issuedWidthInFeet,
        "issuedLengthInFeet": detail.issuedLengthInFeet
      }));
      
      const saveDraftPayload = {
        
        "createdByUserId": this.rgaDetails.createdByUserId,
        "modifiedByUserId": this.rgaDetails.modifiedByUserId,
        "claimRgaHeaderId": this.rgaHeaderId ||0,
        "rgaNumber":this.rgaNumber ||'',
       "rgaStatusCode": status,
        "claimId": this.rgaDetails.claimId,
        "customerNumber": this.rgaDetails.customerNumber,
        "claimReasonCode": this.rgaDetails.reasonCode || 'ALL',
        "amountUsd": this.rgaDetails.amountUsd,
        "claimAmountForeign": 0,
        "claimRgaLineDTO": claimRgaLineDTO,
        "comments": this.rgaDetails.comments,
        "rgaAddressDTO": {
          "rgaAddressId":0,// this.rgaDetails?.rgaAddressDTO?.rgaAddressId||'',
          "addressTypeCode": this.rgaDetails?.rgaAddressDTO?.addressTypeCode||'',
          "fullName": this.rgaDetails?.rgaAddressDTO?.fullName||'',
          "addressLine1": this.rgaDetails?.rgaAddressDTO?.addressLine1 ||'',
          "addressLine2": this.rgaDetails?.rgaAddressDTO?.addressLine2 || '',
          "city": this.rgaDetails?.rgaAddressDTO?.city||'',
          "stateId": this.getFilteredAddress()?.stateId || '',
          "countryId": this.getFilteredAddress()?.countryId || '',
          "postalCode": this.rgaDetails?.rgaAddressDTO?.postalCode||''
        }
      };
      
      console.log(saveDraftPayload, 'viewsaveDraftPayload');
      this.claimDetailService.rgaDraftORSave(saveDraftPayload).subscribe(
        (data) => {
          localStorage.removeItem('setViewEditDAta');
          console.log(this.claimNumber,'this.claimNumber')
          const toastType: 'success' | 'error' | 'warning' = 'success'; 
          const message = 'RGA is successfully issued';
          this.toasterService.showToast(toastType, message);
          this.router.navigate(['/claim-detail', this.claimNumber]);
        },
        (error) => {
        
          const toastType: 'success' | 'error' | 'warning' = 'error'; 
          const message = 'RGA could not be issued. Try again.';
          this.toasterService.showToast(toastType, message);
          console.error('Error issuing RGA:', error);
        }
      );
      
      
     }
     cancelRga(){
      // this.claimDetailService.cancelRga(this.rgaDetails.claimId, this.rgaDetails.rgaNumber).subscribe(
      //   (data) => {
       
          // this.snackBar.open('RGA is successfully canceled', 'Close', {
          //   duration: 3000,
          //   verticalPosition: 'top',
          //   panelClass: ['custom-snackbar'],
          // });
          // const toastType: 'success' | 'error' | 'warning' = 'success'; 
          // const message = 'RGA is successfully canceled';
          // this.toasterService.showToast(toastType, message);
          this.router.navigate(['/claim-detail', this.claimNumber]);
      //   },
      //   (error) => {
      //     // this.snackBar.open('RGA could not be canceled. Try again.', 'Close', {
      //     //   duration: 3000,
      //     //   verticalPosition: 'top',
      //     //   panelClass: ['custom-snackbar'],
      //     // });
      //     const toastType: 'success' | 'error' | 'warning' = 'error'; 
      //     const message = 'RGA could not be canceled. Try again.';
      //     this.toasterService.showToast(toastType, message);
      //   }
      // );
      
   
    }
    
public getReturnLocations(locationCode :string){
  this.claimDetailService.getReturnLocations(locationCode).subscribe(data=>{
    this.locationData = data.body;
console.log(this.rgaDetails.claimRgaLineDTO[0].rdcLocationId,'this.rgaDetails.claimRgaLineDTO[0].rdcLocationId')
const rdcLocationId = this.rgaDetails.claimRgaLineDTO[0]?.rdcLocationId;

// Find the location object in locationData with the matching locationId
const location = this.locationData.find(loc => loc.locationId === rdcLocationId);

// Extract the locationName
this.locationName = location ? location.locationName : '';

// Save the locationName in a variable
console.log(this.locationName, 'locationName');
    console.log(data,'locData')
  })
}
getFilteredAddress() {
  const addressTypeIdMap: { [key: string]: number | null } = {
    billTo: 3,
    soldTo: 14,
    shipTo: 2,
    other: 11,
    none: null
  };

  const typeId = addressTypeIdMap[this.selectedOption as keyof typeof addressTypeIdMap];
  if (typeId !== null) {
    return this.claimAddresses.find(address => address.addressTypeId === typeId);
  }
  return null;
}
navigateToEditRGa(){
  console.log(this.claimDocuments,'viewclaimDocuments')
  const navigationExtras: NavigationExtras = {
    state: {
      claimId: this.claimId, 
      claimNumber: this.claimNumber,
       rgaNumber: this.rgaNumber,
       rgaHeaderId:this.rgaHeaderId,
       claimAddresses: this.claimAddresses,
       claimDocuments: this.claimDocuments,
       rdcLocationCode: this.rdcLocationCode,
       sellingCompany:this.sellingCompany,
        region:this.region,
      division:this.division,
      claimAreaId:this.claimAreaId,
      customerNumber: this.customerNumber
      
    }
  };
console.log(navigationExtras,'navigationExtras-view')
  this.router.navigate(['/edit-rga'], navigationExtras);
 
}
// getFormattedPostalCode(): string {
//   const postalCode = this.getFilteredAddress().postalCode;
//   return `${postalCode.slice(0, 5)}-${postalCode.slice(5)}`;
// }

}
