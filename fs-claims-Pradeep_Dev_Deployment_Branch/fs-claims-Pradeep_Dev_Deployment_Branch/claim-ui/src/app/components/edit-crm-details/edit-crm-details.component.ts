import { Component, OnInit,Input } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, NavigationEnd, NavigationExtras, Router } from '@angular/router';
import { Observable, filter, forkJoin, map, tap } from 'rxjs';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';
import { ToasterComponent } from '../toaster/toaster.component';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-edit-crm-details',
  templateUrl: './edit-crm-details.component.html',
  styleUrls: ['./edit-crm-details.component.scss']
})
export class EditCrmDetailsComponent implements OnInit {
  @Input() screenWidth = 0;
  public isSideNavCollapsed: boolean = true;
  public serviceTypes: any[]=[];
  public documentsByClaimId: any[]=[];
  public crmDetailsByClaimIdAndCrmNumber: any;
 public selectedServiceType :string= '';
 atkAttempted: boolean = false;
 atkAccepted: boolean = false;
 stkAttempted: boolean = false;
 stkAccepted: boolean = false;
  crmNumber: number=0;
  claimId: number=0;
  crmLinesDetails: any[]=[];
  private previousUrl: string = '';
  previousClaimNumber: string = '';
  claimNumber: string='';
  claimDocuments: any[]=[];
  filteredDocuments: any[]=[];
  isAnyLineSelected = false;
  filteredClaimDocuments: any[]=[];
  crmSelectedLine: any[]=[];
  formattedLine: any[]=[];
  chekedLineItem: any[]=[];
  uniqueDocuments: any[]=[];
  crmHeaderId: number=0;
  crmDetails: any={};
  rgaNumberList: any[]=[];
  public selectedRga: string = 'No RGA';
  selectedApplyMethod: string = '';
  customerNumber: string='';
  claimAddresses: any[]=[];
  shipToAddress: any;
  constructor(private claimDetailService:ClaimDetailService,
    private activatedroute: ActivatedRoute,
       private router: Router,
    private toasterService: ToasterService,
    private bodyClassService: BodyClassService,
  ){}
 
 
   
    


  ngOnInit(): void {
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
    
    this.activatedroute.queryParams.subscribe(params => {
      this.claimId = window.history.state.claimId;
      this.claimAddresses= window.history.state.claimAddresses;
        this.claimDocuments= window.history.state.claimDocuments;
      this.claimNumber =window.history.state.claimNumber;
      this.crmNumber =window.history.state.crmNumber||'';
    this.crmHeaderId =window.history.state.crmHeaderId||'';
    this.customerNumber =window.history.state.customerNumber||''; 
     console.log(this.crmNumber, this.claimId,'claim and crm');
     // Use crmNumber and claimId as needed
   
     console.log(this.claimDocuments,'claimDocuments...')
     
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
  // this.prepCRMmodal(this.crmNumber,this.claimId);
  this.prepCRMmodal();
   });
   this.fetchRgaNumber(this.claimId);
   this.fetchServiceToKeepType();
   this.shipToAddress = this.claimAddresses.find(
    (address: any) => address.addressTypeId === 2
  );
  }


