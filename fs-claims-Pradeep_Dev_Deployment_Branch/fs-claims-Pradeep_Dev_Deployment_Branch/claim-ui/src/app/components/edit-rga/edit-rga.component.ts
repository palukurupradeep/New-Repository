import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, NavigationEnd, NavigationExtras, Router } from '@angular/router';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';
import { ToasterComponent } from '../toaster/toaster.component';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
import { InitiateClaimService } from 'src/app/ngrx/store/services/initiate-claim.service';
import { MatSelectChange } from '@angular/material/select';
import { filter } from 'rxjs';
import { DatePipe } from '@angular/common';


interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-edit-rga',
  templateUrl: './edit-rga.component.html',
  styleUrls: ['./edit-rga.component.scss']
})
export class EditRgaComponent implements OnInit {
  @Input() screenWidth = 0;
public isSideNavCollapsed: boolean = true;
  recipientData: any[]=[];
  locationData: any[]=[];
  selectedLine: any[]=[];
  rgaDetails:any={};
  sendCorrespondence: boolean = false;
  pickupFrom: string = "Ship To";
  showAddressFields: boolean = false;
  claimAddresses: any[]=[];
  claimAddressesData: any[]=[];
  selectedOption = 'shipTo';
  claimId: number=1;
  emailForm: FormGroup;
  uniqueDocuments: any[]=[];
  claimDocuments: any[]=[];
  filteredDocuments: any[]=[];
  isAnyLineSelected = false;
  claimNumber: string='';
  customerNumber: string='';
  locationId: number | null = null;
  isToggled :boolean=false;
  rgaNumber!: number;
  rgaHeaderId!: number;
  comment: string = '';
  filteredClaimDocuments: any[]=[]
  rdcLocationCode: string ='';
  isCorrespondenceToggled: boolean = false; 
  public otherFormGroup!:FormGroup
  addressForm!: FormGroup;
  public countries: any = [];
  public filteredStates: any = [];
  public selectedDailcode: string = '';
  public selectedCountryHasStates: boolean = false;
  public selectedCountry : any;
  rdcLocationId: number=0;
  sendMailToTm: boolean=false;
  formattedLine: any[]=[];
  chekedLineItem: any[]=[];
  private previousUrl: string = '';
  previousClaimNumber: string = '';
  prepareRGAData: any={};
  sellingCompany: number=0;
  region: number=0;
  division:number=0;
  claimAreaId: number=0;
  constructor(
    private bodyClassService: BodyClassService,
    private claimDetailService:ClaimDetailService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar,
    private toasterService: ToasterService,
    private initialClaimService : InitiateClaimService,
    private fb: FormBuilder,
    private datePipe: DatePipe){
      this.emailForm = this.fb.group({
        email: ['', [Validators.required, Validators.email]]
      });
    }
  ngOnInit(): void {
    this.router.events.pipe(
      filter((event): event is NavigationEnd => event instanceof NavigationEnd) // Type guard
    ).subscribe((event: NavigationEnd) => {
      const currentUrl = event.urlAfterRedirects;

      if (this.previousUrl && this.previousUrl !== currentUrl) {
        // Remove selectedLine from localStorage on navigation change
        localStorage.removeItem('selectedLine');
      }

      // Check if claimNumber has changed
      if (this.previousClaimNumber && this.previousClaimNumber !== this.claimNumber) {
        // Remove selectedLine from localStorage if claimNumber has changed
        localStorage.removeItem('selectedLine');
      }

      // Update previousUrl and previousClaimNumber
      this.previousUrl = currentUrl;
      this.previousClaimNumber = this.claimNumber;
    });
  

    
      //this.getReturnLocations('0U2');
      this.route.paramMap.subscribe(params => {
        
        // this.selectedLine = window.history.state.selectedLine;
       
       // this.claimAddresses = window.history.state.claimAddresses;
        this.claimId = window.history.state.claimId;
        this.rgaNumber= window.history.state.rgaNumber;
        this.rgaHeaderId = window.history.state.rgaHeaderId;
        this.claimAddresses = window.history.state.claimAddresses;
        this.claimNumber =window.history.state.claimNumber;
        this.claimDocuments= window.history.state.claimDocuments;
        console.log(this.claimDocuments,'claimDocuments')
        this.rdcLocationCode = window.history.state.rdcLocationCode;
        this.sellingCompany =window.history.state.sellingCompany;
        this.region =window.history.state.region;
      this.division=window.history.state.division;
      this.claimAreaId =window.history.state.claimAreaId;
      this.customerNumber=window.history.state.customerNumber;
      this.rgaDetails = window.history.state.rgaDetails;
      console.log(this.rgaNumber,'this.rgaNumber');
      console.log(this.rgaHeaderId,'this.rgaHeaderId');
      console.log(this.sellingCompany,'sellingCompany');
      console.log(this.region,'region');
      console.log(this.division,'division');
      console.log(this.claimAreaId,'claimAreaId')
      console.log(this.rgaDetails,'rgaDetails')
        // Filter claimLineDetails within each document to include only items with detailTypeCode: "MERCH"
this.filteredClaimDocuments = this.claimDocuments
.filter(document => document.documentType.documentTypeCode === "INV" && document.statusId !==3)
.map(document => {
  return {
    ...document,
    claimLineDetails: document.claimLineDetails.filter(
      (lineItem: any) => lineItem.detailType.detailTypeCode === 'MERCH'  && 
      lineItem.statusId !== 3
    )
  };
});
     
      this.claimAddresses.forEach((address, index) => {
        this.claimDetailService.fetchCountryAndState(address.countryId, address.stateId).subscribe(response => {
          // Update the respective object with stateName and countryName
          this.claimAddresses[index].stateName = response.body.stateName;
          this.claimAddresses[index].countryName = response.body.countryName;
        });
      });
    
      console.log(this.claimAddresses,'claimAddressesafter');
        console.log(this.claimDocuments,'this.claimDocuments')
    console.log(this.rgaNumber,'this.rgaNumber')
    this.fetchRecipients(this.claimId);
        this.editRgaDetails(this.claimId,this.rgaNumber);
        console.log(this.claimNumber,'...........claimNumber')
   
        
      });
  
  }
   
