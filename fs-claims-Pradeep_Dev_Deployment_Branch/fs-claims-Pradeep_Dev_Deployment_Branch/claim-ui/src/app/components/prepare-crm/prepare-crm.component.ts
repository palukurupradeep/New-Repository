import { Component, Input, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, NavigationEnd, NavigationExtras, Router } from '@angular/router';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';
import { ToasterComponent } from '../toaster/toaster.component';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
import { filter } from 'rxjs';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-prepare-crm',
  templateUrl: './prepare-crm.component.html',
  styleUrls: ['./prepare-crm.component.scss']
})
export class PrepareCrmComponent implements OnInit {

  @Input() selectedLine: any=[];
 // @Input() claimList: any = [];
 @Input() screenWidth = 0;
public isSideNavCollapsed: boolean = true;
 
@Input() claimDetails: any=[];
public serviceTypes: any[]=[];
public documentsByClaimId: any[]=[];
public crmDetailsByClaimIdAndCrmNumber: any;
public selectedServiceTypeId :any;
public restockFeeData: any = {};
public showUserName: boolean = true;
public showInvoice: boolean = false;
public showCustomer: boolean = false;
public selectedRga: string = 'No RGA';
public selectedDebit: string = 'No Debit';
public associatedRgaNumber: number = 0;
public emailAddress: boolean = false;
public  memoComments: string = '';
public applyMethod: string = 'autoApply';
public autoApply: boolean = true;
public doNotApply: boolean = false;
public manualApply: boolean = false;
public leaveClaimOpen: boolean = true;
public closeClaimWithTasks: boolean = false;
public leaveInspectionOpen: boolean = false;
public removeDispute: boolean = false;
public atkAttempted: boolean = false;
public atkAccepted: boolean = false;
public stkAttempted: boolean = false;
public stkAccepted: boolean = false;
public claimId: number = 0;
public claimLevelTotalAmount: number = 0; 
public fetchedDocumnetsByClaimId: any[]=[];
claimAddresses: any[]=[];
claimAddressesData: any[]=[];
uniqueDocuments: any[]=[];
  claimDocuments: any[]=[];
  filteredDocuments: any[]=[];
  isAnyLineSelected = false;
  claimNumber: string='';
  customerNumber: string='';
  crmNumber: number=0;
  crmHeaderId:number=0;
  chekedLineItem: any[]=[];
  filteredClaimDocuments: any[]=[];
  recipientData: any[]=[];
  formattedLine: any[]=[];
  selectedRecipient: any = null;
  editRestockFeeData:number=0;
  selectedOption = 'shipTo';
  private previousUrl: string = '';
  previousClaimNumber: string = '';
  restockFeePercentage:number=0
  rgaNumber: number=0;
  rgaNumberList: any[]=[];
  shipToAddress: any;
constructor( private snackBar: MatSnackBar,
  private claimDetailService : ClaimDetailService,
  private toasterService: ToasterService,
  private route: ActivatedRoute,
    private router: Router,
    private bodyClassService: BodyClassService,
){}

ngOnInit() {
  this.router.events.pipe(
    filter((event): event is NavigationEnd => event instanceof NavigationEnd) // Type guard
  ).subscribe((event: NavigationEnd) => {
    const currentUrl = event.urlAfterRedirects;

    if (this.previousUrl && this.previousUrl !== currentUrl) {
      // Remove selectedLine from localStorage on navigation change
      localStorage.removeItem('crmSelectedLine');
    }

    // Check if claimNumber has changed
    if (this.previousClaimNumber && this.previousClaimNumber !== this.claimNumber) {
      // Remove selectedLine from localStorage if claimNumber has changed
      localStorage.removeItem('crmSelectedLine');
    }

    // Update previousUrl and previousClaimNumber
    this.previousUrl = currentUrl;
    this.previousClaimNumber = this.claimNumber;
  });

  this.fetchServiceToKeepType();
    this.route.paramMap.subscribe(params => {
    const savedLines = localStorage.getItem('crmSelectedLine');
    if (savedLines && JSON.parse(savedLines).length  > 0) {
      console.log('insidecrm')

      this.selectedLine = JSON.parse(savedLines);
      console.log(this.selectedLine ,'this.selectedLine crm..storrrrrrrrrrrrrrrrrr')
      this.getAllowanceToKeepData()

    } else {
     
      if (window.history.state && window.history.state.selectedLine) {
        this.restockFeePercentage =window.history.state.restockFeePercentage;
        this.selectedLine = window.history.state.selectedLine;
        console.log(this.selectedLine[0].selectedLine.claimReasonDefinition.claimReasonCode,'this.selectedLine crm..2hist')
        this.getAllowanceToKeepData()
       
        this.selectedLine.forEach((line:any) => {
          if (line.selectedLine && line.selectedLine.lineAmountUsd) {
              // Calculate the restocking fee
              const restockingFee = (this.restockFeePercentage / 100) * line.selectedLine.lineAmountUsd;
           
              // Store the remaining value after the restocking fee is applied
             // this.editRestockFeeData = Number((line.selectedLine.lineAmountUsd - restockingFee).toFixed(2));
             if (restockingFee !== 0) {
              this.editRestockFeeData = Number((restockingFee).toFixed(2));
            } else {
              this.editRestockFeeData = 0; // Or set to null/undefined if you prefer
            }
            

              
              // Update the lineAmountUsd to reflect the new value after deducting the fee
              line.selectedLine.lineAmountUsd -= restockingFee;
          }
      });
        console.log(this.selectedLine,'selectedLine')
      }
    }
 
   
   
    this.claimAddresses = window.history.state.claimAddresses;
    console.log(this.claimAddresses,'claimAddress')
    this.claimId = window.history.state.claimId;
    this.claimDocuments= window.history.state.claimDocuments;
   // this.claimNumber =window.history.state.claimNumber;
    this.claimNumber =this.selectedLine[0].claimNumber;
    this.customerNumber= window.history.state.customerNumber;
    this.crmNumber =window.history.state.crmNumber;
    this.crmHeaderId =window.history.state.crmHeaderId;
    this.shipToAddress = this.claimAddresses.find(
      (address: any) => address.addressTypeId === 2
    );
    //this.editRestockFeeData = window.history.state.editRestockFeeData;
   // this.restockFeePercentage =window.history.state.restockFeePercentage;
    // Filter claimLineDetails within each document to include only items with detailTypeCode: "MERCH"
this.filteredClaimDocuments = this.claimDocuments
.filter(document => document.documentType.documentTypeCode === "INV")
.map(document => {
  return {
    ...document,
    claimLineDetails: document.claimLineDetails.filter(
      (lineItem: any) => lineItem.detailType.detailTypeCode === 'MERCH'  && 
      lineItem.statusId !== 3
    )
  };
});

    this.claimAddresses.forEach(address => {
      //need to check which id to be passed
      if (address.addressTypeId === 6) {
        console.log(address,'address')
          const { fullName, claimAddressId,addressLine1, addressLine2, city, countryId, postalCode } = address;
          this.claimAddressesData.push({ fullName, claimAddressId, addressLine1, addressLine2, city, countryId, postalCode });
      }
  });
  if(this.editRestockFeeData){
    
    this.memoComments = `Restocking fees of $ ${this.editRestockFeeData} is deducted`;

  } else  {
    this.memoComments = '';
  }
  console.log(this.claimAddressesData,' this.claimAddressesData')
    console.log( this.claimAddresses,' this.claimAddresses');
   console.log( this.selectedLine,' this.selectedLineData');
   
   console.log( this.claimDocuments,' this.claimDocuments')
   console.log( this.selectedLine[0].claimNumber,' this.claimNumber')
   console.log( this.customerNumber,' this.customerNumber')
   this.fetchRecipients(this.claimId);
   this.fetchRgaNumber(this.claimId);
       this.uniqueDocuments = this.getUniqueDocuments(this.selectedLine);
       console.log(this.uniqueDocuments ,'this.uniqueDocuments ')
       this.filteredDocuments =this.getUniqueDocuments(this.claimDocuments);
  


  });
  
}



public getBodyClass(): string {
  return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
}

public onToggleSideNav(data: SideNavToggle): void {
  this.screenWidth = data.screenWidth;
  this.isSideNavCollapsed = data.collapsed;
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
}


public fetchRecipients(claimId:number){
  this.claimDetailService.fetchRecipients(claimId).subscribe(data=>{
    this.recipientData=data.body
    console.log(this.recipientData,'recipientData')
  })
}
getUniqueDocuments(lines: any[]): any[] {
  
  const uniqueDocuments = lines.reduce((acc, line) => {
      if (!acc.some((item:any) => item.documentNumber === line.documentNumber)) {
          acc.push({
              documentNumber: line.documentNumber,
              documentDate: line.documentDate, // Adjust as per your data structure
              orderNumber: line.orderNumber // Adjust as per your data structure
          });
      }
      return acc;
  }, []);

  return uniqueDocuments;
}
getTotalAmount(documentNumber: string): number {
  return this.selectedLine
      .filter((line:any) => line.documentNumber === documentNumber)
      .reduce((total:any, line:any) => {
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

  

handleSpecialInstruction(option: string) {
  switch (option) {
    case 'EndUser':
      this.showUserName = true;
      this.showInvoice = false;
      this.showCustomer = false;
      break;
    case 'CustomerChargeback':
      this.showUserName = false;
      this.showInvoice = false;
      this.showCustomer = true;
      break;
    case 'InvoicePOOrder':
      this.showUserName = false;
      this.showInvoice = true;
      this.showCustomer = false;
      break;
    default:
      this.showUserName = false;
      this.showInvoice = false;
      this.showCustomer = false;
      break;
  }

  console.log({
    showUserName: this.showUserName,
    showInvoice: this.showInvoice,
    showCustomer: this.showCustomer
  });
}

  // get selectedRgaValue(): string {
  //   return this.selectedRga === 'No RGA' ? '0' : this.selectedRga;
  // }

  get selectedDebitValue(): string {
    return this.selectedDebit === 'No Debit' ? '0' : this.selectedDebit;
  }

  // Example method to log values based on your requirement
  logSelections() {
    console.log('Associated RGA Number:', this.associatedRgaNumber);
  }
 

  toggleEmailAddress(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    this.emailAddress = inputElement.checked;
    console.log(this.emailAddress);
  
    if (this.emailAddress) {
      // Fetch data from recipientData and select the first recipient as an example
      this.selectedRecipient = this.recipientData[0]; // Adjust logic as needed
    } else {
      this.selectedRecipient = null; // Clear the selected recipient when unchecked
    }
  }
  updateApplyMethod(method: string) {
    this.autoApply = method === 'autoApply';
    this.doNotApply = method === 'doNotApply';
    this.manualApply = method === 'manualApply';
    console.log({
      autoApply: this.autoApply,
      doNotApply: this.doNotApply,
      manualApply: this.manualApply
    });
  }
  updateCheckboxState(option: string) {
    this.leaveClaimOpen = option === 'leaveClaimOpen';
    this.closeClaimWithTasks = option === 'closeClaimWithTasks';
    this.leaveInspectionOpen = option === 'leaveInspectionOpen';
    this.removeDispute = option === 'removeDispute';

    console.log({
      leaveClaimOpen: this.leaveClaimOpen,
      closeClaimWithTasks: this.closeClaimWithTasks,
      leaveInspectionOpen: this.leaveInspectionOpen,
      removeDispute: this.removeDispute
    });
  }
 
  toggleCheckboxState(property: string) {
    switch (property) {
      case 'atkAttempted':
        this.atkAttempted = !this.atkAttempted;
        if (this.atkAttempted) {
          this.stkAttempted = false;
          this.atkAccepted = true;
          this.stkAccepted = false;
        }
        break;
      case 'atkAccepted':
        this.atkAccepted = !this.atkAccepted;
        if (this.atkAccepted) {
          this.atkAttempted = true;
          this.stkAccepted = false;
          this.stkAttempted = false;
        } else if (!this.stkAccepted) {
          this.atkAttempted = true;
        }
        break;
      case 'stkAttempted':
        this.stkAttempted = !this.stkAttempted;
        if (this.stkAttempted) {
          this.atkAttempted = false;
          this.stkAccepted = true;
          this.atkAccepted = false;
        }
        break;
      case 'stkAccepted':
        // Toggle the state of stkAccepted directly
        this.stkAccepted = !this.stkAccepted;
        if (this.stkAccepted) {
          this.stkAttempted = true;
          this.atkAttempted = false;
          this.atkAccepted = false;
  
          // Ensure a valid service type is selected
          if (!this.selectedServiceTypeId) {
            this.selectedServiceTypeId = this.serviceTypes[1]?.serviceToKeepTypeId || null;
          }
        } else {
          // Reset stkAttempted if stkAccepted is unchecked
          this.stkAttempted = false;
        }
        break;
    }
  
    // Debug output to track state changes
    console.log('Current State:', {
      atkAttempted: this.atkAttempted,
      atkAccepted: this.atkAccepted,
      stkAttempted: this.stkAttempted,
      stkAccepted: this.stkAccepted,
    });
  }
  

  saveCrmDraft(param:string,page:string){
 
    const saveCrmDraftPayload = {
      "createdByUserId": Number(localStorage.getItem("userId")),
      "modifiedByUserId": Number(localStorage.getItem("userId")),
      "claimCrmHeaderId": 0,
      "crmStatusCode": param,
      "claimId": this.claimId,
      "claimCustomerNumber": this.customerNumber,
      "creditCustomerNumber": "",
      "selectedRga": this.selectedRga,
      "claimReasonCode": this.selectedLine[0].claimReasonCode,
      "amountUsd":(this.getCombinedTotal()).toFixed(2)|| 0,
      "amountForeign": this.selectedLine[0].selectedLine.lineAmountForeign,
      "preparedByUserId": Number(localStorage.getItem("userId")),
      "currentUserId": Number(localStorage.getItem("userId")),
      "issuedByUserId": Number(localStorage.getItem("userId")),
      "associatedRgaNumber": 0,
      "emailAddress": this.emailAddress,
      "autoApply": this.autoApply,
      "doNotApply": this.doNotApply,
      "manualApply": this.manualApply,
      "leaveClaimOpen": this.leaveClaimOpen,
      "closeClaimWithTasks": this.closeClaimWithTasks,
      "leaveInspectionOpen": this.leaveInspectionOpen,
      "removeDispute": this.removeDispute,
      "associatedCcbNumber": "",
      "associatedDebitNumber": "",
      "generalLedgerNumber": "",
      "serviceToKeepTypeId": this.selectedServiceTypeId,
       "comments": this.memoComments,
      "claimCrmLineDTO": this.selectedLine.map((line:any) => ({
        "claimCrmLineId": 0,
        "invoiceLineNumber":line.selectedLine.invoiceLineNumber,
        "crmLineNumber": line.selectedLine.lineNumber,
        "claimLineId": line.claimLineId,
        "invDate":line.documentDate,
        "rollNumber": line.selectedLine.rollNumber,
       // "styleNumber": line.styleNumber,
        "colorNumber": line.selectedLine.colorNumber,
        "lot":line.selectedLine.dyeLot,
        "type":line.selectedLine.rcsCodes.rcsCode,
        "grade":line.selectedLine.grade,
        "unitPriceUsd":line.selectedLine.unitPriceUsd,
        "lineAmountUsd":line.selectedLine.lineAmountUsd,
        "styleNumber": line.selectedLine.styleNumber,
        "unitOfMeasureCode": line.selectedLine.unitOfMeasure.unitOfMeasureCode,
        "reasonCode": line.selectedLine.claimReasonDefinition.claimReasonCode,
        "quantity": line.selectedLine.quantity,
         "issuedAmount": line.selectedLine.lineAmountUsd,//this.editRestockFeeData 
        // ? (line.selectedLine.lineAmountUsd - this.editRestockFeeData).toFixed(2)
        // : line.selectedLine.lineAmountUsd.toFixed(2),// line.selectedLine.lineAmountUsd- this.editRestockFeeData,
        "widthInFeet": line.selectedLine.widthInFeet || 0,
        "lengthInFeet": line.selectedLine.lengthInFeet || 0,
         "amountUsd": line.selectedLine.lineAmountUsd, //this.editRestockFeeData 
        // ? (line.selectedLine.lineAmountUsd - this.editRestockFeeData).toFixed(2)
        // : line.selectedLine.lineAmountUsd.toFixed(2),//line.selectedLine.lineAmountUsd,
        "amountForeign": line.selectedLine.lineAmountForeign
      }))
    };
    
  console.log(saveCrmDraftPayload,'saveCrmDraftPayload')
  this.selectedLine.map((line:any) => {
    console.log(line,'mapline')
  })
  
  const setViewCrmEditDAta  ={   "createdByUserId": Number(localStorage.getItem("userId")),
    "modifiedByUserId": Number(localStorage.getItem("userId")),
    "claimCrmHeaderId": 0,
    "crmStatusCode": param,
    "claimId": this.claimId,
    "claimCustomerNumber": this.customerNumber,
    "creditCustomerNumber": "",
    "editRestockFeeData":this.editRestockFeeData,
    "restockFeePercentage": this.restockFeePercentage,
    "selectedRga": this.selectedRga,
    "claimReasonCode": this.selectedLine[0].claimReasonCode,
    "amountUsd":(this.getCombinedTotal()).toFixed(2)|| 0,
    "amountForeign": this.selectedLine[0].selectedLine.lineAmountForeign,
    "preparedByUserId": Number(localStorage.getItem("userId")),
    "currentUserId": Number(localStorage.getItem("userId")),
    "issuedByUserId": Number(localStorage.getItem("userId")),
    "associatedRgaNumber": 0,
    "emailAddress": this.emailAddress,
      "autoApply": this.autoApply,
      "doNotApply": this.doNotApply,
      "manualApply": this.manualApply,
      "leaveClaimOpen": this.leaveClaimOpen,
      "closeClaimWithTasks": this.closeClaimWithTasks,
      "leaveInspectionOpen": this.leaveInspectionOpen,
      "removeDispute": this.removeDispute,
    "associatedCcbNumber": "",
    "associatedDebitNumber": "",
    "generalLedgerNumber": "",
    "atkAttempted": this.atkAttempted,
  "atkAccepted": this.atkAccepted,
  "stkAttempted": this.stkAttempted,
  "stkAccepted": this.stkAccepted,
    "serviceToKeepTypeId": this.selectedServiceTypeId,
     "comments": this.memoComments,
     "showUserName":this.showUserName,
     "showInvoice" :this.showInvoice,
     "showCustomer" :this.showCustomer,
    "claimCrmLineDTO": this.selectedLine.map((line:any) => ({
      "claimCrmLineId": 0,
      "invoiceLineNumber":line.selectedLine.invoiceLineNumber,
      "crmLineNumber": line.selectedLine.lineNumber,
      "claimLineId": line.claimLineId,
      "invNumber":line.documentNumber,
      "invDate":line.documentDate,
      "rollNumber": line.selectedLine.rollNumber,
     // "styleNumber": line.styleNumber,
      "colorNumber": line.selectedLine.colorNumber,
      "lot":line.selectedLine.dyeLot,
      "type":line.selectedLine.rcsCodes.rcsCode,
      "grade":line.selectedLine.grade,
      "unitPriceUsd":line.selectedLine.unitPriceUsd,
      "lineAmountUsd":line.selectedLine.lineAmountUsd.toFixed(2),
      "styleNumber": line.selectedLine.styleNumber,
      "unitOfMeasureCode": line.selectedLine.unitOfMeasure.unitOfMeasureCode,
      "reasonCode": line.selectedLine.claimReasonDefinition.claimReasonCode,
           "quantity": line.selectedLine.quantity,
      "issuedAmount": line.selectedLine.lineAmountUsd.toFixed(2),
      "widthInFeet": line.selectedLine.widthInFeet || 0,
      "lengthInFeet": line.selectedLine.lengthInFeet || 0,
      "amountUsd":line.selectedLine.lineAmountUsd.toFixed(2),
      "amountForeign": line.selectedLine.lineAmountForeign
    }))}
  if (page === 'crmView') {
    localStorage.setItem('setViewCrmEditDAta', JSON.stringify(setViewCrmEditDAta));
    localStorage.removeItem('crmSelectedLine');
    const navigationExtras: NavigationExtras = {
      state: {
        selectedLine:  this.selectedLine,
        claimAddresses: this.claimAddresses,   
        claimId: this.claimId,
       crmNumber: this.crmNumber,
        crmHeaderId:this.crmHeaderId,
        claimNumber:this.claimNumber,
        customerNumber: this.customerNumber,
        claimDocuments : this.claimDocuments,
        endUserInfo:this.claimDetails.endUserInformation == null ? '': this.claimDetails.endUserInformation
      }
    };
  
   this.router.navigate(['/view-crm'], navigationExtras);
  } else {
    console.log('dont come')
  this.claimDetailService.crmHoldOrIssue(saveCrmDraftPayload).subscribe(
  
    (data) => {
      localStorage.removeItem('crmSelectedLine');
      
        localStorage.removeItem('setViewCrmEditDAta');
      if (data.status === 200) {
        this.crmNumber = data.body;
        console.log(data, 'crmnewData');
       
      //  else if (page === 'claimDetail') {
          const toastType: 'success' = 'success';
          const message = 'CRM saved as draft successfully';
          this.toasterService.showToast(toastType, message);
          this.router.navigate(['/claim-detail',this.claimNumber]);
       // }
      }
    },
    (error:any) => {
    
      const toastType: 'error' = 'error';
    const message = 'Failed to save as draft';
    this.toasterService.showToast(toastType, message);
       }
  );
}
}

navigateToViewCrm(){
  this.saveCrmDraft('P','crmView')
  this.router.navigate(['/view-crm'])
  
}
navigateToClaimDetails(){
  this.router.navigate(['/claim-detail', this.claimNumber]);
}
fetchServiceToKeepType(){
  this.claimDetailService.fetchServiceToKeepType().subscribe(data =>{
this.serviceTypes = data.body;
console.log(this.serviceTypes,'this.serviceTypes')
console.log(this.claimDetails,'claimDetailsoninitkeeptype')
  })
}

  fetchRgaNumber(claimId: number) {
    this.claimDetailService.fetchRgaDetails(claimId).subscribe(data => {
      // Extract all rgaNumbers from the response and remove duplicates
      const allRgaNumbers = data.body.map((item: any) => item.rgaNumber);
  
      // Remove duplicates
      this.rgaNumberList = [...new Set(allRgaNumbers)];
  
      // Set the first RGA as selected by default if the list is not empty
      if (this.rgaNumberList.length > 0) {
        this.selectedRga = this.rgaNumberList[0];
      } else {
        this.selectedRga = 'No RGA'; // Fallback if the list is empty
      }
  
      console.log(this.rgaNumberList, 'rgaNumberList after removing duplicates');
      console.log(this.selectedRga, 'Selected RGA');
    });
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
isChecked(lineNumber: number): boolean {
  return this.selectedLine.some((item:any) => item.selectedLine && item.selectedLine.lineNumber === lineNumber);
}

getTotalAmount1(document: any): number {
  if (document && document.claimLineDetails) {
    return document.claimLineDetails.reduce((total:any, line:any) => total + (line.lineAmountUsd || 0), 0);
  }
  return 0;
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
  console.log(line, 'checked');
  const isChecked = event.target.checked;
  this.isAnyLineSelected = true;
 
  const restockFeeAmount = this.restockFeePercentage 
    ? (this.restockFeePercentage / 100) * line.lineAmountUsd
    : 0;
 
  if (isChecked) {
    console.log('Restock Fee Percentage:', this.restockFeePercentage);
    console.log('Original Line Amount USD:', line.lineAmountUsd);
    
    const formattedLineItem = {
      claimDocumentId: document.claimDocumentId,
      documentNumber: document.documentNumber,
      createdByUserId: line.createdByUserId,
      modifiedByUserId: line.modifiedByUserId,
      documentTypeCode: document.documentType.documentTypeCode,
      claimReasonCode: document.claimReasonDefinition.claimReasonCode,
      claimLineId: line.claimLineId,
      lineNumber: line.lineNumber,
      documentDate: document.documentDate,
      orderNumber: document.orderNumber,
      selectedLine: this.restockFeePercentage 
        ? { 
            ...line, 
            lineAmountUsd: line.lineAmountUsd - restockFeeAmount 
          } 
        : line
    };
 
    console.log('Formatted Line Item:', formattedLineItem);
    this.formattedLine.push(formattedLineItem);
 
    // Update editRestockFeeData by adding the restock fee
    this.editRestockFeeData = this.editRestockFeeData
      ? this.editRestockFeeData + restockFeeAmount
      : restockFeeAmount;
      this.memoComments = `Restocking fees of $ ${this.editRestockFeeData} is deducted`;
 
  } else {
    // Find the line that is being removed
    const removedLine = this.formattedLine.find(item => item.lineNumber === line.lineNumber);

    if (removedLine) {
      // Subtract the restock fee from editRestockFeeData
      const removedRestockFeeAmount = (this.restockFeePercentage / 100) * line.lineAmountUsd;
      this.editRestockFeeData -= removedRestockFeeAmount;
      this.memoComments = `Restocking fees of $ ${this.editRestockFeeData} is deducted`;
    }
 console.log(this.memoComments,"=======");
    // Remove the unselected line from formattedLine
    this.formattedLine = this.formattedLine.filter(item => item.lineNumber !== line.lineNumber);
  }
 
  console.log(this.formattedLine, 'formattedLine,,');
  console.log(this.editRestockFeeData, 'editRestockFeeData,,');
}

addSelectedClaimLines(): void {
  console.log(this.formattedLine, 'selectedLine');

  this.formattedLine.forEach((line) => {
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

    // Only push the line if both conditions are passed
    this.selectedLine.push(line);
    localStorage.setItem('crmSelectedLine', JSON.stringify(this.selectedLine));
    this.closeClaimLinesModal();
    this.chekedLineItem = [];
    this.isAnyLineSelected = false;
    this.formattedLine = [];
  
    // Get unique documents after the selected lines have been updated
    this.uniqueDocuments = this.getUniqueDocuments(this.selectedLine);
  });

  // Update localStorage with the selected lines
 

  console.log(this.selectedLine, 'this.selectedLineafter1');

  // Close the modal and reset the state
  
 
}
removeSelectedLines() {
  console.log(this.chekedLineItem, "thischeckeitem");

  // Extract line numbers to remove from checked items
  const lineNumbersToRemove = this.chekedLineItem.map(item => item.lineNumber);

  console.log(this.selectedLine, 'selectedline');

  // Iterate over each object in the this.selectedLine array
  this.selectedLine = this.selectedLine.map((item:any) => {
    // Check if the selectedLine object exists and contains a lineNumber to be removed
    if (item.selectedLine && lineNumbersToRemove.includes(item.selectedLine.lineNumber)) {
      // Get the dynamic restock fee percentage
      const restockFeePercentage = this.restockFeePercentage / 100;
 
      
      const adjustedAmount = item.selectedLine.lineAmountUsd;
      const originalAmount = adjustedAmount / (1 - restockFeePercentage);
 
    
      const restockFee = restockFeePercentage * originalAmount;
 
 
      this.editRestockFeeData -= restockFee;
      this.memoComments = `Restocking fees of $ ${this.editRestockFeeData} is deducted`;
    
      item.selectedLine = null;
    }
    return item;
  });

  // Remove null or empty entries from selectedLine if necessary
  this.selectedLine = this.selectedLine.filter((item: any) => item.selectedLine !== null);

  console.log(this.selectedLine, 'this.selectedLine afterRemove');
  const c=this.selectedLine.every((item:any)=>item.selectedLine===null)
  console.log(c,"+++c")
  if (c) {
  console.log("inside function --=",this.selectedLine);
    this.editRestockFeeData = 0;
    this.memoComments='';

  }
 
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
  localStorage.setItem('crmSelectedLine', JSON.stringify(this.selectedLine));
  this.isAnyLineSelected = false
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
getAllowanceToKeepData(){
  const reasonCodes = this.selectedLine.map((line:any) => line.selectedLine.claimReasonDefinition.claimReasonCode);

  const payload = {
    reasonCodes: reasonCodes
  };

this.claimDetailService.getAllowanceToKeepData(payload).subscribe(data=>{
  console.log(data.body[0],'mydattattata')
  const allowanceToKeepData =data.body[0];
  console.log(allowanceToKeepData,'allowanceToKeepData.mydattattata')
  this.atkAccepted= allowanceToKeepData.allowanceToKeep;
  this.stkAccepted=allowanceToKeepData.serviceToKeep;

})
}
}
