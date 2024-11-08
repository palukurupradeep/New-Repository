import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';

import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
@Component({
  selector: 'app-add-edit-line',
  templateUrl: './add-edit-line.component.html',
  styleUrls: ['./add-edit-line.component.scss']
})
export class AddEditLineComponent implements OnChanges{

 // @Input() payloadResponse:any=[];
  @Input() editpayloadResponse:any=[]
  @Input() selectedLine: any=[];
  @Input() claimList: any = [];
  @Input() unitOfmeasure: any = [];
  @Output() refreshInvoiceData = new EventEmitter<string>();
  @Input() claimId:number= 0;
  public editLineForm!: FormGroup;
  public selectedValue:string='';
@Input() claimDetails: any=[];
frghtLineAmountUsd: number | undefined;
taxLineAmountUsd: number | undefined;
chrgsLineAmountUsd: number | undefined;
  frghtClaimLineId: string = '';
  taxClaimLineId: string = '';
  chrgsClaimLineId: string = '';
  addeditLinebtn:boolean=true;

  filteredClaimList: any[] = [];
  selectedReasonCode: string = '';
  codeSearch: string = '';
  public amountEdited: boolean = false;
  constructor(
   private claimdetailService:ClaimDetailService,
   private form: FormBuilder,
   private snackBar: MatSnackBar,
   private claimService: ClaimDetailService,
   private router:Router,
  private toasterService: ToasterService,
  ){
    
  }

  ngOnInit(){

    this.editLineForm = this.form.group({
      lines: this.form.array([]) // Using FormArray for multiple lines
    });
  
    if (this.selectedLine.length !== 0) {
      this.loadForm(); // Populate form if there are selected lines
    }
  }

  get lines(): FormArray {
    return this.editLineForm.get('lines') as FormArray;
  }
  
  
  filterClaims() {
    this.filteredClaimList = this.claimList.filter((claim: any) => {
            const searchTextLower = this.codeSearch.toLowerCase();
      return claim.claimReasonCode.toLowerCase().includes(searchTextLower) ||
             claim.claimReasonDescription.toLowerCase().includes(searchTextLower);
    });
  
  
  }
  onSearchInput(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    this.codeSearch = inputElement.value; 
    console.log('codeSearch:', this.codeSearch);
    this.filterClaims(); 
  }

  
  ngOnChanges(changes: SimpleChanges) {

   
    this.filteredClaimList = this.claimList;
  
    if (this.selectedLine.length !== 0) {
      // this.selectedLine = this.selectedLine[0].selectedLine;
      this.loadForm();
    }
  }

