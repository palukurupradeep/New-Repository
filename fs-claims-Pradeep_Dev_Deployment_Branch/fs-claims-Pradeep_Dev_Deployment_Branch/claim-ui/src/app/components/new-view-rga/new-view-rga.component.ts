import { Component, Input, OnInit } from '@angular/core';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { ClaimDetailService } from '../../ngrx/store/services/claim-detail.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { ToasterComponent } from '../toaster/toaster.component';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
import { InitiateClaimService } from 'src/app/ngrx/store/services/initiate-claim.service';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-new-view-rga',
  templateUrl: './new-view-rga.component.html',
  styleUrls: ['./new-view-rga.component.scss']
})
export class NewViewRgaComponent implements OnInit {

  public userId!: number;
  @Input() screenWidth = 0;
  public isSideNavCollapsed: boolean = true;
  rgaDetails :any;
  claimId!: number;
  claimNumber!:number;
  rgaNumber!: number;
  rgaHeaderId!: number;
  locationData: any[]=[];
  locationName: string='';
  claimAddresses: any;
  selectedOption: string = 'soldTo';
  rgaStatus: string='';
  claimDocuments:any[]=[];
  countryList: any[]=[];
  stateName: any;
  rdcLocationCode: string='';
  noteTextArea:string='';
  public noteTextDisable : boolean = true;
  public isButtonDisabled = false;
  rgaStatusAction: string ='';
  constructor(
    private bodyClassService: BodyClassService,
    private claimDetailService: ClaimDetailService,
    private snackBar: MatSnackBar,
    private toasterService: ToasterService,
    private router: Router,
    private route: ActivatedRoute,
    private initiateClaimService: InitiateClaimService,
   ) { }
    


  ngOnInit() {
   
    this.userId = Number(localStorage.getItem('userId'));

    this.route.paramMap.subscribe(params => {
    
      this.claimId = window.history.state.claimId;
      this.rgaNumber= window.history.state.rgaNumber;
      // this.rgaHeaderId = window.history.state.rgaNumber;
      this.claimAddresses = window.history.state.claimAddresses;
       this.claimNumber =window.history.state.claimNumber;
      this.rgaStatus = window.history.state.rgaStatus;
      this.claimDocuments= window.history.state.claimDocuments;
     // this.rdcLocationCode= window.history.state.rdcLocationCode;
      console.log(this.claimDocuments,'newclaimDocuments')
  // console.log(this.rgaNumber,'this.rgaNumber')
  // console.log(this.claimId,'this.claimId')
  // console.log(this.rgaStatus,'this.rgaStatus')
  console.log(this.claimAddresses,'this.claimAddresses')
 // this.getCountriesList();
      this.viewRgaDetails(this.claimId,this.rgaNumber);
      console.log(this.claimNumber,'...........claimNumber')
      
    });
  }
  viewRgaDetails(claimId:number,rgaNumber:number){
    this.claimDetailService.fetchRgaDetailsByClaimIdAndRgaNumber(claimId,rgaNumber).subscribe(data => {
      if(data.status == 200){
     console.log(data,'viewrgaDayta')
      this.rgaDetails= data.body;
      this.rdcLocationCode =  this.rgaDetails.claimRgaLineDTO[0].locationCode
         console.log(this.rdcLocationCode,'rdcLocationCode')
      this.getReturnLocations(this.rdcLocationCode);
      
      console.log(this.rgaDetails,'rgaDetails')
      console.log(this.rgaDetails.rgaAddressDTO, 'data from rgaDetails.rgaAddressDTO..');
      this.claimAddresses.forEach((address:any, index:any) => {
        this.claimDetailService.fetchCountryAndState(address.countryId, address.stateId).subscribe(response => {
          // Update the respective object with stateName and countryName
          this.claimAddresses[index].stateName = response.body.stateName;
          this.claimAddresses[index].countryName = response.body.countryName;
        });
      });
   } });
  }
  public getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
  }

  public onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  public updateRGAStatusByAction(action: string) {
    const modelDiv = document.getElementById('modalEnterClaim');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
    this.rgaStatusAction = action;
    // this.claimDetailService.updateRGAStatusByAction(this.rgaDetails.claimRgaHeaderId, this.userId, action).subscribe(
    //   data => {
    //     const toastType: 'success' | 'error' = 'success';
    //     const message = `RGA ${action} successfully`;
    //     this.toasterService.showToast(toastType, message);
    //     this.router.navigate(['/claim-detail', this.claimNumber]);
    //   },
    //   error => {
    //     const toastType: 'error' = 'error';
    //     const message = `RGA ${action} failed`;
    //     this.toasterService.showToast(toastType, message);
    //   }
    // );
  }