  //public prepCRMmodal(crmNumber:number,claimId:number)
  public prepCRMmodal(){
  console.log('func got called')
   // this.fetchCrmDetailsByClaimIdAndCrmNumber(claimId, crmNumber).subscribe(data=>{
      const savedLines = localStorage.getItem('crmSelectedLine');
      console.log(savedLines,'savedLineslocalstorged')
      if (savedLines && JSON.parse(savedLines).length  > 0) {
        console.log('camhere')
        this.crmSelectedLine = JSON.parse(savedLines);
       
       
        this.crmDetails=JSON.parse(localStorage.getItem('setViewCrmEditDAta') || '{}');
      } else {
        console.log('camhere local')
        const viewEditData = localStorage.getItem('setViewCrmEditDAta');
        if (viewEditData) {
        this.crmSelectedLine = JSON.parse(viewEditData).claimCrmLineDTO;
        // this.crmSelectedLine = data.claimCrmLineDTO;
        this.crmDetails=JSON.parse(viewEditData);
         console.log(this.crmSelectedLine,'this.crmSelectedLinehas data..');
         console.log(this.crmDetails,'this.crmDetails data..')
      } else {
        console.log('empty')
        this.crmSelectedLine = [];
      }
    }
    this.uniqueDocuments = this.getUniqueDocuments(this.crmSelectedLine);
      console.log(this.uniqueDocuments,'uniqueDocuments')
      this.filteredDocuments =this.getUniqueDocuments(this.claimDocuments);
      if (this.crmDetails.autoApply) {
        this.selectedApplyMethod = 'autoApply';
      } else if (this.crmDetails.manualApply) {
        this.selectedApplyMethod = 'manualApply';
      } else if (this.crmDetails.doNotApply) {
        this.selectedApplyMethod = 'doNotApply';
      }
     
          
 
  }
 
  
  fetchDocumentsByClaimId(claimId: number): Observable<any> {
    return this.claimDetailService.fetchDocumentsByClaimId(claimId).pipe(
      tap(data => console.log(data, 'documentsByClaimId')),
      map(response => response.body)
    );
  }
  
  fetchCrmDetailsByClaimIdAndCrmNumber(claimId: number, crmNumber: number): Observable<any> {
    return this.claimDetailService.fetchCrmDetailsByClaimIdAndCrmNumber(claimId, crmNumber).pipe(
      tap(data => console.log(data, 'crmDetailsByClaimIdAndCrmNumber')),
      map(response => response.body)
    );
  }
 