  loadForm(){
    console.log('calling from ng selectedLine',this.selectedLine)
    this.lines.clear();
  
    // Loop through the selectedLine array and add a new FormGroup for each line
    this.selectedLine.forEach((lineItem:any) => {
      this.lines.push(this.form.group({
        inv: [lineItem.claimNumber],
        lineNumber: [lineItem.selectedLine.lineNumber],
        invoiceLineNumber: [lineItem.selectedLine.invoiceLineNumber],
        status: [lineItem.selectedLine.detailStatusType.detailStatusTypeDescription],
        claimCat: [lineItem.selectedLine.detailType.detailTypeCode],
        style: [lineItem.selectedLine.styleNumber],
        color: [lineItem.selectedLine.colorNumber],
        roll: [lineItem.selectedLine.rollNumber.replace(/\s/g, '')],
        grade: [lineItem.selectedLine.grade],
        widthinfeet: [lineItem.selectedLine.claimDetailRecords[0]?.widthInFeet || 0],
        widthInInches:[lineItem.selectedLine.claimDetailRecords[0]?.widthInInches || 0],
        lengthinfeet: [lineItem.selectedLine.claimDetailRecords[0]?.lengthInFeet || 0],
        lengthInInches:[lineItem.selectedLine.claimDetailRecords[0]?.lengthInInches || 0],
        dyeLot: [lineItem.selectedLine.dyeLot],
        reasonCode: [lineItem.selectedLine.claimReasonDefinition.claimReasonCode || 'ALL'],
        qty: [lineItem.selectedLine.quantity],
        uom: [lineItem.selectedLine.unitOfMeasure.unitOfMeasureCode],
        price: [lineItem.selectedLine.unitPriceUsd],
        amount: [lineItem.selectedLine.lineAmountUsd],
        claimLineId:[lineItem.selectedLine.claimLineId],
        detailRecordTypeId:[lineItem.selectedLine.claimDetailRecords[0]?.claimDetailRecordId],
        detailRecordTypeCode:[lineItem.selectedLine.claimDetailRecords[0]?.detailRecordType.detailRecordTypeCode],
              rcsCode: [lineItem.selectedLine.rcsCodes.rcsCode],
            lineAmountForeign: [lineItem.selectedLine.lineAmountForeign],
        unitPriceForeign: [lineItem.selectedLine.unitPriceForeign],
        pricingCurrencyCode: [lineItem.selectedLine.pricingCurrencyCode],
        currencyCodeDesignation: [lineItem.selectedLine.currencyCodeDesignation],
        currencyCodeName: [lineItem.selectedLine.currencyCodeName],
        sellingCompany:[lineItem.selectedLine.sellingCompany] ,
        productCode:[lineItem.selectedLine.productCode] ,
        inventoryStyle: [lineItem.selectedLine.inventoryStyle],
        inventoryColor: [lineItem.selectedLine.inventoryColor],
        manufacturingPlant: [lineItem.selectedLine.manufacturingPlant],
        detailTypeCode:[lineItem.selectedLine.detailType.detailTypeCode],
        lineSourceCode: [lineItem.selectedLine.lineSource.lineSourceCode],
        lineSourceReference: [lineItem.selectedLine.lineSourceReference],
        lineAddReasonCode: [lineItem.selectedLine.lineAddReasonId],
        vendorId: [lineItem.selectedLine.vendorId],
        createdByUserId:[lineItem.createdByUserId],
        modifiedByUserId:[lineItem.modifiedByUserId],
        claimDocumentId:[lineItem.claimDocumentId] ,
        documentNumber: [lineItem.documentNumber],
        exchangeRate:[lineItem.selectedLine.exchangeRate],
        orderNumber: [lineItem.orderNumber||''],
        purchaseOrderNumber:[lineItem.purchaseOrderNumber ||''],
        billofLading:[lineItem.billofLading||''],
        masterBillofLading:[lineItem.masterBillofLading||''],
        documentTypeCode:[lineItem.documentTypeCode||''],
        claimReasonCode: [lineItem.claimReasonCode],
      }));
    });
    console.log(this.claimDetails,'claimDetailsData3')
   // this.fetchLineAmountUsd();

   const lineFrghtDetail = this.getLineDetailUsd('FRGHT');
   const linetTaxDetail = this.getLineDetailUsd('TAX');
   const lineChDetail = this.getLineDetailUsd('CHRGS');
if (lineFrghtDetail || linetTaxDetail ||lineChDetail) {
    this.frghtLineAmountUsd = lineFrghtDetail?.lineAmountUsd;
    this.frghtClaimLineId = lineFrghtDetail?.claimLineId ?? '';

    this.taxLineAmountUsd =linetTaxDetail?.lineAmountUsd;
    this.taxClaimLineId =linetTaxDetail?.claimLineId ?? '';
    this.chrgsLineAmountUsd =lineFrghtDetail?.lineAmountUsd;
    this.chrgsClaimLineId =lineChDetail?.claimLineId ?? '';
  }
    // this.frghtLineAmountUsd = this.getLineAmountUsd('FRGHT');
    // this.taxLineAmountUsd = this.getLineAmountUsd('TAX');
    // this.chrgsLineAmountUsd = this.getLineAmountUsd('CHRGS');

    console.log(this.frghtLineAmountUsd,'lineAmountUsdDayta');
    console.log(this.frghtClaimLineId,'frghtClaimLineId');
    console.log(this.taxLineAmountUsd,'linetaxAmountUsdDayta')
    console.log(this.taxClaimLineId,'taxClaimLineId');
    console.log(this.chrgsLineAmountUsd,'chrgsLineAmountUsd')
    console.log(this.chrgsClaimLineId,'chrgsClaimLineId');
    console.log(this.editLineForm.value,'formData Values for editLineForm4');
   // console.log( lineItem.claimReasonDefinition.claimReasonCode,' lineItem.claimReasonDefinition.claimReasonCode');
    // console.log(this.editLineForm.get('invoiceLineNumber')?.value,'.............check invnumber')
  }
 
  onReasonCodeChange(event: Event) {
    const selectedCode = (event.target as HTMLSelectElement).value;
    console.log(selectedCode,'newCode')
    if (selectedCode) {
      const selectedReason = this.claimList.find((data: any) => data.claimReasonCode === selectedCode);
      this.selectedReasonCode = selectedReason.claimReasonCode;
  console.log(selectedReason,'selectedReason')
  console.log(this.selectedReasonCode,'selectedReason')
      if (selectedReason) {
        this.editLineForm.patchValue({
          reasonCode: selectedReason.claimReasonCode
        });
  
        // Update other fields based on the selected object if needed
      }
    }
  }
  
  
  
  
  