  editRgaDetails(claimId:number,rgaNumber:number){
    console.log('insideeditrgapai')
   // this.claimDetailService.fetchRgaDetailsByClaimIdAndRgaNumber(claimId,rgaNumber).subscribe(data => {
     // First check if rgaDetails has data
if (this.rgaDetails && this.rgaDetails.claimRgaLineDTO) {
  // Replace the 'price' and 'claimLineAmountUsd' property names
this.rgaDetails.claimRgaLineDTO = this.rgaDetails.claimRgaLineDTO.map((line: any) => {
  return {
    ...line, // Spread the existing object
    price: line.price, // Create the new property 'unitPriceUsd' and assign the value of 'price'
    lineAmount: line.issuedAmount, // Create the new property 'lineAmountUsd' and assign the value of 'claimLineAmountUsd'
    // claimLineAmountUsd: undefined // Remove the old 'claimLineAmountUsd' property
  };
});


  this.selectedLine = this.rgaDetails.claimRgaLineDTO;
  this.rgaDetails=this.rgaDetails;
} else {
  // If rgaDetails doesn't have data, check localStorage
  const savedLines = localStorage.getItem('selectedLine');
  
  if (savedLines && JSON.parse(savedLines).length > 0) {
    // If savedLines exist in localStorage and are not empty, use them
    this.selectedLine = JSON.parse(savedLines);
    this.rgaDetails=JSON.parse(localStorage.getItem('setViewEditDAta') || '{}');
  } else {
    // If no savedLines, get data from setViewEditDAta and transform it
    const viewEditData = localStorage.getItem('setViewEditDAta');
    if (viewEditData) {
      this.selectedLine = JSON.parse(viewEditData).claimRgaLineDTO.map((line: any) => {
        return { ...line, lineNumber: line.rgaLineNumber };
      });
      this.rgaDetails=JSON.parse(localStorage.getItem('setViewEditDAta') || '{}');
    } else {
      this.selectedLine = [];  // Fallback in case both localStorage and rgaDetails are empty
    }
  }

  console.log(this.selectedLine, 'oninitlineuber');
}

    //  const savedLines = localStorage.getItem('selectedLine');
    //  if (savedLines && JSON.parse(savedLines).length  > 0) {
    //    this.selectedLine = JSON.parse(savedLines);
    //  } else {
    //   this.selectedLine = JSON.parse(localStorage.getItem('setViewEditDAta') || '{}').claimRgaLineDTO.map((line:any) => {
    //     return { ...line, lineNumber: line.rgaLineNumber };
    //   });
      
      
    //    // this.selectedLine = data.body.claimRgaLineDTO;
        
    //    console.log(this.selectedLine,'oninitlineuber')
    //  }
   //  this.rgaDetails=JSON.parse(localStorage.getItem('setViewEditDAta') || '{}');
   //  this.rdcLocationCode =  this.rgaDetails.claimRgaLineDTO[0].locationCode
     this.getReturnLocations(this.rdcLocationCode);
  
   
    
      this.rdcLocationId =   this.selectedLine[0].rdcLocationId;
      this.sendCorrespondence = this.rgaDetails.sendCorrespondence;
      this.sendMailToTm=this.rgaDetails.sendMailToTm;
      this.comment = this.rgaDetails.comments;
      this.uniqueDocuments = this.getUniqueDocuments(this.selectedLine);
      console.log(this.uniqueDocuments,'uniqueDocuments')
      this.filteredDocuments =this.getUniqueDocuments(this.claimDocuments);
    //  this.markSelectedLines();
      console.log(this.rgaDetails,'this.rgaDetails')
      console.log(this.rdcLocationId, 'data from rdcLocationId...');
      console.log(this.sendCorrespondence, 'data from sendCorrespondence...');
      console.log(this.sendMailToTm, 'data from sendMailToTmd...');
      console.log(this.comment, 'data from comment...');
      console.log(this.rgaDetails, 'data from rgaDetails...');
      console.log(this.selectedLine, 'data from selectedLinergaDetails...');
   // });



  }
   