  saveCrmDraft(param:string,page:string){
    console.log(this.crmDetails,'crmDetailssaveCrmDraftPayload')
    this.crmLinesDetails.forEach((line: any) => {
      console.log(line, 'console line');
      console.log(line.rollNumber, 'console rollNumber');
    });
    console.log(this.crmSelectedLine,'crmSelectedLine..')
    
    const saveCrmDraftPayload = {
      "createdByUserId":  this.crmDetails.createdByUserId,
      "modifiedByUserId": this.crmDetails.modifiedByUserId,
      "claimCrmHeaderId": this.crmDetails.claimCrmHeaderId,
      "crmStatusCode": param,
      "claimId": this.claimId,
      "claimCustomerNumber": this.crmDetails.claimCustomerNumber,
      "creditCustomerNumber": "",
      "claimReasonCode": this.crmDetails.claimReasonCode,
      "amountUsd":this.crmDetails.amountUsd || 0,
      "amountForeign": this.crmDetails.amountForeign ||0,
      "preparedByUserId": this.crmDetails.preparedByUserId,
      "currentUserId":this.crmDetails.currentUserId,
      "issuedByUserId": this.crmDetails.issuedByUserId,
      "associatedRgaNumber": this.crmDetails.associatedRgaNumber,
      "emailAddress": this.crmDetails.emailAddress,
      "autoApply": this.crmDetails.autoApply,
      "doNotApply": this.crmDetails.doNotApply,
      "manualApply": this.crmDetails.manualApply,
      "leaveClaimOpen": this.crmDetails.leaveClaimOpen,
      "closeClaimWithTasks": this.crmDetails.closeClaimWithTasks,
      "leaveInspectionOpen": this.crmDetails.leaveInspectionOpen,
      "removeDispute": this.crmDetails.removeDispute,
      "atkAttempted": this.crmDetails.atkAttempted,
      "atkAccepted": this.crmDetails.atkAccepted,
      "stkAttempted": this.crmDetails.stkAttempted,
      "stkAccepted": this.crmDetails.stkAccepted,
                  "associatedCcbNumber": this.crmDetails.associatedCcbNumber,
      "associatedDebitNumber": "",
      "generalLedgerNumber": this.crmDetails.generalLedgerNumber,
      "serviceToKeepTypeId": this.crmDetails.serviceToKeepTypeId,
       "comments": this.crmDetails.comments,
      "claimCrmLineDTO": this.crmSelectedLine.map((line:any) => ({
        "claimCrmLineId": line.claimCrmLineId ? line.claimCrmLineId : 0,
        "crmLineNumber": line.crmLineNumber ? line.crmLineNumber : '',
        "invoiceLineNumber":line.invoiceLineNumber,
        "claimLineId": line.claimLineId ?  line.claimLineId : '',
        "rollNumber": line.rollNumber || '',
        "styleNumber": line.styleNumber||'',
        "colorNumber": line.colorNumber||'',
        
        "lot":line.dyeLot,
        "type":line.type,
        "grade":line.grade,
        "unitPriceUsd":line.unitPriceUsd,
        "quantity": line.quantity||0,
        "issuedAmount": line.lineAmountUsd,
        "widthInFeet": line.widthInFeet || 0,
        "lengthInFeet": line.lengthInFeet || 0,
        "widthInInches":line.widthInInches || 0,
        "lengthInInches":line.lengthInInches||0,
        "amountUsd": line.amountUsd||0,
        "amountForeign": line.lineAmountForeign|| 0
      }))
    };
    
  console.log(saveCrmDraftPayload,'saveCrmDraftPayload')
  if (page === 'crmView') {
    localStorage.setItem('setViewCrmEditDAta', JSON.stringify(saveCrmDraftPayload));
    localStorage.removeItem('crmSelectedLine');
    const navigationExtras: NavigationExtras = {
      state: {
        claimId: this.claimId,
          crmNumber: this.crmNumber,
          crmHeaderId:this.crmHeaderId,
          claimNumber:this.claimNumber,
          customerNumber: this.customerNumber,
          claimDocuments : this.claimDocuments,
       
      }
    };
    this.router.navigate(['/view-crm'],navigationExtras);
  } else {
  this.claimDetailService.crmHoldOrIssue(saveCrmDraftPayload).subscribe(
    (data) => {
      if (data.status === 200) {
        this.crmNumber=data.body;
        localStorage.removeItem('crmSelectedLine');
        localStorage.removeItem('setViewCrmEditDAta');
 const toastType: 'success' | 'error' | 'warning' = 'error'; 
 const message = 'Crm saved As Draft Successfully';
 this.toasterService.showToast(toastType, message);

        
          this.router.navigate(['/claim-detail',this.claimId]);
      
      }
    },
    (error:any) => {
      console.error('Error occurred:', error);
    //  this.refreshClaimDetails.emit();
      // this.snackBar.open('Failed To Save As Draft ', 'Close', {
      //   duration: 3000,
      //   verticalPosition: 'top',
      //   panelClass: ['custom-snackbar'], 
      // });
      const toastType: 'success' | 'error' | 'warning' = 'error'; 
 const message = 'Failed To Save As Draft';
 this.toasterService.showToast(toastType, message);
    //  this.closeCrmModal();
    //  this.closeRestockFeeModal();
    }
  );
}
}


public getBodyClass(): string {
  return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
}

public onToggleSideNav(data: SideNavToggle): void {
  this.screenWidth = data.screenWidth;
  this.isSideNavCollapsed = data.collapsed;
}


getUniqueDocuments(lines: any[]): any[] {
  console.log(lines,'lines')
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
  console.log('Unique Documents:', uniqueDocuments);
  return uniqueDocuments;
}

// getTotalAmount(documentNumber: string): number {
//   return this.crmSelectedLine
//       .filter(line => line.documentNumber === documentNumber)
//       .reduce((total, line) => total + line.selectedLine.lineAmountUsd, 0);
// }

// getCombinedTotal(): number {
//   return this.uniqueDocuments
//       .map(document => this.getTotalAmount(document.documentNumber))
//       .reduce((combinedTotal, documentTotal) => combinedTotal + documentTotal, 0);
// }


closeClaimLinesModal(){
 
  const modelDiv = document.getElementById('claimLinesModal');
  if (modelDiv != null) {
    modelDiv.style.display = 'none';
    
  }

}

addClaimLinesModal(){
  console.log('modal gets called')
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
}


addSelectedClaimLines(): void {
  console.log(this.formattedLine, 'selectedLine');

  this.formattedLine.forEach((line) => {
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

    // Only push the line if both conditions are passed
    this.crmSelectedLine.push(line);
    localStorage.setItem('crmSelectedLine', JSON.stringify(this.crmSelectedLine));
    this.closeClaimLinesModal();
    this.chekedLineItem = [];
    this.isAnyLineSelected = false;
    this.formattedLine = [];
  
    // Get unique documents after the selected lines have been updated
    this.uniqueDocuments = this.getUniqueDocuments(this.crmSelectedLine);
  });

  // Update localStorage with the selected lines
 

  console.log(this.crmSelectedLine, 'this.selectedLineafter1');

  // Close the modal and reset the state
  
 
}
removeSelectedLines() {
  console.log(this.chekedLineItem, "thischeckeitem");

  // Extract line numbers to remove from checked items
  const lineNumbersToRemove = this.chekedLineItem.map((item:any) => item.lineNumber);

  console.log(lineNumbersToRemove, 'lineNumbersToRemove');

  // Iterate over each object in the this.selectedLine array
  this.crmSelectedLine = this.crmSelectedLine.map(item => {
    
    // Check if the selectedLine object exists and contains a lineNumber to be removed
    if (lineNumbersToRemove.includes(item.lineNumber)) {
      const restockFeePercentage = this.crmDetails.restockFeePercentage / 100;
 
      
      const adjustedAmount = item.amountUsd;
      const originalAmount = adjustedAmount / (1 - restockFeePercentage);
 
    
      const restockFee = restockFeePercentage * originalAmount;
 
 
      this.crmDetails.editRestockFeeData -= restockFee;
      this.crmDetails.memoComments = `Restocking fees of $ ${this.crmDetails.editRestockFeeData} is deducted`;
      item = null; // Set to null or remove the line as needed
    }
    return item;
  });

  // Remove null or empty entries from selectedLine if necessary
  this.crmSelectedLine = this.crmSelectedLine.filter(item => item !== null);
  const c=this.crmSelectedLine.every((item:any)=>item.selectedLine===null)
  if (c) {
    console.log("inside function --=",this.crmSelectedLine);
      this.crmDetails.editRestockFeeData = 0;
      this.crmDetails.memoComments='';
  
    }
  console.log(this.crmSelectedLine, 'this.selectedLine afterRemove');

  // Update uniqueDocuments after removal
  this.uniqueDocuments = this.getUniqueDocuments(this.crmSelectedLine);
  console.log(this.uniqueDocuments, 'this.uniqueDocuments');

  // Uncheck corresponding checkboxes in filteredClaimDocuments
  this.filteredClaimDocuments.forEach(document => {
    document.claimLineDetails.forEach((line:any) => {
      if (lineNumbersToRemove.includes(line.lineNumber)) {
        line.isChecked = false; // You can store the checked status in a property
      }
    });
  });
  localStorage.setItem('selectedLine', JSON.stringify(this.crmSelectedLine));
  this.isAnyLineSelected = false
}
getTotalLineAmount(): number {
  console.log(this.crmSelectedLine,'crmSelectedLine')
  return this.crmSelectedLine.reduce((total, line) => total + (line.amountUsd || 0), 0);
}

isChecked(lineNumber: number): boolean {
  // console.log(this.selectedLine,'selectedLine')
   return this.crmSelectedLine.some(item => item.crmLineNumber === lineNumber);
 }
 
