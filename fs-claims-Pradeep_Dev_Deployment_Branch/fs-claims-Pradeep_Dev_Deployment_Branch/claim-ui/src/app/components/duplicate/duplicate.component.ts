import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';

@Component({
  selector: 'app-duplicate',
  templateUrl: './duplicate.component.html',
  styleUrls: ['./duplicate.component.scss']
})
export class DuplicateComponent {
  @Input() selectedLineItem:any = [];
  @Input() selectedLine: any=[];
  @Input() payloadResponse:any;
  
  @Output() refreshInvoiceData = new EventEmitter<string>();
  @Output() duplicateEvent = new EventEmitter<string>();
  @Input() claimList: any = [];
  @Input() unitOfmeasure: any = [];
  @Input() claimDetails: any=[];
  public dupLineForm!: FormGroup;
  filteredClaimList: any[] = [];
  selectedReasonCode: string = '';
  codeSearch: string = '';
  constructor(
    private claimDetailService : ClaimDetailService,
    private form: FormBuilder,
    private toasterService: ToasterService
  ){
    this.dupLineForm = this.form.group({
      inv: [''],
      status: [''],
      claimCat: [''],
      reasonCode: [''],
      style: [''],
      color: [''],
      roll: [''],
      lot: [''],
      lineType: [''],
      grade: [''],
      widthinfeet: [''],
      lengthinfeet: [''],
      widthininch: [''],
      lengthininch: [''],
      dyeLot:[''],
      qty: [''],
      uom: [''],
      price: [''],
      amount: [''] 
    }); 
    this.dupLineForm.valueChanges.subscribe(() => {
      this.updateAmount();
    });
  }

