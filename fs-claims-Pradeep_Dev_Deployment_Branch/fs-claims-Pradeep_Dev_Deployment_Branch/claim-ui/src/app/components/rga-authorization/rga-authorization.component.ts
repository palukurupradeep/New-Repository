import { Component, Input, OnInit } from '@angular/core';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { ClaimDetailService } from '../../ngrx/store/services/claim-detail.service';
import { ActivatedRoute, NavigationEnd, NavigationExtras, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { debounceTime, filter } from 'rxjs';
import { InitiateClaimService } from 'src/app/ngrx/store/services/initiate-claim.service';
import { ToasterComponent } from '../toaster/toaster.component';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
import { DatePipe } from '@angular/common';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-rga-authorization',
  templateUrl: './rga-authorization.component.html',
  styleUrls: ['./rga-authorization.component.scss']
})
export class RGAAuthorizationComponent implements OnInit {
  recipientData: any[]=[];
  locationData: any[]=[];
  selectedLine: any[]=[];
  rgaDetails:any[]=[];
  sendCorrespondence: boolean = false;
  pickupFrom: string = "Ship To";
  showAddressFields: boolean = false;
  claimAddresses: any[]=[];
 // claimAddressesData: any[]=[];
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
  comments: string = '';
  filteredClaimDocuments: any[]=[]
  chekedLineItem: any[]=[];
  isCorrespondenceToggled: boolean = false; 
  addressForm: FormGroup;
  public countries: any = [];
  public filteredStates: any = [];
  public selectedDailcode: string = '';
  public selectedCountryHasStates: boolean = false;
  public selectedCountry : any;
  public otherFormGroup!:FormGroup
  formattedLine: any[]=[];
  rdcLocationCode: string= '';
  @Input() screenWidth = 0;
  public isSideNavCollapsed: boolean = true;
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
    private initialClaimService: InitiateClaimService,
    private fb: FormBuilder,
    private datePipe: DatePipe){
      this.emailForm = this.fb.group({
        email: ['', [Validators.required, Validators.email]]
      });
      this.addressForm = this.fb.group({
        nameAddress: [''],
        address: [''],
        address2: [''],
        city: [''],
        state: [''],
        zip: ['']
      });
  
      // Debounce input changes to avoid excessive updates
      this.addressForm.valueChanges.pipe(debounceTime(500)).subscribe(() => {
        if (this.addressForm.valid) {
          this.addAddress();
        }
      });
      this.otherFormGroup = this.fb.group({
        name:[''],
        address:[''],
        address2:[''],
        stateother:[''],
        city:['']
      })
        // Debounce input changes to avoid excessive updates
        this.otherFormGroup.valueChanges.pipe(debounceTime(500)).subscribe(() => {
          if (this.otherFormGroup.valid) {
            this.otherAddress();
          }
        });
    }
  ngOnInit(): void {
     localStorage.removeItem('setViewEditDAta');
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
   
      this.route.paramMap.subscribe(params => {
        console.log(window.history.state.sellingCompany,'sellingCompany..');
        console.log(window.history.state.claimId,'claimId');
        const savedLines = localStorage.getItem('selectedLine');
        if (savedLines && JSON.parse(savedLines).length  > 0) {
          this.selectedLine = JSON.parse(savedLines);
        } else {
         
          if (window.history.state && window.history.state.selectedLine) {
            this.selectedLine = window.history.state.selectedLine;
          }
        }
     

        this.rgaDetails = window.history.state.rgaDetails;
        this.claimAddresses = window.history.state.claimAddresses;
        this.claimId = window.history.state.claimId;
        this.claimDocuments= window.history.state.claimDocuments;
        this.rdcLocationCode=window.history.state.rdcLocationCode;
        this.sellingCompany =window.history.state.sellingCompany;
        this.region =window.history.state.region;
      this.division=window.history.state.division;
      this.claimAreaId =window.history.state.claimAreaId;
      this.customerNumber= window.history.state.customerNumber;
      console.log(this.sellingCompany,'sellingCompany')
console.log(this.region,'region')
console.log(this.division,'division')
console.log(this.claimAreaId,'claimAreaId')
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


// Log the filtered claim documents,'
console.log(this.filteredClaimDocuments,'filteredClaimDocuments');
console.log(this.claimAddresses,'claimAddressesbefore');
// Assuming fetchCountryAndState returns an observable or a promise
this.claimAddresses.forEach((address, index) => {
  this.claimDetailService.fetchCountryAndState(address.countryId, address.stateId).subscribe(response => {
    // Update the respective object with stateName and countryName
    this.claimAddresses[index].stateName = response.body.stateName;
    this.claimAddresses[index].countryName = response.body.countryName;
  });
});
console.log(this.claimAddresses,'claimAddressesafter');

        this.getReturnLocations(this.rdcLocationCode);
       // this.claimNumber =window.history.state.claimNumber;
        this.claimNumber =this.selectedLine[0].claimNumber;
       // this.customerNumber= window.history.state.customerNumber;
      //   this.claimAddresses.forEach(address => {
      //     if (address.addressTypeId === 2) {
      //       console.log(address,'address')
      //         const { fullName, claimAddressId,addressLine1, addressLine2, city, countryId, postalCode } = address;
      //         this.claimAddressesData.push({ fullName, claimAddressId, addressLine1, addressLine2, city, countryId, postalCode });
      //     }
      // });
     // console.log(this.claimAddressesData,' this.claimAddressesData')
        console.log( this.claimAddresses,' this.claimAddresses');
       console.log( this.selectedLine,' this.selectedLineData');
       console.log( this.rgaDetails,' this.rgaDetailsDataauth')
       console.log( this.claimDocuments,' this.claimDocuments')
       console.log( this.selectedLine[0].claimNumber,' this.claimNumber')
       console.log( this.customerNumber,' this.customerNumber')
       this.fetchRecipients(this.claimId);
       this.uniqueDocuments = this.getUniqueDocuments(this.selectedLine);
       console.log(this.uniqueDocuments ,'this.uniqueDocuments ')
       this.filteredDocuments =this.getUniqueDocuments(this.claimDocuments);
      

      });
   //this.fetchRgaDetails(542);
   this.getCountriesList();
  }
   

   
    public getBodyClass(): string {
      return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
    }

    public onToggleSideNav(data: SideNavToggle): void {
      this.screenWidth = data.screenWidth;
      this.isSideNavCollapsed = data.collapsed;
    }

