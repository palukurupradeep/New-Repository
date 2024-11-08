import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';

@Component({
  selector: 'app-create-line',
  templateUrl: './create-line.component.html',
  styleUrls: ['./create-line.component.scss']
})
export class CreateLineComponent implements OnChanges{

  @Input() payloadResponse:any;
  @Input() selectedLine: any=[];
  @Output() refreshInvoiceData = new EventEmitter<string>();
  public labourLineForm!: FormGroup;
  public isHighlighted: boolean = false;
  @Input() isLaborLineCreated: boolean = false;
  @Input() claimList: any = [];
  @Input() unitOfmeasure: any = [];
  @Input() claimDetails: any=[];

  filteredClaimList: any[] = [];
  selectedReasonCode: string = '';
  codeSearch: string = '';
  constructor(
    private form: FormBuilder,
    private snackBar: MatSnackBar,
    private claimdetailService:ClaimDetailService,
    private toasterService: ToasterService
   ){
     
   }
 
   ngOnInit(){
     this.labourLineForm = this.form.group({
       inv: [''],
       lineNumber:[''],
      invLineNumber:[''],
       status: [''],
       claimCat: [''],
       reasonCode: [''],
       style: [''],
       color: [''],
       roll: [''],
       lot: [''],
       lineType: [''],
       grade: [''],
       dyeLot:[''],
       widthinfeet: [''],
       lengthinfeet: [''],
       widthininch: [''],
       lengthininch: [''],
       qty: [''],
       uom: [''],
       price: [''],
       amount: [''] 
     });
    //  this.labourLineForm.valueChanges.subscribe(() => {
    //   // this.updateAmount();
    // });
	
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
      // this.isHighlighted = false; // Reset the highlighting when the selected line changes

      
    }
  }

  loadForm(){
    let lineItem = this.selectedLine[0].selectedLine;
    this.labourLineForm.patchValue({
     inv: this.selectedLine[0].claimNumber,
     lineNumber:lineItem.lineNumber,
     invLineNumber:lineItem.invoiceLineNumber,
     status: lineItem.detailStatusType.detailStatusTypeDescription,
     claimCat:lineItem.detailType.detailTypeCode,
     style: lineItem.styleNumber,
     color: lineItem.colorNumber,
    // roll: lineItem.rollNumber,
    roll: lineItem.rollNumber.replace(/\s/g, ''),
     grade: lineItem.grade,
     dyeLot:lineItem.dyeLot,
     reasonCode: lineItem.claimReasonDefinition.claimReasonCode,
     //reasonCode: lineItem.claimReasonDefinition.claimReasonCode + '-' + lineItem.claimReasonDefinition.claimReasonDescription,
     widthinfeet: lineItem.claimDetailRecords[0]?.widthInFeet || 0,
     lengthinfeet: lineItem.claimDetailRecords[0]?.lengthInFeet || 0,
    // widthininch: lineItem.claimDetailRecords[0].widthInInches,
    // lengthininch: lineItem.claimDetailRecords[0].lengthInInches,
     qty: lineItem.quantity,
     uom: lineItem.unitOfMeasure.unitOfMeasureCode,
     price: lineItem.unitPriceUsd,
     amount: 0//lineItem.lineAmountUsd
    });
    console.log(this.labourLineForm.value,'formData Values for labourLineForm');
    console.log('.......for labor....',this.selectedLine)
  }

  updateLabourData() {
    // Assuming that payloadResponse is a deep clone of the original data
    const laborLinePayload = JSON.parse(JSON.stringify(this.payloadResponse));
    console.log(laborLinePayload,'laborLinePayload')
  
    // Remove claimLineId from the labor line payload
    if (laborLinePayload.claimLineDetailDTOS && laborLinePayload.claimLineDetailDTOS[0]) {
      delete laborLinePayload.claimLineDetailDTOS[0].claimLineId;
    }
    console.log(this.labourLineForm.get('reasonCode')?.value,'code')
    const value = this.labourLineForm.get('reasonCode')?.value;
const extractedValue = value ? value.split('-')[0] : '';
console.log(extractedValue, 'code');
    // Update other properties based on the form values
   // laborLinePayload.claimLineDetailDTOS[0].lineNumber = this.labourLineForm.get('lineNumber')?.value ? '00' + this.labourLineForm.get('lineNumber')?.value : '';
   // laborLinePayload.claimLineDetailDTOS[0].invoiceLineNumber =  this.labourLineForm.get('invLineNumber')?.value ? '00' + this.labourLineForm.get('invLineNumber')?.value : '';
       laborLinePayload.claimLineDetailDTOS[0].styleNumber = this.labourLineForm.get('style')?.value;
    laborLinePayload.claimLineDetailDTOS[0].colorNumber = this.labourLineForm.get('color')?.value;
    laborLinePayload.claimLineDetailDTOS[0].rollNumber = this.labourLineForm.get('roll')?.value;
    laborLinePayload.claimLineDetailDTOS[0].grade = this.labourLineForm.get('grade')?.value;
    laborLinePayload.claimLineDetailDTOS[0].dyeLot = this.labourLineForm.get('dyeLot')?.value;
    laborLinePayload.claimLineDetailDTOS[0].claimReasonCode =  extractedValue;//this.labourLineForm.get('reasonCode')?.value;
    laborLinePayload.claimLineDetailDTOS[0].unitOfMeasureCode =  this.labourLineForm.get('uom')?.value;
    if(laborLinePayload.claimLineDetailDTOS &&
      laborLinePayload.claimLineDetailDTOS.length > 0 &&
      laborLinePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS &&
      laborLinePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS.length > 0){
        laborLinePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].widthInFeet =  Number(this.labourLineForm.get('widthinfeet')?.value);
        laborLinePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].lengthInFeet =  Number(this.labourLineForm.get('lengthinfeet')?.value);
        laborLinePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].quantity =   Number(this.labourLineForm.get('qty')?.value);
        laborLinePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].unitOfMeasure =   this.labourLineForm.get('uom')?.value;
        laborLinePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].amountUsd =   Number(this.labourLineForm.get('amount')?.value);
        laborLinePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].amountForeign =   Number(this.labourLineForm.get('amount')?.value);
        laborLinePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].unitPriceUsd =  Number(this.labourLineForm.get('price')?.value);
        laborLinePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].unitPriceForeign =   Number(this.labourLineForm.get('price')?.value);
        
        laborLinePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].widthInFeet =  Number(this.labourLineForm.get('widthinfeet')?.value);
        laborLinePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].lengthInFeet =  Number(this.labourLineForm.get('lengthinfeet')?.value);
        laborLinePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].quantity =   Number(this.labourLineForm.get('qty')?.value);
        laborLinePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].unitOfMeasure =   this.labourLineForm.get('uom')?.value;
        laborLinePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].amountUsd =   Number(this.labourLineForm.get('amount')?.value);
        laborLinePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].amountForeign =   Number(this.labourLineForm.get('amount')?.value);
        laborLinePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].unitPriceUsd =  Number(this.labourLineForm.get('price')?.value);
        laborLinePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].unitPriceForeign =   Number(this.labourLineForm.get('price')?.value);
        
     //  this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].widthInInches =  this.dupLineForm.get('widthininch')?.value;
      //  this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].lengthInInches =  this.dupLineForm.get('lengthininch')?.value;
        }
        laborLinePayload.claimLineDetailDTOS[0].quantity =  Number(this.labourLineForm.get('qty')?.value);
        laborLinePayload.claimLineDetailDTOS[0].lineAmountUsd =  Number(this.labourLineForm.get('amount')?.value);
        laborLinePayload.claimLineDetailDTOS[0].unitPriceUsd = Number(this.labourLineForm.get('price')?.value);
        
  console.log(laborLinePayload,'laborLinePayload')
   
    this.claimdetailService.laborLineItem(laborLinePayload).subscribe(
      data => {
        this.codeSearch ='';
        console.log('data', data);
        this.refreshInvoiceData.emit();
        this.snackBar.open('Labour Line Created successfully', 'Close', {
          duration: 3000,
          verticalPosition: 'top',
          panelClass: ['custom-snackbar'],
        });
        this.isHighlighted = true;
      },
      error=>{
        // this.refreshInvoiceData.emit();
      }
    );
  }
  // updateAmount() {
  //   const qty = this.labourLineForm.get('qty')?.value || 0;
  //   const price = this.labourLineForm.get('price')?.value || 0;
  //   const amount = qty * price;
  //   this.labourLineForm.get('amount')?.setValue(amount, { emitEvent: false });
  // }

  public amountEdited: boolean = false;

  updateAmount() {
    // Check if the amount was manually edited
    if (!this.amountEdited) {
      const qty = this.labourLineForm.get('qty')?.value || 0;
      const price = this.labourLineForm.get('price')?.value || 0;
      const amount = qty * price;
      this.labourLineForm.get('amount')?.setValue(amount);
    }
  }
  
  // Call this method when the amount field is manually edited
  onAmountChange() {
    this.amountEdited = true;
  }
  
  // Call this method when the qty or price fields are changed
  onQtyOrPriceChange() {
    this.amountEdited = false;  // Reset the manual edit flag
    this.updateAmount();        // Recalculate the amount
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
        this.claimdetailService.validateSmartCodeRules(validateReasonPayload).subscribe(
          (response) => {
            console.log(response, 'validateReasonData');
            if (response.status === 200) {
             if(response.body.errorMessage){
            
              this.toasterService.showToast('error', response.body.errorMessage);
            }
            }
          },
          (error) => {
         
            this.toasterService.showToast('error', error.error.detail);
        
          }
        );
         
   
  
  
  }
}