 onCheckboxChange(document: any, line: any, event: any): void {
  console.log(line,'checked')
  console.log(document,'documentchecked')
  const isChecked = event.target.checked;
  this.isAnyLineSelected = true;
  const restockFeeAmount = this.crmDetails.restockFeePercentage 
  ? (this.crmDetails.restockFeePercentage / 100) * line.lineAmountUsd
  : 0;

  if (isChecked) {
    console.log('Restock Fee Percentage:', this.crmDetails.restockFeePercentage);
    console.log('Original Line Amount USD:', line.lineAmountUsd);
    
    const formattedLineItem = {
      claimDocumentId: document.claimDocumentId,
      documentNumber: document.documentNumber,     
     invDate: document.documentDate,
       invNumber: document.documentNumber,
      createdByUserId: line.createdByUserId,
      modifiedByUserId: line.modifiedByUserId,
      documentTypeCode: document.documentType.documentTypeCode,
      claimReasonCode: document.claimReasonDefinition.claimReasonCode,
      claimLineId: line.claimLineId,
            documentDate: document.documentDate,
      orderNumber: document.orderNumber,
      crmLineNumber: line.lineNumber,
      lineNumber: line.lineNumber,
      invoiceLineNumber:line.invoiceLineNumber,
      reasonCode:line.claimReasonDefinition.claimReasonCode,
      styleNumber:line.styleNumber,
      colorNumber:line.colorNumber,
      rollNumber:line.rollNumber,
      lot:line.dyeLot,
      type:line.rcsCodes.rcsCode,
      grade:line.grade,
      widthInFeet:line.claimDetailRecords[0].widthInFeet,
      lengthInFeet:line.claimDetailRecords[0].lengthInFeet,
      quantity:line.quantity,
      unitOfMeasureCode:line.unitOfMeasure.unitOfMeasureCode,
      unitPriceUsd:line.unitPriceUsd,
      amountUsd: this.crmDetails.restockFeePercentage 
      ? line.lineAmountUsd - restockFeeAmount
      : line.lineAmountUsd,
      returnEligible:line.claimReasonDefinition.returnEligible
      // selectedLine: this.crmDetails.restockFeePercentage 
      //   ? { 
      //       ...line, 
      //       lineAmountUsd: line.lineAmountUsd - ((this.crmDetails.restockFeePercentage / 100) * line.lineAmountUsd) 
      //     } 
      //   : line
    };
    
    console.log('Formatted Line Item:', formattedLineItem);
    

    this.formattedLine.push(formattedLineItem);
      // Update editRestockFeeData by adding the restock fee
      this.crmDetails.editRestockFeeData = this.crmDetails.editRestockFeeData
      ? this.crmDetails.editRestockFeeData + restockFeeAmount
      : restockFeeAmount;
      this.crmDetails.memoComments = `Restocking fees of $ ${this.crmDetails.editRestockFeeData} is deducted`;
 
  } else {
    const removedLine = this.formattedLine.find(item => item.lineNumber === line.lineNumber);

    if (removedLine) {
      // Subtract the restock fee from editRestockFeeData
      const removedRestockFeeAmount = (this.crmDetails.restockFeePercentage / 100) * line.lineAmountUsd;
      this.crmDetails.editRestockFeeData -= removedRestockFeeAmount;
      this.crmDetails.memoComments = `Restocking fees of $ ${this.crmDetails.editRestockFeeData} is deducted`;
    }
    this.formattedLine = this.formattedLine.filter(item => item.lineNumber !== line.lineNumber);
  }

  console.log(this.formattedLine, 'formattedLine,,');
}

