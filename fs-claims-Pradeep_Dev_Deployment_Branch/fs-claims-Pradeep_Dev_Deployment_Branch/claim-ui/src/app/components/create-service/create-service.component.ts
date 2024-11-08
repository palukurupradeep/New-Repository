import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';

@Component({
  selector: 'app-create-service',
  templateUrl: './create-service.component.html',
  styleUrls: ['./create-service.component.scss']
})
export class CreateServiceComponent {
  @Input() selectedLineItem: any;
  @Input() payloadResponse:any;
  @Input() selectedLine: any=[];

  enableBtn: boolean = false;
  public serviceForm!: FormGroup;
  @Input() claimList: any = [];
  @Input() unitOfmeasure: any = [];
   @Output() refreshInvoiceData = new EventEmitter<string>();
   @Input() claimDetails: any=[];

   filteredClaimList: any[] = [];
   selectedReasonCode: string = '';
   codeSearch: string = '';
  constructor(
    private claimDetailService : ClaimDetailService,
    private form: FormBuilder,
    private toasterService: ToasterService
  ){
    this.serviceForm = this.form.group({
      //inv: [''],
      lineNumber:[''],
      invLineNumber:[''],
      status: [''],
      claimCat: [''],
      reasonCode: [''],
      style: [''],
      color: [''],
      roll: [''],
      // lot: [''],
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
    // this.serviceForm.valueChanges.subscribe(() => {
    //   // this.updateAmount();
    // });
  }

  ngOnInit() {
    console.log("create service is called")
   
    //console.log(this.payloadResponse);
  
    console.log(this.serviceForm.value,'serviceFormform');
   // this.loadForm()
    console.log('claimList:', this.claimList);
    
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
   // console.log(this.selectedLineItem,'this.selectedLineItemforservice')
    if (this.selectedLineItem.length !== 0) {
      // this.selectedLine = this.selectedLine[0].selectedLine;
      this.loadForm();
      // this.isHighlighted = false; // Reset the highlighting when the selected line changes

      
    }
  }
  loadForm(){
    console.log('...........loadformcalled')
    console.log('....selectedLine.......',this.selectedLine);
    console.log('.....serviceFormselectedLineItem......',this.selectedLineItem);
    let lineItem = this.selectedLineItem;
    console.log(lineItem,'servicelineItem')
    this.serviceForm.patchValue({
    // inv:123, //this.selectedLineItem[0].claimNumber,
    lineNumber:lineItem.lineNumber,
    invLineNumber:lineItem.invoiceLineNumber,
     status: lineItem.detailStatusType.detailStatusTypeDescription,
     claimCat:lineItem.detailType.detailTypeCode,
     reasonCode: lineItem.claimReasonDefinition.claimReasonCode,
     //reasonCode: lineItem.claimReasonDefinition.claimReasonCode + '-' + lineItem.claimReasonDefinition.claimReasonDescription,
     style: lineItem.styleNumber,
     color: lineItem.colorNumber,
     roll: lineItem.rollNumber.replace(/\s/g, ''),
     grade: lineItem.grade,
     dyeLot:lineItem.dyeLot,
    widthinfeet: lineItem.claimDetailRecords[0]?.widthInFeet||0,
     lengthinfeet: lineItem.claimDetailRecords[0]?.lengthInFeet||0,
   //  widthininch: lineItem.claimDetailRecords[0].widthInInches,
    // lengthininch: lineItem.claimDetailRecords[0].lengthInInches,
     qty: lineItem.quantity,
     uom: lineItem.unitOfMeasure.unitOfMeasureCode,
     price: lineItem.unitPriceUsd,
     amount: 0//lineItem.lineAmountUsd
    });
    console.log(this.serviceForm.value,'formData Values for serviceForm');
  
  }
  public closeCreateServiceModal() {
    const modelDiv = document.getElementById('createServiceModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
    this.serviceForm.reset();
  }

  public createService(){
    const servicePayload = JSON.parse(JSON.stringify(this.payloadResponse));
  
    // Remove claimLineId from the labor line payload
    if (servicePayload.claimLineDetailDTOS && servicePayload.claimLineDetailDTOS[0]) {
      delete servicePayload.claimLineDetailDTOS[0].claimLineId;
    }
    console.log(this.serviceForm.get('reasonCode')?.value,'code')
    const value = this.serviceForm.get('reasonCode')?.value;
const extractedValue = value ? value.split('-')[0] : '';
console.log(extractedValue, 'code');
//servicePayload.claimLineDetailDTOS[0].lineNumber =   this.serviceForm.get('lineNumber')?.value ? '00' + this.serviceForm.get('lineNumber')?.value : '';
//servicePayload.claimLineDetailDTOS[0].invoiceLineNumber =  this.serviceForm.get('invLineNumber')?.value ? '00' + this.serviceForm.get('invLineNumber')?.value : '';
    servicePayload.claimLineDetailDTOS[0].styleNumber = this.serviceForm.get('style')?.value;
    servicePayload.claimLineDetailDTOS[0].colorNumber = this.serviceForm.get('color')?.value;
    servicePayload.claimLineDetailDTOS[0].rollNumber = this.serviceForm.get('roll')?.value;
    servicePayload.claimLineDetailDTOS[0].grade = this.serviceForm.get('grade')?.value;
    servicePayload.claimLineDetailDTOS[0].dyeLot = this.serviceForm.get('dyeLot')?.value;
    servicePayload.claimLineDetailDTOS[0].claimReasonCode =  extractedValue;//this.serviceForm.get('reasonCode')?.value;
    servicePayload.claimLineDetailDTOS[0].unitOfMeasureCode =  this.serviceForm.get('uom')?.value;
    if(servicePayload.claimLineDetailDTOS &&
      servicePayload.claimLineDetailDTOS.length > 0 &&
      servicePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS &&
      servicePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS.length > 0){
        servicePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].widthInFeet =  Number(this.serviceForm.get('widthinfeet')?.value);
        servicePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].lengthInFeet =  Number(this.serviceForm.get('lengthinfeet')?.value);
        servicePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].quantity =   Number(this.serviceForm.get('qty')?.value);
        servicePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].unitOfMeasure =   this.serviceForm.get('uom')?.value;
        servicePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].amountUsd =   Number(this.serviceForm.get('amount')?.value);
        servicePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].amountForeign =   Number(this.serviceForm.get('amount')?.value);
        servicePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].unitPriceUsd =  Number(this.serviceForm.get('price')?.value);
        servicePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].unitPriceForeign =   Number(this.serviceForm.get('price')?.value);
        
        servicePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].widthInFeet =  Number(this.serviceForm.get('widthinfeet')?.value);
        servicePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].lengthInFeet =  Number(this.serviceForm.get('lengthinfeet')?.value);
        servicePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].quantity =   Number(this.serviceForm.get('qty')?.value);
        servicePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].unitOfMeasure =   this.serviceForm.get('uom')?.value;
        servicePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].amountUsd =   Number(this.serviceForm.get('amount')?.value);
        servicePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].amountForeign =   Number(this.serviceForm.get('amount')?.value);
        servicePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].unitPriceUsd =  Number(this.serviceForm.get('price')?.value);
        servicePayload.claimLineDetailDTOS[0].claimDetailRecordDTOS[1].unitPriceForeign =   Number(this.serviceForm.get('price')?.value);
        
       // this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].widthInInches =  this.dupLineForm.get('widthininch')?.value;
        //this.payloadResponse.claimLineDetailDTOS[0].claimDetailRecordDTOS[0].lengthInInches =  this.dupLineForm.get('lengthininch')?.value;
        }
      servicePayload.claimLineDetailDTOS[0].quantity =  Number(this.serviceForm.get('qty')?.value);
        servicePayload.claimLineDetailDTOS[0].lineAmountUsd =  Number(this.serviceForm.get('amount')?.value);
      servicePayload.claimLineDetailDTOS[0].unitPriceUsd = Number(this.serviceForm.get('price')?.value);
        
    console.log(servicePayload,'servicePayload');
    this.claimDetailService.createService(servicePayload).subscribe(
      (data) => {
        this.codeSearch ='';
     this.refreshInvoiceData.emit()
        this.toasterService.showToast('success', 'Service created succesfully');
        this.closeCreateServiceModal();
      },
      (error) => {
        this.toasterService.showToast('success', 'Failed to create service');
        this.closeCreateServiceModal();
      }
    )
  }

  public enableCreateServiceBtn($event: any) {
      this.enableBtn = $event.target.checked ? true : false;
  }
  
  // updateAmount() {
  //   const qty = this.serviceForm.get('qty')?.value || 0;
  //   const price = this.serviceForm.get('price')?.value || 0;
  //   const amount = qty * price;
  //   this.serviceForm.get('amount')?.setValue(amount, { emitEvent: false });
  // }

  public amountEdited: boolean = false;

  updateAmount() {
    // Check if the amount was manually edited
    if (!this.amountEdited) {
      const qty = this.serviceForm.get('qty')?.value || 0;
      const price = this.serviceForm.get('price')?.value || 0;
      const amount = qty * price;
      this.serviceForm.get('amount')?.setValue(amount);
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
