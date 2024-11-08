import { Component, ElementRef, EventEmitter, Input, OnInit, Output, SimpleChanges, ViewChildren } from '@angular/core';
import { FormBuilder, FormControlName, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable, debounceTime, fromEvent, merge } from 'rxjs';
import { ClimDetailsResponce } from 'src/app/interfaces/claim-details.model';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';
import { DashboardService } from 'src/app/ngrx/store/services/dashboard.service';
import { InitiateClaimService } from 'src/app/ngrx/store/services/initiate-claim.service';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
import { GenericValidator } from 'src/app/utils/generic-validator';
import { ToasterComponent } from '../../toaster/toaster.component';


@Component({
  selector: 'app-claim-customer-details',
  templateUrl: './claim-customer-details.component.html',
  styleUrls: ['./claim-customer-details.component.scss']
})
export class ClaimCustomerDetailsComponent implements OnInit{

  @Input() claimDetails!: ClimDetailsResponce;
  @Input() claimIsVoid:any = false;
  public getCustomerNumber: any;
  public isSearchIconClicked = false;
  public showCustomerNotFoundError = false;
  public claimForm!: FormGroup;
  public customerData: any = [];
  @ViewChildren(FormControlName, { read: ElementRef }) // Use with the generic validation message class
  public formInputElements!: ElementRef[];
  private genericValidator: GenericValidator;
  public displayMessage: { [key: string]: string } = {};
  public customerDetails: any = [];
  public disableUpdate: boolean = true;
  public userId!: number;
  public storeCustomerNumber:any = [];
  public stickNoteStoreData: any = [];
  public isCustomerInWatchlist: boolean = false;
  @Output() customerDetailsData = new EventEmitter<string>();
  @Output() updatingCustomerDetailsData = new EventEmitter<string>();
  public territoryManagerData: any;
  public regionData: any;
  public divisoinData: any;


  constructor(
    private form: FormBuilder,
    private initialClaimService: InitiateClaimService,
    private claimService: ClaimDetailService,
    private snackBar: MatSnackBar,
    private toasterService: ToasterService,
    private dashBoardService:DashboardService
  ){
    this.genericValidator = new GenericValidator();
  }

  ngOnInit(): void {
    this.claimForm = this.form.group({
      customer: ['', [Validators.maxLength(7)]]
    });
    // this.getCustomerDetailsById();
   
  }

   // track On Update the parent input value
   ngOnChanges(changes: SimpleChanges) {
    // console.log('Changes' +JSON.stringify(changes))
    if(this.claimDetails.customerNumber != undefined){
      if(this.customerDetails.length == 0){
        this.getCustomerDetailsById();
      }
      if(this.storeCustomerNumber.length == 0){
        this.getClaimAssignmentData(this.claimDetails.customerNumber);
      }
      // this.getCustomerDetailsById();
      // this.getClaimAssignmentData(this.claimDetails.customerNumber);
      this.getCustomerWatchListStatus();
      this.stickNote();
      this.territoryManagerDetails();
    }
   
  }

  public getCustomerDetailsById(){
    this.initialClaimService.getCustomerDetails(this.claimDetails.customerNumber).subscribe(
      (data) => {
        this.customerDetails = data;
        this.customerDetails = this.customerDetails[0];
        this.closeCustmoerModel();
        this.customerDetailsData.emit(this.customerDetails);
        // console.log(  '+' + this.customerDetails.phoneCountryCode + ' (' + this.customerDetails.phoneAreaCode + ') ' + this.customerDetails.phoneNumber ,'--------------------check data')
   
      
        

    });
  }

  ngAfterViewInit(): void {
    this.forValidatorMessageProcess();
    // this.getCustomerDetailsById();
  }

  public forValidatorMessageProcess() {
    //Watch for the blur event from any input element on the form.
    const controlBlurs: Observable<any>[] = this.formInputElements.map(
      (formControl: ElementRef) => fromEvent(formControl.nativeElement, 'blur')
    );

    // Merge the blur event observable with the valueChanges observable parent form
    merge(this.claimForm.valueChanges, ...controlBlurs)
      .pipe(debounceTime(100))
      .subscribe((value) => {
        this.displayMessage = this.genericValidator.processMessages(
          this.claimForm
        );
      });
    }

  public prefixWithZeros(customerNumber: string): string {
    if (customerNumber.length < 7) {
      const numberOfZerosToAdd = 7 - customerNumber.length;
      const prefixedNumber = '0'.repeat(numberOfZerosToAdd) + customerNumber;
      return prefixedNumber;
    } else {
      return customerNumber;
    }
  }

  public getCustomerDetails(CustomerNumber: string) {
    this.getCustomerNumber = CustomerNumber;
   
    if (this.isSearchIconClicked) { // Check the flag
      CustomerNumber = this.prefixWithZeros(CustomerNumber);
      this.showCustomerNotFoundError = false;
    
      // const currentCustomerValue = this.claimForm?.get('customer')?.value;
      this.setCustomerNumber(CustomerNumber);
    
     
      this.getCustomerData(CustomerNumber);
    }
    this.isSearchIconClicked= false;
  }

  public setCustomerNumber(customerNumber: String) {
    this.showCustomerNotFoundError = false;
    if (customerNumber.length > 0) {
      this.customerData = [];
      this.customerData = customerNumber;
      this.claimForm.patchValue({
        customer: this.customerData,
      });
    } else {
      //this.displayCustomerError=true
      // this.displayMessageRec.customer="Customer can not be empty"
    }
  }