  editLine(){
    const formValues = this.editLineForm.value.lines;  // Array of line items

    console.log(formValues,'formValues..'); // Inspect the form structure
    let finalPayload: any[] = [];

    formValues.forEach((line: any) => {
      const existingDocumentIndex = finalPayload.findIndex(
        (doc) => doc.documentNumber === line.documentNumber
      );
    
      const claimLineDetail = {
        createdByUserId: line.createdByUserId,
        modifiedByUserId: line.modifiedByUserId,
        unitOfMeasureCode: line.uom,
        rcsCode: line.rcsCode,
        lineNumber: line.lineNumber ? '00' + line.lineNumber : '',
        invoiceLineNumber: line.invoiceLineNumber ? '00' + line.invoiceLineNumber : '',
        rollNumber: line.roll.trim(),
        styleNumber: line.style,
        colorNumber: line.color,
        claimReasonCode: line.reasonCode,
        grade: line.grade,
        lineAmountUsd: line.amount,
        lineAmountForeign: line.lineAmountForeign,
        unitPriceUsd: line.price,
        unitPriceForeign: line.unitPriceForeign,
        pricingCurrencyCode: line.pricingCurrencyCode,
        currencyCodeDesignation: line.currencyCodeDesignation,
        currencyCodeName: line.currencyCodeName,
        exchangeRate: line.exchangeRate,
        dyeLot: line.dyeLot.trim(),
        quantity: line.qty,
        sellingCompany: line.sellingCompany,
        productCode: line.productCode,
        inventoryStyle: line.inventoryStyle,
        inventoryColor: line.inventoryColor,
        manufacturingPlant: line.manufacturingPlant,
        detailTypeCode: line.detailTypeCode,
        lineSourceCode: line.lineSourceCode,
        lineSourceReference: line.lineSourceReference,
        lineAddReasonCode: line.lineAddReasonId,
        vendorId: line.vendorId,
        claimLineId: line.claimLineId,
        claimDetailRecordDTOS: [
          {
            createdByUserId: line.createdByUserId,
            modifiedByUserId: line.modifiedByUserId,
            claimDetailRecordId: line.detailRecordTypeId,
            detailRecordTypeCode: line.detailRecordTypeCode,
            widthInFeet: line.widthinfeet,
            widthInInches: line.widthInInches,
            lengthInFeet: line.lengthinfeet,
            lengthInInches: line.lengthInInches,
            quantity: line.qty,
            unitOfMeasure: line.uom,
            amountUsd: line.amount,
            amountForeign: line.lineAmountForeign,
            unitPriceUsd: line.price,
            unitPriceForeign: line.unitPriceForeign,
            exchangeRate: line.exchangeRate,
          },
        ],
      };
    
      if (existingDocumentIndex === -1) {
        // If documentNumber is not found, create a new document entry
        finalPayload.push({
          createdByUserId: line.createdByUserId,
          modifiedByUserId: line.modifiedByUserId,
          claimDocumentId: line.claimDocumentId,
          documentNumber: line.documentNumber,
          exchangeRate: line.exchangeRate,
          orderNumber: line.orderNumber,
          purchaseOrderNumber: line.purchaseOrderNumber,
          billofLading: line.billofLading,
          masterBillofLading: line.masterBillofLading,
          documentTypeCode: line.documentTypeCode,
          claimReasonCode: line.claimReasonCode,
          claimLineDetailDTOS: [claimLineDetail],
        });
      } else {
        // If documentNumber is found, add to the existing claimLineDetailDTOS
        finalPayload[existingDocumentIndex].claimLineDetailDTOS.push(claimLineDetail);
      }
    });
    
    console.log(finalPayload,'editpayfin');
    

  


     





    this.claimdetailService.updateLineItem(finalPayload).subscribe(
      data => {
       
        this.editLineForm.reset();  // Resets the entire form including the FormArray
    
        // Emit the updated invoice data
        this.refreshInvoiceData.emit(data);
      //  this.router.navigate(['/claim-detail',this.claimDetails.claimNumber]);
       
        // Show success toast
        this.toasterService.showToast('success', 'Line Items Are Updated Successfully');
      },
      error => {
        // Handle error here
        console.error('Error updating line items:', error);
    
        // Show error toast or handle error accordingly
        this.toasterService.showToast('error', 'Failed to update line items.');
      }
    );
    
  
  }
  
  // onDropdownClick(): void {
  //   this.selectedValue = ; // Reset selectedValue to null when dropdown is clicked
  //   console.log('..............ghh',this.selectedValue)
  // }
  onSelectChange(){
    if(this.selectedValue != ''){
      const index = this.selectedValue.indexOf('-');
      this.selectedValue = index !== -1 ? this.selectedValue.substring(0, index) : this.selectedValue;
      // this.selectedValue= 'aaaaaaaaaaaaaaaa';
      console.log('..............ghh',this.selectedValue)
    }
    // let data = this.selectedValue.split('-')
    // this.selectedValue = this.selectedValue.split('-')[1].trim()
  }