public fetchRecipients(claimId:number){
  this.claimDetailService.fetchRecipients(claimId).subscribe(data=>{
    this.recipientData=data.body
    console.log(this.recipientData,'recipientData')
  })
}


public getReturnLocations(locationCode :string){
  this.claimDetailService.getReturnLocations(locationCode).subscribe(data=>{
    this.locationData = data.body;
    console.log(data,'locData')
  })
}

removeRecipient(index: number): void {
  this.recipientData.splice(index, 1);
  this.isAnyLineSelected = false;
}



onPickupOptionChange(option: string): void {
    if (option === "Other") {
        this.showAddressFields = true;
    } else {
        this.showAddressFields = false;
    }
}



  

saveAsDraft(status: string) {
 
  console.log(this.claimAddresses, 'addAddress');
  console.log(status, 'status');
  console.log(this.customerNumber, 'customerNumber');
  console.log(this.claimNumber, 'claimNumber');
  console.log(this.isToggled, 'isToggled');
  console.log(this.recipientData, 'recipientData');
  console.log(this.selectedLine, 'selectedLine');

  const claimRgaLineDTO = this.selectedLine.flatMap(line => {
    // Directly access selectedLine if it's an object
    const selectedLineItem = line.selectedLine;

    // Return an array with the single selectedLineItem mapped to the desired format
    return [{
      "claimRgaLineId": 0,
      "invoiceLineNumber":selectedLineItem.invoiceLineNumber,
      "rgaLineNumber": selectedLineItem.lineNumber,
      "claimLineId": line.claimLineId,
      "colorNumber":selectedLineItem.colorNumber,
           "invNumber":line.documentNumber,
      "invDate":line.documentDate,
      "rollNumber": selectedLineItem.rollNumber,
      "lot":selectedLineItem.dyeLot,
      "type":selectedLineItem.rcsCodes.rcsCode,
      "grade":selectedLineItem.grade,
      "price":selectedLineItem.unitPriceUsd,
      "lineAmount":selectedLineItem.lineAmountUsd,
      "styleNumber": selectedLineItem.styleNumber,
      "unitOfMeasureCode": selectedLineItem.unitOfMeasure.unitOfMeasureCode,
      "issuedQuantity": selectedLineItem.quantity,
      "issuedAmount": selectedLineItem.lineAmountUsd,
      "rdcLocationId": this.locationId,
      "reasonCode": selectedLineItem.claimReasonDefinition.claimReasonCode,
      "reasonCodeDescription":selectedLineItem.claimReasonDefinition.claimReasonDescription,
      "issuedWidthInFeet": selectedLineItem.claimDetailRecords[0]?.widthInFeet || 0,
      "issuedLengthInFeet": selectedLineItem.claimDetailRecords[0]?.lengthInFeet || 0
    }];
  });

  // Dynamically extract values from the first item in selectedLine
  const firstLine = this.selectedLine[0] || {};

  const saveDraftPayload = {
    "createdByUserId": firstLine.createdByUserId || 0,
    "modifiedByUserId": firstLine.modifiedByUserId || 0,
    "claimRgaHeaderId": 0,
    "rgaStatusCode": 'P',
    "claimId": this.claimId,
    "customerNumber": this.customerNumber,
    "claimReasonCode": firstLine.claimReasonCode || '',
    "amountUsd": this.getCombinedTotal() || 0,
    "claimAmountForeign": 0, // Adjust as needed
    "claimRgaLineDTO": claimRgaLineDTO,
    "comments": this.comments,
    "sendMailToTm": this.isToggled,
    "sendCorrespondence": this.isCorrespondenceToggled,
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
  };
const setViewEditDAta ={
  "recipientData":this.recipientData,
  "createdByUserId":firstLine.modifiedByUserId,
  "modifiedByUserId": firstLine.modifiedByUserId,
  "issuedDate":this.datePipe.transform(new Date(), 'dd/MM/yyyy'),
  "claimId": this.claimId,
  "sellingCompany": this.sellingCompany,
  "region": this.region,
  "division":this.division,
  "claimAreaId":this.claimAreaId,
 "claimRgaLineDTO": claimRgaLineDTO,
 "comments": this.comments,
 "sendMailToTm": this.isToggled,
 "sendCorrespondence": this.isCorrespondenceToggled,
 "customerNumber": this.customerNumber,
 "amountUsd": this.getCombinedTotal() || 0,
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
        localStorage.removeItem('selectedLine');
  
  
     
          this.navigateToDetails();
       
      },
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



removeSelectedLines() {
  console.log(this.chekedLineItem, "thischeckeitem");

  // Extract line numbers to remove from checked items
  const lineNumbersToRemove = this.chekedLineItem.map(item => item.lineNumber);

  console.log(this.selectedLine, 'selectedline');

  // Iterate over each object in the this.selectedLine array
  this.selectedLine = this.selectedLine.map(item => {
    // Check if the selectedLine object exists and contains a lineNumber to be removed
    if (item.selectedLine && lineNumbersToRemove.includes(item.selectedLine.lineNumber)) {
      item.selectedLine = null; // Set to null or remove the line as needed
    }
    return item;
  });

  // Remove null or empty entries from selectedLine if necessary
  this.selectedLine = this.selectedLine.filter(item => item.selectedLine !== null);

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
  this.chekedLineItem =[];
}


getUniqueDocuments(lines: any[]): any[] {
  console.log(lines, 'uninisedlines');

  const uniqueDocuments = lines.reduce((acc, line) => {
    if (line.selectedLine && typeof line.selectedLine === 'object') {
      console.log(line.selectedLine, 'selectedLineObject');
      
      if (!acc.some((item: any) => item.documentNumber === line.documentNumber)) {
        console.log(line, 'lineToPush');
        acc.push({
          documentNumber: line.documentNumber,
          documentDate: line.documentDate, 
          orderNumber: line.orderNumber 
        });
      }
    } else {
      console.log('Unexpected structure: selectedLine is not an object', line);
    }
    return acc;
  }, []);

  return uniqueDocuments;
}



getTotalAmount(documentNumber: string): number {
  return this.selectedLine
      .filter(line => line.documentNumber === documentNumber)
      .reduce((total, line) => {
        // Ensure line.selectedLine is defined and is an object
        if (line.selectedLine && typeof line.selectedLine === 'object') {
          return total + (line.selectedLine.lineAmountUsd || 0);
        }
        return total;
      }, 0);
}

getCombinedTotal(): number {
  return this.uniqueDocuments
      .map(document => this.getTotalAmount(document.documentNumber))
      .reduce((combinedTotal, documentTotal) => combinedTotal + documentTotal, 0);
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
    this.chekedLineItem = this.chekedLineItem.filter(selected => selected.lineNumber !== line.lineNumber);
    console.log(this.chekedLineItem,'chekedLineItemafter')
  }
  this.isAnyLineSelected = this.chekedLineItem.length > 0;
}