  ngOnInit() {
    console.log(this.selectedLineItem,'selectedLineItemcccc')
    console.log(this.payloadResponse);
    console.log('dataaaaaaaaaa',this.selectedLine)

   
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

  public closeDuplicateLineModal() {
    const modelDiv = document.getElementById('duplicateLineModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }
 
  loadForm(){
 
   
   
    let lineItem = this.selectedLine[0].selectedLine;
    this.dupLineForm.patchValue({
     inv: this.selectedLine[0].claimNumber,
     status: lineItem.detailStatusType.detailStatusTypeDescription,
     claimCat:lineItem.detailType.detailTypeCode,
     style: lineItem.styleNumber,
     color: lineItem.colorNumber,
     roll: lineItem.rollNumber.replace(/\s/g, ''),
   
     grade: lineItem.grade,
     reasonCode: lineItem.claimReasonDefinition.claimReasonCode,
    // reasonCode: lineItem.claimReasonDefinition.claimReasonCode + '-' + lineItem.claimReasonDefinition.claimReasonDescription,
     widthinfeet: lineItem.claimDetailRecords[0]?.widthInFeet|| 0,
     lengthinfeet: lineItem.claimDetailRecords[0]?.lengthInFeet || 0,
    // widthininch: lineItem.claimDetailRecords[0]?.widthInInches || 0,
     //lengthininch: lineItem.claimDetailRecords[0]?.lengthInInches|| 0,
     dyeLot: lineItem.dyeLot,
     qty: lineItem.quantity,
     uom: lineItem.unitOfMeasure.unitOfMeasureCode,
     price: lineItem.unitPriceUsd,
     amount: lineItem.lineAmountUsd 
    });
    // console.log("Form => "+this.editLineForm);
  }
  duplicateLine(event:Event){
    event.stopPropagation();
    console.log(this.dupLineForm.get('reasonCode')?.value,'code')
    const value = this.dupLineForm.get('reasonCode')?.value;
const extractedValue = value ? value.split('-')[0] : '';
console.log(extractedValue, 'code');
    // this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].unitPriceUsd =  this.dupLineForm.get('price')?.value;
    this.payloadResponse.claimLineDetailDTOS[0].lineSourceCode = "DUPL";
    this.payloadResponse.claimLineDetailDTOS[0].styleNumber =  this.dupLineForm.get('style')?.value;
    this.payloadResponse.claimLineDetailDTOS[0].colorNumber =  this.dupLineForm.get('color')?.value;
    this.payloadResponse.claimLineDetailDTOS[0].rollNumber =  this.dupLineForm.get('roll')?.value;
    this.payloadResponse.claimLineDetailDTOS[0].dyeLot =  this.dupLineForm.get('dyeLot')?.value;
    this.payloadResponse.claimLineDetailDTOS[0].grade =  this.dupLineForm.get('grade')?.value;
    this.payloadResponse.claimLineDetailDTOS[0].claimReasonCode =  extractedValue;//this.dupLineForm.get('reasonCode')?.value;
    this.payloadResponse.claimLineDetailDTOS[0].unitOfMeasureCode =  this.dupLineForm.get('uom')?.value;
    if(this.payloadResponse.claimLineDetailDTOS &&
      this.payloadResponse.claimLineDetailDTOS.length > 0 &&
      this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS &&
      this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS.length > 0){
    this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].widthInFeet =  Number(this.dupLineForm.get('widthinfeet')?.value);
    this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].lengthInFeet =  Number(this.dupLineForm.get('lengthinfeet')?.value);
    this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].quantity =   Number(this.dupLineForm.get('qty')?.value);
    this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].unitOfMeasure =   this.dupLineForm.get('uom')?.value;
    this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].amountUsd =   Number(this.dupLineForm.get('amount')?.value);
    this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].amountForeign =   Number(this.dupLineForm.get('amount')?.value);
    this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].unitPriceUsd =  Number(this.dupLineForm.get('price')?.value);
    this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].unitPriceForeign =   Number(this.dupLineForm.get('price')?.value);
    
    this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].widthInFeet =  Number(this.dupLineForm.get('widthinfeet')?.value);
    this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].lengthInFeet =  Number(this.dupLineForm.get('lengthinfeet')?.value);
    this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].quantity =   Number(this.dupLineForm.get('qty')?.value);
    this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].unitOfMeasure =   this.dupLineForm.get('uom')?.value;
    this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].amountUsd =   Number(this.dupLineForm.get('amount')?.value);
    this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].amountForeign =   Number(this.dupLineForm.get('amount')?.value);
    this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].unitPriceUsd =  Number(this.dupLineForm.get('price')?.value);
    this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].unitPriceForeign =   Number(this.dupLineForm.get('price')?.value);
    
   // this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].widthInInches =  this.dupLineForm.get('widthininch')?.value;
    //this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].lengthInInches =  this.dupLineForm.get('lengthininch')?.value;
    }
    this.payloadResponse.claimLineDetailDTOS[0].quantity =  Number(this.dupLineForm.get('qty')?.value);
    this.payloadResponse.claimLineDetailDTOS[0].lineAmountUsd =  Number(this.dupLineForm.get('amount')?.value);
    this.payloadResponse.claimLineDetailDTOS[0].unitPriceUsd = Number(this.dupLineForm.get('price')?.value);
    
    console.log(this.payloadResponse,'payloadResponseDup')
    this.claimDetailService.duplicateLineItem(this.payloadResponse).subscribe(
      (data) => {
        this.codeSearch ='';
        this.closeDuplicateLineModal();
        this.dupLineForm.reset();
      this.refreshInvoiceData.emit(data);
        this.duplicateEvent.emit();
        this.toasterService.showToast('success', 'Line item duplicated succesfully');

      },
      (error) => {
        this.closeDuplicateLineModal();
        this.dupLineForm.reset();
        this.toasterService.showToast('error', 'Failed to duplicate Line item');

      }
    )
    this.dupLineForm.reset();
  }

  updateAmount() {
    const qty = this.dupLineForm.get('qty')?.value || 0;
    const price = this.dupLineForm.get('price')?.value || 0;
    const amount = qty * price;
    this.dupLineForm.get('amount')?.setValue(amount, { emitEvent: false });
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
        this.claimDetailService.validateSmartCodeRules(validateReasonPayload).subscribe(
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