    public getBodyClass(): string {
      return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
    }

    public onToggleSideNav(data: SideNavToggle): void {
      this.screenWidth = data.screenWidth;
      this.isSideNavCollapsed = data.collapsed;
    }

// public fetchRecipients(claimId:number){
//   this.claimDetailService.fetchRecipients(claimId).subscribe(data=>{
//     this.recipientData=data.body
//     this.rgaDetails.recipientData
//     console.log(this.recipientData,'recipientData')
//   })
// }

public fetchRecipients(claimId: number) {
  this.claimDetailService.fetchRecipients(claimId).subscribe(data => {
    console.log(data.body,'apiresponse fo email')
    // Assuming data.body is an array of objects, convert it to an array of email strings
    this.recipientData = data.body.map((recipient: any) => recipient.email)
      .filter((email: any) => email !== null); // Filter out null values
      console.log(this.rgaDetails.recipientData,'this.recipientData fo emails')
    if (this.rgaDetails && this.rgaDetails.recipientData) {
      // Loop through rgaDetails.recipientData and add new emails to recipientData
      this.rgaDetails.recipientData.forEach((email: string) => {
        if (!this.recipientData.includes(email)) {
          this.recipientData.push(email);
        }
      });
    } else {
      console.log('unfeinfed recipientData',this.recipientData)
      // If this.rgaDetails.recipientData is undefined, push emails from the API response
      this.recipientData = [...data.body];  // Ensure this.recipientData is initialized
    }

    // Optionally filter out any duplicates after adding from rgaDetails
    //this.recipientData = [...new Set(data.body)];

    console.log(this.recipientData, 'Updated recipientData with new emails');
  });
}










public getReturnLocations(locationCode :string){
  this.claimDetailService.getReturnLocations(locationCode).subscribe(data=>{
    this.locationData = data.body;
    console.log(data,'locData')
  })
}

removeRecipient(index: number): void {
  this.recipientData.splice(index, 1);
}



onPickupOptionChange(option: string): void {
    if (option === "Other") {
        this.showAddressFields = true;
    } else {
        this.showAddressFields = false;
    }
}



saveAsDraft(status: string) {
  console.log(this.selectedLine,'this.selectedLine...')
 // console.log(console.log(this.rgaDetails.claimRgaLineDTO[0].locationCode,'rdcLocationCode'))

  const claimRgaLineDTO = this.selectedLine.map(line => ({
   
   // "claimRgaLineId": line.claimRgaLineId ? line.claimRgaLineId : 0,
   "claimRgaLineId": 0,
   "invoiceLineNumber":line.invoiceLineNumber,
    "rgaLineNumber": line.lineNumber,
    "claimLineId": line.claimLineId,
    "colorNumber":line.colorNumber,
    "rollNumber": line.rollNumber,
    "styleNumber": line.styleNumber,
    "unitOfMeasureCode": line.unitOfMeasureCode,
    "issuedQuantity": line.quantity || line.issuedQuantity,
    "issuedAmount": line.lineAmount,
    "rdcLocationId":this.rdcLocationId,
    "issuedWidthInFeet": line.issuedWidthInFeet,
    "issuedLengthInFeet": line.issuedLengthInFeet,
    "invNumber":line.invNumber,
    "invDate":line.invDate,
    "lot":line.dyeLot,
    "type":line.type,
    "grade":line.grade,
    "price":line.price,
    "lineAmount":line.lineAmount,
       "reasonCode": line.reasonCode,
    "reasonCodeDescription":line.reasonCodeDescription,
 
  }));
  const firstLine = this.selectedLine[0] || {};
  const saveDraftPayload = {
    "createdByUserId": this.rgaDetails.createdByUserId || 0,
    "modifiedByUserId": this.rgaDetails.modifiedByUserId || 0,
    "claimRgaHeaderId": this.rgaDetails.claimRgaHeaderId,
    "rgaStatusCode": 'P',
    "claimId": this.claimId,
    "customerNumber": this.customerNumber,
    "claimReasonCode": firstLine.reasonCode || '',
    "amountUsd": this.getTotalLineAmount() || 0,
    "claimAmountForeign": 0, // Adjust as needed
    "claimRgaLineDTO": claimRgaLineDTO,
    "comments": this.comment,
    "sendMailToTm": this.sendMailToTm,
    "sendCorrespondence": this.sendCorrespondence,
    "rgaAddressDTO": {
    //  "rgaAddressId": this.rgaDetails.rgaAddressDTO.rgaAddressId,
            "addressTypeCode": "PKB",
      "fullName": this.getFilteredAddress()?.fullName || '',
      "addressLine1": this.getFilteredAddress()?.addressLine1 || '',
      "addressLine2": this.getFilteredAddress()?.addressLine2 || '',
      "city": this.getFilteredAddress()?.city || '',
      "stateId": this.getFilteredAddress()?.stateId || '',
      "countryId": this.getFilteredAddress()?.countryId || '',
      "postalCode": this.getFilteredAddress()?.postalCode || ''
    }
  };
  const setViewEditDAta ={
    "recipientData":this.recipientData,
    "createdByUserId":firstLine.modifiedByUserId||Number(localStorage.getItem('userId')),
  "modifiedByUserId": firstLine.modifiedByUserId||Number(localStorage.getItem('userId')),
    "issuedDate":this.datePipe.transform(new Date(), 'dd/MM/yyyy'),
    "claimId": this.claimId,
    "sellingCompany": this.sellingCompany,
    "region": this.region,
    "division":this.division,
    "claimAreaId":this.claimAreaId,
   "claimRgaLineDTO": claimRgaLineDTO,
   "comments": this.comment,
   "sendMailToTm": this.sendMailToTm,
   "sendCorrespondence": this.sendCorrespondence,
   "customerNumber": this.customerNumber,
   "amountUsd": this.getTotalLineAmount() || 0,
   "rgaAddressDTO": {
    "rgaAddressId": 0,
    "addressTypeCode": "PKB",
    "fullName": this.getFilteredAddress()?.fullName || '',
    "addressLine1": this.getFilteredAddress()?.addressLine1 || '',
    "addressLine2": this.getFilteredAddress()?.addressLine2 || '',
    "city": this.getFilteredAddress()?.city || '',
    "stateId": this.getFilteredAddress()?.stateId || '',
    "countryId": this.getFilteredAddress()?.countryId || '',
    "postalCode": this.getFilteredAddress()?.postalCode || ''
  }
  }
 console.log(saveDraftPayload, 'saveDraftPayload');
  if (status === 'P') {

 
  this.claimDetailService.rgaDraftORSave(saveDraftPayload).subscribe(
    (data) => {
 

      localStorage.setItem('setViewEditDAta', JSON.stringify(setViewEditDAta));
      localStorage.removeItem('selectedLine');
        const toastType: 'success' | 'error' | 'warning' = 'success'; 
        const message = 'RGA is successfully saved as draft';
        this.toasterService.showToast(toastType, message);
        this.navigateToDetails();
      }
    ,
    (error) => {
      console.log(error,'error')
      this.snackBar.open(error.error.detail, 'Close', {
        duration: 3000,
        verticalPosition: 'top',
        panelClass: ['custom-snackbar'],
      });
      console.error('Error saving RGA:', error);
    }
  ); 
 } else{
  localStorage.setItem('setViewEditDAta', JSON.stringify(setViewEditDAta));
  localStorage.removeItem('selectedLine');
  const navigationExtras: NavigationExtras = {
    state: {
      rgaNumber:this.rgaNumber,
       rgaHeaderId:this.rgaHeaderId,
      claimId: this.claimId,
      claimNumber: this.claimNumber,
         claimAddresses: this.claimAddresses,
      rdcLocationCode:this.rdcLocationCode,
      claimDocuments : this.claimDocuments,
      sellingCompany:this.sellingCompany,
        region:this.region,
        claimAreaId:this.claimAreaId,
        division:this.division,
        customerNumber: this.customerNumber
    }
  };
  console.log(navigationExtras,'navigationExtrasview')
 this.router.navigate(['/view-rga'], navigationExtras);
 }
}

openEmailModal(){
  const modelDiv = document.getElementById('addEmailModal');
  if (modelDiv != null) {
    modelDiv.style.display = 'block';
    
  }
}
closeEmailModal(){
 
    const modelDiv = document.getElementById('addEmailModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
      
    }

}
addEmailModal(){
  this.recipientData.push(this.emailForm.value.email);
  this.emailForm.reset();
  this.closeEmailModal();
  }


get email() {
  return this.emailForm.get('email');
}

// Method to remove selected lines



getUniqueDocuments(lines: any[]): any[] {
  const uniqueDocuments = lines.reduce((acc, line) => {
    if (!acc.some((item: any) => item.documentNumber === line.invNumber)) {
      acc.push({
        documentNumber: line.invNumber, // Mapping to invNumber based on your data
        documentDate: line.invDate,     // Mapping to invDate
        orderNumber: line.orderNumber   // Mapping to orderNumber
      });
    }
    return acc;
  }, []);
  
  return uniqueDocuments;
}

getTotalAmount(documentNumber: string): number {
  return this.selectedLine
      .filter(line => line.documentNumber === documentNumber)
      .reduce((total, line) => total + line.selectedLine.lineAmount, 0);
}

getCombinedTotal(): number {
  return this.uniqueDocuments
      .map(document => this.getTotalAmount(document.documentNumber))
      .reduce((combinedTotal, documentTotal) => combinedTotal + documentTotal, 0);
}




closeClaimLinesModal(){
 
  const modelDiv = document.getElementById('claimLinesModal');
  if (modelDiv != null) {
    modelDiv.style.display = 'none';
    
  }

}

addClaimLinesModal(){
  const modelDiv = document.getElementById('claimLinesModal');
  if (modelDiv != null) {
    modelDiv.style.display = 'block';
    
  }
}
checkSelectedLines(line: any, event: Event): void {
  console.log(line,'line')
  const checkbox = event.target as HTMLInputElement;
  if (checkbox.checked) {
    this.isAnyLineSelected= true;
     this.chekedLineItem.push(line);
    console.log(this.chekedLineItem,'chekedLineItem')
  
  } else {
    this.isAnyLineSelected= false;
    this.chekedLineItem = this.chekedLineItem.filter((selected :any)=> selected.lineNumber !== line.lineNumber);
    console.log(this.chekedLineItem,'chekedLineItemafter')
  }
  this.isAnyLineSelected = this.chekedLineItem.length > 0;
}



addSelectedClaimLines(): void {
  console.log(this.formattedLine, 'selectedLine');

  // Array to store DTOs for the API payload
  let dtosArray: { claimLineId: any; reasonCode: any; squareFeet: any; quantity: any; productCode: any; }[] = [];

  // Iterate through the formatted lines for validation
  for (let line of this.formattedLine) {
    // Check if the claimReasonCode is "ALL"
    if (line.reasonCode === "ALL") {
      this.toasterService.showToast('error', 'Line does not have Reason Code, please update the reason code to Prepare RGA');
      return; // Exit the function if claimReasonCode is "ALL"
    }

    // Check if returnEligible is false
    if (!line.returnEligible) {
      this.toasterService.showToast('error', 'For this reason code, return is not eligible');
      return; // Exit the function if returnEligible is false
    }

    // If validations pass, prepare the DTO object
    let obj = {
      "claimLineId": line.claimLineId,
      "reasonCode": line.reasonCode,
      "squareFeet": line.squareFeet,
      "quantity": line.issuedQuantity,
      "productCode": line.productCode
    };
    dtosArray.push(obj); // Add to the DTOs array
  }

  // If we pass all validations, proceed to call the API
  const rgaPayload = {
    "continueFrom": "",
    "claimId": this.claimId,
    "defectOfPercentage": "",
    "requestLineDetailsDTOS": dtosArray
  };
  console.log(rgaPayload, 'rgaPayload');

  // Call the API and check for errors
  this.claimDetailService.prepareRGA(rgaPayload).subscribe((data: any) => {
    this.prepareRGAData = data.body;
    
    // If there are errors, show the error message and do not push the line
    if (this.prepareRGAData.codeAndErrorMsgDTOS && this.prepareRGAData.codeAndErrorMsgDTOS.length > 0) {
      const errorMessages = this.prepareRGAData.codeAndErrorMsgDTOS
        .map((errorObj: any) => errorObj.errorMessage)
        .join(', ');
      
      this.toasterService.showToast('error', errorMessages);
    } else {
      // If no errors, push the lines to the selectedLine array
      this.selectedLine.push(...this.formattedLine); // Use spread operator to push all lines
      localStorage.setItem('selectedLine', JSON.stringify(this.selectedLine));

      // Close the modal and reset the state
      this.closeClaimLinesModal();
      this.chekedLineItem = [];
      this.isAnyLineSelected = false;
      this.formattedLine = [];

      // Get unique documents after the selected lines have been updated
      this.uniqueDocuments = this.getUniqueDocuments(this.selectedLine);
    }
  });

  console.log(this.selectedLine, 'this.selectedLineafter1');
}

// addSelectedClaimLines(): void {
//   console.log(this.formattedLine, 'selectedLine,,');

//   // Iterate through the formatted lines
//   this.formattedLine.forEach((line) => {
//     // Check if the claimReasonCode is "ALL"
//     if (line.reasonCode === "ALL") {
//       this.toasterService.showToast('error', 'Line does not have Reason Code, please update the reason code to Prepare RGA');
//       return; // Exit the function if claimReasonCode is "ALL"
//     }

//     // Check if returnEligible is false
//     if (!line.selectedLine.claimReasonDefinition.returnEligible) {
//       this.toasterService.showToast('error', 'For this reason code, return is not eligible');
//       return; // Exit the function if returnEligible is false
//     }

//     // Prepare DTOs array for the API payload
//     let dtosArray: { claimLineId: any; reasonCode: any; squareFeet: any; quantity: any; productCode: any; }[] = [];
//     this.formattedLine.forEach((line: any) => {
//       let obj = {
//         "claimLineId": line.claimLineId,
//         "reasonCode": line.selectedLine.claimReasonDefinition.claimReasonCode,
//         "squareFeet": line.selectedLine.squareFeet,
//         "quantity": line.selectedLine.quantity,
//         "productCode": line.selectedLine.productCode
//       };
//       dtosArray.push(obj);
//     });

//     // Create the payload for the prepareRGA API call
//     const rgaPayload = {
//       "continueFrom": "",
//       "claimId": this.claimId,
//       "defectOfPercentage": "",
//       "requestLineDetailsDTOS": dtosArray
//     };
//     console.log(rgaPayload, 'rgaPayload');

//     // Call the API and check for errors
//     this.claimDetailService.prepareRGA(rgaPayload).subscribe((data: any) => {
//       this.prepareRGAData = data.body;
      
//       // If there are errors, show the error message and do not push the line
//       if (this.prepareRGAData.codeAndErrorMsgDTOS && this.prepareRGAData.codeAndErrorMsgDTOS.length > 0) {
//         this.toasterService.showToast('error', this.prepareRGAData.codeAndErrorMsgDTOS);
//       } else {
//         // Only push the line if no errors exist
//         this.selectedLine.push(line);
//         localStorage.setItem('selectedLine', JSON.stringify(this.selectedLine));

//         // Close the modal and reset the state
//         this.closeClaimLinesModal();
//         this.chekedLineItem = [];
//         this.isAnyLineSelected = false;
//         this.formattedLine = [];

//         // Get unique documents after the selected lines have been updated
//         this.uniqueDocuments = this.getUniqueDocuments(this.selectedLine);
//       }
//     });
//   });

//   console.log(this.selectedLine, 'this.selectedLineafter1');
// }


navigateToDetails(){
  console.log(this.claimNumber,'this.claimNumber')
  this.router.navigate(['/claim-detail', this.claimNumber]);
  
}
onToggle(event: Event): void {
  this.sendMailToTm = (event.target as HTMLInputElement).checked;
  console.log('Send Mail to TM Toggled:', this.sendMailToTm);
  // You can add any additional logic here if needed when the toggle is changed
}

onRdcLocationChange(newLocationId: string) {
  console.log('Selected RDC Location ID:', newLocationId);
  // Perform any additional logic if necessary
}


isChecked(lineNumber: number): boolean {
  return this.selectedLine.some(item =>
   
    item && item.lineNumber === lineNumber);
}

onCheckboxChange(document: any, line: any, event: any): void {
  const isChecked = event.target.checked;
  this.isAnyLineSelected = true;

  if (isChecked) {
    

    console.log(line,'line.......')
    const formattedLineItem = {
      "claimRgaLineId": 0,
      "invoiceLineNumber":line.invoiceLineNumber,
      "rgaLineNumber": line.lineNumber,
           "claimLineId": line.claimLineId ||'',
    "rollNumber": line.rollNumber ||'',
    "styleNumber": line.claimLineId,
    "colorNumber":line.colorNumber||'',
    "styleName": line.styleName||'',
    "colorName": line.colorName||'',
    "issuedWidthInFeet": line.claimDetailRecords[0].widthInFeet ||0,
    "issuedLengthInFeet": line.claimDetailRecords[0].lengthInFeet ||0,
    "issuedWidthInInches": line.claimDetailRecords[0].widthInInches ||0,
    "issuedLengthInInches": line.claimDetailRecords[0].lengthInInches ||0,
     "issuedQuantity": line.quantity ||0,
    "unitOfMeasureCode": line.unitOfMeasure.unitOfMeasureCode ||'',
     "type": line.rcsCodes.rcsCode ||'',
    "invNumber": document.documentNumber||'',
    "invDate": document.documentDate || '',
    "dyeLot": line.dyeLot ||'',
    "grade": line.grade ||'',
    "price": line.price ||0,
    "lineAmountUsd":line.lineAmount||0,
    "reasonCode": line.claimReasonDefinition.claimReasonCode ||'',
    "reasonCodeDescription":line.claimReasonDefinition.claimReasonDescription,
      "bolNumber": document.billofLading ||'',
    "poNumber": document.purchaseOrderNumber ||'',
    "orderNumber": document.orderNumber ||'',
      "lineAmount": line.lineAmount||0,
    "lineNumber": line.lineNumber,
    "returnEligible":line.claimReasonDefinition.returnEligible||'',
    "productCode":line.productCode ||'',
    "squareFeet":line.squareFeet ||''
        };

    this.formattedLine.push(formattedLineItem);
  } else {
    this.formattedLine = this.formattedLine.filter(item => item.lineNumber !== line.lineNumber);
  }
  this.isAnyLineSelected = this.formattedLine.length > 0;
  console.log(this.formattedLine, 'formattedLine');
}
getTotalAmount1(document: any): number {
  if (document && document.claimLineDetails) {
    return document.claimLineDetails.reduce((total: any, line: any) => {
      // console.log('Current Total:', total);
      // console.log('Current Line:', line);
     return total + (line.lineAmount || 0);
    }, 0);
  }
  return 0;
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
    if(typeId ==11){
      let filteredValues = this.claimAddresses.filter(address => address.addressTypeId === typeId);
     
      return filteredValues[filteredValues.length -1]
    }else{
      return this.claimAddresses.find(address => address.addressTypeId === typeId);
    }
  }
  return null;

}
onCorrespondenceToggle(event: Event): void {
  const isChecked = (event.target as HTMLInputElement).checked;
  this.sendCorrespondence = isChecked;
 
}
onLocationChange(event: MatSelectChange): void {
  this.rdcLocationId = event.value;
  console.log(this.rdcLocationId ,'this.rdcLocationId ')
 
}
onCommentChange(newComment: string): void {
  this.comment = newComment;
 
}
onSubmit(): void {
  // Optionally handle form submission if needed
  if (this.addressForm.valid) {
    this.addAddress();
  }
}
addAddress(): void {
  const address = {
    addressTypeId: 0,
    nameAddress: this.addressForm.value.nameAddress,
    address: this.addressForm.value.address,
    address2: this.addressForm.value.address2,
    city: this.addressForm.value.city,
    state: this.addressForm.value.state,
    zip: this.addressForm.value.zip
  };

  this.claimAddresses.push(address);

  // Optionally reset the form after adding
  //this.addressForm.reset();
}