navigateClaimDetails(){
  this.router.navigate(['/claim-detail', this.claimNumber]);
}
  openPrepareDisposition(){
    const navigationExtras: NavigationExtras = {
     state: {
       dispositionType:'RGA',
       rgaDetails:this.rgaDetails
     }
   };
   this.router.navigate(['/prepare-disposition'], navigationExtras);
 }

 public getReturnLocations(locationCode :string){
  this.claimDetailService.getReturnLocations(locationCode).subscribe(data=>{
    this.locationData = data.body;
console.log(this.rgaDetails?.claimRgaLineDTO[0]?.rdcLocationId,'this.rgaDetails.claimRgaLineDTO[0].rdcLocationId')
const rdcLocationId = this.rgaDetails?.claimRgaLineDTO[0]?.rdcLocationId;

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
    return this.claimAddresses.find((address:any) => address.addressTypeId === typeId);
  }
  return null;
}

getFormattedPostalCode(): string {
  // Retrieve the postal code and remove all spaces
  let postalCode = this.getFilteredAddress().postalCode.replace(/\s+/g, '');

  // Check if the postal code has more than 5 digits
  if (postalCode.length > 5) {
    // Insert a hyphen after the first 5 digits
    return `${postalCode.slice(0, 5)}-${postalCode.slice(5)}`;
  }

  // Return the postal code as is if it's 5 digits or less
  return postalCode;
}

saveAsDraft(status: string) {
  console.log('function for save');
  const claimRgaLineDTO = this.rgaDetails.claimRgaLineDTO.map((detail:any) => ({
    "claimRgaLineId": detail.claimRgaLineId,
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
    "claimRgaHeaderId": this.rgaDetails.claimRgaHeaderId,
    "rgaNumber":this.rgaDetails.rgaNumber,
   "rgaStatusCode": status,
    "claimId": this.rgaDetails.claimId,
    "customerNumber": this.rgaDetails.customerNumber,
    "claimReasonCode": this.rgaDetails.claimReasonCode,
    "amountUsd": this.rgaDetails.amountUsd,
    "claimAmountForeign": this.rgaDetails.claimAmountForeign,
    "claimRgaLineDTO": claimRgaLineDTO,
    "comments": this.rgaDetails.comments,
    "rgaAddressDTO": {
      "rgaAddressId": this.rgaDetails?.rgaAddressDTO?.rgaAddressId||'',
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
      console.log(data, 'data....');
      // this.snackBar.open('RGA is successfully issued', 'Close', {
      //   duration: 3000,
      //   verticalPosition: 'top',
      //   panelClass: ['custom-snackbar'],
      // });
      console.log(this.claimNumber,'this.claimNumber')
      const toastType: 'success' | 'error' | 'warning' = 'success'; 
      const message = 'RGA is successfully issued';
      this.toasterService.showToast(toastType, message);
      this.router.navigate(['/claim-detail', this.claimNumber]);
    },
    (error) => {
      // this.snackBar.open('RGA could not be issued. Try again.', 'Close', {
      //   duration: 3000,
      //   verticalPosition: 'top',
      //   panelClass: ['custom-snackbar'],
      // });
      const toastType: 'success' | 'error' | 'warning' = 'error'; 
      const message = 'RGA could not be issued. Try again.';
      this.toasterService.showToast(toastType, message);
      console.error('Error issuing RGA:', error);
    }
  );
  
  
 }
 formatPostalCode(postalCode: string): string {
  console.log(postalCode,'funcpostalCode')
  if (!postalCode) {
    return '';
  }

  const cleanedPostalCode = postalCode.replace(/\D/g, '');
  console.log('Cleaned Postal Code:', cleanedPostalCode); // Debugging

  const firstPart = cleanedPostalCode.slice(0, 5);
  const secondPart = cleanedPostalCode.slice(5, 9);

  return secondPart ? `${firstPart}-${secondPart}` : firstPart;
}

navigateToEditRGa(){
  const navigationExtras: NavigationExtras = {
    state: {
      claimId: this.claimId, 
      claimNumber: this.claimNumber,
       rgaNumber: this.rgaDetails.rgaNumber,
       rgaHeaderId:this.rgaDetails.claimRgaHeaderId,
       claimAddresses: this.claimAddresses,
       rdcLocationCode: this.rdcLocationCode,
       claimDocuments: this.claimDocuments,
       sellingCompany:this.rgaDetails.sellingCompany ,
       region:this.rgaDetails.region,
       division:this.rgaDetails.division ,
       claimAreaId:this.rgaDetails.claimAreaId,
       customerNumber:this.rgaDetails.customerNumber,
       rgaDetails: this.rgaDetails
    }
  };
console.log(navigationExtras,'navigationExtras')
 this.router.navigate(['/edit-rga'], navigationExtras);
 
}
public CloseModalEnterClaim() {
  this.isButtonDisabled = false;
  const modelDiv = document.getElementById('modalEnterClaim');
  if (modelDiv != null) {
    modelDiv.style.display = 'none';
  }
}
 
updateRgaStatus(){
  this.claimDetailService.updateRGAStatusByAction(this.rgaDetails.claimRgaHeaderId, this.userId, this.rgaStatusAction, this.noteTextArea).subscribe(
    data => {
      const toastType: 'success' | 'error' = 'success';
      const message = `RGA ${this.rgaStatusAction} successfully`;
      this.toasterService.showToast(toastType, message);
      this.router.navigate(['/claim-detail', this.claimNumber]);
    },
    error => {
      const toastType: 'error' = 'error';
      const message = `RGA ${this.rgaStatusAction} failed`;
      this.toasterService.showToast(toastType, message);
    }
  );
}
}