closeClaimLinesModal(){
 
  const modelDiv = document.getElementById('claimLinesModal');
  if (modelDiv != null) {
    modelDiv.style.display = 'none';
     this.chekedLineItem = [];
  this.isAnyLineSelected = false;
  this.formattedLine = [];
  
  }

}

addClaimLinesModal(){
  console.log(this.filteredClaimDocuments,'filteredClaimDocuments')
  const modelDiv = document.getElementById('claimLinesModal');
  if (modelDiv != null) {
    modelDiv.style.display = 'block';
    
  }
}




// addSelectedClaimLines(): void {
//   console.log(this.formattedLine, 'selectedLine');

//   // Iterate through the formatted lines
//   this.formattedLine.forEach((line) => {
//     // Check if the claimReasonCode is "ALL"
//     if (line.selectedLine.claimReasonDefinition.claimReasonCode === "ALL") {
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
//         const errorMessages = this.prepareRGAData.codeAndErrorMsgDTOS
//     .map((errorObj: any) => errorObj.errorMessage)
//     .join(', ');
  
//   this.toasterService.showToast('error', errorMessages);
//        // this.toasterService.showToast('error', this.prepareRGAData.codeAndErrorMsgDTOS);
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
addSelectedClaimLines(): void {
  console.log(this.formattedLine, 'selectedLine');

  // Array to store DTOs for the API payload
  let dtosArray: { claimLineId: any; reasonCode: any; squareFeet: any; quantity: any; productCode: any; }[] = [];

  // Iterate through the formatted lines for validation
  for (let line of this.formattedLine) {
    // Check if the claimReasonCode is "ALL"
    if (line.selectedLine.claimReasonDefinition.claimReasonCode === "ALL") {
      this.toasterService.showToast('error', 'Line does not have Reason Code, please update the reason code to Prepare RGA');
      return; // Exit the function if claimReasonCode is "ALL"
    }

    // Check if returnEligible is false
    if (!line.selectedLine.claimReasonDefinition.returnEligible) {
      this.toasterService.showToast('error', 'For this reason code, return is not eligible');
      return; // Exit the function if returnEligible is false
    }

    // If validations pass, prepare the DTO object
    let obj = {
      "claimLineId": line.claimLineId,
      "reasonCode": line.selectedLine.claimReasonDefinition.claimReasonCode,
      "squareFeet": line.selectedLine.squareFeet,
      "quantity": line.selectedLine.quantity,
      "productCode": line.selectedLine.productCode
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





navigateToDetails(){
  const toastType: 'success' | 'error' | 'warning' = 'success'; 
  const message = 'RGA is successfully saved as draft';
  this.toasterService.showToast(toastType, message);

  this.router.navigate(['/claim-detail', this.claimNumber]);
  
}
onToggle(event: Event) {
  this.isToggled = (event.target as HTMLInputElement).checked;
}
// Check if the lineNumber exists in selectedLine

isChecked(lineNumber: number): boolean {
  return this.selectedLine.some(item => item.selectedLine && item.selectedLine.lineNumber === lineNumber);
}

getTotalAmount1(document: any): number {
  if (document && document.claimLineDetails) {
    return document.claimLineDetails.reduce((total:any, line:any) => total + (line.lineAmountUsd || 0), 0);
  }
  return 0;
}
opencancelModal(){
  const modelDiv = document.getElementById('cancelModal');
  if (modelDiv != null) {
    modelDiv.style.display = 'block';
    
  }
}
closecancelModal(){
 
  const modelDiv = document.getElementById('cancelModal');
  if (modelDiv != null) {
    modelDiv.style.display = 'none';
    
  }

}
confirmModal(){
  this.navigateToDetails();
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
  // const typeId = addressTypeIdMap[this.selectedOption as keyof typeof addressTypeIdMap];
  // if (typeId !== null) {
  //   return this.claimAddresses.find(address => address.addressTypeId === typeId);
  // }
  // return null;
}
isSaveButtonEnabled(): boolean {
  return this.recipientData.length > 0 && !!this.locationId;
}
onCorrespondenceToggle(event: Event): void {
  const inputElement = event.target as HTMLInputElement;
  this.isCorrespondenceToggled = inputElement.checked;
  console.log('Send Correspondence:', this.isCorrespondenceToggled);
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
    comments:this.comments
  };

  this.claimAddresses.push(address);
}
// onCheckboxChange(document: any, line: any, event: any): void {
//   this.isAnyLineSelected= true;
//   console.log(document,'document');
//   console.log(line,'line');
//   console.log(event,'event');
//   const isChecked = event.target.checked;
//   this.formattedLine = [{
//   "claimDocumentId": document.claimDocumentId,
//   "documentNumber": document.documentNumber,
//   "createdByUserId": line.createdByUserId,
//   "modifiedByUserId": line.modifiedByUserId,
//   "documentTypeCode":  document.documentType.documentTypeCode,
//   "claimReasonCode": document.claimReasonDefinition.claimReasonCode,
//   "claimLineId": line.claimLineId,
//   "lineNumber":line.lineNumber,
//   "documentDate": document.documentDate,
//   "orderNumber":  document.orderNumber,
//   "selectedLine":line
// }]
// console.log(this.formattedLine,'formattedLine')
// }
onCheckboxChange(document: any, line: any, event: any): void {
  const isChecked = event.target.checked;
  this.isAnyLineSelected = true;

  if (isChecked) {
    const formattedLineItem = {
      "claimDocumentId": document.claimDocumentId,
      "documentNumber": document.documentNumber,
      "createdByUserId": line.createdByUserId,
      "modifiedByUserId": line.modifiedByUserId,
      "documentTypeCode": document.documentType.documentTypeCode,
      "claimReasonCode": document.claimReasonDefinition.claimReasonCode,
      "claimLineId": line.claimLineId,
      "lineNumber": line.lineNumber,
      "documentDate": document.documentDate,
      "orderNumber": document.orderNumber,
      "selectedLine": line
    };

    this.formattedLine.push(formattedLineItem);
  } else {
    this.formattedLine = this.formattedLine.filter(item => item.lineNumber !== line.lineNumber);

  }
 this.isAnyLineSelected = this.formattedLine.length > 0;
  console.log(this.formattedLine, 'formattedLine');
}
}