public getCountriesList() {
  this.countries = [];
  this.initialClaimService.getCountriesList().subscribe(
    (data) => {
      this.countries = data;
     const usaCountryObject = this.countries[239];
      //let jsonArray = JSON.parse(this.countries);
      let isoCountryIdToDelete = 240;
      let newObject = {
        createdByUserId: usaCountryObject.createdByUserId,
        modifiedByUserId: usaCountryObject.modifiedByUserId,
        createdDateTime: usaCountryObject.createdDateTime,
        modifiedDateTime: usaCountryObject.modifiedDateTime,
        isoCountryId: usaCountryObject.isoCountryId,
        isoCountryCode: usaCountryObject.isoCountryCode,
        isoCountryCode3: usaCountryObject.isoCountryCode3,
        isoCountryName: usaCountryObject.isoCountryName,
        statusId: usaCountryObject.statusId,
      };
      let indexToDelete = this.countries.findIndex(
        (item: any) => item.isoCountryId === isoCountryIdToDelete
      );

      // Check if the object with the specified isoCountryId exists in the array
      if (indexToDelete !== -1) {
        // Remove the object at the found index
        this.countries.splice(indexToDelete, 1);
        // Add the new object at the first index
        this.countries.unshift(newObject);
      }
      this.selectedCountry = 240;
      this.getCountryCodeByStates();
    },
    (error) => {
      this.countries = error;
    }
  );
}