  public getCustomerData(CustomerNumber: any) {
    
    this.initialClaimService.getCustomerDetails(CustomerNumber).subscribe(
      (data) => {
          this.customerData = data;
          this.customerData = this.customerData[0];
          this.disableUpdate = false;
           },
      (error) => {
          this.customerData = error;
          if (error.status === 404) {
              this.showCustomerNotFoundError = true;
          }
      }
  );
 }

  public closeCustmoerModel(){
    this.customerData = [];
    this.claimForm.patchValue({
      customer: ''
    });
    const modelDiv = document.getElementById('changeCustomerModal');
    const bodyTag = document.body;
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
  bodyTag.style.removeProperty('overflow');
    }
    this.disableUpdate = true;
  }

  public updateCustomerNum(){
    // let id= parseInt(this.claimDetails.claimNumber);
    this.claimService.updateCustomerNumber(this.claimDetails.claimId, this.claimForm.value.customer).
      subscribe( data => {
        // Toster message //
        // this.snackBar.open('Customer Number Updated sucessfully', 'Close', {
        //           duration: 3000,
        //           verticalPosition: 'top',
        //           panelClass: ['custom-snackbar'], 
        //         });
        this.toasterService.showToast('success', 'Customer Number Updated sucessfully');
                this.closechangeCustomerModal();
        this.getCustomerData(this.claimForm.value.customer);
        this.claimDetails.customerNumber = this.claimForm.value.customer;
        this.getCustomerDetailsById();
        this.updatingCustomerDetailsData.emit()
        // this.closeCustmoerModel();
      },
      error => {
        // Toster 
      }
    );
  }

  public openchangeCustomerModal(){
    if(!this.claimIsVoid){
      const modelDiv = document.getElementById('changeCustomerModal');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
    }
   
  }

  public closechangeCustomerModal(){
    const modelDiv = document.getElementById('changeCustomerModal');
      if (modelDiv != null) {
        modelDiv.style.display = 'none';
      }
  }

  public getClaimAssignmentData(customerNumber:string){
    this.initialClaimService.getClaimAssignmentData(customerNumber).subscribe(data=>{
   this.storeCustomerNumber = data.body;
    })
  }


  public customerWatchlistAddAndRemove() {
  if(!this.claimIsVoid){
    if (this.isCustomerInWatchlist) {
        // If customer is already in watchlist, remove from watchlist
        this.RemoveCustomerWatchlist();
    } else {
        // If customer is not in watchlist, add to watchlist
        this.customerWatchlistAdd();
    }
  }
}

public customerWatchlistAdd() {
    this.userId = Number(localStorage.getItem('userId'));
    const customewatchListPayload = {
        "createdByUserId":  this.userId,
        "modifiedByUserId":  this.userId,
        "customerWatchlistId": 0,
        "customerNumber": this.claimDetails.customerNumber,
        "statusId": 1
    }
    this.dashBoardService.addorUpdateCustomerWatchlist(customewatchListPayload).subscribe(dat => {
        // Update the watchlist state
        this.isCustomerInWatchlist = true;
        // this.snackBar.open('Customer Watch List added successfully', 'Close', {
        //     duration: 3000,
        //     verticalPosition: 'top',
        //     panelClass: ['custom-snackbar'],
        // });
        this.toasterService.showToast('success', 'Customer Watch List added successfully');
    })
}

public RemoveCustomerWatchlist() {
    const userId = Number(localStorage.getItem('userId'));
    this.dashBoardService.deleteCustomerWatchList(userId, this.claimDetails.customerNumber ).subscribe(() => {
        // Update the watchlist state
        this.isCustomerInWatchlist = false;
        // this.snackBar.open('Customer Removed from Watchlist Successfully', 'Close', {
        //     duration: 3000,
        //     verticalPosition: 'top',
        //     panelClass: ['custom-snackbar'],
        // });
        this.toasterService.showToast('success', 'Customer Removed from Watchlist Successfully');
        
    }, error => {
        // Handle error
    });
}

public getCustomerWatchListStatus(){
  const userId = Number(localStorage.getItem('userId'));
  this.dashBoardService.getCustomerWatchListStatus(userId,this.claimDetails.customerNumber).subscribe( data=>{
    if(data == 1){
      this.isCustomerInWatchlist = true;
    }else{
      this.isCustomerInWatchlist = false;
    }
  })
}

public stickNote(){
  this.initialClaimService.getCustomerCreditClaimsData(this.claimDetails.customerNumber).subscribe(data=>{
    this.stickNoteStoreData = data.body
  })
}

public openStickNoteModal(){
  const modelDiv = document.getElementById('sticknote');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
}

public closeModal(id:string){
  const modelDiv = document.getElementById(id);
      if (modelDiv != null) {
        modelDiv.style.display = 'none';
      }
}

public territoryManagerDetails(){
  this.initialClaimService.getTerritoryDivisionRegion('0082').subscribe((data:any)=>{
    this.territoryManagerData = data.body.filter((ele:any)=>ele.salesResult == 'Territory')[0]
    this.regionData = data.body.filter((ele:any)=>ele.salesResult == 'Region')[0]
    this.divisoinData = data.body.filter((ele:any)=>ele.salesResult == 'Division')[0]

  })
}
}