  public validateSelection(event:any){
 
 
    const validateReasonPayload= 
      {
        "reasonCode":event.value.claimReasonCode,
        "reasonId":event.value.claimReasonId,
        "claimId":this.selectedLine[0].claimLineId,
        "ruleLevel":"ClaimLevel",
        "customerNumber":this.claimDetails.customerNumber
        }
        this.claimService.validateSmartCodeRules(validateReasonPayload).subscribe(
          (response) => {
            this.codeSearch = '';
            console.log(response, 'validateReasonData');
            if (response.status === 200) {
             if(response.body.errorMessage){
            
              this.toasterService.showToast('error', response.body.errorMessage);
            }
            }
          },
          (error) => {
         
            this.codeSearch = '';

            this.snackBar.open(error.error.detail, 'Close', {
              duration: 3000,
              verticalPosition: 'top',
              panelClass: ['custom-snackbar'],
            });
          }
        );
         
   
  
  
  }



// updateAmount() {
//   // Check if the amount was manually edited
//   if (!this.amountEdited) {
//     const qty = this.editLineForm.get('qty')?.value || 0;
//     const price = this.editLineForm.get('price')?.value || 0;
//     const amount = qty * price;
//     this.editLineForm.get('amount')?.setValue(amount);
//   }
// }
updateAmount(i: number) { 
  // Get the specific line form group using the index
  const lineGroup = this.editLineForm.get('lines') as FormArray;
  const line = lineGroup.at(i); // Get the form group for the specific line

  if (line && !this.amountEdited) {
    const qty = line.get('qty')?.value || 0;
    const price = line.get('price')?.value || 0;
    const amount = ((qty * price).toFixed(2)); 
    line.get('amount')?.setValue(amount); // Update the amount
  }
}

// Call this method when the amount field is manually edited
// onAmountChange() {
//   this.amountEdited = true;
// }
onAmountChange(i:number) {
  this.amountEdited = true;
  const lineGroup = this.editLineForm.get('lines') as FormArray;
  const line = lineGroup.at(i); 
  const amount = line.get('amount');
 
  if (amount) {
    const value = amount.value || '';
    const formattedValue = this.twoDecimalPlaces(value);
    amount.setValue(formattedValue); // 
  }
 
}
twoDecimalPlaces(value: string): string {
  const decimalRegex = /^\d+(\.\d{0,2})?$/; // Matches values with up to two decimals
 
  if (!decimalRegex.test(value)) {
    // Parse and trim to exactly two decimal places without rounding up
    const parts = value.split(".");
    if (parts[1]?.length > 2) {
      value = `${parts[0]}.${parts[1].substring(0, 2)}`;
    }
  }
  return value;
}
// Call this method when the qty or price fields are changed
// onQtyOrPriceChange() {
//   this.amountEdited = false;  // Reset the manual edit flag
//   this.updateAmount();        // Recalculate the amount
// }

// onQtyOrPriceChange(i: number) {
//   this.amountEdited = false;  // Reset the manual edit flag
//   this.updateAmount(i);       // Recalculate the amount for this line
// }

onQtyOrPriceChange(i: number) {
  this.amountEdited = false;  
  const lineGroup = this.editLineForm.get('lines') as FormArray;
  const line = lineGroup.at(i);
 
  if (line) {
    const qtyControl = line.get('qty');
    const priceControl = line.get('price');
 
    //  decimal places for qty
    if (qtyControl) {
      let qtyValue = qtyControl.value || '';
      qtyValue = this.twoDecimalPlaces(qtyValue);
      qtyControl.setValue(qtyValue);
    }
 
    //  decimal places for price
    if (priceControl) {
      let priceValue = priceControl.value || '';
      priceValue = this.twoDecimalPlaces(priceValue);
      priceControl.setValue(priceValue);
    }
  }
 
  
  this.updateAmount(i);     
}

  getLineDetailUsd(detailTypeCode: string): { lineAmountUsd: number, claimLineId: string } | undefined {
    for (let doc of this.claimDetails.claimDocuments) {
        for (let lineDetail of doc.claimLineDetails) {
            if (lineDetail.detailType.detailTypeCode === detailTypeCode) {
                return {
                    lineAmountUsd: lineDetail.lineAmountUsd,
                    claimLineId: lineDetail.claimLineId
                };
            }
        }
    }
    return undefined;
}

selectCheckBox(isChecked:boolean){
this.addeditLinebtn = isChecked ? false:true;
}
}