public getCountryCodeByStates() {
  this.filteredStates = [];
  // this.store.dispatch(getCountryCodeData());
  this.initialClaimService
    .getCountryCodeByStates(Number(this.selectedCountry))
    .subscribe(
      (data) => {

        // Check if data is an array and if it's not empty
        this.selectedCountryHasStates =
          Array.isArray(data) && data.length > 0;


        this.filteredStates = data;
        // this.getSelectedDialCodeByCountryId();
      },
      (error) => {
        // this.store.dispatch(getCountryStateCodeFailure({ error }));
        this.filteredStates = error;
      }
    );
}


otherAddress(): void {
  const address = {
    addressTypeId: 11,
    fullName: this.otherFormGroup.value.name,
    addressLine1: this.otherFormGroup.value.address,
    addressLine2: this.otherFormGroup.value.address2,
    city: this.otherFormGroup.value.city,
    country: this.selectedCountry,
    state:this.otherFormGroup.value.stateother,
    comments:this.comment
  };

  this.claimAddresses.push(address);
}
getTotalLineAmount(): number {
  return this.selectedLine.reduce((total, line) => total + (line.lineAmount || 0), 0);
}

removeSelectedLines() {
  console.log(this.chekedLineItem, "thischeckeitem");

  // Extract line numbers to remove from checked items
  const lineNumbersToRemove = this.chekedLineItem.map((item:any) => item.lineNumber);

  console.log(lineNumbersToRemove, 'lineNumbersToRemove');

  // Iterate over each object in the this.selectedLine array
  this.selectedLine = this.selectedLine.map(item => {
    
    // Check if the selectedLine object exists and contains a lineNumber to be removed
    if (lineNumbersToRemove.includes(item.lineNumber)) {
      item = null; // Set to null or remove the line as needed
    }
    return item;
  });

  // Remove null or empty entries from selectedLine if necessary
  this.selectedLine = this.selectedLine.filter(item => item !== null);

  console.log(this.selectedLine, 'this.selectedLine afterRemove');

  // Update uniqueDocuments after removal
  this.uniqueDocuments = this.getUniqueDocuments(this.selectedLine);
  console.log(this.uniqueDocuments, 'this.uniqueDocuments');

  // Uncheck corresponding checkboxes in filteredClaimDocuments
  this.filteredClaimDocuments.forEach(document => {
    document.claimLineDetails.forEach((line:any) => {
      if (lineNumbersToRemove.includes(line.lineNumber)) {
        line.isChecked = false; // You can store the checked status in a property
      }
    });
  });
  localStorage.setItem('selectedLine', JSON.stringify(this.selectedLine));
  this.isAnyLineSelected = false
}
}