 getTotalAmount1(document: any): number {
  if (document && document.claimLineDetails) {
    return document.claimLineDetails.reduce((total: any, line: any) => {
      // console.log('Current Total:', total);
      // console.log('Current Line:', line);
     return total + (line.lineAmountUsd || 0);
    }, 0);
  }
  return 0;
}
handleSpecialInstruction(option: string) {
  switch (option) {
    case 'EndUser':
      this.crmDetails.showUserName = true;
      this.crmDetails.showInvoice = false;
      this.crmDetails.showCustomer = false;
      break;
    case 'CustomerChargeback':
      this.crmDetails.showUserName = false;
      this.crmDetails.showInvoice = false;
      this.crmDetails.showCustomer = true;
      break;
    case 'InvoicePOOrder':
      this.crmDetails.showUserName = false;
      this.crmDetails.showInvoice = true;
      this.crmDetails.showCustomer = false;
      break;
  }
}
// updateApplyMethod(method: string) {
//   if (method === 'doNotApply') {
//     this.crmDetailsByClaimIdAndCrmNumber.autoApply = false;
//     this.crmDetailsByClaimIdAndCrmNumber.manualApply = false;
//     this.crmDetailsByClaimIdAndCrmNumber.doNotApply = true;
//   } else if (method === 'manualApply') {
//     this.crmDetailsByClaimIdAndCrmNumber.autoApply = false;
//     this.crmDetailsByClaimIdAndCrmNumber.manualApply = true;
//     this.crmDetailsByClaimIdAndCrmNumber.doNotApply = false;
//   } else if (method === 'autoApply') {
//     this.crmDetailsByClaimIdAndCrmNumber.autoApply = true;
//     this.crmDetailsByClaimIdAndCrmNumber.manualApply = false;
//     this.crmDetailsByClaimIdAndCrmNumber.doNotApply = false;
//   }

//   console.log({
//     autoApply: this.crmDetailsByClaimIdAndCrmNumber.autoApply,
//     manualApply: this.crmDetailsByClaimIdAndCrmNumber.manualApply,
//     doNotApply: this.crmDetailsByClaimIdAndCrmNumber.doNotApply
//   });
// }
updateApplyMethod(method: string) {
  console.log(method,'method...')
  // Set the selected method to true and the others to false
  this.crmDetails.autoApply = method === 'autoApply';
  this.crmDetails.manualApply = method === 'manualApply';
  this.crmDetails.doNotApply = method === 'doNotApply';

  console.log({
    autoApply: this.crmDetails.autoApply,
    manualApply: this.crmDetails.manualApply,
    doNotApply: this.crmDetails.doNotApply
  });
}


updateCheckboxState(option: string) {
  // Deselect all checkboxes
  this.crmDetails.leaveClaimOpen = false;
  this.crmDetails.closeClaimWithTasks = false;
  this.crmDetails.leaveInspectionOpen = false;
  this.crmDetails.removeDispute = false;

  // Select the checkbox corresponding to the option clicked
  if (option === 'leaveClaimOpen') {
    this.crmDetails.leaveClaimOpen = true;
  } else if (option === 'closeClaimWithTasks') {
    this.crmDetails.closeClaimWithTasks = true;
  } else if (option === 'leaveInspectionOpen') {
    this.crmDetails.leaveInspectionOpen = true;
  } else if (option === 'removeDispute') {
    this.crmDetails.removeDispute = true;
  }

  console.log({
    leaveClaimOpen: this.crmDetails.leaveClaimOpen,
    closeClaimWithTasks: this.crmDetails.closeClaimWithTasks,
    leaveInspectionOpen: this.crmDetails.leaveInspectionOpen,
    removeDispute: this.crmDetails.removeDispute
  });
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
fetchServiceToKeepType(){
  this.claimDetailService.fetchServiceToKeepType().subscribe(data =>{
this.serviceTypes = data.body;
console.log(this.serviceTypes,'this.serviceTypes')

  })
}

getTotalAmount(documentNumber: string): number {
  return this.crmSelectedLine
    .filter((line: any) => line.invNumber === documentNumber)
    .reduce((total: number, line: any) => {
      // Convert lineAmountUsd to a float and ensure it is a number
      const lineAmount = parseFloat(line.amountUsd);
      return total + (isNaN(lineAmount) ? 0 : lineAmount);
    }, 0);
}

getCombinedTotal(): number {
  return this.uniqueDocuments
    .map(document => this.getTotalAmount(document.documentNumber))
    .reduce((combinedTotal, documentTotal) => combinedTotal + documentTotal, 0);
}

 // Function to handle logic based on checkbox states
 onCheckboxStateChange(type: string) {
  if (type === 'atkAccepted') {
    this.crmDetails.atkAccepted = !this.crmDetails.atkAccepted;
    if (this.crmDetails.atkAccepted) {
      this.crmDetails.atkAttempted = true;
      this.crmDetails.stkAccepted = false;
      this.crmDetails.stkAttempted = false;
    }
  } else if (type === 'stkAccepted') {
    this.crmDetails.stkAccepted = !this.crmDetails.stkAccepted;
    if (this.crmDetails.stkAccepted) {
      this.crmDetails.stkAttempted = true;
      this.crmDetails.atkAccepted = false;
      this.crmDetails.atkAttempted = false;

      // Ensure Service Type is selected if stkAccepted is true
      if (!this.crmDetails.serviceToKeepTypeId) {
        this.crmDetails.serviceToKeepTypeId = this.serviceTypes[0]?.serviceToKeepTypeId || null;
      }
    }
  }
}
navigateToClaimDetails(){
  localStorage.removeItem('crmSelectedLine');
  localStorage.removeItem('setViewCrmEditDAta');
    this.router.navigate(['/claim-detail', this.claimNumber]);
}
}
